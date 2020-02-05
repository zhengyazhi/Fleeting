package com.example.fleeting.db;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class diarycontent extends LitePalSupport implements Serializable {
    public static SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日   HH:mm");
    //内容
    private String content=new String();
    //标题
    private String title=new String();
    //日期
    private String date;


    public diarycontent(){
        setDate();
    }
    public String getTitle(){
        return title;
    }
    public void settitle(String title){
        this.title=title;
    }

    public String getContent(){
        return  content;
    }
    public void setContent(String content){
        this.content=content;
    }
    public String getDate(){
        return date;
    }
    public void setDate(){
        Date temp = new Date(System.currentTimeMillis());
        String date = format.format(temp);
        this.date=date;
    }
    public String get_TitleAndDate(){
        return date + '\n' +  title;
    }
    public String toString() {
        return "日期： "+date + '\n' + "主题： " + title +"\n\n"+ "内容：\n"+content;
    }


}
