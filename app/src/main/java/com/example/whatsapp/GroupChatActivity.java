package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private String currentGroupName;
    private String currentUserId;
    private String currentUsername;
    private DatabaseReference groupRef, messageRef,userRef;
    private FirebaseAuth mAuth;
    private String messageKey;
    private String message;
    private EditText editTextOfMessage;


    private ImageButton sendMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        initializeViews();
        setToolBar();
        setGroupName();
        initializeDatabaseComponents();





    }

    private void initializeDatabaseComponents()
    {
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentUserId).child(currentGroupName);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        messageKey = groupRef.push().getKey();
        messageRef = groupRef.child(messageKey);

         userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&&dataSnapshot.hasChild("name"))
                {
                    currentUsername = dataSnapshot.child("name").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void initializeViews()
    {
        editTextOfMessage = (EditText)findViewById(R.id.editTextOfMessage);
        sendMessageButton = (ImageButton) findViewById(R.id.sendMessageButton);


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = editTextOfMessage.getText().toString();
                if(NetworkConnectivity.isOnline(getApplicationContext())&&(message!=null))
                {

                    sendMessageToDataBase();
                    Toast.makeText(GroupChatActivity.this, "Your Message is Sent", Toast.LENGTH_LONG).show();
                    editTextOfMessage.setText("");

                }
                else if(message==null)
                {
                    Toast.makeText(GroupChatActivity.this, "Enter Message It can't be Empty", Toast.LENGTH_LONG).show();




                }

            }
        });

    }
    private void sendMessageToDataBase()
    {
        Calendar calForDateAndTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd, yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        String currDate = dateFormat.format(calForDateAndTime.getTime());
        String currTime = timeFormat.format(calForDateAndTime.getTime());

        message = editTextOfMessage.getText().toString();


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("message",message);
        hashMap.put("date",currDate);
        hashMap.put("time",currTime);
//        hashMap.put("messageSender", currentUsername);
        messageRef.setValue(hashMap);
    }
    private void setToolBar()
    {
        mToolbar = (androidx.appcompat.widget.Toolbar)findViewById(R.id.groupChatToolbar);
        setSupportActionBar(mToolbar);
        setSupportActionBar(mToolbar);
    }
    private void setGroupName()
    {
        currentGroupName = getIntent().getExtras().get("currentGroupName").toString();
        getSupportActionBar().setTitle("  "+ currentGroupName);
    }
}
