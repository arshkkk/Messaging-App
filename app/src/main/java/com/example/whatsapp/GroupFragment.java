package com.example.whatsapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */


public class GroupFragment extends Fragment {

    private View groupFragment;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_group;
    private DatabaseReference ref;



    public GroupFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragment = inflater.inflate(R.layout.fragment_group, container, false);

        initialize();

        Toast.makeText(getContext(),"Arsh",Toast.LENGTH_LONG).show();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

             String currentGroupName = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(getContext(),currentGroupName ,Toast.LENGTH_LONG).show();

                moveToGroupChatActivity(currentGroupName);
            }
        });

        return groupFragment;
    }
    private void moveToGroupChatActivity(String currentGroupName)
    {
        Intent intent = new Intent(getContext(), GroupChatActivity.class);
        intent.putExtra("currentGroupName",currentGroupName);
        startActivity(intent);

    }


    private void initialize()
    {
        listView = (ListView)groupFragment.findViewById(R.id.listView);
        list_group =  new ArrayList<String>();
        getData();



        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1 ,list_group);

        listView.setAdapter(arrayAdapter);
    }

    private void getData()
    {
        ref= FirebaseDatabase.getInstance().getReference();

        ref.child("Groups").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterator itr = dataSnapshot.getChildren().iterator();
                Set<String> set = new HashSet<>();

                while(itr.hasNext())
                {
                     set.add(((DataSnapshot)itr.next()).getKey());
                }

                list_group.clear();
                list_group.addAll(set);
                arrayAdapter.notifyDataSetChanged();

                Toast.makeText(groupFragment.getContext(),"Groups are Added to List", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

