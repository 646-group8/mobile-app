package com.company.watsloo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.company.watsloo.data.DataOperation;

import org.json.JSONObject;

import java.util.ArrayList;

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

        //String url = "https://console.firebase.google.com/u/0/project/watsloo-d5415/storage/watsloo-d5415.appspot.com/files/~2FE2%20Engineering%20Building";
        //String url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.appfutura.com%2Fuploads%2Fblog%2Fposts%2Fcache%2F315e11c3bcae87162047127bd2a78a271511173794_393_222.jpg&refer=http%3A%2F%2Fwww.appfutura.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630683319&t=b144235024dda80a9e83e50932f126bd";
        Glide.with(DetailActivity.this)
                .load(image_url)
                .into(story_image);

        story_detail.setText(image_url);
        story_title.setTextSize(20);
        //story_image.setImageResource(R.drawable.eggfountain);
    }





}