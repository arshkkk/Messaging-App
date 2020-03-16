package com.example.whatsapp;import android.content.Context;import android.net.ConnectivityManager;import android.net.NetworkInfo;import android.widget.Toast;import com.google.firebase.database.DataSnapshot;import com.google.firebase.database.DatabaseError;import com.google.firebase.database.DatabaseReference;import com.google.firebase.database.FirebaseDatabase;import com.google.firebase.database.ValueEventListener;public class NetworkConnectivity {    public static boolean isOnline(Context context) {        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();    }}