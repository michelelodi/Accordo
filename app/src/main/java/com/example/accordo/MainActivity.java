package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private TextView t;

    private final String CURRENT_USER = "current_user";
    private final String APP_PREFS = "accordo_prefs";
    private final String PREF_VERSION_CODE_KEY = "version";
    private final int DOESNT_EXIST = -1;
    private final String TAG = "MYTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(APP_PREFS,0);
        editor = prefs.edit();
        t = findViewById(R.id.tt);

        checkFirstRun();

        t.setText(prefs.getString(CURRENT_USER,"pippo"));

    }

    private void checkFirstRun() {

        int currentVersionCode = BuildConfig.VERSION_CODE;

        SharedPreferences prefs = getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        if (savedVersionCode == DOESNT_EXIST) {

            //first run
            RequestQueue rq = Volley.newRequestQueue(getApplicationContext());

            final JSONObject jsonStudent = new JSONObject();
            final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/register.php";

            JsonObjectRequest listRequest = new JsonObjectRequest(
                    url, jsonStudent,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                editor.putString(CURRENT_USER, response.get("sid").toString()).apply();
                                t.setText(response.get("sid").toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override

                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "REQUEST FAILED");
                }
            }
            );
            rq.add(listRequest);

        } else if (currentVersionCode > savedVersionCode) {
            // TODO This is an upgrade
        }

        editor.putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
        return;
    }
}