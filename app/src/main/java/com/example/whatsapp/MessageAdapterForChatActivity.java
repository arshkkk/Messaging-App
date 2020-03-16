package com.example.whatsapp;


import android.graphics.Color;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scottyab.aescrypt.AESCrypt;
import com.squareup.picasso.Picasso;

import java.security.GeneralSecurityException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapterForChatActivity extends RecyclerView.Adapter<MessageAdapterForChatActivity.MessageViewHolder>
{
    private List<MessageClass> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;


    public MessageAdapterForChatActivity (List<MessageClass> userMessagesList)
    {
        this.userMessagesList = userMessagesList;
    }



    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView sentMessageText;
        public TextView receivedTextMessage;



        public MessageViewHolder(@NonNull View itemView)
        {
            super(itemView);

            sentMessageText = (TextView) itemView.findViewById(R.id.sentMessage);
            receivedTextMessage =(TextView)itemView.findViewById(R.id.receivedMessage);


        }
    }




    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.message_item, viewGroup, false);

        return new MessageViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, int i)
    {
        MessageClass messages = userMessagesList.get(i);

        String type = messages.getType();

        messageViewHolder.receivedTextMessage.setVisibility(View.GONE);
        messageViewHolder.sentMessageText.setVisibility(View.GONE);

//        String privateKey = PrivateKey.getPrivateKey();
//        String encryptedKey = messages.getKey();
//        String encryptedMessage = messages.getMessage();
//        Decryption newDecryption = new Decryption(encryptedMessage,encryptedKey,privateKey);
//        String decryptedMessage = newDecryption.getDecryptedMessage();

        String password = "password";
        String encryptedMsg = messages.getMessage();
        String decryptedMessage="NULL";
        try {
            decryptedMessage = AESCrypt.decrypt(password, encryptedMsg);
        }catch (GeneralSecurityException e){
            //handle error - could be due to incorrect password or tampered encryptedMsg
        }

        String messageToSet = decryptedMessage + "\n\n" + messages.getTime();
        Spannable wordtoSpan = new SpannableString(messageToSet);
        wordtoSpan.setSpan(new RelativeSizeSpan(0.5f), messageToSet.length()-12,messageToSet.length(), 0); // set size



        if (type.equals("sent")) {

            messageViewHolder.sentMessageText.setVisibility(View.VISIBLE);
            messageViewHolder.sentMessageText.setText(wordtoSpan);
//            messageViewHolder.sentMessageText.setText(messages.getMessage()+"\n"+Html.fromHtml("<html><body><font size=10 color=white>messages.getTime()</font>World</body><html>" ));

//            messageViewHolder.sentMessageText.setText(messages.getMessage() + "\n \n" + messages.getTime() + " - " + messages.getDate());
        }
            else
            {
                messageViewHolder.receivedTextMessage.setVisibility(View.VISIBLE);
                messageViewHolder.receivedTextMessage.setText(wordtoSpan);
//                messageViewHolder.receivedTextMessage.setText(messages.getMessage() + "\n \n" + messages.getTime() + " - " + messages.getDate());
//                messageViewHolder.sentMessageText.setText(messages.getMessage()+"\n"+Html.fromHtml("<html><body><font size=10 color=white>messages.getTime()</font>World</body><html>" ));
            }

    }




    @Override
    public int getItemCount()
    {
        return userMessagesList.size();
    }

}