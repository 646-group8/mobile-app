package com.company.watsloo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.company.watsloo.adapter_pattern.DataUploadAdapter;
import com.company.watsloo.data.DataOperation;
import com.company.watsloo.data.Item;
import com.company.watsloo.data.ItemWithContactInfo;
import com.company.watsloo.data.ItemWithEmail;
import com.company.watsloo.strategy_pattern.client.GPSUpdateWithGPSValueClient;
import com.company.watsloo.strategy_pattern.client.GPSUpdateWithIntentClient;
import com.company.watsloo.strategy_pattern.client.GPSUpdateWithPictureEXIFClient;
import com.company.watsloo.strategy_pattern.client.GPSUpdatreManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class UploadNewPlaceActivity extends AppCompatActivity {

    Button albumBtn, cameraBtn, submitBtn;
    ImageView imageView;
    Bitmap myBitmap;
    EditText textView_lat, textView_lon, textView_name, textView_discription, textView_email, textView_story;
    private final static int SELECT_PHOTOT_FROM_ALBUM = 250;
    private final static int REQUEST_IMAGE_CAPTURE   = 520;
    private final static int REQUEST_FULL_CAMERA_IMAGE   = 100;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    private static final int PERMISSION_FINE_LOCATION = 99;
    private static final int PERMISSION_EXIF_LOCATION = 98;
    private static final int PERMISSION_GRANTED = 101;
    private String FILE_NAME="photo.jpg";
    private File photoFile;
    String currentPhotoPath;
    private Uri fileProvider;

    // Parameters for the Strategy Design Pattern;
    private String currentLat = "None";
    private String currentLon = "None";
    private Intent incomingIntent;
    private Context mycontext;
    private boolean ShouldReadFromIntent = false;

    //Google's API for Location Services
    FusedLocationProviderClient fusedLocationProviderClient;
    // Location request is a config file for all the settings related to FusedLocationProviderClient
    LocationRequest locationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_place);
        this.mycontext = this;



        // initialize all the btns, and editText, and imageView;
        imageView = findViewById(R.id.newPlaceImageView);
        submitBtn = findViewById(R.id.button_submit_new_place);
        textView_lat = findViewById(R.id.tx_lat);
        textView_lon = findViewById(R.id.tx_lon);
        textView_name = findViewById(R.id.editTextTextNewPlaceName);
        textView_discription = findViewById(R.id.eidtTextPlaceDes);
        textView_email = findViewById(R.id.editTextTextEmailAddress);
        textView_story = findViewById(R.id.eidtTextPlaceStory);

        // Analysis the incoming intent:
        this.incomingIntent = getIntent();
        if (incomingIntent.getStringExtra("requestCode")!=null && incomingIntent.getStringExtra("requestCode").equals("READ_GPS_FROM_MAP")){
            ShouldReadFromIntent = true;
        }
        if (incomingIntent.getStringExtra("title")!=null){
            textView_name.setText(incomingIntent.getStringExtra("title"));
        }

        checkAndRequestPermissions();


        //Set all properties of LocationRequest
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000*30);
        locationRequest.setFastestInterval(1000*5);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        prepareGPSconfiguration();

//        updateGPS();
    }



    // This is the major logic section of this activity,
// if the user 1) decide to take a picture, we are going to use the current realtime GPS
// if the user 2) decide to upload a picture from ablum, we are going to use the EXIF GPS
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the user wants to get just the thumbnail of the image
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            myBitmap = bmp;
            imageView.setImageBitmap(bmp);
        }

        // if the user wants to get the full image
        if (requestCode == REQUEST_FULL_CAMERA_IMAGE && resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(), photoFile.toString(), Toast.LENGTH_LONG).show();
            Bitmap imageBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            myBitmap = imageBitmap;
            imageView.setImageBitmap(imageBitmap);
            // update GPS information after a picture is taken
            updateGPS();
            textView_lat.setTextColor(Color.BLACK);
            textView_lon.setTextColor(Color.BLACK);
        }

        // if the user select a image from the album, only support to API 24, current is API 23;
