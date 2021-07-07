package com.company.watsloo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TakePicturesActivity extends AppCompatActivity {

    private TextView submit_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pictures);
        submit_text= findViewById(R.id.submitText);
        //Intent intent=getIntent();
        String input="input";
        submit_text.setText(input);
    }
}