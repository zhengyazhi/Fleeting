package com.example.fleeting;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.fleeting.behavior.CalendarBehavior;
import com.example.fleeting.db.dbcontrol;
import com.example.fleeting.db.schedule;
import com.example.fleeting.left_menu.AboutActivity;
import com.example.fleeting.left_menu.TomatoWorkActivity;
import com.example.fleeting.left_menu.countActivity;
import com.example.fleeting.left_menu.diaryActivity;
import com.example.fleeting.left_menu.finishscheduleActivity;
import com.example.fleeting.left_menu.unfinishscheduleActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.onSlidingViewClickListener , CalendarView.OnDateChangeListener,OnDateSelectedListener {

    private static final DateFormat FORMATTER =SimpleDateFormat.getDateInstance();
    private TextView select_time;
    private Button today;
    private CalendarBehavior calendarBehavior;
    private int dayOfWeek;
    private int dayOfMonth;//日历

    private String string1=null;

    private FloatingActionButton fab;
    private DrawerLayout mDrawerLayout;//滑动菜单

    private RecyclerView mRecyclerView;
    //测试所需
    private List<schedule> schedules = new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCalendarView();//日历
        //数据初始化
        init();
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.draw_layout);

        today=findViewById(R.id.time);
        select_time=findViewById(R.id.select_time);
        select_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                datas();
            }
        });

        mRecyclerView = findViewById(R.id.recyclerview_main);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(this, schedules);
        //将适配的内容放入 mRecyclerView
        mRecyclerView.setAdapter(recyclerViewAdapter);
        datas();//插入数据
        //控制Item增删的动画，需要通过ItemAnimator  DefaultItemAnimator -- 实现自定义动画
        recyclerViewAdapter.notifyDataSetChanged();

        mDrawerLayout = findViewById(R.id.draw_layout);


        NavigationView leftView = findViewById(R.id.left_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(new DrawerArrowDrawable(getApplicationContext()));
            actionBar.setTitle("");
        }
        leftView.setCheckedItem(R.id.calendar);
        leftView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.draw_layout:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.tomatoButton://番茄
                        Intent intent = new Intent(MainActivity.this, TomatoWorkActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.count://统计
                        Intent intent4 = new Intent(MainActivity.this, countActivity.class);
                        startActivity(intent4);
                        break;

                    case R.id.diary://日记本
                        Toast.makeText(MainActivity.this,"日记本",Toast.LENGTH_SHORT).show();
                        Intent intent2=new Intent(MainActivity.this, diaryActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.setting://关于
                        Intent intent3 = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.finish:
                        Intent intent5 = new Intent(MainActivity.this, finishscheduleActivity.class);
                        startActivity(intent5);
                        break;
                    case R.id.unfinish:
                        Intent intent6 = new Intent(MainActivity.this, unfinishscheduleActivity.class);
                        startActivity(intent6);
                        break;
                    case R.id.contact:
                        Intent intent7 = new Intent(MainActivity.this, ContactActivity.class);
                        startActivity(intent7);


                }
                return true;
            }
        });

        //“添加日程”浮动按钮
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        //更新界面
        recyclerViewAdapter.setOnSlidListener(this);

    }

    private void initCalendarView()
    {

        MaterialCalendarView calendarView=findViewById(R.id.calendar);

        calendarView.setOnDateChangedListener(this);

        //隐藏标题栏和箭头
        //calendarView.setTopbarVisible(false);
        CoordinatorLayout.Behavior behavior =((CoordinatorLayout.LayoutParams) calendarView.getLayoutParams()).getBehavior();
        if (behavior instanceof CalendarBehavior) {
            calendarBehavior = (CalendarBehavior) behavior;
        }
        final Calendar calendar = Calendar.getInstance();
//当日选中
        calendarView.setSelectedDate(calendar.getTime());
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        setTitle((calendar.get(Calendar.MONTH) + 1) + "月");


    }

    public void init(){
        mRecyclerView=findViewById(R.id.recyclerview_main);
    }


    public void onItemClick(View view, int position) {
        //在这里可以做出一些反应（跳转界面、弹出弹框之类）
        schedule temp=schedules.get(position);

        Intent intent=new Intent(MainActivity.this,SechdueDetailActivity.class);
        intent.putExtra("schedule",temp);//点击传到一个界面
        startActivity(intent);
    }

    //点击删除按钮时，根据传入的 position 调用 RecyclerAdapter 中的 removeData() 方法
    @Override
    public void onDeleteBtnCilck(View view, int position) {
        recyclerViewAdapter.removeData(position);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.search_button://搜索按钮
            {
                Toast.makeText(this, "You clicked Search", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }break;
            case R.id.time:
            {
                MaterialCalendarView calendarView=findViewById(R.id.calendar);
                CoordinatorLayout.Behavior behavior =((CoordinatorLayout.LayoutParams) calendarView.getLayoutParams()).getBehavior();
                if (behavior instanceof CalendarBehavior) {
                    calendarBehavior = (CalendarBehavior) behavior;
                }
                final Calendar calendar = Calendar.getInstance();
//当日选中
                calendarView.setSelectedDate(calendar.getTime());

            }
            default:
        }
        return true;
    }
/*
    @Override//接收AddActivity中传回的新建日程的信息
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){

            if(resultCode==RESULT_OK){
                String textData = data.getStringExtra("textData");
                //    mTextView.setText(textData);  显示AddActivity传回的数据到哪里
            }
        }
    }*/


    public void datas(){
        String todays;
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        if(string1==null){
            todays=format.format(new java.util.Date());
        }
         else{
             todays=string1;
        }
        schedules= dbcontrol.findtody(todays);
        recyclerViewAdapter.notifyDataSetChanged(schedules);
        //暂时是显示全部的日期，以后显示当天的
    }
    protected void onResume(){
        super.onResume();
        datas();
    }

    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        if (date==null)
        {
            select_time.setText(null);
        }
        else
        {

            Date date1=date.getDate();
            select_time.setText(FORMATTER.format(date1));
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            string1=format.format(date1);
        }
    }
    /**
     * 监听日期改变函数
     * @param widget the view associated with this listener
     * @param date   the new date. May be null if selection was cleared
     */
    public void onDateChanged(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date) {
        if(date == null) {
            select_time.setText(null);
        }
        else {


            select_time.setText(FORMATTER.format(date.getDate()));
        }
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

    }
}
