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
        switch(story_title_str) {
            case "Put coins in the fountain and make a wish, you will get good mark in your final exams":
                story_detail.setText("    ");
                story_title.setTextSize(20);
                story_image.setImageResource(R.drawable.eggfountain);
                break;
            case "Hold a party here":
                story_detail.setText("    We're holding a BBQ at the Egg Fountain this Wednesday! " +
                        "Come out and grab some food and chat with our responders:)\n" +
                        "    Cotton candy and freezies will also be available if you'd like a little treat!");
                story_title.setTextSize(20);

                break;
            case "The Birthday of QNC":
                story_detail.setText("    Dr. Stephen Hawking was here in 2012 to celebrate the grand" +
                        "openning for QNC");
                story_title.setTextSize(20);
                story_image.setImageResource(R.drawable.qnc);
                break;
            case "An Admirable Person":
                story_detail.setText("    Donna Strickland wins 2018 Nobel Prize for laser physics, " +
                        "she is the first women to win the physics prize since 1963.");
                story_title.setTextSize(20);
                story_image.setImageResource(R.drawable.physics);
                break;


        }



    }



}