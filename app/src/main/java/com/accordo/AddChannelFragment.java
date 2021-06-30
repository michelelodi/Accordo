package com.accordo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.accordo.controller.ConnectionController;
import com.accordo.controller.SharedPreferencesController;
import com.accordo.data.AppModel;
import com.accordo.data.Channel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.accordo.data.AccordoValues.*;

public class AddChannelFragment extends Fragment {

    private final String TAG = "MYTAG_AddChannelFragment";

    private EditText cTitle;
    private ConnectionController cc;
    private SharedPreferencesController spc;
    private String cTitleString = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cc = new ConnectionController(getContext());
        spc = SharedPreferencesController.getInstance(getContext());
        return inflater.inflate(R.layout.fragment_add_channel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.showBottomNavigation();
        cTitle = view.findViewById(R.id.newCTitle);
        Button addChannel = view.findViewById(R.id.addChannel);
        addChannel.setOnClickListener(v -> {
            if(cTitleCheck()){
                cTitleString = cTitle.getText().toString();
                cc.addChannel(spc.readStringFromSP(CURRENT_USER,DOESNT_EXIST), cTitleString,
                        this::addChannelResponse,
                        error -> cc.handleVolleyError(error,getContext(),TAG));
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

    private boolean cTitleCheck() { return cTitle.getText().toString().length() > 0 && cTitle.getText().toString().length() < 21; }
}