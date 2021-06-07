package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.accordo.controller.ConnectionController;
import com.example.accordo.data.CurrentUser;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView t;
    private int currentVersionCode;

    private final String CURRENT_USER = "current_user";
    private final String APP_PREFS = "accordo_prefs";
    private final String PREF_VERSION_CODE_KEY = "version";
    private final int DOESNT_EXIST = -1;
    private final String TAG = "MYTAG";
    private ConnectionController cc;
    private CurrentUser cU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs;
        prefs = getSharedPreferences(APP_PREFS,0);

        SharedPreferences.Editor editor;
        editor = prefs.edit();

        t = findViewById(R.id.tt);
        cc = new ConnectionController(this);

        checkFirstRun(editor);

        if(prefs.getString(CURRENT_USER,null) != null) t.setText(prefs.getString(CURRENT_USER,null));

    }

    private void checkFirstRun(SharedPreferences.Editor editor) {

        currentVersionCode = BuildConfig.VERSION_CODE;

        SharedPreferences prefs = getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        if (savedVersionCode == DOESNT_EXIST) {
            cc.register( response -> createUser(response, editor),
                    error -> notifyUser(error) );
        }

        editor.putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }

    private void createUser(JSONObject response, SharedPreferences.Editor editor) {
        try {
            editor.putString(CURRENT_USER, response.get("sid").toString()).apply();
            t.setText(response.get("sid").toString());
            // TODO: aggiungi al model new CurrentUser(response.get("uid").toString(), response.get("sid").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void notifyUser(VolleyError error) {
        Log.d(TAG, error.toString());
        currentVersionCode = DOESNT_EXIST;
        t.setText("An error occurred. Close and reopen the app.");
    }
}