package com.lucidleanlabs.dev.lcatalog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.lucidleanlabs.dev.lcatalog.utils.PrefManager;

import java.io.File;

public class UserTypeActivity extends AppCompatActivity {

    private static final String TAG = "UserTypeActivity";
    private static final int MY_PERMISSIONS_REQUEST = 10;
    private static final int REQUEST_USERTYPE = 0;

    TextView app_name, welcome_aboard, who_are_you, powered;
    //    TextView customer, new_customer, shopper;
    ImageButton _customer, _newCustomer, _shopper;

    private PrefManager prefManager1;
    private boolean success = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        RequestPermissions();
        CreateFolderStructure();

        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Cookie-Regular.ttf");

        app_name = (TextView) findViewById(R.id.application_name);
        app_name.setTypeface(custom_font1);

        welcome_aboard = (TextView) findViewById(R.id.welcome_aboard);
        welcome_aboard.setTypeface(custom_font2);

        who_are_you = (TextView) findViewById(R.id.who_are_you);
        who_are_you.setTypeface(custom_font2);

        powered = (TextView) findViewById(R.id.lucidleanlabs);
        powered.setTypeface(custom_font2);

        _customer = (ImageButton) findViewById(R.id.btn_customer);
        _customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserTypeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        _newCustomer = (ImageButton) findViewById(R.id.btn_new_customer);
        _newCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserTypeActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        _shopper = (ImageButton) findViewById(R.id.btn_shopper);
        _shopper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserTypeActivity.this, GuestActivity.class);
                startActivity(intent);
                finish();
            }
        });

        prefManager1 = new PrefManager(this);
        Log.e(TAG, "" + prefManager1.isFirstTimeLaunchScreen1());
        if (prefManager1.isFirstTimeLaunchScreen1()) {
            ShowcaseView();
        }

        //Disables the keyboard to appear on the activity launch
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        checkInternetConnection();
    }

    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

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

    private void CreateFolderStructure() {
        String root_Path = Environment.getExternalStorageDirectory().toString() + "//L_CATALOGUE";
        String models_Path = Environment.getExternalStorageDirectory().toString() + "//L_CATALOGUE/Models";
        String screenshots_Path = Environment.getExternalStorageDirectory().toString() + "//L_CATALOGUE/Screenshots";
        String cache_Path = Environment.getExternalStorageDirectory().toString() + "//L_CATALOGUE/cache";

        File Root_Folder, Models_Folder, Screenshots_Folder, Cache_Folder;

        if (Environment.getExternalStorageState().contains(Environment.MEDIA_MOUNTED)) {
            Root_Folder = new File(root_Path);
            Models_Folder = new File(models_Path);
            Screenshots_Folder = new File(screenshots_Path);
            Cache_Folder = new File(cache_Path);
        } else {
            Root_Folder = new File(root_Path);
            Models_Folder = new File(models_Path);
            Screenshots_Folder = new File(screenshots_Path);
            Cache_Folder = new File(cache_Path);
        }

        if (Root_Folder.exists()) {
            Toast.makeText(getBaseContext(), "Welcome Back !!", Toast.LENGTH_SHORT).show();
        } else {

            if (!Root_Folder.exists()) {
                success = Root_Folder.mkdirs();
            }
            if (!Models_Folder.exists()) {
                success = Models_Folder.mkdirs();
            }
            if (!Screenshots_Folder.exists()) {
                success = Screenshots_Folder.mkdirs();
            }
            if (!Cache_Folder.exists()) {
                success = Cache_Folder.mkdirs();
            }
            if (success) {
                Toast.makeText(getBaseContext(), "Get Set Go !!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /*Showcaseview for the Signup, Login, Guest*/
    private void ShowcaseView() {
        prefManager1.setFirstTimeLaunchScreen1(false);
        Log.e(TAG, "" + prefManager1.isFirstTimeLaunchScreen1());

        final TapTargetSequence sequence = new TapTargetSequence(this).targets(
                TapTarget.forView(findViewById(R.id.btn_customer), "Login", "Click here if you visited before")
                        .cancelable(false)

                        .tintTarget(false)
                        .textColor(R.color.white)
                        .id(1),
                TapTarget.forView(findViewById(R.id.btn_shopper), "Guest", "Click here if you are Guest visitor")
                        .cancelable(false)
                        .tintTarget(false)
                        .id(2)
        ).listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {

            }
        });
        sequence.start();
        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.btn_new_customer), "Click here If you are new   ")
                        .cancelable(false)
                        .tintTarget(false)
                        .textColor(R.color.white)
                , new TapTargetView.Listener() {

                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                    }
                });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Alert");
        builder.setMessage("Press OK to get out of this App");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(0);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_USERTYPE) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

     /*Permissions Required for the app and granted*/

    private void RequestPermissions() {
        if (ContextCompat.checkSelfPermission(UserTypeActivity.this, android.Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(UserTypeActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) +
                +ContextCompat.checkSelfPermission(UserTypeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(UserTypeActivity.this, android.Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(UserTypeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(UserTypeActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

         /*    Show an explanation to the user *asynchronously* -- don't block this thread waiting for the user's response!
                After the user sees the explanation, try again to request the permission.*/

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(UserTypeActivity.this,
                        new String[]{android.Manifest.permission.CAMERA
                                , android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
