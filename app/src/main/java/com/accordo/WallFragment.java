package com.accordo;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accordo.controller.ChannelAdapter;
import com.accordo.controller.ConnectionController;
import com.accordo.controller.SharedPreferencesController;
import com.accordo.data.AppModel;
import com.accordo.data.Channel;
import com.accordo.data.Post;
import com.accordo.data.roomDB.AccordoDB;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import static com.accordo.data.AccordoValues.CURRENT_USER;
import static com.accordo.data.AccordoValues.DOESNT_EXIST;

public class WallFragment extends Fragment {

    private final String TAG = "MYTAG_WallFragment";

    private AppModel model;
    private ConnectionController cc;
    private SharedPreferencesController spc;
    private AccordoDB db;
    private Looper secondaryThreadLooper;
    ChannelAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        model = AppModel.getInstance();
        cc = new ConnectionController(requireContext());
        spc = SharedPreferencesController.getInstance(requireContext());
        db = Room.databaseBuilder(requireContext(),
                AccordoDB.class, "accordo_database")
                .build();
        HandlerThread handlerThread = new HandlerThread("MyHandlerThreadWall");
        handlerThread.start();
        secondaryThreadLooper = handlerThread.getLooper();
        return inflater.inflate(R.layout.fragment_wall, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.wallRecyclerView);
        adapter = new ChannelAdapter(requireContext(), this::handleListClick);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);
        rv.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if(oldScrollY < scrollY) MainActivity.hideBottomNavigation();
            else MainActivity.showBottomNavigation();
        });
        cc.getWall(spc.readStringFromSP(CURRENT_USER,"" + DOESNT_EXIST),
                this::getWallResponse,
                error -> cc.handleVolleyError(error, requireContext(), TAG));
    }

    private void handleListClick(View v, int position) {
        if(model.hasFullChannel(model.getChannel(position).getCTitle()))
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, ChannelFragment.newInstance(model.getChannel(position).getCTitle()))
                    .addToBackStack(null)
                    .commit();
        else {
            cc.getChannel(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), model.getChannel(position).getCTitle(),
                    (response) -> getChannelResponse(response, model.getChannel(position).getCTitle()),
                    error -> cc.handleVolleyError(error, requireContext(), TAG));
        }
    }

    private void getWallResponse(JSONObject response) {
        model.emptyWall();
        try {
            for (int i = 0; i < response.getJSONArray("channels").length(); i++) {
                model.addChannel((new Channel(response.getJSONArray("channels").getJSONObject(i).get("ctitle").toString(),
                        response.getJSONArray("channels").getJSONObject(i).get("mine").toString().equals("t"))));
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) { e.printStackTrace(); }
    }

    private void getChannelResponse(JSONObject response, String cTitle){
        try {
            for (int i = 0; i < response.getJSONArray("posts").length(); i++) {
                JSONObject post = response.getJSONArray("posts").getJSONObject(i);
                model.addPost(post, cTitle);
                if(post.get("type").toString().equals("i")) {
                    (new Handler(secondaryThreadLooper)).post(() -> AccordoDB.databaseWriteExecutor.execute(() -> {
                        Post p = null;
                        try {
                            p = model.getPost(cTitle, post.get("pid").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(p != null) p.setContent(db.postImageDao().get(p.getPid()));
                        model.updatePost(cTitle, p);
                    }));
                }
            }
        } catch (JSONException e) { e.printStackTrace(); }
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, ChannelFragment.newInstance(cTitle))
                .addToBackStack(null)
                .commit();
    }
}