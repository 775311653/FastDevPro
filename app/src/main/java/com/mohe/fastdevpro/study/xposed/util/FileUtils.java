package com.mohe.fastdevpro.study.xposed.util;

import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;

public class FileUtils {
    public static void writeTxtToFile(String str, String str2, String str3) {
        makeFilePath(str2, str3);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str2);
        stringBuilder.append(str3);
        str2 = stringBuilder.toString();
        try {
            File file = new File(str2);
            if (!file.exists()) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Create the file:");
                stringBuilder2.append(str2);
                Log.d("TestFile", stringBuilder2.toString());
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.write(str.getBytes());
            randomAccessFile.close();
        } catch (Exception e) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Error on write File:");
            stringBuilder3.append(e);
            Log.e("TestFile", stringBuilder3.toString());
        }
    }

    public static File makeFilePath(String str, String str2) {
        File file;
        Exception e;
        makeRootDirectory(str);
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(str2);
            file = new File(stringBuilder.toString());
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                return file;
            }
        } catch (Exception e3) {
            e = e3;
            file = null;
            e.printStackTrace();
            return file;
        }
        return file;
    }

    public static void makeRootDirectory(String str) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(e);
            stringBuilder.append("");
            Log.i("error:", stringBuilder.toString());
        }
    }
}
