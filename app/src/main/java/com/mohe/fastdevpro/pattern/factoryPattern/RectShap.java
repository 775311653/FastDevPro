package com.mohe.fastdevpro.pattern.factoryPattern;

import com.blankj.utilcode.util.LogUtils;

/**
 * Created by xiePing on 2019/3/31 0031.
 * Description:
 */
public class RectShap implements Shape {
    @Override
    public void draw() {
        LogUtils.i(getClass().getSimpleName());
    }
}
