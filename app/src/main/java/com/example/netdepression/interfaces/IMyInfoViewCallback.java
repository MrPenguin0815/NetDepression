package com.example.netdepression.interfaces;

import com.example.netdepression.gson.PlaylistItem;

import java.util.List;

public interface IMyInfoViewCallback {

    void onPlaylistLoaded(List<PlaylistItem> playlistItems);

}
