package com.example.accordo.controller;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesController {

    private final String APP_PREFS = "accordo_prefs";

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public SharedPreferencesController(Context context) {
        this.prefs = context.getSharedPreferences(APP_PREFS,0);
        this.editor = prefs.edit();
    }

    public int readIntFromSP(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    public void writeIntToSP(String key, int value) {
        editor.putInt(key, value).apply();
    }

    public void writeStringToSP(String key, String value) {
        editor.putString(key, value).apply();
    }
}
