package com.lucidleanlabs.dev.lcatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lucidleanlabs.dev.lcatalog.MainActivity;
import com.lucidleanlabs.dev.lcatalog.NotificationsActivity;
import com.lucidleanlabs.dev.lcatalog.ProductPageActivity;
import com.lucidleanlabs.dev.lcatalog.R;

import java.util.ArrayList;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private static final String TAG = "NotificationAdapter";

    private Activity activity;
    private ArrayList<String> notify_title;
    private ArrayList<String> notify_message;
    private ArrayList<String> notify_image;

    public NotificationAdapter(NotificationsActivity activity,
                               ArrayList<String> notify_title,
                               ArrayList<String> notify_message,
                               ArrayList<String> notify_image) {

        this.notify_title = notify_title;
        this.notify_message = notify_message;
        this.notify_image = notify_image;

        Log.e(TAG, "title" + notify_title);
        Log.e(TAG, "message" + notify_message);
        Log.e(TAG, "image" + notify_image);

        this.activity = activity;

    }

    /**
     * View holder to display each RecylerView item
     */

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title, message;
        private LinearLayout container;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.notify_image);
            container = (LinearLayout) itemView.findViewById(R.id.notification_container);
            title = (TextView) itemView.findViewById(R.id.notification_title);
            message = (TextView) itemView.findViewById(R.id.notification_data);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_notification, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Context[] context = new Context[1];
        holder.imageView.setImageResource(R.drawable.dummy_icon);
        holder.title.setText("Notifications");
        holder.message.setText("hi hello welcome to the Immersions software labs");
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context[0] = v.getContext();
                Intent intent = new Intent(context[0], MainActivity.class);
                context[0].startActivity(intent);
                Toast.makeText(activity, "position" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return 10;
    }


}
