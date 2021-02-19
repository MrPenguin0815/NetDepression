package com.example.netdepression.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.netdepression.R;
import com.example.netdepression.gson.PlaylistItem;

import java.util.List;

public class MyViewUtil {


    /**
     * 设置顶部状态栏和底部导航栏透明
     */
    public static void setSysBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }


    /**
     * 设置顶部状态栏透明且不作为布局的一部分测试
     */
    public static void seStatusBarTransparent555(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }
    }


    /**
     * 设置顶部状态栏透明
     */
    public static void setStatusBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }



    /**
     * 加载单个歌单布局
     * @param playlistItems 数据源
     * @param container 要加入的布局
     */
    public static void loadPlaylistItem(List<PlaylistItem> playlistItems, ViewGroup container,boolean isVisible) {
        for (PlaylistItem playlistItem : playlistItems) {
            View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.playlist_item, container, false);
            TextView mt = view.findViewById(R.id.playlist_name);
            TextView st = view.findViewById(R.id.playlist_description);
            ImageView cover = view.findViewById(R.id.cover_img);
            ImageView choose = view.findViewById(R.id.choose_img);
           if(isVisible){
               choose.setVisibility(View.VISIBLE);
           }else {
               choose.setVisibility(View.GONE);
           }
            mt.setText(playlistItem.name);
            String des;
            if (playlistItem.description != null) {
                des = playlistItem.description + "  已播放" + playlistItem.playCount + "次，共" + playlistItem.trackCount + "首";
            } else {
                des = "已播放" + playlistItem.playCount + "次，共" + playlistItem.trackCount + "首";
            }
            st.setText(des);
            Glide.with(MyApplication.getContext()).load(playlistItem.coverImgUrl).into(cover);
            container.addView(view);
        }
    }
}