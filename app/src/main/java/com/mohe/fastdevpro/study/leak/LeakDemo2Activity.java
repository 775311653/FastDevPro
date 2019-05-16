package com.mohe.fastdevpro.study.leak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.ui.activity.SplashActivity;

public class LeakDemo2Activity extends AppCompatActivity {

    public static byte[] bytes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_demo2);
        bytes=new byte[1024*1024*10];
        LeakDemoActivity.activities.add(this);
    }
}
