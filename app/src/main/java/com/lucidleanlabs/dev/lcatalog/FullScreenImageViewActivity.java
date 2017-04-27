package com.lucidleanlabs.dev.lcatalog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.lucidleanlabs.dev.lcatalog.adapters.FullScreenImageAdapter;
import com.lucidleanlabs.dev.lcatalog.utils.ImageUtils;

public class FullScreenImageViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image_view);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        ImageUtils imageUtils = new ImageUtils(getApplicationContext());

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        FullScreenImageAdapter adapter = new FullScreenImageAdapter(FullScreenImageViewActivity.this, imageUtils.getFilePaths());

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
    }
}
