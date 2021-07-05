package com.accordo.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accordo.R;
import com.accordo.data.AppModel;
import com.accordo.data.Channel;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelViewHolder> {

    private LayoutInflater mInflater;
    private OnListClickListener mListClickListener;
    private ChannelViewHolder holder;

    public ChannelAdapter(Context context, OnListClickListener listClickListener) {
        mInflater = LayoutInflater.from(context);
        mListClickListener = listClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public ChannelViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_channel,parent,false);
        return new ChannelViewHolder(view,mListClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChannelViewHolder holder, int position) {
        Channel ch = AppModel.getInstance().getChannel(position);
        holder.updateContent(ch.getCTitle(), Boolean.toString(ch.isMine()));
        this.holder = holder;
    }

    @Override
    public int getItemCount() { return AppModel.getInstance().channelsSize(); }

    public int getAdapterPosition() { return holder.getAdapterPosition(); }
}
