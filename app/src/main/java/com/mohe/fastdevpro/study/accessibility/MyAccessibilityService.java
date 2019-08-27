package com.mohe.fastdevpro.study.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;

import java.util.List;

/**
 * 无障碍服务
 */
public class MyAccessibilityService extends AccessibilityService {
    int mX = 40, mY = 140;

    private static final String TAG = "MyAccessibilityService";

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            try {
                String className = (String) event.getClassName();
                LogUtils.i("无障碍服务接收的类名：" + className);
                if (className.equals("com.taobao.idlefish.search.v1.SingleRowSearchResultActivity")) {
                    Thread.sleep(2000);
                    scroll2PositionClick(this, "男朋友的二手正品耐克", "com.taobao.idlefish:id/list_recyclerview", 1);
                }
            } catch (Exception e) {
                LogUtils.i(e.getMessage());
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void dispatchGestureView(int x, int y) {
        Point position = new Point(x, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path p = new Path();
        p.moveTo(position.x, position.y);
        builder.addStroke(new GestureDescription.StrokeDescription(p, 0L, 1000L));
        GestureDescription gesture = builder.build();
        dispatchGesture(gesture, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
            }
        }, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void clickPoint(final int x, final int y) {
        Point position = new Point(x, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path p = new Path();
        p.moveTo(position.x, position.y);
        builder.addStroke(new GestureDescription.StrokeDescription(p, 0L, 200L));
        GestureDescription gesture = builder.build();
        dispatchGesture(gesture, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                LogUtils.i("点击位置", x, y);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                LogUtils.i("未成功点击位置", x, y);
            }
        }, null);
    }

    @Override
    public void onInterrupt() {

    }

    /**
     * 滑动直到控件显示后，触发点击事件
     *
     * @param text   查找的控件显示的内容
     * @param listId 滚动的容器id
     * @param num    触发控件的点击次数
     */
    public static void scroll2PositionClick(AccessibilityService service, String text, String listId, int num) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) { //必须android4.3以上的版本
            boolean isWorkOk = false;
            while (!isWorkOk) {
                AccessibilityNodeInfo rootInActiveWindow = service.getRootInActiveWindow(); //获取当前展示的窗口

                if (rootInActiveWindow != null) {
                    List<AccessibilityNodeInfo> item = rootInActiveWindow.findAccessibilityNodeInfosByText(text); //根据关键字查找某控件元素
                    List<AccessibilityNodeInfo> list = rootInActiveWindow.findAccessibilityNodeInfosByViewId(listId); //根据resource id 查找容器元素；判断关键字查找出的元素是否在该容器元素中；

                    if (item == null || item.size() == 0) { // 关键字元素不存在，则滚动容器元素
                        LogUtils.d(TAG, "不存在 " + text);
                        if (list != null && list.size() > 0) {
                            try {
                                Thread.sleep(2000); //隔200 ms 滚动一次
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            list.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD); //触发容器元素的滚动事件
                            LogUtils.d(TAG, "---- [ " + text + " ] 滚动查找中 ----");
                        }
                    } else {
                        LogUtils.d(TAG, "有存在 " + text);
                        isWorkOk = true;
                        AccessibilityNodeInfo clickableItem = item.get(0);
                        if (clickableItem.isEnabled() && clickableItem.isClickable()) { //关键字元素存在，则判断它是否可用，是否可点击
                            for (int i = 0; i < num; i++) {
                                clickableItem.performAction(AccessibilityNodeInfo.ACTION_CLICK); //触发点击 num 次

                                LogUtils.d(TAG, "点击: " + text);
                            }
                        } else {
                            AccessibilityNodeInfo parent = clickableItem.getParent(); //关键字元素不可用或者不可点击，则直接获取它的父元素
                            if (parent.isEnabled() && parent.isClickable()) {
                                for (int i = 0; i < num; i++) {
                                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);  //触发点击 num 次
                                    LogUtils.d(TAG, "点击parent: " + text);
                                }
                            }
                        }
                    }

                }
            }

        }
    }

    /**
     * 自动确认收货功能
     *
     * @param event
     * @throws Exception
     */
    @TargetApi(Build.VERSION_CODES.N)
    private void autoConfirmReceive(AccessibilityEvent event) throws Exception {
        Thread.sleep(2000);
        //点击订单中心的待收货按钮
        clickPoint(ConvertUtils.dp2px(247), ConvertUtils.dp2px(100));

        List<AccessibilityNodeInfo> list = event.getSource().findAccessibilityNodeInfosByText("确认收货");

        //确认了有待收货的订单需要处理
        if (null != list && list.size() != 0) {
            Thread.sleep(2000);
            //点击确认收货按钮
            clickPoint(ConvertUtils.dp2px(308), ConvertUtils.dp2px(500));
            Thread.sleep(1000);
            //点击dialog确认按钮
            clickPoint(ConvertUtils.dp2px(250), ConvertUtils.dp2px(385));
            Thread.sleep(2000);
            //点击返回
            clickPoint(ConvertUtils.dp2px(23), ConvertUtils.dp2px(57));
            Thread.sleep(1000);
            //点击dialog退出
            clickPoint(ConvertUtils.dp2px(200), ConvertUtils.dp2px(400));
        }
    }
}
