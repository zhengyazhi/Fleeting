package com.example.fleeting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import com.example.fleeting.db.admin;
import com.example.fleeting.utils.MD5Utils;

import org.litepal.LitePal;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private Button btn_register;//注册按钮
    private EditText et_user_name,et_psw,et_psw_again;//编辑框
    private String userName,psw,pswAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init(){
        btn_register=findViewById(R.id.zhuce_button);
        et_user_name=findViewById(R.id.zcusername_edit);
        et_psw=findViewById(R.id.zcuserpassword_edit);
        et_psw_again=findViewById(R.id.reuserpassword_edit);

        //注册按钮
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName=et_user_name.getText().toString().trim();
                psw=et_psw.getText().toString().trim();
                pswAgain=et_psw_again.getText().toString().trim();
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(pswAgain)){
                    Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!psw.equals(pswAgain)){
                    Toast.makeText(RegisterActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                }else if (isExistUserName(userName)){
                    Toast.makeText(RegisterActivity.this, "此账户名已经存在", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    saveRegisterInfo(userName,psw);
                    //注册成功后把账号传递到LoginActivity.java中
                    // 返回值到loginActivity显示
                    Intent data=new Intent();
                    data.putExtra("username", userName);
                    setResult(RESULT_OK, data);
                    //RESULT_OK为Activity系统常量，状态码为-1，
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    RegisterActivity.this.finish();

                }

            }
        });
    }

    //判断是否注册的名字重复
    private boolean isExistUserName(String userName){
        String psw=null;
        List<admin> admins= LitePal.where("user_name=?",userName).find(admin.class);
        if(admins.isEmpty())
            return false;
        else
            return true;
    }

    //把新注册的用户信息保存在数据库
    private void saveRegisterInfo(String userName,String psw){
        String md5psw= MD5Utils.getMD5(psw);
        admin newman=new admin();
        newman.setUser_name(userName);
        newman.setPassword(md5psw);
        newman.save();
    }
}
