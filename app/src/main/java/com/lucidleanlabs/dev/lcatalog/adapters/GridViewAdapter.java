package com.lucidleanlabs.dev.lcatalog.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lucidleanlabs.dev.lcatalog.ProductPage;
import com.lucidleanlabs.dev.lcatalog.R;

import java.util.ArrayList;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {

    private static final String TAG = "GridViewAdapter";

    private Activity activity;

    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;

    public GridViewAdapter(Activity activity, ArrayList<String> item_names, ArrayList<String> item_descriptions, ArrayList<String> item_prices) {
        this.item_names = item_names;
        this.item_descriptions = item_descriptions;
        this.item_prices = item_prices;
        Log.e(TAG, "names--" + item_names);
        Log.e(TAG, "descriptions--" + item_descriptions);
        Log.e(TAG, "prices--" + item_prices);
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_grid, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewAdapter.ViewHolder viewHolder, final int position) {

        final Context[] context = new Context[1];

        viewHolder.item_image.setImageResource(R.drawable.dummy_icon);
        viewHolder.item_name.setText("I am a " +item_names.get(position)+"");
        viewHolder.item_description.setText(item_descriptions.get(position));
        viewHolder.item_price.setText("I cost Rs "+item_prices.get(position)+"/-");

        viewHolder.grid_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context[0] = v.getContext();

                Intent intent = new Intent(context[0], ProductPage.class);
                context[0].startActivity(intent);

                Toast.makeText(activity, "You clicked on Article: " + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return item_names.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item_name, item_description, item_price;
        private ImageView item_image;
        private RelativeLayout grid_container;

        ViewHolder(View view) {
            super(view);
            grid_container = (RelativeLayout) view.findViewById(R.id.grid_container);
            item_image = (ImageView) view.findViewById(R.id.grid_item_image);
            item_name = (TextView) view.findViewById(R.id.grid_item_name);
            item_description = (TextView) view.findViewById(R.id.grid_item_description);
            item_price = (TextView) view.findViewById(R.id.grid_item_price);
        }
    }
}
