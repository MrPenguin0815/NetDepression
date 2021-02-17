package com.example.netdepression.interfaces;

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
