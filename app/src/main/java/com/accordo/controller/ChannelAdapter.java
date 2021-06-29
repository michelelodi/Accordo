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
    private ConnectionController cc;
    private SharedPreferencesController spc;

    public ChannelAdapter(Context context, OnListClickListener listClickListener) {
        mInflater = LayoutInflater.from(context);
        mListClickListener = listClickListener;
        cc = new ConnectionController(context);
        spc = SharedPreferencesController.getInstance(context);
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
    }

    @Override
    public int getItemCount() { return AppModel.getInstance().channelsSize(); }
}
