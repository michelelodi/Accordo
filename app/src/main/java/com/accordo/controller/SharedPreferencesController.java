package com.accordo.controller;

import android.content.Context;
import android.content.SharedPreferences;

import static com.accordo.data.AccordoValues.*;

public class SharedPreferencesController {

    private static SharedPreferencesController instance = null;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    private SharedPreferencesController(Context context) {
        this.prefs = context.getSharedPreferences(APP_PREFS,0);
        this.editor = prefs.edit();
    }

    public static synchronized SharedPreferencesController getInstance(Context context) {
        if (instance == null) instance = new SharedPreferencesController(context);
        return instance;
    }

    public int readIntFromSP(String key, int defaultValue) { return prefs.getInt(key, defaultValue); }

    public String readStringFromSP(String key, String defaultValue) { return prefs.getString(key, defaultValue); }

    public void writeIntToSP(String key, int value) {
        editor.putInt(key, value).apply();
    }

    public void writeStringToSP(String key, String value) {
        editor.putString(key, value).apply();
    }
}
