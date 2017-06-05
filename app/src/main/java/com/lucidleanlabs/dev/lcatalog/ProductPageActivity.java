package com.lucidleanlabs.dev.lcatalog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
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
import com.lucidleanlabs.dev.lcatalog.adapters.ImageSliderAdapter;
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

    private PrefManager prefManager4;


    private static final String TAG = "ProductPageActivity";

    private static final String REGISTER_URL = "http://35.154.252.64:8080/lll/web/vendor/by?id=";
    private static String file_url = "http://35.154.252.64:8080/models/";
    private static String extended_url;

    private static String VENDOR_URL = null;

    private String vendor_name;
    private String vendor_address;
    private String vendor_image;
    private String vendor_id;

    private TextView article_vendor_name;
    private TextView article_vendor_location;
    private ImageView vendor_logo, article_image;

    String Article_Name, Article_Id;
    String ZipFileLocation, ExtractLocation, Object3DFileLocation;
    String width, length, height;
    String article_position;

    String image1, image2, image3, image4, image5;

    private ViewPager ArticleViewPager;
    private LinearLayout Slider_dots;
    ImageSliderAdapter imagesliderAdapter;
    ArrayList<String> slider_images = new ArrayList<>();
    TextView[] dots;
    int page_position = 0;

    private boolean zip_downloaded = true;
    File zip_file, object_3d_file;


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
        final ImageButton article_download = (ImageButton) findViewById(R.id.article_download_icon);
        final ImageButton article_3d_view = (ImageButton) findViewById(R.id.article_3dview_icon);

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

        String oldPrice = (String) b.getCharSequence("article_price");
        String discount = (String) b.getCharSequence("article_discount");
        article_position = (String) b.getCharSequence("article_position");
        TextView article_price_new = (TextView) findViewById(R.id.article_price_value_new);

        Integer x = Integer.parseInt(oldPrice);
        Integer y = Integer.parseInt(discount);
        Integer z = (x * (100 - y)) / 100;
        String newPrice = Integer.toString(z);
        Log.e(TAG, "newPrice-- " + newPrice);

        article_title.setText(b.getCharSequence("article_title"));
        Article_Name = (String) b.getCharSequence("article_title");
        Log.e(TAG, "names----" + b.getCharSequence("article_title"));

        article_description.setText(b.getCharSequence("article_description"));
        article_price.setText(Html.fromHtml("<strike>" + (oldPrice) + "</strike>"));
        article_discount.setText("-" + discount + "% OFF");
        article_price_new.setText(newPrice);

        Article_Id = (String) b.getCharSequence("article_id");
        System.out.print("Article ID" + Article_Id);

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
        article_width.setText(width + " in");
        article_height.setText(height + " in");
        article_length.setText(length + " in");

        String article_images = (String) b.getCharSequence("article_images");

        try {
            JSONObject image_json = new JSONObject(article_images);
            image1 = image_json.getString("image1");
            image2 = image_json.getString("image2");
            image3 = image_json.getString("image3");
            image4 = image_json.getString("image4");
            image5 = image_json.getString("image5");

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

        Log.e(TAG, "id=======" + b.getCharSequence("article_id"));
        Log.e(TAG, "name=======" + b.getCharSequence("article_title"));

         /*Extract zip file from 3D view button */
        ZipFileLocation = Environment.getExternalStorageDirectory() + "/L_CATALOGUE/Models/" + Article_Name + "/" + Article_Id + ".zip";
        Log.e(TAG, "ZipFileLocation--" + ZipFileLocation);
        ExtractLocation = Environment.getExternalStorageDirectory() + "/L_CATALOGUE/Models/" + Article_Name + "/";
        Log.e(TAG, "ExtractLocation--" + ExtractLocation);
        Object3DFileLocation = Environment.getExternalStorageDirectory() + "/L_CATALOGUE/Models/" + Article_Name + "/article_view_" + article_position + ".3ds";
        Log.e(TAG, "Object3DFileLocation--" + Object3DFileLocation);

        zip_file = new File(ZipFileLocation);
        object_3d_file = new File(Object3DFileLocation);

        zip_downloaded = false;

        article_3d_view.setEnabled(false);
        if (object_3d_file.exists()) {
            article_3d_view.setEnabled(true);
            article_download.setEnabled(false);
            zip_downloaded = true;
        }

        article_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(ProductPageActivity.this, "You clicked on download", Toast.LENGTH_SHORT).show();
                final ProgressDialog progressDialog = new ProgressDialog(ProductPageActivity.this, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Downloading Article, Just for once....");
                progressDialog.setTitle("Object Downloading");
                progressDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                try {
                                    addModelFolder();
                                    extended_url = file_url + Article_Id + ".zip";
                                    Log.e(TAG, "URL ---------- " + extended_url);
//                                    new DownloadFileTask().execute(extended_url);
                                    new DownloadManager(extended_url, Article_Name, Article_Id);

                                    if (zip_file.exists()) {
                                        new UnzipUtil(ZipFileLocation, ExtractLocation);
                                    } else {
                                        Toast.makeText(ProductPageActivity.this, "Failed to download the Files", Toast.LENGTH_SHORT).show();
                                    }

                                    zip_downloaded = true;
                                    progressDialog.dismiss();
                                    Log.e(TAG, "Zip Downloaded ---------- " + zip_downloaded);
                                    article_download.setEnabled(false);
                                    article_3d_view.setEnabled(true);

                                } catch (IOException e) {
                                    article_download.setEnabled(true);
                                    article_3d_view.setEnabled(false);
                                    zip_downloaded = false;
                                    Log.e(TAG, "Zip Not Downloaded ---------- " + zip_downloaded);
                                    e.printStackTrace();
                                }
                            }
                        }, 3000);
            }
        });

        article_3d_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b3 = new Bundle();
                b3.putString("3ds_location", ExtractLocation);
                b3.putString("article_position", article_position);
                Log.e(TAG, "3ds Location--" + b3.getCharSequence("3ds_location"));
                Log.e(TAG, "article_position--" + b3.getCharSequence("article_position"));

                if (zip_downloaded == true) {
                    Intent _3dintent = new Intent(ProductPageActivity.this, Article3dViewActivity.class).putExtras(b3);
                    startActivity(_3dintent);

                } else {
                    Toast.makeText(ProductPageActivity.this, "Can't provide the 3d View, unless the Object is Downloaded, \n Please Click the download button beside", Toast.LENGTH_SHORT).show();
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
                ArticleViewPager.setCurrentItem(page_position, true);
            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 2000, 5000);


        prefManager4 = new PrefManager(this);
        Log.e(TAG, "" + prefManager4.isFirstTimeLaunchScreen4());
        if (prefManager4.isFirstTimeLaunchScreen4()) {
            ShowcaseView();
        }

    }

    /*showcaseview for the product page Activity*/
    private void ShowcaseView() {
        prefManager4.setFirstTimeLaunchScreen4(false);
        Log.e(TAG, "" + prefManager4.isFirstTimeLaunchScreen4());

        final Display display = getWindowManager().getDefaultDisplay();
        final TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        // This tap target will target the back button, we just need to pass its containing toolbar
                        TapTarget.forView(findViewById(R.id.article_fav_icon), "Like", "click here if u  like it ")
                                .cancelable(false)
                                .targetRadius(30)
                                .outerCircleColor(R.color.primary_darker)
                                .id(1),
                        TapTarget.forView(findViewById(R.id.article_share_icon), "Share", "you can share the screen")
                                .cancelable(false)
                                .targetRadius(30)
                                .outerCircleColor(R.color.primary_darker)
                                .id(2),
                        TapTarget.forView(findViewById(R.id.article_3dview_icon), "3d", "you can see the object in 3d View")
                                .cancelable(false)
                                .targetRadius(30)
                                .outerCircleColor(R.color.primary_darker)
                                .id(3),
                        TapTarget.forView(findViewById(R.id.article_download_icon), "Download", "click Here for downloading the object")
                                .cancelable(false)
                                .targetRadius(30)
                                .outerCircleColor(R.color.primary_darker)
                                .id(4)

                ).listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                    }
                });
        sequence.start();

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
            boolean wasSuccessful = folder.mkdirs();
            Log.e(TAG, "Model Directory is Created --- '" + wasSuccessful + "' Thank You !!");
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
                    vendor_address = resp.getString("code");
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

        final String[] Images = {image1, image2, image3, image4, image5};

        Collections.addAll(slider_images, Images);

        ArticleViewPager = (ViewPager) findViewById(R.id.article_view_pager);
        imagesliderAdapter = new ImageSliderAdapter(ProductPageActivity.this, slider_images);
        ArticleViewPager.setAdapter(imagesliderAdapter);

        Slider_dots = (LinearLayout) findViewById(R.id.article_slider_dots);

        ArticleViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        Slider_dots.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            Slider_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this,CatalogActivity.class);
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("activity", "SplashScreen");

        startActivity(intent);

       // startActivity(new Intent(this, CatalogActivity.class));
        finish();
    }

}

