package com.example.fleeting.db;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.Date;

public class schedule extends LitePalSupport implements Serializable {
    private long id;
    private String tag;//任务标签（例如：学习类，生活类）
    private String content;//任务的备注
    private String flag;//紧急色阶
    //设置紧急程度
    //1-红
    //2-黄
    //3-蓝
    //4-绿
    private String remindline;//提醒时间
    private int done;//任务是否完成


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public schedule(){
        done=0;
    }

    public String getTag(){
        return tag;
    }
    public void setTag(String tag){
        this.tag=tag;
    }

    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content=content;
    }


    public String getFlag(){
        return flag;
    }
    public void setFlag(String flag){
        this.flag=flag;
    }

    public String getRemindline(){
        return remindline;
    }
    public void setRemiandline(String remiandline){
        this.remindline=remiandline;
    }

    public int getDone(){
        return done;
    }
    public void setDone(){
        this.done=1;
    }
}
