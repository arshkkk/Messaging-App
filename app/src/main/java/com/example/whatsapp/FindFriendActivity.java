package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendActivity extends AppCompatActivity {

    private DatabaseReference refUsers;
    private int i;
    private Toolbar toolbar;
    private RecyclerView findFriendRecyclerView;
    private FirebaseRecyclerAdapter<Contact,ContactsAdapterForFindFriendActivity.ContactViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findFriendRecyclerView = (RecyclerView)findViewById(R.id.findFriendRecyclerView);

        FirebaseRecyclerOptions<Contact> options =
                new FirebaseRecyclerOptions.Builder<Contact>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), Contact.class)
                        .build();

        adapter = new ContactsAdapterForFindFriendActivity(options,this);
        findFriendRecyclerView.setAdapter(adapter);
        findFriendRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        Log.i("Adapter Set","Yes");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
             }
        });
        getSupportActionBar().setTitle("Find Friends");

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

        Log.i("Adapter Listening", "Yes");




//        findFriendRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        fetch();
//        findFriendRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        findFriendRecyclerView.setAdapter(tempAdapter);
//        tempAdapter.startListening();
//
//
//        Toast.makeText(getApplicationContext(),Integer.toString(i),Toast.LENGTH_LONG ).show();





    }
//
//    private void fetch()
//    {
//        Query query = FirebaseDatabase.getInstance()
//                .getReference()
//                .child("Users");
//
//        FirebaseRecyclerOptions<Contact> options = new FirebaseRecyclerOptions.Builder<Contact>().setQuery(query,Contact.class).build();
//        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Contact, FindFriendViewHolder>(options) {
//            @NonNull
//            @Override
//            public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
//
//                return new FindFriendViewHolder(view);  }
//
//
//            @Override
//            protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, int position, @NonNull Contact model) {
//                holder.nameViewHolder.setText(model.getName());
//                i++;
//                holder.statusViewHolder.setText(model.getStatus());
//                Picasso.get().load(model.getImage()).into(holder.imageViewHolder);
//
//                Log.i("Username",model.getName());
//                Log.i("Status",model.getStatus());
//                Log.i("ImageUrl",model.getImage());
//            }
//
//
//        };
//
//        findFriendRecyclerView.setAdapter(adapter);
//        adapter.startListening();
////        Toast.makeText(getApplicationContext(),adapter.getItemCount(),Toast.LENGTH_LONG).show();
//        tempAdapter=adapter;
//
//    }
//
//
//    public static class FindFriendViewHolder extends RecyclerView.ViewHolder
//    {
//        TextView nameViewHolder, statusViewHolder;
//        CircleImageView imageViewHolder;
//
//        public FindFriendViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            nameViewHolder = itemView.findViewById(R.id.name_in_contact_item);
//            statusViewHolder = itemView.findViewById(R.id.status_in_contact_item);
//            imageViewHolder = itemView.findViewById(R.id.image_in_contact_item);
//
//        }
//    }

    public void openProfileDialogInFindFriendActivity(final String clickedUserId, final String nameOfUser)
    {
        Log.i("In Open Dialog","Ok");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_profile,null);

        Button sendMessageButton = view.findViewById(R.id.sendMessageButton);
        Button extraButton = view.findViewById(R.id.extraButton);
        final CircleImageView profileImageViewInDialog = view.findViewById(R.id.profileImageInDialog);
        TextView nameInDialogBox = view.findViewById(R.id.nameInDialog);
        nameInDialogBox.setText(nameOfUser);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindFriendActivity.this, ChatActivity.class);
                intent.putExtra("receiverName", nameOfUser);
                intent.putExtra("receiverUserId",clickedUserId);

                startActivity(intent);
            }
        });

        extraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Extra Button is Clicked", Toast.LENGTH_LONG);

            }
        });


        FirebaseDatabase.getInstance().getReference().child("Users").child(clickedUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&&dataSnapshot.hasChild("image"))
                {
                    String imageUrl = dataSnapshot.child("image").getValue().toString();

                    Picasso.get().load(imageUrl).placeholder(R.drawable.profile_image).into(profileImageViewInDialog);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        builder.setView(view);




        final AlertDialog alertDialog = builder.create();

        //For Dialog Animation
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Vertical;

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
