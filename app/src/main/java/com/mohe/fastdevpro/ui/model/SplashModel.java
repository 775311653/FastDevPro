package com.mohe.fastdevpro.ui.model;

import android.os.CountDownTimer;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.mohe.fastdevpro.ui.contract.SplashContract;

/**
 * Created by xiePing on 2018/7/16 0016.
 */
public class SplashModel implements SplashContract.Model {
    @Override
    public int getTimeCount(final OnCountDownListener onCountDownListener) {
        OkGo.<String>post("00").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {

            }
        });
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
                onCountDownListener.onTick(l);
            }

            @Override
            public void onFinish() {
                onCountDownListener.onFinish();
            }
        }.start();
        return 0;
    }
    public interface OnCountDownListener{
        void onTick(long l);
        void onFinish();
    }
}
