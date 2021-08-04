package com.company.watsloo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.company.watsloo.data.DataOperation;
import com.company.watsloo.data.Item;
import com.company.watsloo.data.ItemWithContactInfo;
import com.company.watsloo.data.ItemWithEmail;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataBaseDevelopActivity extends AppCompatActivity {

    private Button buttonUpload;
    private TextView myText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_develop);

        myText = findViewById(R.id.textView1231);


        // for firebase test, ignore
        buttonUpload = findViewById(R.id.button_upload);

        // ===== Upload example 1 : item WITHOUT email =====
        List<String> stories = new ArrayList<>();
        stories.add("story 11");
        stories.add("story 22");

        Resources res = getResources();
        Bitmap bmp1 = BitmapFactory.decodeResource(res, R.drawable.common_google_signin_btn_text_light_normal_background);
        Bitmap bmp2 = BitmapFactory.decodeResource(res, R.drawable.common_full_open_on_phone);
        List<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.add(bmp1);
        bitmaps.add(bmp2);

        Item itemWithoutEmail = new Item("Badmiton Center", 40.468866, -90.541278,
                "A hall in UWaterloo", stories, bitmaps);


        // ===== Upload example 2 : item with email =====
        Item itemWrapee = new Item("Tennis Center", 40.468866, -90.541278,
                "A hall in UWaterloo", stories, bitmaps);
        ItemWithContactInfo itemWithEmail =
                new ItemWithEmail(itemWrapee, "myEmailAddress@google.com");


        // upload
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemWithoutEmail.addItem(DataBaseDevelopActivity.this);
                itemWithEmail.addItem(DataBaseDevelopActivity.this);
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

    public void testforReadDataBase(View view){
        // 先从firebase读出所有数据存到spots.json
        DataOperation.readInfoFromFirebase(DataBaseDevelopActivity.this);
        String data = DataOperation.readFileFromInternalStorage(DataBaseDevelopActivity.this, "spots.json");

        JSONObject obj = DataOperation.stringToDetails(data, "Hagey Hall");
        myText.setText(obj.toString());


    }
}