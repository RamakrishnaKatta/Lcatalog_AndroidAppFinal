package com.lucidleanlabs.dev.lcatalog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class MyScheduledReceiver extends BroadcastReceiver {

    public static final String TAG = "MyScheduledReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {

        Toast.makeText(context, "Your Guest Session will Expires in 2 minutes ", Toast.LENGTH_LONG).show();
        Log.v("MyScheduledReceiver", "Intent Fired");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run:here we go ");
                System.out.println("here we go");
                Intent broadcastIntent = new Intent(context, UserTypeActivity.class);
                broadcastIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                broadcastIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(broadcastIntent);

            }
        }, 2 * 60 * 1000);

    }
}