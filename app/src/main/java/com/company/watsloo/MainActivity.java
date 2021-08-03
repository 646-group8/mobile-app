package com.company.watsloo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.company.watsloo.strategy_pattern.UpdateGPSBehavior;
import com.company.watsloo.strategy_pattern.client.GPSUpdateWithGPSValueClient;
import com.company.watsloo.strategy_pattern.client.GPSUpdatreManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private static final String PROJ_PATH = System.getProperty("user.dir");
    private static final int PERMISSION_GRANTED = 101;
    private Context mycontext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context thiscontext = this;
        mycontext = thiscontext;

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
//                    Toast.makeText(thiscontext, "upload your own story", Toast.LENGTH_LONG).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(thiscontext, UploadNewPlaceActivity.class );
                    startActivity(intent);

                } else if (id == R.id.nav_databasetest){
                    gotoDataBaseDevelopActivity(null);
                }

                return true;
            }
        });

        // Check and Ask for GPS, Camera, Media Store Permissions
        checkAndRequestPermissions();


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

    // put all the required permissions in a list, and ask them from the user one by one;
    private boolean checkAndRequestPermissions() {

        int ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int ACCESS_CAMERA= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int ACCESS_EXIF_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (ACCESS_CAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (ACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ACCESS_EXIF_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_MEDIA_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), PERMISSION_GRANTED);
            return false;
        }
        return true;
    }

    // update GPS information on Screen after all the permissions are granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int sum = 0;
        for (int i : grantResults){
            sum +=i;
        }

        switch (requestCode){
            case PERMISSION_GRANTED:
                if (sum==0) {
                    // if we got all the premissions, Great, we can try to obtain CURRENT GPS information
                    // Get GPS is very slow, so it would be better if we do it right at the begining
                    FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED ){
                        // if have the permission for both GPS and Camera already
                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Greate, we got the GPS information
//                                Toast.makeText(mycontext,"Got the GPS permission to run the App", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
//                        Toast.makeText(this,"Can not obtain GPS Inforamtion ", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(this,"This app requires all the permissions to be granted in order to work properly", Toast.LENGTH_SHORT).show();
                }



                break;
        }
    }
}