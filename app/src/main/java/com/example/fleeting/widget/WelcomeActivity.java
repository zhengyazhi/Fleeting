package com.example.fleeting.widget;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.fleeting.MainActivity;
import com.example.fleeting.R;

public class WelcomeActivity extends AppCompatActivity {

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            goMain();
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        handler.sendEmptyMessageDelayed(0,2000);
    }

    private void goMain(){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);  //将控制权交给MainActivity
        finish();   //结束
    }
}
