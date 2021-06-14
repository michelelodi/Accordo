package com.accordo.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.accordo.MainActivity;

public class SharedPreferencesController {

    private static SharedPreferencesController instance = null;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    private final String APP_PREFS = "accordo_prefs";

    private SharedPreferencesController(Context context) {
        this.prefs = context.getSharedPreferences(APP_PREFS,0);
        this.editor = prefs.edit();
    }

    public static synchronized SharedPreferencesController getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesController(MainActivity.getAppContext());
        }
        return instance;
    }

    public int readIntFromSP(String key, int defaultValue) { return prefs.getInt(key, defaultValue); }

    public void writeIntToSP(String key, int value) {
        editor.putInt(key, value).apply();
    }

    public String readStringFromSP(String key, String defaultValue) { return prefs.getString(key, defaultValue); }


    public void writeStringToSP(String key, String value) {
        editor.putString(key, value).apply();
    }
}
