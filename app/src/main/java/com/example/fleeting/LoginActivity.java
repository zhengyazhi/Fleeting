package com.example.fleeting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import java.util.List;

import com.example.fleeting.db.*;
import com.example.fleeting.utils.MD5Utils;

import org.litepal.LitePal;

public class LoginActivity extends AppCompatActivity {
    private Button btn_login,btn_register;//登录按钮,注册按钮
    private String username,psw,spPsw;//获取用户名，密码，加密密码
    private EditText et_user_name,et_psw;//编辑框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toast.makeText(LoginActivity.this,"进入登录界面",Toast.LENGTH_SHORT).show();
        LitePal.getDatabase();
        init();
    }

    private void init(){
        et_user_name= findViewById(R.id.username_edit);
        et_psw=findViewById(R.id.userpassword_edit);
        btn_register= findViewById(R.id.register);
        btn_login=findViewById(R.id.login_button);

        //注册按钮
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1);
            }
        });

        //登录按钮
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username=et_user_name.getText().toString().trim();
                psw=et_psw.getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                //先对当前的用户输入的密码进行MD5加密有在进行对比判断
                String md5psw= MD5Utils.getMD5(psw);

                List<admin> adminList=LitePal.where("user_name=?",username).find(admin.class);
                spPsw=adminList.get(0).getPassword();


                if(md5psw.equals(spPsw)){
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //保存登录状态，在界面保存登录的用户名 定义个方法 saveLoginStatus boolean 状态 , userName 用户名;
                    saveLoginStatus(username);

                    //销毁登录界面
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivityForResult(intent,1);

                    LoginActivity.this.finish();
                    //跳转到主界面，登录成功的状态传递到 MainActivity 中


                }else if((spPsw!=null&&!TextUtils.isEmpty(spPsw)&&!md5psw.equals(spPsw))){
                    Toast.makeText(LoginActivity.this, "输入的用户名和密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(LoginActivity.this, "此用户名不存在", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

    }

    //保存登录的状态
    private void saveLoginStatus(String username){
        List<admin> admina= LitePal.where("user_name=?",username).find(admin.class);
        admina.get(0).setLoginflag();
    }

    //在注册成功之后直接在登录界面填上用户名，在
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    String username = data.getStringExtra("username");
                    if (!TextUtils.isEmpty(username)) {
                        //设置用户名到 et_user_name 控件
                        et_user_name.setText(username);
                        //et_user_name控件的setSelection()方法来设置光标位置
                        et_user_name.setSelection(username.length());
                    }
                }
                break;
            default:
        }

    }



}
