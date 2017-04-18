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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    private static final String LOGIN_URL = "http://192.168.0.6:8080/lll/web/user/login";

    private TextView _signupLink;
    private EditText _emailText, _passwordText;
    private Button _loginButton, _guestLoginButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView app_name = (TextView) findViewById(R.id.application_name);
        _loginButton = (Button) findViewById(R.id.btn_login);
        TextView _returnToLogin = (TextView) findViewById(R.id.return_to_login);
        _guestLoginButton = (Button) findViewById(R.id.btn_guest);
        _signupLink = (TextView) findViewById(R.id.link_signup);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        app_name.setTypeface(custom_font);

        //Disables the keyboard to appear on the activity launch
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        animate();

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Providing Access...");
        progressDialog.show();

        // Implement your own authentication logic here.

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

    public void login() throws JSONException {
        Log.d(TAG, "Login");

        Log.d(TAG, "KEY_EMAIL--" + KEY_EMAIL);
        Log.d(TAG, "KEY_PASSWORD--" + KEY_PASSWORD);

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

        _emailText = (EditText) findViewById(R.id.input_email);
        final String email = _emailText.getText().toString().trim();
        Log.d(TAG, "email--" + email);

        _passwordText = (EditText) findViewById(R.id.input_password);
        final String password = _passwordText.getText().toString().trim();
        Log.d(TAG, "password--" + password);


        // Implement your own authentication logic here.

        final JSONObject request = new JSONObject();
        request.put("email", email);
        request.put("password", password);
        Log.d(TAG, "email--" + email);
        Log.d(TAG, "password--" + password);

        final JSONObject baseClass = new JSONObject();
        baseClass.put("request", request);
        Log.d(TAG, "baseclass--" + baseClass);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, baseClass, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject baseClass) {
                Log.e(TAG, "response" + baseClass);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject request = new JSONObject(res);
                    } catch (UnsupportedEncodingException | JSONException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }
            }
        }) {
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put(KEY_EMAIL, email);
                params.put(KEY_PASSWORD, password);
                Log.e(TAG, "Hash map...." + String.valueOf(params));
                Log.e(TAG, "Hash map .. " + params);
                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

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
        Toast.makeText(getBaseContext(), "Login failed Please Signup or Try Logging Again", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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
