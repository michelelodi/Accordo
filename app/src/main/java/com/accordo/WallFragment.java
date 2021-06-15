package com.accordo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.accordo.controller.ChannelAdapter;
import com.accordo.controller.ConnectionController;
import com.accordo.controller.SharedPreferencesController;
import com.accordo.data.AppModel;
import com.accordo.data.Channel;
import com.accordo.data.ImagePost;
import com.accordo.data.LocationPost;
import com.accordo.data.Post;
import com.accordo.data.TextPost;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WallFragment extends Fragment {

    AppModel model;
    SharedPreferencesController spc;
    private final String CURRENT_USER = "current_user";
    private final String TAG = "MYTAG_WallFragment";
    private final String CTITLE = "lastChannel";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        model = AppModel.getInstance();
        spc = SharedPreferencesController.getInstance();
        return inflater.inflate(R.layout.fragment_wall, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.recyclerView);
        ChannelAdapter adapter = new ChannelAdapter(getContext(), (v, position) -> {

            //handle listClick
            spc.writeStringToSP(CTITLE, model.getChannel(position).getCTitle());

            ConnectionController cc = new ConnectionController(getContext());
            if(model.hasChannel(model.getChannel(position).getCTitle())) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, new ChannelFragment())
                        .addToBackStack(null)
                        .commit();
            } else {
                cc.getChannel(spc.readStringFromSP(CURRENT_USER, ""), model.getChannel(position).getCTitle(),
                        (response) -> getChannelResponse(response, model.getChannel(position).getCTitle()),
                        this::getChannelError);
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }

    private void getChannelResponse(JSONObject response, String cTitle){
        Log.d(TAG,"response");
        try {

            for (int i = 0; i < response.getJSONArray("posts").length(); i++) {

                JSONObject post = response.getJSONArray("posts").getJSONObject(i);
                String type = post.get("type").toString();
                switch (type) {
                    case "t": {
                        Post p = new TextPost(post.get("pid").toString(), post.get("uid").toString());
                        p.setContent(post.get("content").toString());
                        model.addPost(p, cTitle);
                        break;
                    }
                    case "i": {
                        Post p = new ImagePost(post.get("pid").toString(), post.get("uid").toString());
                        p.setContent(post.get("content").toString());
                        model.addPost(p, cTitle);
                        break;
                    }
                    case "l": {
                        Post p = new LocationPost(post.get("pid").toString(), post.get("uid").toString());
                        p.setContent(post.get("lat").toString() + "," + post.get("lon").toString());
                        model.addPost(p, cTitle);
                        break;
                    }
                    default:
                        break;
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(AppModel.getInstance().channelSize(cTitle) > -1) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, new ChannelFragment())
                    .addToBackStack(null)
                    .commit();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Please try with a different channel")
                    .setTitle("Something went wrong");

            builder.setNegativeButton("Ok", (dialog, id) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void getChannelError(VolleyError error){
        Log.e(TAG, error.toString());
        //TODO handle error
    }
}