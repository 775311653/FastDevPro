package com.mohe.fastdevpro.pattern.factoryPattern;

import android.support.annotation.StringDef;

import com.blankj.utilcode.util.PermissionUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xiePing on 2019/3/31 0031.
 * Description:
 */
public class ShapeUtils {

    public static final String CIRCLE="circle";
    public static final String RECT="rect";
    public static final String SQUARE="square";
    public static Shape getShape(@ShapeType String type){
        switch (type){
            case CIRCLE:
                return new CircleShap();
            case RECT:
                return new RectShap();
            case SQUARE:
                return new SquareShap();
        }
        return new RectShap();
    }

    @StringDef({CIRCLE,RECT,SQUARE})
    @Retention(RetentionPolicy.SOURCE)
    @interface ShapeType{}
}
