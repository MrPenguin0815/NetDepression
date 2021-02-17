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


    //block[0]:HOMEPAGE_BANNER
    //block[1]:HOMEPAGE_BLOCK_PLAYLIST_RCMD
    //block[2]:HOMEPAGE_MUSIC_MLOG
    //block[3]:HOMEPAGE_BLOCK_STYLE_RCMD
    //block[4]:HOMEPAGE_MUSIC_CALENDAR
    //block[5]:HOMEPAGE_BLOCK_OFFICIAL_PLAYLIST
    //block[6]:HOMEPAGE_BLOCK_NEW_ALBUM_NEW_SONG
    //block[7]:HOMEPAGE_YUNBEI_NEW_SONG
    //block[8]:HOMEPAGE_PODCAST24
    //block[9]:HOMEPAGE_VOICELIST_RCMD
    //block[10]:HOMEPAGE_BLOCK_VIDEO_PLAYLIST
    @Override
    public void getDiscoverData() {
        HttpUtil.sendHttpRequest("http://sandyz.ink:3000/homepage/block/page", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
               List<String> blockStrings = ParseUtil.handleDiscoverResponse(response);
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
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                //Log.e("DiscoverFragment","error message:" + e.getMessage());
            }
        });

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
