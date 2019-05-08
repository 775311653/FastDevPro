package com.mohe.fastdevpro.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mohe.fastdevpro.study.xposed.MyXposedHelper;

import java.util.Timer;
import java.util.TimerTask;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XposedQueryTransService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public XposedQueryTransService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        Timer timer=new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                XposedBridge.log("间隔两秒执行queryTrans的方法");
//                XposedBridge.log("hookClass="+finalHookclass.getName());
//
//                try {
//                    XposedHelpers.findMethodBestMatch(finalHookclass, MyXposedHelper.METHOD_QUERY_TRANSFER_MONEY).invoke((Context)param.args[0]);
//                } catch (Exception e){
//                    XposedBridge.log(e.getMessage());
//                }
////                                    XposedHelpers.callMethod(finalHookclass,MyXposedHelper.METHOD_QUERY_TRANSFER_MONEY);
//            }
//        },2000);
    }
}
