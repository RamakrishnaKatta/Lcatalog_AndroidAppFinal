package com.lucidleanlabs.dev.lcatalog;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class UserTypeActivity extends AppCompatActivity {

    private static final String TAG = "UserTypeActivity";
    private static final int MY_PERMISSIONS_REQUEST = 10;
    private static final int REQUEST_USERTYPE = 0;

    TextView app_name, welcome_aboard, who_are_you, powered;
    //    TextView customer, new_customer, shopper;
    Button _customer, _newCustomer, _shopper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        RequestPermissions();

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

//        customer = (TextView) findViewById(R.id.txt_customer);
//        customer.setTypeface(custom_font2);
//
//        new_customer = (TextView) findViewById(R.id.txt_new_customer);
//        new_customer.setTypeface(custom_font2);
//
//        shopper = (TextView) findViewById(R.id.txt_shopper);
//        shopper.setTypeface(custom_font2);

        _customer = (Button) findViewById(R.id.btn_customer);
        _customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserTypeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        _newCustomer = (Button) findViewById(R.id.btn_new_customer);
        _newCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserTypeActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        _shopper = (Button) findViewById(R.id.btn_shopper);
        _shopper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserTypeActivity.this, GuestActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //Disables the keyboard to appear on the activity launch
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

}
