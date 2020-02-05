package com.example.fleeting.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;

import com.example.fleeting.AddActivity;
import com.example.fleeting.MainActivity;
import com.example.fleeting.R;
import com.example.fleeting.SechdueDetailActivity;
import com.example.fleeting.db.schedule;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;



public class AlarmService extends Service {
    static Timer timer = null;

    // 清除通知
    public static void cleanAllNotification() {
        NotificationManager mn = (NotificationManager) AddActivity
                .getContext().getSystemService(NOTIFICATION_SERVICE);
        mn.cancelAll();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    public void onCreate(Intent intent) {
        Log.e("addNotification", "===========create=======");
        //    addNotification(60*1000,"tick2","title2","text2");
        startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(final Intent intent, int flags, int startId) {

        long period = 24 * 60 * 60 * 1000; // 24小时一个周期
        //    int delay = intent.getIntExtra("delayTime", 0);  //如果没有取到"delayTime"的值，则默认返回0.
        //    Log.d("AlarmService","!!!!!!!!延迟时间为："+delay);
        int delay = 0;
        if (null == timer) {
            timer = new Timer();
        }
        //调用定时器方法，传入TimerTask,延时时间delay, 周期period 也可只传入Date
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                NotificationManager mn = (NotificationManager) AlarmService.this
                        .getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(
                        AlarmService.this);


                Intent notificationIntent = new Intent(AlarmService.this,
                        MainActivity.class);// 点击跳转位置 点击通知后跳转至添加界面（暂时的）
                //        notificationIntent.putExtra("schedule",(schedule)intent.getSerializableExtra("schedule"));

                PendingIntent contentIntent = PendingIntent.getActivity(
                        AlarmService.this, 0, notificationIntent, 0);
                builder.setContentIntent(contentIntent);
                builder.setSmallIcon(R.drawable.app);// 设置通知图标(不知道用什么好，暂时先这个代替)
                builder.setTicker(intent.getStringExtra("tickerText")); // 测试通知栏标题
                builder.setContentText(intent.getStringExtra("contentText")); // 下拉通知啦内容
                builder.setContentTitle(intent.getStringExtra("contentTitle"));// 下拉通知栏标题
                builder.setAutoCancel(true);// 点击弹出的通知后,让通知将自动取消
                //        builder.setVibrate(new long[] { 0, 2000, 1000, 4000 }); // 震动需要真机测试-延迟0秒震动2秒延迟1秒震动4秒
                //        builder.setSound(Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                //         "5"));//获取Android多媒体库内的铃声
                //        builder.setSound(Uri.parse(new File("file:///Fleeting/app/src/main/assets/lingling1.mp3").toString()))
                //         ;//自定义铃声
                builder.setDefaults(Notification.DEFAULT_ALL);// 设置使用系统默认声音
                // builder.addAction("图标", title, intent); //此处可设置点击后 打开某个页面，暂时先留着
                Notification notification = builder.build();
                notification.flags = notification.FLAG_INSISTENT;//让声音、振动无限循环，直到用户响应 （取消或者打开）
                mn.notify((int) System.currentTimeMillis(), notification);
            }
        }, delay, period);
        //period为一天后再调用一次run方法，周期为一天24 * 60 * 60 * 1000


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("addNotification", "===========destroy=======");
        super.onDestroy();
    }
}
