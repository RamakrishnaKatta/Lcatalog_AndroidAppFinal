package com.lucidleanlabs.dev.lcatalog;

import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucidleanlabs.dev.lcatalog.utils.NetworkConnectivity;

public class AboutUsActivity extends AppCompatActivity {
    ImageView facebook, linkedIn, twitter, instagram, youtube;
    TextView heading3, heading4, heading5;

    public static final String TAG = "AboutUsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        facebook = (ImageView) findViewById(R.id.facebook_about);
        instagram = (ImageView) findViewById(R.id.instagram_about);
        twitter = (ImageView) findViewById(R.id.twitter_about);
        linkedIn = (ImageView) findViewById(R.id.linkedin_about);
        youtube = (ImageView) findViewById(R.id.youtube_about);

        heading3 = (TextView) findViewById(R.id.textview5);
        heading4 = (TextView) findViewById(R.id.textview7);
        heading5 = (TextView) findViewById(R.id.textview9);


        heading3.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        heading4.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        heading4.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);


        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebook_url = "https://www.facebook.com/immersionslabs/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook_url));
                startActivity(browserIntent);
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instagram_url = "https://www.instagram.com/immersionslabs/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagram_url));
                startActivity(browserIntent);
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String twitter_url = "https://twitter.com/immersionslabs/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter_url));
                startActivity(browserIntent);
            }
        });
        linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String linkedIn_url = "https://www.linkedin.com/in/lucidleanlabs/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkedIn_url));
                startActivity(browserIntent);
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtube_url = "https://www.youtube.com/channel/UCbFTPamOyx9GqVdlYqjckqQ/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube_url));
                startActivity(browserIntent);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about_us);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (NetworkConnectivity.checkInternetConnection(AboutUsActivity.this)) {

        } else {
            InternetMessage();
        }
    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                if (NetworkConnectivity.checkInternetConnection(AboutUsActivity.this)) {


                } else {

                    InternetMessage();
                    // CustomMessage.getInstance().CustomMessage(this,"Check Your Internet connection.");
                }
            }
        });
        snackbar.show();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
