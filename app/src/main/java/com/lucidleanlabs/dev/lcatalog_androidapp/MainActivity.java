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

            RecyclerView gridView = (RecyclerView) findViewById(R.id.grid_recycler);
            gridView.setHasFixedSize(true);

            GridLayoutManager gridManager = new GridLayoutManager(this, 2);
            gridView.setLayoutManager(gridManager);
            GridViewAdapter gridAdapter = new GridViewAdapter(this);
            gridView.setAdapter(gridAdapter);


            RelativeLayout grid_layout = (RelativeLayout) findViewById(R.id.grid_layout);
            RelativeLayout horizontal_view = (RelativeLayout) findViewById(R.id.horizontal_layout);
            RelativeLayout vertical_view = (RelativeLayout) findViewById(R.id.vertical_layout);

            if(horizontal_view.getVisibility() == View.VISIBLE || vertical_view.getVisibility() == View.VISIBLE){
                horizontal_view.setVisibility(View.GONE);
                vertical_view.setVisibility(View.GONE);
                grid_layout.setVisibility(View.VISIBLE);
            }

        } else if (id == R.id.nav_gallery) {

            RecyclerView horizontalList = (RecyclerView) findViewById(R.id.horizontal_recycler);
            horizontalList.setHasFixedSize(true);

            LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            horizontalList.setLayoutManager(horizontalManager);
            ListViewHorizontalAdapter horizontalAdapter = new ListViewHorizontalAdapter(this);
            horizontalList.setAdapter(horizontalAdapter);

            RelativeLayout grid_layout = (RelativeLayout) findViewById(R.id.grid_layout);
            RelativeLayout horizontal_view = (RelativeLayout) findViewById(R.id.horizontal_layout);
            RelativeLayout vertical_view = (RelativeLayout) findViewById(R.id.vertical_layout);

            if(grid_layout.getVisibility() == View.VISIBLE || vertical_view.getVisibility() == View.VISIBLE){
                grid_layout.setVisibility(View.GONE);
                vertical_view.setVisibility(View.GONE);
                horizontal_view.setVisibility(View.VISIBLE);
            }

        } else if (id == R.id.nav_slideshow) {

            RecyclerView verticalList = (RecyclerView) findViewById(R.id.vertical_recycler);
            verticalList.setHasFixedSize(true);

            LinearLayoutManager verticalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            verticalList.setLayoutManager(verticalManager);
            ListViewVerticalAdapter verticalAdapter = new ListViewVerticalAdapter(this);
            verticalList.setAdapter(verticalAdapter);

            RelativeLayout grid_layout = (RelativeLayout) findViewById(R.id.grid_layout);
            RelativeLayout horizontal_view = (RelativeLayout) findViewById(R.id.horizontal_layout);
            RelativeLayout vertical_view = (RelativeLayout) findViewById(R.id.vertical_layout);

            if(grid_layout.getVisibility() == View.VISIBLE || horizontal_view.getVisibility() == View.VISIBLE){
                grid_layout.setVisibility(View.GONE);
                horizontal_view.setVisibility(View.GONE);
                vertical_view.setVisibility(View.VISIBLE);
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
