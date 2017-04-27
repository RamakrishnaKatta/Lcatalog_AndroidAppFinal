package com.lucidleanlabs.dev.lcatalog;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPassword";
    private static final int REQUEST_FORGOT = 0;


    // private EditText _Enterpassword, _reenterpassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        TextView app_name = (TextView) findViewById(R.id.application_name);


        Button _submitButton = (Button) findViewById(R.id.btn_submit);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Graduate-Regular.ttf");
        app_name.setTypeface(custom_font);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        animate();
        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        TextView _login_forgot_Link = (TextView) findViewById(R.id.link_forgot_login);
        _login_forgot_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        TextView _signup_forgot_link = (TextView) findViewById(R.id.link_forgot_signup);
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

    private void animate() {

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

    private void submit() {
        Log.d(TAG, "Submit");

        if (!validate()) {
            onSubmitfailed();
            return;
        }
        Button _submitBtn = (Button) findViewById(R.id.btn_submit);
        _submitBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Providing Access...");
        progressDialog.show();

        EditText _emailText = (EditText) findViewById(R.id.input_forgot_email);
        EditText _passwordText = (EditText) findViewById(R.id.input_reset_password);
        EditText _reenterPasswordText = (EditText) findViewById(R.id.input_reEnter_forgot_Password);

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String ReEnterPass = _reenterPasswordText.getText().toString();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onSubmitSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    private boolean validate() {
        boolean valid = true;
        EditText _emailText = (EditText) findViewById(R.id.input_forgot_email);
        EditText _passwordText = (EditText) findViewById(R.id.input_reset_password);
        EditText _reEnterPasswordText = (EditText) findViewById(R.id.input_reEnter_forgot_Password);

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

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

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
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

    private void onSubmitSuccess() {
        Button _submitbtn = (Button) findViewById(R.id.btn_submit);
        _submitbtn.setEnabled(false);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        finish();
    }
                /*If Submit failed */
    private void onSubmitfailed() {
        Button _submitbtn = (Button) findViewById(R.id.btn_submit);
        Toast.makeText(getBaseContext(), "Please Enter valid Email Id", Toast.LENGTH_SHORT).show();

        _submitbtn.setEnabled(true);

    }

}
