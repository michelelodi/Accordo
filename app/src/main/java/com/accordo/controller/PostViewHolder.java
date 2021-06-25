package com.accordo.controller;

import android.view.View;
import android.widget.TextView;

import com.accordo.R;
import com.accordo.data.Post;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostViewHolder extends RecyclerView.ViewHolder {

    private TextView name, content;
    private OnListClickListener mListClickListener;

    public PostViewHolder(@NonNull @NotNull View itemView, OnListClickListener listClickListener) {
        super(itemView);
        content = itemView.findViewById(R.id.content);
        name = itemView.findViewById(R.id.name);
        itemView.setOnClickListener(v -> mListClickListener.onListClick(v, getAdapterPosition()));
        mListClickListener = listClickListener;
    }

    public void updateContent(Post p) {
        this.content.setText(p.getContent());
        this.name.setText(p.getAuthor());
    }
}
