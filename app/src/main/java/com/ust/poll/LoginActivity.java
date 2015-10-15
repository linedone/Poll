package com.ust.poll;


//testing 123456

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.linedone.poll.R;
import com.ust.poll.ui.dialog.DialogHelper;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {
    @Bind(R.id.txt_username)
    BootstrapEditText txt_username;
    @Bind(R.id.txt_password)
    BootstrapEditText txt_password;
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        fnInitParse();
    }

    /*
    * Core functions to connect to Parse backend
    * */
    private void fnInitParse() {
        // Use for analytic
        ParseAnalytics.trackAppOpenedInBackground(this.getIntent());

        user = ParseUser.getCurrentUser();
        // IF already login, move to next page
        fnCheckUser();
    }

    /*
    * Buttons handlers
    * */
    @OnClick(R.id.btn_login)
    public void fnLogin(View view) {
        String usernametxt = txt_username.getText().toString();
        String passwordtxt = txt_password.getText().toString();

        fnPreSave();
        final LoginActivity ctx = this;
        // Send data to Parse.com for verification
        ParseUser.logInInBackground(usernametxt, passwordtxt,
                new LogInCallback() {
                    public void done(ParseUser parseUser, ParseException e) {
                        fnPostSave();
                        if (parseUser != null) {
                            // If user exist and authenticated
                            user = parseUser;
                            fnPostLogin();
                        } else {
                            DialogHelper.getOkAlertDialog(ctx,
                                    "Error in connecting server..",
                                    e.getMessage()).show();
                        }
                    }
                });
    }

    @OnClick(R.id.btn_signup)
    public void fnSignup(View view) {
        String usernametxt = txt_username.getText().toString();
        String passwordtxt = txt_password.getText().toString();

        // Force user to fill up the form
        if (usernametxt.equals("") || passwordtxt.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please complete the sign up form", Toast.LENGTH_LONG)
                    .show();

        } else {
            final LoginActivity ctx = this;
            fnPreSave();
            // Save new user data into Parse.com Data Storage
            final ParseUser puser = new ParseUser();
            puser.setUsername(usernametxt);
            puser.setPassword(passwordtxt);
            puser.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    fnPostSave();
                    if (e == null) {
                        // Show a simple Toast message upon successful
                        // registration
                        Toast.makeText(getApplicationContext(),
                                "Successfully Signed up, please log in.",
                                Toast.LENGTH_LONG).show();

                        user = puser;
                        fnPostLogin();
                    } else {
                        DialogHelper.getOkAlertDialog(ctx,
                                "Error in connecting server..", e.getMessage())
                                .show();
                    }
                }
            });
        }
    }


    private void fnCheckUser() {
        if (user != null && user.getObjectId() != null) {
            fnPostLogin();
            return;
        }
    }

    private void fnPostLogin() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Successfully Logged in",
                Toast.LENGTH_LONG).show();
        finish();
    }

    private void fnPreSave() {
        DialogHelper.fnShowDialog(this);
    }

    private void fnPostSave() {
        DialogHelper.fnCloseDialog();
    }
}
