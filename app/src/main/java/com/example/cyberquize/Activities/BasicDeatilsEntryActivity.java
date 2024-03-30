package com.example.cyberquize.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cyberquize.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BasicDeatilsEntryActivity extends AppCompatActivity {

    EditText firstName, lastName;
    RelativeLayout nextBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_deatils_entry);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        nextBtn = findViewById(R.id.next_btn);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Check if the activity has already been opened
        boolean activityOpenedBefore = preferences.getBoolean("activityOpened", false);
        if (activityOpenedBefore) {
            goToHomeActivity();
        }

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserDetailsToFirebase();
            }
        });

    }

    private void saveUserDetailsToFirebase() {
        String firstNameStr = firstName.getText().toString().trim();
        String lastNameStr = lastName.getText().toString().trim();

        if (firstNameStr.isEmpty() || lastNameStr.isEmpty()) {
            Toast.makeText(this, "Please enter your first and last name", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                String phoneNumber = currentUser.getPhoneNumber();

                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    // Upload user details to Firebase Realtime Database or Firestore
                    mDatabase.child("users").child(userId).child("firstName").setValue(firstNameStr);
                    mDatabase.child("users").child(userId).child("lastName").setValue(lastNameStr);
                    mDatabase.child("users").child(userId).child("phoneNumber").setValue(phoneNumber);

                    // Mark the activity as opened
                    preferences.edit().putBoolean("activityOpened", true).apply();

                    goToHomeActivity();
                } else {
                    Toast.makeText(this, "Phone number not found!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void goToHomeActivity() {
        startActivity(new Intent(BasicDeatilsEntryActivity.this, HomeActivity.class));
        finish();
    }

}