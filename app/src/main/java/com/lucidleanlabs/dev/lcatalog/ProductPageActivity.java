package com.lucidleanlabs.dev.lcatalog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.lucidleanlabs.dev.lcatalog.adapters.CatalogAdapter;
import com.lucidleanlabs.dev.lcatalog.adapters.ImageSliderAdapter;
import com.lucidleanlabs.dev.lcatalog.ar.ARNativeActivity;
import com.lucidleanlabs.dev.lcatalog.utils.DownloadImageTask;
import com.lucidleanlabs.dev.lcatalog.utils.DownloadManager;
import com.lucidleanlabs.dev.lcatalog.utils.PrefManager;
import com.lucidleanlabs.dev.lcatalog.utils.UnzipUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class ProductPageActivity extends AppCompatActivity {

    private static final String TAG = "ProductPageActivity";

    private static final String REGISTER_URL = "http://35.154.252.64:8080/lll/web/vendor/by?id=";
    private static String FILE_URL = "http://35.154.252.64:8080/models/";
    private static String EXTENDED_URL;


    LinearLayout note;
    ImageView vendor_logo, article_image;
    ImageButton article_share, article_download, article_3d_view, article_augment;
    TextView article_title, article_description, article_price, article_discount, article_width, article_height, article_length, article_price_new;
    TextView article_vendor_name, article_vendor_location;

    private static String VENDOR_URL = null;

    String article_images;
    // article_images is split in to five parts and assigned to each string
    String image1, image2, image3, image4, image5;

    String oldPrice, discount;
    // calculate the new price using the old price and discount and assign ti newPrice
    String newPrice;

    String dimensions;
    //Split the dimensions into three parts and assign to width, height and length
    String width, length, height;

    String position, name, id, description;

    String vendor_name, vendor_address, vendor_image, vendor_id;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        article_image = (ImageView) findViewById(R.id.article_image_view);
        vendor_logo = (ImageView) findViewById(R.id.article_vendor_logo);
        article_share = (ImageButton) findViewById(R.id.article_share_icon);
        article_download = (ImageButton) findViewById(R.id.article_download_icon);
        article_3d_view = (ImageButton) findViewById(R.id.article_3dview_icon);
        article_augment = (ImageButton) findViewById(R.id.article_augment_icon);
        article_title = (TextView) findViewById(R.id.article_title_text);
        article_description = (TextView) findViewById(R.id.article_description_text);
        article_price = (TextView) findViewById(R.id.article_price_value);
        article_discount = (TextView) findViewById(R.id.article_price_discount_value);
        article_width = (TextView) findViewById(R.id.article_width_text);
        article_height = (TextView) findViewById(R.id.article_height_text);
        article_length = (TextView) findViewById(R.id.article_length_text);
        article_vendor_name = (TextView) findViewById(R.id.article_vendor_text);
        article_vendor_location = (TextView) findViewById(R.id.article_vendor_address_text);
        article_price_new = (TextView) findViewById(R.id.article_price_value_new);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_article);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final Bundle b = getIntent().getExtras();

        name = (String) b.getCharSequence("article_title");
        description = (String) b.getCharSequence("article_description");
        position = (String) b.getCharSequence("article_position");
        id = (String) b.getCharSequence("article_id");

        oldPrice = (String) b.getCharSequence("article_price");
        discount = (String) b.getCharSequence("article_discount");
        Integer x = Integer.parseInt(oldPrice);
        Integer y = Integer.parseInt(discount);
        Integer z = (x * (100 - y)) / 100;
        newPrice = Integer.toString(z);

        dimensions = (String) b.getCharSequence("article_dimensions");
        try {
            JSONObject dimension_json = new JSONObject(dimensions);
            width = dimension_json.getString("width");
            length = dimension_json.getString("length");
            height = dimension_json.getString("height");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VENDOR_URL = REGISTER_URL + b.getCharSequence("article_vendor");
        Log.e(TAG, "VENDOR_URL--" + VENDOR_URL);

        try {
            getVendorData();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        article_images = (String) b.getCharSequence("article_images");

        // All this Data should be sent to fragments in the form of bundle !!

        Log.e(TAG, "Article Name----" + name);
        Log.e(TAG, "Article Description----" + description);
        Log.e(TAG, "Article NewPrice----" + newPrice);
        Log.e(TAG, "Article Dimensions----" + dimensions);
        Log.e(TAG, "Article Width----" + width);
        Log.e(TAG, "Article Height----" + height);
        Log.e(TAG, "Article Length----" + length);
        Log.e(TAG, "Article Position----" + position);
        Log.e(TAG, "Article Images----" + article_images);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.product_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("IMAGES"));
        tabLayout.addTab(tabLayout.newTab().setText("OVERVIEW"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.product_pager);
        final CatalogAdapter adapter = new CatalogAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),
                name, description, oldPrice, discount, newPrice, dimensions, width, height, length, position, id, article_images);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {          }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {         }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {         }
        });

    }

    private void getVendorData() throws JSONException {

        final JSONObject baseclass = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, VENDOR_URL, baseclass, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "response--" + response);

                try {
                    JSONObject resp = response.getJSONObject("resp");
                    vendor_id = resp.getString("id");
                    vendor_name = resp.getString("name");
                    vendor_address = resp.getString("code");
                    vendor_image = resp.getString("logo");

                    Log.e(TAG, "vendor ID--" + vendor_id);
                    Log.e(TAG, "vendor Name--" + vendor_name);
                    Log.e(TAG, "vendor Address--" + vendor_address);
                    Log.e(TAG, "vendor Image--" + vendor_image);

//                    article_vendor_name.setText(vendor_name);
//                    article_vendor_location.setText(vendor_address);
//                    new DownloadImageTask(vendor_logo).execute(vendor_image);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductPageActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject request = new JSONObject(res);
                        Log.e(TAG, "request--" + request);
                    } catch (UnsupportedEncodingException | JSONException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();

        Intent intent = new Intent(this, CatalogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("activity", "SplashScreen");
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}