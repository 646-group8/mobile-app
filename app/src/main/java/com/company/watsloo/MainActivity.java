package com.company.watsloo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.company.watsloo.data.DataOperation;
import com.company.watsloo.data.Item;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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


        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DataOperation.addItem(MainActivity.this, testItem1);
//                DataOperation.addStories(MainActivity.this, "Hagey Hall", stories);

                DataOperation.addItem(MainActivity.this, testItem2);
            }
        });
    }
}