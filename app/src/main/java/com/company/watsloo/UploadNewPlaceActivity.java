package com.company.watsloo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class UploadNewPlaceActivity extends AppCompatActivity {

    Button albumBtn, cameraBtn, submitBtn;
    ImageView imageView;
    TextView textView_lat, textView_lon;
    private final static int SELECT_PHOTOT_FROM_ALBUM = 250;
    private final static int REQUEST_IMAGE_CAPTURE   = 520;
    private final static int REQUEST_FULL_CAMERA_IMAGE   = 100;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    private static final int PERMISSION_FINE_LOCATION = 99;
    private static final int PERMISSION_GRANTED = 101;
    private String FILE_NAME="photo.jpg";
    private File photoFile;
    String currentPhotoPath;
    private Uri fileProvider;

    //Google's API for Location Services
    FusedLocationProviderClient fusedLocationProviderClient;
    // Location request is a config file for all the settings related to FusedLocationProviderClient
    LocationRequest locationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_place);

        // initialize all the btns, and editText, and imageView;
        imageView = findViewById(R.id.newPlaceImageView);
        albumBtn = findViewById(R.id.button_choose_from_album);
        cameraBtn = findViewById(R.id.button_choose_from_camera);
        submitBtn = findViewById(R.id.button_submit_new_place);
        textView_lat = findViewById(R.id.tx_lat);
        textView_lon = findViewById(R.id.tx_lon);

        checkAndRequestPermissions();


        //Set all properties of LocationRequest
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000*30);
        locationRequest.setFastestInterval(1000*5);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        albumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickfromAlbum = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickfromAlbum,SELECT_PHOTOT_FROM_ALBUM);
            }
        });
        updateGPS();
    }



    // Get the permission of Camera
    public boolean checkPermissionForCamera(){
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    // Get the permission of GPS
    public boolean checkPermissionForGPS(){
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForCamera(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            Toast.makeText(this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
            updateGPS();
        }
    }

    public void requestPermissionForGPS(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(this, "GPS permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_FINE_LOCATION);
            updateGPS();
        }
    }



    public void takePicture(View v){
        Toast.makeText(this, "Return a Thumbnail of Picture", Toast.LENGTH_LONG).show();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the user wants to get just the thumbnail of the image
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bmp);
        }

        // if the user wants to get the full image
        if (requestCode == REQUEST_FULL_CAMERA_IMAGE && resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(), photoFile.toString(), Toast.LENGTH_LONG).show();
            Bitmap imageBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            imageView.setImageBitmap(imageBitmap);
        }

        // if the user select a image from the album
        if (requestCode == SELECT_PHOTOT_FROM_ALBUM && resultCode == RESULT_OK){
            Uri selectedImage = data.getData(); // get the Uri of the selected picture from the album;
            try {
                Bitmap imageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                imageView.setImageBitmap(imageBitmap); // get the bitmap of the picture, and set the image above
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }



    public void dispatchTakePictureIntent(View v) {
        Toast.makeText(this, "Return a full picture", Toast.LENGTH_LONG).show();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = getPhotoFile(FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileProvider =  FileProvider.getUriForFile(getApplicationContext(),"com.company.watsloo.fileprovider",photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_FULL_CAMERA_IMAGE);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File getPhotoFile(String file_name) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                file_name,
                ".jpg",
                storageDir
        );
        return image;
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        EditText editTextEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        String emailAddress = editTextEmail.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, emailAddress);

        EditText editTextDes = (EditText) findViewById(R.id.eidtTextPlaceDes);
        String Description = editTextDes.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, Description);

        ImageView inputImage = (ImageView) findViewById(R.id.newPlaceImageView);
        inputImage.buildDrawingCache();
        Bitmap bitmapImage = inputImage.getDrawingCache();
        intent.putExtra(EXTRA_MESSAGE, bitmapImage);

        startActivity(intent);
    }

    // update GPS information on the Screen
    private void updateGPS(){
        //get permissions from the user to track GPS
        //get the current lcoaiton from the fused client
        //update the text view
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED ){
            // if have the permission for both GPS and Camera already
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    textView_lat.setText(String.valueOf(location.getLatitude()));
                    textView_lon.setText(String.valueOf(location.getLongitude()));
                }
            });
        }else{
            // if we do not have GPS permission, we are going to ask for it
        }
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
//            case PERMISSION_FINE_LOCATION:
//                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    updateGPS();
//                }else {
//                    Toast.makeText(this,"This app requires GPS permission to be granted in order to work properly", Toast.LENGTH_SHORT).show();
////                    finish();
//                }
//                break;
//
//            case CAMERA_PERMISSION_REQUEST_CODE:
//                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    updateGPS();
//                }else {
//                    Toast.makeText(this,"This app requires CAMERA permission to be granted in order to work properly", Toast.LENGTH_SHORT).show();
////                    finish();
//                }
//                break;
            case PERMISSION_GRANTED:
               if (sum==0)
                    updateGPS();
               else
                Toast.makeText(this,"This app requires the permissions to be granted in order to work properly", Toast.LENGTH_SHORT).show();
               break;
        }
    }

    // put all the required permissions in a list, and ask them from the user one by one;
    private boolean checkAndRequestPermissions() {

        int ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int ACCESS_CAMERA= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (ACCESS_CAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (ACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), PERMISSION_GRANTED);
            return false;
        }
        return true;
    }



}