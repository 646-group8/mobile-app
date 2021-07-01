package com.company.watsloo.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.company.watsloo.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataOperation {

    private static DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private static StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public static void addItem(Context context, Item item) {
        DatabaseReference itemRef = dbRef.child(item.getName());
        itemRef.child("latitude").setValue(item.getLatitude()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context,
                        "Successfully add latitude!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,
                        "Fail to add latitude!", Toast.LENGTH_SHORT).show();
            }
        });
        itemRef.child("longitude").setValue(item.getLongitude()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context,
                        "Successfully add longitude!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,
                        "Fail to add longitude!", Toast.LENGTH_SHORT).show();
            }
        });
        itemRef.child("description").setValue(item.getDescription()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context,
                        "Successfully add description!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,
                        "Fail to add description!", Toast.LENGTH_SHORT).show();
            }
        });

        if (!item.getStories().isEmpty()) {
            addStories(context, item.getName(), item.getStories());
        }
    }

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
    public static void addBitmap(Context context, String itemName, Bitmap bitmap) {
        UUID randomId = UUID.randomUUID();
        String bitmapName = itemName + "/" + randomId + ".bmp";
        StorageReference bitmapRef = storageRef.child(bitmapName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = bitmapRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                String filepath = taskSnapshot.getMetadata().getPath();
                dbRef.child(itemName).child("images").child(randomId.toString()).setValue(filepath).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,
                                "Successfully stored the image url!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,
                                "Fail to store the image url!", Toast.LENGTH_SHORT).show();
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
}
