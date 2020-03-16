//package com.example.whatsapp;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.UriMatcher;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.material.textfield.TextInputLayout;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.Picasso;
//import com.theartofdev.edmodo.cropper.CropImage;
//
//import java.net.URI;
//import java.net.URL;
//import java.sql.Ref;
//import java.util.HashMap;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import com.google.firebase.storage.*;
//import com.theartofdev.edmodo.cropper.CropImageView;
//
//public class SettingActivity {
//
//    private TextInputLayout textInputLayoutName;
//    private TextInputLayout textInputLayoutStatus;
//    private TextInputEditText name;
//    private TextInputEditText status;
//    private Button update;
//    private Uri profileImageUri;
//    private String profileImageUrl = " ";
//    private CircleImageView imageView;
//    private boolean imageChange=false;
//    private boolean statusChange = false;
//    private boolean nameChange = false;
//
//    private FirebaseAuth mAuth;
//    private FirebaseUser user;
//
//    private DatabaseReference ref;
//    private static final int galleryPick =1;
//
//    private StorageReference mRef;
//
//
//
//    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_setting);
//
//        initViews();
//        getInfo();
//        getProfilePicture();
//    }
//
//
//
//
//
//    public void initViews() {
//        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
//        textInputLayoutStatus = (TextInputLayout) findViewById(R.id.textInputLayoutStatus);
//
//        name = (TextInputEditText) findViewById(R.id.name);
//        status = (TextInputEditText) findViewById(R.id.status);
//
//        update = (Button) findViewById(R.id.update);
//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               if(NetworkConnectivity.isOnline(getApplicationContext()))
//               {
//                   addDetails();
//               }
//               else{
//                   Toast.makeText(getApplicationContext(),"Device is Not Connected to Internet", Toast.LENGTH_LONG);
//               }
//            }
//
//
//        });
//
//        imageView = (CircleImageView) findViewById(R.id.profile_image);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(SettingActivity.this, ImageShowingForSettingActivity.class);
////                intent.putExtra()
////                intent.setType("image/*");
////                startActivityForResult(intent, galleryPick);
//
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .start(SettingActivity.this);
//
//
//            }
//        });
//
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//       super.onActivityResult(requestCode,resultCode,data);
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                profileImageUri = resultUri;
////                imageView.setImageURI(resultUri);
//                sendProfileImageToDataBase();
//
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
//    }
//
//    private void sendProfileImageToDataBase()
//    {
//        mAuth = FirebaseAuth.getInstance();
//
//
//        mRef = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(mAuth.getCurrentUser().getUid()).child("image.png");
//
//
//        mRef.putFile(profileImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//
//                Toast.makeText(getApplicationContext(), profileImageUrl,Toast.LENGTH_LONG ).show();
//
//            }
//        });
//
//        mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                // Got the download URL for 'users/me/profile.png'
//
//                Picasso.get().load(String.valueOf(uri)).into(imageView);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });
//
//
//    }
//    private void getInfo()
//    {
//        mAuth = FirebaseAuth.getInstance();
//        ref = FirebaseDatabase.getInstance().getReference();
//
//        ref.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()&&dataSnapshot.hasChild("name"))
//                {
//                    name.setText(dataSnapshot.child("name").getValue().toString());
//                    status.setText(dataSnapshot.child("status").getValue().toString());
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }
//a
//    private void getProfilePicture()
//    {
//        mAuth = FirebaseAuth.getInstance();
//        ref= FirebaseDatabase.getInstance().getReference();
//        mRef = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(mAuth.getCurrentUser().getUid()).child("image.png");
//
//
//
//
//        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//
//        ref.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()&&dataSnapshot.hasChild("image"))
//                {
//                    if(!dataSnapshot.child("image").getValue().toString().equals(" "))
//                        mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                // Got the download URL for 'users/me/profile.png'
//                                profileImageUrl = String.valueOf(uri);
//                                Picasso.get().load(String.valueOf(uri)).into(imageView);
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                // Handle any errors
//                            }
//                        });
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//
//    });
//    }
//
//        private void addDetails()
//        {
//
//
//
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Updating Details");
//            progressDialog.setMessage("Please Wait While We are Uploading Details to Server");
//            progressDialog.setCanceledOnTouchOutside(true);
//            progressDialog.show();
//
//            mAuth = FirebaseAuth.getInstance();
//            ref = FirebaseDatabase.getInstance().getReference();
//
//            String userId = mAuth.getCurrentUser().getUid();
//            String emailText = mAuth.getCurrentUser().getEmail();
//            String nameText = name.getText().toString();
//            String statusText = status.getText().toString();
//
//            HashMap<String,String> hashMap = new HashMap<>();
//            hashMap.put("email",emailText);
//            hashMap.put("uid",userId);
//            hashMap.put("name",nameText);
//            hashMap.put("status",statusText);
//            hashMap.put("image", profileImageUrl);
//
//            ref.child("Users").child(userId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    progressDialog.dismiss();
//                    Snackbar.make(update, "Welcome to The App", Snackbar.LENGTH_SHORT)
//                            .show();
//
//                    if(mAuth.getCurrentUser()!=null)
//                    {
//                        moveToMainActivity();
//                    }
//                    else{
//                        moveToLoginActivity();
//                    }
//                }
//
//
//            });
//        }
//
//
//        private  void moveToMainActivity()
//        {
//            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//        }
//
//        private  void moveToLoginActivity()
//        {
//            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//        }
//
//
//    }
//
//
//
//
