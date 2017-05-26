package com.lucidleanlabs.dev.lcatalog;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPassword";
    private static final int REQUEST_FORGOT = 0;

    private static final String PASSWORD_UPDATE_URL = "http://35.154.252.64:8080/lll/web/user/update_password";

    private TextView _login_forgot_Link, _signup_forgot_link, app_name, powered;
    private Button _submitButton;
    private EditText _emailText, _passwordText, _reenterPasswordText;
    private String email, password, ReEnterPass;

    String code, message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        app_name = (TextView) findViewById(R.id.application_name);
        powered = (TextView) findViewById(R.id.lucidleanlabs);
        _submitButton = (Button) findViewById(R.id.btn_submit);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Cookie-Regular.ttf");

        app_name.setTypeface(custom_font);
        powered.setTypeface(custom_font2);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    submit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        _login_forgot_Link = (TextView) findViewById(R.id.link_forgot_login);
        _login_forgot_Link.setTypeface(custom_font2);
        _login_forgot_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _signup_forgot_link = (TextView) findViewById(R.id.link_forgot_signup);
        _signup_forgot_link.setTypeface(custom_font2);
        _signup_forgot_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    private void submit() throws JSONException {
        Log.d(TAG, "Password Update Request");

        if (!validate()) {
            onSubmitFailed();
            return;
        }
        _submitButton = (Button) findViewById(R.id.btn_submit);
        _submitButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Password...");
        progressDialog.show();

        _emailText = (EditText) findViewById(R.id.input_forgot_email);
        _passwordText = (EditText) findViewById(R.id.input_New_password);
        _reenterPasswordText = (EditText) findViewById(R.id.input_reEnter_New_Password);

        email = _emailText.getText().toString().trim();
        password = _passwordText.getText().toString().trim();
        ReEnterPass = _reenterPasswordText.getText().toString().trim();


        final JSONObject password_update_parameters = new JSONObject();
        password_update_parameters.put("email", email);
        password_update_parameters.put("newPassword", password);
        Log.e(TAG, "Request--" + password_update_parameters);

        final JSONObject request = new JSONObject();
        request.put("request", password_update_parameters);
        Log.e(TAG, "Request--" + request);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, PASSWORD_UPDATE_URL, request, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject requestResponse) {
                Log.e(TAG, "response--" + requestResponse);
                try {
                    code = requestResponse.getString("code");
                    message = requestResponse.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ForgotPasswordActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                // On complete call either onLoginSuccess or onLoginFailed

                if (Objects.equals(message, "SUCCESS")) {
                    onSubmitSuccess();
                } else {
                    onSubmitFailed();
                }
                progressDialog.dismiss();
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Alert");
        builder.setMessage("Press OK to exit from this app");
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

    /*Validations for the Forgot Password Activity*/

    private boolean validate() {
        boolean valid = true;
        _emailText = (EditText) findViewById(R.id.input_forgot_email);
        _passwordText = (EditText) findViewById(R.id.input_New_password);
        _reenterPasswordText = (EditText) findViewById(R.id.input_reEnter_New_Password);

        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        ReEnterPass = _reenterPasswordText.getText().toString();

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

        if (ReEnterPass.isEmpty() || ReEnterPass.length() < 4 || ReEnterPass.length() > 10 || !(ReEnterPass.equals(password))) {
            _reenterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reenterPasswordText.setError(null);
        }
        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_FORGOT) {
            if (resultCode == RESULT_OK) {

                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void onSubmitSuccess() {
        _submitButton = (Button) findViewById(R.id.btn_submit);
        _submitButton.setEnabled(false);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        finish();
    }

    private void onSubmitFailed() {
        Button _submitbtn = (Button) findViewById(R.id.btn_submit);
        Toast.makeText(getBaseContext(), "Please Enter valid Email Id", Toast.LENGTH_SHORT).show();
        _submitbtn.setEnabled(true);

    }
}
