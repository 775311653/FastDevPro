package com.mohe.fastdevpro.pattern.responsibilityChainPattern;

import android.support.annotation.IntDef;

import com.blankj.utilcode.util.LogUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xiePing on 2019/4/5 0005.
 * Description:
 */
public class RespChainLogUtil {

    public static final int LOG_TYPE_INFO=1;
    public static final int LOG_TYPE_DEBUG=2;
    public static final int LOG_TYPE_ERROR=3;

    @IntDef({LOG_TYPE_INFO,LOG_TYPE_DEBUG,LOG_TYPE_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    @interface RCLogType{}

    public static void doLog(@RCLogType int logType,String logMsg){
        switch (logType){
            case LOG_TYPE_ERROR:
                doErrorLog(logMsg);
            case LOG_TYPE_DEBUG:
                doDebugLog(logMsg);
            case LOG_TYPE_INFO:
                doInfoLog(logMsg);
                break;
        }
    }

    private static void doInfoLog(String infoMsg){
        LogUtils.i(infoMsg);
    }

    private static void doDebugLog(String debugMsg){
        LogUtils.d(debugMsg);
    }

    private static void doErrorLog(String errorMsg){
        LogUtils.e(errorMsg);
    }
}
