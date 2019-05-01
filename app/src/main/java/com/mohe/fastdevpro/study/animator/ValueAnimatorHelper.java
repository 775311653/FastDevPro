package com.mohe.fastdevpro.study.animator;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.support.annotation.IntDef;
import android.util.Log;
import android.view.View;

import com.mohe.fastdevpro.bean.ViewPropertyBean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xiePing on 2019/5/1 0001.
 * Description:
 */
public class ValueAnimatorHelper {

    public static final int SHOW_TYPE_OFINT=0;
    public static final int SHOW_TYPE_OFFLOAT=1;

    @IntDef({SHOW_TYPE_OFINT,SHOW_TYPE_OFFLOAT})
    @Retention(RetentionPolicy.SOURCE)
    @interface ShowType{}

    public static void showAnimatorOfInt(final View view, int startValue, int endValue){
        showAnimator(SHOW_TYPE_OFINT,view, startValue, endValue);
    }

    public static void showAnimatorOfFloat(final View view, int startValue, int endValue){
        showAnimator(SHOW_TYPE_OFFLOAT,view, startValue, endValue);
    }

    public static void showAnimatorOfObject(final View view, ViewPropertyBean startValue, ViewPropertyBean endValue){
        ValueAnimator valueAnimator=ValueAnimator.ofObject(new AnimatorEvaluator(),startValue,endValue);
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewPropertyBean viewPropertyBean= (ViewPropertyBean) animation.getAnimatedValue();
                Log.i("animator","with="+viewPropertyBean.getWidth()+",height="+viewPropertyBean.getHeight());
                view.getLayoutParams().width=viewPropertyBean.getWidth();
                view.getLayoutParams().height=viewPropertyBean.getHeight();
                view.requestLayout();
            }
        });
        valueAnimator.start();
    }

    //自定义的估值器
    private static class AnimatorEvaluator implements TypeEvaluator{

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            //估值器里面有一个fraction是指变化系数，由系统自动给我们。
            //每次动画的变化值为总的变化值*fraction；所以新的值就是把初始值加上前面得出的值。

            ViewPropertyBean startVP= (ViewPropertyBean) startValue;
            ViewPropertyBean endVP= (ViewPropertyBean) endValue;
            int currentWidth= (int) (startVP.getWidth()+(
                                fraction*(endVP.getWidth()-startVP.getWidth())));
            int currentHeight=(int) (startVP.getHeight()+(
                    fraction*(endVP.getHeight()-startVP.getHeight())));
            return new ViewPropertyBean(currentWidth,currentHeight);
        }
    }



    //展示动画效果
    private static void showAnimator(@ShowType final int showType, final View view, int startValue, int endValue) {
        ValueAnimator valueAnimator;
        switch (showType){
            case SHOW_TYPE_OFINT:
                valueAnimator=ValueAnimator.ofInt(startValue,endValue);
                break;
            case SHOW_TYPE_OFFLOAT:
                valueAnimator=ValueAnimator.ofFloat(startValue,endValue);
                break;
            default:
                valueAnimator=ValueAnimator.ofFloat(startValue,endValue);
                break;
        }
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (showType==SHOW_TYPE_OFINT){
                    int currentValue= (int) animation.getAnimatedValue();
                    view.getLayoutParams().width=currentValue;
                    Log.i("animator",currentValue+"");
                }else if (showType==SHOW_TYPE_OFFLOAT){
                    float currentValue= (float) animation.getAnimatedValue();
                    view.getLayoutParams().width= (int) currentValue;
                    Log.i("animator",currentValue+"");
                }
                view.requestLayout();
            }
        });
        valueAnimator.start();
    }
}
