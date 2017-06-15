package com.lucidleanlabs.dev.lcatalog.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import com.lucidleanlabs.dev.lcatalog.IntroductionActivity;
import com.lucidleanlabs.dev.lcatalog.OverviewActivity;
import com.lucidleanlabs.dev.lcatalog.ProductDetails;
import com.lucidleanlabs.dev.lcatalog.ProductPageActivity;
import com.lucidleanlabs.dev.lcatalog.Product_new;

public class CatalogAdapter extends FragmentStatePagerAdapter {


    String a_name, a_description, a_old_price, a_discount, a_newPrice, a_dimensions, a_width, a_height, a_length, a_position,
            a_id, a_article_images;

    int mNumOfTabs;

    public CatalogAdapter(FragmentManager fragmentManager, int tabCount,
                          String name, String description, String oldPrice,
                          String discount, String newPrice, String dimensions,
                          String width, String height, String length,
                          String position, String id, String article_images) {
        super(fragmentManager);
        this.mNumOfTabs = tabCount;
        this.a_name = name;
        this.a_description = description;
        this.a_newPrice = newPrice;
        this.a_old_price = oldPrice;
        this.a_discount = discount;
        this.a_dimensions = dimensions;
        this.a_height = height;
        this.a_length = length;
        this.a_width = width;
        this.a_article_images = article_images;
        this.a_position = position;
        this.a_id = id;

        Log.e("", "" + a_name + a_dimensions + a_description + a_newPrice);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle b_tab1 = new Bundle();
                b_tab1.putString("article_images", a_article_images);
                b_tab1.putString("article_id",a_id);

                Product_new tab1 = new Product_new();
                tab1.setArguments(b_tab1);
                return tab1;
            case 1:
                Bundle b_tab2 = new Bundle();
                b_tab2.putString("article_title",a_name);
                b_tab2.putString("article_description",a_description);
                b_tab2.putString("article_price",a_newPrice);
                b_tab2.putString("article_title",a_old_price);
                b_tab2.putString("article_discount",a_discount);
                b_tab2.putString("article_dimensions",a_dimensions);
                b_tab2.putString("height",a_height);
                b_tab2.putString("width",a_width);
                b_tab2.putString("length",a_length);
                b_tab2.putString("article_position",a_position);

                ProductDetails tab2 = new ProductDetails();
                tab2.setArguments(b_tab2);
                return tab2;
            default:
                return null;
        }
    }
}
