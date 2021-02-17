package com.example.netdepression.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MLogBlock {
    @SerializedName("extInfo")
    public List<Info> info;


    public class Info{
        public Resource resource;
    }


    public class Resource{
        public MLogBaseData mLogBaseData;
    }



    public class MLogBaseData{
        public String text;
        public String coverUrl;
    }
}