//        https://stackoverflow.com/questions/34696787/a-final-answer-on-how-to-get-exif-data-from-uri
        // Dangerous permission after API23: https://stackoverflow.com/questions/32635704/android-permission-doesnt-work-even-if-i-have-declared-it

        if (requestCode == SELECT_PHOTOT_FROM_ALBUM && resultCode == RESULT_OK){
            Uri selectedImage = data.getData(); // get the Uri of the selected picture from the album;

            String selectedImagestring = selectedImage.toString();

            try (InputStream inputStream = getContentResolver().openInputStream(selectedImage)) {
                Bitmap imageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                myBitmap = imageBitmap;
                imageView.setImageBitmap(imageBitmap); // get the bitmap of the picture, and set the image above
                ExifInterface exifInterface = new ExifInterface(inputStream);
                // If the intent has GPS inforamtion already, we do not need to obtain from Google API:
                // If the user come to this page from a MAP Icon, then we use the GPS information from the Map Icon;
                if (ShouldReadFromIntent){
                    GPSUpdatreManager gpsUpdatreManager = new GPSUpdateWithIntentClient(textView_lat,textView_lon,incomingIntent);
                    return;
                }
                GPSUpdatreManager gpsUpdatreManager = new GPSUpdateWithPictureEXIFClient(textView_lat,textView_lon,exifInterface);
//////////////////////////////////////////////////////////////////////////////////////////////////////
// The following code was be remove after we applied the strategy design pattern
//////////////////////////////////////////////////////////////////////////////////////////////////////
//                String lat = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
//                String log = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//                String logREF = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
//                if (lat!=null){
////                    Toast.makeText(this, log + logREF, Toast.LENGTH_LONG).show();
//                textView_lat.setText(String.valueOf(covertRationalGPS2DecimalGPS(lat)));
//
//                if (logREF.equals("W")){
//                    //need to show negative value when Longitrude is in West
//                textView_lon.setText(String.valueOf(-1*covertRationalGPS2DecimalGPS(log)));}
//                else{
//                    textView_lon.setText(String.valueOf(covertRationalGPS2DecimalGPS(log)));
//                }
//                textView_lat.setTextColor(Color.BLUE);
//                textView_lon.setTextColor(Color.BLUE);
//                } else{
//                    Toast.makeText(this, "Please manually input the GPS information for this picture", Toast.LENGTH_LONG).show();
//                    textView_lat.setText("None");
//                    textView_lon.setText("None");
//                    textView_lat.setTextColor(Color.RED);
//                    textView_lon.setTextColor(Color.RED);
//                }

            } catch (IOException  e) {
                e.printStackTrace();
            }

        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////
// The following code was be remove after we applied the strategy design pattern
//////////////////////////////////////////////////////////////////////////////////////////////////////
//    Convert the rational GPS information into the decimal gps information
//    private Float covertRationalGPS2DecimalGPS(String gpsinput){
//        String[] gpsList  = gpsinput.replace("/", ",").split(",");
//
//        Float gpsD =0f;
//
//        Float gpsM =0f;
//
//        Float gpsS =0f;
//
//        if (gpsList.length >=2) {
//
//            gpsD = Float.parseFloat(gpsList[0]) / Float.parseFloat(gpsList[1]);
//
//        }
//
//        if (gpsList.length >=4) {
//
//            gpsM = Float.parseFloat(gpsList[2]) / Float.parseFloat(gpsList[3]);
//
//        }
//
//        if (gpsList.length >=6) {
//
//            gpsS = Float.parseFloat(gpsList[4]) / Float.parseFloat(gpsList[5]);
//
//        }
//
//        return  gpsD + gpsM /60 + gpsS /3600;
//    }


    // Start the take picture intent;
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
    // Start the select picture from ablum intent;
    public void dispatchSelectPictureIntent(View v){
        Intent pickfromAlbum = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickfromAlbum,SELECT_PHOTOT_FROM_ALBUM);
    }

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }

    private File getPhotoFile(String file_name) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                file_name,
                ".jpg",
                storageDir
        );
        return image;
    }

