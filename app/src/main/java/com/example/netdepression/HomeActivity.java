package com.example.netdepression;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.netdepression.fragments.DiscoverFragment;
import com.example.netdepression.fragments.ClassifyFragment;
import com.example.netdepression.fragments.MyInfoFragment;
import com.example.netdepression.utils.MyViewUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    int[] icInts = {R.drawable.ic_discover02,R.drawable.ic_broadcast01,R.drawable.ic_my01};
    String[] tabTitles = {"发现","热门","我的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MyViewUtil.setStatusBarTransparent(this);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setSelectedTabIndicator(null);



        //初始化FragmentLists
        List<Fragment> FragmentLists = new ArrayList<>();
        FragmentLists.add(new DiscoverFragment());
        FragmentLists.add(new ClassifyFragment());
        FragmentLists.add(new MyInfoFragment());



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