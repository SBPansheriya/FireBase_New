package com.kmsoft.firebase_new;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kmsoft.firebase_new.databinding.ActivityAddBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Add_Activity extends AppCompatActivity {

    ActivityAddBinding binding;
    FirebaseFirestore firebaseFireStorage;
    ArrayList<Users> usersArrayList;
    Users users;
    FirebaseStorage firebaseStorage;
    StorageReference imgfolder;
    String imgURI;
    Uri resultUri;
    String button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseFireStorage = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        String imgname = String.valueOf(new Random().nextInt() * 10000 + ".jpg");
        imgfolder = firebaseStorage.getReference().child("Images/" + imgname);

        String name = getIntent().getStringExtra("name");
        String number = getIntent().getStringExtra("number");
        String image = getIntent().getStringExtra("img");

        if (name != null) {
            binding.productname.setText("" + name);
            binding.productnumber.setText("" + number);

            Glide.with(getApplicationContext())
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.productimg);
            button = getIntent().getStringExtra("button");
        }

        button = getIntent().getStringExtra("button");
        if (button.equals("add")) {
            binding.consubmit.setVisibility(View.VISIBLE);
        }
        if (button.equals("update")) {
            binding.conupdate.setVisibility(View.VISIBLE);
        }

        binding.productimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });

        binding.consubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Name = binding.productname.getText().toString();
                String Number = binding.productnumber.getText().toString();

                if (TextUtils.isEmpty(Name)) {
                    Toast.makeText(Add_Activity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Number)) {
                    Toast.makeText(Add_Activity.this, "Enter Number", Toast.LENGTH_SHORT).show();
                } else {
                    uploadimage();
                }
            }
        });

        usersArrayList = new ArrayList<>();
        binding.conupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateimage();
//                firebaseFireStorage = FirebaseFirestore.getInstance();
//
//                users = new Users(id,binding.productname.getText().toString(),binding.productnumber.getText().toString(),imgURI);
//                firebaseFireStorage = FirebaseFirestore.getInstance();
//
//                Map<String, Object> updateMap = new HashMap<>();
//
//                updateMap = new Users().getHashMap(users);
//
//                firebaseFireStorage.collection("Contact").document(id).update(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(Add_Activity.this, "Course has been updated", Toast.LENGTH_SHORT).show();
//                        for (int i = 0; i < usersArrayList.size(); i++) {
//                            if (usersArrayList.get(i).getUid().equalsIgnoreCase(id)) {
//                                usersArrayList.remove(i);
//                                usersArrayList.add(i, users);
//                                break;
//                            }
//                        }
//                        Intent intent = new Intent(Add_Activity.this,MainActivity.class);
//                        startActivity(intent);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(Add_Activity.this, "Failed to update the data", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
    }

    private void updateimage() {
        binding.productimg.setDrawingCacheEnabled(true);
        binding.productimg.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) binding.productimg.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgfolder.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return imgfolder.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String id = getIntent().getStringExtra("id");
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Log.d("PPP", "onComplete: " + downloadUri);
                            imgURI = String.valueOf(downloadUri);

                            firebaseFireStorage = FirebaseFirestore.getInstance();

                            users = new Users(id, binding.productname.getText().toString(), binding.productnumber.getText().toString(), imgURI);
                            firebaseFireStorage = FirebaseFirestore.getInstance();

                            Map<String, Object> updateMap = new HashMap<>();

                            updateMap = new Users().getHashMap(users);

                            firebaseFireStorage.collection("Contact").document(id).update(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Add_Activity.this, "Course has been updated", Toast.LENGTH_SHORT).show();
                                    for (int i = 0; i < usersArrayList.size(); i++) {
                                        if (usersArrayList.get(i).getUid().equalsIgnoreCase(id)) {
                                            usersArrayList.remove(i);
                                            usersArrayList.add(i, users);
                                            break;
                                        }
                                    }
                                    Intent intent = new Intent(Add_Activity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Add_Activity.this, "Failed to update the data", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
            }
        });
    }

    private void uploadimage() {
        binding.productimg.setDrawingCacheEnabled(true);
        binding.productimg.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) binding.productimg.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgfolder.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return imgfolder.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Log.d("PPP", "onComplete: " + downloadUri);
                            imgURI = String.valueOf(downloadUri);
                            DocumentReference docref = firebaseFireStorage.collection("Contact").document();
                            Users users = new Users(docref.getId(), binding.productname.getText().toString(), binding.productnumber.getText().toString(), imgURI);
                            docref.set(users);
                            Intent intent = new Intent(Add_Activity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
            }
        });
    }

    private void getImage() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(Add_Activity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                binding.productimg.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
//    public void UpdateDatatoFirestore(String uid, String str_updateName, String str_updateAge, int position) {
//
//        users = new Users(str_updateName,str_updateAge,uid);
//        firebaseFireStorage = FirebaseFirestore.getInstance();
//
//        Map<String,Object> updateMap = new HashMap<>();
//
//        updateMap =  new Users().getHashMap(users);
//
//        firebaseFireStorage.collection("Contact").document(uid).update(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(Add_Activity.this, "Course has been updated", Toast.LENGTH_SHORT).show();
//
//                for(int i= 0;i<usersArrayList.size();i++){
//                    if(usersArrayList.get(i).getUid().equalsIgnoreCase(uid)){
//
//                        usersArrayList.remove(i);
//                        usersArrayList.add(i,users);
////                        adp.notifyDataSetChanged();
//                        break;
//                    }
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Add_Activity.this, "Failed to update the data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


//    public void UpdateData() {
//
//    }
}