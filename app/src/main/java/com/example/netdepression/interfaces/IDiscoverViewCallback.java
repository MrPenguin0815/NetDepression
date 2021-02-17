package com.example.netdepression.interfaces;

import com.example.netdepression.gson.BannerBlock;
import com.example.netdepression.gson.PlaylistBlock;

public interface IDiscoverViewCallback {
    void onBannerBlockLoaded(BannerBlock bannerBlock);


    void onPlaylistBlockLoaded(PlaylistBlock playlistBlock);

    void onOfficialPlaylistBlockLoaded(PlaylistBlock playlistBlock);

    void onVideoBlockLoaded(PlaylistBlock playlistBlock);
}
