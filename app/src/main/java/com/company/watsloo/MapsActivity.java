package com.company.watsloo;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.company.watsloo.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private void drawCampusOutline(){
        try{
            ArrayList latlngs=new ArrayList();
            ArrayList nums=new ArrayList();
            InputStream is =getResources().openRawResource(R.raw.coordinates);
            InputStreamReader isr;//指定编码格式
            isr = new InputStreamReader(is, StandardCharsets.UTF_8);

            BufferedReader bfr = new BufferedReader(isr);
            String in;
            while ((in = bfr.readLine()) != null) {
                nums.add(Double.parseDouble(in));
            }
            bfr.close();
            isr.close();

            for(int i=0;i<nums.size()/2;i++){
                Double lng=(Double)nums.get(2*i),lat=(Double)nums.get(2*i+1);
                if(lng==0&&lat==0){ //start a new polygon
                    Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
                            .clickable(true)
                            .addAll(latlngs)
                            .addHole(latlngs));
                    latlngs.clear();
                }
                else{ //add latlng to current polygon
                    latlngs.add(new LatLng(lat,lng));
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
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        drawCampusOutline();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        mMap.setOnMarkerClickListener(this::onMarkerClick);
        mMap.setOnInfoWindowClickListener(this::onInfoWindowClick);
        // Add a marker in Sydney and move the camera
        LatLng loo = new LatLng(43.4723,-80.5449),mc=new LatLng(43.47207511,-80.54394739);
        Marker loo_mark=mMap.addMarker(new MarkerOptions().position(loo).title("Marker in Loo").snippet("Click to get more info"));
        Marker mc_mark=mMap.addMarker(new MarkerOptions().position(mc).title("Mathematics & Computer Building").snippet("Click to get more info"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loo,15));
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
        startActivity(intent);
    }
}