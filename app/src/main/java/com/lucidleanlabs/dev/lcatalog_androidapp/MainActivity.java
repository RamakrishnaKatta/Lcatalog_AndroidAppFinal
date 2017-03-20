package com.lucidleanlabs.dev.lcatalog_androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.lucidleanlabs.dev.lcatalog_androidapp.adapters.GridViewAdapter;
import com.lucidleanlabs.dev.lcatalog_androidapp.adapters.ListViewHorizontalAdapter;
import com.lucidleanlabs.dev.lcatalog_androidapp.adapters.ListViewVerticalAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                frameLayout.getBackground().setAlpha(240);
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem Nav_item) {
        // Handle navigation view item clicks here.
        int id = Nav_item.getItemId();

        if (id == R.id.nav_catalog) {

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

        } else if (id == R.id.nav_gallery) {

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

        } else if (id == R.id.nav_slideshow) {

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


        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
