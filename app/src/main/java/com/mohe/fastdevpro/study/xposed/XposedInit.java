package com.mohe.fastdevpro.study.xposed;

import android.os.Bundle;
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
        //逻辑:在fast1里面写xposed的代码，修改fast2里面的tv的内容，并且在fast1里面获取并打印出来。
        if (lpparam.packageName.equals(MyXposedHelper.HOOK_PACKAGE_NAME)){
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
        }
    }
}
