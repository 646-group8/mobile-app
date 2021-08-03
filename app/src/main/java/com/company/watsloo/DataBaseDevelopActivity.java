package com.company.watsloo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.company.watsloo.data.DataOperation;
import com.company.watsloo.data.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseDevelopActivity extends AppCompatActivity {

    private Button buttonUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_develop);

        // for firebase test, ignore
        buttonUpload = findViewById(R.id.button_upload);

        List<String> stories = new ArrayList<>();
        stories.add("banal story 1");
        stories.add("banal story 2");
        stories.add("banal story 3");
//        Item testItem1 = new Item("Hagey Hall", 43.468866, -80.541278,
//                "A hall in UWaterloo", stories);

        Resources res = getResources();
        Bitmap bmp1 = BitmapFactory.decodeResource(res, R.drawable.common_google_signin_btn_text_light_normal_background);
        Bitmap bmp2 = BitmapFactory.decodeResource(res, R.drawable.common_full_open_on_phone);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DataOperation.addItem(DataBaseDevelopActivity.this, testItem1);
//                DataOperation.addStories(DataBaseDevelopActivity.this, "Hagey Hall", stories);
//
//                try {
//                    DataOperation.addBitmap(DataBaseDevelopActivity.this, "Hagey Hall", bmp1);
//                    DataOperation.addBitmap(DataBaseDevelopActivity.this, "Hagey Hall", bmp2);
//                } catch (IOException e) {
//                    // exception handling
//                }
            }
        });
    }

    public void testButforMengYao(View view){
        Intent intent = new Intent(this, UploadNewPlaceActivity.class);
        intent.putExtra("lat","Lat_from_YM");
        intent.putExtra("log","Log_from_MY");
        intent.putExtra("title","Title_from_MY");
        intent.putExtra("requestCode", "READ_GPS_FROM_MAP");
        startActivity(intent);
    }
}