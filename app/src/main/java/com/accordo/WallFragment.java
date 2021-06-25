package com.accordo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accordo.controller.ChannelAdapter;
import com.accordo.controller.ConnectionController;
import com.accordo.controller.SharedPreferencesController;
import com.accordo.data.AppModel;
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
    ConnectionController cc;
    SharedPreferencesController spc;
    private final String CURRENT_USER = "current_user";
    private final String TAG = "MYTAG_WallFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        model = AppModel.getInstance();
        cc = new ConnectionController(getContext());
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

        if(model.hasFullChannel(model.getChannel(position).getCTitle()))
            this.openChannelFragment(model.getChannel(position).getCTitle());
        else {
            cc.getChannel(spc.readStringFromSP(CURRENT_USER, ""), model.getChannel(position).getCTitle(),
                    (response) -> getChannelResponse(response, model.getChannel(position).getCTitle()),
                    this::getChannelError);
        }
    }

    private void getChannelResponse(JSONObject response, String cTitle){
        try {
            for (int i = 0; i < response.getJSONArray("posts").length(); i++) {
                JSONObject post = response.getJSONArray("posts").getJSONObject(i);
                model.addPost(post, cTitle);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"Channel has " + AppModel.getInstance().channelSize(cTitle) + " posts");
        this.openChannelFragment(cTitle);
    }

    private void getChannelError(VolleyError error){
        Log.e(TAG, error.toString());
        //TODO handle error
    }

    private void openChannelFragment(String cTitle){
        if(AppModel.getInstance().channelSize(cTitle) > -1) {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, ChannelFragment.newInstance(cTitle))
                    .addToBackStack(null)
                    .commit();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setMessage("Please try with a different channel")
                    .setTitle("Something went wrong");

            builder.setNegativeButton("Ok", (dialog, id) -> {});
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}