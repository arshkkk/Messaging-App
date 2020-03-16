package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scottyab.aescrypt.AESCrypt;
import com.squareup.picasso.Picasso;

import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private ImageButton sendMessageButton;
    private ImageButton addImageToSend;
    private EditText messageText;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private String receiverUserId;
    private DatabaseReference chatReference;
    private MessageAdapterForChatActivity messageAdapter;
    private RecyclerView userMessagesList;
    private final List<MessageClass> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference refForShowingChatsSender;
    private DatabaseReference refForShowingChatsReceiver;
    private DatabaseReference refForNotifications;
    private String receiverName;
    private DatabaseReference publicKeyReferenceOfReceiver;
    private String publicKeyForEncrption;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        initToolbar();
        initViews();

        getValuesFromIntent();
//        publicKeyReferenceOfReceiver = FirebaseDatabase.getInstance().getReference().child("Public Keys").child(receiverUserId).child("key");
//        publicKeyReferenceOfReceiver.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists())
//                {
//                    publicKeyForEncrption = dataSnapshot.getValue().toString();
//                    sendMessageButton.setEnabled(true);
//                    Toast.makeText(getApplicationContext(), publicKeyForEncrption, Toast.LENGTH_LONG).show();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "Please Tell the Another User to Login Again for Encryption",Toast.LENGTH_LONG).show();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



        initMessageList();
        initFirebaseReferences();
        refForShowingChatsSender = FirebaseDatabase.getInstance().getReference().child("ChatsActivityList").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(receiverUserId);
        refForShowingChatsReceiver = FirebaseDatabase.getInstance().getReference().child("ChatsActivityList").child(receiverUserId).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        refForNotifications = FirebaseDatabase.getInstance().getReference().child("Notifications");

        DatabaseReference RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.child("Chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(receiverUserId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {
                        MessageClass messages = dataSnapshot.getValue(MessageClass.class);

                        messagesList.add(messages);

                        messageAdapter.notifyDataSetChanged();

                        userMessagesList.smoothScrollToPosition(userMessagesList.getAdapter().getItemCount());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initMessageList(){
        messageAdapter = new MessageAdapterForChatActivity(messagesList);
        userMessagesList = (RecyclerView) findViewById(R.id.chatActivityRecylerView);
        linearLayoutManager = new LinearLayoutManager(this);
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messageAdapter);
    }
    private void sendMessageToDatabase()
    {

        String newMessageKey = chatReference.push().getKey();
        Calendar calForDateAndTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd / MM / yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        final String currDate = dateFormat.format(calForDateAndTime.getTime());
        String currTime = timeFormat.format(calForDateAndTime.getTime());
        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String planeTextMessage= messageText.getText().toString();
        String encryptedMsg="NULL";
//        Encryption newEncryption = new Encryption(publicKeyForEncrption, planeTextMessage);
//        String encryptedText = newEncryption.getEncryptedText();
//        String encryptedSecretKey = newEncryption.getEncryptedSecretKey();

        //Encryption
        String password = "password";
        try {
            encryptedMsg = AESCrypt.encrypt(password, planeTextMessage);
        }catch (GeneralSecurityException e){
            //handle error
        }





        MessageClass newMessageForSender = new MessageClass(encryptedMsg,"Secret Key", currDate, currTime,"sent", currentUserId, receiverUserId);
        MessageClass newMessageForReceiver = new MessageClass(encryptedMsg,"Secret Key", currDate, currTime,"received",currentUserId, receiverUserId);

        String messageSenderRef = "Chats/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + receiverUserId;
        String messageReceiverRef = "Chats/" + receiverUserId + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map messageBodyDetails = new HashMap();
        messageBodyDetails.put(messageSenderRef + "/" + newMessageKey, newMessageForSender);
        messageBodyDetails.put( messageReceiverRef + "/" + newMessageKey, newMessageForReceiver);

        FirebaseDatabase.getInstance().getReference().updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task)
        {
            if (task.isSuccessful())
            {
                messageText.setText("");

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("type", "message");
                hashMap.put("from", currentUserId);
                String newNotificationKey = refForNotifications.child(currentUserId).push()
                        .getKey();
                refForNotifications.child(currentUserId).child(newNotificationKey).setValue(hashMap);

                //For Chat Activity when New Chat is Added for First Time
                refForShowingChatsSender.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!(dataSnapshot.exists())){
                            refForShowingChatsSender.setValue(receiverUserId);
                            refForShowingChatsReceiver.setValue(currentUserId);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                Toast.makeText(ChatActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

        }
    });

    }
    private void initFirebaseReferences()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        chatReference = databaseReference.child("Chats").child(currentUserId).child(receiverUserId);
    }
    private void initToolbar()
    {
        Toolbar toolbar= (Toolbar)findViewById(R.id.chatActivityToolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setTitle(receiverName);

    }
    private void initViews()
    {
        sendMessageButton = findViewById(R.id.sendMessageButton);
        sendMessageButton.setEnabled(true);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkConnectivity.isOnline(getApplicationContext()))
                sendMessageToDatabase();

                else
                    Toast.makeText(getApplicationContext(),"No Internet Connection", Toast.LENGTH_LONG).show();

            }
        });
        messageText = findViewById(R.id.messageText);
        addImageToSend = findViewById(R.id.addImage);
    }
    private void getValuesFromIntent()
    {
        receiverUserId = getIntent().getExtras().get("receiverUserId").toString();
        receiverName = getIntent().getExtras().get("receiverName").toString();
    }


}
