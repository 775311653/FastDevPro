package com.mohe.fastdevpro.study.xposed;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by xieping on 2019/5/7.
 * Xposed的入口类，配置在assets的xposed_init文件里面。在virtual xposed等里面直接就会加载了。
 * 等到打开了我们想要的包名，再找到类名里面的方法名，在我们想处理的方法的前面和后面进行执行想要的逻辑。
 *
 * 教程地址：https://blog.csdn.net/coder_pig/article/details/80031285
 */

public class XposedInit implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("loadPackage="+lpparam.packageName);
        MyXposedHelper.initHooking(lpparam);
        //逻辑:在fast1里面写xposed的代码，修改fast2里面的tv的内容，并且在fast1里面获取并打印出来。
        if (lpparam.packageName.equals(MyXposedHelper.PACKAGE_NAME_FAST_DEV_PRO)){
            XposedHelpers.findAndHookMethod(MyXposedHelper.SPLASH_ACTIVITY_NAME
                    , lpparam.classLoader, MyXposedHelper.STR_ON_CREATE, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log("afterHookedMethod param="+param.toString());
                            try {
                                //注意加载的类名不要错了
                                Class c=lpparam.classLoader.loadClass(MyXposedHelper.SPLASH_ACTIVITY_NAME);
                                Field field=c.getDeclaredField("tv");
                                field.setAccessible(true);
                                XposedBridge.log("afterHookedMethod");
                                TextView tv= (TextView) field.get(param.thisObject);
                                tv.setText("fast1修改后的内容");
                                XposedBridge.log(tv.getText().toString());
                            }catch (Exception e){
                                XposedBridge.log("hookMethod exception="+ e.getMessage());
                            }

                        }
                    });
        }else if (lpparam.packageName.equals(MyXposedHelper.PACKAGE_NAME_STAR_POS)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        hookStarPosQueryTrans(lpparam);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();

        }
    }

    /**
     * 星管家的查询交易的接口
     * @param lpparam
     */
    private void hookStarPosQueryTrans(final XC_LoadPackage.LoadPackageParam lpparam) {
        XposedBridge.log("hookStarPosQueryTrans");
        XposedHelpers.findAndHookMethod(MyXposedHelper.CLS_TRANS_ACTION_QUERY
                , lpparam.classLoader
                , MyXposedHelper.METHORD_QUERY_TRANSFER_MONEY
                , new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Class c=lpparam.classLoader.loadClass(MyXposedHelper.SPLASH_ACTIVITY_NAME);
                        XposedBridge.log("调用了星管家的查询交易接口，"+param.toString());
                    }
                });

        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook(){
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                ClassLoader cl = ((Context) param.args[0]).getClassLoader();
                //cl.loadclass("className")找其他类
                //Class.forName("className",true,cl)
                Class<?> hookclass = null;
                try {
                    hookclass = cl.loadClass("XXX.XXX.ClassName");
                } catch (Exception e) {
                    Log.e("MutiDex", "寻找XXX.XXX.ClassName失败", e);
                    return;
                }
                Log.e("MutiDex", "寻找XXX.XXX.ClassName成功");

                XposedHelpers.findAndHookMethod(
                        hookclass,
                        "methodName",//方法名称
                        args.class,//参数列表
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                super.beforeHookedMethod(param);
                            }
                        });
            }});
    }
}
