package com.lucidleanlabs.dev.lcatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListViewHorizontalAdapter extends RecyclerView.Adapter<ListViewHorizontalAdapter.ViewHolder> {

    private static final String TAG = "ListViewHorizontalAdapter";

    private Activity activity;

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_vendors;
    private ArrayList<String> item_images;
    private ArrayList<String> item_dimensions;

    public ListViewHorizontalAdapter(Activity activity,
                                     ArrayList<String> item_ids,
                                     ArrayList<String> item_names,
                                     ArrayList<String> item_descriptions,
                                     ArrayList<String> item_prices,
                                     ArrayList<String> item_discounts,
                                     ArrayList<String> item_vendors,
                                     ArrayList<String> item_images,
                                     ArrayList<String> item_dimensions) {

        this.item_ids = item_ids;
        this.item_names = item_names;
        this.item_descriptions = item_descriptions;
        this.item_prices = item_prices;
        this.item_discounts = item_discounts;
        this.item_vendors = item_vendors;
        this.item_images = item_images;
        this.item_dimensions = item_dimensions;

        Log.e(TAG, "ids----" + item_ids);
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

        private TextView item_name, item_description, item_price, item_discount, item_vendor, item_price_new;
        private ImageView item_image;
        private RelativeLayout h_container;

        ViewHolder(View view) {
            super(view);
            h_container = (RelativeLayout) view.findViewById(R.id.h_container);
            item_image = (ImageView) view.findViewById(R.id.h_item_image);
            item_name = (TextView) view.findViewById(R.id.h_item_name);
            item_description = (TextView) view.findViewById(R.id.h_item_description);
            item_price = (TextView) view.findViewById(R.id.h_item_price);
            item_discount = (TextView) view.findViewById(R.id.h_item_discount_value);
            item_price_new = (TextView) view.findViewById(R.id.h_item_price_new);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_horizontal_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHorizontalAdapter.ViewHolder viewHolder, final int position) {

        final Context[] context = new Context[1];

        viewHolder.item_image.setImageResource(R.drawable.dummy_icon);

        String im1 = null;
        String get_image = item_images.get(position);
        try {
            JSONObject images_json = new JSONObject(get_image);
            im1 = images_json.getString("image1");
            Log.e(TAG, "image1=====" + im1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new DownloadImageTask(viewHolder.item_image).execute(im1);

        Integer x = Integer.parseInt(item_prices.get(position));
        Integer y = Integer.parseInt(item_discounts.get(position));
        Integer z = (x * (100 - y)) / 100;
        String itemNewPrice = Integer.toString(z);

        viewHolder.item_name.setText(item_names.get(position));
        viewHolder.item_description.setText(item_descriptions.get(position));
        viewHolder.item_price.setText("ORIGINAL PRICE: Rs " + (Html.fromHtml("<strike>" + item_prices.get(position) + "</strike>")) + "/-");
        viewHolder.item_discount.setText(" Discount - " + item_discounts.get(position) + "% OFF");
        viewHolder.item_price_new.setText("AFTER DISCOUNT PRICE: Rs " + itemNewPrice + "/-");


        viewHolder.h_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context[0] = v.getContext();

                Intent intent = new Intent(context[0], ProductPageActivity.class);
                Bundle b = new Bundle();

                b.putString("article_id", item_ids.get(position));
                b.putString("article_title", item_names.get(position));
                b.putString("article_description", item_descriptions.get(position));
                b.putString("article_price", item_prices.get(position));
                b.putString("article_discount", item_discounts.get(position));
                b.putString("article_vendor", item_vendors.get(position));
                b.putString("article_dimensions", item_dimensions.get(position));
                b.putString("article_images", item_images.get(position));
                b.putString("article_position", String.valueOf(position));

                intent.putExtras(b);

                context[0].startActivity(intent);

//                Toast.makeText(activity, "Position clicked: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return item_names.size();
    }
}
