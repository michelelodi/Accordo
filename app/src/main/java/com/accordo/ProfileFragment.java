package com.accordo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.accordo.data.roomDB.ProfilePicture;
import com.accordo.utils.ImageUtils;
import com.android.volley.VolleyError;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import static android.app.Activity.RESULT_OK;
import static com.accordo.data.AccordoValues.*;

public class ProfileFragment extends Fragment {

    private final String TAG = "MYTAG_ProfileFragment";
    private final int RESULT_LOAD_IMAGE = 1;

    private SharedPreferencesController spc;
    private EditText profileName;
    private Button editName, editPicture;
    private ConnectionController cc;
    private ImageView profilePic;
    private AccordoDB db;
    private AppModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        spc = SharedPreferencesController.getInstance(requireContext());
        cc = new ConnectionController(requireContext());
        model = AppModel.getInstance();
        db = Room.databaseBuilder(requireContext(), AccordoDB.class, "accordo_database").build();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.showBottomNavigation();
        profileName = view.findViewById(R.id.profileName);
        editName = view.findViewById(R.id.editName);
        editPicture = view.findViewById(R.id.editPicture);
        profilePic = view.findViewById(R.id.profilePicture);
        fillViewsWithProfileData();
        setUpOnClickListeners();
    }

    private void fillViewsWithProfileData() {
        if(!spc.readStringFromSP(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), DOESNT_EXIST).equals("-1"))
            profileName.setText(spc.readStringFromSP(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), DOESNT_EXIST));
        if(model.getProfilePicture(spc.readStringFromSP(UID,null)) != null)
            profilePic.setImageBitmap(model.getProfilePicture(spc.readStringFromSP(UID, null)));
        else
            AccordoDB.databaseWriteExecutor.execute(()-> {
                if(db.profilePictureDao().getPicture(spc.readStringFromSP(UID,null)) != null) {
                    Bitmap bm = ImageUtils.base64ToBitmap(db.profilePictureDao().getPicture(spc.readStringFromSP(UID,null)));
                    requireActivity().runOnUiThread(() -> profilePic.setImageBitmap(bm));
                    model.addProfilePicture(spc.readStringFromSP(UID,null), ImageUtils.base64ToBitmap(db.profilePictureDao().getPicture(spc.readStringFromSP(UID,null))),
                            db.profilePictureDao().getVersion(spc.readStringFromSP(UID,null)));
                } else requireActivity().runOnUiThread(() -> profilePic.setImageResource(R.drawable.missing_profile));
            });
    }

    private void setUpOnClickListeners(){
        editName.setOnClickListener( v -> {
            if(editProfileNameCheck())
                cc.setProfileName(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), profileName.getText().toString(),
                        response -> setProfileNameResponse(response,profileName.getText().toString()), this::setProfileNameError);
        });
        editPicture.setOnClickListener( v -> startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), RESULT_LOAD_IMAGE));
    }

    private boolean editProfileNameCheck(){
        String name = profileName.getText().toString();
        return name.length() > 0 && name.length() < 21 && !name.equals("Name") && !name.equals(spc.readStringFromSP(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), DOESNT_EXIST)) ;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), data.getData());
                cc.setProfilePicture(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST),ImageUtils.bitmapToBase64(bm),
                        response -> setProfilePictureResponse(response, bm),
                        error -> cc.handleVolleyError(error,requireContext(),TAG));
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private void setProfileNameResponse(JSONObject response, String name) {
        profileName.setText(name);
        Toast.makeText(requireContext(), getString(R.string.name_updated), Toast.LENGTH_SHORT).show();
        spc.writeStringToSP(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST),name);
    }

    private void setProfileNameError(VolleyError error) {
        cc.handleVolleyError(error, requireContext(), TAG);
        if(error.toString().equals(getString(R.string.client_error)))
            Toast.makeText(requireContext(), getString(R.string.client_fail), Toast.LENGTH_SHORT).show();
    }

    private void setProfilePictureResponse(JSONObject response, Bitmap bm) {
        profilePic.setImageBitmap(bm);
        int pversion = model.hasProfilePic(spc.readStringFromSP(UID, DOESNT_EXIST)) ? model.getProfilePictureVersion(spc.readStringFromSP(UID, DOESNT_EXIST)) + 1 : 1;
        model.addProfilePicture(spc.readStringFromSP(UID, DOESNT_EXIST), bm, Integer.toString(pversion));
        AccordoDB.databaseWriteExecutor.execute(()-> {
            ProfilePicture newPic = new ProfilePicture(spc.readStringFromSP(UID, DOESNT_EXIST),Integer.toString(pversion),ImageUtils.bitmapToBase64(bm));
            db.profilePictureDao().insertPicture(newPic);
        });
    }
}
