package com.lucidleanlabs.dev.lcatalog;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
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
import com.lucidleanlabs.dev.lcatalog.adapters.ImageSliderAdapter;
import com.lucidleanlabs.dev.lcatalog.utils.DownloadImageTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ProductPageActivity extends AppCompatActivity {

    private static final String TAG = "ProductPageActivity";
    private static final String REGISTER_URL = "http://35.154.252.64:8080/lll/web/vendor/by?id=";
    private static String VENDOR_URL = null;

    private ViewPager viewPager;
    private LinearLayout ll_dots;
    ImageSliderAdapter imagesliderAdapter;
    ArrayList<Integer> slider_images = new ArrayList<>();
    TextView[] dots;
    int page_position = 0;

    private String vendor_name;
    private String vendor_address;
    private String vendor_image;
    private String vendor_id;

    private TextView article_vendor_name;
    private TextView article_vendor_location;
    private ImageView vendor_logo, article_image;
    String Article_Name;

    String width, length, height;
    String image1, image2, image3, image4, image5;

    Drawable im1, im2, im3, im4, im5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_article);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        article_image = (ImageView) findViewById(R.id.article_image_view);
        vendor_logo = (ImageView) findViewById(R.id.article_vendor_logo);

        ImageButton article_share = (ImageButton) findViewById(R.id.article_share_icon);
        ImageButton article_download = (ImageButton) findViewById(R.id.article_download_icon);
        ImageButton article_3d_view = (ImageButton) findViewById(R.id.article_3dview_icon);

        final TextView article_title = (TextView) findViewById(R.id.article_title_text);
        TextView article_description = (TextView) findViewById(R.id.article_description_text);
        TextView article_price = (TextView) findViewById(R.id.article_price_value);
        TextView article_discount = (TextView) findViewById(R.id.article_price_discount_value);
        TextView article_width = (TextView) findViewById(R.id.article_width_text);
        TextView article_height = (TextView) findViewById(R.id.article_height_text);
        TextView article_length = (TextView) findViewById(R.id.article_length_text);
        article_vendor_name = (TextView) findViewById(R.id.article_vendor_text);
        article_vendor_location = (TextView) findViewById(R.id.article_vendor_address_text);

        final Bundle b = getIntent().getExtras();

        article_title.setText(b.getCharSequence("article_title"));
        Article_Name = (String) b.getCharSequence("article_title");
        Log.e(TAG, "names----" + b.getCharSequence("article_title"));

        article_description.setText(b.getCharSequence("article_description"));
        article_price.setText(b.getCharSequence("article_price"));
        article_discount.setText(b.getCharSequence("article_discount"));

        String article_dimensions = (String) b.getCharSequence("article_dimensions");
        System.out.print("Article Dimensions" + article_dimensions);

        try {
            JSONObject dimension_json = new JSONObject(article_dimensions);
            width = dimension_json.getString("width");
            length = dimension_json.getString("length");
            height = dimension_json.getString("height");

            Log.e(TAG, " width-- " + width + " length-- " + length + " height-- " + height);
            Log.e(TAG, " dimension_json-- " + dimension_json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        article_width.setText(width);
        article_height.setText(height);
        article_length.setText(length);

        String article_images = (String) b.getCharSequence("article_images");

        try {
            JSONObject image_json = new JSONObject(article_images);

            image1 = image_json.getString("image1");
            im1 = new BitmapDrawable(getResources(), String.valueOf(new DownloadImageTask(article_image).execute(image1)));
            Log.e(TAG, " image1-- " + image1 + "bitmap" + im1);

            image2 = image_json.getString("image2");
            //im2 = new BitmapDrawable(getResources(), String.valueOf(new DownloadMultiImage().execute(image2)));
            Log.e(TAG, " image2-- " + image2 + "bitmap" + im2);

            image3 = image_json.getString("image3");
            //im3 = new BitmapDrawable(getResources(), String.valueOf(new DownloadMultiImage().execute(image3)));
            Log.e(TAG, " image3-- " + image3 + "bitmap" + im3);

            image4 = image_json.getString("image4");
            //im4 = new BitmapDrawable(getResources(), String.valueOf(new DownloadMultiImage().execute(image4)));
            Log.e(TAG, " image4-- " + image4 + "bitmap" + im4);

            image5 = image_json.getString("image5");
            //im5 = new BitmapDrawable(getResources(), String.valueOf(new DownloadMultiImage().execute(image5)));
            Log.e(TAG, " image5-- " + image5 + "bitmap" + im5);

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

        article_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    addModelFolder();
                    Toast.makeText(ProductPageActivity.this, "You clicked on download", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        init();

        addBottomDots(0);

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (page_position == slider_images.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                viewPager.setCurrentItem(page_position, true);
            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 2000, 5000);
    }

    /*creation of directory in external storage */
    private void addModelFolder() throws IOException {
        String state = Environment.getExternalStorageState();

        File folder = null;
        if (state.contains(Environment.MEDIA_MOUNTED)) {
            Log.e(TAG, "Article Name--" + Article_Name);
            folder = new File(Environment.getExternalStorageDirectory() + "/L_CATALOGUE/Models/" + Article_Name);
        }
        assert folder != null;
        if (!folder.exists()) {
            folder.mkdirs();
        }
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
                    Log.e(TAG, "vendor ID--" + vendor_id);
                    vendor_name = resp.getString("name");
                    Log.e(TAG, "vendor Name--" + vendor_name);
                    vendor_address = resp.getString("location");
                    Log.e(TAG, "vendor Address--" + vendor_address);
                    vendor_image = resp.getString("logo");
                    Log.e(TAG, "vendor Image--" + vendor_image);

                    article_vendor_name.setText(vendor_name);
                    article_vendor_location.setText(vendor_address);
                    new DownloadImageTask(vendor_logo).execute(vendor_image);

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


    /*Image slider in product page Activity */
    private void init() {
        final Integer[] Images = {R.drawable.dummy_icon, R.drawable.background};
        for (int i = 0; i < Images.length; i++)
            slider_images.add(Images[i]);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        imagesliderAdapter = new ImageSliderAdapter(ProductPageActivity.this, slider_images);
        viewPager.setAdapter(imagesliderAdapter);

        ll_dots = (LinearLayout) findViewById(R.id.LL_Dots);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /*Image Slider Indicators for the product page Activity*/
    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_images.size()];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        ll_dots.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }


}

