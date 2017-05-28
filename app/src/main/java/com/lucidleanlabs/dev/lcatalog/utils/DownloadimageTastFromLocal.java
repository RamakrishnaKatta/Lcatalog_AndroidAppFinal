package com.lucidleanlabs.dev.lcatalog.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class DownloadimageTastFromLocal extends AsyncTask<String, Void, Bitmap> {
    private ImageView bmImage;

    public DownloadimageTastFromLocal(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = "http://192.168.0.7:8080" + urls[0];
        Log.e("DownloadimageTastFromLocal", "Image Location" + urldisplay);
        Bitmap mIcon = null;
        try {

            InputStream in = new URL(urldisplay).openStream();

            mIcon = BitmapFactory.decodeStream(in);

        } catch (Exception e) {

            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            bmImage.setImageBitmap(result);
        }
    }
}