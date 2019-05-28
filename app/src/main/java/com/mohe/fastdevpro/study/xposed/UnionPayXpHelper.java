package com.mohe.fastdevpro.study.xposed;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.IOUtils;
import com.mohe.fastdevpro.bean.CPOrderBean;
import com.mohe.fastdevpro.bean.CPOrderBeanDao;
import com.mohe.fastdevpro.study.xposed.util.Des3;
import com.mohe.fastdevpro.utils.JSONUtil;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by xiePing on 2019/5/29 0029.
 * Description:
 */
public class UnionPayXpHelper {

    public static boolean AUTO = false;
    public static boolean UNIONPAY_HOOK = false;
    public static Class UPPushService = null;
    public static Activity activity = null;
    public static Application app = null;
    public static final String checkOrder = "com.android.unionpay.chexk";
    public static String encvirtualCardNo = null;
    public static MyHandler handler = null;
    public static ClassLoader mClassLoader = null;
    public static String mNotifyurl = "http://103.249.104.14/commonApi/callback";
    public static MyNweHandler newhandler;
    public static Service pushService;
    public static Long time;

    public static final String PACKAGE_NAME_CLOUD_PAY="com.unionpay";
    //    public static final String CLS_CP_TRANSFER_RESP_PARAM="com.unionpay.network.model.resp.UPQrp2ptransferRecordRespParam";
//    public static final String CLS_CP_TRANSFER_RESP_PARAM="com.unionpay.network.model.UPID";
    public static final String CLS_CP_TRANSFER_RESP_PARAM="com.unionpay.push.UPPushService";
    //    public static final String METHOD_GET_ORDERS="getOrders";
    public static final String METHOD_GET_ORDERS="onCreateView";

    public static final String ACTION_CONNECT = "com.chuxin.socket.ACTION_CONNECT";
    public static final String ACTION_NOTIFI = "com.chuxin.socket.ACTION_NOTIFI";
    public static final String ACTION_NOTIFI_NEW = "com.chuxin.socket.ACTION_NOTIFI_NEW";


    //云闪付是否初始化过
    private static boolean isCPOrderInited =false;
    public static CPOrderBeanDao orderBeanDao;
    private static final String TAG = "UnionPayXpHelper";

