package com.example.fleeting.utils;

import android.app.admin.DeviceAdminReceiver;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;

import com.example.fleeting.utils.AlarmService;

public class AlarmReceiver extends DeviceAdminReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1=new Intent(context, AlarmService.class);

        intent1.putExtra("delayTime", intent.getStringExtra("delaytime"));  //延时时间
        intent1.putExtra("tickerText", intent.getStringExtra("tickerText")); //不知道
        intent1.putExtra("contentTitle", intent.getStringExtra("contentText")); //通知标题
        intent1.putExtra("contentText", intent.getStringExtra("contentTitle"));  //通知内容

    /*    ComponentName componentName = new ComponentName("com.example.fleeting.utils","AlarmService");
        intent1.setComponent(componentName);*/


        context.startService(intent1);



    }
}
