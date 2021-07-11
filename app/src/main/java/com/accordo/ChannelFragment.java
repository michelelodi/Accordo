package com.accordo;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import static com.accordo.data.AccordoValues.CTITLE;
import static com.accordo.data.AccordoValues.CURRENT_USER;

public class ChannelFragment extends Fragment {

    private final String TAG = "MYTAG_ChannelFragment";

    private String mCtitle;
    private PostAdapter adapter;
    private ConnectionController cc;
    private SharedPreferencesController spc;
    private AppModel model;
    private AccordoDB db;
    private Looper secondaryThreadLooper;
    private FloatingActionButton addPost;

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
        cc = new ConnectionController(requireContext());
        spc = SharedPreferencesController.getInstance(requireContext());
        model = AppModel.getInstance();
        db = Room.databaseBuilder(requireContext(),
                AccordoDB.class, "accordo_database")
                .build();
        HandlerThread handlerThread = new HandlerThread("MyHandlerThreadChannel");
        handlerThread.start();
        secondaryThreadLooper = handlerThread.getLooper();
        if (getArguments() != null) mCtitle = getArguments().getString(CTITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(model.channelSize(mCtitle)>-1)
            return inflater.inflate(R.layout.fragment_channel, container, false);
        else
            return inflater.inflate(R.layout.fragment_empty_channel, container, false);
        }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.showBottomNavigation();
        addPost = view.findViewById(R.id.addPostButton);
        addPost.setOnClickListener( v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, AddPostFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        });
        if(model.channelSize(mCtitle)>-1) {
            RecyclerView rv = view.findViewById(R.id.channelRecyclerView);
            adapter = new PostAdapter(requireContext(), this::handleListClick, requireActivity(), mCtitle);
            rv.setLayoutManager(new LinearLayoutManager(requireContext()));
            rv.setAdapter(adapter);
            rv.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if(oldScrollY < scrollY && adapter.getAdapterPosition() >= adapter.getItemCount()-2) {
                    MainActivity.hideBottomNavigation();
                    addPost.hide();
                }
                else {
                    MainActivity.showBottomNavigation();
                    addPost.show();
                }
            });
            for (Post post : model.getChannelPosts(mCtitle))
                if (post instanceof ImagePost && post.getContent() == null)
                    cc.getPostImage(spc.readStringFromSP(CURRENT_USER, ""), post.getPid(),
                            response -> getPostImageResponse(response, post),
                            error -> cc.handleVolleyError(error, requireContext(), TAG));
        }
    }

    private void handleListClick(View v, int position){
        Post p = AppModel.getInstance().getPost(mCtitle, position);
        if(p instanceof LocationPost) {
            String[] coords = ((LocationPost) p).getCoords();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, MapFragment.newInstance(coords[0],coords[1]))
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void getPostImageResponse(JSONObject response, Post post) {
        try {
            post.setContent(response.get("content").toString());
            model.updatePost(post.getCTitle(),post);
            (new Handler(secondaryThreadLooper)).post(() -> AccordoDB.databaseWriteExecutor.execute(()-> db.postImageDao().insert(new PostImage(post.getPid(),post.getContent()))));
            adapter.notifyItemChanged(model.getPostPosition(post.getCTitle(),post));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
