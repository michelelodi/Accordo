package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.accordo.controller.ConnectionController;
import com.example.accordo.controller.SharedPreferencesController;
import com.example.accordo.data.CurrentUser;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView t;
    private int currentVersionCode;

    private final String CURRENT_USER = "current_user";
    private final String PREF_VERSION_CODE_KEY = "version";
    private final int DOESNT_EXIST = -1;
    private final String TAG = "MYTAG";
    private ConnectionController cc;
    private SharedPreferencesController spc;
    private CurrentUser cU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spc = new SharedPreferencesController(this);
        cc = new ConnectionController(this);

        checkFirstRun();

        Bundle bundle = new Bundle();
        bundle.putInt("some_int", 0);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, WallFragment.class, bundle)
                .commit();

        //if(prefs.getString(CURRENT_USER,null) != null) t.setText(prefs.getString(CURRENT_USER,null));

    }

    private void checkFirstRun() {

        currentVersionCode = BuildConfig.VERSION_CODE;

        int savedVersionCode = spc.readIntFromSP(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        if (savedVersionCode == DOESNT_EXIST) {
            cc.register( response -> createUser(response),
                    error -> notifyUser(error) );
        }

        spc.writeIntToSP(PREF_VERSION_CODE_KEY, currentVersionCode);
    }

    private void createUser(JSONObject response) {
        try {
            spc.writeStringToSP(CURRENT_USER, response.get("sid").toString());
            // TODO: aggiungi al model new CurrentUser(response.get("uid").toString(), response.get("sid").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void notifyUser(VolleyError error) {
        Log.d(TAG, error.toString());
        currentVersionCode = DOESNT_EXIST;
    }
}