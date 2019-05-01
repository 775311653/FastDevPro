package com.mohe.fastdevpro.study.animator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.bean.ViewPropertyBean;
import com.mohe.fastdevpro.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 属性动画测试demo,valueAnimator动画，ObjectAnimator动画
 * 教程
 * https://www.jianshu.com/p/2412d00a0ce4
 */
public class AnimatorDemoActivity extends BaseActivity {

    @BindView(R.id.btn_animator)
    Button btnAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator_demo);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_animator,R.id.btn_showOfInt, R.id.btn_showOfFloat, R.id.btn_showOfObject
            ,R.id.btn_showObjectAnimator,R.id.btn_showCustomObjectAnimator,R.id.btn_viewPropertyAnimator
            ,R.id.btn_showXMLObjectAnimator})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_animator:
                ToastUtils.showShort("点击了动画按钮");
                break;
            case R.id.btn_showOfInt:
                //showOfInt测试
                ValueAnimatorHelper.showAnimatorOfInt(btnAnimator
                        , btnAnimator.getMeasuredWidth()
                        , btnAnimator.getMeasuredWidth() + 300);
                break;
            case R.id.btn_showOfFloat:
                //showOfFloat测试
                ValueAnimatorHelper.showAnimatorOfFloat(btnAnimator
                        , btnAnimator.getMeasuredWidth()
                        , btnAnimator.getMeasuredWidth() + 300);
                break;
            case R.id.btn_showOfObject:
                ValueAnimatorHelper.showAnimatorOfObject(btnAnimator
                        ,new ViewPropertyBean(btnAnimator.getMeasuredWidth(),btnAnimator.getMeasuredHeight())
                        ,new ViewPropertyBean(btnAnimator.getMeasuredWidth()+300,btnAnimator.getMeasuredHeight()+300));
                break;
            case R.id.btn_showObjectAnimator:
                ObjectAnimatorHelper.showCommonPropertyAnim(btnAnimator);
                break;
            case R.id.btn_showCustomObjectAnimator:
                ObjectAnimatorHelper.showCustomPropertyAnim(btnAnimator);
                break;
            case R.id.btn_viewPropertyAnimator:
                ObjectAnimatorHelper.showViewPropertyAnim(btnAnimator);
                break;
            case R.id.btn_showXMLObjectAnimator:
                ObjectAnimatorHelper.showXMLPropertyAnim(btnAnimator);
                break;
        }
    }
}
