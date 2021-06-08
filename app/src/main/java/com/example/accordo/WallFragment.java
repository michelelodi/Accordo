package com.example.accordo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.accordo.controller.SharedPreferencesController;

public class WallFragment extends Fragment {

    private SharedPreferencesController spc;
    private final String CURRENT_USER = "current_user";
    private final String DOESNT_EXIST = "-1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wall, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spc = new SharedPreferencesController(getContext());
        TextView tv = getActivity().findViewById(R.id.tv);
        tv.setText(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST));
    }
}