package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;


    private  TabAccessorAdapter myTabAccessorAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        PrivateKey.setPrivateKey(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("private_key", "NoKey"));
//
//
//        if(PrivateKey.getPrivateKey().equals("NoKey")){
//            moveToLogin();
//        }
//
//        getPrivateKeyFromFirebase();

        toolbar= (Toolbar)findViewById(R.id.main_page_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("WhatsApp");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.myTabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Chats"));
        tabLayout.addTab(tabLayout.newTab().setText("Groups"));
        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
        final ViewPager viewPager =(ViewPager)findViewById(R.id.myViewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        TabAccessorAdapter tabsAdapter = new TabAccessorAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        }

        @Override
        protected void onStart()
        {
            super.onStart();
            mAuth=FirebaseAuth.getInstance();
            user= mAuth.getCurrentUser();
            if(user==null)
            {
                moveToLogin();
            }
            else {

            }



        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection


        switch (item.getItemId()) {
            case R.id.setting:
               moveToSetting();
                return true;
            case R.id.signOut:
                final ProgressDialog progressDialog= new ProgressDialog(this);
                progressDialog.setTitle("Logging In");
                progressDialog.setMessage("Please Wait While We are Logging In");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();

                mAuth=FirebaseAuth.getInstance();
                mAuth.signOut();
                moveToLogin();
                progressDialog.dismiss();
                return true;
            case R.id.createGroup:
                createNewGroup();
                return true;

            case R.id.findFriends:
                moveToFindFriendsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private  void moveToFindFriendsActivity()
    {
        Intent intent = new Intent(MainActivity.this, FindFriendActivity.class);
        startActivity(intent);
    }

    private void createNewGroup()
    {
       final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Enter Group Name");
        final EditText groupName = new EditText(MainActivity.this);
        builder.setView(groupName);


        builder.setPositiveButton("Create Group", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            if(!TextUtils.isEmpty(groupName.getText().toString()))
            {
                addGroupToDatabase(groupName.getText().toString());
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Enter Group Name", Toast.LENGTH_LONG);
            }

        }}).setNegativeButton("Cancel",null);


       final AlertDialog alert = builder.create();
        alert.show();





    }

    private void addGroupToDatabase(final String groupName)
    {
        ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        ref.child("Groups").child(mAuth.getCurrentUser().getUid()).child(groupName).setValue(" ").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"Group Created: "+ groupName, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void moveToSetting()
    {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        intent.putExtra("UserType","OldUser");
        startActivity(intent);
    }
    private void moveToLogin()
    {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();


    }

    private void getPrivateKeyFromFirebase(){
        DatabaseReference privateKeyReference = FirebaseDatabase.getInstance().getReference().child("Private Keys").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("key");
        privateKeyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    PrivateKey.setPrivateKey(dataSnapshot.getValue().toString());

                    Toast.makeText(getApplicationContext(), PrivateKey.getPrivateKey(), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "No Private Key",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    }

