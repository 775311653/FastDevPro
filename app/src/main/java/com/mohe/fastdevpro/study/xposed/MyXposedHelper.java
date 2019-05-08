package com.mohe.fastdevpro.study.xposed;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.mohe.fastdevpro.bean.QueryTransPresenterBean;
import com.mohe.fastdevpro.service.XposedQueryTransService;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by xieping on 2019/5/7.
 */

public class MyXposedHelper {

    public static final String PACKAGE_NAME_FAST_DEV_PRO="com.mohe.fastdevpro_test";
    public static final String PACKAGE_NAME_STAR_POS="com.newland.satrpos.starposmanager";
    public static final String SPLASH_ACTIVITY_NAME="com.mohe.fastdevpro.ui.activity.SplashActivity";

    public static final String CLS_TRANS_ACTION_QUERY="com.newland.satrpos.starposmanager.module.home.transactionquery.TransactionQueryPresenter";
    public static final String METHOD_QUERY_TRANSFER_MONEY ="queryTransferMoney";

    public static final String ACTIVITY_TRANS_QUERY="com.newland.satrpos.starposmanager.module.home.transactionquery.TransactionQueryActivity";
    public static final String METHOD_ACTIVITY_QUERY_TRANSFER="queryTransaction";

    public static final String BEAN_TRANS_ACTION_QUERY_RSP="com.newland.satrpos.starposmanager.model.responsebean.TransactionQueryRspBean";


    public static final String STR_ON_CREATE="onCreate";

    public MyXposedHelper() {
    }

