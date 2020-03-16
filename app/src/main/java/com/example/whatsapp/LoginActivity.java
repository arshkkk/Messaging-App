package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText email;
    private TextInputEditText password;

    private Button signIn;

    private TextView toSignUp;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private DatabaseReference ref;
    private DatabaseReference tokenReference;
    private DatabaseReference publicKeyReference;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    public void initViews() {
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);

        signIn = (Button) findViewById(R.id.signin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }


        });


        toSignUp = (TextView) findViewById(R.id.tosignup);
        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSignUp();
            }
        });
    }

    public void moveToSignUp() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void signInUser() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("Please Wait While We are Logging In");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            ref = FirebaseDatabase.getInstance().getReference();

                            user = mAuth.getCurrentUser();
                            if (user != null) {

                                Snackbar.make(signIn, "Logged In Successfully", Snackbar.LENGTH_SHORT)
                                        .show();
                                String token = FirebaseInstanceId.getInstance().getToken();

                                tokenReference = FirebaseDatabase.getInstance().getReference().child("NotificationTokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                tokenReference.setValue(token);


//                                final GenerateRsaKeyPair newGenerateRsaKeyPair = new GenerateRsaKeyPair();
//
//
//                                publicKeyReference = FirebaseDatabase.getInstance().getReference().child("Public Keys").child(mAuth.getCurrentUser().getUid()).child("key");
//                                publicKeyReference.addValueEventListener(new ValueEventListener() {
//
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        if(!dataSnapshot.exists())
//                                        {
//                                            String publicKey = newGenerateRsaKeyPair.getPublicKey();
//                                            publicKeyReference.setValue(publicKey);
//
//                                            String privateKey = newGenerateRsaKeyPair.getPrivateKey();
//                                            DatabaseReference privateKeyReference = FirebaseDatabase.getInstance().getReference().child("Private Keys").child(mAuth.getCurrentUser().getUid()).child("key");
//                                            privateKeyReference.setValue(privateKey);
//                                        }
//
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });



                                moveToMainActivity();
                            }

                                else{
                                    // If sign in fails, display a message to the user.
                                    String message = task.getException().toString();
                                    Snackbar.make(signIn, "Failed : " + message, Snackbar.LENGTH_SHORT)
                                            .show();
                                }

                            }

                            // ...
                        }
                     });


    }

    private void moveToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}