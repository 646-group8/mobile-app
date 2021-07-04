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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String PROJ_PATH = System.getProperty("user.dir");

    private Button buttonUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // for firebase test, ignore
        buttonUpload = findViewById(R.id.button_upload);

        List<String> stories = new ArrayList<>();
        stories.add("banal story 1");
        stories.add("banal story 2");
        stories.add("banal story 3");
        Item testItem1 = new Item("Hagey Hall", 43.468866, -80.541278,
                "A hall in UWaterloo", stories);

        Resources res = getResources();
        Bitmap bmp1 = BitmapFactory.decodeResource(res, R.drawable.common_google_signin_btn_text_light_normal_background);
        Bitmap bmp2 = BitmapFactory.decodeResource(res, R.drawable.common_full_open_on_phone);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataOperation.addItem(MainActivity.this, testItem1);
                DataOperation.addStories(MainActivity.this, "Hagey Hall", stories);

                try {
                    DataOperation.addBitmap(MainActivity.this, "Hagey Hall", bmp1);
                    DataOperation.addBitmap(MainActivity.this, "Hagey Hall", bmp2);
                } catch (IOException e) {
                    // exception handling
                }
            }
        });
    }

    public void gotoUploadNewPlaceActivity(View view){
        Intent intent = new Intent(this, UploadNewPlaceActivity.class );
        startActivity(intent);
    }
}