package com.lucidleanlabs.dev.lcatalog;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class Notify extends FirebaseInstanceIdService {

    private static final String TAG = "Notify";
    String refreshedToken;

    public void onTokenRefresh() {
        // Get updated InstanceID token.

        try {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "Refreshed token: " + refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {

    }
}
