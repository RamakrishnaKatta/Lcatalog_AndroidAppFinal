package com.lucidleanlabs.dev.lcatalog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lucidleanlabs.dev.lcatalog.adapters.ImageSliderAdapter;
import com.lucidleanlabs.dev.lcatalog.ar.ARNativeActivity;
import com.lucidleanlabs.dev.lcatalog.utils.DownloadManager;
import com.lucidleanlabs.dev.lcatalog.utils.PrefManager;
import com.lucidleanlabs.dev.lcatalog.utils.UnzipUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_CANCELED;


public class Product_new extends Fragment {

    private static final String TAG = "Product_new";

    private static final String REGISTER_URL = "http://35.154.252.64:8080/lll/web/vendor/by?id=";
    private static String FILE_URL = "http://35.154.252.64:8080/models/";
    private static String EXTENDED_URL;

    private static String VENDOR_URL = null;

    LinearLayout note;
    ImageView vendor_logo, article_image;
    ImageButton article_share, article_download, article_3d_view, article_augment;
    TextView article_title, article_description, article_price, article_discount, article_width, article_height, article_length, article_price_new;
    TextView article_vendor_name, article_vendor_location;

    String article_images;
    // article_images is split in to five parts and assigned to each string
    String image1, image2, image3, image4, image5;

    String position, name, id, description;

    private ViewPager ArticleViewPager;
    private LinearLayout Slider_dots;
    ImageSliderAdapter imagesliderAdapter;
    ArrayList<String> slider_images = new ArrayList<>();
    TextView[] dots;
    int page_position = 0;

    String Article_ZipFileLocation, Article_ExtractLocation, Article_3DSFileLocation;
    private boolean zip_downloaded = true;
    File zip_file, object_3d_file;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_new, container, false);


        article_share = (ImageButton) view.findViewById(R.id.article_share_icon);
        article_download = (ImageButton) view.findViewById(R.id.article_download_icon);
        article_3d_view = (ImageButton) view.findViewById(R.id.article_3dview_icon);
        article_augment = (ImageButton) view.findViewById(R.id.article_augment_icon);

        article_images = getArguments().getString("article_images");

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

        Log.e(TAG, "Article Image 1----" + image1);
        Log.e(TAG, "Article Image 2----" + image2);
        Log.e(TAG, "Article Image 3----" + image3);
        Log.e(TAG, "Article Image 4----" + image4);
        Log.e(TAG, "Article Image 5----" + image5);


        final String[] Images = {image1, image2, image3, image4, image5};

        Collections.addAll(slider_images, Images);

        ArticleViewPager = (ViewPager) view.findViewById(R.id.article_view_pager);
        imagesliderAdapter = new ImageSliderAdapter(getContext(), slider_images);
        ArticleViewPager.setAdapter(imagesliderAdapter);

        Slider_dots = (LinearLayout) view.findViewById(R.id.article_slider_dots);

        ArticleViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        Article_ZipFileLocation = Environment.getExternalStorageDirectory() + "/L_CATALOGUE/Models/" + article_title + "/" + id + ".zip";
        Log.e(TAG, "ZipFileLocation--" + Article_ZipFileLocation);
        Article_ExtractLocation = Environment.getExternalStorageDirectory() + "/L_CATALOGUE/Models/" + name + "/";
        Log.e(TAG, "ExtractLocation--" + Article_ExtractLocation);
        Article_3DSFileLocation = Environment.getExternalStorageDirectory() + "/L_CATALOGUE/Models/" + name + "/article_view.3ds";
        Log.e(TAG, "Object3DFileLocation--" + Article_3DSFileLocation);

        note = (LinearLayout) view.findViewById(R.id.download_note);


        zip_file = new File(Article_ZipFileLocation);
        object_3d_file = new File(Article_3DSFileLocation);

        zip_downloaded = false;

        article_3d_view.setEnabled(false);
        if (object_3d_file.exists()) {
            article_3d_view.setEnabled(true);
            article_download.setVisibility(View.GONE);
            note.setVisibility(View.GONE);
            zip_downloaded = true;
        }

        article_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Downloading Article, Just for once....");
                progressDialog.setTitle("Article Downloading");
                progressDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                try {
                                    addModelFolder();
                                    EXTENDED_URL = FILE_URL + id + ".zip";
                                    Log.e(TAG, "URL ---------- " + EXTENDED_URL);
                                    new DownloadManager(EXTENDED_URL, name, id);

                                    if (zip_file.exists()) {
                                        new UnzipUtil(Article_ZipFileLocation, Article_ExtractLocation);
                                    } else {
                                        Toast.makeText(getContext(), "Failed to download the Files", Toast.LENGTH_SHORT).show();
                                    }

                                    zip_downloaded = true;
                                    Log.e(TAG, "Zip Downloaded ---------- " + zip_downloaded);
                                    progressDialog.dismiss();
                                    article_download.setVisibility(View.GONE);
                                    article_3d_view.setEnabled(true);
                                    note.setVisibility(View.GONE);

                                } catch (IOException e) {
                                    article_download.setVisibility(View.VISIBLE);
                                    article_3d_view.setEnabled(false);
                                    zip_downloaded = false;
                                    Log.e(TAG, "Zip Not Downloaded ---------- " + zip_downloaded);
                                    e.printStackTrace();
                                    note.setVisibility(View.VISIBLE);
                                }
                            }
                        }, 6000);
            }
        });

        article_3d_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zip_downloaded == true) {

                    Bundle b3 = new Bundle();
                    b3.putString("article_name", name);
                    b3.putString("article_position", position);
                    Intent _3d_intent = new Intent(getContext(), Article3dViewActivity.class).putExtras(b3);
                    startActivity(_3d_intent);
                }
            }
        });


        article_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "L_CATALOGUE");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "If you want to know more details Click here to visit http://lucidleanlabs.com/ ");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        article_augment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ARNativeActivity.class);
                startActivity(intent);
            }
        });

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

        return view;

    }

    /*creation of directory in external storage */

    private void addModelFolder() throws IOException {
        String state = Environment.getExternalStorageState();

        File folder = null;
        if (state.contains(Environment.MEDIA_MOUNTED)) {
            Log.e(TAG, "Article Name--" + name);
            folder = new File(Environment.getExternalStorageDirectory() + "/L_CATALOGUE/Models/" + name);
        }
        assert folder != null;
        if (!folder.exists()) {
            boolean wasSuccessful = folder.mkdirs();
            Log.e(TAG, "Model Directory is Created --- '" + wasSuccessful + "' Thank You !!");
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_images.size()];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        Slider_dots.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            Slider_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }




}
