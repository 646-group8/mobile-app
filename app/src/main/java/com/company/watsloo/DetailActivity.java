package com.company.watsloo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private TextView story_detail;
    private TextView story_title;
    private ImageView story_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        story_detail= findViewById(R.id.detailText);
        story_title = findViewById(R.id.titleText);
        story_image = findViewById(R.id.storyImage);

        Bundle bundle = getIntent().getExtras();
        String spot_title_str = bundle.getString("spot_title");
        String story_title_str = bundle.getString("story_title");
        story_title.setText(story_title_str);
        story_detail.setText("    inputaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n" +
                "    dddfd");
        story_title.setTextSize(20);
        story_image.setImageResource(R.drawable.uwaterloologo);






    }



}