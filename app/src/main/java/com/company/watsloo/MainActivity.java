package com.company.watsloo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.company.watsloo.data.DataOperation;
import com.company.watsloo.data.Item;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PROJ_PATH = System.getProperty("user.dir");

    private Button buttonUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonUpload = findViewById(R.id.button_upload);

        Item testItem1 = new Item("Hagey Hall", 43.468866, -80.541278,
                "A hall in UWaterloo");
        List<String> stories = new ArrayList<>();
        stories.add("banal story 4");
        stories.add("banal story 5");
        stories.add("banal story 3");

        List<String> stories2 = new ArrayList<>();
        stories2.add("interesting story 1");
        stories2.add("interesting story 2");
        stories2.add("interesting story 3");
        Item testItem2 = new Item("Hagey Hall 2", 43.468866, -80.541278,
                "A hall in UWaterloo 2", stories2);

        String imagePath = "/home/xuzishuo1996/Desktop/mobile-app/app/src/main/java/com/company/watsloo/data/";
        String path1 = imagePath + "ic_launcher_round.bmp";
        String path2 = imagePath + "ic_launcher.bmp";
        Bitmap bitmap1 = BitmapFactory.decodeFile(path1);
        Bitmap bitmap2 = BitmapFactory.decodeFile(path2);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DataOperation.addItem(MainActivity.this, testItem1);
//                DataOperation.addStories(MainActivity.this, "Hagey Hall", stories);

//                DataOperation.addItem(MainActivity.this, testItem2);
//                DataOperation.addStories(MainActivity.this, "Hagey Hall 2", stories);

                DataOperation.addBitmap(MainActivity.this, "Hagey Hall 2", bitmap1);
                DataOperation.addBitmap(MainActivity.this, "Hagey Hall 2", bitmap2);
            }
        });
    }
}