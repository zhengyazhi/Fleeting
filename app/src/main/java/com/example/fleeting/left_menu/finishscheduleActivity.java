package com.example.fleeting.left_menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.fleeting.R;
import com.example.fleeting.RecyclerViewAdapter;
import com.example.fleeting.SechdueDetailActivity;
import com.example.fleeting.db.dbcontrol;
import com.example.fleeting.db.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class finishscheduleActivity extends AppCompatActivity implements RecyclerViewAdapter.onSlidingViewClickListener {

    private RecyclerView mRecyclerView;
    public final int WRITEDNEWDIART = 4;
    private List<schedule> schedules=new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finishschedule);

        mRecyclerView=findViewById(R.id.recyclerview_finish);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter=new RecyclerViewAdapter(this,schedules);
        //将适配的内容放入 mRecyclerView
        mRecyclerView.setAdapter(recyclerViewAdapter);
        datas();
        recyclerViewAdapter.notifyDataSetChanged(schedules);
        recyclerViewAdapter.setOnSlidListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        schedule temp=schedules.get(position);

        Intent intent=new Intent(finishscheduleActivity.this, SechdueDetailActivity.class);
        intent.putExtra("schedule",temp);//点击传到一个界面
        startActivityForResult(intent,WRITEDNEWDIART);
    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        recyclerViewAdapter.removeData(position);
    }

    public void datas(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String today=format.format(new java.util.Date());
        schedules= dbcontrol.finishse(today);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==WRITEDNEWDIART){
            recreate();
        }
    }
}
