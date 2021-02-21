package com.example.netdepression;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayerService extends Service {


    private MediaPlayer mediaPlayer;
    private Timer timer;

    public MusicPlayerService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //TODO:设置那个回调接口  因为很大概率要播放失败
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicControl(); // 绑定服务的时候，把音乐控制类实例化
    }


    /**
     * 内部类music control继承Binder。
     * 该内部类的功能是让主程序能够控制service里面的多媒体对象
     */
    class MusicControl extends Binder{

      Uri musicUri;

        public void setMusicUri(Uri musicUri) {
            this.musicUri = musicUri;
            //设置音频文件到MediaPlayer对象中
            try {
                mediaPlayer.setDataSource(getApplicationContext(),musicUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //让MediaPlayer对象准备，用这个方法防止加载时耗时导致anr
            mediaPlayer.prepareAsync();
        }

        // 播放音乐
        public void play() {
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }
            addTimer();
        }

        // 暂停
        public void pause() {
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        }

        // 停止
        public void stop() {
            mediaPlayer.stop(); // 停止音乐
            mediaPlayer.release();
            mediaPlayer = null;
            //如果没有设置计时器，时间就会成为一个null，这时候如果调用cancel会报错
            try {
                if(timer != null){
                    timer.cancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 打带
        public void seekTo(int ms) {
            mediaPlayer.seekTo(ms);
        }

    }


    // 增加计时器
    public void addTimer() {
        if (timer == null) {
            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    int duration = mediaPlayer.getDuration(); // 获取歌曲总时长
                    int currentPos = mediaPlayer.getCurrentPosition(); // 获取当前播放进度
                    Message msg = PlayActivity.handler.obtainMessage(); // 创建消息对象
                    // 将音乐的总时长和播放进度封装至消息对象中
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration", duration);
                    bundle.putInt("currentPosition", currentPos);
                    msg.setData(bundle);
                    // 将消息发送到主线程的消息队列
                    PlayActivity.handler.sendMessage(msg);
                }
            };
            //开始计时任务后的5毫秒，第一次执行task任务，以后每500毫秒执行一次
            timer.schedule(task, 5, 500);
        }
    }
}