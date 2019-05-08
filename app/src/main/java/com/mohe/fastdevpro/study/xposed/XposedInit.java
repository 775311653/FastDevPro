package com.mohe.fastdevpro.study.xposed;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.mohe.fastdevpro.bean.TransactionQueryRspBean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Timer;
import java.util.TimerTask;

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

    private Object instanceQueryPresenterClass;
    private Object instanceQueryActivity;

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
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                    ClassLoader cl = ((Context)param.args[0]).getClassLoader();
                    Class<?> hookclass = null;
                    Class<?> clsHookActivityQuery= null;
                    try {
                        hookclass = cl.loadClass(MyXposedHelper.CLS_TRANS_ACTION_QUERY);
                        clsHookActivityQuery = cl.loadClass(MyXposedHelper.ACTIVITY_TRANS_QUERY);
                    } catch (Exception e) {
                        Log.e("jyy", "寻找"+MyXposedHelper.CLS_TRANS_ACTION_QUERY+"报错", e);
                        return;
                    }
                    Log.i("jyy", "寻找成功"+MyXposedHelper.CLS_TRANS_ACTION_QUERY);

                    final Class<?> finalHookclass = hookclass;
                    hookActivityStarPosQueryTrans(lpparam,clsHookActivityQuery);
                    //获取查询交易类的实例对象，才能使用
                    instanceQueryPresenterClass =XposedHelpers.newInstance(finalHookclass);
                    instanceQueryActivity=XposedHelpers.newInstance(clsHookActivityQuery);
                    //开启循环执行查询交易数据的服务
                    MyXposedHelper.startScheduleService(((Context)param.args[0]),instanceQueryPresenterClass);
                }
            });


        }
    }




    /**
     * 星管家的查询交易presenter类里面的接口
     */
    private void hookStarPosQueryTrans(final XC_LoadPackage.LoadPackageParam lpparam, final Class hookClass) {
        XposedBridge.log("hookStarPosQueryTrans");

        XposedHelpers.findAndHookMethod(hookClass
                , MyXposedHelper.METHOD_QUERY_TRANSFER_MONEY
                , new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        XposedBridge.log("调用了星管家的查询交易接口，beforeHookedMethod,"+param.toString());
                        Class c= instanceQueryPresenterClass.getClass().getSuperclass();
                        Field fieldView=c.getDeclaredField("mView");
                        Field fieldGuid=c.getDeclaredField("mGuid");
                        fieldView.setAccessible(true);
                        fieldGuid.setAccessible(true);
                        Object mView=fieldView.get(instanceQueryPresenterClass);
                        Object mGuid=fieldGuid.get(instanceQueryPresenterClass);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        Class c=hookClass.getClassLoader().loadClass(MyXposedHelper.CLS_TRANS_ACTION_QUERY);
                        XposedBridge.log("调用了星管家的查询交易接口，afterHookedMethod,"+param.toString());
                    }
                });
    }

    /**
     * hook查询交易记录的页面的查询方法
     * @param lpparam xposed框架提供的工具参数
     * @param hookClass transActionQuery的方法
     */
    private void hookActivityStarPosQueryTrans(final XC_LoadPackage.LoadPackageParam lpparam, final Class hookClass) {
        Class<?> clsBeanTrans = XposedHelpers.findClass(MyXposedHelper.BEAN_TRANS_ACTION_QUERY_RSP,lpparam.classLoader);
        Object beanTrans=XposedHelpers.newInstance(clsBeanTrans);
        XposedHelpers.findAndHookMethod(hookClass
                , MyXposedHelper.METHOD_ACTIVITY_QUERY_TRANSFER
                ,beanTrans.getClass()
                , new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("hookActivityStarPosQueryTrans,beforeHookedMethod,"+param.toString());
                        Object data=param.args[0];
                        String json=GsonUtils.toJson(data);
                        XposedBridge.log(json);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("hookActivityStarPosQueryTrans，afterHookedMethod,"+param.toString());
                    }
                });
    }

}
