package com.company.watsloo.data;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.company.watsloo.DetailActivity;
import com.company.watsloo.MainActivity;
import com.company.watsloo.MapsActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
                storiesRef.setValue(existedStories).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,
                                "Succeed to add the item!", Toast.LENGTH_SHORT).show();

                        // 子硕我在着往main activity 发一个 intent哈，这样成功了我就可以跳转走了 YW
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("ON_DATA_SUCCESSFUL_UPLOAD","TURE");
                        context.startActivity(intent);
//                        context.startActivityForResult(intent, requestCode);
                    }
                }).addOnFailureListener(new OnFailureListener() {
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

        // store the image in the Storage of firebase
        UploadTask uploadTask = bitmapRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference myStorageRef = FirebaseStorage.getInstance().getReference(bitmapName);
                myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String filepath = uri.toString();

                        dbRef.child(itemName).child("images").child(randomId.toString())
                                .setValue(filepath).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context,
                                                "Fail to store the image url into realtime database!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
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

    public static void addSingleField(Context context, DatabaseReference ref,
                                      String fieldName, boolean fieldValue) {
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

    public static void readInfoFromFirebase(Context context) {
        List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Position ps = ds.getValue(Position.class);
                    JSONObject jo = new JSONObject();

                    try {
                        jo.put("name", ds.getKey());
                        jo.put("description", ps.description);
                        jo.put("latitude", ps.latitude);
                        jo.put("longitude", ps.longitude);
                        jo.put("isAudited", ps.isAudited);
                        jo.put("isEasterEgg", ps.isEasterEgg);
//                        JSONObject images = new JSONObject();
//                        Iterator iter = ps.images.entrySet().iterator();
//                        while(iter.hasNext()) {
//                            Map.Entry entry = (Map.Entry) iter.next();
//                            Object key = entry.getKey();
//                            Object value = entry.getValue();
//                            images.put((String)key, (String)value);
//                        }
                        jo.put("images", ps.images);

//                        JSONArray stories = new JSONArray();
//                        for(int i = 0; i < ps.getStoryNum(ps.stories); i++) {
//                            stories.put(ps.stories.get(i));
//                        }
                        jo.put("stories", ps.stories);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    jsonObjectList.add(jo);
                }
                writeFileOnInternalStorage(context, "spots.json", jsonObjectList.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });
    }

    public static void writeFileOnInternalStorage(Context context, String filename, String data) {
        File dir = new File(context.getFilesDir(), "offline");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            File file = new File(dir, filename);
            FileWriter writer = new FileWriter(file);
            writer.append(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String readFileFromInternalStorage(Context context, String filename) {
        StringBuffer data = new StringBuffer();

        File dir = new File(context.getFilesDir(), "offline");

        try {
            File file = new File(dir, filename);
            FileReader reader = new FileReader(file);

            int ch;
            while ((ch = reader.read()) != -1) {
                data.append((char) ch);
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data.toString();
    }

    // return audited data's hashmap
    public static Map<String, double[]> stringToSpotsMap(String data) {
        Map<String, double[]> res = new HashMap<>();

        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                boolean isAudited = jsonObject.getBoolean("isAudited");
                boolean isEasterEgg = jsonObject.getBoolean("isEasterEgg");

                if (isAudited && !isEasterEgg) {
                    double[] pos = new double[2];
                    pos[0] = jsonObject.getDouble("latitude");
                    pos[1] = jsonObject.getDouble("longitude");
                    res.put(jsonObject.getString("name"), pos);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    // return easter eggs' hashmap
    public static Map<String, double[]> stringToEggsMap(String data) {
        Map<String, double[]> res = new HashMap<>();

        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                boolean isAudited = jsonObject.getBoolean("isAudited");
                boolean isEasterEgg = jsonObject.getBoolean("isEasterEgg");

                if (isAudited && isEasterEgg) {
                    double[] pos = new double[2];
                    pos[0] = jsonObject.getDouble("latitude");
                    pos[1] = jsonObject.getDouble("longitude");
                    res.put(jsonObject.getString("name"), pos);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    // return data based on name
    public static JSONObject stringToDetails(String data, String name) {

        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("name").equals(name)) {
                    return jsonObject;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // return data based on name+story key
    public static JSONObject stringToStory(String data, String name) {

        JSONObject res = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("name").equals(name)) {
                    String stories = jsonObject.getString("stories");
                    String images = jsonObject.getString("images");

                    System.out.println(images);

                    stories = stories.substring(1, stories.length());
                    String[] storiesList = stories.split("\\$+,|\\$+]");

                    images = images.substring(1, images.length() - 1);
                    String[] imagesList = images.split(",");

                    Random r = new Random();
                    int imageRand = r.nextInt(imagesList.length - 1);
                    int storyRand = r.nextInt(storiesList.length - 1);

                    String[] urlList = imagesList[imageRand].split("=");
                    StringBuilder sb = new StringBuilder();
                    for (int k = 1; k < urlList.length; k++) {
                        sb.append(urlList[k]);
                        sb.append("=");
                    }
                    sb.substring(0, sb.length() - 1);

                    res.put("name", name);
                    res.put("image", sb.toString());
                    res.put("story", storiesList[storyRand]);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
}
