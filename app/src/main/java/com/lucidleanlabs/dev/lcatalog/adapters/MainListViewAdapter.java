package com.lucidleanlabs.dev.lcatalog.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lucidleanlabs.dev.lcatalog.Fragment_Overview;
import com.lucidleanlabs.dev.lcatalog.R;
import com.lucidleanlabs.dev.lcatalog.utils.DownloadImageTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainListViewAdapter extends RecyclerView.Adapter<MainListViewAdapter.ViewHolder> {

    private static final String TAG = "MainListViewAdapter";

    private Fragment_Overview activity;

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_images;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_descriptions;

    public MainListViewAdapter(Fragment_Overview activity, ArrayList<String> item_ids,
                               ArrayList<String> item_names,
                               ArrayList<String> item_images,
                               ArrayList<String> item_prices,
                               ArrayList<String> item_discounts,
                               ArrayList<String> item_descriptions) {
        this.item_ids = item_ids;
        this.item_names = item_names;
        this.item_images = item_images;
        this.item_prices = item_prices;
        this.item_discounts = item_discounts;
        this.item_descriptions = item_descriptions;

        Log.e(TAG, "ids----" + item_ids);
        Log.e(TAG, "Images----" + item_images);
        Log.e(TAG, "names----" + item_names);
        Log.e(TAG, "prices----" + item_prices);
        Log.e(TAG, "discounts----" + item_discounts);
        Log.e(TAG, "descriptions----" + item_descriptions);

        this.activity = activity;
    }

    /**
     * View holder to display each RecylerView item
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView item_image;
        private TextView item_name, item_description, item_price, item_discount, item_price_new;

        LinearLayout main_container;

        ViewHolder(View view) {
            super(view);

            main_container = (LinearLayout) view.findViewById(R.id.main_container);
            item_image = (ImageView) view.findViewById(R.id.main_image);
            item_name = (TextView) view.findViewById(R.id.main_title);
            item_description = (TextView) view.findViewById(R.id.main_data);

            Typeface custom_font = Typeface.createFromAsset(activity.getActivity().getAssets(), "fonts/Graduate-Regular.ttf");
            Typeface custom_font2 = Typeface.createFromAsset(activity.getActivity().getAssets(), "fonts/Cookie-Regular.ttf");
            item_name.setTypeface(custom_font);
            item_description.setTypeface(custom_font2);

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

        viewHolder.item_image.setImageResource(R.drawable.dummy_icon);

        String im1 = null;
        String get_image = item_images.get(position);
        try {
            JSONObject images_json = new JSONObject(get_image);
            im1 = images_json.getString("image1");
            Log.e(TAG, "image >>>>" + im1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new DownloadImageTask(viewHolder.item_image).execute(im1);
       // Picasso.with(activity.getContext()).load(im1).centerCrop().into(viewHolder.item_image);
        viewHolder.item_name.setText(item_names.get(position));
        viewHolder.item_description.setText(item_descriptions.get(position));

    }

    @Override
    public int getItemCount() {
        return item_names.size();
    }
}
