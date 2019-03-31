package com.mohe.fastdevpro.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.pattern.builderPattern.BuilderDemoActivity;
import com.mohe.fastdevpro.pattern.factoryPattern.FactoryPatternActivity;
import com.mohe.fastdevpro.ui.base.BaseActivity;
import com.mohe.fastdevpro.ui.mvp.contract.SplashContract;
import com.mohe.fastdevpro.ui.mvp.presenter.SplashPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    }

    private void initData() {
//        splashPresenter = new SplashPresenter((SplashActivity) mContext);
//        splashPresenter.downCountTime();
        ActivityUtils.startActivity(BuilderDemoActivity.class);
        finish();
    }

    @Override
    public void setTimeCount(int timeCount) {
        splashTv.setText("还剩下"+timeCount+"秒");
    }
}
