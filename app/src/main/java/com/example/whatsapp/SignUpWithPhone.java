package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUpWithPhone extends AppCompatActivity {

    TextInputEditText phoneNumber;
    Button sendVerificationCodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_with_phone);

        phoneNumber = findViewById(R.id.phoneNumber);
        final String phoneNumberText = phoneNumber.getText().toString();
        sendVerificationCodeButton = findViewById(R.id.sendVerificationCodeButton);
        sendVerificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumberText,        // Phone number to verify
                        30L,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        SignUpWithPhone.this,               // Activity (for callback binding)
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {

//                                signInWithPhoneAuthCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {

                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    // Invalid request
                                    // ...
                                } else if (e instanceof FirebaseTooManyRequestsException) {
                                    // The SMS quota for the project has been exceeded
                                    // ...
                                }

                                // Show a message and update the UI
                                // ...
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {


                                // Save verification ID and resending token so we can use them later
//                                mVerificationId = verificationId;
//                                mResendToken = token;

                                // ...
                            }
                        });        // OnVerificationStateChangedCallbacks
            }
        });



    }
}
