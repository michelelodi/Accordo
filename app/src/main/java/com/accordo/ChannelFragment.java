package com.accordo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accordo.controller.PostAdapter;
import com.accordo.controller.SharedPreferencesController;
import com.accordo.data.AppModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelFragment extends Fragment {

    private AppModel model;
    private SharedPreferencesController spc;
    private final String CTITLE = "lastChannel";
    private final String DOESNT_EXIST = "-1";
    private final String TAG = "MYTAG_ChannelFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        model =AppModel.getInstance();
        spc = SharedPreferencesController.getInstance();
        return inflater.inflate(R.layout.fragment_channel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.channelRecyclerView);
        PostAdapter adapter = new PostAdapter(getContext(), this::handleListClick, spc.readStringFromSP(CTITLE,DOESNT_EXIST));
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }

    private void handleListClick(View v, int position){

    }
}
