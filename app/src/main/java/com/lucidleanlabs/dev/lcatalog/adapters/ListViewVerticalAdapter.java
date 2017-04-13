package com.lucidleanlabs.dev.lcatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lucidleanlabs.dev.lcatalog.ProductPage;
import com.lucidleanlabs.dev.lcatalog.R;

public class ListViewVerticalAdapter extends RecyclerView.Adapter<ListViewVerticalAdapter.ViewHolder> {

    private Activity activity;

    public ListViewVerticalAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_vertical_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final Context[] context = new Context[1];

        viewHolder.imageView.setImageResource(R.drawable.dummy_icon);
        viewHolder.container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                context[0] = v.getContext();

                Intent intent = new Intent(context[0], ProductPage.class);
                context[0].startActivity(intent);
                Toast.makeText(activity, "Position: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    /**
     * View holder to display each RecylerView item
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private RelativeLayout container;

        ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            container = (RelativeLayout) view.findViewById(R.id.container);
        }
    }
}
