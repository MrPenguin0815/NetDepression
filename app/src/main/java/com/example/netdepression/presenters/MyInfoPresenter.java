package com.example.netdepression.presenters;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.netdepression.gson.PlaylistItem;
import com.example.netdepression.interfaces.HttpCallbackListener;
import com.example.netdepression.interfaces.IMyInfoPresenter;
import com.example.netdepression.interfaces.IMyInfoViewCallback;
import com.example.netdepression.utils.HttpUtil;
import com.example.netdepression.utils.MyApplication;
import com.example.netdepression.utils.ParseUtil;

import java.util.List;


public class MyInfoPresenter implements IMyInfoPresenter {


    private IMyInfoViewCallback mCallback;

    private MyInfoPresenter() {
    }

    private static MyInfoPresenter sInstance = null;

    /**
     * 获取单例对象
     */
    public static MyInfoPresenter getInstance() {
        if (sInstance == null) {
            synchronized (MyInfoPresenter.class) {
                if (sInstance == null) {
                    sInstance = new MyInfoPresenter();
                }
            }
        }
        return sInstance;
    }


    @Override
    public void getMyData(String uid) {
        HttpUtil.sendHttpRequest("http://sandyz.ink:3000/user/playlist?uid=" + uid, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                List<PlaylistItem> playlistItems = ParseUtil.handleMyInfoResponse(response);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
                editor.putString("playlist",response);
                editor.apply();
                mCallback.onPlaylistLoaded(playlistItems);
            }
            @Override
            public void onError(Exception e) {
                //Log.e("DiscoverFragment","error message:" + e.getMessage());
            }
        });

    }


    @Override
    public void registerViewCallback(IMyInfoViewCallback callback) {
        if (callback != null) {
            mCallback = callback;
        }
    }

    @Override
    public void unRegisterViewCallback(IMyInfoViewCallback callback) {
        if (callback != null) {
            mCallback = null;
        }
    }
}
