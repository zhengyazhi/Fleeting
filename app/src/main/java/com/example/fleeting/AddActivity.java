package com.example.fleeting;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.fleeting.R;

import com.example.fleeting.datepicker.*;
import com.example.fleeting.datepicker.CustomDatePicker;
import com.example.fleeting.db.dbcontrol;
import com.example.fleeting.db.schedule;
import com.example.fleeting.utils.AlarmReceiver;
import com.example.fleeting.utils.AlarmService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class AddActivity extends AppCompatActivity implements View.OnClickListener{

    //对应各个控件
    private boolean isAllDay = false;//全日默认否
    private RelativeLayout layout_add_replay;
    private RelativeLayout layout_add_remind;
    private RelativeLayout layout_add_color;
    private RelativeLayout layout_add_sort;
    private TextView mTvSelectedTime;
    private CustomDatePicker mTimerPicker;
    private ImageButton reback; //返回按钮
    private TextView yes;  //确定保存按钮
    private TextView add_color;

    private TextView flaga;
    private EditText tag;
    private TextView remind;
    private EditText content;


    String tickerText ;
    String contentTitle;
    String contentText ;

    //声明一个AlertDialog构造器
    private AlertDialog.Builder builder;

    private static Context sContext = null;//传过去闹钟提醒类作为参数

    private String NotificationDate; //提醒日期+时间

    AlarmManager am;

    //传过去闹钟提醒类作为参数
    public static Context getContext() {
        return sContext;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        add_color=findViewById(R.id.add_color);

        sContext = this;//传过去闹钟提醒类作为参数
        am = (AlarmManager)getSystemService(ALARM_SERVICE);
    //    Delaytime = (TextView)findViewById(R.id.textView9);

        //实例化控件

        layout_add_remind= (RelativeLayout) findViewById(R.id.layout_add_remind);
        layout_add_color= (RelativeLayout) findViewById(R.id.layout_add_color);
        findViewById(R.id.add_remind).setOnClickListener(this);

        tag=findViewById(R.id.content);
        content=findViewById(R.id.add_description);
        flaga=findViewById(R.id.add_color);

        findViewById(R.id.add_remind).setOnClickListener(this);
        mTvSelectedTime = findViewById(R.id.add_remind);//用户选择的提醒时间
        reback = findViewById(R.id.edit_add); //返回按钮
        yes = findViewById(R.id.ok_add); //确定保存按钮
        remind=findViewById(R.id.add_remind);

        initTimerPicker();//私有函数
        //监听点击事件
        layout_add_remind.setOnClickListener(this);
        layout_add_color.setOnClickListener(this);
        reback.setOnClickListener(this);
        yes.setOnClickListener(this);
        findViewById(R.id.layout_add_remind).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.layout_add_remind:
                //add_remind中显示时间
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(mTvSelectedTime.getText().toString());
                break;
            case R.id.layout_add_color:
                showColorDialog(v);
                break;
            case R.id.edit_add: //返回
                Toast.makeText(AddActivity.this,"取消保存",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.ok_add:  //保存退出
                if(tag.getText().toString().equals("")&&content.getText().toString().equals("")){
                    Toast.makeText(AddActivity.this,"请输入的标题",Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    schedule temp = new schedule();

                    temp.setTag(tag.getText().toString());
                    temp.setContent(content.getText().toString());
                    temp.setRemiandline(remind.getText().toString());
                    temp.setFlag(flaga.getText().toString());
                    tickerText = content.getText().toString();
                    contentTitle = content.getText().toString();
                    contentText =tag .getText().toString();



                    dbcontrol.add(temp);
                    int IDnumber =(int) temp.getId();
                    Toast.makeText(AddActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
            //        Toast.makeText(AddActivity.this,"保存成功"+IDnumber,Toast.LENGTH_SHORT).show();
                    if(mTvSelectedTime.getText().toString()!=null){
                        startremind(IDnumber);//保存退出时若检测到闹钟提醒框里的日期时间不为空，则启动 通知函数
                    }
                    finish();

                    break;
                }
        }
    }

    public void startremind(int IDnumber){
        //从控件获取提醒日期和时间
        NotificationDate = mTvSelectedTime.getText().toString();
        //判断用户有无都选好了日期和时间
        if(TextUtils.isEmpty(NotificationDate)){
            Toast.makeText(getApplicationContext(), "请选择提醒日期与时间", Toast.LENGTH_SHORT).show();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); //设置日期时间的格式
        Date date;
        long value = 0; //用户选择的提醒时间
        String str_date = NotificationDate;//+" "+NotificationTime;//进一步格式化日期时间使得其跟"yyyy-MM-dd HH:mm:ss"一样
        try {
            date = sdf.parse(str_date);//按照样式格式化日期
            value = date.getTime();//获得已经格式化的日期
            System.out.println("当前设置时间:"+value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("str_date=",str_date);
        Log.e("value=",value+"");
        long value2 = System.currentTimeMillis();//获得当前系统的时间
        //判断若用户选择的时间小于系统时间则提示
        if(value<=value2){
            Toast.makeText(getApplicationContext(), "选择时间不能小于当前系统时间", Toast.LENGTH_SHORT).show();
            return;
        }
        int delayTime = (int)(value-value2); //提醒时间-当前时间=此刻开始延迟提醒时间
        Intent intent = new Intent(AddActivity.this, AlarmReceiver.class);
        intent.putExtra("delayTime", delayTime);  //延时时间
        intent.putExtra("tickerText", tickerText); //不知道
        intent.putExtra("contentTitle", contentTitle); //通知标题
        intent.putExtra("contentText", contentText);  //通知内容
        PendingIntent pi = PendingIntent.getBroadcast(AddActivity.this,IDnumber,intent,0);
        Toast.makeText(AddActivity.this,"设置提醒成功",Toast.LENGTH_SHORT).show();
        am.set(AlarmManager.RTC_WAKEUP, value, pi);
        //设置闹钟;
    }



    private void showColorDialog(View view) {
        builder=new AlertDialog.Builder(this);


        /**
         * 点击重复跳出的alerdialog
         */
        final String[] coloritems={"紧急","一般","不紧急"};
        builder.setSingleChoiceItems(coloritems, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "You clicked "+coloritems[i], Toast.LENGTH_SHORT).show();
                //Toast.makeText(AddActivity.this, coloritems[i], Toast.LENGTH_SHORT).show();
               add_color.setText(coloritems[i]);
        //       layout_add_color.setTag(coloritems[i]);
            }
        });
       builder.show();
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
       //dialog.show();
        dialog.dismiss();
    }

    private void initTimerPicker() {
        String beginTime = "1970-01-01 00:00";//设置起始时间
        String endTime = "2030-12-31 00:00";//设置结束时间
        String currentTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);



        mTvSelectedTime.setText(currentTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedTime.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }
}
