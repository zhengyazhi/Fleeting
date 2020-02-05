package com.example.fleeting.left_menu;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fleeting.R;
import com.example.fleeting.db.Tomatonumber;
import com.example.fleeting.utils.TomatoView;
import com.example.fleeting.widget.RippleView;

public class TomatoWorkActivity extends AppCompatActivity implements RippleView.RippleStateListener {

    TomatoView clockView;
    TextView textView;
    private RippleView mRippleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_work);

        //流星
        WebView webView=findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/star.html");

        //水波纹
        mRippleView=(RippleView)findViewById(R.id.root_tv);
        mRippleView.setRippleStateListener(this);


        clockView=new TomatoView(this);
        clockView = findViewById(R.id.clockView);
        textView = findViewById(R.id.tv_start);
        textView.setShadowLayer(12,0,0, Color.WHITE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开始按钮
                mRippleView.startRipple();
                if(textView.getText().toString()!="结束"){
                    textView.setText("结束");
                    clockView.start();
                    Toast.makeText(TomatoWorkActivity.this,"开启番茄工作法",Toast.LENGTH_SHORT).show();
                }
                //结束按钮
                else{
                    if(clockView.iffinish()){
                        Toast.makeText(TomatoWorkActivity.this,"太棒了，完成了",Toast.LENGTH_SHORT).show();
                        //在此记录一下番茄工作法的次数和番茄时间，数据库
                        Tomatonumber tomatonumber = new Tomatonumber();
                        tomatonumber.setDate();
                        tomatonumber.setTime(clockView.getCountdownTime());
                        tomatonumber.save();
                        finish();
                    }
                    else{
                        AlertDialog alertDialog = new AlertDialog.Builder(TomatoWorkActivity.this)
                                .setTitle("确定退出吗？")
                                .setMessage("退出番茄工作法后，本次番茄计时将失败")
                                .setPositiveButton("结束", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        textView.setText("开始");
                                        finish();
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        return;
                                    }
                                }).create();
                        alertDialog.show();
                    }
                }
            }
        });
    }
    public void tos(){
        Toast.makeText(TomatoWorkActivity.this,"太棒了，完成了",Toast.LENGTH_SHORT).show();

    }
    @Override
    public void startRipple()
    {
    }

    @Override
    public void stopRipple() {

    }

    @Override
    public void onRippleUpdate(ValueAnimator animation) {

    }
}
