package com.example.parkingfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "ParkingRatePrefs";
    private static final String KEY_PARKING_RATE = "parking_rate";

    private static SharedPreferencesManager instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context);
        }
        return instance;
    }

    public void setParkingRate(double rate) {
        editor.putFloat(KEY_PARKING_RATE, (float) rate);
        editor.apply();
        Log.w("Rate", String.valueOf(rate));
    }

    public double getParkingRate() {
        return sharedPreferences.getFloat(KEY_PARKING_RATE, 0f); // Default value is 0
    }
}
