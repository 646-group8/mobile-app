package com.company.watsloo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private static final String PROJ_PATH = System.getProperty("user.dir");



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
                    gotoOverViewActivity(null);
                } else if (id == R.id.nav_follow) {
                    // send an intent to active the following mode here
                    drawerLayout.closeDrawer(GravityCompat.START);
                    gotoFollowActivity(null);
                } else if (id == R.id.nav_upload) {
                    // send an intent to active the story upload mode here
                    Toast.makeText(thiscontext, "upload your own story", Toast.LENGTH_LONG).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(thiscontext, UploadNewPlaceActivity.class );
                    startActivity(intent);

                } else if (id == R.id.nav_databasetest){
                    gotoDataBaseDevelopActivity(null);
                }

                return true;
            }
        });


    }

    // Display the sidebar when the toggle is pressed
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, "Welcome to UWaterloo Campus", Toast.LENGTH_SHORT).show();
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void gotoUploadNewPlaceActivity(View view){
        Intent intent = new Intent(this, UploadNewPlaceActivity.class );
        startActivity(intent);
    }

    public void gotoOverViewActivity(View view){
        Intent intent = new Intent(this, MapsActivity.class );
        startActivity(intent);
    }
    public void gotoFollowActivity(View view){
        Intent intent=new Intent(this,MapsFollowActivity.class);
        startActivity(intent);
    }
    public void gotoDataBaseDevelopActivity(View view){
        Intent intent = new Intent(this, DataBaseDevelopActivity.class );
        startActivity(intent);
    }
}