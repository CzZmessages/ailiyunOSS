package com.lenkeng.videoads.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;

import java.lang.reflect.Field;

/**
 * @author $
 * @version 1.0
 * @description: TODO
 * @date $ $
 */
public class SpanTextView extends AppCompatTextView {
    private static final String TAG = "SpanTextView";

    public SpanTextView(Context context) {
        super(context);
    }

    public SpanTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpanTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setSingleLine();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    /**
     * 利用反射 设置跑马灯的速度
     * 在onLayout中调用即可
     *
     * @param newSpeed 新的速度
     */
    @SuppressLint("PrivateApi")
    public void setMarqueeSpeed(float newSpeed) {
        try {
            // 获取走马灯配置对象
            Class tvClass = Class.forName("android.widget.TextView");
            Field marqueeField = tvClass.getDeclaredField("mMarquee");
            marqueeField.setAccessible(true);
            Object marquee = marqueeField.get(this);
            if (marquee == null) {
                return;
            }
            // 设置新的速度
            Class<?> marqueeClass = marquee.getClass();

            // 速度变量的名称可能与此示例的不相同 可自行打印查看
//            for (Field field : marqueeClass.getDeclaredFields()) {
//                Log.d(TAG, field.getName());
//            }
//            int currentApiVersion = Build.VERSION.SDK_INT;
//            LogUtils.e("Android 版本号:"+currentApiVersion);
            // SDK28中的是mPixelsPerMs，但我的开发机是下面的名称
            Field speedField = marqueeClass.getDeclaredField("mPixelsPerSecond");
            speedField.setAccessible(true);
            float orgSpeed = (float) speedField.get(marquee);
            // 这里设置了相对于原来的20倍
            speedField.set(marquee, newSpeed);
            Log.d(TAG, "setMarqueeSpeed: " + orgSpeed);
            Log.d(TAG, "setMarqueeSpeed: " + newSpeed);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            Log.e(TAG, "setMarqueeSpeed1: 设置跑马灯速度失败", e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "setMarqueeSpeed2: 设置跑马灯速度失败", e);
            e.printStackTrace();
        }
    }

    private void init() {
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setSelected(true);


    }

}
