package com.example.netdepression.adapters;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    List<View> cardViews;

    public void setViewList(List<View> viewList){
        cardViews = viewList;
    }

    @Override
    public int getCount() {
//        Log.e("XXX", "getCount: " + cardViews.size());
        return cardViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View v = cardViews.get(position);
        container.removeView(v);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = cardViews.get(position);
        container.addView(v);
        return v;
    }
}
