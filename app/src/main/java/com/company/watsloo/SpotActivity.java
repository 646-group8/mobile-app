package com.company.watsloo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SpotActivity extends AppCompatActivity {

    private TextView spot_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);
        spot_title= findViewById(R.id.spotTitleText);
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        spot_title.setText(title);
    }
}