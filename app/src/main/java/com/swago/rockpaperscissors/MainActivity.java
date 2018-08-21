package com.swago.rockpaperscissors;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swago.rockpaperscissors.model.User;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getName();

    private DatabaseReference mDatabase;

    private TextView mUsernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsernameTextView = findViewById(R.id.username_text);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        showProgressDialog();
        // [START single_value_read]
        final String userId = getUid();
        Log.d(TAG, "User ID: " + userId);
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(MainActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            mUsernameTextView.setText(user.username);
                        }
                        hideProgressDialog();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        hideProgressDialog();
                    }
                });
        // [END single_value_read]
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
