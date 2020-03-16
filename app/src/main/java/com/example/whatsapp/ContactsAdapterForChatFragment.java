package com.example.whatsapp;


import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;


public class ContactsAdapterForChatFragment extends RecyclerView.Adapter<ContactsAdapterForChatFragment.ContactsViewHolder>
{
    private List<Contact> userChatList;



    public ContactsAdapterForChatFragment (List<Contact> userChatList)
    {
        this.userChatList = userChatList;
    }



    public class ContactsViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView image;
        public TextView status;
        public TextView name;



        public ContactsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image_in_contact_item);
            status =(TextView)itemView.findViewById(R.id.status_in_contact_item);
            name = (TextView) itemView.findViewById(R.id.name_in_contact_item);


        }
    }




    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contact_item, viewGroup, false);

        return new ContactsViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final ContactsViewHolder contactsViewHolder, int i)
    {
        Contact contact = userChatList.get(i);

        contactsViewHolder.name.setText(contact.getName());
        contactsViewHolder.status.setText(contact.getStatus());

        if(!(contact.getImage().equals("NoImage")))
            Picasso.get().load(contact.getImage()).placeholder(R.drawable.profile_image).into(contactsViewHolder.image);
        else
            Picasso.get().load(R.drawable.profile_image).into(contactsViewHolder.image);





    }

    @Override
    public int getItemCount() {
        return userChatList.size();
    }


}