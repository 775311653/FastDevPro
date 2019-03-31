package com.mohe.fastdevpro.ui.mvp.presenter;

import com.mohe.fastdevpro.BuildConfig;
import com.mohe.fastdevpro.bean.User;
import com.mohe.fastdevpro.rxjava.RxJavaTestSchedulerRule;
import com.mohe.fastdevpro.ui.mvp.contract.LoginContract;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by xiePing on 2018/10/26 0026.
 * Description:
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class LoginPresenterTest {

    private LoginPresenter mPresenter;

    @Rule
    public MockitoRule mockitoRule= MockitoJUnit.rule();

    @Mock
    private LoginContract.View view;

    @Rule
    public RxJavaTestSchedulerRule rule = new RxJavaTestSchedulerRule();

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream=System.out;
        mPresenter=new LoginPresenter();
        mPresenter.attachView(view);
    }

    @Test
    public void getIdentify() {
        mPresenter.getIdentify();
        rule.getTestScheduler().advanceTimeTo(10, TimeUnit.SECONDS);
        verify(view,times(10)).countdownNext(anyString());
        rule.getTestScheduler().advanceTimeTo(120,TimeUnit.SECONDS);
        verify(view,times(120)).countdownNext(anyString());
        verify(view).countdownComplete();
    }

    @Test
    public void requestLogin() {
        initRxJava();
        mPresenter.requestLogin("123","123");
        verify(view).showError("手机号码不正确");
        mPresenter.requestLogin("12345678901","123");
        verify(view).showError("验证码不正确");
        mPresenter.requestLogin("12345678901","123456");
        verify(view).showLoading();
        verify(view).loginSuccess((User) any());
        verify(view).hideLoading();

    }
    private void initRxJava() {
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
        RxAndroidPlugins.reset();
        RxAndroidPlugins.setMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
    }
}