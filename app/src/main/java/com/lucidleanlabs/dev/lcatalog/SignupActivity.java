package com.lucidleanlabs.dev.lcatalog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private static final int REQUEST_SIGNUP = 0;

    public static final String KEY_USERNAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE_NO = "mobileNo";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_TYPE = "type";
    public static final String KEY_VENDORID = "vendorId";

    private static final String REGISTER_URL = "http://35.154.252.64:8080/lll/web/user/register";


    private TextView app_name, powered;
    private EditText _nameText, _addressText, _emailText, _mobileText, _passwordText, _reEnterPasswordText;
    private Button _signupButton;

    String response, code, message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        powered = (TextView) findViewById(R.id.lucidleanlabs);
        app_name = (TextView) findViewById(R.id.application_name);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Cookie-Regular.ttf");
        app_name.setTypeface(custom_font);
        powered.setTypeface(custom_font2);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_signup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignupActivity.this, UserTypeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        _signupButton = (Button) findViewById(R.id.btn_signup);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signup();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void signup() throws JSONException {
        Log.d(TAG, "Signup");

        Log.d(TAG, "KEY_USERNAME--" + KEY_USERNAME);
        Log.d(TAG, "KEY_PASSWORD--" + KEY_PASSWORD);
        Log.d(TAG, "KEY_EMAIL--" + KEY_EMAIL);
        Log.d(TAG, "KEY_MOBILE_NO--" + KEY_MOBILE_NO);
        Log.d(TAG, "KEY_ADDRESS--" + KEY_ADDRESS);
        Log.d(TAG, "KEY_TYPE--" + KEY_TYPE);
        Log.d(TAG, "KEY_VENDORID--" + KEY_VENDORID);

        _nameText = (EditText) findViewById(R.id.input_name);
        final String name = _nameText.getText().toString().trim();
        Log.d(TAG, "name--" + name);

        _addressText = (EditText) findViewById(R.id.input_address);
        final String address = _addressText.getText().toString().trim();
        Log.d(TAG, "address--" + address);

        _emailText = (EditText) findViewById(R.id.input_email);
        final String email = _emailText.getText().toString().trim();
        Log.d(TAG, "email--" + email);

        _mobileText = (EditText) findViewById(R.id.input_mobile);
        final String mobile = _mobileText.getText().toString().trim();
        Log.d(TAG, "mobile--" + mobile);

        _passwordText = (EditText) findViewById(R.id.input_password);
        final String password = _passwordText.getText().toString().trim();
        Log.d(TAG, "password--" + password);

        _reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);

        if (!validate()) {
            onSignupFailed();
            return;
        }
        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        // SIGNUP LOGIC !!

        JSONObject request = new JSONObject();
        request.put("name", name);
        Log.d(TAG, "name--" + name);
        request.put("address", address);
        Log.d(TAG, "address--" + address);
        request.put("email", email);
        Log.d(TAG, "email--" + email);
        request.put("mobileNo", mobile);
        Log.d(TAG, "mobile--" + mobile);
        request.put("password", password);
        Log.d(TAG, "password--" + password);
        request.put("type", "CUSTOMER");
        // This Value should be changed when a user is reggistered under specific customer
        request.put("vendorId", "100000");

        Log.d(TAG, "request--" + request);

        JSONObject baseClass = new JSONObject();
        baseClass.put("request", request);
        Log.d(TAG, "baseclass--" + baseClass);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, REGISTER_URL, baseClass, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject baseClass) {
                Log.e(TAG, "response--" + baseClass);
                try {
                    response = baseClass.getString("resp");
                    code = baseClass.getString("code");
                    message = baseClass.getString("message");
                    Log.d(TAG, "response--" + response + "code--" + code + "message--" + message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put(KEY_USERNAME, name);
                params.put(KEY_PASSWORD, password);
                params.put(KEY_EMAIL, email);
                params.put(KEY_ADDRESS, address);
                params.put(KEY_MOBILE_NO, mobile);
                params.put(KEY_TYPE, "CUSTOMER");

                // This Value should be changed when a user is registered under specific customer
                params.put(KEY_VENDORID, "600000");

                Log.e(TAG, "HashMap--" + String.valueOf(params));
                Log.e(TAG, "HashMap--" + params);

                return params;
            }

            @Override
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
                        if (Objects.equals(message, "FAILURE")) {
                            onSignupFailed();
                        } else {
                            onSignupSuccess();
                        }
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

    public void onSignupSuccess() {
        Toast.makeText(getBaseContext(), "SignUp Success", Toast.LENGTH_LONG).show();
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "SignUp failed", Toast.LENGTH_LONG).show();
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _signupButton.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, UserTypeActivity.class));
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        _nameText = (EditText) findViewById(R.id.input_name);
        _addressText = (EditText) findViewById(R.id.input_address);
        _emailText = (EditText) findViewById(R.id.input_email);
        _mobileText = (EditText) findViewById(R.id.input_mobile);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);

        String name = _nameText.getText().toString().trim();
        String address = _addressText.getText().toString().trim();
        String email = _emailText.getText().toString().trim();
        String mobile = _mobileText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();
        String reEnterPassword = _reEnterPasswordText.getText().toString().trim();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}
