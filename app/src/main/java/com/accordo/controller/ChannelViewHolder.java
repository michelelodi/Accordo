package com.accordo.controller;

import android.view.View;
import android.widget.TextView;

import com.accordo.R;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelViewHolder extends RecyclerView.ViewHolder {

    private TextView cTitle, creator;
    private OnListClickListener mListClickListener;
    public ChannelViewHolder(@NonNull @NotNull View itemView, OnListClickListener listClickListener) {
        super(itemView);
        cTitle = itemView.findViewById(R.id.cTitle);
        creator = itemView.findViewById(R.id.creator);
        itemView.setOnClickListener(v -> mListClickListener.onListClick(v, getAdapterPosition()));
        mListClickListener = listClickListener;
    }

    public void updateContent(String cTitle, String creator) {
        this.cTitle.setText(cTitle);
        this.creator.setText(creator);
    }
}
