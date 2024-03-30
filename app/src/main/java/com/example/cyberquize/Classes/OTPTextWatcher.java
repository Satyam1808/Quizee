package com.example.cyberquize.Classes;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.cyberquize.R;


public class OTPTextWatcher implements TextWatcher {
    private EditText currentEditText;
    private EditText nextEditText;

    public OTPTextWatcher(EditText currentEditText, EditText nextEditText) {
        this.currentEditText = currentEditText;
        this.nextEditText = nextEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 1) {
            // When a digit is entered, move the focus to the next EditText field
            currentEditText.clearFocus();
            currentEditText.setBackgroundResource(R.drawable.otp_after_fill_dig);
            currentEditText.setTextColor(Color.WHITE);
            nextEditText.requestFocus();
            nextEditText.setBackgroundResource(R.drawable.otp_after_fill_dig);
            nextEditText.setTextColor(Color.WHITE);
        }
    }

}

