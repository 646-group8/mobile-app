package com.company.watsloo.data;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataOperation {

    private static DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private static StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public static void addStories(Context context, String itemName, List<String> stories) {
        DatabaseReference itemRef = dbRef.child(itemName);
        DatabaseReference storiesRef = itemRef.child("stories");
        storiesRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<String> existedStories = (List<String>)dataSnapshot.getValue();
                if (existedStories == null) {
                    existedStories = new ArrayList<>();
                }
                existedStories.addAll(stories);
                storiesRef.setValue(existedStories).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,
                                "Fail to add stories!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,
                        "Fail to add stories!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ref: https://stackoverflow.com/questions/40885860/how-to-save-bitmap-to-firebase
    public static void addBitmap(Context context, String itemName, Bitmap bitmap) throws IOException {
        UUID randomId = UUID.randomUUID();
        String bitmapName = itemName + "/" + randomId + ".bmp";
        StorageReference bitmapRef = storageRef.child(bitmapName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        baos.flush();
        baos.close();

        UploadTask uploadTask = bitmapRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                String filepath = taskSnapshot.getMetadata().getPath();
                dbRef.child(itemName).child("images").child(randomId.toString()).setValue(filepath)
                        .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,
                                "Fail to store the image!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context,
                        "Fail to upload the image!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void addContactInfo(Context context, String itemName,
                                      String contactMethod, String contactInfo) {
        DatabaseReference itemRef = dbRef.child(itemName);
        addSingleField(context, itemRef, contactMethod, contactInfo);
    }

    public static void addSingleField(Context context, DatabaseReference ref,
                                       String fieldName, String fieldValue) {
        ref.child(fieldName).setValue(fieldValue).addOnFailureListener(new OnFailureListener() {
            final String text = String.format("Fail to add %s!", fieldName);

            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void addSingleField(Context context, DatabaseReference ref,
                                      String fieldName, double fieldValue) {
        ref.child(fieldName).setValue(fieldValue).addOnFailureListener(new OnFailureListener() {
            final String text = String.format("Fail to add %s!", fieldName);

            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void downloadByPath(Context context, String path) {
        StorageReference ref = storageRef.child(path);

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                String url = uri.toString();
                String fileName = path.split("/")[1];
                System.out.println(fileName);
                downloadFile(context, fileName, Environment.DIRECTORY_DOWNLOADS, url);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    public static void downloadFile(Context context, String fileName, String destDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destDirectory, fileName);
        downloadManager.enqueue(request);
    }

    public static void readPosData() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Position ps = ds.getValue(Position.class);
                    System.out.println(ps);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public static void readDataByName(String name, String property) {
        DatabaseReference ref = dbRef.child(name);
        ref.child(property).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    System.out.println(ds.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
