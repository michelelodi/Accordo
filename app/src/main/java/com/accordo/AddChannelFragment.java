package com.accordo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.accordo.controller.ConnectionController;
import com.accordo.controller.SharedPreferencesController;
import com.accordo.data.AppModel;
import com.accordo.data.Channel;
import com.android.volley.VolleyError;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddChannelFragment extends Fragment {

    private final String CURRENT_USER = "current_user";
    private final String DOESNT_EXIST = "-1";
    private final String TAG = "MYTAG_AddChannelFragment";

    private EditText cTitle;
    private Button addChannel;
    private ConnectionController cc;
    private SharedPreferencesController spc;
    private String cTitleString = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cc = new ConnectionController(getContext());
        spc = SharedPreferencesController.getInstance();
        return inflater.inflate(R.layout.fragment_add_channel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cTitle = view.findViewById(R.id.newCTitle);
        addChannel = view.findViewById(R.id.addChannel);

        addChannel.setOnClickListener(v -> {
            if(cTitleCheck()){
                cTitleString = cTitle.getText().toString();
                cc.addChannel(spc.readStringFromSP(CURRENT_USER,DOESNT_EXIST), cTitle.getText().toString(),
                        this::addChannelResponse, this::addChannelError);
            }
        });
    }

    private void addChannelResponse(JSONObject response) {
        AppModel.getInstance().addChannel(new Channel(cTitleString, true));
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, WallFragment.class, new Bundle())
                .addToBackStack(null)
                .commit();
    }

    private void addChannelError(VolleyError error) {
        //TODO handle error
        Log.e(TAG,error.toString());
    }

    private boolean cTitleCheck() { return cTitle.getText().toString().length() > 0 && cTitle.getText().toString().length() < 21; }
}