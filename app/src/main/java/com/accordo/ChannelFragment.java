package com.accordo;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.accordo.controller.SharedPreferencesController;
import com.accordo.data.AppModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChannelFragment extends Fragment {

    private AppModel model;
    private TextView tv;
    private SharedPreferencesController spc;
    private final String CTITLE = "selected_ctitle";
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
        tv = view.findViewById(R.id.textView);
        String t = Integer.toString(AppModel.getInstance().channelSize(spc.readStringFromSP(CTITLE,DOESNT_EXIST)));
        Log.d(TAG, t);
        tv.setText(t);
    }
}
