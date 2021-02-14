package com.example.netdepression;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.netdepression.indicator.CommonNavigator;
import com.example.netdepression.indicator.CommonNavigatorAdapter;
import com.example.netdepression.indicator.CommonPagerTitleView;
import com.example.netdepression.indicator.IPagerIndicator;
import com.example.netdepression.indicator.IPagerTitleView;
import com.example.netdepression.indicator.MagicIndicator;
import com.example.netdepression.indicator.ViewPagerHelper;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    int[] icInts = {R.drawable.ic_discover02,R.drawable.ic_broadcast01,R.drawable.ic_my01};
    String[] tabTitles = {"发现","播客","我的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //设置顶部透明
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(Color.TRANSPARENT);
        }
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setSelectedTabIndicator(null);



        //初始化FragmentLists
        List<Fragment> FragmentLists = new ArrayList<>();
        FragmentLists.add(new FirstFragment());
        FragmentLists.add(new SecondFragment());
        FragmentLists.add(new ThirdFragment());



        //重写适配器中的方法
        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return FragmentLists.get(position);
            }

            @Override
            public int getCount() {
                return FragmentLists.size();
            }




        };


        //给viewPager添加适配器
        viewPager.setAdapter(mPagerAdapter);



        //设置TabLayout和ViewPager联动
        tabLayout.setupWithViewPager(viewPager,false);


        //初始化每一个tab中的布局
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(getTabView(i));
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tab.getCustomView().findViewById(R.id.tab_icon).setBackgroundResource(R.drawable.ic_discover02);
                        break;
                    case 1:
                        tab.getCustomView().findViewById(R.id.tab_icon).setBackgroundResource(R.drawable.ic_broadcast02);
                        break;
                    case 2:
                        tab.getCustomView().findViewById(R.id.tab_icon).setBackgroundResource(R.drawable.ic_my02);
                        break;

                }
                TextView textView = tab.getCustomView().findViewById(R.id.tab_text);
                textView.setTextColor(getResources().getColor(R.color.black));
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tab.getCustomView().findViewById(R.id.tab_icon).setBackgroundResource(R.drawable.ic_discover01);
                        break;
                    case 1:
                        tab.getCustomView().findViewById(R.id.tab_icon).setBackgroundResource(R.drawable.ic_broadcast01);
                        break;
                    case 2:
                        tab.getCustomView().findViewById(R.id.tab_icon).setBackgroundResource(R.drawable.ic_my01);
                        break;

                }
                TextView textView = tab.getCustomView().findViewById(R.id.tab_text);
                textView.setTextColor(getResources().getColor(R.color.unselected));
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }


    public View getTabView(int position) {
        View v = LayoutInflater.from(HomeActivity.this).inflate(R.layout.tab_item, null);
        ImageView iv = v.findViewById(R.id.tab_icon);
        TextView tv = v.findViewById(R.id.tab_text);
        iv.setBackgroundResource(icInts[position]);
        tv.setText(tabTitles[position]);
        if (position == 0) {
            tv.setTextColor(v.getResources().getColor(R.color.black));
        }else {
            tv.setTextColor(v.getResources().getColor(R.color.unselected));
        }
        return v;
    }

}