package com.example.netdepression.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netdepression.R;
import com.example.netdepression.gson.Track;
import com.example.netdepression.utils.MyApplication;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder>{

    private List<Track> mTrackList;
    private OnItemClickListener mOnItemClickListener;

    public TrackAdapter(List<Track> tracks) {
        mTrackList = tracks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Track track = mTrackList.get(position);
            holder.name.setText(track.al.name);
            Glide.with(MyApplication.getContext()).load(track.al.picUrl).into(holder.cover);
            holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrackList.size();
    }


    //内部类
    static class ViewHolder extends RecyclerView.ViewHolder{
        View rootView;
        ImageView cover;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            cover = itemView.findViewById(R.id.track_cover);
            name = itemView.findViewById(R.id.track_name);
        }
    }


    //内部接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }



    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
