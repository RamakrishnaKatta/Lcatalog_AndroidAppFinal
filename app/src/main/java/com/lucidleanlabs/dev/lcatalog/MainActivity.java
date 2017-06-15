package com.lucidleanlabs.dev.lcatalog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.lucidleanlabs.dev.lcatalog.adapters.MainPageAdapter;
import com.lucidleanlabs.dev.lcatalog.ar.ARNativeActivity;
import com.lucidleanlabs.dev.lcatalog.utils.PrefManager;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private PrefManager prefManager3;
    private static final String TAG = "MainActivity";

    boolean doubleBackToExitPressedOnce = false;

//    public static int notificationCount = 0;

    String name, email, phone, address, user_log_type;
    String guest_name, guest_phone;
    TextView user_type, user_email, user_name, app_name, powered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("ILLUSTRATION"));
        tabLayout.addTab(tabLayout.newTab().setText("OVERVIEW"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final MainPageAdapter adapter = new MainPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
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
        Log.d(TAG, "Dummy -- " + guest_data);

        guest_name = guest_data.getString("guest_name");
        Log.e(TAG, "guest name:  " + guest_name);

        guest_phone = guest_data.getString("guest_phone");
        Log.e(TAG, "guest phone:  " + guest_phone);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        app_name = (TextView) header.findViewById(R.id.application_name);
        powered = (TextView) header.findViewById(R.id.lucidleanlabs);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Cookie-Regular.ttf");
        app_name.setTypeface(custom_font);
        powered.setTypeface(custom_font2);

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

        prefManager3 = new PrefManager(this);
        Log.e(TAG, "" + prefManager3.MainActivityScreenLaunch());
        if (prefManager3.MainActivityScreenLaunch()) {
            ShowcaseView();
        }

        checkInternetConnection();
    }


    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getActiveNetworkInfo().getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getActiveNetworkInfo().getState() == android.net.NetworkInfo.State.CONNECTING) {

            // if connected with internet
            return true;

        } else if (
                connec.getActiveNetworkInfo().getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getActiveNetworkInfo().getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            Toast.makeText(this, " Internet Not Available  ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }


    /*showcaseview for the MainActivity(Notifications and Welcome screen*/
    private void ShowcaseView() {
        prefManager3.SetMainActivityScreenLaunch(false);
        Log.e(TAG, "" + prefManager3.MainActivityScreenLaunch());

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
        final Display display = getWindowManager().getDefaultDisplay();

        final TapTargetSequence sequence = new TapTargetSequence(this).targets(
                TapTarget.forToolbarMenuItem(toolbar, R.id.action_notifications, "Notifications", "All the notifications can be displayed Here")
                        .transparentTarget(true)
                        .outerCircleColor(R.color.primary_dark)
                        .targetRadius(25)
                        .textColor(R.color.white)
                        .tintTarget(true)
                        .id(1),
                TapTarget.forToolbarMenuItem(toolbar, R.id.action_replay_info, "welcome", "If u miss the welcome screen you can see here ")
                        .transparentTarget(true)
                        .textColor(R.color.white)
                        .targetRadius(25)
                        .tintTarget(true)
                        .outerCircleColor(R.color.primary_dark)
                        .id(2))
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() { }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {  }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {  }
                });
        sequence.start();

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
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(0);
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
            startActivity(new Intent(MainActivity.this, NotifyActivity.class));
            return true;

        } else if (id == R.id.action_replay_info) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Watch the welcome Slider, If you missed it");
            builder.setMessage("To see the welcome slider again, either you can go to Settings -> apps -> welcome slider -> clear data or Press OK ");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // We normally won't show the welcome slider again in real app but this is for testing

                    PrefManager prefManager = new PrefManager(getApplicationContext());

                    // make first time launch TRUE
                    prefManager.SetWelcomeActivityScreenLaunch(true);

                    startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
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

        } else if (id == R.id.nav_user_notify) {

            Toast.makeText(this, "here are all your notifications, Check out !!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, NotifyActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_sign_up) {

            Toast.makeText(this, "Thanks for your thought on Creating an Account, Appreciated !!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Successfully Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, UserTypeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {

            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
