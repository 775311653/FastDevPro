package com.mohe.fastdevpro.study.animator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

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
    @BindView(R.id.tv_ai)
    TextView tvAi;
    @BindView(R.id.tv_oi)
    TextView tvOi;
    @BindView(R.id.tv_adi)
    TextView tvAdi;
    @BindView(R.id.tv_ani)
    TextView tvAni;
    @BindView(R.id.tv_anoi)
    TextView tvAnoi;
    @BindView(R.id.tv_bi)
    TextView tvBi;
    @BindView(R.id.tv_ci)
    TextView tvCi;
    @BindView(R.id.tv_di)
    TextView tvDi;
    @BindView(R.id.tv_li)
    TextView tvLi;
    @BindView(R.id.tv_dai)
    TextView tvDai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator_demo);
        ButterKnife.bind(this);
    }


    /***
     * 展示差值器的动画
     */
    private void showInterpolator() {
        ObjectAnimator objectAnimatorAi=ObjectAnimatorHelper.getTranslateOA(tvAi);
        objectAnimatorAi.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator objectAnimatorAndi=ObjectAnimatorHelper.getTranslateOA(tvAdi);
        objectAnimatorAndi.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator objectAnimatorAni=ObjectAnimatorHelper.getTranslateOA(tvAni);
        objectAnimatorAni.setInterpolator(new AnticipateInterpolator());

        ObjectAnimator objectAnimatorAoi=ObjectAnimatorHelper.getTranslateOA(tvAnoi);
        objectAnimatorAoi.setInterpolator(new AnticipateOvershootInterpolator());

        ObjectAnimator objectAnimatorBi=ObjectAnimatorHelper.getTranslateOA(tvBi);
        objectAnimatorBi.setInterpolator(new BounceInterpolator());

        ObjectAnimator objectAnimatorCi=ObjectAnimatorHelper.getTranslateOA(tvCi);
        objectAnimatorCi.setInterpolator(new CycleInterpolator(1));

        ObjectAnimator objectAnimatorDai=ObjectAnimatorHelper.getTranslateOA(tvDai);
        objectAnimatorDai.setInterpolator(new MyAccelerateDecelerateInterpolator());

        ObjectAnimator objectAnimatorDi=ObjectAnimatorHelper.getTranslateOA(tvDi);
        objectAnimatorDi.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator objectAnimatorLi=ObjectAnimatorHelper.getTranslateOA(tvLi);
        objectAnimatorLi.setInterpolator(new LinearInterpolator());

        ObjectAnimator objectAnimatorOi=ObjectAnimatorHelper.getTranslateOA(tvOi);
        objectAnimatorOi.setInterpolator(new OvershootInterpolator());

        AnimatorSet animatorSet= new AnimatorSet();
        animatorSet.play(objectAnimatorAi)
                .with(objectAnimatorAndi)
                .with(objectAnimatorAni)
                .with(objectAnimatorAoi)
                .with(objectAnimatorBi)
                .with(objectAnimatorCi)
                .with(objectAnimatorDi)
                .with(objectAnimatorLi)
                .with(objectAnimatorOi)
                .with(objectAnimatorDai);
        animatorSet.start();
    }

    @OnClick({R.id.btn_animator, R.id.btn_showOfInt, R.id.btn_showOfFloat, R.id.btn_showOfObject
            , R.id.btn_showObjectAnimator, R.id.btn_showCustomObjectAnimator, R.id.btn_viewPropertyAnimator
            , R.id.btn_showXMLObjectAnimator,R.id.btn_showInterpolator})
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
                        , new ViewPropertyBean(btnAnimator.getMeasuredWidth(), btnAnimator.getMeasuredHeight())
                        , new ViewPropertyBean(btnAnimator.getMeasuredWidth() + 300, btnAnimator.getMeasuredHeight() + 300));
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
            case R.id.btn_showInterpolator:
                showInterpolator();
                break;
        }
    }

}
