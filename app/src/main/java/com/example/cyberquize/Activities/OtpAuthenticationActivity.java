package com.example.cyberquize.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cyberquize.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpAuthenticationActivity extends AppCompatActivity {

    Spinner countryCode;
    EditText numberEdt;
    Button sendOtp;
    LinearLayout signWithGoogle;
    FirebaseAuth mAuth;
    String selectedCountryCode;
    ProgressBar otpProgressBar;
    Spinner spinnerCountryCode;
    private static final int RC_SIGN_IN = 123;
    private static final String MOBILE_NUMBER_REGEX = "^[0-9]{10}$";


    @Override
    public void onStart() {
        super.onStart();
        // Check if the user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // do your stuff
        if (currentUser != null) {
            startActivity(new Intent(OtpAuthenticationActivity.this,BasicDeatilsEntryActivity.class));
            finish();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_authentication);
        sendOtp = findViewById(R.id.sendOtpBtn);
        countryCode = findViewById(R.id.spinnerCountryCode);
        numberEdt = findViewById(R.id.edt_number);



        spinnerCountryCode = findViewById(R.id.spinnerCountryCode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.country_codes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner
        spinnerCountryCode.setAdapter(adapter);
        int defaultPosition = adapter.getPosition("+91");
        spinnerCountryCode.setSelection(defaultPosition);

        // Retrieve the selected country code
        selectedCountryCode = spinnerCountryCode.getSelectedItem().toString();


        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);


        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = numberEdt.getText().toString().trim();

                if (isValidMobileNumber(number)){
                    sendVerificationCode(number);
                } else if (number.isEmpty()) {
                    Toast.makeText(OtpAuthenticationActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(OtpAuthenticationActivity.this, "Please enter correct number", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }


    private void sendVerificationCode(String phoneNumber) {

        otpProgressBar = findViewById(R.id.otpNumerProgressbar);
        otpProgressBar.setVisibility(View.VISIBLE);
        sendOtp.setVisibility(View.INVISIBLE);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                selectedCountryCode + phoneNumber,
                60, // Timeout duration in seconds
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        // Automatically verifies the OTP when it's received
                        // You can proceed with signing in the user or any other action
                        // signInWithPhoneAuthCredential(credential);
                        otpProgressBar.setVisibility(View.INVISIBLE);
                        sendOtp.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        // Handle verification failure, e.g., invalid phone number
                        Toast.makeText(OtpAuthenticationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        otpProgressBar.setVisibility(View.INVISIBLE);
                        sendOtp.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        // OTP code has been sent to the user's phone number
                        // You can store 'verificationId' and 'token' for later use
                        // Typically, you'd store them in class fields for use when the user enters the OTP.
                        // Start the OTP verification activity here if needed.
                        Toast.makeText(OtpAuthenticationActivity.this, "OTP send successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OtpAuthenticationActivity.this, OtpVerificationActivity.class);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("token", token);
                        intent.putExtra("number",phoneNumber);
                        intent.putExtra("countryCode",selectedCountryCode);
                        startActivity(intent);
                        otpProgressBar.setVisibility(View.INVISIBLE);
                        sendOtp.setVisibility(View.VISIBLE);

                    }
                }
        );
    }


    // continue with google


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(OtpAuthenticationActivity.this, BasicDeatilsEntryActivity.class));
                            finish();
                            Toast.makeText(OtpAuthenticationActivity.this, "Sign-In successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(OtpAuthenticationActivity.this, "Sign-In failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public static boolean isValidMobileNumber(String mobileNumber) {
        Pattern pattern = Pattern.compile(MOBILE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(mobileNumber);
        return matcher.matches();
    }
}