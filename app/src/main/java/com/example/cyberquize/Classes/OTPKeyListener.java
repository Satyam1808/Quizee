package com.example.cyberquize.Classes;


import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.example.cyberquize.R;


public class OTPKeyListener implements View.OnKeyListener {
    private EditText currentEditText;
    private EditText previousEditText;

    public OTPKeyListener(EditText currentEditText, EditText previousEditText) {
        this.currentEditText = currentEditText;
        this.previousEditText = previousEditText;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (currentEditText.getText().toString().isEmpty() && previousEditText != null) {
                previousEditText.requestFocus();
                currentEditText.setBackgroundResource(R.drawable.otp_num_editbox);

                return true;
            }


        }
        return false;
    }
}