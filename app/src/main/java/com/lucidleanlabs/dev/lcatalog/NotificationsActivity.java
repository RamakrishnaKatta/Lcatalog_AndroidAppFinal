package com.lucidleanlabs.dev.lcatalog;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lucidleanlabs.dev.lcatalog.adapters.NotificationAdapter;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {

    ArrayList<String> notify_title;
    ArrayList<String> notify_message;
    ArrayList<String> notify_image;

    private static final String TAG = "NotificationsActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_notify);
        NotificationContent();

    }

    private void NotificationContent() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.Notification_recycler);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        NotificationAdapter adapter = new NotificationAdapter(this,notify_title,notify_message,notify_image);
        recyclerView.setAdapter(adapter);

    }
}
