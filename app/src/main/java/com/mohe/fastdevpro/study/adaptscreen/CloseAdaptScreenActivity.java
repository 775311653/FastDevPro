package com.mohe.fastdevpro.study.adaptscreen;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.ui.base.BaseActivity;

public class CloseAdaptScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapt_screen_close);
    }

    @Override
    public Resources getResources() {
        return AdaptScreenUtils.closeAdapt(super.getResources());
    }
}
