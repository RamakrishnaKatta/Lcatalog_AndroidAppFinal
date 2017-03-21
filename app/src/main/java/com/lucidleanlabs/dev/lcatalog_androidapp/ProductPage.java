package com.lucidleanlabs.dev.lcatalog_androidapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class ProductPage extends AppCompatActivity {

    private static final String TAG = "ProductPage";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        ImageView productimage = (ImageView) findViewById(R.id.product_image);
        ImageButton share = (ImageButton) findViewById(R.id.share_icon);
        ImageButton augment = (ImageButton) findViewById(R.id.augment_icon);
        ImageButton view = (ImageButton) findViewById(R.id.view_icon);
        TextView title = (TextView) findViewById(R.id.title_text);
        TextView titledisplay = (TextView) findViewById(R.id.title_display);
        TextView description = (TextView) findViewById(R.id.description_text);
        TextView descriptiondisplay = (TextView) findViewById(R.id.description_display);
        TextView price = (TextView) findViewById(R.id.pricetext);
        TextView pricedisplay = (TextView) findViewById(R.id.price_display);
        ImageView priceicon = (ImageView) findViewById(R.id.price_icon);

    }
}
