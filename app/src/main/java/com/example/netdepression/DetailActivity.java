package com.example.netdepression;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.netdepression.adapters.TrackAdapter;
import com.example.netdepression.gson.PlaylistItem;
import com.example.netdepression.gson.Track;
import com.example.netdepression.interfaces.HttpCallbackListener;
import com.example.netdepression.utils.HttpUtil;
import com.example.netdepression.utils.MyApplication;
import com.example.netdepression.utils.MyViewUtil;
import com.example.netdepression.utils.ParseUtil;
import com.example.netdepression.views.MaskImageView;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView trackRecycler;
    private ImageView cover;
    private TextView creatorName;
    private TextView creatorSignature;
    private CardView nothingCard;
    private MaskImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        MyViewUtil.setStatusBarTransparent(this);
        trackRecycler = findViewById(R.id.track_recycler);
        cover = findViewById(R.id.song_cover);
        creatorName = findViewById(R.id.creator_name);
        creatorSignature = findViewById(R.id.creator_signature);
        nothingCard = findViewById(R.id.none_card);
        background = findViewById(R.id.song_bg);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //获取歌单id
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String detailUrl = "http://sandyz.ink:3000/playlist/detail?id=" + id;

        //发送网络请求
        sendRequest(detailUrl);


    }


    /**
     * 发送网络请求
     * @param detailUrl 地址
     */
    private void sendRequest(String detailUrl) {
        HttpUtil.sendHttpRequest(detailUrl, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                List<Track> tracks = ParseUtil.handlePlayListDetail(response);
                PlaylistItem playlistItem = ParseUtil.handlePlayListCreator(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showBasicInfo(playlistItem);
                        if (tracks != null && tracks.size() != 0) {
                            showTrackList(tracks);
                        } else {
                            nothingCard.setVisibility(View.VISIBLE);
                        }
                    }
                });


            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    private void showBasicInfo(PlaylistItem playlistItem) {
        Glide.with(this).load(playlistItem.coverImgUrl).into(cover);
        Glide.with(this).load(playlistItem.coverImgUrl)
                .apply(background.setGaussBlur())
                .into(background);
        String s1 = "creator: " + playlistItem.creator.nickname;
        creatorName.setText(s1);
        if(playlistItem.creator.signature != null){
            String s2 = "signature: " + playlistItem.creator.signature;
            creatorSignature.setText(s2);
        }
    }



    /**
     * 展示歌单详情 加载trackRecycler
     * @param tracks 数据源
     */
    private void showTrackList(List<Track> tracks) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        trackRecycler.setLayoutManager(linearLayoutManager);
        TrackAdapter adapter = new TrackAdapter(tracks);


        //点击事件中，要发送请求并开启活动
        adapter.setOnItemClickListener(new TrackAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String trackId = tracks.get(position).id;
                String trackImgUrl = tracks.get(position).al.picUrl;
                String uriRequest = "http://sandyz.ink:3000/song/url?id=" + trackId;

                HttpUtil.sendHttpRequest(uriRequest, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        String uriString = ParseUtil.getMusicUrl(response);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (uriString != null) {
                                Intent intent = new Intent(DetailActivity.this, PlayActivity.class);
                                intent.putExtra("uri", uriString);
                                intent.putExtra("trackImgUrl",trackImgUrl);
                                startActivity(intent);
                                }else {
                                    Toast.makeText(MyApplication.getContext(),"抱歉，该音乐暂时无法播放",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyApplication.getContext(),"抱歉，该音乐暂时无法播放",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
        trackRecycler.setAdapter(adapter);
    }
}