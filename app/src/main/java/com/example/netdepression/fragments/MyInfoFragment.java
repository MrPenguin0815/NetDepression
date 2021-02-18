package com.example.netdepression.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.netdepression.R;
import com.example.netdepression.gson.PlaylistItem;
import com.example.netdepression.interfaces.IMyInfoViewCallback;
import com.example.netdepression.presenters.MyInfoPresenter;
import com.example.netdepression.utils.ParseUtil;
import com.example.netdepression.views.RippleImageView;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

public class  MyInfoFragment extends BaseFragment implements IMyInfoViewCallback {

    private String backgroundUrl;
    private String avatarUrl;
    private String userId;
    private String nickname;
    private LinearLayout createdList;



    @Override
    protected View onSubViewLoaded(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        View view = inflater.inflate(R.layout.my_fragment,null);
        createdList = view.findViewById(R.id.created_list);
        backgroundUrl = prefs.getString("backgroundUrl",null);
        avatarUrl = prefs.getString("avatarUrl",null);
        userId = prefs.getString("userId",null);
        nickname = prefs.getString("nickname",null);


        MyInfoPresenter myInfoPresenter = MyInfoPresenter.getInstance();
        myInfoPresenter.registerViewCallback(this);

        String playlistContent = prefs.getString("playlist",null);
        if(playlistContent != null){
            List<PlaylistItem> playlistItems = ParseUtil.handleMyInfoResponse(playlistContent);
            assert playlistItems != null;
            onPlaylistLoaded(playlistItems);
        }else {
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
        RippleImageView rippleImage = view.findViewById(R.id.ri);
        rippleImage.startWaveAnimation();

        //显示用户名和id
        TextView nicknameView = view.findViewById(R.id.nickname);
        TextView uidView = view.findViewById(R.id.uid);
        String s = "欢迎回来，"+ nickname;
        nicknameView.setText(s);
        uidView.setText(userId);
    }



    @Override
    public void onPlaylistLoaded(List<PlaylistItem> playlistItems) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (PlaylistItem playlistItem : playlistItems) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.playlist_item,createdList,false);
                    TextView mt = view.findViewById(R.id.playlist_name);
                    TextView st = view.findViewById(R.id.playlist_description);
                    ImageView cover = view.findViewById(R.id.cover_img);
                    mt.setText(playlistItem.name);
                    String des;
                    if(playlistItem.description != null) {
                        des = playlistItem.description + "  已播放" + playlistItem.playCount + "次，共" + playlistItem.trackCount + "首";
                    }else {
                        des = "已播放" + playlistItem.playCount + "次，共" + playlistItem.trackCount + "首";
                    }
                    st.setText(des);
                    Glide.with(getActivity()).load(playlistItem.coverImgUrl).into(cover);
                    createdList.addView(view);
                }
            }
        });
    }

    @Override
    public void onCache(String response) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putString("playlist",response);
        editor.apply();
    }
}
