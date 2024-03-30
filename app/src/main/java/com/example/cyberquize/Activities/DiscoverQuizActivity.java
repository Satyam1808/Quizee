package com.example.cyberquize.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.PackageInfoCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cyberquize.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DiscoverQuizActivity extends AppCompatActivity {


    TextView discoverQuizName;
    ShapeableImageView discoverQuizImg;
    Button discoverStartQuizBtn;
    String quizName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_quiz);


        discoverQuizName = findViewById(R.id.discoverQuizName);
        discoverQuizImg = findViewById(R.id.discoverQuizImage);
        discoverStartQuizBtn = findViewById(R.id.discoverStartQuizBtn);

        Intent intent = getIntent();
        quizName = intent.getStringExtra("discoverQuizName");
        String quizImg = intent.getStringExtra("discoverQuizImg");

        discoverQuizName.setText(quizName);
        Picasso.get().load(quizImg).into(discoverQuizImg);


        discoverStartQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  Intent intent = new Intent(DiscoverQuizActivity.this, DiscoverQuizStartActivity.class);
                intent.putExtra("quizTitle",quizName);
                startActivity(intent);*/

                checkQuizScoreAndProceed();

            }
        });



    }


    private void checkQuizScoreAndProceed() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("discoverUserScore").child(quizName).child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        showQuizScoreDialog();
                    } else {
                        // User has not taken the quiz yet, start the quiz activity
                        Intent intent = new Intent(DiscoverQuizActivity.this, DiscoverQuizStartActivity.class);
                        intent.putExtra("quizTitle",quizName);
                        startActivity(intent);                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    private void showQuizScoreDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Quiz Already Taken")
                .setMessage("You have already taken the quiz.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }


}