package com.example.netdepression;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.example.netdepression.utils.MyViewUtil;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_page);
        MyViewUtil.setSysBarTransparent(this);
        TextView loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LaunchActivity.this, PasswordLoginActivity.class);
                startActivity(intent);
            }
        });



         //判断是否已登录
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LaunchActivity.this);
        if(prefs.getInt("LOGIN_STATE",0) != 0){
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}