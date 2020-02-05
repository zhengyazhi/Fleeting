package com.example.fleeting.db;

import com.example.fleeting.utils.MD5Utils;
import org.litepal.crud.LitePalSupport;
import java.lang.String;

public class admin extends LitePalSupport {
    private String user_name;//用户名
    private String password;//储存加密后的密码
    private Boolean loginflag;//用户的登录状态
    public admin(){
        loginflag=false;
    }
    public String getUser_name(){
        return user_name;
    }
    public void setUser_name(String user_name){
        this.user_name=user_name;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password= password;
    }
    public void setLoginflag(){
        loginflag=true;
    }
    public Boolean getLoginflag(){
        return loginflag;
    }
}
