package com.example.netdepression.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.netdepression.R;
import com.example.netdepression.adapters.CardListAdapter;
import com.example.netdepression.adapters.IcSetAdapter;
import com.example.netdepression.adapters.ViewPagerAdapter;
import com.example.netdepression.base.DiscoverIc;
import com.example.netdepression.base.CardlistItem;
import com.example.netdepression.gson.Banner;
import com.example.netdepression.gson.BannerBlock;
import com.example.netdepression.gson.Creative;
import com.example.netdepression.gson.PlaylistBlock;
import com.example.netdepression.interfaces.IDiscoverViewCallback;
import com.example.netdepression.presenters.DiscoverPresenter;
import com.example.netdepression.utils.ParseUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;


public class DiscoverFragment extends BaseFragment implements IDiscoverViewCallback {


    private ViewPager imgViewpager;
    private RecyclerView icSetRecycler;
    private RecyclerView recommendPlaylistRecycler;
    private RecyclerView exclusivePlaylistRecycler;
    private RecyclerView videoListRecycler;
    private View carouselView;
    private View testView;
    private LinearLayout calendarView;
    private DiscoverPresenter mPresenter;
    private final int PIC_COUNT = 3;

    private int currentItem;

    @Override
    protected View onSubViewLoaded(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.discover_fragment, null);

        carouselView = view.findViewById(R.id.carousel);
        testView = view.findViewById(R.id.tri_vp_item);
        recommendPlaylistRecycler = view.findViewById(R.id.card_recycler1);
        exclusivePlaylistRecycler = view.findViewById(R.id.card_recycler2);
        videoListRecycler = view.findViewById(R.id.card_recycler3);
        icSetRecycler = view.findViewById(R.id.ic_set_recycler);
        calendarView = view.findViewById(R.id.calendar);


        initIcSetRecycler();
        mPresenter = DiscoverPresenter.getInstance();
        mPresenter.registerViewCallback(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Set<String> set = prefs.getStringSet("blockStrings",null);
        if (set == null){
            mPresenter.getDiscoverData();
        }else {
            List<String> list = new ArrayList<>(set);
            mPresenter.getDiscoverInfo(list);
        }
        return view;
    }


    /**
     * 加载索引图标recycler
     */
    private void initIcSetRecycler() {
        //初始化discoverIcList
        List<DiscoverIc> discoverIcList = new ArrayList<>();
        discoverIcList.add(new DiscoverIc("每日推荐", R.drawable.icset_daily_recommend));
        discoverIcList.add(new DiscoverIc("私人FM", R.drawable.icset_private_fm));
        discoverIcList.add(new DiscoverIc("歌单", R.drawable.icset_songlist));
        discoverIcList.add(new DiscoverIc("排行榜", R.drawable.icset_chart));
        discoverIcList.add(new DiscoverIc("数字专辑", R.drawable.icset_record));
        discoverIcList.add(new DiscoverIc("歌房", R.drawable.icset_song_house));
        discoverIcList.add(new DiscoverIc("身边", R.drawable.icset_surround));

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        icSetRecycler.setLayoutManager(linearLayoutManager);


        //设置适配器
        IcSetAdapter icSetAdapter = new IcSetAdapter();
        icSetAdapter.setDiscoverIcList(discoverIcList);
        icSetRecycler.setAdapter(icSetAdapter);
    }


    /**
     * 加载轮播图
     */
    @Override
    public void onBannerBlockLoaded(BannerBlock bannerBlock) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<View> dots = new ArrayList<>();
                final ArrayList<View> cardViews = new ArrayList<>();

