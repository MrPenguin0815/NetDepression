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

public class PasswordLoginActivity extends AppCompatActivity {


    private String phoneNum;
    private String password;
    private boolean isRegistered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_login);
        MyViewUtil.setSysBarTransparent(this);
        EditText phone = findViewById(R.id.phone);
        EditText ps = findViewById(R.id.ps);
        Button loginBtn = findViewById(R.id.login);



        ps.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginBtn.setBackground(getResources().getDrawable(R.drawable.login_frame2));
            }
        });





        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone.getText() == null || ps.getText() == null ){
                    Toast.makeText(PasswordLoginActivity.this,"输入信息不完整",Toast.LENGTH_LONG).show();

                }else{
                    phoneNum = phone.getText().toString();
                    password = ps.getText().toString();
                    //检查是否注册
                    isRegistered = checkIsRegistered();
                    if (isRegistered){
                        checkIsPsTrue();
                    }
                }
            }
        });
    }


    /**
     *检查手机号是否注册
     */
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
                                    Toast.makeText(PasswordLoginActivity.this,"手机号不存在或未注册",Toast.LENGTH_LONG).show();
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


    /**
     * 检查密码是否正确
     */
    private void checkIsPsTrue() {
        HttpUtil.sendHttpRequest("http://sandyz.ink:3000/login/cellphone?phone=" + phoneNum + "&password=" + password,
                new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        LoginResponse loginResponse = new Gson().fromJson(response,LoginResponse.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(loginResponse.code == 502){
                                    Toast.makeText(PasswordLoginActivity.this,loginResponse.message,Toast.LENGTH_LONG).show();
                                }else if(loginResponse.code == 400){
                                    Toast.makeText(PasswordLoginActivity.this,"手机号格式错误",Toast.LENGTH_LONG).show();
                                } else if (loginResponse.code == 200){
                                    Toast.makeText(PasswordLoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();

                                    //修改登录状态，记录uid
                                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PasswordLoginActivity.this).edit();
                                    editor.putInt("LOGIN_STATE",1);
                                    editor.putString("userId",loginResponse.profile.userId);
                                    editor.putString("avatarUrl",loginResponse.profile.avatarUrl);
                                    editor.putString("backgroundUrl",loginResponse.profile.backgroundUrl);
                                    editor.putString("nickname",loginResponse.profile.nickname);
                                    editor.apply();

                                    //进入主页面
                                    Intent intent = new Intent(PasswordLoginActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    finish();
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