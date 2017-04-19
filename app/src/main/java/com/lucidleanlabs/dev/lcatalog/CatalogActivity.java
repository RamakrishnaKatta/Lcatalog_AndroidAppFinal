package com.lucidleanlabs.dev.lcatalog;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.lucidleanlabs.dev.lcatalog.adapters.GridViewAdapter;
import com.lucidleanlabs.dev.lcatalog.adapters.ListViewHorizontalAdapter;
import com.lucidleanlabs.dev.lcatalog.adapters.ListViewVerticalAdapter;

public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_catalog);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        gridView();

        final FloatingActionButton fab_grid = (FloatingActionButton) findViewById(R.id.fab_grid);
        final FloatingActionButton fab_vertical_list = (FloatingActionButton) findViewById(R.id.fab_vertical_list);
        final FloatingActionButton fab_horizontal_list = (FloatingActionButton) findViewById(R.id.fab_horizontal_list);

        fab_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v, "You Choose Grid View", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                gridView();
                fab_vertical_list.setSize(1);
                fab_horizontal_list.setSize(1);
                fab_grid.setSize(0);
            }
        });

        fab_vertical_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verticalRecyclerListView();
                fab_vertical_list.setSize(0);
                fab_horizontal_list.setSize(1);
                fab_grid.setSize(1);
            }
        });

        fab_horizontal_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalRecyclerListView();
                fab_vertical_list.setSize(1);
                fab_horizontal_list.setSize(0);
                fab_grid.setSize(1);
            }
        });
    }

    public void gridView() {
        RecyclerView horizontal_recycler = (RecyclerView) findViewById(R.id.horizontal_recycler);
        RecyclerView vertical_recycler = (RecyclerView) findViewById(R.id.vertical_recycler);
        RecyclerView grid_recycler = (RecyclerView) findViewById(R.id.grid_recycler);
        grid_recycler.setHasFixedSize(true);

        GridLayoutManager gridManager = new GridLayoutManager(this, 2);
        grid_recycler.setLayoutManager(gridManager);
        GridViewAdapter gridAdapter = new GridViewAdapter(this);
        grid_recycler.setAdapter(gridAdapter);

        if (horizontal_recycler.getVisibility() == View.VISIBLE || vertical_recycler.getVisibility() == View.VISIBLE) {
            horizontal_recycler.setVisibility(View.GONE);
            vertical_recycler.setVisibility(View.GONE);
            grid_recycler.setVisibility(View.VISIBLE);
        }
    }

    public void horizontalRecyclerListView() {
        RecyclerView grid_recycler = (RecyclerView) findViewById(R.id.grid_recycler);
        RecyclerView horizontal_recycler = (RecyclerView) findViewById(R.id.horizontal_recycler);
        RecyclerView vertical_recycler = (RecyclerView) findViewById(R.id.vertical_recycler);
        horizontal_recycler.setHasFixedSize(true);

        LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler.setLayoutManager(horizontalManager);
        ListViewHorizontalAdapter horizontalAdapter = new ListViewHorizontalAdapter(this);
        horizontal_recycler.setAdapter(horizontalAdapter);

        if (grid_recycler.getVisibility() == View.VISIBLE || vertical_recycler.getVisibility() == View.VISIBLE) {
            grid_recycler.setVisibility(View.GONE);
            vertical_recycler.setVisibility(View.GONE);
            horizontal_recycler.setVisibility(View.VISIBLE);
        }
    }

    public void verticalRecyclerListView() {
        RecyclerView grid_recycler = (RecyclerView) findViewById(R.id.grid_recycler);
        RecyclerView horizontal_recycler = (RecyclerView) findViewById(R.id.horizontal_recycler);
        RecyclerView vertical_recycler = (RecyclerView) findViewById(R.id.vertical_recycler);
        vertical_recycler.setHasFixedSize(true);

        LinearLayoutManager verticalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        vertical_recycler.setLayoutManager(verticalManager);
        ListViewVerticalAdapter verticalAdapter = new ListViewVerticalAdapter(this);
        vertical_recycler.setAdapter(verticalAdapter);

        if (grid_recycler.getVisibility() == View.VISIBLE || horizontal_recycler.getVisibility() == View.VISIBLE) {
            grid_recycler.setVisibility(View.GONE);
            horizontal_recycler.setVisibility(View.GONE);
            vertical_recycler.setVisibility(View.VISIBLE);
        }
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
