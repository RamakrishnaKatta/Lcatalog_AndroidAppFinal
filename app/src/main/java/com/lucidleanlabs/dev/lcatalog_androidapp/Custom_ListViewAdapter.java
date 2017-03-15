package com.lucidleanlabs.dev.lcatalog_androidapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


class Custom_ListViewAdapter extends ArrayAdapter<Product> {
    public Custom_ListViewAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (null == v) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.custom_listitem, null);
        }
        Product product = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);
        TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) v.findViewById(R.id.txtDescription);

        assert product != null;
        img.setImageResource(product.getImageId());
        txtTitle.setText(product.getTitle());
        txtDescription.setText(product.getDescription());

        return v;
    }
}
