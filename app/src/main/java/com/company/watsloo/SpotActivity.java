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

import com.company.watsloo.data.DataOperation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

public class SpotActivity extends AppCompatActivity {

    private TextView spot_title;
    private TextView spot_content;
    private ListView listView;
    private String description;
    private Double latitude;
    private Double longitude;
//    private JSONArray stories;
//    private JSONObject images;
    private String stories;
    private String images;
    private String[] title_image;
    private String[] storiesList;
    private String[] imagesList;
    private JSONObject obj;
    ArrayList<String> image_list = new ArrayList<>();
    ArrayList<String> title_list = new ArrayList<>();
    ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);
        spot_title = findViewById(R.id.spotTitleText);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        spot_title.setText(title);

        getSpotData(title);
        getSpotStory();
        listviewAdapter();
        toDetailAct(title);
        toUploadAct(title);
        backMap();

    }


    private void getSpotData(String title) {
        String data = DataOperation.readFileFromInternalStorage(SpotActivity.this, "spots.json");

        obj = DataOperation.stringToDetails(data, title);
    }


    private void getSpotStory() {
        try {
            description  = obj.getString("description");
            latitude = obj.getDouble("latitude");
            longitude = obj.getDouble("longitude");
//            stories = obj.getJSONArray("stories");
//            for(int i = 0; i < stories.length(); i++) {
//                story_list.add(stories.getString(i));
//                title_list.add(stories.getString(i).substring(0, 20));
//            }
//            images = obj.getJSONObject("images");
            stories = obj.getString("stories");
            images = obj.getString("images");

            stories = stories.substring(1, stories.length() - 1);
            storiesList = stories.split(",");
            images = images.substring(1, images.length() - 1);
            imagesList = images.split(",");

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        if(storiesList != null && storiesList.length > 0) {
            for(int i = 1; i <= storiesList.length; i++) {
                String s = "Story " + i + ":" + storiesList[i - 1].substring(0, 20);
                title_list.add(s);

            }
        }
        System.out.println(Arrays.toString(imagesList));
        for(int i = 0; i < imagesList.length; i++) {
            String imageUrl = imagesList[i].split("=")[1];
            image_list.add(imageUrl);
            System.out.println(imageUrl);
        }

        title_image = image_list.toArray(new String[image_list.size()]);



        int[] image_story = new int[]{R.drawable.uwaterloologo,
        };

        for(int i = 0; i < title_list.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("text", title_list.get(i));
            if(image_list.size() > i) {
                map.put("pic",image_story[0]);
            }
            else {
                map.put("pic", image_story[0]);
            }

            dataList.add(map);
        }
    }


    private void listviewAdapter() {
        listView = findViewById(R.id.spotList);
        SimpleAdapter adapter = new SimpleAdapter(this, dataList,
                R.layout.fragment_story_list, new String[]{"text", "pic"},
                new int[]{R.id.story_title, R.id.story_image});

        listView.setAdapter(adapter);
    }


    private void toDetailAct(String title) {
        // story's detail -> DetailActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intentDetail = new Intent();
                intentDetail.setClass(SpotActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("story_title", storiesList[position]);
                bundle.putString("spot_title", title);
                bundle.putString("image_url", title_image[position]);
                System.out.println(imagesList[position]);

                intentDetail.putExtras(bundle);
                startActivity(intentDetail);
            }
        });
    }


    private void toUploadAct(String title) {
        Button buttonSubmit = (Button) this.findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpotActivity.this, UploadNewPlaceActivity.class);
                intent.putExtra("lat",String.valueOf(latitude));
                intent.putExtra("log",String.valueOf(longitude));
                intent.putExtra("title",title);
                intent.putExtra("requestCode", "READ_GPS_FROM_MAP");
                intent.putExtra("desc", description);
                startActivity(intent);
//                Intent intentSubmit = new Intent();
//                intentSubmit.setClass(SpotActivity.this, UploadNewPlaceActivity.class);
//                startActivity(intentSubmit);
            }
        });
    }


    private void backMap() {
        //back to home page
        Button buttonBack = (Button) this.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }


}