package com.example.netdepression.presenters;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.netdepression.gson.PlaylistItem;
import com.example.netdepression.gson.Topic;
import com.example.netdepression.interfaces.HttpCallbackListener;
import com.example.netdepression.interfaces.IClassifyPresenter;
import com.example.netdepression.interfaces.IClassifyViewCallback;
import com.example.netdepression.utils.HttpUtil;
import com.example.netdepression.utils.MyApplication;
import com.example.netdepression.utils.ParseUtil;

import java.util.List;


public class ClassifyPresenter implements IClassifyPresenter {

    private IClassifyViewCallback mCallback;

    private ClassifyPresenter() {
    }

    private static ClassifyPresenter sInstance = null;

    /**
     * 获取单例对象
     */
    public static ClassifyPresenter getInstance() {
        if (sInstance == null) {
            synchronized (ClassifyPresenter.class) {
                if (sInstance == null) {
                    sInstance = new ClassifyPresenter();
                }
            }
        }
        return sInstance;
    }


    @Override
    public void getTopicData() {
//        HttpUtil.sendHttpRequest("http://sandyz.ink:3000/hot/topic?limit=2", new HttpCallbackListener() {
//            @Override
//            public void onFinish(String response) {
//                //回调
//                Log.e("handleTopicResponse: ", "response:" + response);
//                List<Topic> topics = ParseUtil.handleTopicResponse(response);
////                mCallback.onTopicDataLoaded(topics);
//
//                //缓存
//                if(topics != null){
//                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
//                    editor.putString("topic",response);
//                    editor.apply();
//                }
        String topicDetailUrl1 = "http://sandyz.ink:3000/topic/detail/event/hot?actid=111701206";
        String topicDetailUrl2 = "http://sandyz.ink:3000/topic/detail/event/hot?actid=111695362";
        getT1Pics(topicDetailUrl1);
        getT2Pics(topicDetailUrl2);
//            }
//
//           @Override
//           public void onError(Exception e) {
//
//            }
//        });
    }


    /**
     * 获取图片
     */
    @Override
    public void getT1Pics(String url) {
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                List<String> picUrls1 = ParseUtil.handleTopicDetailResponse(response);
                //缓存
                if (picUrls1 != null) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
                    editor.putString("topic1Pic", response);
                    editor.apply();
                }
                //回调
                mCallback.onTopic1PicLoaded(picUrls1);
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }


    @Override
    public void getT2Pics(String url) {
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                List<String> picUrls2 = ParseUtil.handleTopicDetailResponse(response);
                //缓存
                if (picUrls2 != null) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
                    editor.putString("topic2Pic", response);
                    editor.apply();
                }
                //回调
                mCallback.onTopic2PicLoaded(picUrls2);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }


    @Override
    public void getChiMusic() {
        HttpUtil.sendHttpRequest("http://sandyz.ink:3000/top/playlist?limit=5&cat=华语", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //缓存
                if (response != null) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
                    editor.putString("ChiMus", response);
                    editor.apply();
                }
                //回调
                List<PlaylistItem> playlistItems = ParseUtil.handleClassifyListResponse(response);
                mCallback.onChiMusLoaded(playlistItems);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }


    @Override
    public void getRockMusic() {
        HttpUtil.sendHttpRequest("http://sandyz.ink:3000/top/playlist?limit=5&cat=摇滚", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                List<PlaylistItem> playlistItems = ParseUtil.handleClassifyListResponse(response);
                //缓存
                if (playlistItems != null) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
                    editor.putString("RockMus", response);
                    editor.apply();
                }
                //回调
                mCallback.onRockMusLoaded(playlistItems);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }


    @Override
    public void registerViewCallback(IClassifyViewCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unRegisterViewCallback(IClassifyViewCallback callback) {

    }
}
