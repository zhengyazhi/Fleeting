package com.example.fleeting.db;

import android.graphics.ColorSpace;
import android.widget.Toast;

import com.example.fleeting.MainActivity;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class dbcontrol {
    public static long staticid=0;



    //返回当日日程
    public static List<schedule> findtody(String today){

        return LitePal.where("remindline like ? and done = ?",today+"%","0").find(schedule.class);
    }

    //搜索
    public static List<schedule> find(String content){
        return LitePal.where("content like  ? or tag like ?","%"+ content + "%","%"+ content + "%").find(schedule.class);
    }

    public static  List<schedule> findbyid(long i){

        return LitePal.where("id = ?",i+"").find(schedule.class);
    }

    //添加日程
    public static boolean add(schedule temp){
        temp.setId(staticid++);
        return temp.save();
    }

    //更新日程
    public static boolean update(schedule temp){
        return temp.save();
    }

    //删除日程
    public static boolean delete(schedule temp){
        long id=temp.getId();
        LitePal.deleteAll(schedule.class,"id=?",""+id);
        return true;
    }

    //查询全部番茄数量
    public static int findnumbers(){
        List<Tomatonumber> tomatonumberList=new ArrayList<>();
        tomatonumberList=LitePal.findAll(Tomatonumber.class);
        int numbers=0;
        if(tomatonumberList.size()!=0){
            numbers=tomatonumberList.size();
        }
        return numbers;
    }
    //查询当天番茄数量
    public static int findtodaynumbers(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
        String today=format.format(new java.util.Date());
        List<Tomatonumber> tomatonumberList=new ArrayList<>();
        tomatonumberList=LitePal.where("date like ?",today+"%").find(Tomatonumber.class);
        int numbers=0;
        if(tomatonumberList.size()!=0){
            numbers=tomatonumberList.size();
        }
        return numbers;
    }

    //找到过去完成的(改动）
    public static List<schedule> finishse(String today){

        return LitePal.where("remindline < ? and done = ?" ,today+"%","1").find(schedule.class);
    }
    //找到过去未完成的
    public static List<schedule>unfinishse(String today){
        return LitePal.where("remindline < ? and done = ?",today+"%","0").find(schedule.class);
    }

    //当天完成的日程数
    public static int finish_tody_schedule(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date temp = new Date(System.currentTimeMillis());
        String date = format.format(temp);
        List<schedule> list=new ArrayList<>();
        list=LitePal.where("remindline like ? and done == ? " ,date+"%","1").find(schedule.class);
        return  list.size();
    }
    //返回总的完成日程数
    public static int finish_sum_schedule(){
        List<schedule> list=new ArrayList<>();
        list=LitePal.where("done == ? " ,"1").find(schedule.class);
        return  list.size();
    }
    //返回总的未完成数
    public static int unfinish_sum_schedule(){
        List<schedule> list=new ArrayList<>();
        list=LitePal.where("done == ? " ,"0").find(schedule.class);
        return  list.size();
    }

    //返回当天得到的番茄时间
    public static int today_tomatotimes(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
        Date temp = new Date(System.currentTimeMillis());
        String dates = format.format(temp);
        List<Tomatonumber> list=LitePal.where("date like ?",dates+"%").find(Tomatonumber.class);
        int sum=0;
        for(Tomatonumber i:list){
            sum+=i.getTime();
        }
        return sum/10;
    }

    //返回总的获取番茄时间
    public static int sum_tomatotimes() {
        List<Tomatonumber> list = LitePal.findAll(Tomatonumber.class);
        int sum = 0;
        for (Tomatonumber i : list) {
            sum += i.getTime();
        }
        return sum/10;
    }

}
