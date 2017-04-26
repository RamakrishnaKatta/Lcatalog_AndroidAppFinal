package com.lucidleanlabs.dev.lcatalog.adapters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lucidleanlabs.dev.lcatalog.R;
import com.lucidleanlabs.dev.lcatalog.ScreenshotActivity;

import java.util.ArrayList;


public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ViewHolder> {
    private static final String TAG = "ImageviewAdapter";

    private ArrayList<String> f = new ArrayList<String>();
    LayoutInflater inflater;
    private ScreenshotActivity screenshotActivity;


    public ImageViewAdapter(ScreenshotActivity screenshotActivity, ArrayList<String> f) {
        this.screenshotActivity = screenshotActivity;
        this.f = f;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = screenshotActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.gallery_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewAdapter.ViewHolder holder, final int position) {
        Bitmap bitmap = BitmapFactory.decodeFile(f.get(position));
        holder.imageView.setImageBitmap(bitmap);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "Items count of products" + f);
        return f.size();
    }

    /**
     * View holder to display each RecylerView item
     */

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.thumbImage);
        }
    }
}
