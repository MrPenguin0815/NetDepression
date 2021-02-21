package com.example.netdepression.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.netdepression.R;
import com.example.netdepression.gson.PlaylistItem;
import com.example.netdepression.interfaces.IMyInfoViewCallback;
import com.example.netdepression.presenters.MyInfoPresenter;
import com.example.netdepression.utils.MyViewUtil;
import com.example.netdepression.utils.ParseUtil;
import com.example.netdepression.views.WaveImageView;

import java.util.List;
import java.util.Objects;

public class  MyInfoFragment extends BaseFragment implements IMyInfoViewCallback {

    private String backgroundUrl;
    private String avatarUrl;
    private String userId;
    private String nickname;
    private LinearLayout createdList;
    private MyInfoPresenter myInfoPresenter;


    @Override
    protected View onSubViewLoaded(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        View view = inflater.inflate(R.layout.my_fragment, null);
        createdList = view.findViewById(R.id.created_list);
        backgroundUrl = prefs.getString("backgroundUrl", null);
        avatarUrl = prefs.getString("avatarUrl", null);
        userId = prefs.getString("userId", null);
        nickname = prefs.getString("nickname", null);


        myInfoPresenter = MyInfoPresenter.getInstance();
        myInfoPresenter.registerViewCallback(this);

        String playlistContent = prefs.getString("playlist", null);
        if (playlistContent != null) {
            List<PlaylistItem> playlistItems = ParseUtil.handleMyInfoResponse(playlistContent);
            assert playlistItems != null;
            onPlaylistLoaded(playlistItems);
        } else {
            myInfoPresenter.getMyData(userId);
        }

        initView(view);
        return view;
    }


    private void initView(View view) {

        //加载折叠前的图片
        ImageView backgroundImgView = view.findViewById(R.id.background_img);
        Glide.with(getActivity()).load(backgroundUrl).into(backgroundImgView);

        //加载头像
        ImageView avatar = view.findViewById(R.id.avatar_img);
        Glide.with(getActivity()).load(avatarUrl).into(avatar);

        //开始动画
        WaveImageView rippleImage = view.findViewById(R.id.ri);
        rippleImage.startWaveAnimation();

        //显示用户名和id
        TextView nicknameView = view.findViewById(R.id.nickname);
        TextView uidView = view.findViewById(R.id.uid);
        String s = "回来干嘛，" + nickname;
        nicknameView.setText(s);
        uidView.setText(userId);
    }


    @Override
    public void onPlaylistLoaded(List<PlaylistItem> playlistItems) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyViewUtil.loadPlaylistItem(playlistItems,createdList,true);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myInfoPresenter.unRegisterViewCallback(this);
    }
}
