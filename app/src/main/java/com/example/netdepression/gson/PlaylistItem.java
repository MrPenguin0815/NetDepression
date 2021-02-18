package com.example.netdepression.gson;

public class PlaylistItem {
    public String name;
    public String description;
    public String playCount;
    public String trackCount;
    public String coverImgUrl;
    public Creator creator;

    public class Creator{
        public String userId;
    }
}
