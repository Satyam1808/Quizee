package com.example.cyberquize.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyberquize.Models.QuizQuestionModel;
import com.example.cyberquize.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StartQuizActivity extends AppCompatActivity {

    TextView countDownTxt,questionNumberTxt,questionTxt,optionFirstTxt,optionSecondTxt,optionThirdTxt
            ,optionFourthTxt;
    ImageView previousImg,nextImg;
    Button nextBtn,submitBtn;
    RelativeLayout optionFirstLy,optionSecondLy,optionThreeLy,optionFourthLy;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 600000; // 10 minutes in milliseconds
    private boolean timerRunning;
    private ArrayList<QuizQuestionModel> questions;
    private int currentQuestionIndex = 0;
    private Map<Integer, Integer> selectedOptionsMap = new HashMap<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        countDownTxt = findViewById(R.id.countdownTextView);
        questionTxt = findViewById(R.id.questionTxt);
        questionNumberTxt = findViewById(R.id.quetionCount);
        optionFirstTxt = findViewById(R.id.optionFirstTxt);
        optionSecondTxt = findViewById(R.id.optionSecTxt);
        optionThirdTxt = findViewById(R.id.optionThirdTxt);
        optionFourthTxt = findViewById(R.id.optionFourthTxt);
        previousImg = findViewById(R.id.quiz_previous_btn);
        nextImg = findViewById(R.id.quiz_next_btn);
        nextBtn = findViewById(R.id.next_quiz_button);
        submitBtn = findViewById(R.id.submit_quiz_button);
        optionFirstLy = findViewById(R.id.optionFirstLy);
        optionSecondLy = findViewById(R.id.optionSecondLy);
        optionThreeLy = findViewById(R.id.optionThreeLy);
        optionFourthLy = findViewById(R.id.optionFourthLy);


        mAuth = FirebaseAuth.getInstance();
        startTimer();

        fetchQuestionsFromFirebase();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextButtonClick(view);
            }
        });
        previousImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPreviousButtonClick(view);
            }
        });
        nextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextButtonClick(view);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClick(view);
            }
        });

    }

    private void fetchQuestionsFromFirebase() {
        DatabaseReference quizRef = FirebaseDatabase.getInstance().getReference("QuizOfWeek").child("QuizeOfWeek01").child("questions");
        quizRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questions = new ArrayList<>();
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    QuizQuestionModel question = questionSnapshot.getValue(QuizQuestionModel.class);
                    questions.add(question);
                }
                // Display initial question and options
                displayQuestionAndOptions();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                // Handle when the timer finishes
                timerRunning = false;
                // Update UI or perform any action
                // For example, show a message that time's up
                Toast.makeText(StartQuizActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
            }
        }.start();

        timerRunning = true;
    }

    private void updateCountDownText() {
        // Calculate minutes and seconds from milliseconds
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        // Update the TextView displaying the countdown timer
        TextView countdownTextView = findViewById(R.id.countdownTextView);
        countdownTextView.setText(timeLeftFormatted);
    }

    // Override onPause to pause the timer when the activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        if (timerRunning) {
            countDownTimer.cancel();
        }
    }


    private void displayQuestionAndOptions() {
        if (questions != null && currentQuestionIndex < questions.size()) {
            QuizQuestionModel currentQuestion = questions.get(currentQuestionIndex);
            questionTxt.setText(currentQuestion.getQuestion());
            optionFirstTxt.setText(currentQuestion.getOption01());
            optionSecondTxt.setText(currentQuestion.getOption02());
            optionThirdTxt.setText(currentQuestion.getOption03());
            optionFourthTxt.setText(currentQuestion.getOption04());

            // Update the question number text
            int questionNumber = currentQuestionIndex + 1;
            questionNumberTxt.setText("Question " + questionNumber);


            if (currentQuestionIndex == 0) {
                previousImg.setVisibility(View.GONE);
            } else {
                previousImg.setVisibility(View.VISIBLE);
            }

            if (currentQuestionIndex == questions.size() - 1) {
                // If it's the last question, hide the next button and show the submit button
                nextBtn.setVisibility(View.GONE);
                submitBtn.setVisibility(View.VISIBLE);
            } else {
                // If it's not the last question, show the next button and hide the submit button
                nextBtn.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.GONE);
            }

            resetOptionStyles();

            // Set click listeners for options
            optionFirstLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionFirstLy, optionFirstTxt);
                }
            });
            optionSecondLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionSecondLy, optionSecondTxt);
                }
            });
            optionThreeLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionThreeLy, optionThirdTxt);
                }
            });
            optionFourthLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionFourthLy, optionFourthTxt);
                }
            });

            Integer selectedOptionId = selectedOptionsMap.get(currentQuestionIndex);
            if (selectedOptionId != null) {
                applySelectedOptionStyle(selectedOptionId);
            }

        }
    }

    public void onNextButtonClick(View view) {
        currentQuestionIndex++;
        displayQuestionAndOptions();
    }

    public void onPreviousButtonClick(View view) {
        currentQuestionIndex--;
        displayQuestionAndOptions();

    }

    public void onSaveButtonClick(View view) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Submit Quiz");
        builder.setMessage("Are you sure you want to submit?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If the user clicks "OK", upload selected options to Firebase
                uploadSelectedOptionsToFirebase();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If the user clicks "Cancel", dismiss the dialog
                dialog.dismiss();
            }
        });
        builder.show();

    }
    private void resetOptionStyles() {
        optionFirstLy.setBackgroundResource(R.drawable.quiz_option_unselected_bg);
        optionSecondLy.setBackgroundResource(R.drawable.quiz_option_unselected_bg);
        optionThreeLy.setBackgroundResource(R.drawable.quiz_option_unselected_bg);
        optionFourthLy.setBackgroundResource(R.drawable.quiz_option_unselected_bg);
        optionFirstTxt.setTextColor(ContextCompat.getColor(this, R.color.grey));
        optionSecondTxt.setTextColor(ContextCompat.getColor(this, R.color.grey));
        optionThirdTxt.setTextColor(ContextCompat.getColor(this, R.color.grey));
        optionFourthTxt.setTextColor(ContextCompat.getColor(this, R.color.grey));

        optionFirstLy.setTag(false);
        optionSecondLy.setTag(false);
        optionThreeLy.setTag(false);
        optionFourthLy.setTag(false);
    }

    private void selectOption(RelativeLayout optionLayout, TextView optionTextView) {
        if (optionLayout.getTag() != null && (boolean) optionLayout.getTag()) {
            // If already selected, unselect it
            resetOptionStyles();
            return;
        }
        resetOptionStyles(); // Reset all options first
        optionLayout.setBackgroundResource(R.drawable.quize_option_selected_bg);
        optionTextView.setTextColor(ContextCompat.getColor(this, R.color.white));
        // Set the tag to true to mark the option as selected
        selectedOptionsMap.put(currentQuestionIndex, optionLayout.getId());
        optionLayout.setTag(true);
    }

    private void applySelectedOptionStyle(int selectedOptionId) {
        if (selectedOptionId == R.id.optionFirstLy) {
            selectOption(optionFirstLy, optionFirstTxt);
        } else if (selectedOptionId==R.id.optionSecondLy) {
            selectOption(optionSecondLy, optionSecondTxt);
        } else if (selectedOptionId==R.id.optionThreeLy) {
            selectOption(optionThreeLy, optionThirdTxt);
        }else {
            selectOption(optionFourthLy, optionFourthTxt);
        }
    }

    private void uploadSelectedOptionsToFirebase() {


        // Create a Firebase reference to the current user's data
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("quizOfTheWeekUserAnswer").child("QuizeOfWeek01").child(userId);
        int score = 0;

        // Iterate through the selected options map
        for (Map.Entry<Integer, Integer> entry : selectedOptionsMap.entrySet()) {
            int questionIndex = entry.getKey();
            int selectedOptionId = entry.getValue();

            // Find the corresponding question and selected option text
            QuizQuestionModel question = questions.get(questionIndex);
            String selectedOptionText = getSelectedOptionText(selectedOptionId, question);
            String correctOptionText = question.getCorrectOption();

            // Upload the question and selected option text to Firebase
            userRef.child("questions").child("question" + questionIndex).setValue(question.getQuestion());
            userRef.child("questions").child("question" + questionIndex).child("selectedOption").setValue(selectedOptionText);

            // Check if the selected option matches the correct answer
            if (selectedOptionText.equals(correctOptionText)) {
                score++; // Increment score if the answer is correct
            }


        }
        uploadScoreToFirebase(score);
        Intent intent = new Intent(StartQuizActivity.this, QuizeScoreActivity.class);
        intent.putExtra("reqCode",1);
        intent.putExtra("scoreOfQuizOfWeek", score);
        startActivity(intent);

        // Inform the user that the quiz has been submitted
        Toast.makeText(this, "Quiz submitted successfully!", Toast.LENGTH_SHORT).show();


    }

    private void uploadScoreToFirebase(Integer scoreOfUser){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userScoreRef = FirebaseDatabase.getInstance().getReference("quizOfTheWeekUserScore").child("QuizeOfWeek01").child(userId);

            // Upload the score to Firebase
            userScoreRef.child("score").setValue(scoreOfUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    } else {
                    }
                }
            });
        }
    }

    private String getSelectedOptionText(int selectedOptionId, QuizQuestionModel question) {
        // Determine the selected option text based on the selectedOptionId
        if (selectedOptionId == R.id.optionFirstLy) {
            return question.getOption01();
        } else if (selectedOptionId == R.id.optionSecondLy) {
            return question.getOption02();
        } else if (selectedOptionId == R.id.optionThreeLy) {
            return question.getOption03();
        } else {
            return question.getOption04();
        }

    }

    @Override
    public void onBackPressed() {

            // If options are selected, prompt the user to confirm cancellation
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle("Cancel Quiz");
            builder.setMessage("Are you sure you want to cancel the quiz?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   StartQuizActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // If the user cancels, dismiss the dialog
                    dialog.dismiss();
                }
            });
            builder.show();

    }


}