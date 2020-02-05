package com.example.fleeting.db;

import org.litepal.crud.LitePalSupport;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tomatonumber extends LitePalSupport {

    public static SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
    private String date;
    private int  time;

    public void setDate() {
        Date temp=new Date(System.currentTimeMillis());
        String dates=format.format(temp);
        this.date = dates;
    }

    public String getDate() {
        return date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
