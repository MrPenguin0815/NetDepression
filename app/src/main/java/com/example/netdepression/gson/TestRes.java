package com.example.netdepression.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestRes {
    @SerializedName("hot")
    public List<Topic> topics;
}
