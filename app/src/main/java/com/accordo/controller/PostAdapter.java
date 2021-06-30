package com.accordo.controller;

import android.app.Activity;
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
    private Context context;
    private Activity mActivity;

    public PostAdapter(Context context, OnListClickListener listClickListener, Activity activity, String cTitle) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        mActivity = activity;
        mListClickListener = listClickListener;
        this.cTitle = cTitle;
    }

    @NonNull
    @NotNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_post,parent,false);
        return new PostViewHolder(view, mListClickListener, context, mActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostViewHolder holder, int position) {
        Post p = AppModel.getInstance().getPost(cTitle, position);
        holder.updateContent(p);
    }

    @Override
    public int getItemCount() { return AppModel.getInstance().channelSize(cTitle); }
}
