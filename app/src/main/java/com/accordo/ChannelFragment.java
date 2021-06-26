package com.accordo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accordo.controller.ConnectionController;
import com.accordo.controller.PostAdapter;
import com.accordo.controller.SharedPreferencesController;
import com.accordo.data.AppModel;
import com.accordo.data.ImagePost;
import com.accordo.data.LocationPost;
import com.accordo.data.Post;
import com.accordo.data.roomDB.AccordoDB;
import com.accordo.data.roomDB.PostImage;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class ChannelFragment extends Fragment {

    private final String TAG = "MYTAG_ChannelFragment";
    private final String CURRENT_USER = "current_user";
    private static final String CTITLE = "cTitle";

    private String mCtitle;
    private PostAdapter adapter;
    private ConnectionController cc;
    private SharedPreferencesController spc;
    private AppModel model;
    private AccordoDB db;

    public ChannelFragment() {}

    public static ChannelFragment newInstance(String cTitle) {
        ChannelFragment fragment = new ChannelFragment();
        Bundle args = new Bundle();
        args.putString(CTITLE, cTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cc = new ConnectionController(getContext());
        spc = SharedPreferencesController.getInstance();
        model = AppModel.getInstance();
        db = Room.databaseBuilder(MainActivity.getAppContext(),
                AccordoDB.class, "accordo_database")
                .build();
        if (getArguments() != null) mCtitle = getArguments().getString(CTITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { return inflater.inflate(R.layout.fragment_channel, container, false); }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.channelRecyclerView);
        adapter = new PostAdapter(getContext(), ChannelFragment.this::handleListClick, mCtitle);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        for(Post post : model.getChannelPosts(mCtitle)){
            if(post instanceof ImagePost && post.getContent() == null) {
                cc.getPostImage(spc.readStringFromSP(CURRENT_USER, ""), post.getPid(),
                        response -> getPostImageResponse(response, post),
                        this::getPostImageError);
            }
        }
    }

    private void handleListClick(View v, int position){
        Post p = AppModel.getInstance().getPost(mCtitle, position);
        if(p instanceof LocationPost) {
            String[] coords = ((LocationPost) p).getCoords();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, MapFragment.newInstance(coords[0],coords[1]))
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void getPostImageResponse(JSONObject response, Post post) {
        Post p = post;
        try {
            p.setContent(response.get("content").toString());
            model.updatePost(p.getCTitle(),p);
            AccordoDB.databaseWriteExecutor.execute(()-> db.postImageDao().insert(new PostImage(p.getPid(),p.getContent())));
            adapter.notifyItemChanged(model.getPostPosition(p.getCTitle(),p));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPostImageError(VolleyError error){
        Log.e(TAG, error.toString());
        //TODO handle error
    }
}
