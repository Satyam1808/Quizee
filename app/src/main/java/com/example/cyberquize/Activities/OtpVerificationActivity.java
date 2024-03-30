package com.example.cyberquize.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyberquize.Classes.OTPKeyListener;
import com.example.cyberquize.Classes.OTPTextWatcher;
import com.example.cyberquize.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpVerificationActivity extends AppCompatActivity {
    ImageView back_img;
    EditText editText1,editText2,editText3,editText4,editText5,editText6;
    private FirebaseAuth mAuth;
    Button verifyBtn;
    ProgressBar progressBar;
    TextView resend_btn,otpVerifyDessc;
    String userPhoneNumber,selectedCountryCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);


        back_img = findViewById(R.id.otp_verification_back_icn);
        editText1 = findViewById(R.id.editTextDigit1);
        editText2 = findViewById(R.id.editTextDigit2);
        editText3 = findViewById(R.id.editTextDigit3);
        editText4 = findViewById(R.id.editTextDigit4);
        editText5 = findViewById(R.id.editTextDigit5);
        editText6 = findViewById(R.id.editTextDigit6);
        verifyBtn = findViewById(R.id.verifyOtpBtn);
        resend_btn = findViewById(R.id.resendOtp);
        otpVerifyDessc = findViewById(R.id.otpVerfyTxtDesc);

        mAuth  = FirebaseAuth.getInstance();

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userPhoneNumber = getIntent().getStringExtra("number");
        selectedCountryCode = getIntent().getStringExtra("countryCode");
        otpVerifyDessc.setText(getString(R.string.otpVerifyTxt)+" "+selectedCountryCode+userPhoneNumber);

        editText1.addTextChangedListener(new OTPTextWatcher(editText1, editText2));
        editText2.addTextChangedListener(new OTPTextWatcher(editText2, editText3));
        editText3.addTextChangedListener(new OTPTextWatcher(editText3, editText4));
        editText4.addTextChangedListener(new OTPTextWatcher(editText4, editText5));
        editText5.addTextChangedListener(new OTPTextWatcher(editText5, editText6));


        editText1.setOnKeyListener(new OTPKeyListener(editText1, null));
        editText2.setOnKeyListener(new OTPKeyListener(editText2, editText1));
        editText3.setOnKeyListener(new OTPKeyListener(editText3, editText2));
        editText4.setOnKeyListener(new OTPKeyListener(editText4, editText3));
        editText5.setOnKeyListener(new OTPKeyListener(editText5,editText4));
        editText6.setOnKeyListener(new OTPKeyListener(editText6,editText5));


        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = editText1.getText().toString().trim()+
                        editText2.getText().toString().trim()+
                        editText3.getText().toString().trim()+
                        editText4.getText().toString().trim()+
                        editText5.getText().toString().trim()+
                        editText6.getText().toString().trim();
                String verificationId = getIntent().getStringExtra("verificationId");

                if (otp.isEmpty()){
                    Toast.makeText(OtpVerificationActivity.this, "Please enter otp", Toast.LENGTH_SHORT).show();
                }else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });


        resend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOtp();
            }
        });


    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressBar = findViewById(R.id.otpVerifyProgressbar);
        progressBar.setVisibility(View.VISIBLE);
        verifyBtn.setVisibility(View.INVISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Phone authentication successful, proceed to the next activity or task
                        // For example, you can start the main activity
                        startActivity(new Intent(OtpVerificationActivity.this, BasicDeatilsEntryActivity.class));
                        finish();
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        // Phone authentication failed, display an error message
                        // You can also handle specific failure cases here
                    }
                });
    }

    private boolean canResendOTP() {

        return true; // Change this based on your implementation
    }

    private void resendOtp(){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                selectedCountryCode + userPhoneNumber,
                60, // Timeout duration in seconds
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        // Automatically verifies the OTP when it's received
                        // You can proceed with signing in the user or any other action
                        // signInWithPhoneAuthCredential(credential);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        // Handle verification failure, e.g., invalid phone number
                        Toast.makeText(OtpVerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        // OTP code has been sent to the user's phone number
                        // You can store 'verificationId' and 'token' for later use
                        // Typically, you'd store them in class fields for use when the user enters the OTP.
                        // Start the OTP verification activity here if needed.
                        Toast.makeText(OtpVerificationActivity.this, "OTP successfully send", Toast.LENGTH_SHORT).show();

                    }
                }
        );
    }
}