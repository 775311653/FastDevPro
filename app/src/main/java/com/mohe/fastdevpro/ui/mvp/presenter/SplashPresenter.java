package com.mohe.fastdevpro.ui.mvp.presenter;

import com.blankj.utilcode.util.ActivityUtils;
import com.mohe.fastdevpro.ui.activity.MainActivity;
import com.mohe.fastdevpro.ui.activity.SplashActivity;
import com.mohe.fastdevpro.ui.mvp.contract.SplashContract;
import com.mohe.fastdevpro.ui.mvp.model.SplashModel;

/**
 * Created by xiePing on 2018/7/16 0016.
 */
public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View iView;
    private SplashModel splashModel;

    public SplashPresenter(SplashActivity context) {
        splashModel=new SplashModel();
        iView=context;
    }

    @Override
    public void downCountTime() {
        splashModel.getTimeCount(new SplashModel.OnCountDownListener() {
            @Override
            public void onTick(long l) {
                iView.setTimeCount((int) (l/1000));
            }

            @Override
            public void onFinish() {
                ActivityUtils.startActivity(MainActivity.class);
            }
        });

    }

}
