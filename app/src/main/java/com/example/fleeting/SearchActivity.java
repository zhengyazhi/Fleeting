package com.example.fleeting;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fleeting.db.dbcontrol;
import com.example.fleeting.db.schedule;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements RecyclerViewAdapter.onSlidingViewClickListener{

    private EditText mEditText; //搜索文本框
    private ImageView mImageView; //一键删除
    private RecyclerView mRecyclerView;  //列表框
    private TextView mTextView;  //返回按钮
    private List<schedule> schedules=new ArrayList<>();
    RecyclerViewAdapter adapter;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context=this;
        initView();   //构造函数

        mRecyclerView=findViewById(R.id.recyclerview_main);
        mRecyclerView.setVisibility(View.VISIBLE);


        //从结果集获得time的list数组

        //线性布局
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(context,schedules);

        mRecyclerView.setAdapter(adapter);//传入适配器
        adapter.setOnSlidListener(this);

    }

    private void initView(){

        mEditText = (EditText) findViewById(R.id.edittext);
        mImageView = (ImageView) findViewById(R.id.imageview);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        mTextView = (TextView) findViewById(R.id.textview);

        //设置一键删除的点击事件
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把EditText内容设置为空
                mEditText.setText("");
                //把ListView隐藏
                mRecyclerView.setVisibility(View.GONE);
            }
        });


        //EditText添加监听
        mEditText.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}//文本改变之前执行

            @Override
            //文本改变的时候执行检查，若文本框为空则隐藏“一键删除”
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果长度为0
                if (s.length() == 0) {
                    //隐藏“一键删除”图片
                    mImageView.setVisibility(View.GONE);
                } else {//长度不为0
                    //显示“一键删除”图片
                    mImageView.setVisibility(View.VISIBLE);
                    //获得输入的内容
                    String str = mEditText.getText().toString();

                    schedules = dbcontrol.find(str);//从数据库搜索日程
                    //显示RecyclerView
                    adapter.notifyDataSetChanged(schedules);

                }
            }

            public void afterTextChanged(Editable s) { }//文本改变之后执行
        });

        mTextView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //如果输入框内容为空，提示请输入搜索内容
                finish();

            }
        });


    }


    @Override
    public void onItemClick(View view, int position) {
        schedule temp=schedules.get(position);
        Intent intent=new Intent(SearchActivity.this,SechdueDetailActivity.class);
        intent.putExtra("schedule",temp);//点击传到一个界面
        startActivity(intent);
    }



    @Override
    public void onDeleteBtnCilck(View view, int position) {
        adapter.removeData(position);
    }

}
