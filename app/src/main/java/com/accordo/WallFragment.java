package com.accordo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accordo.controller.ChannelAdapter;
import com.accordo.controller.ConnectionController;
import com.accordo.controller.SharedPreferencesController;
import com.accordo.data.AppModel;
import com.accordo.data.ImagePost;
import com.accordo.data.LocationPost;
import com.accordo.data.Post;
import com.accordo.data.TextPost;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

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

        RecyclerView rv = view.findViewById(R.id.wallRecyclerView);
        ChannelAdapter adapter = new ChannelAdapter(getContext(), this::handleListClick);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }

    private void handleListClick(View v, int position) {

        ConnectionController cc = new ConnectionController(getContext());

        if(model.hasChannel(model.getChannel(position).getCTitle()))
            openChannelFragment(model.getChannel(position).getCTitle());
         else
            cc.getChannel(spc.readStringFromSP(CURRENT_USER, ""), model.getChannel(position).getCTitle(),
                    (response) -> getChannelResponse(response, model.getChannel(position).getCTitle()),
                    this::getChannelError);
    }

    private void getChannelResponse(JSONObject response, String cTitle){

        try {
            for (int i = 0; i < response.getJSONArray("posts").length(); i++) {
                JSONObject post = response.getJSONArray("posts").getJSONObject(i);
                makePostFromResponseAndUpdateModel(post,cTitle);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"Channel has " + AppModel.getInstance().channelSize(cTitle) + " posts");
        openChannelFragment(cTitle);
    }

    private void getChannelError(VolleyError error){
        Log.e(TAG, error.toString());
        //TODO handle error
    }

    private void makePostFromResponseAndUpdateModel(JSONObject post, String cTitle){
        Post p;
        try {
            switch (post.get("type").toString()) {
                case "t": {
                    p = new TextPost(post.get("pid").toString(), post.get("uid").toString(), cTitle);
                    p.setContent(post.get("content").toString());
                    model.addPost(p, cTitle);
                    break;
                }
                case "i": {
                    p = new ImagePost(post.get("pid").toString(), post.get("uid").toString(), cTitle);
                    p.setContent(post.get("content").toString());
                    model.addPost(p, cTitle);
                    break;
                }
                case "l": {
                    p = new LocationPost(post.get("pid").toString(), post.get("uid").toString(), cTitle);
                    p.setContent(post.get("lat").toString() + "," + post.get("lon").toString());
                    model.addPost(p, cTitle);
                    break;
                }
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openChannelFragment(String cTitle){
        if(AppModel.getInstance().channelSize(cTitle) > -1) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, ChannelFragment.newInstance(cTitle))
                    .addToBackStack(null)
                    .commit();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Please try with a different channel")
                    .setTitle("Something went wrong");

            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}