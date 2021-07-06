package com.company.watsloo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Context;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private static final String PROJ_PATH = System.getProperty("user.dir");
    private Button buttonUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context thiscontext = this;

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open,R.string.close);
        navigationView = findViewById(R.id.nav_view);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //https://www.legendblogs.com/navigation-drawer-layout-with-an-event-listener
        // Add listener to each of the menu items
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_overview) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    // send an intent to active the overview mode here
                } else if (id == R.id.nav_follow) {
                    // send an intent to active the following mode here
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (id == R.id.nav_upload) {
                    // send an intent to active the story upload mode here
                    Toast.makeText(thiscontext, "upload your own story", Toast.LENGTH_LONG).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(thiscontext, UploadNewPlaceActivity.class );
                    startActivity(intent);

                }

                return true;
            }
        });


    }

    // Display the sidebar when the toggle is pressed
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, "Welcome to explore our Campus", Toast.LENGTH_LONG).show();
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


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




    public void gotoUploadNewPlaceActivity(View view){
        Intent intent = new Intent(this, UploadNewPlaceActivity.class );
        startActivity(intent);
    }
}