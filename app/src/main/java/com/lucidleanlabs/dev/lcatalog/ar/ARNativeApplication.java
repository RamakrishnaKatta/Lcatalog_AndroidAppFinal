package com.lucidleanlabs.dev.lcatalog.ar;

import android.app.Application;

import org.artoolkit.ar.base.assets.AssetHelper;


public class ARNativeApplication extends Application {

    private static Application sInstance;

    // Anywhere in the application where an instance is required, this method
    // can be used to retrieve it.
    public static Application getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
//        ((ARNativeApplication) sInstance).initializeInstance();
    }


}
