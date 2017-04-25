package com.lucidleanlabs.dev.lcatalog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class VendorRegistrationActivity extends AppCompatActivity {

    private static final String TAG = "VendorRegistrationActivity";

    public static final String KEY_V_COMPANYNAME = "companyName";
    public static final String KEY_V_CONTACTPERSONNAME = "contactPersonName";
    public static final String KEY_V_TOTALMODELS = "totModels";
    public static final String KEY_V_EMAIL = "email";
    public static final String KEY_V_ADDRESS = "address";
    public static final String KEY_V_LOCATION = "loc";
    public static final String KEY_V_STATE = "state";
    public static final String KEY_V_PIN = "pin";
    public static final String KEY_V_MOBILENO = "mobile";

    private static final String REGISTER_URL = "http://192.168.0.6:8080/lll/app/vendor/add_vendor_req";

    private EditText v_companyName, v_companyContactPerson, v_companyAddress, v_companyLocation, v_companyState, v_companyPin, v_companyEmail, v_companyMobileNo, v_totalModels;
    private Button v_registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_registration);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_vendor_reg);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        v_registerButton = (Button) findViewById(R.id.btn_vendor_submit);
        v_registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    vendorRegister();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void vendorRegister() throws JSONException {
        Log.d(TAG, "Signup");

        Log.d(TAG, "KEY_V_COMPANYNAME--" + KEY_V_COMPANYNAME);
        Log.d(TAG, "KEY_V_CONTACTPERSONNAME--" + KEY_V_CONTACTPERSONNAME);
        Log.d(TAG, "KEY_V_TOTALMODELS--" + KEY_V_TOTALMODELS);
        Log.d(TAG, "KEY_V_EMAIL--" + KEY_V_EMAIL);
        Log.d(TAG, "KEY_V_ADDRESS--" + KEY_V_ADDRESS);
        Log.d(TAG, "KEY_V_LOCATION--" + KEY_V_LOCATION);
        Log.d(TAG, "KEY_V_STATE--" + KEY_V_STATE);
        Log.d(TAG, "KEY_V_PIN--" + KEY_V_PIN);
        Log.d(TAG, "KEY_V_MOBILENO--" + KEY_V_MOBILENO);

        v_companyName = (EditText) findViewById(R.id.vendor_name);
        final String companyName = v_companyName.getText().toString().trim();
        Log.d(TAG, "Company Name--" + companyName);

        v_companyContactPerson = (EditText) findViewById(R.id.vendor_contact_name);
        final String companyContactName = v_companyContactPerson.getText().toString().trim();
        Log.d(TAG, "Company ContactName--" + companyContactName);

        v_companyAddress = (EditText) findViewById(R.id.vendor_address);
        final String companyAddress = v_companyAddress.getText().toString().trim();
        Log.d(TAG, "Company Address--" + companyAddress);

        v_companyLocation = (EditText) findViewById(R.id.vendor_location);
        final String companyLocation = v_companyLocation.getText().toString().trim();
        Log.d(TAG, "Company Location--" + companyLocation);

        v_companyState = (EditText) findViewById(R.id.vendor_state);
        final String companyState = v_companyState.getText().toString().trim();
        Log.d(TAG, "Company State--" + companyState);

        v_companyPin = (EditText) findViewById(R.id.vendor_pincode);
        final String companyPin = v_companyPin.getText().toString().trim();
        Log.d(TAG, "company PinCode--" + companyPin);

        v_companyEmail = (EditText) findViewById(R.id.vendor_email);
        final String companyEmail = v_companyEmail.getText().toString().trim();
        Log.d(TAG, "Company Email--" + companyEmail);

        v_companyMobileNo = (EditText) findViewById(R.id.vendor_mobile);
        final String companyMobileNo = v_companyMobileNo.getText().toString().trim();
        Log.d(TAG, "company Mobile No--" + companyMobileNo);

        v_totalModels = (EditText) findViewById(R.id.vendor_modelcount);
        final String companyModelCount = v_totalModels.getText().toString().trim();
        Log.d(TAG, "Company Model Count--" + companyModelCount);

        if (!validate()) {
            onVendorRegistrationFailed();
            return;
        }
        v_registerButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(VendorRegistrationActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting Vendor Registration Form......");
        progressDialog.show();

        JSONObject vendor_request = new JSONObject();
        vendor_request.put(KEY_V_COMPANYNAME, companyName);
        vendor_request.put(KEY_V_CONTACTPERSONNAME, companyContactName);
        vendor_request.put(KEY_V_ADDRESS, companyAddress);
        vendor_request.put(KEY_V_LOCATION, companyLocation);
        vendor_request.put(KEY_V_STATE, companyState);
        vendor_request.put(KEY_V_PIN, companyPin);
        vendor_request.put(KEY_V_EMAIL, companyEmail);
        vendor_request.put(KEY_V_MOBILENO, companyMobileNo);
        vendor_request.put(KEY_V_TOTALMODELS, companyModelCount);
        Log.d(TAG, "vendor_request--" + vendor_request);

        final JSONObject baseClass = new JSONObject();
        baseClass.put("request", vendor_request);
        Log.d(TAG, "baseclass--" + baseClass);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, REGISTER_URL, baseClass, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "response--" + baseClass);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VendorRegistrationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
                params.put(KEY_V_COMPANYNAME, companyName);
                params.put(KEY_V_CONTACTPERSONNAME, companyContactName);
                params.put(KEY_V_ADDRESS, companyAddress);
                params.put(KEY_V_LOCATION, companyLocation);
                params.put(KEY_V_STATE, companyState);
                params.put(KEY_V_PIN, companyPin);
                params.put(KEY_V_EMAIL, companyEmail);
                params.put(KEY_V_MOBILENO, companyMobileNo);
                params.put(KEY_V_TOTALMODELS, companyModelCount);

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
                        // On complete call either onSignupSuccess or onSignupFailed depending on success
                        onVendorRegistrationSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public boolean validate() {
        boolean valid = true;

        v_companyName = (EditText) findViewById(R.id.vendor_name);
        String companyName = v_companyName.getText().toString().trim();
        if (companyName.isEmpty() || companyName.length() < 3) {
            v_companyName.setError("Enter Valid Name, at least 3 characters");
            valid = false;
        } else {
            v_companyName.setError(null);
        }

        v_companyContactPerson = (EditText) findViewById(R.id.vendor_contact_name);
        String companyContactName = v_companyContactPerson.getText().toString().trim();
        if (companyContactName.isEmpty() || companyContactName.length() < 3) {
            v_companyContactPerson.setError("Enter Valid Name, at least 3 characters");
            valid = false;
        } else {
            v_companyContactPerson.setError(null);
        }

        v_companyAddress = (EditText) findViewById(R.id.vendor_address);
        String companyAddress = v_companyAddress.getText().toString().trim();
        if (companyAddress.isEmpty()) {
            v_companyAddress.setError("Enter Valid Address");
            valid = false;
        } else {
            v_companyAddress.setError(null);
        }

        v_companyLocation = (EditText) findViewById(R.id.vendor_location);
        final String companyLocation = v_companyLocation.getText().toString().trim();
        if (companyLocation.isEmpty()) {
            v_companyLocation.setError("Enter Valid Location");
            valid = false;
        } else {
            v_companyLocation.setError(null);
        }

        v_companyState = (EditText) findViewById(R.id.vendor_state);
        String companyState = v_companyState.getText().toString().trim();
        if (companyState.isEmpty()) {
            v_companyState.setError("Enter Valid State");
            valid = false;
        } else {
            v_companyState.setError(null);
        }

        v_companyPin = (EditText) findViewById(R.id.vendor_pincode);
        String companyPin = v_companyPin.getText().toString().trim();
        if (companyPin.isEmpty()) {
            v_companyPin.setError("Enter Valid Pin");
            valid = false;
        } else {
            v_companyPin.setError(null);
        }

        v_companyEmail = (EditText) findViewById(R.id.vendor_email);
        String companyEmail = v_companyEmail.getText().toString().trim();
        if (companyEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(companyEmail).matches()) {
            v_companyEmail.setError("enter a valid email address");
            valid = false;
        } else {
            v_companyEmail.setError(null);
        }

        v_companyMobileNo = (EditText) findViewById(R.id.vendor_mobile);
        String companyMobileNo = v_companyMobileNo.getText().toString().trim();
        if (companyMobileNo.isEmpty() || companyMobileNo.length() != 10) {
            v_companyMobileNo.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            v_companyMobileNo.setError(null);
        }

        v_totalModels = (EditText) findViewById(R.id.vendor_modelcount);
        String companyModelCount = v_totalModels.getText().toString().trim();
        if (companyModelCount.isEmpty()) {
            v_totalModels.setError("Enter a valid no of models you require");
            valid = false;
        } else {
            v_totalModels.setError(null);
        }

        return valid;
    }

    public void onVendorRegistrationSuccess() {

        v_registerButton = (Button) findViewById(R.id.btn_vendor_submit);
        v_registerButton.setEnabled(true);
        setResult(RESULT_OK, null);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void onVendorRegistrationFailed() {

        Toast.makeText(getBaseContext(), "Vendor Registration failed", Toast.LENGTH_LONG).show();

        v_registerButton = (Button) findViewById(R.id.btn_vendor_submit);
        v_registerButton.setEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vendor_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}