                //获取imgViewpager
                imgViewpager = carouselView.findViewById(R.id.img_viewpager);
                //获取imgViewpager中的滑动子项
                for (int i = 0; i < PIC_COUNT; i++) {
                    View carouselCard = LayoutInflater.from(getContext()).inflate(R.layout.carousel_item, null);
                    ImageView imageView = carouselCard.findViewById(R.id.carousel_img);
                    Banner banner = bannerBlock.extInfo.banners.get(i);
                    Glide.with(getContext()).load(banner.pic).into(imageView);
                    cardViews.add(carouselCard);
                }
                //显示的点的集合
                dots.add(carouselView.findViewById(R.id.p1));
                dots.add(carouselView.findViewById(R.id.p2));
                dots.add(carouselView.findViewById(R.id.p3));
                //添加适配器
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
                viewPagerAdapter.setViewList(cardViews);
                imgViewpager.setAdapter(viewPagerAdapter);
                //监听页面滑动，切换点的效果
                imgViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    //记录位置
                    int oldPosition = 0;

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        dots.get(oldPosition).setBackgroundResource(R.drawable.dot_unfocusd);
                        dots.get(position).setBackgroundResource(R.drawable.dot_focused);
                        oldPosition = position;
                        currentItem = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

            }
        });

    }







    /**
     * 加载推荐歌单
     */
    @Override
    public void onPlaylistBlockLoaded(PlaylistBlock playlistBlock) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<CardlistItem> cardlistItems = ParseUtil.handlePlaylistBlock(playlistBlock);
                //设置布局管理器
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                recommendPlaylistRecycler.setLayoutManager(linearLayoutManager);
                //设置适配器
                CardListAdapter cardListAdapter = new CardListAdapter(cardlistItems);
                recommendPlaylistRecycler.setAdapter(cardListAdapter);
            }
        });
    }










    /**
     * 加载专属场景歌单
     */
    @Override
    public void onOfficialPlaylistBlockLoaded(PlaylistBlock playlistBlock) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<CardlistItem> cardlistItems = ParseUtil.handlePlaylistBlock(playlistBlock);
                //设置布局管理器
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                exclusivePlaylistRecycler.setLayoutManager(linearLayoutManager);
                //设置适配器
                CardListAdapter cardListAdapter = new CardListAdapter(cardlistItems);
                exclusivePlaylistRecycler.setAdapter(cardListAdapter);
            }
        });
    }







    /**
     * 加载视频合辑
     */
    @Override
    public void onVideoBlockLoaded(PlaylistBlock playlistBlock) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<CardlistItem> cardlistItems = ParseUtil.handlePlaylistBlock(playlistBlock);
                //设置布局管理器
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                videoListRecycler.setLayoutManager(linearLayoutManager);
                //设置适配器
                CardListAdapter cardListAdapter = new CardListAdapter(cardlistItems);
                videoListRecycler.setAdapter(cardListAdapter);
            }
        });
    }







    /**
     * 加载音乐日历
     */
    @Override
    public void onCalendarLoaded(PlaylistBlock playlistBlock) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView mt = calendarView.findViewById(R.id.m_t);
                LinearLayout itemContainer = calendarView.findViewById(R.id.itemContainer);
                mt.setText(playlistBlock.blockUiElement.subTitle.title);
                List<CardlistItem> cardlistItems = ParseUtil.handlePlaylistBlock(playlistBlock);
                for (CardlistItem c: cardlistItems) {
                    View v = LayoutInflater.from(getContext()).inflate(R.layout.simple_list_item,null);
                    ImageView imageView = v.findViewById(R.id.simple_cover_img);
                    TextView textView = v.findViewById(R.id.main_title);
                    Glide.with(getContext()).load(c.getImageUrl()).into(imageView);
                    textView.setText(c.getTitle());
                    itemContainer.addView(v);
                }
            }
        });
    }



    /**
     * 加载私人订制
     */
    @Override
    public void onStyleBlockLoaded(PlaylistBlock playlistBlock) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView mt = testView.findViewById(R.id.mt);
                TextView st = testView.findViewById(R.id.st);
                mt.setText(playlistBlock.blockUiElement.mainTitle.title);
                st.setText(playlistBlock.blockUiElement.subTitle.title);


                Creative c = playlistBlock.creatives.get(0);
                TextView t1 = testView.findViewById(R.id.main_title1);
                TextView t2 = testView.findViewById(R.id.main_title2);
                TextView t3 = testView.findViewById(R.id.main_title3);
                t1.setText(c.resources.get(0).uiElement.mainTitle.title);
                t2.setText(c.resources.get(1).uiElement.mainTitle.title);
                t3.setText(c.resources.get(2).uiElement.mainTitle.title);
                TextView s1 = testView.findViewById(R.id.sub_title1);
                TextView s2 = testView.findViewById(R.id.sub_title2);
                TextView s3 = testView.findViewById(R.id.sub_title3);
                if (c.resources.get(0).uiElement.subTitle != null)
                    s1.setText(c.resources.get(0).uiElement.subTitle.title);
                if (c.resources.get(1).uiElement.subTitle != null)
                    s2.setText(c.resources.get(1).uiElement.subTitle.title);
                if (c.resources.get(2).uiElement.subTitle != null)
                    s3.setText(c.resources.get(2).uiElement.subTitle.title);
                ImageView m1 = testView.findViewById(R.id.simple_cover_img1);
                ImageView m2 = testView.findViewById(R.id.simple_cover_img2);
                ImageView m3 = testView.findViewById(R.id.simple_cover_img3);
                Glide.with(getContext()).load(c.resources.get(0).uiElement.image.imageUrl).into(m1);
                Glide.with(getContext()).load(c.resources.get(1).uiElement.image.imageUrl).into(m2);
                Glide.with(getContext()).load(c.resources.get(2).uiElement.image.imageUrl).into(m3);
            }
        });
    }


    /**
     * 通知接口的解绑
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.unRegisterViewCallback(this);
        }
    }

//
//    //以下实现自动轮播功能
//    @Override
//    public void onStart() {
//        super.onStart();
//        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(),3,3, TimeUnit.SECONDS);
//    }
//
//
//    class ViewPagerTask implements Runnable{
//
//        @Override
//        public void run() {
//            currentItem = (currentItem + 1) % 3;
//            handler.obtainMessage().sendToTarget();
//        }
//    }
//
//
//
//    @SuppressLint("HandlerLeak")
//    private final Handler handler=new Handler(){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            imgViewpager.setCurrentItem(currentItem);
//        }
//    };


}