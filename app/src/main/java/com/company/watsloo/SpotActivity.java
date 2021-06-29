package com.company.watsloo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SpotActivity extends AppCompatActivity {

    private TextView spot_title;
    private TextView spot_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);
        spot_title= findViewById(R.id.spotTitleText);
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        spot_title.setText(title);

        // spot stories
        //setContentView(R.layout.activity_spot);
        spot_content= findViewById(R.id.spotContent);
        String content = "Story1' tilte\nStory2's title\ntest";
        spot_content.setText(content);
        spot_content.setMovementMethod(ScrollingMovementMethod.getInstance());



        //button :submit story/take pictures->文哥
        Button buttonSubmit = (Button)this.findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSubmit = new Intent();
                intentSubmit.setClass(SpotActivity.this, TakePicturesActivity.class);
                startActivity(intentSubmit);
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






    }