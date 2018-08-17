package com.swago.rockpaperscissors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            Log.d("TEST", "User Already Logged in");
            onAuthSuccess(mAuth.getCurrentUser());
        } else {
            Log.d("TEST", "Initiate Google Login");
            startSignIn();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Log.d("TEST", "Returned from Firebase Auth UI");
            if (resultCode == RESULT_OK) {
                Log.d("TEST", "Firebase Auth Result OK");
                // Sign in succeeded
                if (mAuth.getCurrentUser() != null) {
                    onAuthSuccess(mAuth.getCurrentUser());
                }
            } else {
                Log.d("TEST", "Firebase Auth Result Error");

                IdpResponse response = IdpResponse.fromResultIntent(data);
                Log.d("TEST", "Firebase Auth Result Error" + response.getError().getErrorCode());
                Log.d("TEST", "Firebase Auth Result Error" + response.getError().getMessage());

                // Sign in failed
                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startSignIn() {
        // Build FirebaseUI sign in intent. For documentation on this operation and all
        // possible customization see: https://github.com/firebase/firebaseui-android
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                .setLogo(R.mipmap.ic_launcher)
                .build();

        Log.d("TEST", "Starting Firebase Auth UI");
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void onAuthSuccess(FirebaseUser user) {
        if (user.getEmail() != null) {
            String username = usernameFromEmail(user.getEmail());
            Log.d("TEST", "Username: " + username);
        }

        // Write new user
//        writeNewUser(user.getUid(), username, user.getEmail());
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}
