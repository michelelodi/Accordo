package com.accordo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.accordo.controller.ConnectionController;
import com.accordo.controller.SharedPreferencesController;
import com.android.volley.VolleyError;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private final String CURRENT_USER = "current_user";
    private final String SUCCESS = "Name successfully updated";
    private final String FAIL = "An error occurred. Please check your Internet Connection";
    private final String DOESNT_EXIST = "-1";
    private final String TAG = "MYTAG_ProfileFragment";

    private SharedPreferencesController spc;
    private EditText profileName;
    private Button editName;
    private ConnectionController cc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        spc = SharedPreferencesController.getInstance();
        cc = new ConnectionController(getContext());
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileName = view.findViewById(R.id.profileName);
        editName = view.findViewById(R.id.editName);

        profileNameSetUp();

        editName.setOnClickListener((v)->{
            if(editProfileNameCheck())
                cc.setProfile(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), profileName.getText().toString(),
                        response -> setProfileResponse(response,profileName.getText().toString()), this::setProfileError);
        });
    }

    private void profileNameSetUp() {
        if(!spc.readStringFromSP(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), DOESNT_EXIST).equals("-1")) {
            profileName.setText(spc.readStringFromSP(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), DOESNT_EXIST));
        }
    }

    private boolean editProfileNameCheck(){
        String name = profileName.getText().toString();
        return name.length() > 0 && name.length() < 21 && !name.equals("Name") && !name.equals(spc.readStringFromSP(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), DOESNT_EXIST));
    }

    private void setProfileResponse(JSONObject response, String name) {
        profileName.setText(name);
        Toast.makeText(getContext(), SUCCESS, Toast.LENGTH_SHORT).show();
        spc.writeStringToSP(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST),name);
    }

    private void setProfileError(VolleyError error) {
        Toast.makeText(getContext(), FAIL, Toast.LENGTH_SHORT).show();
        Log.e(TAG, error.toString());
    }
}