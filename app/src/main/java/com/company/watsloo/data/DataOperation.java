package com.company.watsloo.data;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.company.watsloo.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

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
        });;
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
    }
}
