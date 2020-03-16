package com.example.whatsapp;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.UriMatcher;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.net.URI;
import java.net.URL;
import java.sql.Ref;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import com.google.firebase.storage.*;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SettingActivity extends AppCompatActivity {

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutStatus;
    private TextInputEditText name;
    private TextInputEditText status;
    private Button update;
    private Uri profileImageUri;
    private String profileImageUrl;
    private CircleImageView imageView;
    private boolean firstTimePictureChange =false;
    private Toolbar toolbar;
    private String userType;
    private String dataRecentlyUploaded;
    private AlertDialog alertDialog;



    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private DatabaseReference databaseReference;
    private static final int galleryPick =1;

    private StorageReference storageReference;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        dataRecentlyUploaded="NO";
        userType = getIntent().getExtras().get("UserType").toString();
        setToolbar();
        initViews();
        getInfo();
    }

    private void setToolbar()
    {
        toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setTitle("Setting");
    }




    public void initViews() {
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutStatus = (TextInputLayout) findViewById(R.id.textInputLayoutStatus);

        name = (TextInputEditText) findViewById(R.id.name);
        status = (TextInputEditText) findViewById(R.id.status);

        update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkConnectivity.isOnline(getApplicationContext()))
                {

                    uploadProfilePictureToDatabase();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Device is Not Connected to Internet", Toast.LENGTH_LONG);
                }
            }


        });

        imageView = (CircleImageView) findViewById(R.id.profile_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SettingActivity.this, ImageShowingForSettingActivity.class);
//                intent.putExtra()
//                intent.setType("image/*");
//                startActivityForResult(intent, galleryPick);

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1)
                        .start(SettingActivity.this);


            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if(NetworkConnectivity.isOnline(SettingActivity.this)) {
                    profileImageUri = resultUri;

                    imageView.setImageURI(resultUri);
//                    sendProfileImageToDataBase();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Device is Not Connected to Internet",Toast.LENGTH_LONG);
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



    private void getInfo()
    {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    name.setText(dataSnapshot.child("name").getValue().toString());
                    status.setText(dataSnapshot.child("status").getValue().toString());

                    Toast.makeText(getApplicationContext(), profileImageUrl, Toast.LENGTH_LONG).show();
                }
                if(dataSnapshot.exists()&&dataSnapshot.hasChild("image")){
                   profileImageUrl = dataSnapshot.child("image").getValue().toString();

                    Picasso.get().load(profileImageUrl).placeholder(R.drawable.profile_image).into(imageView);
                    firstTimePictureChange=true;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateDetailsToDatabase()
    {

//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Updating Details");
//        progressDialog.setMessage("Please Wait While We are Uploading Details to Server");
//        progressDialog.setCanceledOnTouchOutside(true);
//        progressDialog.show();



        //For Dialog Animation
//        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Vertical;

//        alertDialog.setCanceledOnTouchOutside(true);


        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();


        String userId = mAuth.getCurrentUser().getUid();
        String emailText = mAuth.getCurrentUser().getEmail();
        String nameText = name.getText().toString();
        String statusText = status.getText().toString();





        HashMap<String,String> hashMap = new HashMap<String,String>();

        hashMap.put("email",emailText);
        hashMap.put("uid",userId);
        hashMap.put("name",nameText);
        hashMap.put("status",statusText);
        hashMap.put("image",profileImageUrl);

        Log.i("Database Uploaded","OK");

        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

//                progressDialog.dismiss();
                alertDialog.dismiss();

            }

        });

        if(profileImageUri==null)
        {
            alertDialog.dismiss();
        }
        else {
            alertDialog.dismiss();
        }

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            moveToMainActivity();
        }
        else {
            moveToLoginActivity();
        }
    }

    private  void uploadProfilePictureToDatabase()
    {
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Updating Profile Picture");
//        progressDialog.setMessage("Please Wait We are Uploading Profile Picture");
//        progressDialog.setCanceledOnTouchOutside(true);
//        progressDialog.show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.spinner_loader,null);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        if(profileImageUri!=null){


            storageReference = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(mAuth.getCurrentUser().getUid()).child("image.png");
            storageReference.putFile(profileImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(getApplicationContext(),"File Uploaded", Toast.LENGTH_LONG).show();
//                progressDialog.dismiss();
                    getImageUrl();

                }
            });
        }
        else{
            getImageUrl();
        }



    }

    private void getImageUrl()
    {
       if(profileImageUri!=null){
           storageReference = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(mAuth.getCurrentUser().getUid()).child("image.png");
           storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
               @Override
               public void onSuccess(Uri uri) {
                   // Got the download URL for 'users/me/profile.png'
                   profileImageUrl = uri.toString();
                   updateDetailsToDatabase();
                   Toast.makeText(getApplicationContext(),profileImageUrl , Toast.LENGTH_LONG).show();
                   Log.i("Profile Url",profileImageUrl);

               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception exception) {
                   // Handle any errors
               }
           });
       }
    else if(profileImageUrl==null&&profileImageUri==null){
        profileImageUrl="NoImage";
        updateDetailsToDatabase();
    }
    else if(profileImageUri==null&&profileImageUrl!=null)
       {
           updateDetailsToDatabase();
       }

    }




    private  void moveToMainActivity()
    {
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private  void moveToLoginActivity()
    {
        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}




