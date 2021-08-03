package com.company.watsloo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.company.watsloo.data.DataOperation;
import com.company.watsloo.databinding.ActivityMapsFollowBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;


public class MapsFollowActivity extends FragmentActivity implements OnMapReadyCallback
        , GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener
        , GoogleMap.OnMapLongClickListener
        , GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener{

    private GoogleMap mMap;
    private ActivityMapsFollowBinding binding;

    private HashMap<String, double[]> spotsMap;
    private HashMap<String, double[]> eggsMap;
    private HashMap<String, Marker> eggsMarkerMap;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private static final String TAG = MapsFollowActivity.class.getSimpleName();
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final String snippet="Click here to get more info";
    private final String snippet_egg="You find an easter egg! Click here to get more info";
    private final LatLng defaultLocation = new LatLng(0, 0);
    private static final int DEFAULT_ZOOM = 20;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    private void initialVariables(){
        spotsMap=new HashMap<String, double[]>();
        eggsMap=new HashMap<String, double[]>();
        eggsMarkerMap=new HashMap<String, Marker>();


        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationCallback=new LocationCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if(mMap!=null){
                    Location location=locationResult.getLastLocation();
                    if(lastKnownLocation==null||location.distanceTo(lastKnownLocation)>0.5) {
                        lastKnownLocation=location;
                        LatLng currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatlng));
                        showEggs();
                    }
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsFollowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initialVariables();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            //always trigger locationlistener when location is changed
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @SuppressLint("NewApi")
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                updateEggs();
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }


    private void setupMap(){
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);


        mMap.setOnMarkerClickListener(this::onMarkerClick);
        mMap.setOnInfoWindowClickListener(this::onInfoWindowClick);

        mMap.setOnMapLongClickListener(this::onMapLongClick);
        mMap.setOnMyLocationClickListener(this::onMyLocationClick);
        mMap.setOnMyLocationButtonClickListener(this::onMyLocationButtonClick);
    }

    private void getSpotsandEggs(){
        // 先从firebase读出所有数据存到spots.json
        DataOperation.readInfoFromFirebase(MapsFollowActivity.this);
        // 把spots.json的数据读到str中
        String str = DataOperation.readFileFromInternalStorage(MapsFollowActivity.this, "spots.json");
        // 过滤掉未审核，彩蛋以及不需要的数据，保存到hashmap中
        spotsMap = (HashMap<String, double[]>) DataOperation.stringToSpotsMap(str);
        eggsMap=(HashMap<String, double[]>) DataOperation.stringToEggsMap(str);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showSpots(){
        spotsMap.forEach((key,value)->{
            LatLng tempPos=new LatLng(value[0],value[1]);
            mMap.addMarker(new MarkerOptions().position(tempPos).title(key).snippet(snippet));
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showEggs(){
        if(eggsMarkerMap.isEmpty()){
            //initial all EasterEgg spots
            eggsMap.forEach((key,value)->{
                LatLng eggPos=new LatLng(value[0],value[1]);
                Marker egg=mMap.addMarker(new MarkerOptions().position(eggPos).title(key).snippet(snippet_egg)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                eggsMarkerMap.put(key,egg);
            });
        }
        //check the distance between user and egg
        updateEggs();
    }

    @SuppressLint("NewApi")
    private void updateEggs(){
        eggsMarkerMap.forEach((key,value)->{
            LatLng eggPos=value.getPosition();
            float[] result=new float[1];
            Location.distanceBetween(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude(),eggPos.latitude,eggPos.longitude,result);
            if(result[0]>20)
                value.setVisible(false);
            else value.setVisible(true);
        });
    }

    private void drawCampusOutline() {
        try {
            ArrayList latlngs = new ArrayList();
            ArrayList nums = new ArrayList();
            InputStream is = getResources().openRawResource(R.raw.coordinates);
            InputStreamReader isr;
            isr = new InputStreamReader(is, StandardCharsets.UTF_8);

            BufferedReader bfr = new BufferedReader(isr);
            String in;
            while ((in = bfr.readLine()) != null) {
                nums.add(Double.parseDouble(in));
            }
            bfr.close();
            isr.close();

            for (int i = 0; i < nums.size() / 2; i++) {
                Double lng = (Double) nums.get(2 * i), lat = (Double) nums.get(2 * i + 1);
                if (lng == 0 && lat == 0) { //start a new polygon
                    Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
                            .clickable(true)
                            .addAll(latlngs)
                            .addHole(latlngs));
                    latlngs.clear();
                } else { //add latlng to current polygon
                    latlngs.add(new LatLng(lat, lng));
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        drawCampusOutline();
        setupMap();

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        getSpotsandEggs();
        showSpots();
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent;
        intent=new Intent(MapsFollowActivity.this,SpotActivity.class);
        intent.putExtra("title",marker.getTitle());
        intent.putExtra("latitude",marker.getPosition().latitude);
        intent.putExtra("longitude",marker.getPosition().longitude);
        startActivity(intent);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Map Type")
                .setMessage("Do you want to change the map type to?")
                .setNeutralButton("Hybrid", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    }
                })
                .setPositiveButton("Satellite", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    }
                })
                .setNegativeButton("Normal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    }
                });
        builder.show();
    }

    @Override
    public void onMyLocationClick(@NonNull @NotNull Location location) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Spot")
                .setMessage("Do you want to upload a new spot in your current location?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent=new Intent(MapsFollowActivity.this,UploadNewPlaceActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        LatLng currentLatlng=new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatlng,DEFAULT_ZOOM));
        return true;//the default behavior should not occur
    }
}