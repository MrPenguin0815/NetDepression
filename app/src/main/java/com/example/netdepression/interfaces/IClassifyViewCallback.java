package com.example.netdepression.interfaces;

import com.example.netdepression.gson.PlaylistItem;
import com.example.netdepression.gson.Topic;

import java.util.List;

public interface IClassifyViewCallback {

    void onTopic1PicLoaded(List<String> picUrls);
    void onTopic2PicLoaded(List<String> picUrls);
    void onTopicDataLoaded(List<Topic> topics);
    void onChiMusLoaded(List<PlaylistItem> playlistItems);
    void onRockMusLoaded(List<PlaylistItem> playlistItems);
}
