package com.lucidleanlabs.dev.lcatalog_androidapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lucidleanlabs.dev.lcatalog_androidapp.R;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {

    private Activity activity;
    private Context mcontext;

    public GridViewAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_grid, viewGroup, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(GridViewAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.imageView.setImageResource(R.drawable.dummy_icon);
        viewHolder.textView.setText("Article: " + (position + 1));
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "You clicked on Article: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void RecyclerItemClickLstner (Context contect, AdapterView.OnItemClickListener listener){

    }
    @Override
    public int getItemCount() {
        return 20;
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            textView = (TextView) view.findViewById(R.id.item_name);
        }
    }
}