    public static void hookCPGetOrders(final XC_LoadPackage.LoadPackageParam lpparam, final Class hookClass){
        try {
            XposedHelpers.findAndHookMethod(hookClass
                    , "c"
//                    ,XposedHelpers.newInstance(lpparam.classLoader.loadClass("com.unionpay.network.model.UPID"),1).getClass()
                    ,String.class
//                    ,Class.class
                    , new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            XposedBridge.log("getOrders方法调用开始");
                            String str = (String) param.args[0];
                            XposedBridge.log(str);
                            try {
                                if (str.contains("付款") && str.contains("动账通知")) {
                                    str = new org.json.JSONObject(str).getJSONObject("body").getString("alert");
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("FORRECODE alert:");
                                    stringBuilder.append(str);
                                    XposedBridge.log(stringBuilder.toString());
                                    String[] split = str.split("元,")[0].split("通过扫码向您付款");
                                    if (split.length == 2) {
                                        String str2 = split[0];
                                        str = split[1];
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append("New Push Msg u:");
                                        stringBuilder.append(str2);
                                        stringBuilder.append(" m:");
                                        stringBuilder.append(str);
                                        XposedBridge.log(stringBuilder.toString());
                                        Intent intent = new Intent(checkOrder);
                                        intent.putExtra("name", str2);
                                        intent.putExtra("title", str);
                                        getContext().sendBroadcast(intent);
                                    }
                                }
                            } catch (Throwable th) {
                                XposedBridge.log(th);
                            }
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            Log.i(TAG,"getOrders方法调用结束");
                            Log.i(TAG,"UPQrp2ptransferRecordRespParam==null is"+(param.getResult()==null));
                            ThreadUtils.executeByCpuWithDelay(new ThreadUtils.SimpleTask<Object>() {
                                @Nullable
                                @Override
                                public Object doInBackground() throws Throwable {
                                    return null;
                                }

                                @Override
                                public void onSuccess(@Nullable Object result) {
                                }
                            },2, TimeUnit.SECONDS);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        private MyBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String stringExtra;
            String stringExtra2;
            if (intent.getAction().equals(ACTION_CONNECT)) {
                stringExtra = intent.getStringExtra("money");
                stringExtra2 = intent.getStringExtra("mark");
                if (stringExtra == null) {
                    stringExtra = "0.01";
                }
                if (stringExtra2 == null) {
                    stringExtra2 = "测试";
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("money =");
                stringBuilder.append(stringExtra);
                stringBuilder.append("mark=");
                stringBuilder.append(stringExtra2);
                XposedBridge.log(stringBuilder.toString());
                final String finalStringExtra = stringExtra;
                final String finalStringExtra1 = stringExtra2;
                getVirtualCardNum(new GetCardNumListener() {
                    public void error(String str) {
                    }

                    public void success(String str) {
                        time = Long.valueOf(System.currentTimeMillis());
                        GenQrCode(finalStringExtra, finalStringExtra1);
                    }
                });
            } else if (intent.getAction().equals(checkOrder)) {
                stringExtra = intent.getStringExtra("name");
                stringExtra2 = intent.getStringExtra("title");
                final String finalStringExtra2 = stringExtra;
                final String finalStringExtra3 = stringExtra2;
                new Thread(new Runnable() {
                    public void run() {
                        String CheckNewOrder = CheckNewOrder(finalStringExtra2, finalStringExtra3);
                        String str = "";
                        String str2 = "";
                        if (!(CheckNewOrder == null || "".equals(CheckNewOrder))) {
                            try {
                                JSONObject parseObject = JSONUtil.getJSONObject(CheckNewOrder);
                                String string = parseObject.getString("orderDetail");
                                CheckNewOrder = parseObject.getString("orderId");
                                if (string != null) {
                                    try {
                                        if (!"".equals(string)) {
                                            str =JSONUtil.getJSONObject(string).optString("postScript");
                                        }
                                    } catch (Exception unused) {
                                    }
                                }
                                str2 = CheckNewOrder;
                            } catch (Exception unused2) {
                            }
                        }
                        Message message = new Message();
                        message.what = 1;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("收到扫码转账：name");
                        stringBuilder.append(finalStringExtra2);
                        stringBuilder.append("   title:");
                        stringBuilder.append(finalStringExtra3);
                        stringBuilder.append("  mark:");
                        stringBuilder.append(str);
                        message.obj = stringBuilder.toString();
                        handler.sendMessage(message);
                        httpPost(str, str2, finalStringExtra3);
                    }
                }).start();
            }
        }
    }

    private static class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            if (message.what == 1) {
                String str = (String) message.obj;
                Intent intent = new Intent(ACTION_NOTIFI);
                intent.putExtra("message", str);
                XposedBridge.log(str);
                if (app == null) {
                    ToastUtils.showShort(str);
                    getContext().sendBroadcast(intent);
                    return;
                }
                ToastUtils.showShort(str);
                app.sendBroadcast(intent);
            }
        }
    }

