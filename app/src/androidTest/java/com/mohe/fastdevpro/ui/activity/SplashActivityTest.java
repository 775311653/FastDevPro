package com.mohe.fastdevpro.ui.activity;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import com.blankj.utilcode.util.ConvertUtils;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Matcher;

import static org.junit.Assert.*;

/**
 * Created by xiePing on 2019/2/13 0013.
 * Description:
 */

@RunWith(AndroidJUnit4.class)
//@LargeTest
public class SplashActivityTest {
    @Rule
    public ActivityTestRule<SplashActivity> activityTestRule=new ActivityTestRule<>(SplashActivity.class);
    UiDevice uiDevice;
    @Before
    public void setUp() throws Exception {
        uiDevice=UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testloveClick() throws Exception {
        Thread.sleep(2000);
        uiDevice.pressRecentApps();
        uiDevice.wait(Until.findObject(By.text("全部清除")),2000);
        uiDevice.click(ConvertUtils.dp2px(180),ConvertUtils.dp2px(320));
        uiDevice.wait(Until.findObject(By.text("搜索结果")),2000);
    }
}