package com.lucidleanlabs.dev.lcatalog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProductDetails extends Fragment {

    private static final String TAG = "ProductDetails";

    private static final String REGISTER_URL = "http://35.154.252.64:8080/lll/web/vendor/by?id=";
    private static String FILE_URL = "http://35.154.252.64:8080/models/";
    private static String EXTENDED_URL;

    String article_title;
    String article_description;
    String article_price;
    String article_discount;
    String article_width;
    String article_height;
    String article_length;
    String article_price_new;
    String article_dimensions;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_product_details, container, false);

        article_title = getArguments().getString("article_title");

        Log.e(TAG,""+article_title);
        article_description = getArguments().getString("article_description");
        Log.e(TAG,""+article_description);


        article_price = getArguments().getString("article_price");
        Log.e(TAG,""+article_price);

        article_discount = getArguments().getString("article_discount");
        Log.e(TAG,""+article_discount);

        article_width = getArguments().getString("width");
        article_height = getArguments().getString("height");
        article_length = getArguments().getString("length");
        article_dimensions = getArguments().getString("article_dimensions");

        return view;
    }

}
