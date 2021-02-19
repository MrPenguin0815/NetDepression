package com.example.netdepression.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.netdepression.R;
import com.example.netdepression.gson.PlaylistItem;
import com.example.netdepression.gson.Topic;
import com.example.netdepression.interfaces.IClassifyPresenter;
import com.example.netdepression.interfaces.IClassifyViewCallback;
import com.example.netdepression.presenters.ClassifyPresenter;
import com.example.netdepression.utils.MyViewUtil;
import com.example.netdepression.utils.ParseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassifyFragment extends BaseFragment implements IClassifyViewCallback {

    private IClassifyPresenter mPresenter;
    private ViewGroup chiMusLayout;
    private ViewGroup rockMusLayout;
    private View topicCard1;
    private View topicCard2;

    @Override
    protected View onSubViewLoaded(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.classify_fragment,null);
        chiMusLayout = view.findViewById(R.id.chi_layout);
        rockMusLayout = view.findViewById(R.id.rock_layout);
        topicCard1 = view.findViewById(R.id.topic1);
        topicCard2 = view.findViewById(R.id.topic2);
        mPresenter = ClassifyPresenter.getInstance();
        mPresenter.registerViewCallback(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String topic2Pic = prefs.getString("topic2Pic", null);
        String topic1Pic = prefs.getString("topic1Pic", null);
        if (topic2Pic != null && topic1Pic != null){
            List<String> picUrls2 = ParseUtil.handleTopicDetailResponse(topic2Pic);
            List<String> picUrls1 = ParseUtil.handleTopicDetailResponse(topic1Pic);
            for (String s : picUrls2) {

                Log.e("picUrls2", "picUrls2:" + s);
            }
            for (String s : picUrls1) {

                Log.e("picUrls1", "picUrls1:" + s);
            }
            onTopic2PicLoaded(picUrls2);
            onTopic1PicLoaded(picUrls1);
        }else {
            mPresenter.getTopicData();
        }


        String ChiMus = prefs.getString("ChiMus", null);
        if(ChiMus != null){
            List<PlaylistItem> playlistItems = ParseUtil.handleClassifyListResponse(ChiMus);
            onChiMusLoaded(playlistItems);
        }else {
            mPresenter.getChiMusic();
        }


        String RockMus = prefs.getString("RockMus", null);
        if(ChiMus != null){
            List<PlaylistItem> playlistItems = ParseUtil.handleClassifyListResponse(RockMus);
            onRockMusLoaded(playlistItems);
        }else {
            mPresenter.getRockMusic();
        }


        return view;
    }


    /**
     * 展示热门话题的图片
     */
    @Override
    public void onTopic1PicLoaded(List<String> picUrls) {
        showPic(picUrls,topicCard1);
    }

    @Override
    public void onTopic2PicLoaded(List<String> picUrls) {
        showPic(picUrls,topicCard2);
    }

    private void showPic(List<String> picUrls, View topicCard) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView imageView1 = topicCard.findViewById(R.id.img1);
                if(picUrls != null && picUrls.size() != 0){
                    Glide.with(getContext()).load(picUrls.get(0)).into(imageView1);
                }
                ImageView imageView2 = topicCard.findViewById(R.id.img2);
                if(picUrls != null && picUrls.size() > 0){
                    Glide.with(getContext()).load(picUrls.get(1)).into(imageView2);
                }
                ImageView imageView3 = topicCard.findViewById(R.id.img3);
                if(picUrls != null && picUrls.size() > 1){
                    Glide.with(getContext()).load(picUrls.get(2)).into(imageView3);
                }
            }
        });
    }


    /**
     * 展示热门话题基本信息
     */
    @Override
    public void onTopicDataLoaded(List<Topic> topics) {
           showTopic(topics.get(0),topicCard1);
           showTopic(topics.get(1),topicCard2);
    }

    private void showTopic(Topic topic, View topicCard) {
        TextView titleView = topicCard.findViewById(R.id.topic_title);
        TextView textView = topicCard.findViewById(R.id.topic_text);
        titleView.setText(topic.title);
        if((topic.text != null) && (topic.text.size() != 0)){
            String s = topic.participateCount + "人正在参与. . .";
            textView.setText(s);
        }else {
            textView.setText(topic.text.get(0));
        }
    }





    @Override
    public void onChiMusLoaded(List<PlaylistItem> playlistItems) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyViewUtil.loadPlaylistItem(playlistItems,chiMusLayout,false);
            }
        });
    }



    @Override
    public void onRockMusLoaded(List<PlaylistItem> playlistItems) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyViewUtil.loadPlaylistItem(playlistItems,rockMusLayout,false);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unRegisterViewCallback(this);
    }
}