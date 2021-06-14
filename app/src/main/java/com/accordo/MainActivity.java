package com.accordo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.accordo.controller.ConnectionController;
import com.accordo.controller.SharedPreferencesController;
import com.accordo.data.AppModel;
import com.accordo.data.CurrentUser;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int currentVersionCode;

    private final String CURRENT_USER = "current_user";
    private final String PREF_VERSION_CODE_KEY = "version";
    private final int DOESNT_EXIST = -1;
    private final String TAG = "MYTAG";
    private ConnectionController cc;
    private String myUid;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = getApplicationContext();

        checkFirstRun();

    }

    private void checkFirstRun() {

        cc = new ConnectionController(this);
        currentVersionCode = BuildConfig.VERSION_CODE;

        int savedVersionCode = SharedPreferencesController.getInstance().readIntFromSP(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        if (savedVersionCode == DOESNT_EXIST) {
            cc.register(this::createUser,
                    this::notifyUser);
        }

        SharedPreferencesController.getInstance().writeIntToSP(PREF_VERSION_CODE_KEY, currentVersionCode);
    }

    private void createUser(JSONObject response) {
        try {
            String mySid = response.get("sid").toString();
            SharedPreferencesController.getInstance().writeStringToSP(CURRENT_USER, mySid);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, WallFragment.class, new Bundle())
                    .commit();
            cc.getProfile(mySid, this::handleFromResponse, this::handleError);

            //TODO: in realtà andrà salvato su room
            AppModel.getInstance()
                    .addUser(new CurrentUser(myUid,
                            response.get("sid").toString()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void notifyUser(VolleyError error) {
        Log.e(TAG, error.toString());
        currentVersionCode = DOESNT_EXIST;
    }

    private void handleFromResponse(JSONObject response){
        try {
            myUid = response.get("uid").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleError(VolleyError error){
        Log.e(TAG,error.toString());
    }

    public synchronized Context getAppContext() { return context; }
}