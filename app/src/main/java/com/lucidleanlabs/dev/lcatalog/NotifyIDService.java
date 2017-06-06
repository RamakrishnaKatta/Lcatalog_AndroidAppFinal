package com.lucidleanlabs.dev.lcatalog;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class NotifyIDService extends FirebaseInstanceIdService {

    private static final String TAG = "NotifyIDService";
    String refreshedToken;
    private static final String REGISTER_URL = "http://35.154.252.64:8080/lll/app/notify/device?tokenid=";
    private static String TOKEN_REG_URL = null;

    String token_id, token_value, reg_code, reg_message;

    public void onTokenRefresh() {
        // Get updated InstanceID token.

        try {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "Refreshed token: " + refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {

        TOKEN_REG_URL = REGISTER_URL + refreshedToken;
        Log.e(TAG, "TOKEN_REG_URL--" + TOKEN_REG_URL);

        final JSONObject baseclass = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, TOKEN_REG_URL, baseclass, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "response--" + response);

                try {
                    JSONObject resp = response.getJSONObject("resp");
                    token_id = resp.getString("id");
                    Log.e(TAG, "vendor ID--" + token_id);
                    token_value = resp.getString("tokenId");
                    Log.e(TAG, "vendor Name--" + token_value);
                    reg_code = response.getString("code");
                    Log.e(TAG, "Code--" + reg_code);
                    reg_message = response.getString("message");
                    Log.e(TAG, "Code--" + reg_message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NotifyIDService.this, error.toString(), Toast.LENGTH_SHORT).show();
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject request = new JSONObject(res);
                        Log.e(TAG, "request--" + request);
                    } catch (UnsupportedEncodingException | JSONException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

}
