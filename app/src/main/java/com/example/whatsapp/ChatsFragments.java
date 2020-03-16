package com.example.whatsapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragments extends Fragment {

    private View view;
    private List<Contact> contactList;
    private ContactsAdapterForChatFragment contactsAdapterForChatFragment;
    private RecyclerView contactRecyclerView;
    Contact contact;

    public ChatsFragments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_chats_fragments, container, false);
        initViews();
        initRecyclerViewList();

        return view;
    }

    private void initViews() {

        contactRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewInChatPage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        contactRecyclerView.setLayoutManager(linearLayoutManager);
        contactList = new ArrayList<>();
        contactsAdapterForChatFragment = new ContactsAdapterForChatFragment(contactList);
        contactRecyclerView.setAdapter(contactsAdapterForChatFragment);
        contactRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), contactRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Contact tempContact = contactList.get(position);
                        moveToChatActivity(tempContact.getUid(),tempContact.getName());
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );




    }

    private void initRecyclerViewList() {
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.child("ChatsActivityList").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        String toGetDetailsFrom = dataSnapshot.getValue().toString();

                        RootRef.child("Users").child(toGetDetailsFrom).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists() && dataSnapshot.hasChild("image")) {
                                    contact = dataSnapshot.getValue(Contact.class);
                                    contactList.add(contact);
                                    contactsAdapterForChatFragment.notifyDataSetChanged();

                                    contactRecyclerView.smoothScrollToPosition(contactRecyclerView.getAdapter().getItemCount());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



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
    public void onStart() {
        super.onStart();


    }

    private void moveToChatActivity(String otherUserId, String otherUsername)
    {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("receiverUserId", otherUserId);
        intent.putExtra("receiverName",otherUsername );
        startActivity(intent);
    }
}
