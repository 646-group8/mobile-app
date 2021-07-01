package com.company.watsloo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.company.watsloo.data.DataOperation;
import com.company.watsloo.data.Item;

import java.io.IOException;
import java.io.InputStream;
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

//        Bitmap bitmap1 = BitmapFactory.decodeResource(
//                MainActivity.this.getResources(), R.drawable.ic_launcher_foreground);
//        Bitmap bitmap2 = BitmapFactory.decodeResource(
//                MainActivity.this.getResources(), R.drawable.ic_launcher_background);
//        Resources r = MainActivity.this.getResources();
//        @SuppressLint("ResourceType") InputStream is1 = r.openRawResource(R.drawable.ic_launcher_foreground);
//        BitmapDrawable bmpDraw1 = new BitmapDrawable(is1);
//        Bitmap bmp1 = bmpDraw1.getBitmap();
        Resources res = getResources();
        Bitmap bmp1 = BitmapFactory.decodeResource(res, R.drawable.common_google_signin_btn_text_light_normal_background);
        Bitmap bmp2 = BitmapFactory.decodeResource(res, R.drawable.common_full_open_on_phone);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DataOperation.addItem(MainActivity.this, testItem1);
//                DataOperation.addStories(MainActivity.this, "Hagey Hall", stories);

//                DataOperation.addItem(MainActivity.this, testItem2);
//                DataOperation.addStories(MainActivity.this, "Hagey Hall 2", stories);

                try {
                    DataOperation.addBitmap(MainActivity.this, "Hagey Hall 2", bmp2);
                    DataOperation.addBitmap(MainActivity.this, "Hagey Hall 2", bmp1);
                } catch (IOException e) {
                    // exception handling
                }

            }
        });
    }
}