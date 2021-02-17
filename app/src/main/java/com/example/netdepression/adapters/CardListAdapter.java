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
import com.example.netdepression.base.CardlistItem;
import com.example.netdepression.utils.MyApplication;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    private List<CardlistItem> cardItemList;

    public CardListAdapter(List<CardlistItem> cardItemList) {
        this.cardItemList = cardItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardlistItem cardlistItem = cardItemList.get(position);
        holder.title.setText(cardlistItem.getTitle());
        Glide.with(MyApplication.getContext()).load(cardlistItem.getImageUrl()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img ;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title);
        }
    }
}
