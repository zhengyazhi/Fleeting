package com.example.fleeting.left_menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fleeting.Bean.CakeValue;
import com.example.fleeting.R;
import com.example.fleeting.db.dbcontrol;
import com.example.fleeting.view.CakeSurfaceView;
import com.example.fleeting.view.PieChartView;

import java.util.ArrayList;
import java.util.List;

public class countActivity extends AppCompatActivity {
    private TextView today_tomatotimes;
    private TextView sum_tomatotimes;
    private TextView today_tomatos;
    private TextView sum_tomatos;
    private TextView today_schedules;
    private TextView sum_schedules;
    private CakeSurfaceView pieChart;
    private PieChartView pieChartNow;
    private int unfinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        today_tomatotimes=findViewById(R.id.today_tomato_time);
        sum_tomatotimes=findViewById(R.id.sum_tomato_time);
        today_tomatos=findViewById(R.id.today_sum_tomato);
        sum_tomatos=findViewById(R.id.sum_tomato);
        today_schedules=findViewById(R.id.today_sum_task);
        sum_schedules=findViewById(R.id.sum_task);

        String today_time=dbcontrol.today_tomatotimes()+"";
        today_tomatotimes.setText(today_time);
        sum_tomatotimes.setText(dbcontrol.sum_tomatotimes()+"");
        today_tomatos.setText(dbcontrol.findtodaynumbers()+"");
        sum_tomatos.setText(dbcontrol.findnumbers()+"");
        today_schedules.setText(dbcontrol.finish_tody_schedule()+"");
        sum_schedules.setText(dbcontrol.finish_sum_schedule()+"");
        pieChart = (CakeSurfaceView)findViewById(R.id.assets_pie_chart);
        unfinish=dbcontrol.unfinish_sum_schedule();
        initData();
    }

    private void initData() {
        List<CakeValue> itemBeanList = new ArrayList<>();
        itemBeanList.add(new CakeValue("未完成日程",unfinish,"#cccccc"));
        itemBeanList.add(new CakeValue("已完成日程",dbcontrol.finish_sum_schedule(),"#ff4040"));

        pieChart.setData(itemBeanList);


    }

}

