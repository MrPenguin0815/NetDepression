package com.example.netdepression.gson;

import java.util.List;

public class Topic {

    public String title;
    public List<String> text;
    public String participateCount;
    public String actId;
   public List<String> picUrls;

    public class Event{
        public List<Pic> pics;
    }


    public class Pic{
        public String squareUrl;
    }
}
