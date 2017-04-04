package com.lucidleanlabs.dev.lcatalog_androidapp.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lucidleanlabs.dev.lcatalog_androidapp.R;

import java.util.ArrayList;


public class ImagesliderAdapter extends PagerAdapter {

    private ArrayList<Integer> Images;
//    private int[] img ={R.drawable.background,R.drawable.dummy_icon};
    private LayoutInflater inflater;
    private Context context;
    public ImagesliderAdapter(Context context, ArrayList<Integer> Images){
        this.context = context;
        this.Images = Images;
        inflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return Images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position){
    inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_product_page,container,false);
        ImageView images= (ImageView)v.findViewById(R.id.product_image);
        images.setImageResource(Images.get(position));
        container.addView(v);
        return v;

    }
    @Override
    public void destroyItem(ViewGroup container,int position,Object object){
        container.invalidate();
    }

}