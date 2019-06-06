package com.mohe.fastdevpro.study.adaptscreen;

import android.content.res.Resources;
import android.os.Bundle;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AdaptScreenUtils;
import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 教程地址 https://juejin.im/post/5c18039d5188253b7e74987e
 * 使用AdaptScreenUtils.adaptWidth(.,1080);就说明宽度是1080pt，即1像素点=1pt;
 * 使用这个功能的目的是为了:
 * 1、转化文档里面的像素功能比较方便。
 * 2、不同的手机能显示相同比例的宽度。比普通使用360dp更好用。
 *    框架会根据手机的宽度尺寸大小比如3英寸,分成1080份。这样每个手机显示的宽度就都一样了。
 *
 * 注意：使用close的时候会导致1pt=1dp；所以如果要关闭的话，建议在布局里面就使用dp和sp了，不要用pt。
 */
public class AdaptScreenDemoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapt_screen_demo);
        ButterKnife.bind(this);
    }

    @Override
    public Resources getResources() {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 1080);
    }

    @OnClick(R.id.btnGoClose)
    public void onViewClicked() {
        ActivityUtils.startActivity(CloseAdaptScreenActivity.class);
    }
}
