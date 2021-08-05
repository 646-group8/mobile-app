package com.company.watsloo;


import android.os.Bundle;

import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class DetailActivity extends AppCompatActivity {

    private TextView story_detail;
    private TextView story_title;
    private ImageView story_image;
    private String spot_title_str;
    private String story_title_str;
    private String image_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        story_detail= findViewById(R.id.detailText);
        story_title = findViewById(R.id.titleText);
        story_image = findViewById(R.id.storyImage);
        story_title.setMovementMethod(ScrollingMovementMethod.getInstance());

        story_image.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Bundle bundle = getIntent().getExtras();
        spot_title_str = bundle.getString("spot_title");
        story_title_str = bundle.getString("story_title");
        image_url = bundle.getString("image_url");
        story_title.setText(story_title_str);
        System.out.println(image_url);
        readPicture();

    }


    private void readPicture() {
        // read data from firebase
//        String data = DataOperation.readFileFromInternalStorage(DetailActivity.this, "spots.json");
//        JSONObject obj1 = DataOperation.stringToStory(data, spot_title_str);

        Glide.with(DetailActivity.this)
                .load(image_url)
                .into(story_image);

        //story_detail.setText(image_url);
        story_title.setTextSize(18);

        //story_image.setImageResource(R.drawable.eggfountain);
    }





}