    public static void initHooking(XC_LoadPackage.LoadPackageParam lpparam) throws NoSuchMethodException {


        final Class <?> httpUrlConnection = findClass("java.net.HttpURLConnection",lpparam.classLoader);


        hookAllConstructors(httpUrlConnection, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                if (param.args.length != 1 || param.args[0].getClass() != URL.class)
                    return;

                XposedBridge.log("HttpURLConnection: " + param.args[0] + "");
            }
        });

        XC_MethodHook ResponseHook = new XC_MethodHook() {

            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                HttpURLConnection urlConn = (HttpURLConnection) param.thisObject;

                if (urlConn != null) {
                    StringBuilder sb = new StringBuilder();
                    int code = urlConn.getResponseCode();
                    if(code==200){

                        Map<String, List<String>> properties = urlConn.getHeaderFields();
                        if (properties != null && properties.size() > 0) {

                            for (Map.Entry<String, List<String>> entry : properties.entrySet()) {
                                sb.append(entry.getKey() + ": " + entry.getValue() + ", ");
                            }
                        }
                    }

                    XposedBridge.log( "RESPONSE: method=" + urlConn.getRequestMethod() + " " +
                            "URL=" + urlConn.getURL().toString() + " " +
                            "Params=" + sb.toString());
                }

            }
        };




        findAndHookMethod("java.io.OutputStream", lpparam.classLoader, "write", byte[].class,int.class,int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                OutputStream os = (OutputStream)param.thisObject;
                if(!os.toString().contains("internal.http"))
                    return;
                String print = new String((byte[]) param.args[0]);
                XposedBridge.log("DATA"+print.toString());
                Pattern pt = Pattern.compile("(\\w+=.*)");
                Matcher match = pt.matcher(print);
                if(match.matches())
                {
                    XposedBridge.log("POST DATA: "+print.toString());
                }
            }
        });


        findAndHookMethod("java.io.OutputStream", lpparam.classLoader, "write", byte[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                OutputStream os = (OutputStream)param.thisObject;
                if(!os.toString().contains("internal.http"))
                    return;
                String print = new String((byte[]) param.args[0]);
                XposedBridge.log("DATA: "+print.toString());
                Pattern pt = Pattern.compile("(\\w+=.*)");
                Matcher match = pt.matcher(print);
                if(match.matches())
                {
                    XposedBridge.log("POST DATA: "+print.toString());
                }
            }
        });

        try {
            final Class<?> okHttpClient = findClass("com.android.okhttp.OkHttpClient", lpparam.classLoader);
            if(okHttpClient != null) {
                findAndHookMethod(okHttpClient, "open", URI.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        URI uri = null;
                        if (param.args[0] != null)
                            uri = (URI) param.args[0];
                        XposedBridge.log( "OkHttpClient: " + uri.toString() + "");
                    }
                });
            }
        } catch (Error e) {

        }



        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {

                findAndHookMethod("libcore.net.http.HttpURLConnectionImpl", lpparam.classLoader, "getOutputStream", ResponseHook);
            } else {
                findAndHookMethod("com.android.okhttp.internal.http.HttpURLConnectionImpl", lpparam.classLoader, "getOutputStream", ResponseHook);
                findAndHookMethod("com.android.okhttp.internal.http.HttpURLConnectionImpl", lpparam.classLoader, "getInputStream", ResponseHook);
            }
        } catch (Error e){
        }



        /* Hook org.apache.http 包中的 HttpPost 请求 */
        findAndHookMethod("org.apache.http.impl.client.AbstractHttpClient",
                lpparam.classLoader, "execute", HttpUriRequest.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (!param.args[0].getClass().getCanonicalName().contains("HttpPost")) {
                            return;
                        }
                        HttpPost request = (HttpPost) param.args[0];
                        String url = request.getURI().toString();
                        String test1=  request.getMethod();


                        XposedBridge.log("HttpPost——getURI:"+url);
                        XposedBridge.log("HttpPost——getMethod:"+test1);



                        Header[] headers = request.getAllHeaders();
                        if (headers != null) {
                            for (int i = 0; i < headers.length; i++) {
                                XposedBridge.log("headers:"+headers[i].getName() + ":" + headers[i].getValue());
                            }
                        }
                        HttpPost httpPost = (HttpPost) request;

                        HttpEntity entity = httpPost.getEntity();
                        String contentType = null;
                        if (entity.getContentType() != null) {
                            contentType = entity.getContentType().getValue();
                            if (URLEncodedUtils.CONTENT_TYPE.equals(contentType)) {

                                try {
                                    byte[] data = new byte[(int) entity.getContentLength()];
                                    entity.getContent().read(data);
                                    String content = new String(data, HTTP.UTF_8);
                                    XposedBridge.log("HTTP POST Content : " + content);
                                } catch (IllegalStateException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            } else if (contentType.startsWith(HTTP.UTF_8)) {
                                try {
                                    byte[] data = new byte[(int) entity.getContentLength()];
                                    entity.getContent().read(data);
                                    String content = new String(data, contentType.substring(contentType.lastIndexOf("=") + 1));
                                    XposedBridge.log("HTTP POST Content : " + content);
                                } catch (IllegalStateException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                        }else{
                            byte[] data = new byte[(int) entity.getContentLength()];
                            try {
                                entity.getContent().read(data);
                                String content = new String(data, HTTP.UTF_8);
                                XposedBridge.log("HTTP POST Content : " + content);
                            } catch (IllegalStateException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }


                    }
                });

    }


    //初始化的方法应该单独写到helper里面，再给回调方法
    public static void initPackageApp(final OnInitAppCallback callback){
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                ClassLoader cl = ((Context)param.args[0]).getClassLoader();
                Class<?> hookclass = null;
                try {
                    hookclass = cl.loadClass("xxx.xxx.xxx");
                } catch (Exception e) {
                    Log.e("jyy", "寻找xxx.xxx.xxx报错", e);
                    return;
                }
                Log.i("jyy", "寻找xxx.xxx.xxx成功");
                callback.onSuccess(hookclass);
                XposedHelpers.findAndHookMethod(hookclass, "xxx", new XC_MethodHook(){
                    //进行hook操作
                });
            }
        });
    }

    /**
     * 循环执行交易查询页面的查询方法
     * @param instanceQueryPresenterClass 查询页面的presenter类实例对象
     */
    public static void scheduleQueryTrans(final Object instanceQueryPresenterClass){
        Class<?> clzQryTraPresent = null;
        ClassLoader classLoader=instanceQueryPresenterClass.getClass().getClassLoader();
        try {
            clzQryTraPresent = classLoader.loadClass(MyXposedHelper.CLS_TRANS_ACTION_QUERY);
        } catch (Exception e) {
            Log.e("jyy", "寻找"+MyXposedHelper.CLS_TRANS_ACTION_QUERY+"报错", e);
            return;
        }

        //找到类里面的查询交易方法
        final Method methodQueryTrans=XposedHelpers.findMethodBestMatch(clzQryTraPresent,MyXposedHelper.METHOD_QUERY_TRANSFER_MONEY);
        final Class<?> finalClzQryTraPresent = clzQryTraPresent;
        //循环调用查询交易方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                Timer timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        XposedBridge.log("间隔两秒执行queryTrans的方法");
                        XposedBridge.log("hookClass="+ finalClzQryTraPresent.getName());
                        try {
                            methodQueryTrans.invoke(instanceQueryPresenterClass);
                        } catch (Exception e){
                            XposedBridge.log(e.getMessage());
                        }
                    }
                },2000);
            }
        }).start();
    }

    private static final String TAG = "MyXposedHelper";

    /**
     * 开启循环执行的服务
     * @param transQryPresInstance 查询交易的实例对象
     */
    public static void startScheduleService(Context context,Object transQryPresInstance){
        Log.i(TAG,"startScheduleService");
        Intent intent=new Intent(context,OpenServiceActivity.class);
        intent.putExtra("instance",new QueryTransPresenterBean(transQryPresInstance));
        context.startActivity(intent);
    }

    public interface OnCallback{
        void onSuccess(Class cls);
        void onFail();
    }

    public interface OnInitAppCallback{
        void onSuccess(Class hookclass);
    }

}
