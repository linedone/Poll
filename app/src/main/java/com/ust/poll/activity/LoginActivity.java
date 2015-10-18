package com.ust.poll.activity;



import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.linedone.poll.R;
import com.ust.poll.MainActivity;
import com.ust.poll.ui.dialog.DialogHelper;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends Activity {


    private static final String TAG = "LoginActivity";
    private static final String url = "http://www.afreesms.com/worldwide/hong-kong";

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

        new readsmsForm().execute();
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


    private class readsmsForm extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Document document = Jsoup.connect(url).get();
                // Get the html document title
                //title = document.title();


                Document doc = Jsoup.connect(url).get();

                Element hiddenInput = doc.select("input[name=IL_IN_TAG]").first();
                String hiddenInputValue = hiddenInput.attr("value");
                Log.d(TAG, "-"+hiddenInput);

                Element hiddenInput2 = doc.select("input[name=countrycode]").first();
                String hiddenInputValue2 = hiddenInput2.attr("value");
                Log.d(TAG, "-"+hiddenInput2);

                Elements allInput = doc.getElementsByTag("input");
                int counter = 0;

                // Loop through img tags
                for (Element el : allInput) {
                    // If alt is empty or null, add one to counter
                    if(el.attr("value").length() >20) {

                        Log.d(TAG, "-"+el.attr("value"));
                        Log.d(TAG, "-"+el.attr("name"));
                    }

                }

/*
                Elements allImage = doc.getElementsByTag("img");
                for (Element el : allImage) {
                    // If alt is empty or null, add one to counter
                        Log.d(TAG, "-"+el.absUrl("src"));
                }

                Elements scriptElements = doc.getElementsByTag("script");

                for (Element element :scriptElements ){
                    for (DataNode node : element.dataNodes()) {
                        Log.d(TAG, "-"+node.getWholeData());
                    }

                }
*/
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            //TextView txttitle = (TextView) findViewById(R.id.titletxt);
            //txttitle.setText(title);
            //mProgressDialog.dismiss();
        }
    }








    private class sendsmsForm extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document document = Jsoup.connect("http://www.afreesms.com/worldwide/hong-kong")
                        .data("IL_IN_TAG", "1")
                        .data("countrycode", "852")
                        .data("smsto", "") //mobile phone no.
                        .data("msgLen", "") //message length
                        .data("imgcode", "") //captcha
                        .data("", "") //two hidden long value (name and value)

                        .post();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            //TextView txttitle = (TextView) findViewById(R.id.titletxt);
            //txttitle.setText(title);
            //mProgressDialog.dismiss();
        }
    }














}
