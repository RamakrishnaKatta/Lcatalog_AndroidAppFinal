package com.lucidleanlabs.dev.lcatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lucidleanlabs.dev.lcatalog.ProductPage;
import com.lucidleanlabs.dev.lcatalog.R;

public class ListViewHorizontalAdapter extends RecyclerView.Adapter<ListViewHorizontalAdapter.ViewHolder> {

    private Activity activity;

    public ListViewHorizontalAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_horizontal_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHorizontalAdapter.ViewHolder viewHolder, final int position) {

        final Context[] context = new Context[1];

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context[0] = v.getContext();

                Intent intent = new Intent(context[0], ProductPage.class);
                context[0].startActivity(intent);

                Toast.makeText(activity, "Position clicked: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    /**
     * View holder to display each RecylerView item
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout linearLayout;

        ViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.layout);
        }
    }
}
