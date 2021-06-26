package com.accordo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.accordo.controller.ConnectionController;
import com.accordo.controller.SharedPreferencesController;
import com.accordo.data.AppModel;
import com.accordo.data.roomDB.AccordoDB;
import com.accordo.utils.ImageUtils;
import com.android.volley.VolleyError;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

public class ProfileFragment extends Fragment {

    private final String CURRENT_USER = "current_user";
    private final String SUCCESS = "Name successfully updated";
    private final String FAIL = "An error occurred. Please check your Internet Connection";
    private final String DOESNT_EXIST = "-1";
    private final String UID = "uid";
    private final String TAG = "MYTAG_ProfileFragment";

    private SharedPreferencesController spc;
    private EditText profileName;
    private Button editName;
    private ConnectionController cc;
    private ImageView profilePic;
    private AccordoDB db;
    private AppModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        spc = SharedPreferencesController.getInstance();
        cc = new ConnectionController(getContext());
        model = AppModel.getInstance();
        db = Room.databaseBuilder(MainActivity.getAppContext(),
                AccordoDB.class, "accordo_database")
                .build();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileName = view.findViewById(R.id.profileName);
        editName = view.findViewById(R.id.editName);
        profilePic = view.findViewById(R.id.profilePicture);

        profileDataSetUp();

        editName.setOnClickListener((v)->{
            if(editProfileNameCheck())
                cc.setProfile(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), profileName.getText().toString(),
                        response -> setProfileResponse(response,profileName.getText().toString()), this::setProfileError);
        });
    }

    private void profileDataSetUp() {
        if(!spc.readStringFromSP(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), DOESNT_EXIST).equals("-1")) {
            profileName.setText(spc.readStringFromSP(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), DOESNT_EXIST));
        }

        if(model.getProfilePicture(spc.readStringFromSP(UID,null)) != null)
            profilePic.setImageBitmap(model.getProfilePicture(spc.readStringFromSP(UID,null)));
        else{
            AccordoDB.databaseWriteExecutor.execute(()-> {
                if(db.profilePictureDao().getPicture(spc.readStringFromSP(UID,null)) != null) {
                    getActivity().runOnUiThread(() -> profilePic.setImageBitmap(ImageUtils.base64ToBitmap(db.profilePictureDao().getPicture(spc.readStringFromSP(UID,null)))));
                    model.addProfilePicture(spc.readStringFromSP(UID,null),
                            ImageUtils.base64ToBitmap(db.profilePictureDao().getPicture(spc.readStringFromSP(UID,null))),
                            db.profilePictureDao().getPicture(spc.readStringFromSP(UID,null)));
                }
                else getActivity().runOnUiThread(() -> profilePic.setImageResource(R.drawable.missing_profile));
            });
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