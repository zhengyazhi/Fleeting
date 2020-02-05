package com.example.fleeting.utils;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.example.fleeting.R;

public class TomatoView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint timePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mColor = Color.parseColor("#000000"); //灰色底色#D1D1D1
    private int centerX;
    private int centerY;
    private int radius;
    private RectF mRectF = new RectF();
    public static final float START_ANGLE = -90;
    public static final int MAX_TIME = 60;   //最大分钟值
    private float sweepVelocity ; //旋转速度，圆弧扫过的角度
    private String textTime = "25:00";
    //分钟
    private int time=25;
    //倒计时
    private int countdownTime=25*60;  //倒计时
    private int getTime1;
    private float touchX;
    private float touchY;
    private float offsetX;
    private float offsetY;
    private boolean isStarted;
    private boolean isfinish=false;

    //判断是否结束番茄工作
    public boolean iffinish()
    {
        return isfinish;
    }
    public TomatoView(Context context) {
        super(context);
    }

    public TomatoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TomatoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static float dpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }//适应屏幕大小

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        centerX = width / 2;
        centerY = height / 2;
        radius = (int) dpToPixel(100);
        setMeasuredDimension(width, height);//继承View，实现自己想要的组件，那这个方法决定了当前View的大小

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        //白圆
        canvas.save();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dpToPixel(2));
        canvas.drawCircle(centerX, centerY, radius, mPaint);//在画布上绘制圆形 圆心坐标+半径+画笔
        canvas.restore();
        //灰圆
        canvas.save();
        mPaint.setColor(mColor);
        canvas.drawArc(mRectF, START_ANGLE, 360 * sweepVelocity, false, mPaint);//绘制灰色圆弧,绘制速度
        canvas.restore();
        //时间数字
        canvas.save();
        timePaint.setColor(Color.WHITE);
        timePaint.setStyle(Paint.Style.FILL);
        timePaint.setTextSize(dpToPixel(40));
        canvas.drawText(textTime, centerX - timePaint.measureText(textTime) / 2,
                centerY - (timePaint.ascent() + timePaint.descent()) / 2, timePaint);
        canvas.restore();
    }

    //触摸选择时间
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isStarted) {
            return true;
        }
        float x = event.getX();
        float y = event.getY();
        boolean isContained = isContained(x, y);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (isContained) {
                    touchX = x;
                    touchY = y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isContained) {
                    offsetX = x - touchX;
                    offsetY = y - touchY;
                    time = (int) (offsetY / 2 / radius * MAX_TIME);
                    if (time <= 0) {
                        time = 0;
                    }
                    textTime = formatTime(time);
                    countdownTime = time * 60;
                    getTime1=countdownTime;
                    invalidate();//重绘刷新
                }
                break;
        }
        return true;
    }

    //时间转换
    private boolean isContained(float x, float y) {
        if (Math.sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY)) > radius) {
            return false;
        } else {
            return true;
        }
    }

    //控制时间分钟显示
    private String formatTime(int time) {
        StringBuilder sb = new StringBuilder();
        if (time < 10) {
            sb.append("0" + time + ":00");
        } else {
            sb.append(time + ":00");
        }
        return sb.toString();
    }

    //可变字符串显示倒计时时间
    private String formatCountdownTime(int countdownTime) {
        StringBuilder sb = new StringBuilder();
        int minute = countdownTime / 60;
        int second = countdownTime - 60 * minute;
        if (minute < 10) {
            sb.append("0" + minute + ":");
        } else {
            sb.append(minute + ":");
        }
        if (second < 10) {
            sb.append("0" + second);
        } else {
            sb.append(second);
        }
        return sb.toString();
    }

    public void start(){
        isfinish=false;
        if (countdownTime == 0 || isStarted) {
            return;
        }
        isStarted = true;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.0f);//动画
        valueAnimator.setDuration(countdownTime * 1000);//设置属性动画时长
        valueAnimator.setInterpolator(new LinearInterpolator());
        //valueAnimator添加更新监听事件
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            // 启动动画之后, 会不断回调此方法来获取最新的值
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                sweepVelocity = (float) animation.getAnimatedValue();
                mColor = Color.parseColor("#000000");
                invalidate();//重绘刷新
            }
        });
        valueAnimator.start();//开启属性动画

        new CountDownTimer(countdownTime * 1000 + 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTime = (countdownTime * 1000 - 1000) / 1000;
                textTime = formatCountdownTime(countdownTime);
                invalidate();//重绘刷新
            }

            //倒计时结束动作
            @Override
            public void onFinish() {
                mColor = Color.RED;
                sweepVelocity = 0;
                isStarted = false;
                invalidate();//重绘刷新
                isfinish=true;

            }
        }.start();
    }

    public int getCountdownTime(){

        return getTime1;
    }




}
