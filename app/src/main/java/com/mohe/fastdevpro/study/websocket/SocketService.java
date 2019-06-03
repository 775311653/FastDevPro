package com.mohe.fastdevpro.study.websocket;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by chenxinyou on 2019/3/7.
 */

public class SocketService extends Service {

    private static final String TAG = "SocketService";
    //url地址需要修改
    public static String address ="ws://" + "47.244.149.48:84/ws.ashx" ;
//            + "?user=C1";
    public static String message;
    //自己定义接口用来传参
    private static AddReqPayQrCodeInterface mInterface;
    // 发送心跳包
    private static Handler mHandler = new Handler();
    private static Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this, 15 * 1000);
            sendMsg("heat");
        }
    };
    private static WebSocket mWebSocket;
    private SocketBinder socketBinder = new SocketBinder();

    public static void receiverMessage(String msg) {
        if (mInterface != null) {
            mInterface.reqQrCodeUrl(msg);
        }
    }

    //断开连接
    public static void closeConnect() {
        try {
            mWebSocket.close(1000, null);
        } catch (Exception e) {
            Log.e(TAG, "close connect");
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } finally {
            mWebSocket = null;
        }
    }

    /**
     * 发送消息
     */
    public static void sendMsg(String msg) {

        LogUtils.i(msg);
        if (mWebSocket == null)
            return;
        try {
            mWebSocket.send(msg);
            LogUtils.i("webSocket send msg="+msg);
        } catch (WebsocketNotConnectedException e) {
            e.printStackTrace();
            closeConnect();
            initSocketClient();
        }
    }

    public static void initSocketClient() {

        EchoWebSocketListener listener = new EchoWebSocketListener();
        Request request = new Request.Builder()
                .url(address)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newWebSocket(request, listener);

        client.dispatcher().executorService().shutdown();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initSocketClient();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i("执行了onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("执行了onDestory()");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return socketBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.i("绑定服务");
        return super.onUnbind(intent);
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        LogUtils.i("解绑服务");
        super.unbindService(conn);
    }

    public static class SocketBinder extends Binder {

        public void service_connect_Activity() {
            mHandler.post(heartBeatRunnable);
            LogUtils.i("Service关联了Activity,并在Activity执行了Service的方法");

        }

        /**
         * 发送请求付款的url到服务端，服务端用这个来生成二维码
         *
         * @param js 发送到服务端的包含Url的json对象
         */
        public void send(JSONObject js) {
            sendMsg(js.toString());
        }

        public void addReqPayQrCodeInterface(AddReqPayQrCodeInterface anInterface) {
            mInterface = anInterface;
        }
    }

    private static final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            mWebSocket = webSocket;
            webSocket.send("welcome");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            LogUtils.i("onMessage: " + text);
            receiverMessage(text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            LogUtils.i("onMessage byteString: " + bytes);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(1000, null);
            LogUtils.i("onClosing: " + code + "/" + reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            LogUtils.i("onClosed: " + code + "/" + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            LogUtils.i("onFailure: " + t.getMessage());
        }
    }
}
