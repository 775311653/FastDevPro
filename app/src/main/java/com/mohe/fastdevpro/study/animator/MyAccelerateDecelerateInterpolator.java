package com.mohe.fastdevpro.study.animator;

import android.util.Log;
import android.view.animation.Interpolator;

/**
 * Created by xiePing on 2019/5/4 0004.
 * Description:自定义的减速再加速的差值器
 */
public class MyAccelerateDecelerateInterpolator implements Interpolator {
    private static final String TAG = "DecelerateAccelerateInt";

    @Override
    public float getInterpolation(float input) {
        float result;
        //正弦函数从x=-0.5π开始，到0.5π，y从0/2到2/2；满足先加速，后减速。最后值是一直递增到1的效果
        result = (float) Math.abs((Math.sin(Math.PI * (input - 0.5)) + 1) / 2);
        return result;
    }
}
