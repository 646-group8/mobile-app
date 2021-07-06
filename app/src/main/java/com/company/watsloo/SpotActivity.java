package com.company.watsloo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SpotActivity extends AppCompatActivity {

    private TextView spot_title;
    private TextView spot_content;
    private ListView listView;
    ArrayList<String> story_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);
        spot_title= findViewById(R.id.spotTitleText);
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        spot_title.setText(title);

        // spot stories
//        spot_content = findViewById(R.id.spotContent);
//        String content = "Story1' tilte\nStory2's title\ntest";
//        spot_content.setText(content);
//        spot_content.setMovementMethod(ScrollingMovementMethod.getInstance());


        story_list.add("story1");
        story_list.add("story2"); // 后续取数据库数据
        listView = findViewById(R.id.spotList);
        ArrayAdapter<String> adapter = new ArrayAdapter( this,R.layout.support_simple_spinner_dropdown_item,story_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public  void  onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intentDetail = new Intent();
                intentDetail.setClass(SpotActivity.this, DetailActivity.class);
                startActivity(intentDetail);
            }

        });


        //button :submit story/take pictures->文哥 -> 我用 intent link 过去了（梅耀文留)
        Button buttonSubmit = (Button)this.findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUploadNewPlaceActivity(null);
//                Intent intentSubmit = new Intent();
//                intentSubmit.setClass(SpotActivity.this, TakePicturesActivity.class);
//                startActivity(intentSubmit);
                }
        });
        //back to home page
        Button buttonBack = (Button)this.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent();
                intentBack.setClass(SpotActivity.this, MapsActivity.class);
                startActivity(intentBack);

            }
        });
    }


    public void gotoUploadNewPlaceActivity(View view){
        Intent intent = new Intent(this, UploadNewPlaceActivity.class );
        startActivity(intent);
    }




}