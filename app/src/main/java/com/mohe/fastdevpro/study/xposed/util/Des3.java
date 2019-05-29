package com.mohe.fastdevpro.study.xposed.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Des3 {
    private static final String encoding = "utf-8";
    private static final String iv = "01234567";

    public static String encode(String str, String str2) throws Exception {
        if (str2.length() > 32) {
            str2 = str2.substring(0, 32);
        }
        if (str2.length() < 32) {
            for (int length = str2.length(); length < 32; length++) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str2);
                stringBuilder.append("1");
                str2 = stringBuilder.toString();
            }
        }
        SecretKey generateSecret = SecretKeyFactory.getInstance("desede").generateSecret(new DESedeKeySpec(str2.getBytes()));
        Cipher instance = Cipher.getInstance("desede/CBC/PKCS5Padding");
        instance.init(1, generateSecret, new IvParameterSpec(iv.getBytes()));
        return Base64.encode(instance.doFinal(str.getBytes(encoding)));
    }

    public static String decode(String str, String str2) throws Exception {
        SecretKey generateSecret = SecretKeyFactory.getInstance("desede").generateSecret(new DESedeKeySpec(str2.getBytes()));
        Cipher instance = Cipher.getInstance("desede/CBC/PKCS5Padding");
        instance.init(2, generateSecret, new IvParameterSpec(iv.getBytes()));
        return new String(instance.doFinal(Base64.decode(str)), encoding);
    }
}
