package com.mohe.fastdevpro.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.LogUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by xiePing on 2019/5/3 0003.
 * Description:
 */
public class CommonUtils {

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    /**
     * 判断activity是否可用
     *
     * @param activity
     * @return true:可用
     */
    public static boolean isActivityUseable(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isRestricted()) {
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

            while (var3.hasNext()) {
                String var4 = (String) var3.next();
                var1.put(var4, var2.get(var4) + "");
            }
        } catch (Exception var5) {
            LogUtils.i(var5);
        }

        return var1;
    }

    //截图的方法

    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }
        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();
    /*   if(widthOrg > edgeLength && heightOrg > edgeLength)
      {*/
        //压缩到一个最小长度是edgeLength的bitmap
        int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
        int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
        int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
        Bitmap scaledBitmap;
        try {
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
        } catch (Exception e) {
            return null;
        }
        //从图中截取正中间的正方形部分。
        int xTopLeft = (scaledWidth - edgeLength) / 2;
        int yTopLeft = (scaledHeight - edgeLength - 600) / 2;
        try {
            result = Bitmap.createBitmap(scaledBitmap, 200, 400, 600, 600);
            scaledBitmap.recycle();
        } catch (Exception e) {
            return null;
        }
        /* }  */
        return result;
    }


    public static void saveMyBitmap(Bitmap mBitmap, String bitName) {
        File f = new File("/sdcard/ewmtp/" + bitName + ".jpg");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * aes加解密 用这个就好，不用blankj的
     * @param data
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static String encryptAES(String data, String key, String iv) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;

            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);


            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            if (iv != null) {
                IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
                cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, keyspec);
            }

            byte[] encrypted = cipher.doFinal(plaintext);

            return EncodeUtils.base64Encode2String(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param data 密文
     * @param key  密钥，长度16
     * @param iv   偏移量，长度16
     * @return 明文
     * @author miracle.qu
     */
    public static String decryptAES(String data, String key, String iv) throws Exception {
        try {
            byte[] encrypted1 = EncodeUtils.base64Decode(data);

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            if (iv != null) {
                IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
                cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, keyspec);
            }


            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString.trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //删除截图

    public static void deleteimg(String path) {
        File file = new File(path);
        file.delete();
    }
}
