package com.lucidleanlabs.dev.lcatalog.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {
    private SharedPreferences pref, pref1;
    private SharedPreferences.Editor editor, editor1;

    // Shared preferences file name
    private static final String PREF_NAME_1 = " L_Catalog_welcome_Screen ";
    private static final String PREF_NAME_2 = " L_Catalog_Screen_1";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_FIRST_TIME_LAUNCH_SCREEN1 = "IsFirstTimeLaunchScreen1";

    public PrefManager(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME_1, PRIVATE_MODE);
        editor = pref.edit();

        pref1 = context.getSharedPreferences(PREF_NAME_2, PRIVATE_MODE);
        editor1 = pref1.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunchScreen1(boolean isFirstTimeScreen1) {
        editor1.putBoolean(IS_FIRST_TIME_LAUNCH_SCREEN1, isFirstTimeScreen1);
        editor1.commit();
    }

    public boolean isFirstTimeLaunchScreen1() {
        return pref1.getBoolean(IS_FIRST_TIME_LAUNCH_SCREEN1, true);
    }

}
