package com.example.netdepression.interfaces;

public interface IMyInfoPresenter {
    void getMyData(String uid);

    void registerViewCallback(IMyInfoViewCallback callback);

    void unRegisterViewCallback(IMyInfoViewCallback callback);
}
