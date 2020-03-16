package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class RegistrationActivity extends AppCompatActivity {


    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText email;
    private TextInputEditText password;
    private Button signUp;
    private TextView toSignIn;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private DatabaseReference ref;
    private DatabaseReference tokenReference;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        initDatabase();
        initViews();
    }

    public void initDatabase()
    {
        mAuth = FirebaseAuth.getInstance();
    }



    public void initViews() {
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);



        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);

        signUp = (Button) findViewById(R.id.signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpNewUser();
            }


        });


        toSignIn = (TextView) findViewById(R.id.tosignin);
        toSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSignIn();
            }
        });
    }

    public void moveToSignIn() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);

        Snackbar.make(signUp, "To Sign In Clicked", Snackbar.LENGTH_SHORT)
                .show();
    }

    public void signUpNewUser() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating New Account");
        progressDialog.setMessage("Please Wait While We Are Creating Your New Account");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();


        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
//                            ref = FirebaseDatabase.getInstance().getReference();
//                            ref.child("Users").child(mAuth.getCurrentUser().getUid()).setValue("");
                            Snackbar.make(signUp, "Done Registering", Snackbar.LENGTH_SHORT)
                                    .show();

                            tokenReference = FirebaseDatabase.getInstance().getReference().child("NotificationTokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            String token = FirebaseInstanceId.getInstance().getToken();
                            tokenReference.setValue(token);

                            addDetails();

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            String message = task.getException().getMessage();
                            Snackbar.make(signUp, "Error while Registering" + message, Snackbar.LENGTH_SHORT)
                                    .show();
                        }

                        // ...
                    }
                });





        }
    private void addDetails()
    {
        Intent intent = new Intent(RegistrationActivity.this, SettingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("UserType", "NewUser");
        startActivity(intent);
        finish();

    }
}