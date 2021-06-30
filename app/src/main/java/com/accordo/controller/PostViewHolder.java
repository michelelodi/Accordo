package com.accordo.controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.accordo.R;
import com.accordo.data.AppModel;
import com.accordo.data.ImagePost;
import com.accordo.data.LocationPost;
import com.accordo.data.Post;
import com.accordo.data.TextPost;
import com.accordo.data.roomDB.AccordoDB;
import com.accordo.utils.ImageUtils;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class PostViewHolder extends RecyclerView.ViewHolder {

    private TextView name, textContent;
    private ImageView imageContent, profilePic;
    private OnListClickListener postListClickListener;
    private Context context;
    private AppModel model;
    private Looper secondaryThreadLooper;
    private AccordoDB db;
    private Activity mActivity;
    private ConnectionController cc;

    public PostViewHolder(@NonNull @NotNull View itemView, OnListClickListener listClickListener, Context context, Activity activity) {
        super(itemView);
        HandlerThread handlerThread = new HandlerThread("MyHandlerThreadPostViewHolder");
        handlerThread.start();
        secondaryThreadLooper = handlerThread.getLooper();
        textContent = itemView.findViewById(R.id.textContent);
        name = itemView.findViewById(R.id.name);
        imageContent = itemView.findViewById(R.id.imageContent);
        profilePic = itemView.findViewById(R.id.profilePic);
        itemView.setOnClickListener(v -> postListClickListener.onListClick(v, getAdapterPosition()));
        postListClickListener = listClickListener;
        this.context = context;
        model = AppModel.getInstance();
        db = Room.databaseBuilder(context,
                AccordoDB.class, "accordo_database")
                .build();
        mActivity = activity;
        cc = new ConnectionController(context);
    }

    public void updateContent(Post p) {
        textContent.setVisibility(View.GONE);
        imageContent.setVisibility(View.GONE);
        name.setText(p.getAuthor());
        if(model.hasProfilePic(p.getAuthorUid())) profilePic.setImageBitmap(model.getProfilePicture(p.getAuthorUid()));
        else {
            (new Handler(secondaryThreadLooper)).post(() -> AccordoDB.databaseWriteExecutor.execute(() -> {
                if(db.profilePictureDao().getPicture(p.getAuthorUid()) != null){
                    Bitmap bm = ImageUtils.base64ToBitmap(db.profilePictureDao().getPicture(p.getAuthorUid()));
                    model.addProfilePicture(p.getAuthorUid(),
                            bm,
                            db.profilePictureDao().getVersion(p.getAuthorUid()));
                    mActivity.runOnUiThread(() -> profilePic.setImageBitmap(bm));
                }
                else {
                    //TODO getPicture from server
                }
            }));
        }
        profilePic.setImageResource(R.drawable.missing_profile);
        if(p instanceof ImagePost) {
            imageContent.setVisibility(View.VISIBLE);
            if(p.getContent() != null) imageContent.setImageBitmap(ImageUtils.base64ToBitmap(p.getContent()));
            else imageContent.setImageResource(R.drawable.content_missing);
            imageContent.setAdjustViewBounds(true);
            imageContent.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else if(p instanceof LocationPost || p instanceof TextPost){
            textContent.setVisibility(View.VISIBLE);
            textContent.setText(p.getContent());
        }
    }
}
