package com.mohe.fastdevpro.study.xposed;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.bumptech.glide.Glide;
import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.bean.CreateQrcodeBean;
import com.mohe.fastdevpro.study.websocket.AddReqPayQrCodeInterface;
import com.mohe.fastdevpro.study.websocket.SocketService;
import com.mohe.fastdevpro.ui.base.BaseActivity;
import com.mohe.fastdevpro.utils.GsonUtils;
import com.mohe.fastdevpro.utils.JSONUtil;
import com.mohe.fastdevpro.utils.ThreadSimpleTask;
import com.mohe.fastdevpro.utils.ZXingUtils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.extensions.IExtension;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.protocols.IProtocol;
import org.java_websocket.protocols.Protocol;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mohe.fastdevpro.study.xposed.UnionPayXpHelper.ACTION_CONNECT;

public class XPUnionPayActivity extends BaseActivity implements AddReqPayQrCodeInterface {

    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.et_mark)
    EditText etMark;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
    @BindView(R.id.tv_qr_url)
    TextView tvQrUrl;

    private MyBroadcastReceiver mReceiver;
    private SocketService socketService;
    private SocketService.SocketBinder socketBinder;
    private WebSocketClient socketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xpunion_pay);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        etAmount.setText("0.02");
        etMark.setText("哈哈哈");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UnionPayXpHelper.ACTION_NOTIFI);
        intentFilter.addAction(UnionPayXpHelper.ACTION_NOTIFI_NEW);
        this.mReceiver = new MyBroadcastReceiver();
        registerReceiver(mReceiver,intentFilter);

        //绑定socketService
//        bindSocketService();
        try {
            startWebSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startWebSocket() throws Exception {
        HashMap<String,String> map=new HashMap<>();
        map.put("origin",  "");
        map.put("echo-protocol", "origin");
        List<IProtocol> list = new ArrayList<>();
        list.add(new Protocol("echo-protocol"));
        Draft_6455 draft = new Draft_6455(Collections.<IExtension>emptyList(), list);
        socketClient=new WebSocketClient(new URI("ws://" +"47.244.149.48:84/ws.ashx"+"?user=C1")) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                LogUtils.i("webSocket启动了","httpCode="+handshakedata.getHttpStatus(),handshakedata.getHttpStatusMessage());
                send("{money: \"100\", uid: \"c123\", descUser: \"001\"}");
            }

            @Override
            public void onMessage(String message) {
                LogUtils.i("服务器发起请求"+message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                LogUtils.i("webSocket close  code="+code+"  reason="+reason+"  isRemote="+remote);
            }

            @Override
            public void onError(Exception ex) {
                LogUtils.i("socketClient失败了"+ex.getMessage());
            }
        };
        socketClient.connect();

        ThreadUtils.executeByCpuWithDelay(new ThreadSimpleTask() {
            @Nullable
            @Override
            public Object doInBackground() throws Throwable {
                socketClient.send("{money: \"100\", uid: \"c123\", descUser: \"001\"}");
                return null;
            }
        },5,TimeUnit.SECONDS);

    }

    private void bindSocketService() {
        Intent intent =new Intent(this,SocketService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                socketBinder= (SocketService.SocketBinder) service;

//                ThreadUtils.executeByCpuWithDelay(new ThreadSimpleTask() {
//                    @Nullable
//                    @Override
//                    public Object doInBackground() throws Throwable {
//
//                        return null;
//                    }
//                },3, TimeUnit.SECONDS);
                socketBinder.service_connect_Activity();
                socketBinder.addReqPayQrCodeInterface(XPUnionPayActivity.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        },Context.BIND_AUTO_CREATE);
        //需要待处理，其实只要bind就启动了，
//        startService(intent);
    }

    /**
     * 服务端发送请求获取收款的二维码地址
     * @param s 服务端发送过来的金额和提示语
     */
    @Override
    public void reqQrCodeUrl(String s) {
        CreateQrcodeBean qrcodeBean= GsonUtils.fromJson(s,CreateQrcodeBean.class);
        Intent intent = new Intent(ACTION_CONNECT);
        String mount = qrcodeBean.getAmount();
        String mark = qrcodeBean.getMark();
        if (!TextUtils.isEmpty(mount)) {
            intent.putExtra("money", mount);
        }
        if (!TextUtils.isEmpty(mark)) {
            intent.putExtra("mark", mark);
        }
        sendBroadcast(intent);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        private MyBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String stringExtra;
            StringBuilder stringBuilder;
            if (UnionPayXpHelper.ACTION_NOTIFI.equals(intent.getAction())) {
                stringExtra = intent.getStringExtra("message");
                if (stringExtra != null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(stringExtra);
                    stringBuilder.append("\r\n");
                    tvQrUrl.setText(stringBuilder.toString());

                    JSONObject js=new JSONObject();
                    JSONUtil.setString(js,"reqPayUrl",stringExtra);
                    //通过webSocket服务，把请求的付款的url发送给服务端
                    socketBinder.sendReqPayUrl(js);

                    createQrCode(stringExtra);
                }
            } else if (UnionPayXpHelper.ACTION_NOTIFI.equals(intent.getAction())) {
                stringExtra = intent.getStringExtra("message");
                if (stringExtra != null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(stringExtra);
                    stringBuilder.append("\r\n");
                    tvQrUrl.setText(stringBuilder.toString());
                }
            }
        }
    }

    private void createQrCode(final String url) {
        ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<Bitmap>() {
            @Nullable
            @Override
            public Bitmap doInBackground() throws Throwable {
                return ZXingUtils.createQRcodeImage(url);
            }

            @Override
            public void onSuccess(@Nullable Bitmap result) {
                Glide.with(XPUnionPayActivity.this)
                        .load(result)
                        .into(ivQrCode);
            }
        });
    }


    @OnClick(R.id.btn_qr_code)
    public void onViewClicked() {
//        getQrCode();
        socketClient.send("{money: \"100\", uid: \"c123\", descUser: \"001\"}");
    }

    public void getQrCode() {
        Intent intent = new Intent(ACTION_CONNECT);
        String mount = etAmount.getText().toString();
        String mark = etMark.getText().toString();
        if (!TextUtils.isEmpty(mount)) {
            intent.putExtra("money", mount);
        }
        if (!TextUtils.isEmpty(mark)) {
            intent.putExtra("mark", mark);
        }
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
