package com.ayandev.meymey;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class App extends Application {
    public static final String MEYMEY_APP_PREFS_NAME = "MeyMeyAppPreferences";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static SharedPreferences.Editor getSharedPrefEditor(Context context) {
        return context.getSharedPreferences(MEYMEY_APP_PREFS_NAME, MODE_PRIVATE).edit();
    }

    public static SharedPreferences getSavedPrefs(Context context) {
        return context.getSharedPreferences(MEYMEY_APP_PREFS_NAME, MODE_PRIVATE);
    }
}
