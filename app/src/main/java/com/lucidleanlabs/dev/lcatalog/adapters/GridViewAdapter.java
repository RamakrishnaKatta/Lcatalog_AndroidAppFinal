package com.lucidleanlabs.dev.lcatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lucidleanlabs.dev.lcatalog.ProductPageActivity;
import com.lucidleanlabs.dev.lcatalog.R;
import com.lucidleanlabs.dev.lcatalog.utils.DownloadImageTask;

import java.util.ArrayList;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {

    private static final String TAG = "GridViewAdapter";

    private Activity activity;

    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_vendors;
    private ArrayList<String> item_images;
    private ArrayList<String> item_dimensions;

    public GridViewAdapter(Activity activity,
                           ArrayList<String> item_names,
                           ArrayList<String> item_descriptions,
                           ArrayList<String> item_prices,
                           ArrayList<String> item_discounts,
                           ArrayList<String> item_vendors,
                           ArrayList<String> item_images,
                           ArrayList<String> item_dimensions) {

        this.item_names = item_names;
        this.item_descriptions = item_descriptions;
        this.item_prices = item_prices;
        this.item_discounts = item_discounts;
        this.item_vendors = item_vendors;
        this.item_images = item_images;
        this.item_dimensions = item_dimensions;

        Log.e(TAG, "names----" + item_names);
        Log.e(TAG, "descriptions----" + item_descriptions);
        Log.e(TAG, "prices----" + item_prices);
        Log.e(TAG, "discounts----" + item_discounts);
        Log.e(TAG, "vendors----" + item_vendors);
        Log.e(TAG, "Images----" + item_images);
        Log.e(TAG, "Dimensions----" + item_dimensions);

        this.activity = activity;
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_grid, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GridViewAdapter.ViewHolder viewHolder, final int position) {

        final Context[] context = new Context[1];

        viewHolder.item_image.setImageResource(R.drawable.dummy_icon);
        new DownloadImageTask(viewHolder.item_image).execute(item_images.get(position));

        viewHolder.item_name.setText("I am a " + item_names.get(position) + "");
        viewHolder.item_description.setText(item_descriptions.get(position));
        viewHolder.item_price.setText("I cost Rs " + item_prices.get(position) + "/-");

        viewHolder.grid_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context[0] = v.getContext();

                Intent intent = new Intent(context[0], ProductPageActivity.class);
                Bundle b = new Bundle();

                b.putString("article_title", item_names.get(position));
                Log.e(TAG, "names----" + b.getCharSequence("article_title"));
                b.putString("article_description", item_descriptions.get(position));
                b.putString("article_price", item_prices.get(position));
                b.putString("article_discount", item_discounts.get(position));
                b.putString("article_vendor", item_vendors.get(position));
                b.putString("article_dimensions", item_dimensions.get(position));
                b.putString("article_images", item_images.get(position));

                intent.putExtras(b);

                context[0].startActivity(intent);

                Toast.makeText(activity, "You clicked on Article: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return item_names.size();
    }
}
