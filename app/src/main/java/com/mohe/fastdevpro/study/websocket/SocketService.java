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
import com.blankj.utilcode.util.ThreadUtils;
import com.mohe.fastdevpro.utils.ThreadSimpleTask;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.extensions.IExtension;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.protocols.IProtocol;
import org.java_websocket.protocols.Protocol;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by chenxinyou on 2019/3/7.
 */

public class SocketService extends Service {

    //自己定义接口用来传参
    private static AddReqPayQrCodeInterface mInterface;

    private SocketBinder socketBinder = new SocketBinder();

    //url地址需要修改
    public static String address ="ws://" +"47.244.149.48:84/ws.ashx";

    private static Map<String, String> map = new HashMap<>();
    private static WebSocketClient mSocketClient;

    // 发送心跳包
    private static Handler mHandler = new Handler();
    private static final String TAG = "cxySocket";

    private static Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this, 15 * 1000);
            sendMsg("heat");
        }
    };

    public static String message;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            initSocketClient();
            connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

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


    public static void receiverMessage(String msg) {
        if (mInterface != null) {
            mInterface.reqQrCodeUrl(msg);
        }
    }

    public static class SocketBinder extends Binder {

        public void service_connect_Activity() {
            mHandler.post(heartBeatRunnable);
            LogUtils.i("Service关联了Activity,并在Activity执行了Service的方法");

        }

        public String getNewOrder() {
            return message;
        }

        public void getOrder(String orderId) {
            JSONObject object = new JSONObject();
            try {
                object.put("service", "RECEIVEORDER");
                object.put("orderNo", orderId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendMsg(object.toString());
        }

        /**
         * 发送请求付款的url到服务端，服务端用这个来生成二维码
         * @param js 发送到服务端的包含Url的json对象
         */
        public void sendReqPayUrl(JSONObject js){
            sendMsg(js.toString());
        }

        public void addReqPayQrCodeInterface(AddReqPayQrCodeInterface anInterface) {
            mInterface = anInterface;
        }
    }

    //断开连接
    public static void closeConnect() {
        try {
            mSocketClient.close();
        } catch (Exception e) {
            Log.e(TAG, "close connect");
            Log.e(TAG, e.toString());

            e.printStackTrace();
        } finally {
            mSocketClient = null;
        }
    }

    /**
     * 发送消息
     */
    public static void sendMsg(String msg) {

        LogUtils.i(msg);
        if (mSocketClient == null)
            return;
        try {
            mSocketClient.send(msg);
        } catch (WebsocketNotConnectedException e) {
            e.printStackTrace();
            closeConnect();
            try {
                initSocketClient();
            } catch (URISyntaxException ee) {
                ee.printStackTrace();
            }
            connect();
        }
    }


    public static void initSocketClient() throws URISyntaxException {
        map.put("echo-protocol", "origin");
//        List<IProtocol> list = new ArrayList<>();
//        list.add(new Protocol("echo-protocol"));
//        Draft_6455 draft = new Draft_6455(Collections.<IExtension>emptyList(), list);
        if (mSocketClient == null) {
            mSocketClient = new WebSocketClient(new URI(address)) {

                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    //连接成功
//                    L.e("socket连接成功");
                    Log.d(TAG, "socket 链接成功");
                }

                @Override
                public void onMessage(String s) {
                    //服务端发送消息 通过接口传给fragment

                    receiverMessage(s);
                }

                @Override
                public void onClose(int i, String s, boolean remote) {
                    //连接断开，remote判定是客户端断开还是服务端断开
//                    L.e("Connection closed by " + (remote ? "remote peer?" : "us") + ", info=" + s);
                    Log.d(TAG, "Connection closed by " + (remote ? "remote peer?" : "us") + ", info=" + s);
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, "error:" + e);
                }
            };
        }
    }

    //连接
    public static void connect() {
        ThreadUtils.executeByIo(new ThreadSimpleTask() {
            @Nullable
            @Override
            public Object doInBackground() throws Throwable {
                if (mSocketClient != null) {
                    try {
                        mSocketClient.connect();
                    } catch (IllegalStateException e) {
                        LogUtils.i(e.toString());
                    }
                }
                LogUtils.i("socket连接");
                return null;
            }
        });
    }
}
