package com.accordo.controller;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.accordo.R;
import com.accordo.data.ImagePost;
import com.accordo.data.LocationPost;
import com.accordo.data.Post;
import com.accordo.data.TextPost;
import com.accordo.utils.ImageUtils;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class PostViewHolder extends RecyclerView.ViewHolder {

    private TextView name, textContent;
    private ImageView imageContent;
    private OnListClickListener postListClickListener;

    public PostViewHolder(@NonNull @NotNull View itemView, OnListClickListener listClickListener) {
        super(itemView);
        textContent = itemView.findViewById(R.id.textContent);
        name = itemView.findViewById(R.id.name);
        imageContent = itemView.findViewById(R.id.imageContent);
        itemView.setOnClickListener(v -> postListClickListener.onListClick(v, getAdapterPosition()));
        postListClickListener = listClickListener;
    }

    public void updateContent(Post p) {
        textContent.setVisibility(View.GONE);
        imageContent.setVisibility(View.GONE);
        if(p instanceof ImagePost) {
            imageContent.setVisibility(View.VISIBLE);
            if(p.getContent() != null) imageContent.setImageBitmap(ImageUtils.base64ToBitmap(p.getContent()));
            else {
                imageContent.setImageResource(R.drawable.content_missing);
            }
            imageContent.setAdjustViewBounds(true);
            imageContent.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    500));
            imageContent.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else if(p instanceof LocationPost || p instanceof TextPost){
            textContent.setVisibility(View.VISIBLE);
            textContent.setText(p.getContent());
        }
        name.setText(p.getAuthor());
    }
}
