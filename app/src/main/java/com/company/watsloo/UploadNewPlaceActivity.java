package com.company.watsloo;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadNewPlaceActivity extends AppCompatActivity {
    Button albumBtn, cameraBtn, submitBtn;
    ImageView imageView;
    private final static int SELECT_PHOTOT_FROM_ALBUM = 250;
    private final static int REQUEST_IMAGE_CAPTURE   = 520;
    private final static int REQUEST_FULL_CAMERA_IMAGE   = 100;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    private String FILE_NAME="photo.jpg";
    private File photoFile;
    String currentPhotoPath;
    private Uri fileProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_place);

        if(!checkPermissionForCamera())
            requestPermissionForCamera();

        // initialize all the btns, and editText, and imageView;
        imageView = findViewById(R.id.newPlaceImageView);
        albumBtn = findViewById(R.id.button_choose_from_album);
        cameraBtn = findViewById(R.id.button_choose_from_camera);
        submitBtn = findViewById(R.id.button_submit_new_place);

        albumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickfromAlbum = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickfromAlbum,SELECT_PHOTOT_FROM_ALBUM);
            }
        });

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

    public void requestPermissionForCamera(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            Toast.makeText(this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    public void takePicture(View v){
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


}