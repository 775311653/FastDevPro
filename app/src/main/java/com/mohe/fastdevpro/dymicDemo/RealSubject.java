package com.mohe.fastdevpro.dymicDemo;

import com.blankj.utilcode.util.LogUtils;

/**
 * Created by xiePing on 2019/4/16 0016.
 * Description:
 */
public class RealSubject implements Subject {

    @Override
    public void request() {
        LogUtils.i("realSubject执行了request方法");
    }
}
