package com.lucidleanlabs.dev.lcatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lucidleanlabs.dev.lcatalog.ProductPageActivity;
import com.lucidleanlabs.dev.lcatalog.R;
import com.lucidleanlabs.dev.lcatalog.utils.DownloadImageTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainListViewAdapter extends RecyclerView.Adapter<MainListViewAdapter.ViewHolder> {

    ImageButton readmore;

    private static final String TAG = "MainListViewAdapter";

    private Activity activity;

    private ArrayList<String> item_names;
    private ArrayList<String> image_one;
    private ArrayList<String> image_two;
    private ArrayList<String> image_three;
    private ArrayList<String> image_four;
    private ArrayList<String> image_share;
    private ArrayList<String> Click_readmore;

    public MainListViewAdapter(Activity activity,
                               ArrayList<String> item_names,
                               ArrayList<String> image_one,
                               ArrayList<String> image_two,
                               ArrayList<String> image_three,
                               ArrayList<String> image_four,
                               ArrayList<String> image_share,
                               ImageButton click_more) {

        this.item_names = item_names;
        this.image_one = image_one;
        this.image_two = image_two;
        this.image_three = image_three;
        this.image_four = image_four;

        this.activity = activity;

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView1, imageView2, imageView3, imageView4;
        ImageButton share, readmore;
        TextView item_name;

        private LinearLayout main_container;

        ViewHolder(View itemView) {
            super(itemView);

            main_container = (LinearLayout) itemView.findViewById(R.id.main_container);
            imageView1 = (ImageView) itemView.findViewById(R.id.main_image1);
            imageView2 = (ImageView) itemView.findViewById(R.id.main_image2);
            imageView3 = (ImageView) itemView.findViewById(R.id.main_image3);
            imageView4 = (ImageView) itemView.findViewById(R.id.main_image4);
            readmore = (ImageButton) itemView.findViewById(R.id.read_more);

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.content_display, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainListViewAdapter.ViewHolder holder, final int position) {

        final Context[] context = new Context[1];

        holder.imageView1.setImageResource(R.drawable.dummy_icon);
        holder.imageView2.setImageResource(R.drawable.furniture);
        holder.imageView3.setImageResource(R.drawable.livingroom);
        holder.imageView4.setImageResource(R.drawable.furniture);
        String im1 = null;
        String get_image = image_one.get(position);
        try {
            JSONObject image_json = new JSONObject("images");
           // JSONObject images_json = new JSONObject(get_image);
            im1 = image_json.getString("image1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new DownloadImageTask(holder.imageView1).execute(im1);
//        im2 = null,im3 = null,im4 = null;
//        String get_image1 = image_one.get(position);
//        String get_image2 = image_two.get(position);
//        String get_image3 = image_three.get(position);
//        String get_image4 = image_four.get(position);
//        try {
//            JSONObject images  = new JSONObject("article_images");
//
//            JSONObject images_json1 = new JSONObject(get_image1);
//            JSONObject images_json2 = new JSONObject(get_image2);
//            JSONObject images_json3 = new JSONObject(get_image3);
//            JSONObject images_json4 = new JSONObject(get_image4);
//            im1 = images_json1.getString("image1");
//            im2 = images_json2.getString("image2");
//            im3 = images_json3.getString("image3");
//            im4 = images_json4.getString("image4");
//
//            Log.e(TAG,"image1"+im1);
//            Log.e(TAG,"image2"+im2);
//            Log.e(TAG,"image3"+im3);
//            Log.e(TAG,"image4"+im4);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        new DownloadImageTask(holder.imageView1).execute(im1);
//        new DownloadImageTask(holder.imageView2).execute(im2);
//        new DownloadImageTask(holder.imageView3).execute(im3);
//        new DownloadImageTask(holder.imageView4).execute(im4);

        holder.readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context[0] = v.getContext();

                Intent intent = new Intent(context[0], ProductPageActivity.class);
                Bundle b = new Bundle();
                b.putString("images", image_one.get(position));

                intent.putExtras(b);
                context[0].startActivity(intent);

            }
        });
    }
    @Override
    public int getItemCount() {
        return item_names.size();
    }


}
