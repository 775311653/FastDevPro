package com.mohe.fastdevpro.pattern.responsibilityChainPattern;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mohe.fastdevpro.R;

public class ResponsibilityChainDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsibility_chain_demo);
        RespChainLogUtil.doLog(RespChainLogUtil.LOG_TYPE_ERROR,"logError");
        RespChainLogUtil.doLog(RespChainLogUtil.LOG_TYPE_DEBUG,"logDebug");
        RespChainLogUtil.doLog(RespChainLogUtil.LOG_TYPE_INFO,"logInfo");
    }
}
