package com.lucidleanlabs.dev.lcatalog;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
import com.lucidleanlabs.dev.lcatalog.adapters.MainListViewAdapter;
import com.lucidleanlabs.dev.lcatalog.ar.ARNativeActivity;
import com.lucidleanlabs.dev.lcatalog.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST = 10;
    private static final String TAG = "MainActivity";

    private static final String REGISTER_URL = "http://35.154.252.64:8080/lll/web/article/all";

    boolean doubleBackToExitPressedOnce = false;

//    public static int notificationCount = 0;

    String name, email, phone, address, user_log_type;

    TextView user_type, user_email, user_name, app_name;

    private ArrayList<String> item_names;
    private ArrayList<String> item_images;
    private ArrayList<String> item_ids;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestPermissions();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final Bundle user_data = getIntent().getExtras();
        Log.d(TAG, "Dummy -- " + user_data);

        name = user_data.getString("name");
        Log.e(TAG, "name:  " + name);

        address = user_data.getString("address");
        Log.e(TAG, "address:  " + address);

        email = user_data.getString("email");
        Log.e(TAG, "email:  " + email);

        phone = user_data.getString("phone");
        Log.e(TAG, "phone:  " + phone);

        final Bundle guest_data = getIntent().getExtras();
        Log.d(TAG, "Dummy -- " + user_data);

        String guest_name = guest_data.getString("guest_name");
        Log.e(TAG, "guest name:  " + guest_name);
        String guest_phone = guest_data.getString("guest_phone");
        Log.e(TAG, "guest phone:  " + guest_phone);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        app_name = (TextView) header.findViewById(R.id.application_name);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        app_name.setTypeface(custom_font);

        user_name = (TextView) header.findViewById(R.id.user_name);
        if (name != null) {
            user_name.setText(name);
        } else {
            user_name.setText(guest_name);
        }

        user_type = (TextView) header.findViewById(R.id.guest_image);
        user_email = (TextView) header.findViewById(R.id.user_email);
        if (email != null) {
            user_email.setText(email);
            user_type.setText("CUSTOMER");
        } else {
            user_email.setText("Phone No: " + guest_phone);
            user_type.setText("GUEST");
        }

        item_names = new ArrayList<>();
        item_images = new ArrayList<>();
        item_ids = new ArrayList<>();

        try {
            getData();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getData() throws JSONException {

        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...", "Fetching data...", false, false);

        final JSONObject baseclass = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REGISTER_URL, baseclass, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "response--" + response);

                try {
                    JSONArray resp = response.getJSONArray("resp");
                    loading.dismiss();
                    mainRecyclerView(resp);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    private void mainRecyclerView(JSONArray m_jsonArray) {

        RecyclerView main_recycler = (RecyclerView) findViewById(R.id.main_recycler);
        main_recycler.setHasFixedSize(true);

        for (int i = 0; i < m_jsonArray.length(); i++) {
            JSONObject obj = null;
            try {
                obj = m_jsonArray.getJSONObject(i);

                item_ids.add(obj.getString("id"));
                item_images.add(obj.getString("images"));
                item_names.add(obj.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(TAG, "ids******" + item_ids);
            Log.e(TAG, "images******" + item_images);
            Log.e(TAG, "names******" + item_names);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            main_recycler.setLayoutManager(linearLayoutManager);
            MainListViewAdapter gridAdapter = new MainListViewAdapter(this, item_ids, item_names, item_images);
            main_recycler.setAdapter(gridAdapter);
        }
    }

    /*Permissions Required for the app and granted*/

    private void RequestPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) +
                +ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

         /*    Show an explanation to the user *asynchronously* -- don't block this thread waiting for the user's response!
                After the user sees the explanation, try again to request the permission.*/

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA
                                , Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);

               /*  MY_PERMISSIONS_REQUEST is an app-defined int constant. The callback method gets the result of the request.*/
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the tasks you need to do.
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
            }
            // other 'case' lines to check for other permissions this app might request
        }
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Get the notifications MenuItem and its LayerDrawable (layer-list)

//        MenuItem item = menu.findItem(R.id.action_notifications);
//        NotificationCountSetClass.setAddToCart(MainActivity.this, item, notificationCount);

        // force the ActionBar to relayout its MenuItems. onCreateOptionsMenu(Menu) will be called again.

        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will automatically handle clicks on the
        // Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notifications) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Watch the welcome Slider, If you missed it");
            builder.setMessage("To see the welcome slider again, either you can go to Settings -> apps -> welcome slider -> clear data or Press OK ");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // We normally won't show the welcome slider again in real app but this is for testing

                    PrefManager prefManager = new PrefManager(getApplicationContext());

                    // make first time launch TRUE
                    prefManager.setFirstTimeLaunch(true);

                    startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();

//            startActivity(new Intent(this, NotificationsActivity.class));

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

            Intent intent = new Intent(this, CatalogActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_augment) {

            Intent intent = new Intent(this, ARNativeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, GalleryActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_user_account) {

            Bundle user_details = new Bundle();

            user_log_type = user_type.getText().toString().trim();

            user_details.putString("user_email", email);
            user_details.putString("user_name", name);
            user_details.putString("user_address", address);
            user_details.putString("user_phone", phone);
            user_details.putString("user_type", user_log_type);

            if (Objects.equals(user_log_type, "CUSTOMER")) {

                Toast.makeText(this, "This is Your Profile !!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, UserAccountActivity.class).putExtras(user_details);
                startActivity(intent);
            } else {
                Toast.makeText(this, "You are a Guest, You dont possess an Account !! Thanks and try Signing up ", Toast.LENGTH_SHORT).show();
            }


        } else if (id == R.id.nav_camera) {

            Toast.makeText(this, "You are now entering the custom camera !!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_ven_reg) {

            Toast.makeText(this, "We will not disappoint you, Lets get in Touch !!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, VendorRegistrationActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_sign_up) {

            Toast.makeText(this, "Thanks for your thought on Creating an Account, Appreciated !!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Successfully Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {

            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