    private static class MyNweHandler extends Handler {
        public MyNweHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            if (message.what == 2) {
                String str = (String) message.obj;
                Intent intent = new Intent(ACTION_NOTIFI_NEW);
                intent.putExtra("message", str);
                XposedBridge.log(str);
                if (app == null) {
                    getContext().sendBroadcast(intent);
                } else {
                    app.sendBroadcast(intent);
                }
            }
        }
    }

    /**
     * 获取新的交易数据数组,插入到数组里面
     * @param objects hook到的交易数据数组
     * @return 本地的交易数据数组
     */
    private static List<CPOrderBean> getCPNewOrderBean(Object[] objects) {
        List<CPOrderBean> cpOrderBeans=new ArrayList<>();
        CPOrderBean lastOrder=new CPOrderBean();
        try {
            //降序排列获取最新插入的数据
            lastOrder=orderBeanDao.queryBuilder()
                    .orderDesc(CPOrderBeanDao.Properties.OrderTime)
                    .limit(1).list().get(0);
        }catch (Exception e){
            LogUtils.i(e.getMessage());
        }

        for (int i=0;i<objects.length;i++){
            Object o=objects[i];
            String mAmount= (String) XposedHelpers.getObjectField(o,"mAmount");
            String mCurrencyUnit= (String) XposedHelpers.getObjectField(o,"mCurrencyUnit");
            String mOrderId= (String) XposedHelpers.getObjectField(o,"mOrderId");
            String mOrderStatus= (String) XposedHelpers.getObjectField(o,"mOrderStatus");
            String mOrderTime= (String) XposedHelpers.getObjectField(o,"mOrderTime");
            String mOrderType= (String) XposedHelpers.getObjectField(o,"mOrderType");
            String mTitle= (String) XposedHelpers.getObjectField(o,"mTitle");
            String mTn= (String) XposedHelpers.getObjectField(o,"mTn");

            LogUtils.i(mAmount,mCurrencyUnit,mOrderId,mOrderStatus,mOrderTime,mOrderType,mTitle,mTn);
            if (lastOrder.getOrderId().equals(mOrderId)){
                return cpOrderBeans;
            }

            CPOrderBean orderBean=new CPOrderBean(mAmount
                    ,mCurrencyUnit
                    ,mOrderId
                    ,mOrderStatus
                    ,mOrderTime
                    ,mOrderType
                    ,mTitle
                    ,mTn);
            orderBeanDao.insert(orderBean);
            cpOrderBeans.add(orderBean);
        }
        return cpOrderBeans;
    }

    public static void getVirtualCardNum(final GetCardNumListener getCardNumListener) {
        new Thread(new Runnable() {
            public void run() {
                GetCardNumListener getCardNumListener = null;
                StringBuilder stringBuilder;
                try {
                    mlog("GetVirtualCardNum");
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("https://pay.95516.com/pay-web/restlet/qr/p2pPay/getInitInfo?cardNo=&cityCode=");
                    stringBuilder2.append(Enc(getcityCd()));
                    String stringBuilder3 = stringBuilder2.toString();
                    OkHttpClient okHttpClient = new OkHttpClient();
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("0;");
                    stringBuilder4.append(System.currentTimeMillis());
                    String string = okHttpClient.newCall(new Request.Builder().url(stringBuilder3)
                            .header("X-Tingyun-Id", getXTid())
                            .header("X-Tingyun-Lib-Type-N-ST", stringBuilder4.toString())
                            .header("sid", getSid())
                            .header("urid", geturid())
                            .header("cityCd", getcityCd())
                            .header("locale", "zh-CN")
                            .header(HTTP.USER_AGENT, "Android CHSP")
                            .header("dfpSessionId", getDfpSessionId())
                            .header("gray", getgray()).header("key_session_id", "")
                            .header(HTTP.TARGET_HOST, "pay.95516.com")
                            .build()).execute().body().string();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("GetVirtualCardNum str2=>");
                    stringBuilder.append(stringBuilder3);
                    stringBuilder.append(" RSP=>");
                    stringBuilder.append(string);
                    mlog(stringBuilder.toString());
                    string = Dec(string);
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("GetVirtualCardNum str2=>");
                    stringBuilder.append(stringBuilder3);
                    stringBuilder.append(" RSP=>");
                    stringBuilder.append(string);
                    mlog(stringBuilder.toString());
                    encvirtualCardNo = Enc(new JSONObject(string).getJSONObject("params").getJSONArray("cardList").getJSONObject(0).getString("virtualCardNo"));
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("encvirtualCardNo");
                    stringBuilder2.append(encvirtualCardNo);
                    mlog(stringBuilder2.toString());
                    if (getCardNumListener != null) {
                        getCardNumListener.success(encvirtualCardNo);
                    }
                } catch (Throwable th) {
                    mlog(th);
                    if (getCardNumListener != null) {
                        getCardNumListener = getCardNumListener;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(th.getMessage());
                        stringBuilder.append(th.getCause());
                        getCardNumListener.error(stringBuilder.toString());
                    }
                }
            }
        }).start();
    }

    public static String Dec(String str) {
        try {
            return (String) XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.unionpay.encrypt.IJniInterface", mClassLoader), "decryptMsg", str);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static String Enc(String str) {
        try {
            return (String) XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.unionpay.encrypt.IJniInterface", mClassLoader), "encryptMsg", str);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    private static String getXTid() {
        try {
            Class findClass = XposedHelpers.findClass("com.networkbench.agent.impl.m.s", mClassLoader);
            Object callMethod = XposedHelpers.callMethod(XposedHelpers.callStaticMethod(findClass, "f", new Object[0]), "H", new Object[0]);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("h=>");
            stringBuilder.append(callMethod);
            mlog(stringBuilder.toString());
            Object callStaticMethod = XposedHelpers.callStaticMethod(findClass, "I", new Object[0]);
            String obj = findClass.getDeclaredMethod("a", new Class[]{String.class, Integer.TYPE}).invoke(null, new Object[]{callMethod, callStaticMethod}).toString();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("xtid:");
            stringBuilder2.append(obj);
            stringBuilder2.append("");
            mlog(stringBuilder2.toString());
            return obj;
        } catch (Throwable th) {
            mlog(th);
            return null;
        }
    }

    private static String getSid() {
        String str = "";
        try {
            str = XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.unionpay.network.aa", mClassLoader), "b", new Object[0]), "e", new Object[0]).toString();
        } catch (Throwable th) {
            mlog(th);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("sid:");
        stringBuilder.append(str);
        stringBuilder.append("");
        mlog(stringBuilder.toString());
        return str;
    }

    private static String geturid() {
        String str = "";
        try {
            String obj = XposedHelpers.callMethod(XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.unionpay.data.d", mClassLoader), "a", new Class[]{Context.class}, activity), "A", new Object[0]), "getHashUserId", new Object[0]).toString();
            if (!TextUtils.isEmpty(obj) && obj.length() >= 15) {
                str = obj.substring(obj.length() - 15);
            }
        } catch (Throwable th) {
            mlog(th);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cacheurid:");
        stringBuilder.append(str);
        stringBuilder.append("");
        mlog(stringBuilder.toString());
        return str;
    }

    private static String getDfpSessionId() {
        String str = "";
        try {
            Object callStaticMethod = XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.unionpay.service.b", mClassLoader), "d", new Object[0]);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("o=>");
            stringBuilder.append(callStaticMethod);
            mlog(stringBuilder.toString());
            str = callStaticMethod.toString();
        } catch (Throwable th) {
            mlog(th);
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("CacheDfpSessionId:");
        stringBuilder2.append(str);
        stringBuilder2.append("");
        mlog(stringBuilder2.toString());
        return str;
    }

    private static String getcityCd() {
        String str = "";
        try {
            str = XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.unionpay.location.a", mClassLoader), "i", new Object[0]).toString();
        } catch (Throwable th) {
            mlog(th);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CachecityCd:");
        stringBuilder.append(str);
        stringBuilder.append("");
        mlog(stringBuilder.toString());
        return str;
    }

    private static String getgray() {
        String str = "";
        try {
            str = XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.unionpay.network.aa", mClassLoader), "b", new Object[0]), "d", new Object[0]).toString();
        } catch (Throwable th) {
            mlog(th);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cachegray:");
        stringBuilder.append(str);
        stringBuilder.append("");
        mlog(stringBuilder.toString());
        return str;
    }

    public static void httpPost(String str, String str2, String str3) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.currentTimeMillis());
        stringBuilder.append("");
        String stringBuilder2 = stringBuilder.toString();
        sendMessage("红包进来了", Boolean.valueOf(true));
        String str4 = "123456";
        HashMap hashMap = new HashMap();
        HashMap<String,String> requestParams =new HashMap<>();
        requestParams.put("type", "unionpay");
        requestParams.put("remark", str);
        requestParams.put("alipayAccount", str2);
        requestParams.put("dt", stringBuilder2);
        if (TextUtils.isEmpty(str3)) {
            requestParams.put("moeny", "null");
        } else {
            requestParams.put("money", str3);
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(str);
        stringBuilder3.append(str3);
        stringBuilder3.append(stringBuilder2);
        str = stringBuilder3.toString();
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("unionpay");
        stringBuilder4.append(str4);
        stringBuilder4.append(str2);
        str2 = stringBuilder4.toString();
        stringBuilder4 = new StringBuilder();
        stringBuilder4.append("context:");
        stringBuilder4.append(str);
        sendMessage(stringBuilder4.toString(), Boolean.valueOf(false));
        stringBuilder4 = new StringBuilder();
        stringBuilder4.append("key:");
        stringBuilder4.append(str2);
        sendMessage(stringBuilder4.toString(), Boolean.valueOf(false));
        try {
            str = Des3.encode(str, str2);
        } catch (Exception e) {
            StringBuilder stringBuilder5 = new StringBuilder();
            stringBuilder5.append("加密异常");
            stringBuilder5.append(e.getMessage());
            sendMessage(stringBuilder5.toString(), Boolean.valueOf(false));
            e.printStackTrace();
            str = str4;
        }
        requestParams.put("sign", str);
        sendMessage("开始回掉", Boolean.valueOf(false));
        OkGo.<String>post(mNotifyurl)
                .params(requestParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String str = (String) response.body();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(">>>>onSuccess");
                        stringBuilder.append(str);
                        XposedBridge.log(stringBuilder.toString());
                        if (str.contains("success")) {
                            try {
                                sendMessage("服务器成功返回", Boolean.valueOf(false));
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("服务器返回失败：");
                        stringBuilder.append(str);
                        sendMessage(stringBuilder.toString(), Boolean.valueOf(false));
                    }
                });
    }

    public static void GenQrCode(final String str, final String str2) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Message message = new Message();
                    message.what = 2;
                    message.obj = "开始生成二维码";
                    newhandler.sendMessage(message);
                    String str = str2;
                    String replace = new BigDecimal(str).setScale(2, RoundingMode.HALF_UP).toPlainString().replace(".", "");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("FORRECODE GenQrCode:0 money:");
                    stringBuilder.append(replace);
                    stringBuilder.append(" mark:");
                    stringBuilder.append(str);
                    mlog(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("https://pay.95516.com/pay-web/restlet/qr/p2pPay/applyQrCode?txnAmt=");
                    stringBuilder.append(Enc(replace));
                    stringBuilder.append("&cityCode=");
                    stringBuilder.append(Enc(getcityCd()));
                    stringBuilder.append("&comments=");
                    stringBuilder.append(Enc(str));
                    stringBuilder.append("&virtualCardNo=");
                    stringBuilder.append(encvirtualCardNo);
                    str = stringBuilder.toString();
                    OkHttpClient okHttpClient = new OkHttpClient();
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("0;");
                    stringBuilder2.append(System.currentTimeMillis());
                    replace = okHttpClient.newCall(new Request.Builder().url(str)
                            .header("X-Tingyun-Id", getXTid())
                            .header("X-Tingyun-Lib-Type-N-ST", stringBuilder2.toString())
                            .header("sid", getSid())
                            .header("urid",geturid())
                            .header("cityCd",getcityCd())
                            .header("locale", "zh-CN")
                            .header(HTTP.USER_AGENT, "Android CHSP")
                            .header("dfpSessionId",getDfpSessionId())
                            .header("gray",getgray())
                            .header("key_session_id", "")
                            .header(HTTP.TARGET_HOST, "pay.95516.com")
                            .build()).execute().body().string();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("GenQrCode str2=>");
                    stringBuilder.append(str);
                    stringBuilder.append(" RSP=>");
                    stringBuilder.append(replace);
                    mlog(stringBuilder.toString());
                    str = Dec(replace);
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(" RSP=>");
                    stringBuilder3.append(str);
                    mlog(stringBuilder3.toString());
                    JSONObject jSONObject = new JSONObject(str).getJSONObject("params");
                    Object obj = "";
                    if (jSONObject.has("certificate")) {
                        obj = jSONObject.get("certificate").toString();
                    }
                    message = new Message();
                    message.what = 1;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("二维码地址:");
                    stringBuilder.append(obj);
                    message.obj = stringBuilder.toString();
                    FileUtils fileUtils = new FileUtils();
                    File file=FileUtils.getFileByPath("/sdcard/code/code.txt");
                    String read = IOUtils.toString(new FileInputStream(file));
                    mlog(read);
                    JSONArray jSONArray = new JSONArray();
                    if (!(read == null || read == "")) {
                        jSONArray = new JSONArray(read);
                        for (int i=0;i<jSONArray.length();i++){
                            JSONObject js=jSONArray.getJSONObject(i);
                            if (str.equals(js.getString("money"))) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    jSONArray.remove(i);
                                }
                            }
                        }
                    }
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("money", str);
                    jSONObject2.put("mark", str2);
                    jSONObject2.put("payurl", obj);
                    jSONArray.put(jSONObject2);
                    com.mohe.fastdevpro.study.xposed.util.FileUtils.writeTxtToFile(jSONArray.toString(), "/sdcard/code/", "code.txt");
//                    CommonData.setPayUrl(obj);
                    handler.sendMessage(message);
                } catch (Throwable th) {
                    mlog(th);
                }
            }
        }).start();
    }

    public static void sendMessage(String str, Boolean bool) {
        Message message = new Message();
        message.what = 1;
        if (bool.booleanValue()) {
            message.what = 2;
        }
        message.obj = str;
        handler.sendMessage(message);
    }

    public static String CheckNewOrder(String str, String str2) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("FORRECODE CheckNewOrder user:");
            stringBuilder.append(str);
            stringBuilder.append(" money:");
            stringBuilder.append(str2);
            mlog(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("https://wallet.95516.com/app/inApp/order/list?currentPage=");
            stringBuilder.append(Enc("1"));
            stringBuilder.append("&month=");
            stringBuilder.append(Enc("0"));
            stringBuilder.append("&orderStatus=");
            stringBuilder.append(Enc("0"));
            stringBuilder.append("&orderType=");
            stringBuilder.append(Enc("A30000"));
            stringBuilder.append("&pageSize=");
            stringBuilder.append(Enc("10"));
            stringBuilder.append("");
            String stringBuilder2 = stringBuilder.toString();
            OkHttpClient.Builder newBuilder = new OkHttpClient().newBuilder();
            newBuilder.connectTimeout(50, TimeUnit.SECONDS);
            newBuilder.writeTimeout(50, TimeUnit.SECONDS);
            newBuilder.readTimeout(50, TimeUnit.SECONDS);
            OkHttpClient build = newBuilder.build();
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("0;");
            stringBuilder3.append(System.currentTimeMillis());
            String string = build.newCall(new Request.Builder().url(stringBuilder2).header("X-Tingyun-Id", getXTid()).header("X-Tingyun-Lib-Type-N-ST", stringBuilder3.toString()).header("sid", getSid()).header("urid", geturid()).header("cityCd", getcityCd()).header("locale", "zh-CN").header(HTTP.USER_AGENT, "Android CHSP").header("dfpSessionId", getDfpSessionId()).header("gray", getgray()).header("Accept", "*/*").header("key_session_id", "").header(HTTP.TARGET_HOST, "wallet.95516.com").build()).execute().body().string();
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append("CheckNewOrder str2=>");
            stringBuilder4.append(stringBuilder2);
            stringBuilder4.append(" RSP=>");
            stringBuilder4.append(string);
            mlog(stringBuilder4.toString());
            string = Dec(string);
            stringBuilder4 = new StringBuilder();
            stringBuilder4.append("CheckNewOrder str2=>");
            stringBuilder4.append(stringBuilder2);
            stringBuilder4.append(" DecRSP=>");
            stringBuilder4.append(string);
            mlog(stringBuilder4.toString());
            org.json.JSONArray jSONArray = new org.json.JSONObject(string).getJSONObject("params").getJSONArray("uporders");
            for (int i = 0; i < jSONArray.length(); i++) {
                org.json.JSONObject jSONObject = jSONArray.getJSONObject(i);
                String string2 = jSONObject.getString("orderId");
                if (jSONObject.getString("amount").equals(str2) && jSONObject.getString("title").contains(str)) {
                    return DoOrderInfoGet(string2);
                }
            }
            return "NO_MATCH_ORDER";
        } catch (Throwable th) {
            mlog(th);
            StringBuilder stringBuilder5 = new StringBuilder();
            stringBuilder5.append("ERR:");
            stringBuilder5.append(th.getLocalizedMessage());
            return stringBuilder5.toString();
        }
    }

    public static String DoOrderInfoGet(String str) {
        StringBuilder stringBuilder;
        if (str.length() > 5) {
            try {
                stringBuilder = new StringBuilder();
                stringBuilder.append("{\"orderType\":\"21\",\"transTp\":\"simple\",\"orderId\":\"");
                stringBuilder.append(str);
                stringBuilder.append("\"}");
                str = stringBuilder.toString();
                String str2 = "https://wallet.95516.com/app/inApp/order/detail";
                OkHttpClient okHttpClient = new OkHttpClient();
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("0;");
                stringBuilder2.append(System.currentTimeMillis());
                str = okHttpClient.newCall(new Request.Builder().url(str2).header("X-Tingyun-Id", getXTid()).header("X-Tingyun-Lib-Type-N-ST", stringBuilder2.toString()).header("sid", getSid()).header("urid", geturid()).header("cityCd", getcityCd()).header("locale", "zh-CN").header(HTTP.USER_AGENT, "Android CHSP").header("dfpSessionId", getDfpSessionId()).header("gray", getgray()).header("Accept", "*/*").header("key_session_id", "").header("Content-Type", "application/json; charset=utf-8").header(HTTP.TARGET_HOST, "wallet.95516.com").post(RequestBody.create(null, Enc(str))).build()).execute().body().string();
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("DoOrderInfoGet str2=>");
                stringBuilder3.append(str2);
                stringBuilder3.append(" RSP=>");
                stringBuilder3.append(str);
                mlog(stringBuilder3.toString());
                str = Dec(str);
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("FORRECODE DoOrderInfoGet str2=>");
                stringBuilder3.append(str2);
                stringBuilder3.append(" DecRSP=>");
                stringBuilder3.append(str);
                mlog(stringBuilder3.toString());
                org.json.JSONObject jSONObject = new org.json.JSONObject(str).getJSONObject("params");
                String string = jSONObject.getString("orderDetail");
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append("FORRECODE DoOrderInfoGet str2=>");
                stringBuilder4.append(str2);
                stringBuilder4.append(" orderDetail=>");
                stringBuilder4.append(string);
                mlog(stringBuilder4.toString());
                org.json.JSONObject jSONObject2 = new org.json.JSONObject(string);
                string = jSONObject2.getString("payUserName");
                String string2 = jSONObject2.getString("postScript");
                String string3 = jSONObject.getString("totalAmount");
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("FORRECODE DoOrderInfoGet str2=>");
                stringBuilder2.append(str2);
                stringBuilder2.append(" u:");
                stringBuilder2.append(string);
                stringBuilder2.append(" mark:");
                stringBuilder2.append(string2);
                stringBuilder2.append(" totalAmount:");
                stringBuilder2.append(string3);
                mlog(stringBuilder2.toString());
                return jSONObject.toString().replaceAll("[\\u4e00-\\u9fa5]", "*");
            } catch (Throwable th) {
                mlog(th);
                stringBuilder = new StringBuilder();
                stringBuilder.append("ERR:");
                stringBuilder.append(th.getLocalizedMessage());
                return stringBuilder.toString();
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("ERROR_ORDER:");
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    public static void mlog(String str) {
        XposedBridge.log(str);
    }

    public static void mlog(Throwable th) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(th.getMessage());
        stringBuilder.append("--");
        stringBuilder.append(th.getCause());
        mlog(stringBuilder.toString());
    }

    public static Context getContext() {
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Object invoke = cls.getMethod("currentActivityThread", new Class[0]).invoke(cls, new Object[0]);
            Context context = (Context) invoke.getClass().getMethod("getApplication", new Class[0]).invoke(invoke, new Object[0]);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Context ");
            stringBuilder.append(context);
            XposedBridge.log(stringBuilder.toString());
            return context;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
