package com.company.watsloo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.company.watsloo.data.DataOperation;
import com.company.watsloo.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
        , GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener
        , GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private final String snippet="Click here to get more info";
    private final LatLng defaultLatlng = new LatLng(43.4723, -80.5449);
    private static final int DEFAULT_ZOOM = 15;

    private HashMap<String, double[]> spotsMap;

    // 把 从FireBase数据库读 的操作放到了 onCreate 之前的method 里执行，这样
    // 很大概率上来说，onCreate 显示地图的时候，这些地点已经加载好了： https://www.jianshu.com/p/d52960e3f6f2
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        getSpots();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        spotsMap=new HashMap<String, double[]>();
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

    private void setupMap(){
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        mMap.setOnMarkerClickListener(this::onMarkerClick);
        mMap.setOnInfoWindowClickListener(this::onInfoWindowClick);

        mMap.setOnMapLongClickListener(this::onMapLongClick);
    }
    private void getSpots(){
        // 先从firebase读出所有数据存到spots.json
        DataOperation.readInfoFromFirebase(MapsActivity.this);

        // 把spots.json的数据读到str中
        String str = DataOperation.readFileFromInternalStorage(MapsActivity.this, "spots.json");
        // 过滤掉未审核，彩蛋以及不需要的数据，保存到hashmap中
        spotsMap = (HashMap<String, double[]>) DataOperation.stringToSpotsMap(str);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showSpots(){
        spotsMap.forEach((key,value)->{
            LatLng tempPos=new LatLng(value[0],value[1]);
            mMap.addMarker(new MarkerOptions().position(tempPos).title(key).snippet(snippet));
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatlng,DEFAULT_ZOOM));
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
        getSpots();
        showSpots();

        // getSpots() 需要读写数据库，然后写txt到手机里，再读回来，
        // 出于某种 同步还是异步 我不知道的机理，showSpots（） 运行之的时候，getSpots() 还没有返回值
        // 于是第一次安装 app 的时候 map就不会有marker，
        // 暴力的解决方式是 让 showspot 等 getSpots 的返回值 等上 300毫秒左右
//        new android.os.Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                showSpots();
//            }
//        },3000); // milliseconds: 0.3 sed.
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent;
        intent=new Intent(MapsActivity.this,SpotActivity.class);
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
}