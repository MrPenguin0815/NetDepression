package com.example.netdepression.utils;

import android.util.Log;

import com.example.netdepression.base.CardlistItem;
import com.example.netdepression.gson.Creative;
import com.example.netdepression.gson.PlaylistBlock;
import com.example.netdepression.gson.PlaylistItem;
import com.example.netdepression.gson.TestRes;
import com.example.netdepression.gson.Topic;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseUtil {


    /**
     *处理Discover Response
     */
    public static List<String> handleDiscoverResponse(String response) {
        List<String> blockStrings = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonData = jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonData.getJSONArray("blocks");
            for (int i = 0; i < jsonArray.length(); i++) {
                String blockContent = jsonArray.getJSONObject(i).toString();
                blockStrings.add(blockContent);
            }
            return blockStrings;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }




    /**
     *将playlistBlock打包成数据源，适用于creative数组里面只包含一个resource的情况
     */
    public static List<CardlistItem> handlePlaylistBlock(PlaylistBlock playlistBlock) {
        if(playlistBlock == null){
            Log.d("ParseUtil", "handlePlaylistBlock: 获取失败");
        }
        List<CardlistItem> cardlistItems = new ArrayList<>();
        for (Creative creative: playlistBlock.creatives) {
            String mainTitle = creative.resources.get(0).uiElement.mainTitle.title;
            String imageUrl = creative.resources.get(0).uiElement.image.imageUrl;
            String resourceId = creative.resources.get(0).resourceId;
           CardlistItem cardlistItem = new CardlistItem(mainTitle,imageUrl,resourceId);
           cardlistItems.add(cardlistItem);
        }
        return cardlistItems;
    }



    /**
     *处理MyInfo Response
     */
    public static List<PlaylistItem> handleMyInfoResponse(String response){
        List<PlaylistItem> items = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("playlist");
            for (int i = 0; i < jsonArray.length(); i++) {
                String itemContent = jsonArray.getJSONObject(i).toString();
                PlaylistItem item = new Gson().fromJson(itemContent,PlaylistItem.class);
                items.add(item);
            }

            return items;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }



    /**
     *处理Topic Response
     */
    public static List<Topic> handleTopicResponse(String response){
        List<Topic> topics = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("hot");
            for (int i = 0; i < jsonArray.length(); i++) {
                String itemContent = jsonArray.getJSONObject(i).toString();
                Topic item = new Gson().fromJson(itemContent,Topic.class);
                Log.e("handleTopicResponse: ", "actId" + item.actId);
                topics.add(item);
            }

            return topics;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
//        TestRes testRes = new Gson().fromJson(response,TestRes.class);
//        if(testRes != null){
//            return testRes.topics;
//        }else {
//            return null;
//        }

    }


    /**
     *处理TopicDetail Response
     */
    public static List<String> handleTopicDetailResponse(String response) {
        try {
            List<String> picUrls = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("events");
            for (int i = 0; i < jsonArray.length(); i++) {
                String eventContent = jsonArray.getJSONObject(i).toString();
                Topic.Event event = new Gson().fromJson(eventContent,Topic.Event.class);
                if(event.pics != null && event.pics.size() != 0){
                    for (Topic.Pic pic : event.pics) {
                        picUrls.add(pic.squareUrl);
                    }
                }
            }

            return picUrls;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }




    /**
     *处理分类歌单 Response
     */
    public static List<PlaylistItem> handleClassifyListResponse(String response){
        List<PlaylistItem> items = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("playlists");
            for (int i = 0; i < jsonArray.length(); i++) {
                String itemContent = jsonArray.getJSONObject(i).toString();
                PlaylistItem item = new Gson().fromJson(itemContent,PlaylistItem.class);
                items.add(item);
            }
            return items;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
