package com.lucidleanlabs.dev.lcatalog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.lucidleanlabs.dev.lcatalog.utils.UserCheckUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_LOGIN = 0;
    private static final int REQUEST_FORGOT_PASSWORD = 0;

    private static final String LOGIN_URL = "http://35.154.252.64:8080/lll/web/user/login";

    TextView app_name, _forgot_password, powered;
    EditText _emailText, _passwordText;
    Button _loginButton;
    ImageButton get_details;

    String code, message;
    String userName, userEmail, userPhone, userAddress;
    String email, password;

    File file_customer;
    String[] text_from_customer_file;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        app_name = (TextView) findViewById(R.id.application_name);
        powered = (TextView) findViewById(R.id.lucidleanlabs);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _forgot_password = (TextView) findViewById(R.id.link_forgot_password);
        get_details = (ImageButton) findViewById(R.id.btn_get_data);

        String customer_text_file_location = Environment.getExternalStorageDirectory() + "/L_CATALOGUE/customer.txt";
        file_customer = new File(customer_text_file_location);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Cookie-Regular.ttf");

        app_name.setTypeface(custom_font);
        powered.setTypeface(custom_font2);
        _forgot_password.setTypeface(custom_font2);

        //Disables the keyboard to appear on the activity launch
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_login);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, UserTypeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        get_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login();
//                    overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        _forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivityForResult(intent, REQUEST_FORGOT_PASSWORD);
                finish();
            }
        });
    }

    private void setData() {
        if (!file_customer.exists()) {
            Toast.makeText(getBaseContext(), "No Saved Details Yet", Toast.LENGTH_LONG).show();
        } else {
            text_from_customer_file = UserCheckUtil.readFromFile("customer").split(" ### ");

            _emailText = (EditText) findViewById(R.id.input_email);
            _emailText.setText(text_from_customer_file[0].trim());

            _passwordText = (EditText) findViewById(R.id.input_password);
            _passwordText.setText(text_from_customer_file[1].trim());
        }
    }

    public void login() throws JSONException {
        Log.e(TAG, "Customer Login");

        Log.e(TAG, "KEY_EMAIL--" + "email");
        Log.e(TAG, "KEY_PASSWORD--" + "password");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        _loginButton = (Button) findViewById(R.id.btn_login);
        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        _emailText = (EditText) findViewById(R.id.input_email);
        email = _emailText.getText().toString().trim();
        Log.e(TAG, "Entered email--" + email);

        _passwordText = (EditText) findViewById(R.id.input_password);
        password = _passwordText.getText().toString().trim();
        Log.e(TAG, "Entered password--" + password);

        // Implement your own authentication logic here.

        final String Credentials = email + "  ###  " + password;
        UserCheckUtil.writeToFile(Credentials, "customer");
        String text_file_date = UserCheckUtil.readFromFile("customer");
        Log.d(TAG, "User Details-- " + text_file_date);

        final JSONObject login_parameters = new JSONObject();
        login_parameters.put("email", email);
        login_parameters.put("password", password);
        Log.e(TAG, "Request--" + login_parameters);

        final JSONObject request = new JSONObject();
        request.put("request", login_parameters);
        Log.e(TAG, "Request--" + request);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject requestResponse) {
                Log.e(TAG, "response--" + requestResponse);
                try {

                    JSONObject user_details = requestResponse.getJSONObject("resp");
                    userName = user_details.getString("name");
                    userAddress = user_details.getString("address");
                    userEmail = user_details.getString("email");
                    userPhone = user_details.getString("mobileNo");
                    Log.e(TAG, "User Name > " + userName + "\n User Address > " + userAddress + "\n User Email > " + userEmail + "\n User Phone > " + userPhone);
                    code = requestResponse.getString("code");
                    message = requestResponse.getString("message");
                    Log.e(TAG, " code--" + code + " message--" + message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                params.put("email", email);
                params.put("password", password);
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

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                // On complete call either onLoginSuccess or onLoginFailed
                if (Objects.equals(message, "FAILURE")) {
                    onLoginFailed();
                } else {
                    onLoginSuccess();
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

    public void onLoginSuccess() {
        Button _loginButton = (Button) findViewById(R.id.btn_login);
        Toast.makeText(getBaseContext(), "Login Success", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
        setResult(RESULT_OK, null);

        Bundle user_data = new Bundle();
        user_data.putString("name", userName);
        user_data.putString("phone", userPhone);
        user_data.putString("address", userAddress);
        user_data.putString("email", userEmail);
        Log.d(TAG, "User -- " + user_data);

        Intent intent = new Intent(this, MainActivity.class).putExtras(user_data);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Button _loginButton = (Button) findViewById(R.id.btn_login);
        Toast.makeText(getBaseContext(), "Login failed Please Signup or Try Logging Again", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(false);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, UserTypeActivity.class));
        finish();
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
