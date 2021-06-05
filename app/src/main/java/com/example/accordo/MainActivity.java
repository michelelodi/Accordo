package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private final String CURRENT_USER = "current_user";
    private final String VERSION = "version_check";
    private final String PREF_VERSION_CODE_KEY = "1.0";
    private final int DOESNT_EXIST = -1;
    private final String MYTAG = "MYTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(VERSION,0);
        editor = prefs.edit();

        checkFirstRun();


    }

    private void checkFirstRun() {



        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(VERSION, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            //normal run
            TextView t = findViewById(R.id.tt);
            t.setText(prefs.getString(CURRENT_USER,"pippo"));

        } else if (savedVersionCode == DOESNT_EXIST) {
            // first run
            editor.putString(CURRENT_USER, "michele");

        } else if (currentVersionCode > savedVersionCode) {
            Log.d(MYTAG,"Upgraded run");
            // TODO This is an upgrade
        }

        // Update the shared preferences with the current version code
        editor.putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
        return;
    }
}