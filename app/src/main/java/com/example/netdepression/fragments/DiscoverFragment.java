package com.example.netdepression.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.example.netdepression.R;
import com.example.netdepression.adapters.IcSetAdapter;
import com.example.netdepression.adapters.ViewPagerAdapter;
import com.example.netdepression.base.DiscoverIc;
import com.example.netdepression.utils.MyViewUtils;

import java.util.ArrayList;
import java.util.List;


public class DiscoverFragment extends Fragment {


    private ViewPager imgViewpager;
    private ViewPager simpleListPager;
    private RecyclerView icSetRecycler;

    private int currentItem;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.discover_fragment, null);

        initImgViewpager(view, inflater);
        initSimpleListPager(view, inflater);
        initIcSetRecycler(view);

        return view;
    }


    /**
     * 初始化 索引图标recycler
     */
    private void initIcSetRecycler(View view) {
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
        icSetRecycler = view.findViewById(R.id.ic_set_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        icSetRecycler.setLayoutManager(linearLayoutManager);


        //设置适配器
        IcSetAdapter icSetAdapter = new IcSetAdapter();
        icSetAdapter.setDiscoverIcList(discoverIcList);
        icSetRecycler.setAdapter(icSetAdapter);
    }


    /**
     * 初始化 简单列表Viewpager
     */
    private void initSimpleListPager(View view, LayoutInflater inflater) {
        final ArrayList<View> simpleListViews = new ArrayList<>();

        //获取simpleListPager
        View newSongCardView = view.findViewById(R.id.new_song_card);
        simpleListPager = newSongCardView.findViewById(R.id.simple_list_pager);
        //获取imgViewpager中的滑动子项
        simpleListViews.add(inflater.inflate(R.layout.simple_list_item, null));

        //添加适配器
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
        viewPagerAdapter.setViewList(simpleListViews);
        simpleListPager.setAdapter(viewPagerAdapter);
    }


    /**
     * 初始化 轮播图Viewpager
     */
    private void initImgViewpager(View view, LayoutInflater inflater) {
        final int[] imageId = {R.drawable.lunbotu01, R.drawable.lunbotu02, R.drawable.lunbotu03};
        final ArrayList<View> dots = new ArrayList<>();
        final ArrayList<View> cardViews = new ArrayList<>();

        //获取imgViewpager
        View carouselView = view.findViewById(R.id.carousel);
        imgViewpager = carouselView.findViewById(R.id.img_viewpager);
        //获取imgViewpager中的滑动子项
        for (int value : imageId) {
            View carouselCard = inflater.inflate(R.layout.carousel_item, null);
            ImageView imageView = carouselCard.findViewById(R.id.carousel_img);
            imageView.setImageResource(value);
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