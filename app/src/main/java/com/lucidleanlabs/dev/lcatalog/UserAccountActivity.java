package com.lucidleanlabs.dev.lcatalog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import static android.R.id.message;

public class UserAccountActivity extends AppCompatActivity {

    private static final String TAG = "UserAccountActivity";

    private static final int REQUEST_UPDATE = 0;

    private EditText name, email, address, mobile;
    private KeyListener listener;
    private Button edit_user, update_user;
    private String user_email, user_name, user_address, user_phone, user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user_account);
        setSupportActionBar(toolbar);

        TextView app_name = (TextView) findViewById(R.id.application_name);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        app_name.setTypeface(custom_font);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final Bundle user_details = getIntent().getExtras();

        user_email = user_details.getString("user_email");
        user_name = user_details.getString("user_name");
        user_address = user_details.getString("user_address");
        user_phone = user_details.getString("user_phone");
        user_type = user_details.getString("user_type");

        name = (EditText) findViewById(R.id.user_input_name);
        disableEditText(name);
        name.setText(user_name);

        address = (EditText) findViewById(R.id.user_input_address);
        disableEditText(address);
        address.setText(user_address);

        email = (EditText) findViewById(R.id.user_input_email);
        disableEditText(email);
        email.setText(user_email);

        mobile = (EditText) findViewById(R.id.user_input_mobile);
        disableEditText(mobile);
        mobile.setText(user_phone);

        edit_user = (Button) findViewById(R.id.btn_edit_account);
        edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {

                enableEditText(name);
                enableEditText(email);
                enableEditText(address);
                enableEditText(mobile);

                edit_user.setVisibility(View.GONE);
                update_user = (Button) findViewById(R.id.btn_update_account);
                update_user.setVisibility(View.VISIBLE);
                update_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        update();
                    }
                });
            }
        });
    }

    public void update() {

        if (!validate()) {
            UpdateFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(UserAccountActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Account...");
        progressDialog.show();

        // UPDATE LOGIC !!

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (Objects.equals(message, "FAILURE")) {
                            UpdateFailed();
                        } else {
                            updateSuccess();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void updateSuccess() {

        Toast.makeText(getBaseContext(), "Update Success", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, UserAccountActivity.class);
        startActivity(intent);
        finish();
    }

    public void UpdateFailed() {


        Toast.makeText(getBaseContext(), "Update failed", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, UserAccountActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_UPDATE) {
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

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setClickable(false);
        listener = editText.getKeyListener();
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    private void enableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setEnabled(true);
        editText.setClickable(true);
        editText.setCursorVisible(true);
        editText.setKeyListener(listener);
    }

    public boolean validate() {
        boolean valid = true;

        name = (EditText) findViewById(R.id.user_input_name);
        address = (EditText) findViewById(R.id.user_input_address);
        email = (EditText) findViewById(R.id.user_input_email);
        mobile = (EditText) findViewById(R.id.user_input_mobile);

        String user_name = name.getText().toString().trim();
        String user_address = address.getText().toString().trim();
        String user_email = email.getText().toString().trim();
        String user_mobile = mobile.getText().toString().trim();

        if (user_name.isEmpty() || user_name.length() < 3) {
            name.setError("at least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }

        if (user_address.isEmpty()) {
            address.setError("Enter Valid Address");
            valid = false;
        } else {
            address.setError(null);
        }

        if (user_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (user_mobile.isEmpty() || user_mobile.length() != 10) {
            mobile.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mobile.setError(null);
        }

        return valid;
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
