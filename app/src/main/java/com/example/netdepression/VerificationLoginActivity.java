package com.example.netdepression;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class VerificationLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_verification_login);
        MyViewUtils.setSysBarTransparent(this);
    }
}