package com.mohe.fastdevpro.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.mohe.fastdevpro.utils.CommonUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_DCIM;
import static org.junit.Assert.*;

/**
 * Created by xieping on 2019/6/3.
 */
@RunWith(AndroidJUnit4.class)
public class SplashActivityTest {
    @Rule
    public ActivityTestRule activityTestRule=new ActivityTestRule(SplashActivity.class);
    private SplashActivity mActivity;

    private UiDevice mDevice;

    @Before
    public void setUp() throws Exception{
        mDevice=UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        List<Activity> activities= ActivityUtils.getActivityList();
        for (int i=0;i<activities.size();i++){
            if (activities.get(i) instanceof SplashActivity) {
                mActivity = (SplashActivity) activities.get(i);
                break;
            }
        }

    }

    @Test
    public void scheduleGetQRCode()throws Exception{
        openRecentApp();
        List<String> mounts=getMountsByArray();
        LogUtils.i(mounts.size());
        for (int i=0;i<mounts.size();i++){
            getQRCode(mounts.get(i));
        }
    }

    /**
     * 打开最近一个app
     */
    private void openRecentApp() throws Exception {
        mDevice.pressRecentApps();
        threadSleep(300);
        mDevice.click(ConvertUtils.dp2px(180),ConvertUtils.dp2px(270));
        threadSleep(300);
    }

    private void getQRCode(String amount) throws Exception {
        //点击清除金额
        mDevice.click(400,1560);

        threadSleep(200);
        //点击设置金额
        mDevice.click(400,1450);

        threadSleep(300);
        mDevice.findObject(new UiSelector().textContains("请输入收款二维码的金额")).setText(amount);

        //点击确定
        mDevice.click(540,560);

        threadSleep(300);

        threadSleep(1500);
        String path="/sdcard/ccbPicture/1701/"+amount+".png";
        // 下面这句指定调用相机拍照后的照片存储的路径
        File file= new File(path);
        mDevice.takeScreenshot(file);
        LogUtils.i(path);
        threadSleep(100);
    }

    public static void threadSleep(long l){
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static float[] values=new float[]{100,200,300,500,1000,1500,2000,2500,3000,3500,4000,4500,5000,6000,7000,8000,10000};

    private List<String> getMountsByArray(){
        List<String> mounts=new ArrayList<>();
        for (int i=0;i<values.length;i++){
            float d=values[i];
            mounts.addAll(getMounts(d));
        }

        return mounts;
    }

    private List<String> getMounts(float value) {
        List<String> mounts=new ArrayList<>();
        for (float i=value-0.5f;i<value+0.01f;i=i+0.01f){
            BigDecimal b = new BigDecimal(i);
            i = b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
            mounts.add(i+"");
        }
        return mounts;
    }
}