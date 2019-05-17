package online.lahloba.www.lahloba.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String APP_SETTINGS = "APP_SETTINGS";


    // properties
    private static final String CURRENT_LOCATION_LAT = "current_location_lat";
    private static final String CURRENT_LOCATION_LAN = "current_location_lan";
    private static final String CURRENT_LOCATION_ADDRESS = "current_location_address";
    // other properties...


    private SharedPreferencesManager() {}

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static String getCurrentLocationLat(Context context) {
        return getSharedPreferences(context).getString(CURRENT_LOCATION_LAT , null);
    }

    public static void setCurrentLocationLat(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(CURRENT_LOCATION_LAT , newValue);
        editor.commit();
    }


    public static String getCurrentLocationLan(Context context) {
        return getSharedPreferences(context).getString(CURRENT_LOCATION_LAN , null);
    }

    public static void setCurrentLocationLan(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(CURRENT_LOCATION_LAN , newValue);
        editor.commit();
    }


    public static String getCurrentLocationAddress(Context context) {
        return getSharedPreferences(context).getString(CURRENT_LOCATION_ADDRESS , null);
    }

    public static void setCurrentLocationAddress(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(CURRENT_LOCATION_ADDRESS , newValue);
        editor.commit();
    }

    // other getters/setters
}

