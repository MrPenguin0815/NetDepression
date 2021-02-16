package com.example.netdepression.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netdepression.R;
import com.example.netdepression.base.DiscoverIc;

import java.util.List;

public class IcSetAdapter extends RecyclerView.Adapter<IcSetAdapter.ViewHolder> {

    private List<DiscoverIc> mDiscoverIcList;

    public void setDiscoverIcList(List<DiscoverIc> mDiscoverIcList) {
        this.mDiscoverIcList = mDiscoverIcList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_icset_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiscoverIc discoverIc = mDiscoverIcList.get(position);
        holder.icImg.setImageResource(discoverIc.getImageId());
        holder.icName.setText(discoverIc.getName());
    }

    @Override
    public int getItemCount() {
        return mDiscoverIcList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icImg;
        TextView icName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icImg = itemView.findViewById(R.id.icset_ic);
            icName = itemView.findViewById(R.id.icset_name);
        }
    }
}
