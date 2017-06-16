package com.lucidleanlabs.dev.lcatalog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.lucidleanlabs.dev.lcatalog.utils.CustomMessage;
import com.lucidleanlabs.dev.lcatalog.utils.NetworkConnectivity;
import com.lucidleanlabs.dev.lcatalog.utils.PrefManager;
import com.lucidleanlabs.dev.lcatalog.utils.UserCheckUtil;

import java.io.File;

public class GuestActivity extends AppCompatActivity {

    private PrefManager prefManager2;

    private static final String TAG = "GuestActivity";
    private static final int REQUEST_GUEST_LOGIN = 0;

    TextView app_name, powered;
    EditText _guestNameText, _GuestPhoneText;
    Button _guestLoginButton;
    EditText _nameText, _mobileText;
    ImageButton get_details;

    String guest_name, guest_phone;
    File file_guest;
    String[] text_from_guest_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);


        app_name = (TextView) findViewById(R.id.application_name);
        powered = (TextView) findViewById(R.id.lucidleanlabs);
        _guestLoginButton = (Button) findViewById(R.id.btn_guest);
        get_details = (ImageButton) findViewById(R.id.btn_get_data);


        String guest_text_file_location = Environment.getExternalStorageDirectory() + "/L_CATALOGUE/guest.txt";
        file_guest = new File(guest_text_file_location);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Cookie-Regular.ttf");

        app_name.setTypeface(custom_font);
        powered.setTypeface(custom_font2);

        //Disables the keyboard to appear on the activity launch
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_guest);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(GuestActivity.this, UserTypeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        get_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
                _guestLoginButton.setEnabled(true);
            }
        });

        _guestLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guest_login();
            }
        });

        prefManager2 = new PrefManager(this);
        Log.e(TAG, "" + prefManager2.GuestActivityScreenLaunch());
        if (prefManager2.GuestActivityScreenLaunch()) {
            ShowcaseView();
        }

        if (NetworkConnectivity.checkInternetConnection(GuestActivity.this)){

        }else {
            InternetMessage();
        }
    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view,"Check Your Internet connection",Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(R.color.red));
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                if (NetworkConnectivity.checkInternetConnection(GuestActivity.this)) {
                } else {
                    InternetMessage();
                }
            }
        });
        snackbar.show();
    }

    private void ShowcaseView() {
        prefManager2.setGuestActivityScreenLaunch(false);
        Log.e(TAG, "" + prefManager2.GuestActivityScreenLaunch());

        final Display display = getWindowManager().getDefaultDisplay();
        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.btn_get_data), "Click here to autofill your recent credentials ")
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

    private void setData() {
        if (!file_guest.exists()) {
            CustomMessage.getInstance().CustomMessage(GuestActivity.this, "No Saved Details Yet");

        } else {
            text_from_guest_file = UserCheckUtil.readFromFile("guest").split(" ### ");

            _guestNameText = (EditText) findViewById(R.id.input_name);
            _guestNameText.setText(text_from_guest_file[0].trim());

            _GuestPhoneText = (EditText) findViewById(R.id.input_mobile);
            _GuestPhoneText.setText(text_from_guest_file[1].trim());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    public void guest_login() {
        Log.e(TAG, "Guest Customer Login");

        if (!validateGuest()) {
            onLoginFailed();
            return;
        }

        _guestLoginButton = (Button) findViewById(R.id.btn_guest);
        _guestLoginButton.setEnabled(false);

        _guestNameText = (EditText) findViewById(R.id.input_name);
        guest_name = _guestNameText.getText().toString().trim();

        _GuestPhoneText = (EditText) findViewById(R.id.input_mobile);
        guest_phone = _GuestPhoneText.getText().toString().trim();


        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Providing Access...");
        progressDialog.show();

        // Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onLoginSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onLoginFailed() {
        Button _guestLoginButton = (Button) findViewById(R.id.btn_guest);

        CustomMessage.getInstance().CustomMessage(GuestActivity.this, "Login failed Please Signup or Try Logging Again");
        _guestLoginButton.setEnabled(true);
    }

    public void onLoginSuccess() {
        _guestLoginButton = (Button) findViewById(R.id.btn_guest);
        CustomMessage.getInstance().CustomMessage(GuestActivity.this, "Login Success");

        Bundle user_data = new Bundle();
        user_data.putString("guest_name", guest_name);
        user_data.putString("guest_phone", guest_phone);
        Log.e(TAG, "User -- " + user_data);

        final String Credentials = guest_name + "  ###  " + guest_phone;
        UserCheckUtil.writeToFile(Credentials, "guest");
        String text_file_date = UserCheckUtil.readFromFile("guest");
        Log.e(TAG, "User Details-- " + text_file_date);

        Intent intent = new Intent(this, MainActivity.class).putExtras(user_data);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, UserTypeActivity.class));
        finish();
    }

    public boolean validateGuest() {
        boolean validGuest = true;

        _nameText = (EditText) findViewById(R.id.input_name);
        _mobileText = (EditText) findViewById(R.id.input_mobile);

        String name = _nameText.getText().toString();
        String mobile = _mobileText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            validGuest = false;
        } else {
            _nameText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter Valid Mobile Number");
            validGuest = false;
        } else {
            _mobileText.setError(null);
        }

        return validGuest;
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
