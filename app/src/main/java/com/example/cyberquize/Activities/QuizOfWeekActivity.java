package com.example.cyberquize.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

public class QuizOfWeekActivity extends AppCompatActivity {

    Button startQuizBtn;
    ShapeableImageView QuizOfWeekImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_of_week);

        startQuizBtn = findViewById(R.id.startQuiz_btn);
        QuizOfWeekImg = findViewById(R.id.quizOfWeekQuizImage);

        Intent intent = getIntent();
        String imgUrl = intent.getStringExtra("quickImg");

        Picasso.get().load(imgUrl).into(QuizOfWeekImg);


        startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkQuizScoreAndProceed();

            }
        });
    }
    private void checkQuizScoreAndProceed() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("quizOfTheWeekUserAnswer").child("QuizeOfWeek01").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        showQuizScoreDialog();
                    } else {
                        // User has not taken the quiz yet, start the quiz activity
                        startActivity(new Intent(QuizOfWeekActivity.this, StartQuizActivity.class));
                    }
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