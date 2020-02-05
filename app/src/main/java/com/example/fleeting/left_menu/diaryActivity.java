package com.example.fleeting.left_menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.fleeting.DetailContentActivity;
import com.example.fleeting.R;
import com.example.fleeting.db.diarycontent;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class diaryActivity extends AppCompatActivity {

    Button writediary;
    public final int WRITEDNEWDIART = 4;
    private List<String> dcontent=new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        writediary=findViewById(R.id.write_diary);
        writediary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(diaryActivity.this, adddiary.class);
                startActivityForResult(intent,WRITEDNEWDIART);
            }
        });
        listView=findViewById(R.id.textview_showdiary);

        final List<diarycontent> diarycontents=LitePal.findAll(diarycontent.class);

        for(int i=0;i<diarycontents.size();i++){
            dcontent.add(diarycontents.get(i).get_TitleAndDate());
        }
        arrayAdapter=new ArrayAdapter<String>(diaryActivity.this,android.R.layout.simple_list_item_1,dcontent);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                diarycontent temp=diarycontents.get(position);
                Intent intent=new Intent(diaryActivity.this, DetailContentActivity.class);
                intent.putExtra("diary",temp);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==WRITEDNEWDIART){
            recreate();
        }
    }
}
