package com.example.netdepression.utils;

import android.util.Log;

import com.example.netdepression.base.CardlistItem;
import com.example.netdepression.gson.Creative;
import com.example.netdepression.gson.PlaylistBlock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseUtil {


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
     *将playlistBlock打包成数据源
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
}
