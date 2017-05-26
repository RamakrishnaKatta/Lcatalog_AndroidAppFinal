package com.lucidleanlabs.dev.lcatalog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class UserTypeActivity extends AppCompatActivity {

    private static final String TAG = "UserTypeActivity";
    private static final int REQUEST_USERTYPE = 0;

    TextView app_name, welcome_aboard, who_are_you, powered;
    //    TextView customer, new_customer, shopper;
    Button _customer, _newCustomer, _shopper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

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
                Intent intent = new Intent(UserTypeActivity.this, LoginActivity.class);
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
}
