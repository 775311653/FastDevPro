package com.mohe.fastdevpro.ui.mvp.contract;

import com.mohe.fastdevpro.ui.mvp.model.SplashModel;

/**
 * Created by xiePing on 2018/7/16 0016.
 */
public interface SplashContract {
    interface Model {
        int getTimeCount(SplashModel.OnCountDownListener onCountDownListener);
    }

    interface View {
        void setTimeCount(int timeCount);
    }

    interface Presenter {
        void downCountTime();
    }
}