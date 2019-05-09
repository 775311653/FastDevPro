package com.mohe.fastdevpro.study.xposed;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.mohe.fastdevpro.bean.QueryTransPresenterBean;
import com.mohe.fastdevpro.bean.TransactionQueryBean;
import com.mohe.fastdevpro.bean.TransactionQueryRspBean;
import com.mohe.fastdevpro.bean.TransactionQueryStoreBean;
import com.mohe.fastdevpro.bean.TransactionQueryStoreRspBean;
import com.mohe.fastdevpro.bean.TrsacnQryStoreQuestBean;
import com.mohe.fastdevpro.bean.UserBean;
import com.mohe.fastdevpro.utils.CommonUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

    public static final String baseUrl="https://gateway.starpos.com.cn";
    public static final String QryTranListUrl=baseUrl+"/estmadp2/qrytran_list.json";

    public static final String LastQueryRspBean="lastQueryRspBean";
    public static final String StrLastQueryStoreHashMap="StrLastQueryStoreHashMap";

    private static int scheduleTaskCnt =0;

    public static Application APP_STOR_POS;
    public static UserBean userBean;
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

    public interface OnCallback<T>{
        void onSuccess(T t);
    }

    public interface OnInitAppCallback{
        void onSuccess(Class hookclass);
    }

    /**
     * 判断交易数据是否有变，通过tot_cnt总交易数判断
     * @param queryRspBean
     * @return true:有变
     */
    public static boolean isTransDataChange(TransactionQueryRspBean queryRspBean){
        String strLastQueryRspBean= SPUtils.getInstance(userBean.getNumber()).getString(LastQueryRspBean);
        TransactionQueryRspBean lastQuerRspBean= GsonUtils.fromJson(strLastQueryRspBean,TransactionQueryRspBean.class);
        String lastTotCnt=lastQuerRspBean.getTot_cnt();
        String nowTotCnt=queryRspBean.getTot_cnt();
        return !lastTotCnt.equals(nowTotCnt) ;
    }

    /**先判断tot_cnt是否有变，不变就过
     有变，循环从商户下的商店中判断每个商店bean的toto_txn_cnt是否有变，
     有变，获取对应的数值，就取出该商店下的前n+5个订单数据，从本地取出该商店的历史最新数据，比对是哪一个，取出剩下的前面的数据进行发送，
     记录最新的一个数据到本地保存。即外面的商店列表的接口只是判断是否有变，具体发送什么数据，由获取商店订单的数据进行判断是最新才发送。
     在循环未结束时，停止获取新数据。 对获取到的数据整合发送到服务端。

     * 通过所有商户的数据进行查询新的交易数据.
     * @param queryRspBean 商户数据
     */
    public static void getStoeNewTrans(final TransactionQueryRspBean queryRspBean){
        String strLastQueryRspBean= SPUtils.getInstance(userBean.getNumber()).getString(LastQueryRspBean);
        TransactionQueryRspBean lastQuerRspBean= GsonUtils.fromJson(strLastQueryRspBean,TransactionQueryRspBean.class);
        List<TransactionQueryBean> lastQueryBeans=lastQuerRspBean.getQryList();

//        final TransactionQueryBean queryBean=queryRspBean.getQryList().get(0);
//        getStoeNewTrans(queryBean, 5, new OnCallback<TransactionQueryStoreRspBean>() {
//            @Override
//            public void onSuccess(TransactionQueryStoreRspBean transactionQueryStoreRspBean) {
//                List<TransactionQueryStoreBean> queryStoreBeans= getNewQueryStoresCompareLocal(transactionQueryStoreRspBean,queryBean.getMerc_id());
//                for (int i=0;i<queryStoreBeans.size();i++){
//                    LogUtils.i(GsonUtils.toJson(queryStoreBeans.get(i)));
//                }
//
//            }
//        });

        List<TransactionQueryBean> queryBeans=queryRspBean.getQryList();
        for (int i=0;i<queryBeans.size();i++){
            TransactionQueryBean queryBean= queryBeans.get(i);
            TransactionQueryBean lastQryBean=getQueryBeanByMercId(queryBean.getMerc_id(),lastQueryBeans);

            String nowCnt=queryBean.getTot_txn_cnt();
            String lastCnt=lastQryBean.getTot_txn_cnt();
            //本地没数据，就查询该商铺的交易数据
            if (StringUtils.isEmpty(lastCnt)) lastCnt="0";
            int diffCnt=0;
            try {
                diffCnt=Integer.parseInt(nowCnt)-Integer.parseInt(lastCnt);
            }catch (Exception e){
                XposedBridge.log(e.getMessage());
            }

            //如果交易笔数和历史的不一样就开始查询该店铺的数据
            if (diffCnt>0){
                scheduleTaskCnt++;
                getStoeNewTrans(queryBean, diffCnt, new OnCallback<TransactionQueryStoreRspBean>() {
                    @Override
                    public void onSuccess(TransactionQueryStoreRspBean transactionQueryStoreRspBean) {
                        scheduleTaskCnt--;
                    }
                });
            }
        }
    }

    /**
     * 是否循环获取商铺列表里面的商铺交易数据结束
     * @return
     */
    public static boolean isScheduleGetStoreTransOver(){
        return scheduleTaskCnt==0;
    }

    /**
     * 通过店铺数据，获取该店铺下的交易数据
     * @param queryBean 店铺数据
     * @param cnt 需要查询的数量
     *{
        "usr_no": "00000241389",
        "token_id": "XqnOWV1Bg7aqM5E72I91zYNKb577R2i7",
        "end_dt": "20190509235900",
        "eqm_type": "nubia",
        "pag_size": "10",
        "characterSet": "GBK",
        "beg_dt": "20190501000000",
        "merc_id": "800318000001013",
        "contentTyp": "application/json",
        "pag_no": "1",
        "sys_cnl": "app",
        "txn_cd": "",
        "stoe_id": "101318057220005",
        "trm_no": "",
        "version": "3.0.0",
        "sn_no": "",
        "opSys": "0",
        "channel": "oppo",
        "usr_typ": "0",
        "pay_type": ""
        }
     */
    private static void getStoeNewTrans(TransactionQueryBean queryBean, int cnt, final OnCallback<TransactionQueryStoreRspBean> callback) {
        Map<String, String> hashMap = new HashMap(16);
        hashMap.put("stoe_id", queryBean.getStoe_id());
        hashMap.put("merc_id", queryBean.getMerc_id());
        TrsacnQryStoreQuestBean requestBean=setRequestBean(queryBean);

        hashMap.put("usr_typ", requestBean.getUsr_typ());
        hashMap.put("beg_dt", requestBean.getBeg_dt());
        hashMap.put("end_dt", requestBean.getEnd_dt());
        hashMap.put("txn_cd", requestBean.getTxn_cd());
        hashMap.put("pay_type",requestBean.getPay_type());
        hashMap.put("trm_no",requestBean.getTrm_no());
        hashMap.put("sn_no",requestBean.getSn_no());
        hashMap.put("pag_no", String.valueOf(1));
        hashMap.put("pag_size", String.valueOf(cnt+5));
        setCommonReqParams(hashMap);

        OkGo.<String>post(QryTranListUrl)
                .upJson(new JSONObject(hashMap))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        ToastUtils.showLong(response.body());
                        LogUtils.i(response.body());
                        callback.onSuccess(GsonUtils.fromJson(response.body(),TransactionQueryStoreRspBean.class));
                    }
                });

    }


    public static TrsacnQryStoreQuestBean setRequestBean(TransactionQueryBean queryBean) {
        String mEndDate = getTimeToStr(TimeUtils.getNowDate(), "yyyyMMdd") + "235900";
        String mBegDate =getTimeToStr(TimeUtils.getNowDate(), "yyyyMMdd")  + "000000";

        TrsacnQryStoreQuestBean trsacnQryStoreQuestBean = new TrsacnQryStoreQuestBean();
        trsacnQryStoreQuestBean.setMerc_id(queryBean.getMerc_id());
        trsacnQryStoreQuestBean.setBeg_dt(mBegDate);
        trsacnQryStoreQuestBean.setEnd_dt(mEndDate);
        trsacnQryStoreQuestBean.setUsr_typ(userBean.getType());
        trsacnQryStoreQuestBean.setTrm_no("");
        trsacnQryStoreQuestBean.setPay_type("");
        trsacnQryStoreQuestBean.setTxn_cd("");
        trsacnQryStoreQuestBean.setSn_no("");
        return trsacnQryStoreQuestBean;
    }

    public static Map<String,String> setCommonReqParams(Map<String,String> map){
        map.put("contentTyp", "application/json");
        map.put("opSys", "0");
        map.put("characterSet", "GBK");
        map.put("sys_cnl", "app");
        String versionName =AppUtils.getAppVersionName(PACKAGE_NAME_STAR_POS);

        if (versionName.contains("-debug")) {
            map.put("version", versionName.substring(0, versionName.indexOf("-")));
        } else {
            map.put("version", versionName);
        }
        map.put("token_id", userBean.getToken_id());
        map.put("usr_no", userBean.getNumber());
        map.put("channel", "oppo");
        map.put("eqm_type", Build.MANUFACTURER);
        return map;
    }

    /**
     * 通过店铺id获取所有店铺列表里面对应的店铺数据
     * @param merc_id 店铺id
     * @param queryBeans 所有店铺列表
     * @return 对应的店铺
     */
    public static TransactionQueryBean getQueryBeanByMercId(String merc_id,List<TransactionQueryBean> queryBeans){
        for (int i=0;i<queryBeans.size();i++){
            TransactionQueryBean queryBean=queryBeans.get(i);
            if (merc_id.equals(queryBean.getMerc_id())){
                return queryBean;
            }
        }
        return new TransactionQueryBean();
    }

    /**
     * 此商铺下的所有交易数据和本地保存的上一个最新数据进行对比,取出这个数据时间线之后的所有数据
     * @param storeRspBean 该商铺下查询到的一定数量的交易数据(查到的应该是1天时间内的)
     * @param merc_id 该商铺Id
     * @return 最新的数据
     */
    public static List<TransactionQueryStoreBean> getNewQueryStoresCompareLocal(TransactionQueryStoreRspBean storeRspBean,String merc_id){
        List<TransactionQueryStoreBean> newQueryStoreBeans=storeRspBean.getStoeTxnList();
        //每个账号表示一个商户，一个商户下有很多个商铺
        String strStoreBeanHashMap=SPUtils.getInstance(userBean.getNumber()).getString(StrLastQueryStoreHashMap);

        //本地保存的是一个商铺下对应的历史最新的一条数据的hashMap
        Map<String,String> lastStoreBeanHashMap= CommonUtils.jsonToMap(strStoreBeanHashMap);
        String strLastStoreBean=lastStoreBeanHashMap.get(merc_id);

        TransactionQueryStoreBean lastQueryStoreBean=GsonUtils.fromJson(strLastStoreBean,TransactionQueryStoreBean.class);

        //保存该商铺下的最新一条数据到sp，key值为该商铺id
        lastStoreBeanHashMap.put(merc_id,GsonUtils.toJson(newQueryStoreBeans.get(0)));
        SPUtils.getInstance(userBean.getNumber())
                .put(StrLastQueryStoreHashMap
                        ,new JSONObject(lastStoreBeanHashMap).toString());

        //如果本地没有保存数据，就把获取到的所有新数据发送到服务端
        if (lastQueryStoreBean==null) return newQueryStoreBeans;

        for (int i=0;i<newQueryStoreBeans.size();i++){
            TransactionQueryStoreBean storeBean=newQueryStoreBeans.get(i);
            if (storeBean.getLog_no().equals(lastQueryStoreBean.getLog_no())){
                return newQueryStoreBeans.subList(0,i);
            }
        }
        return newQueryStoreBeans;
    }

    @SuppressLint({"SimpleDateFormat"})
    public static String getTimeToStr(Date date, String str) {
        String str2 = "";
        if (date != null) {
            return new SimpleDateFormat(str).format(date);
        }
        return str2;
    }

}
