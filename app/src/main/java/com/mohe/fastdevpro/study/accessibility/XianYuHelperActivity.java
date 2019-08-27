package com.mohe.fastdevpro.study.accessibility;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class XianYuHelperActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xian_yu_helper);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnStartAccessibility)
    public void onViewClicked() {
        startAccessibility();
    }

    //启动无障碍服务
    private void startAccessibility() {
        Intent intent3 = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent3);
    }
}
