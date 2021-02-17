package com.example.netdepression.interfaces;

public interface IDiscoverPresenter {

    void getDiscoverData();

    void registerViewCallback(IDiscoverViewCallback callback);

    void unRegisterViewCallback(IDiscoverViewCallback callback);
}
