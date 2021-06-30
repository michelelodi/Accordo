package com.accordo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.accordo.controller.ConnectionController;
import com.accordo.controller.SharedPreferencesController;
import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

import static com.accordo.data.AccordoValues.*;

import static com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_SELECTED;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MYTAG_MainActivity";

    private int currentVersionCode;
    private ConnectionController cc;
    private SharedPreferencesController spc;
    private static BottomNavigationView myNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myNav = findViewById(R.id.bottomNavigationView);
        firstRunSetUp();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, WallFragment.class, new Bundle())
                .addToBackStack(null)
                .commit();
        setupNavbar();
    }

    private void firstRunSetUp() {
        spc = SharedPreferencesController.getInstance(this);
        cc = new ConnectionController(this);
        currentVersionCode = BuildConfig.VERSION_CODE;
        int savedVersionCode = spc.readIntFromSP(PREF_VERSION_CODE_KEY, Integer.parseInt(DOESNT_EXIST));
        if (savedVersionCode == Integer.parseInt(DOESNT_EXIST))
            cc.register(this::registrationResponse,
                    this::registrationError);
        spc.writeIntToSP(PREF_VERSION_CODE_KEY, currentVersionCode);
    }

    @SuppressLint("NonConstantResourceId")
    private void setupNavbar() {
        myNav.setLabelVisibilityMode(LABEL_VISIBILITY_SELECTED);
        myNav.setItemIconSize(100);
        myNav.setOnNavigationItemSelectedListener( item -> {
            switch(item.getItemId()) {
                case R.id.wallItem:
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_container_view, WallFragment.class, new Bundle())
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.addChannelItem:
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_container_view, AddChannelFragment.class, new Bundle())
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

    private void registrationResponse(JSONObject response) {
        try {
            spc.writeStringToSP(CURRENT_USER, response.get("sid").toString());
            cc.getProfile(response.get("sid").toString(), this::getProfileResponse,
                    error -> cc.handleVolleyError(error,this,TAG));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void registrationError(VolleyError error) {
        cc.handleVolleyError(error,this,TAG);
        currentVersionCode = Integer.parseInt(DOESNT_EXIST);
    }

    private void getProfileResponse(JSONObject response) {
        try {
            spc.writeStringToSP(UID, response.get("uid").toString());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, WallFragment.class, new Bundle())
                    .addToBackStack(null)
                    .commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void hideBottomNavigation(){
        myNav.setVisibility(View.GONE);
    }

    public static void showBottomNavigation(){
        myNav.setVisibility(View.VISIBLE);
    }
}