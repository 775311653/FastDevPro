package com.mohe.fastdevpro.study.leak;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.blankj.utilcode.util.ActivityUtils;
import com.mohe.fastdevpro.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 在这个界面使用静态的activity数组进行保存activity对象，用leakCanary获取activity的泄漏的内存.
 */
public class LeakDemoActivity extends AppCompatActivity {

    public static List<Activity> activities = new ArrayList<>();
    @BindView(R.id.btn_jump)
    Button btnJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_demo);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        btnJump.performClick();
    }

    @OnClick(R.id.btn_jump)
    public void onViewClicked() {
        ActivityUtils.startActivity(LeakDemo2Activity.class);
    }
}
