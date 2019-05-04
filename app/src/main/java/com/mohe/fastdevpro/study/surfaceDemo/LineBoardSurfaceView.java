package com.mohe.fastdevpro.study.surfaceDemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.blankj.utilcode.util.LogUtils;

/**
 * Created by xiePing on 2019/5/3 0003.
 * Description:画板的SurfaceView
 * 注意使用surfaceView的时候主题不能设置背景颜色，这会把surfaceView给遮住
 * 教程
 * https://www.cnblogs.com/zhangyingai/p/7087371.html
 */
public class LineBoardSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private Canvas canvas;
    private Path path;
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private boolean isDrawing=false;

    private static final String TAG = "LineBoardSurfaceView";

    public LineBoardSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        path=new Path();
        paint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        paint.setStrokeWidth(10f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.YELLOW);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        surfaceHolder=getHolder();
        surfaceHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing=true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing=false;
    }

    @Override
    public void run() {
        while (isDrawing){
            draw();
        }
    }

    public void draw(){
        try {
            //获取画布
            canvas=surfaceHolder.lockCanvas();
            //清理画布
            canvas.drawColor(Color.WHITE);
            //画线
            canvas.drawPath(path,paint);
        }catch (Exception e){
            LogUtils.i(e.getMessage());
        }finally {
            if (canvas!=null){
                //提交画布内容
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x= (int) event.getX();
        int y= (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x,y);
                Log.i(TAG,"画线X="+x+",y="+y);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    /**
     * 清屏
     */
    public void reDraw(){
        path.reset();
    }
}
