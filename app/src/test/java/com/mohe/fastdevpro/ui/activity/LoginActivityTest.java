package com.mohe.fastdevpro.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;
import com.mohe.fastdevpro.BuildConfig;
import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.rxjava.RxJavaTestSchedulerRule;
import com.mohe.fastdevpro.ui.mvp.contract.LoginContract;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowProgressDialog;
import org.robolectric.shadows.ShadowToast;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by xiePing on 2018/10/27 0027.
 * Description:
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 23)
public class LoginActivityTest {

    private LoginActivity loginActivity;
    private TextView mTvSendIdentify;
    private TextView mTvLogin;
    private EditText mEtMobile;
    private EditText mEtIdentify;

    @Rule
    public RxJavaTestSchedulerRule rule = new RxJavaTestSchedulerRule();

    @Mock
    public LoginContract.View view;

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
        Utils.init(RuntimeEnvironment.application);
        loginActivity = Robolectric.setupActivity(LoginActivity.class);
        mTvSendIdentify = (TextView) loginActivity.findViewById(R.id.tv_send_identify);
        mTvLogin = (TextView) loginActivity.findViewById(R.id.tv_login);
        mEtMobile = (EditText) loginActivity.findViewById(R.id.et_mobile);
        mEtIdentify = (EditText) loginActivity.findViewById(R.id.et_identify);

    }

    @Test
    public void testGetIdentity(){
        assertEquals(mTvSendIdentify.getText().toString(),"发送验证码");
        mTvSendIdentify.performClick();
        assertEquals(mTvSendIdentify.isEnabled(),false);
        rule.getTestScheduler().advanceTimeTo(10, TimeUnit.SECONDS);
        assertEquals(mTvSendIdentify.getText().toString(), "111秒后重试");
        rule.getTestScheduler().advanceTimeTo(120,TimeUnit.SECONDS);
        assertEquals(mTvSendIdentify.getText().toString(),"发送验证码");
        assertEquals(mTvSendIdentify.isEnabled(),true);
    }

    @Test
    public void testLogin(){
        mEtMobile.setText("123");
        mEtIdentify.setText("123");
        mTvLogin.performClick();
//        verify(view).showError("手机号码不正确");
//        assertEquals("手机号码不正确", ShadowToast.getTextOfLatestToast());

        mEtMobile.setText("12345678901");
        mEtIdentify.setText("123");
        mTvLogin.performClick();
//        verify(view).showError("验证码不正确");
//        assertEquals("验证码不正确", ShadowToast.getTextOfLatestToast());

        mEtMobile.setText("12345678901");
        mEtIdentify.setText("123456");
        mTvLogin.performClick();

        assertNotNull(ShadowProgressDialog.getLatestDialog());
//        verify(view).showError("登录成功");
//        assertEquals("登录成功",ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void testShowLoading(){
        loginActivity.showLoading();
        assertTrue(ShadowProgressDialog.getLatestDialog().isShowing());

        loginActivity.hideLoading();
        assertFalse(ShadowProgressDialog.getLatestDialog().isShowing());

        loginActivity.onDestroy();
        loginActivity
    }
}