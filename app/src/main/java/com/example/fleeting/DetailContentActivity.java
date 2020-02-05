package com.example.fleeting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fleeting.db.diarycontent;

public class DetailContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_content);

        Intent intent=getIntent();
        diarycontent temp=(diarycontent)intent.getSerializableExtra("diary");

        TextView textView=findViewById(R.id.textview_detail);
        textView.setText(temp.toString());
        temp.save();
    }
}
