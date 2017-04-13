package com.lucidleanlabs.dev.lcatalog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView app_name = (TextView) findViewById(R.id.application_name);
        Button _loginButton = (Button) findViewById(R.id.btn_login);
        TextView _returnToLogin = (TextView) findViewById(R.id.return_to_login);
        final Button _guestLoginButton = (Button) findViewById(R.id.btn_guest);
        TextView _signupLink = (TextView) findViewById(R.id.link_signup);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        app_name.setTypeface(custom_font);

        //Disables the keyboard to appear on the activity launch
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        animate();

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _guestLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                convert_view_guest();

                _guestLoginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v1) {
                        guest_login();
                    }
                });

            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _returnToLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Return back to Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
//                overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
            }
        });
    }

    public void animate() {

        final ImageView imageView = (ImageView) findViewById(R.id.icon);
        final Animation animation_1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anti_rotate);

        imageView.startAnimation(animation_2);


        animation_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(animation_1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        animation_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    public void convert_view_guest() {

        TextInputLayout area_nameText = (TextInputLayout) findViewById(R.id.area_input_name);
        TextInputLayout area_mobileText = (TextInputLayout) findViewById(R.id.area_input_mobile);
        TextInputLayout area_emailText = (TextInputLayout) findViewById(R.id.area_input_email);
        TextInputLayout area_passwordText = (TextInputLayout) findViewById(R.id.area_input_password);
        Button _loginButton = (Button) findViewById(R.id.btn_login);
        TextView _returnToLogin = (TextView) findViewById(R.id.return_to_login);

        area_emailText.setVisibility(View.GONE);
        area_passwordText.setVisibility(View.GONE);
        area_nameText.setVisibility(View.VISIBLE);
        area_mobileText.setVisibility(View.VISIBLE);
        _loginButton.setVisibility(View.GONE);
        _returnToLogin.setVisibility(View.VISIBLE);

    }

    public void guest_login() {
        Log.d(TAG, "Guest User Login");

        if (!validateGuest()) {
            onLoginFailed();
            return;
        }

        Button _guestLoginButton = (Button) findViewById(R.id.btn_guest);
        _guestLoginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Providing Access...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        Button _loginButton = (Button) findViewById(R.id.btn_login);
        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        EditText _emailText = (EditText) findViewById(R.id.input_email);
        EditText _passwordText = (EditText) findViewById(R.id.input_password);

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Alert");
        builder.setMessage("Press OK to exit");
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

    public void onLoginSuccess() {
        Button _loginButton = (Button) findViewById(R.id.btn_login);
        _loginButton.setEnabled(true);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    public void onLoginFailed() {
        Button _loginButton = (Button) findViewById(R.id.btn_login);
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validateGuest() {
        boolean validGuest = true;

        EditText _nameText = (EditText) findViewById(R.id.input_name);
        EditText _mobileText = (EditText) findViewById(R.id.input_mobile);

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

    public boolean validate() {

        EditText _emailText = (EditText) findViewById(R.id.input_email);
        EditText _passwordText = (EditText) findViewById(R.id.input_password);
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
