package com.example.netdepression;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.netdepression.utils.MyViewUtil;
import com.example.netdepression.views.MaskImageView;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener{

    private Uri uri;
    private static SeekBar musicProgressBar; // 进度条
    private static TextView currentTv; // 当前音乐播放时长
    private static TextView totalTv; // 当前音乐总时长
    private ObjectAnimator animator;
    private MaskImageView trackBg;
    private ImageView trackImg;
    private String trackImgUrl;



    //让service返回的music control对象与主程序的music control对象挂钩
    private MusicPlayerService.MusicControl control;
    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //一与服务连接上，就开始准备播放器
            control = (MusicPlayerService.MusicControl) service;
            control.setMusicUri(uri);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        MyViewUtil.setSysBarTransparent(this);
        trackBg = findViewById(R.id.track_bg);
        trackImg = findViewById(R.id.track_img);
        Intent detailIntent = getIntent();
        String uriString = detailIntent.getStringExtra("uri");
        trackImgUrl = detailIntent.getStringExtra("trackImgUrl");
        uri = Uri.parse(uriString);

        Intent intent = new Intent(getApplicationContext(), MusicPlayerService.class);
        bindService(intent,conn,BIND_AUTO_CREATE);

        init();
        loadPics();
    }


    //加载中间和背景图片
    private void loadPics() {

        Glide.with(this).load(trackImgUrl)
                .apply(trackBg.setGaussBlur())
                .into(trackBg);
        Glide.with(this).load(trackImgUrl).into(trackImg);
    }


    public void init() {
        // 光盘（设置动画）
        View iv_disk = findViewById(R.id.iv_music);
        musicProgressBar = findViewById(R.id.sb);
        currentTv = findViewById(R.id.tv_progress);
        totalTv = findViewById(R.id.tv_total);
        // 四个控制按钮
        Button btn_play = findViewById(R.id.btn_play);
        Button btn_pause = findViewById(R.id.btn_pause);
        Button btn_exit = findViewById(R.id.btn_exit);
        //监听
        btn_play.setOnClickListener(this);
        btn_pause.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        //动画
        animator = ObjectAnimator.ofFloat(iv_disk, "rotation", 0, 360.0F); // 对象是iv_disk，动作是rotation，角度从0到360度
        animator.setDuration(10000); // 设置动画的时长，单位为毫秒，这里设置10秒一圈
        animator.setInterpolator(new LinearInterpolator()); // 旋转时间函数为线性，意为匀速旋转
        animator.setRepeatCount(-1); // 设置转动圈数，-1为一直转动


        // 音乐进度条增加拖动事件监听
        musicProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当音乐停止后，停止光盘动画
                if (progress == seekBar.getMax()) {
                    animator.pause();
                }
                // 判断是不是用户拖动的
                if (fromUser) {
                    control.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                control.pause();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                control.play();
            }
        });


    }


    //三个按钮的点击事件
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                if(musicProgressBar.getProgress() == 0){
                    animator.start();
                }else {
                    animator.resume();
                }
                control.play();
                break;
            case R.id.btn_pause:
                // 暂停音乐
                // 光盘暂停
                animator.pause();
                control.pause();
                break;
            case R.id.btn_exit:
                // 退出应用
                finish();
                break;
        }
    }




    //页面销毁事件
    protected void onDestroy() {
        control.stop();
        super.onDestroy();
        unbindService(conn);
    }



    // 获取从MusicPlayerServices传递过来的消息
    public static Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData(); //获取从子线程发送过来的音乐播放进度
            int duration = bundle.getInt("duration");                  //歌曲的总时长
            int currentPosition = bundle.getInt("currentPosition");//歌曲当前进度
            musicProgressBar.setMax(duration);                //设置SeekBar的最大值为歌曲总时长
            musicProgressBar.setProgress(currentPosition);//设置SeekBar当前的进度位置
            String totalTime = msToMinSec(duration); // 歌曲总时长
            String currentTime = msToMinSec(currentPosition); // 歌曲当前播放时长
            totalTv.setText(totalTime);
            currentTv.setText(currentTime);
        }
    };



    //毫秒转换 分：秒
    public static String msToMinSec(int ms) {
        int sec = ms / 1000;
        int min = sec / 60;
        sec -= min * 60;
        return String.format("%02d:%02d", min, sec);
    }


}