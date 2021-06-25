package com.accordo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.accordo.controller.ConnectionController;
import com.accordo.controller.SharedPreferencesController;
import com.accordo.data.AppModel;
import com.accordo.data.Channel;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int currentVersionCode;

    private final String CURRENT_USER = "current_user";
    private final String ERROR_TITLE = "Something went wrong";
    private final String PREF_VERSION_CODE_KEY = "version";
    private final String ERROR_NEGATIVE_BUTTON_TEXT = "OK";
    private final int DOESNT_EXIST = -1;
    private final String TAG = "MYTAG_MainActivity";
    private ConnectionController cc;
    private String myUid;
    private static Context context;
    private SharedPreferencesController spc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.context = getApplicationContext();
        spc = SharedPreferencesController.getInstance();

        firstRunSetUp();

        cc.getWall("" + spc.readStringFromSP(CURRENT_USER,"" + DOESNT_EXIST),
                (Response.Listener<JSONObject>) this::getWallResponse,
                this::getWallError);

    }

    private void firstRunSetUp() {

        cc = new ConnectionController(this);
        currentVersionCode = BuildConfig.VERSION_CODE;

        int savedVersionCode = spc.readIntFromSP(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        if (savedVersionCode == DOESNT_EXIST) {
            cc.register(this::registrationResponse,
                    this::registrationError);
        }

        spc.writeIntToSP(PREF_VERSION_CODE_KEY, currentVersionCode);

    }

    private void registrationResponse(JSONObject response) {
        try {

            spc.writeStringToSP(CURRENT_USER, "" + response.get("sid"));

            cc.getWall("" + response.get("sid"),
                    (Response.Listener<JSONObject>) this::getWallResponse,
                    this::getWallError);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getWallResponse(JSONObject response){
        try {
            for (int i = 0; i < response.getJSONArray("channels").length(); i++) {
                AppModel.getInstance().addChannel((new Channel(response.getJSONArray("channels").getJSONObject(i).get("ctitle").toString(),
                        response.getJSONArray("channels").getJSONObject(i).get("mine").toString().equals("t"))));
            }

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, WallFragment.class, new Bundle())
                    .commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getWallError(VolleyError error){
        Log.e(TAG, error.toString() + " in getWall");
    }

    private void registrationError(VolleyError error) {
        Log.e(TAG, error.toString() + " in registration");
        currentVersionCode = DOESNT_EXIST;
    }

    public static synchronized Context getAppContext() { return context; }
}