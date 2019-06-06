package com.mohe.fastdevpro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.dymicDemo.RealSubject;
import com.mohe.fastdevpro.dymicDemo.Subject;
import com.mohe.fastdevpro.dymicDemo.SubjectUtils;
import com.mohe.fastdevpro.service.StudentUtilsService;
import com.mohe.fastdevpro.study.adaptscreen.AdaptScreenDemoActivity;
import com.mohe.fastdevpro.study.layoutOptimize.LayoutOptimizeActivity;
import com.mohe.fastdevpro.study.leak.LeakDemoActivity;
import com.mohe.fastdevpro.study.xposed.XPUnionPayActivity;
import com.mohe.fastdevpro.ui.base.BaseActivity;
import com.mohe.fastdevpro.ui.mvp.contract.SplashContract;
import com.mohe.fastdevpro.ui.mvp.presenter.SplashPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class SplashActivity extends BaseActivity implements SplashContract.View {

    @BindView(R.id.splash_tv)
    TextView splashTv;
    private SplashPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initData();


        //aidl跨进程通信的测试
//        aidlDemo();
        //动态代理的代码
//        dymicDelegate();
    }

    //aidl跨进程通信的测试
    private void aidlDemo() {
        Intent intent = new Intent(this, StudentUtilsService.class);
        startService(intent);
        ActivityUtils.startActivity(MainActivity.class);
    }

    //动态代理的代码
    private void dymicDelegate() {
        Subject subject = new RealSubject();
        Subject subjectDelegate = (Subject) new SubjectUtils().bindSubject(subject);
        subjectDelegate.request();
    }

    private void initData() {
        splashPresenter = new SplashPresenter((SplashActivity) mContext);
        PermissionUtils.permission(PermissionConstants.PHONE
                ,PermissionConstants.STORAGE
                ,PermissionConstants.CONTACTS
                ,PermissionConstants.CAMERA).request();
//        splashPresenter.downCountTime();
        splashTv.performClick();
    }

    @Override
    public void setTimeCount(int timeCount) {
        splashTv.setText("还剩下" + timeCount + "秒");
    }

    @OnClick(R.id.splash_tv)
    public void onViewClicked() {
        ActivityUtils.startActivity(AdaptScreenDemoActivity.class);
    }
}