//    public void sendMessage(View view) {
//        Intent intent = new Intent(this, MainActivity.class);
//
//        EditText editTextEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
//        String emailAddress = editTextEmail.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, emailAddress);
//
//        EditText editTextDes = (EditText) findViewById(R.id.eidtTextPlaceDes);
//        String Description = editTextDes.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, Description);
//
//        ImageView inputImage = (ImageView) findViewById(R.id.newPlaceImageView);
//        inputImage.buildDrawingCache();
//        Bitmap bitmapImage = inputImage.getDrawingCache();
//        intent.putExtra(EXTRA_MESSAGE, bitmapImage);
//
//        startActivity(intent);
//    }

    public void sendStory(View view){
        String strLat = textView_lat.getText().toString();
        String strLog = textView_lon.getText().toString();
        String strName = textView_name.getText().toString();
        String strDisc = textView_discription.getText().toString();
        String strEmail = textView_email.getText().toString();
        String strStory = textView_story.getText().toString();

        float fLat = Float.parseFloat(strLat);
        float fLog = Float.parseFloat(strLog);

        DataUploadAdapter adapter = new DataUploadAdapter(strName,strLat,strLog,strDisc,strStory,myBitmap,strEmail);
        adapter.ItemUpload(this);

//////////////////////////////////////////////////////////////////////////////////////////////////////
// The following code was be remove after we applied the adapter design pattern
//////////////////////////////////////////////////////////////////////////////////////////////////////

//        List<String> stories = new ArrayList<>();
//        stories.add(strStory);
//        Item testItem1 = new Item(strName, fLat, fLog, strName, stories);
//        Resources res = getResources();
//        Bitmap bmp1 = myBitmap;
//        DataOperation.addItem(this, testItem1);
//        DataOperation.addStories(this, strName, stories);
//        try{
//        DataOperation.addBitmap(this, strName, bmp1);}
//        catch (IOException e){
//
//        }
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
                    if (location ==null ){
                        textView_lat.setText("No GPS information found");
                        textView_lon.setText("No GPS information found");
                    }else{
                        textView_lat.setText(String.valueOf(location.getLatitude()));
                        textView_lon.setText(String.valueOf(location.getLongitude()));}
                }
            });
        }else{
            // if we do not have GPS permission, we are going to ask for it
        }
    }

    // Get the current GPS information and save them in two private strings for latter use
    // The purpose of this function is to get the current GPS information, and save them as two string
    private void prepareGPSconfiguration(){
        // get the current location from Intent or from the the fused client
        // update the text view
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // If the intent has GPS inforamtion already, we do not need to obtain from Google API:
        if (ShouldReadFromIntent){
            GPSUpdatreManager gpsUpdatreManager = new GPSUpdateWithIntentClient(textView_lat,textView_lon,incomingIntent);
            return;
        }

        // If the intent has not GPS information already, we need to obtain from the Google API:
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED ){
            // if have the permission for both GPS and Camera already
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location ==null ){
                        currentLat = "No GPS information found";
                        currentLon = "No GPS information found";
                    }else {
                        currentLat = String.valueOf(location.getLatitude());
                        currentLon = String.valueOf(location.getLongitude());
                        Toast.makeText(mycontext,"GPS Obtained", Toast.LENGTH_SHORT).show();
                        GPSUpdatreManager gpsUpdatreManager = new GPSUpdateWithGPSValueClient(textView_lat,textView_lon,currentLat,currentLon);
                    }
                }
            });

        }else{
            // if we do not have GPS permission, we are going to ask for it
            currentLat = "Please give GPS Permission";
            currentLon = "Please give GPS Permission";
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
            case PERMISSION_GRANTED:
                if (sum==0) {
//                    updateGPS();
                    prepareGPSconfiguration();
                    GPSUpdatreManager gpsUpdatreManager = new GPSUpdateWithGPSValueClient(textView_lat, textView_lon, currentLat, currentLon);
                }
                else
                    Toast.makeText(this,"This app requires the permissions to be granted in order to work properly", Toast.LENGTH_SHORT).show();
                break;
        }
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


    protected class LoadStuffTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void ... params) // your background thread calls this method
        {
            // do your loading
            prepareGPSconfiguration();
            return null;
        }

        @Override
        protected void onPostExecute (Void v) // this method is called when doInBackground returns - once it finishes, the AsyncTask will have its status set to FINISHED
        {
            // up to you if you want to do anything here
        }
    };



}