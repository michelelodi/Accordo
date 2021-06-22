package com.accordo.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accordo.R;
import com.accordo.data.AppModel;
import com.accordo.data.Post;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private LayoutInflater mInflater;
    private OnListClickListener mListClickListener;
    private String cTitle;

    public PostAdapter(Context context, OnListClickListener listClickListener, String cTitle) {
        mInflater = LayoutInflater.from(context);
        mListClickListener = listClickListener;
        this.cTitle = cTitle;
    }

    @NonNull
    @NotNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_post,parent,false);
        return new PostViewHolder(view,mListClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostViewHolder holder, int position) {
        Post p = AppModel.getInstance().getPost(cTitle, position);
        holder.updateContent(p.getContent(),p.getAuthor());
    }

    @Override
    public int getItemCount() { return AppModel.getInstance().channelSize(cTitle); }
}
