package com.example.netdepression;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.netdepression.gson.LoginResponse;
import com.example.netdepression.interfaces.HttpCallbackListener;
import com.example.netdepression.utils.HttpUtil;
import com.example.netdepression.utils.MyViewUtil;
import com.google.gson.Gson;

public class VerificationLoginActivity extends AppCompatActivity {

    private String phoneNum;
    private String message;
    private boolean isRegistered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_verification_login);
        MyViewUtil.setSysBarTransparent(this);
        EditText phone = findViewById(R.id.phone);
        EditText sm = findViewById(R.id.sm);
        TextView sendBtn = findViewById(R.id.send);
        Button loginBtn = findViewById(R.id.login);



        sm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    loginBtn.setBackgroundColor(getResources().getColor(R.color.can_click));
            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone.getText() == null){
                    Toast.makeText(VerificationLoginActivity.this,"请输入手机号",Toast.LENGTH_LONG).show();
                }else {
                    phoneNum = phone.getText().toString();
                    requestMes();
                }
            }
        });



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone.getText() == null || sm.getText() == null ){
                  Toast.makeText(VerificationLoginActivity.this,"输入信息不完整",Toast.LENGTH_LONG).show();

              }else{
                  phoneNum = phone.getText().toString();
                  message = sm.getText().toString();
                  //检查是否注册
                  isRegistered = checkIsRegistered();
                  if (isRegistered){
                      checkIsMesTrue();
                  }
              }
            }
        });
    }






    private void requestMes() {
        HttpUtil.sendHttpRequest("http://sandyz.ink:3000/captcha/sent?phone=" + phoneNum, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                LoginResponse loginResponse = new Gson().fromJson(response,LoginResponse.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //手机号码不符合规范
                            if(loginResponse.code == 400){
                                Toast.makeText(VerificationLoginActivity.this,loginResponse.message,Toast.LENGTH_LONG).show();
                            }else if(loginResponse.code == 200){
                                Toast.makeText(VerificationLoginActivity.this,"已发送 十分钟内有效",Toast.LENGTH_LONG).show();

                            }
                        }
                    });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }





    private boolean checkIsRegistered() {
        HttpUtil.sendHttpRequest("http://sandyz.ink:3000/cellphone/existence/check?phone=" + phoneNum,
                new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        LoginResponse loginResponse = new Gson().fromJson(response,LoginResponse.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(loginResponse.exist == -1){
                                    Toast.makeText(VerificationLoginActivity.this,"手机号不存在或未注册",Toast.LENGTH_LONG).show();
                                    isRegistered = false;
                                }else if (loginResponse.exist == 1){
                                   isRegistered = true;
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
        return isRegistered;
    }






    private void checkIsMesTrue() {
        HttpUtil.sendHttpRequest("http://sandyz.ink:3000/captcha/verify?phone=" + phoneNum + "&captcha=" + message,
                new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        LoginResponse loginResponse = new Gson().fromJson(response,LoginResponse.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(loginResponse.code == 503){
                                    Toast.makeText(VerificationLoginActivity.this,loginResponse.message,Toast.LENGTH_LONG).show();
                                }else if (loginResponse.code == 200){
                                    Toast.makeText(VerificationLoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
//
//                                    //修改登录状态
//                                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(VerificationLoginActivity.this).edit();
//                                    editor.putInt("LOGIN_STATE",1);
//                                    editor.apply();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }





}