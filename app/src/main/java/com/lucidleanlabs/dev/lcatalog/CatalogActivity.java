package com.lucidleanlabs.dev.lcatalog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lucidleanlabs.dev.lcatalog.adapters.GridViewAdapter;
import com.lucidleanlabs.dev.lcatalog.adapters.ListViewHorizontalAdapter;
import com.lucidleanlabs.dev.lcatalog.adapters.ListViewVerticalAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CatalogActivity extends AppCompatActivity {

    private static final String REGISTER_URL = "http://35.154.252.64:8080/lll/web/article/all";
    private static final String TAG = "CatalogActivity";

    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_vendors;
    private ArrayList<String> item_images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        final FloatingActionButton fab_grid = (FloatingActionButton) findViewById(R.id.fab_grid);
        final FloatingActionButton fab_vertical_list = (FloatingActionButton) findViewById(R.id.fab_vertical_list);
        final FloatingActionButton fab_horizontal_list = (FloatingActionButton) findViewById(R.id.fab_horizontal_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_catalog);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        item_names = new ArrayList<>();
        item_descriptions = new ArrayList<>();
        item_prices = new ArrayList<>();
        item_discounts = new ArrayList<>();
        item_vendors = new ArrayList<>();
        item_images = new ArrayList<>();

        fab_vertical_list.setSize(1);
        fab_horizontal_list.setSize(1);
        fab_grid.setSize(0);

        try {

            getData();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        fab_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_vertical_list.setSize(1);
                fab_horizontal_list.setSize(1);
                fab_grid.setSize(0);
                try {
                    item_names.clear();
                    item_descriptions.clear();
                    item_prices.clear();
                    item_vendors.clear();
                    item_prices.clear();
                    item_discounts.clear();
                    item_images.clear();

                    getData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        fab_vertical_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_vertical_list.setSize(0);
                fab_horizontal_list.setSize(1);
                fab_grid.setSize(1);
                try {
                    item_names.clear();
                    item_descriptions.clear();
                    item_prices.clear();
                    item_vendors.clear();
                    item_prices.clear();
                    item_discounts.clear();
                    item_images.clear();

                    getData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        fab_horizontal_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_vertical_list.setSize(1);
                fab_horizontal_list.setSize(0);
                fab_grid.setSize(1);
                try {
                    item_names.clear();
                    item_descriptions.clear();
                    item_prices.clear();
                    item_vendors.clear();
                    item_prices.clear();
                    item_discounts.clear();
                    item_images.clear();

                    getData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getData() throws JSONException {

        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...", "Fetching data...", false, false);

        final FloatingActionButton fab_grid = (FloatingActionButton) findViewById(R.id.fab_grid);
        final FloatingActionButton fab_vertical_list = (FloatingActionButton) findViewById(R.id.fab_vertical_list);
        final FloatingActionButton fab_horizontal_list = (FloatingActionButton) findViewById(R.id.fab_horizontal_list);

        final JSONObject baseclass = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REGISTER_URL, baseclass, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "response--" + response);

                try {
                    JSONArray resp = response.getJSONArray("resp");
                    loading.dismiss();

                    if (fab_vertical_list.getSize() == 1 && fab_horizontal_list.getSize() == 1 && fab_grid.getSize() == 0) {
                        gridView(resp);
                    } else if (fab_vertical_list.getSize() == 0 && fab_horizontal_list.getSize() == 1 && fab_grid.getSize() == 1) {
                        verticalRecyclerListView(resp);
                    } else if (fab_vertical_list.getSize() == 1 && fab_horizontal_list.getSize() == 0 && fab_grid.getSize() == 1) {
                        horizontalRecyclerListView(resp);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CatalogActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject request = new JSONObject(res);
                        Log.e(TAG, "request--" + request);
                    } catch (UnsupportedEncodingException | JSONException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


    public void gridView(JSONArray g_jsonArray) {

        RecyclerView horizontal_recycler = (RecyclerView) findViewById(R.id.horizontal_recycler);
        RecyclerView vertical_recycler = (RecyclerView) findViewById(R.id.vertical_recycler);
        RecyclerView grid_recycler = (RecyclerView) findViewById(R.id.grid_recycler);

        grid_recycler.setHasFixedSize(true);

        for (int i = 0; i < g_jsonArray.length(); i++) {
            JSONObject obj = null;
            try {
                obj = g_jsonArray.getJSONObject(i);
                item_names.add(obj.getString("name"));
                item_descriptions.add(obj.getString("description"));
                item_prices.add(obj.getString("price"));
                item_discounts.add(obj.getString("discount"));
                item_vendors.add(obj.getString("vendorId"));
                item_images.add(obj.getString("img"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "names******" + item_names);
        Log.e(TAG, "descriptions******" + item_descriptions);
        Log.e(TAG, "prices******" + item_prices);
        Log.e(TAG, "discounts******" + item_discounts);
        Log.e(TAG, "vendors******" + item_vendors);
        Log.e(TAG, "images******" + item_images);

        GridLayoutManager gridManager = new GridLayoutManager(this, 2);
        grid_recycler.setLayoutManager(gridManager);
        GridViewAdapter gridAdapter = new GridViewAdapter(this, item_names, item_descriptions, item_prices, item_discounts, item_vendors, item_images);
        grid_recycler.setAdapter(gridAdapter);

        if (horizontal_recycler.getVisibility() == View.VISIBLE || vertical_recycler.getVisibility() == View.VISIBLE) {
            horizontal_recycler.setVisibility(View.GONE);
            vertical_recycler.setVisibility(View.GONE);
            grid_recycler.setVisibility(View.VISIBLE);
        }
    }

    public void horizontalRecyclerListView(JSONArray h_jsonArray) {
        RecyclerView horizontal_recycler = (RecyclerView) findViewById(R.id.horizontal_recycler);
        RecyclerView vertical_recycler = (RecyclerView) findViewById(R.id.vertical_recycler);
        RecyclerView grid_recycler = (RecyclerView) findViewById(R.id.grid_recycler);

        horizontal_recycler.setHasFixedSize(true);

        for (int i = 0; i < h_jsonArray.length(); i++) {
            JSONObject obj = null;
            try {
                obj = h_jsonArray.getJSONObject(i);
                item_names.add(obj.getString("name"));
                item_descriptions.add(obj.getString("description"));
                item_prices.add(obj.getString("price"));
                item_discounts.add(obj.getString("discount"));
                item_vendors.add(obj.getString("vendorId"));
                item_images.add(obj.getString("img"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "names******" + item_names);
        Log.e(TAG, "descriptions******" + item_descriptions);
        Log.e(TAG, "prices******" + item_prices);
        Log.e(TAG, "discounts******" + item_discounts);
        Log.e(TAG, "vendors******" + item_vendors);
        Log.e(TAG, "images******" + item_images);

        LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler.setLayoutManager(horizontalManager);
        ListViewHorizontalAdapter horizontalAdapter = new ListViewHorizontalAdapter(this, item_names, item_descriptions, item_prices, item_discounts, item_vendors, item_images);
        horizontal_recycler.setAdapter(horizontalAdapter);

        if (grid_recycler.getVisibility() == View.VISIBLE || vertical_recycler.getVisibility() == View.VISIBLE) {
            grid_recycler.setVisibility(View.GONE);
            vertical_recycler.setVisibility(View.GONE);
            horizontal_recycler.setVisibility(View.VISIBLE);
        }
    }

    public void verticalRecyclerListView(JSONArray v_jsonArray) {
        RecyclerView horizontal_recycler = (RecyclerView) findViewById(R.id.horizontal_recycler);
        RecyclerView vertical_recycler = (RecyclerView) findViewById(R.id.vertical_recycler);
        RecyclerView grid_recycler = (RecyclerView) findViewById(R.id.grid_recycler);

        vertical_recycler.setHasFixedSize(true);

        for (int i = 0; i < v_jsonArray.length(); i++) {
            JSONObject obj = null;
            try {
                obj = v_jsonArray.getJSONObject(i);
                item_names.add(obj.getString("name"));
                item_descriptions.add(obj.getString("description"));
                item_prices.add(obj.getString("price"));
                item_discounts.add(obj.getString("discount"));
                item_vendors.add(obj.getString("vendorId"));
                item_images.add(obj.getString("img"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "names******" + item_names);
        Log.e(TAG, "descriptions******" + item_descriptions);
        Log.e(TAG, "prices******" + item_prices);
        Log.e(TAG, "images******" + item_images);
        Log.e(TAG, "discounts******" + item_discounts);
        Log.e(TAG, "vendors******" + item_vendors);

        LinearLayoutManager verticalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        vertical_recycler.setLayoutManager(verticalManager);
        ListViewVerticalAdapter verticalAdapter = new ListViewVerticalAdapter(this, item_names, item_descriptions, item_prices, item_discounts, item_vendors, item_images);
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
