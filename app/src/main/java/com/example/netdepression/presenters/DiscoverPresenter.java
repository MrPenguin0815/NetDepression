package com.example.netdepression.presenters;

import android.util.Log;

import com.example.netdepression.gson.Banner;
import com.example.netdepression.gson.BannerBlock;
import com.example.netdepression.gson.Block;
import com.example.netdepression.gson.PlaylistBlock;
import com.example.netdepression.interfaces.HttpCallbackListener;
import com.example.netdepression.interfaces.IDiscoverPresenter;
import com.example.netdepression.interfaces.IDiscoverViewCallback;
import com.example.netdepression.utils.HttpUtil;
import com.example.netdepression.utils.ParseUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class DiscoverPresenter implements IDiscoverPresenter {


    private List<IDiscoverViewCallback> mCallbacks = new ArrayList<>();

    private DiscoverPresenter(){};

    private static DiscoverPresenter sInstance = null;

    /**
     * 获取单例对象
     */
    public static DiscoverPresenter getInstance(){
        if(sInstance == null){
            synchronized (DiscoverPresenter.class){
                if(sInstance == null){
                    sInstance = new DiscoverPresenter();
                }
            }
        }
        return  sInstance;
    }




    @Override
    public void getDiscoverData() {
        HttpUtil.sendHttpRequest("http://sandyz.ink:3000/homepage/block/page", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
               List<String> blockStrings = ParseUtil.handleDiscoverResponse(response);
               if(blockStrings != null ){
                   for (IDiscoverViewCallback callback : mCallbacks) {
                       callback.onCache(blockStrings);
                   }
               }
            }

            @Override
            public void onError(Exception e) {
                //Log.e("DiscoverFragment","error message:" + e.getMessage());
            }
        });

    }





    public void getDiscoverInfo(List<String> blockStrings){
        for (String blockContent:blockStrings) {
            Block block = new Gson().fromJson(blockContent,Block.class);
            if("HOMEPAGE_BANNER".equals(block.blockCode)){
                BannerBlock bannerBlock = new Gson().fromJson(blockContent,BannerBlock.class);
                for (IDiscoverViewCallback callback : mCallbacks) {
                    callback.onBannerBlockLoaded(bannerBlock);
                }
            }else if("HOMEPAGE_BLOCK_PLAYLIST_RCMD".equals(block.blockCode)){
                PlaylistBlock playlistBlock = new Gson().fromJson(blockContent,PlaylistBlock.class);
                for (IDiscoverViewCallback callback : mCallbacks) {
                    callback.onPlaylistBlockLoaded(playlistBlock);
                }
            }else if("HOMEPAGE_BLOCK_OFFICIAL_PLAYLIST".equals(block.blockCode)) {
                PlaylistBlock playlistBlock = new Gson().fromJson(blockContent, PlaylistBlock.class);
                for (IDiscoverViewCallback callback : mCallbacks) {
                    callback.onOfficialPlaylistBlockLoaded(playlistBlock);
                }
            }else if("HOMEPAGE_BLOCK_VIDEO_PLAYLIST".equals(block.blockCode)) {
                PlaylistBlock playlistBlock = new Gson().fromJson(blockContent, PlaylistBlock.class);
                for (IDiscoverViewCallback callback : mCallbacks) {
                    callback.onVideoBlockLoaded(playlistBlock);
                }
            }else if ("HOMEPAGE_BLOCK_STYLE_RCMD".equals(block.blockCode)){
                PlaylistBlock playlistBlock = new Gson().fromJson(blockContent, PlaylistBlock.class);
                for (IDiscoverViewCallback callback : mCallbacks) {
                    callback.onStyleBlockLoaded(playlistBlock);
                }
            }else if("HOMEPAGE_MUSIC_CALENDAR".equals(block.blockCode)) {
                PlaylistBlock playlistBlock = new Gson().fromJson(blockContent, PlaylistBlock.class);
                for (IDiscoverViewCallback callback : mCallbacks) {
                    callback.onCalendarLoaded(playlistBlock);
                }
            }
        }
    }



    @Override
    public void registerViewCallback(IDiscoverViewCallback callback) {
        if(! mCallbacks.contains(callback)){
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unRegisterViewCallback(IDiscoverViewCallback callback) {
        if(callback != null){
            mCallbacks.remove(callback);
        }
    }


}
