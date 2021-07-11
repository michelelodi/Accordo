package com.accordo.controller;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.accordo.R;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelViewHolder extends RecyclerView.ViewHolder {

    private TextView cTitle;
    private OnListClickListener mListClickListener;

    public ChannelViewHolder(@NonNull @NotNull View itemView, OnListClickListener listClickListener) {
        super(itemView);
        cTitle = itemView.findViewById(R.id.cTitle);
        itemView.setOnClickListener(v -> mListClickListener.onListClick(v, getAdapterPosition()));
        mListClickListener = listClickListener;
    }

    public void updateContent(String cTitle, String creator) {
        this.cTitle.setText(cTitle);
        if(creator.equals("true")) {
            this.cTitle.setTextColor(itemView.getResources().getColor(R.color.primary_blue));
            this.cTitle.setTypeface(Typeface.DEFAULT_BOLD);
        }
        else {
            this.cTitle.setTextColor(itemView.getResources().getColor(R.color.secondary_light_blue));
            this.cTitle.setTypeface(Typeface.SANS_SERIF);
        }
    }
}
