package com.lucidleanlabs.dev.lcatalog_androidapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lucidleanlabs.dev.lcatalog_androidapp.adapters.ImagesliderAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class ProductPage extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout ll_dots;
    ImagesliderAdapter imagesliderAdapter;
    ArrayList<Integer> slider_images = new ArrayList<Integer>();
    TextView[] dots;
    int page_position = 0;
    private static final Integer[] Images = {R.drawable.dummy_icon, R.drawable.background};

    private static final String TAG = "ProductPage";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_article);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ImageView productimage = (ImageView) findViewById(R.id.product_image);
        ImageButton share = (ImageButton) findViewById(R.id.share_icon);
        ImageButton augment = (ImageButton) findViewById(R.id.augment_icon);
        final ImageButton view = (ImageButton) findViewById(R.id.view_icon);
        TextView title = (TextView) findViewById(R.id.title_text);
        TextView titledisplay = (TextView) findViewById(R.id.title_display);
        TextView description = (TextView) findViewById(R.id.description_text);
        TextView descriptiondisplay = (TextView) findViewById(R.id.description_display);
        TextView price = (TextView) findViewById(R.id.pricetext);
        TextView pricedisplay = (TextView) findViewById(R.id.price_display);
        ImageView priceicon = (ImageView) findViewById(R.id.price_icon);


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    private void init() {
        for (int i = 0; i < Images.length; i++)
            slider_images.add(Images[i]);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        imagesliderAdapter = new ImagesliderAdapter(ProductPage.this, slider_images);
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

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_images.size()];

//        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
//        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);


        ll_dots.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            // dots[i].setTextColor(colorsInactive[currentPage]);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
        // dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

}

