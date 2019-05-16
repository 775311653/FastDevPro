package com.mohe.fastdevpro.study.layoutOptimize;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 布局优化activity
 */
public class LayoutOptimizeActivity extends BaseActivity {

    @BindView(R.id.vs_content)
    ViewStub vsContent;

    private TextView tvContent1,tvContent2;
    private LinearLayout llContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_optimize);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_show, R.id.btn_hide})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_show:
                showTvContent();
                break;
            case R.id.btn_hide:
                vsContent.setVisibility(View.GONE);
                break;
        }
    }

    private void showTvContent() {
        if (llContent==null){
            llContent= (LinearLayout) vsContent.inflate();
            tvContent1=llContent.findViewById(R.id.tv_content1);
            tvContent2=llContent.findViewById(R.id.tv_content2);
        }else llContent.setVisibility(View.VISIBLE);

    }
}
