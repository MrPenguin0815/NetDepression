package com.example.netdepression.interfaces;

public interface IClassifyPresenter {

    void getTopicData();

    void getT1Pics(String url);
    void getT2Pics(String url);

    void getChiMusic();
    void getRockMusic();

    void registerViewCallback(IClassifyViewCallback callback);

    void unRegisterViewCallback(IClassifyViewCallback callback);

}
