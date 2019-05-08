package com.mohe.fastdevpro.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.mohe.fastdevpro.bean.QueryTransPresenterBean;
import com.mohe.fastdevpro.study.xposed.MyXposedHelper;

public class XposedQueryTransService extends Service {

    private Object instanceQueryPresent;

    public XposedQueryTransService() {
    }

    private static final String TAG = "XposedQueryTransService";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"XposedQueryTransService onStartCommand");
        QueryTransPresenterBean bean=intent.getParcelableExtra("instance");
        instanceQueryPresent=bean.getQueryTransPreInstance();
        MyXposedHelper.scheduleQueryTrans(instanceQueryPresent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        MyXposedHelper.startScheduleService(this,instanceQueryPresent);
        super.onDestroy();
    }
}
