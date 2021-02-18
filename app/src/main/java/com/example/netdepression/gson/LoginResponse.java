package com.example.netdepression.gson;

public class LoginResponse {
    public int code;
    public int exist;
    public String message;
    public Profile profile;

    public class Profile{
        public String userId;
        public String nickname;
        public String avatarUrl;
        public String backgroundUrl;
    }
}
