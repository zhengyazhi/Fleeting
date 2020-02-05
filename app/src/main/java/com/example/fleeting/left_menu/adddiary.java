package com.example.fleeting.left_menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fleeting.R;
import com.example.fleeting.db.diarycontent;

public class adddiary extends AppCompatActivity {

    private Button write;
    private EditText title;
    private EditText content;

    private ImageButton backb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddiary);

        title=findViewById(R.id.diary_title);

        content=findViewById(R.id.note_content);

        write=findViewById(R.id.write_done);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().equals(" ")&&content.getText().toString().equals(" ")){
                    Toast.makeText(adddiary.this,"没有保存日记哦！",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    diarycontent temp=new diarycontent();
                    temp.setContent(content.getText().toString());
                    temp.settitle(title.getText().toString());
                    temp.save();
                    finish();
                }
            }
        });
        backb=findViewById(R.id.edit_add_diary);
        backb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(adddiary.this,"没有添加日记！",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
