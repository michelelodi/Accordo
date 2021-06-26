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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final String CURRENT_USER = "current_user";
    private final String PREF_VERSION_CODE_KEY = "version";
    private final int DOESNT_EXIST = -1;
    private final String TAG = "MYTAG_MainActivity";
    private final String UID = "uid";

    private int currentVersionCode;
    private ConnectionController cc;
    private static Context context;
    private SharedPreferencesController spc;
    private BottomNavigationView myNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        spc = SharedPreferencesController.getInstance();
        myNav = findViewById(R.id.bottomNavigationView);

        firstRunSetUp();

        cc.getWall("" + spc.readStringFromSP(CURRENT_USER,"" + DOESNT_EXIST),
                (Response.Listener<JSONObject>) this::getWallResponse,
                this::getWallError);

        setupNavbar();

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

            spc.writeStringToSP(CURRENT_USER, response.get("sid").toString());

            cc.getProfile(response.get("sid").toString(), this::getProfileResponse, this::getProfileError);

            cc.getWall(response.get("sid").toString(),
                    (Response.Listener<JSONObject>) this::getWallResponse,
                    this::getWallError);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void registrationError(VolleyError error) {
        Log.e(TAG, error.toString() + " in registration");
        currentVersionCode = DOESNT_EXIST;
    }

    private void getProfileResponse(JSONObject response) {
        try {
            spc.writeStringToSP(UID, response.get("uid").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getProfileError(VolleyError error) {
        //TODO handle error
        Log.e(TAG, error.toString());
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

    public static synchronized Context getAppContext() { return context; }

    private void setupNavbar() {
        myNav.setOnNavigationItemSelectedListener( item -> {
            switch(item.getItemId()) {
                case R.id.wallItem:
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_container_view, WallFragment.class, new Bundle())
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.profileItem:
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_container_view, ProfileFragment.class, new Bundle())
                            .addToBackStack(null)
                            .commit();
                    break;
                default:
                    break;
            }

            return true;
        });
    }
}