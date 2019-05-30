package com.mohe.fastdevpro.study.xposed;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.ui.base.BaseActivity;

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
    }

    @OnClick(R.id.btn_qr_code)
    public void onViewClicked() {
        getQrCode();
    }

    public void getQrCode() {
        Intent intent = new Intent(ACTION_CONNECT);
        String mount = etAmount.getText().toString();
        String mark =etMark.getText().toString();
        if (!TextUtils.isEmpty(mount)) {
            intent.putExtra("money", mount);
        }
        if (!TextUtils.isEmpty(mark)) {
            intent.putExtra("mark", mark);
        }
        sendBroadcast(intent);
    }
}
