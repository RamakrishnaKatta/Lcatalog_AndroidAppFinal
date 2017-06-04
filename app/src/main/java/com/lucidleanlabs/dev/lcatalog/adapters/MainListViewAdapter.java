package com.lucidleanlabs.dev.lcatalog.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lucidleanlabs.dev.lcatalog.OverviewActivity;
import com.lucidleanlabs.dev.lcatalog.R;
import com.lucidleanlabs.dev.lcatalog.utils.DownloadImageTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainListViewAdapter extends RecyclerView.Adapter<MainListViewAdapter.ViewHolder> {

    private static final String TAG = "MainListViewAdapter";

    private OverviewActivity activity;

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_images;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;

    public MainListViewAdapter(OverviewActivity activity,
                               ArrayList<String> item_ids,
                               ArrayList<String> item_names,
                               ArrayList<String> item_images,
                               ArrayList<String> item_prices,
                               ArrayList<String> item_discounts) {

        this.item_ids = item_ids;
        this.item_names = item_names;
        this.item_images = item_images;
        this.item_prices = item_prices;
        this.item_discounts = item_discounts;

        Log.e(TAG, "ids----" + item_ids);
        Log.e(TAG, "Images----" + item_images);
        Log.e(TAG, "names----" + item_names);
        Log.e(TAG, "prices----" + item_prices);
        Log.e(TAG, "discounts----" + item_discounts);

        this.activity = activity;
    }

    /**
     * View holder to display each RecylerView item
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView item_image1, item_image2, item_image3, item_image4;
        private TextView item_name, item_price, item_discount, item_price_new;
        LinearLayout main_container;

        ViewHolder(View view) {
            super(view);

            main_container = (LinearLayout) view.findViewById(R.id.main_container);
            item_image1 = (ImageView) view.findViewById(R.id.main_image1);
            item_image2 = (ImageView) view.findViewById(R.id.main_image2);
            item_image3 = (ImageView) view.findViewById(R.id.main_image3);
            item_image4 = (ImageView) view.findViewById(R.id.main_image4);
            item_name = (TextView) view.findViewById(R.id.main_item_name);
            item_price = (TextView) view.findViewById(R.id.main_item_price);
            item_discount = (TextView) view.findViewById(R.id.main_item_discount_value);
            item_price_new = (TextView) view.findViewById(R.id.main_item_price_new);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater(null);
        View view = inflater.inflate(R.layout.content_display, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MainListViewAdapter.ViewHolder viewHolder, final int position) {

        final Context[] context = new Context[1];

        viewHolder.item_image1.setImageResource(R.drawable.dummy_icon);
        viewHolder.item_image2.setImageResource(R.drawable.dummy_icon);
        viewHolder.item_image3.setImageResource(R.drawable.dummy_icon);
        viewHolder.item_image4.setImageResource(R.drawable.dummy_icon);

        String im1 = null, im2 = null, im3 = null, im4 = null;
        String get_image = item_images.get(position);
        try {
            JSONObject images_json = new JSONObject(get_image);
            im1 = images_json.getString("image1");
            Log.e(TAG, "image1 >>>>" + im1);
            im2 = images_json.getString("image2");
            Log.e(TAG, "image2 >>>>" + im2);
            im3 = images_json.getString("image3");
            Log.e(TAG, "image3 >>>>" + im3);
            im4 = images_json.getString("image4");
            Log.e(TAG, "image4 >>>>" + im4);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new DownloadImageTask(viewHolder.item_image1).execute(im1);
        new DownloadImageTask(viewHolder.item_image2).execute(im2);
        new DownloadImageTask(viewHolder.item_image3).execute(im3);
        new DownloadImageTask(viewHolder.item_image4).execute(im4);

        viewHolder.item_name.setText(item_names.get(position));

        Integer x = Integer.parseInt(item_prices.get(position));
        Integer y = Integer.parseInt(item_discounts.get(position));
        Integer z = (x * (100 - y)) / 100;
        String itemNewPrice = Integer.toString(z);

        viewHolder.item_price.setText("Rs " + (Html.fromHtml("<strike>" + item_prices.get(position) + "</strike>")) + "/-");
        viewHolder.item_discount.setText("-" + item_discounts.get(position) + "%");
        viewHolder.item_price_new.setText(itemNewPrice + "/-");
    }

    @Override
    public int getItemCount() {
        return item_names.size();
    }
}
