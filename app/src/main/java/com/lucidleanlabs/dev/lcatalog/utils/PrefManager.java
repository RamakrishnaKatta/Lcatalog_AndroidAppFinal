package com.lucidleanlabs.dev.lcatalog.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {
    private SharedPreferences pref, pref1,pref2,pref3,pref4;
    private SharedPreferences.Editor editor, editor1,editor2,editor3,editor4;

    // Shared preferences file name
    private static final String PREF_NAME_1 = " L_Catalog_welcome_Screen ";
    private static final String PREF_NAME_2 = " L_Catalog_Screen_1";
    private static final String PREF_NAME_3 = "L_Catalog_Screen_2";
    private static final String PREF_NAME_4 = "L_Catalog_Screen_3";
    private static final String PREF_NAME_5 = "L_Catalog_Screen_4";


    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_FIRST_TIME_LAUNCH_SCREEN1 = "IsFirstTimeLaunchScreen1";
    private static final String IS_FIRST_TIME_LAUNCH_SCREEN2 = "IsFirstTimeLaunchScreen2";
    private static final String IS_FIRST_TIME_LAUNCH_SCREEN3 = "IsFirstTimeLaunchScreen3";
    private static final String IS_FIRST_TIME_LAUNCH_SCREEN4 = "IsFirstTimeLaunchScreen4";


    public PrefManager(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME_1, PRIVATE_MODE);
        editor = pref.edit();

        pref1 = context.getSharedPreferences(PREF_NAME_2, PRIVATE_MODE);
        editor1 = pref1.edit();

        pref2 = context.getSharedPreferences(PREF_NAME_3,PRIVATE_MODE);
        editor2 = pref2.edit();
        pref3 = context.getSharedPreferences(PREF_NAME_4,PRIVATE_MODE);
        editor3 = pref3.edit();
        pref4 = context.getSharedPreferences(PREF_NAME_5,PRIVATE_MODE);
        editor4 = pref4.edit();
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
    public void setFirstTimeLaunchScreen2(boolean isFirstTimeScreen2) {
        editor2.putBoolean(IS_FIRST_TIME_LAUNCH_SCREEN2, isFirstTimeScreen2);
        editor2.commit();
    }

    public boolean isFirstTimeLaunchScreen2() {
        return pref2.getBoolean(IS_FIRST_TIME_LAUNCH_SCREEN2, true);
    }


    public void setFirstTimeLaunchScreen3(boolean isFirstTimeScreen3) {
        editor3.putBoolean(IS_FIRST_TIME_LAUNCH_SCREEN3, isFirstTimeScreen3);
        editor3.commit();
    }

    public boolean isFirstTimeLaunchScreen3() {
        return pref3.getBoolean(IS_FIRST_TIME_LAUNCH_SCREEN3, true);
    }
    public void setFirstTimeLaunchScreen4(boolean isFirstTimeScreen4) {
        editor4.putBoolean(IS_FIRST_TIME_LAUNCH_SCREEN4, isFirstTimeScreen4);
        editor4.commit();
    }

    public boolean isFirstTimeLaunchScreen4() {
        return pref4.getBoolean(IS_FIRST_TIME_LAUNCH_SCREEN4, true);
    }

}
