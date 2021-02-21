package com.example.netdepression.gson;

import java.util.List;

public class PlaylistItem {
    public String name;
    public String description;
    public String playCount;
    public String trackCount;
    public String coverImgUrl;
    public String id;
    public Creator creator;
    public List<Track> tracks;

    public class Creator{
        public String nickname;
        public String signature;
        public String userId;
    }
}
