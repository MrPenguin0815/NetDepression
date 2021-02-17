package com.example.netdepression.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.netdepression.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class MyInfoFragment extends BaseFragment {



    @Override
    protected View onSubViewLoaded(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment,null);
        initCollapsingToolbar(view);
        return view;
    }


    private void initCollapsingToolbar(View view) {

        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.bringToFront();

        //配置toolbar
        toolbar.setTitle("TestTitle");
        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);

        //把配置好的toolbar放到actionBar的位置  这里可以设置自己的ic
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        assert appCompatActivity != null;
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //配置折叠toolbar
        collapsingToolbarLayout.setTitleEnabled(false);
        ImageView backgroundImgView = view.findViewById(R.id.background_img);
        int testImageId = R.drawable.lunbotu01;
        Glide.with(getActivity()).load(testImageId).into(backgroundImgView);
    }
}
