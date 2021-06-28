package com.accordo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.MediaStore;
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

public class ProfileFragment extends Fragment {

    private final String CURRENT_USER = "current_user";
    private final String SUCCESS = "Name successfully updated";
    private final String CONNECTION_FAIL = "An error occurred. Please check your Internet Connection";
    private final String DOESNT_EXIST = "-1";
    private final String UID = "uid";
    private final String TAG = "MYTAG_ProfileFragment";
    private final int RESULT_LOAD_IMAGE = 1;
    private final String CLIENT_ERROR = "com.android.volley.ClientError";
    private final String CLIENT_FAIL = "This name is already taken. Please try with a different one.";

    private SharedPreferencesController spc;
    private EditText profileName;
    private Button editName, editPicture;
    private ConnectionController cc;
    private ImageView profilePic;
    private AccordoDB db;
    private AppModel model;
    private Looper secondaryThreadLooper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        spc = SharedPreferencesController.getInstance();
        cc = new ConnectionController(getContext());
        model = AppModel.getInstance();
        db = Room.databaseBuilder(MainActivity.getAppContext(),
                AccordoDB.class, "accordo_database")
                .build();
        HandlerThread handlerThread = new HandlerThread("MyHandlerThreadProfile");
        handlerThread.start();
        secondaryThreadLooper = handlerThread.getLooper();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileName = view.findViewById(R.id.profileName);
        editName = view.findViewById(R.id.editName);
        editPicture = view.findViewById(R.id.editPicture);
        profilePic = view.findViewById(R.id.profilePicture);

        fillViewsWithProfileData();
        setUpOnClickListeners();
    }



    private boolean editProfileNameCheck(){
        String name = profileName.getText().toString();
        return name.length() > 0 && name.length() < 21 && !name.equals("Name") && !name.equals(spc.readStringFromSP(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), DOESNT_EXIST)) ;
    }

    private void fillViewsWithProfileData() {
        if(!spc.readStringFromSP(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), DOESNT_EXIST).equals("-1")) {
            profileName.setText(spc.readStringFromSP(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), DOESNT_EXIST));
        }

        if(model.getProfilePicture(spc.readStringFromSP(UID,null)) != null) {
            profilePic.setImageBitmap(model.getProfilePicture(spc.readStringFromSP(UID, null)));
        }
        else{
            (new Handler(secondaryThreadLooper)).post(() -> AccordoDB.databaseWriteExecutor.execute(()-> {
                if(db.profilePictureDao().getPicture(spc.readStringFromSP(UID,null)) != null) {
                    Bitmap bm = ImageUtils.base64ToBitmap(db.profilePictureDao().getPicture(spc.readStringFromSP(UID,null)));
                    getActivity().runOnUiThread(() -> profilePic.setImageBitmap(bm));
                    model.addProfilePicture(spc.readStringFromSP(UID,null),
                            bm,
                            db.profilePictureDao().getVersion(spc.readStringFromSP(UID,null)));
                }
                else getActivity().runOnUiThread(() -> profilePic.setImageResource(R.drawable.missing_profile));
            }));
        }
    }

    private void setProfileNameResponse(JSONObject response, String name) {
        profileName.setText(name);
        Toast.makeText(getContext(), SUCCESS, Toast.LENGTH_SHORT).show();
        spc.writeStringToSP(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST),name);
    }

    private void setProfileNameError(VolleyError error) {
        if(error.toString().equals(CLIENT_ERROR))
            Toast.makeText(getContext(), CLIENT_FAIL, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), CONNECTION_FAIL, Toast.LENGTH_SHORT).show();
        Log.e(TAG, error.toString());
    }

    private void setProfilePictureResponse(JSONObject response, Bitmap bm) {
        profilePic.setImageBitmap(bm);
        int pversion = model.hasProfilePic(spc.readStringFromSP(UID,DOESNT_EXIST)) ? model.getProfilePictureVersion(spc.readStringFromSP(UID,DOESNT_EXIST)) + 1 : 1;
        model.addProfilePicture(spc.readStringFromSP(UID, DOESNT_EXIST), bm, Integer.toString(pversion));
        (new Handler(secondaryThreadLooper)).post(() -> AccordoDB.databaseWriteExecutor.execute(()-> {
            ProfilePicture newPic = new ProfilePicture(spc.readStringFromSP(UID,DOESNT_EXIST),Integer.toString(pversion),ImageUtils.bitmapToBase64(bm));
            db.profilePictureDao().insertPicture(newPic);
        }));
    }

    private void setProfilePictureError(VolleyError error) {
        //TODO
        Log.e(TAG,error.toString());
    }

    private void setUpOnClickListeners(){
        editName.setOnClickListener( v -> {
            if(editProfileNameCheck())
                cc.setProfileName(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST), profileName.getText().toString(),
                        response -> setProfileNameResponse(response,profileName.getText().toString()), this::setProfileNameError);
        });

        editPicture.setOnClickListener( v -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                    RESULT_LOAD_IMAGE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                cc.setProfilePicture(spc.readStringFromSP(CURRENT_USER, DOESNT_EXIST),ImageUtils.bitmapToBase64(bm),
                        response -> setProfilePictureResponse(response, bm),
                        this::setProfilePictureError);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
