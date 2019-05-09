package com.mohe.fastdevpro.utils;

import android.app.Activity;
import android.os.Build;

import com.blankj.utilcode.util.LogUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by xiePing on 2019/5/3 0003.
 * Description:
 */
public class CommonUtils {

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    /**
     * 判断activity是否可用
     * @param activity
     * @return true:可用
     */
    public static boolean isActivityUseable(Activity activity){
        if (activity==null||activity.isFinishing()||activity.isRestricted()){
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed())
            return false;
        }
        return true;
    }


    public static Map<String, String> jsonToMap(String var0) {
        HashMap var1 = new HashMap();

        try {
            JSONObject var2 = JSONUtil.getJSONObject(var0);
            Iterator var3 = var2.keys();

            while(var3.hasNext()) {
                String var4 = (String)var3.next();
                var1.put(var4, var2.get(var4) + "");
            }
        } catch (Exception var5) {
            LogUtils.i(var5);
        }

        return var1;
    }
}
