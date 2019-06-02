package com.mohe.fastdevpro.study.xposed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ThreadUtils;
import com.bumptech.glide.Glide;
import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.ui.base.BaseActivity;
import com.mohe.fastdevpro.utils.ZXingUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mohe.fastdevpro.study.xposed.UnionPayXpHelper.ACTION_CONNECT;

public class XPUnionPayActivity extends BaseActivity {

    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.et_mark)
    EditText etMark;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
    @BindView(R.id.tv_qr_url)
    TextView tvQrUrl;

    private MyBroadcastReceiver mReceiver;

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
        getQrCode();
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
