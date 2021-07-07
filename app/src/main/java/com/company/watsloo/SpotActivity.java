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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class SpotActivity extends AppCompatActivity {

    private TextView spot_title;
    private TextView spot_content;
    private ListView listView;
    ArrayList<String> story_list = new ArrayList<>();

    ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);
        spot_title = findViewById(R.id.spotTitleText);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        spot_title.setText(title);

        int[] image_story = new int[]{R.drawable.uwaterloologo, R.drawable.uwaterloologo};
        String[] title_story = new String[image_story.length];
        switch (title) {
            case "Mathematics & Computer Building":
                title_story[0] = "story11";
                title_story[1] = "story22";
                for (int i = 0; i < image_story.length; i++) {

                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("text", title_story[i]);
                    map.put("pic", image_story[i]);

                    dataList.add(map);
                }
                break;
            case "Marker in Loo":
                title_story[0] = "story33";
                title_story[1] = "story44";
                for (int i = 0; i < image_story.length; i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("text", title_story[i]);
                    map.put("pic", image_story[i]);
                    dataList.add(map);
                }
                break;

        }

        listView = findViewById(R.id.spotList);
        SimpleAdapter adapter = new SimpleAdapter(this, dataList,
                R.layout.fragment_story_list, new String[]{"text", "pic"},
                new int[]{R.id.story_title, R.id.story_image});

        listView.setAdapter(adapter);

//        story_list.add("story1");
//        story_list.add("story2");
//        listView = findViewById(R.id.spotList);
//        ArrayAdapter<String> adapter = new ArrayAdapter( this,R.layout.support_simple_spinner_dropdown_item,story_list);
//        listView.setAdapter(adapter);

        // story's detail -> DetailActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intentDetail = new Intent();
                intentDetail.setClass(SpotActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("story_title", title_story[position]);
                bundle.putString("spot_title", title);

                intentDetail.putExtras(bundle);
                startActivity(intentDetail);
            }
        });


        //button :submit story/take pictures
        Button buttonSubmit = (Button) this.findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSubmit = new Intent();
                intentSubmit.setClass(SpotActivity.this, TakePicturesActivity.class);
                startActivity(intentSubmit);
            }
        });
        //back to home page
        Button buttonBack = (Button) this.findViewById(R.id.buttonBack);
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