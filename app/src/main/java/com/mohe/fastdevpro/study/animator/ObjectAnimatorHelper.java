package com.mohe.fastdevpro.study.animator;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.Animation;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.mohe.fastdevpro.R;
import com.mohe.fastdevpro.bean.ViewPropertyBean;

/**
 * Created by xiePing on 2019/5/1 0001.
 * Description:
 */
public class ObjectAnimatorHelper {

    /**
     * 展示一些用ObjectAnimator实现的通用动画
     */
    public static void showCommonPropertyAnim(View view){
        ObjectAnimator oaTranslationX=ObjectAnimator.ofFloat(view,"translationX",200);
        oaTranslationX.setDuration(1500);
        ObjectAnimator oaTranslationY=ObjectAnimator.ofFloat(view,"translationY",200);
        oaTranslationY.setDuration(1500);
        ObjectAnimator oaAlpha=ObjectAnimator.ofFloat(view,"alpha",1,0,1);
        oaAlpha.setDuration(1500);
        ObjectAnimator oaScaleX=ObjectAnimator.ofFloat(view,"scaleX",100);
        oaScaleX.setDuration(1500);
        ObjectAnimator oaRotation=ObjectAnimator.ofFloat(view,"Rotation",180);
        oaRotation.setDuration(1500);

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.play(oaTranslationX)
                .with(oaTranslationY)
                .before(oaAlpha)
                .after(oaRotation)
                .before(oaScaleX);
//        play(objectAnimator);开始动画
//        with(oa);同时开始oa动画
//        after(oa1);oa1先执行完。
//        before(oa2);先于oa2执行。
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ToastUtils.showShort("动画执行完成");
            }
        });
    }

    /**
     * 展示自定义的属性变化动画
     */
    @SuppressLint("ObjectAnimatorBinding")
    public static void showCustomPropertyAnim(View view){
        //可以按照面向对象来修改属性，执行动画。
        ViewWrapper viewWrapper=new ViewWrapper(view);
        ViewPropertyBean startProperty=new ViewPropertyBean(view.getMeasuredWidth(),view.getMeasuredHeight());
        ViewPropertyBean endProperty=new ViewPropertyBean(view.getMeasuredWidth()+300,view.getMeasuredHeight()+300);
        ObjectAnimator.ofObject(viewWrapper,"viewProperty",new ViewTypeEvaluator(),startProperty,endProperty)
            .setDuration(3000)
            .start();
        //下面这些是单纯只改int属性使用
//        ObjectAnimator.ofInt(viewWrapper,"width"
//                ,view.getMeasuredWidth()
//                ,view.getMeasuredWidth()+300)
//                .setDuration(3000)
//                .start();
    }

    /**
     * 自定义的估值器，通过系统返回的变化系数fraction，自己实现得到变化后的对象
     */
    private static class ViewTypeEvaluator implements TypeEvaluator{

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            ViewPropertyBean startVP= (ViewPropertyBean) startValue;
            ViewPropertyBean endVP= (ViewPropertyBean) endValue;
            int currentWidth= (int) (startVP.getWidth()+(
                    fraction*(endVP.getWidth()-startVP.getWidth())));
            int currentHeight=(int) (startVP.getHeight()+(
                    fraction*(endVP.getHeight()-startVP.getHeight())));
            return new ViewPropertyBean(currentWidth,currentHeight);
        }
    }

    /**
     * 对View进行包装，使用这个包装类，具体调用还是View
     */
    private static class ViewWrapper{

        private View mView;
        private int width;
        private ViewPropertyBean viewProperty;

        public ViewPropertyBean getViewProperty() {
            return viewProperty;
        }

        public void setViewProperty(ViewPropertyBean viewPropertyBean) {
            this.viewProperty = viewPropertyBean;
            mView.getLayoutParams().width=viewProperty.getWidth();
            mView.getLayoutParams().height=viewProperty.getHeight();
            mView.requestLayout();
        }

        public ViewWrapper(View view) {
            mView=view;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
            mView.getLayoutParams().width=width;
            mView.requestLayout();
        }

    }

    /**
     *展示view的直接调用的属性动画
     */
    public static void showViewPropertyAnim(View view){
        view.animate()
                .alpha(0.3f)
                .x(view.getX()+200)
                .y(view.getY()+200)
                .setDuration(1500);
    }

    public static void showXMLPropertyAnim(View view){
        AnimatorSet animatorSet= (AnimatorSet) AnimatorInflater.loadAnimator(ActivityUtils.getTopActivity(), R.animator.anim_rotation_translationx_alpha);
        animatorSet.setTarget(view);
        animatorSet.start();
    }

    /**
     * 获取位移的ObjectAnimator，给view进行区别设置差值器使用
     * @return
     */
    public static ObjectAnimator getTranslateOA(View view){
        return ObjectAnimator.ofFloat(view,"translationX",0,600)
                .setDuration(1500);
    }
}
