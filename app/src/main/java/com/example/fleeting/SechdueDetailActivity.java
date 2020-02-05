package com.example.fleeting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fleeting.R;
import com.example.fleeting.db.dbcontrol;
import com.example.fleeting.db.schedule;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class SechdueDetailActivity extends AppCompatActivity {

    private TextView title;//主题
    private TextView remindline;//提醒日期
    private TextView content;//备注
    private TextView review;//重复
    private TextView color;//颜色
    private ImageButton delete;//删除事件
    private schedule temp;
    private ImageButton back;
    private Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sechdue_detail);

        title=findViewById(R.id.detail_alarm_title);
        remindline=findViewById(R.id.detail_alarm_remind);
        content=findViewById(R.id.detail_alarm_description);
        color=findViewById(R.id.detail_add_color);
        delete=findViewById(R.id.tv_delete);
        back=findViewById(R.id.left_alarm_back);
        ok = findViewById(R.id.detail_ok);

        Intent intent=getIntent();
        temp=(schedule)intent.getSerializableExtra("schedule");

        if(temp!=null) {

            title.setText(temp.getTag());
            remindline.setText(temp.getRemindline());
            content.setText(temp.getContent());
            color.setText(temp.getFlag());
        }
        else{

        }

        //保存按钮
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp.setRemiandline(remindline.getText().toString());
                temp.setContent(content.getText().toString());
                temp.setTag(title.getText().toString());
                temp.setFlag(color.getText().toString());
                temp.save();
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbcontrol.delete(temp);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp.save();
                finish();
            }
        });

    }
    @Override
    public void onBackPressed(){
        temp.save();
        super.onBackPressed();

    }

}
