package com.example.netdepression.fragments;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.netdepression.gson.PlaylistBlock;
import com.example.netdepression.interfaces.IDiscoverViewCallback;
import com.example.netdepression.presenters.DiscoverPresenter;
import com.example.netdepression.utils.ParseUtil;

import java.util.ArrayList;
import java.util.List;


public class DiscoverFragment extends BaseFragment implements IDiscoverViewCallback {


    private ViewPager imgViewpager;
    private RecyclerView icSetRecycler;
    private RecyclerView recommendPlaylistRecycler;
    private RecyclerView exclusivePlaylistRecycler;
    private RecyclerView videoListRecycler;
    private View carouselView;
    private final int PIC_COUNT = 3;
    DiscoverPresenter mPresenter;

    private int currentItem;

    @Override
    protected View onSubViewLoaded(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.discover_fragment, null);
        carouselView = view.findViewById(R.id.carousel);
        recommendPlaylistRecycler = view.findViewById(R.id.card_recycler1);
        exclusivePlaylistRecycler = view.findViewById(R.id.card_recycler2);
        videoListRecycler = view.findViewById(R.id.card_recycler3);
        icSetRecycler = view.findViewById(R.id.ic_set_recycler);

       // initSimpleListPager(view, inflater);
        initIcSetRecycler();

        mPresenter = DiscoverPresenter.getInstance();
        mPresenter.registerViewCallback(this);
        mPresenter.getDiscoverData();
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
     * 加载简单列表Viewpager
     */
    private void initSimpleListPager(View view, LayoutInflater inflater) {
        final ArrayList<View> simpleListViews = new ArrayList<>();

        //获取simpleListPager
        View newSongCardView = view.findViewById(R.id.new_song_card);
        ViewPager simpleListPager = newSongCardView.findViewById(R.id.simple_list_pager);

        //获取imgViewpager中的滑动子项
        simpleListViews.add(inflater.inflate(R.layout.simple_list_item, (ViewGroup) newSongCardView));

        //添加适配器
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
        viewPagerAdapter.setViewList(simpleListViews);
        simpleListPager.setAdapter(viewPagerAdapter);
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
              for (int i = 0;i < PIC_COUNT;i++) {
                  View carouselCard = LayoutInflater.from(getContext()).inflate(R.layout.carousel_item, null);
                  ImageView imageView = carouselCard.findViewById(R.id.carousel_img);
                  Banner banner = bannerBlock.extInfo.banners.get(i);
                  Glide.with(getContext()).load(banner.pic).into(imageView);
                  Log.e("DiscoverFragment", "bannerBlock.extInfo.banner: " + banner.pic);
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
     * 通知接口的解绑
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPresenter != null){
            mPresenter.unRegisterViewCallback(this);
        }
    }


    //以下实现自动轮播功能
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