package com.example.whatsapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapterForFindFriendActivity extends FirebaseRecyclerAdapter<Contact,ContactsAdapterForFindFriendActivity.ContactViewHolder>   {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    FindFriendActivity objForFunctionCalling;

    public ContactsAdapterForFindFriendActivity(@NonNull FirebaseRecyclerOptions<Contact> options, FindFriendActivity objForFunctionCalling) {
        super(options);
        this.objForFunctionCalling = objForFunctionCalling;
    }

    @Override
    protected void onBindViewHolder(@NonNull ContactViewHolder holder, final int position, @NonNull Contact model) {

//        if(model.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
//        {
//            holder.parentTemp.removeAllViewsInLayout();
//        }
//        else{
            holder.name.setText(model.getName());
            holder.status.setText(model.getStatus());

            Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_image).into(holder.imageView);

            Log.i("InOnBindViewHolder","Yes");


            final String nameOfUserForFunctionParsinng = model.getName();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String clickedUserId = getRef(position).getKey();
                    objForFunctionCalling.openProfileDialogInFindFriendActivity(clickedUserId, nameOfUserForFunctionParsinng);

                }
            });



    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        Log.i("InOnCreateViewHolder","Yes");
        return new ContactViewHolder(view, parent);
    }

    public static class  ContactViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name,status;
        View itemViewTemp;
        ViewGroup parentTemp;

        public ContactViewHolder(@NonNull View itemView, ViewGroup parent) {
            super(itemView);
            itemViewTemp = itemView;
            parentTemp = parent;
            imageView = (CircleImageView)itemView.findViewById(R.id.image_in_contact_item);
            name = (TextView)itemView.findViewById(R.id.name_in_contact_item);
            status = (TextView)itemView.findViewById(R.id.status_in_contact_item);
        }

    }


    }


