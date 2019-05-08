package com.mohe.fastdevpro.study.xposed;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.mohe.fastdevpro.service.XposedQueryTransService;

public class OpenServiceActivity extends AppCompatActivity {
    private static final String TAG = "OpenServiceActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"OpenServiceActivity onCreate");
        Intent intent=getIntent();
        intent.setComponent(new ComponentName(this, XposedQueryTransService.class));
        startService(intent);
        finish();
    }
}
