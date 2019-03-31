package com.mohe.fastdevpro.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.bean.User;
import com.mohe.fastdevpro.ui.base.BaseActivity;
import com.mohe.fastdevpro.ui.mvp.contract.LoginContract;
import com.mohe.fastdevpro.ui.mvp.presenter.LoginPresenter;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginContract.View{

    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_identify)
    EditText etIdentify;
    @BindView(R.id.tv_send_identify)
    TextView tvSendIdentify;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    private LoginPresenter mPresenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initEvent();
    }

    private void initEvent() {
        mPresenter =new LoginPresenter();
        mPresenter.attachView(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("载入中");
    }

    @OnClick({R.id.tv_send_identify, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_send_identify:
                tvSendIdentify.setEnabled(false);
                mPresenter.getIdentify();
                break;
            case R.id.tv_login:
                mPresenter.requestLogin(etMobile.getText().toString().trim(),
                        etIdentify.getText().toString().trim());
                break;
        }
    }

    @Override
    public void loginSuccess(User user) {
        showError("登录成功");
    }

    @Override
    public void loginFail() {
        showError("登录失败");
    }

    @Override
    public void countdownComplete() {
        tvSendIdentify.setText(R.string.login_send_identify);
        tvSendIdentify.setEnabled(true);
    }

    @Override
    public void countdownNext(String time) {
        tvSendIdentify.setText(TextUtils.concat(time, "秒后重试"));
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(@NotNull String errorMsg) {
        ToastUtils.showShort(errorMsg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
