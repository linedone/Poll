package com.ust.poll.activity;



import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.linedone.poll.R;
import com.ust.poll.MainActivity;
import com.ust.poll.model.smsData;
import com.ust.poll.ui.dialog.DialogHelper;
import com.parse.ParseAnalytics;
import com.parse.ParseUser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.Bind;


public class LoginActivity extends Activity {


    private static final String TAG = "LoginActivity";
    private static final String url = "http://www.afreesms.com/worldwide/hong-kong";
    smsData newData = new smsData();

    @Bind(R.id.txt_phone_no)
    BootstrapEditText txt_phone_no;
    @Bind(R.id.txt_captcha_code)
    BootstrapEditText txt_captcha_code;
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_reg);

        //ButterKnife.bind(this);
        //fnInitParse();

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


    public void fnReset(View view) {

        txt_phone_no.setText("");
        txt_captcha_code.setText("");
    }

    public void fnSubmit(View view) {

        new sendsmsForm().execute();
    }

    /*
    * Buttons handlers
    * */

    /*
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
*/

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
                        newData.setformValue(el.attr("value"));
                        Log.d(TAG, "-" + el.attr("value"));
                        newData.setformName(el.attr("name"));
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

            WebView contentWebView = (WebView) findViewById(R.id.image_captcha);
            contentWebView.getSettings().setJavaScriptEnabled(true);




            //contentWebView.loadUrl("javascript: ord=Math.random();  ord=ord*10000000000000000000; document.write('<img id=\"captcha\" src=\"http://www.afreesms.com/image.php?o='+ord+'\" align=\"top\" />')");
            //contentWebView.loadUrl("<script>ord=Math.random(); ord=ord*10000000000000000000; document.write('<img id=\"captcha\" src=\"http://www.afreesms.com/image.php?o='+ ord +'\" align=\"top\" />');</script>");
            contentWebView.loadUrl("javascript:document.write('<img id=\"captcha\" src=\"http://www.afreesms.com/image.php?o='+Math.random()*10000000000000000000+'\" align=\"top\" />')");
            //Log.d(TAG, "captcha URL" + "javascript: ord=Math.random();  ord=ord*10000000000000000000; document.write('<img id=\"captcha\" src=\"http://www.afreesms.com/image.php?o='+ord+'\" align=\"top\" />')");
        }
    }








    private class sendsmsForm extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Log.d(TAG, "b4 die");

            final EditText phone_no = (EditText) findViewById (R.id.txt_phone_no);
            String tmpPhone = phone_no.getText().toString();
            final EditText captcha = (EditText) findViewById (R.id.txt_captcha_code);
            String tmpCaptcha = captcha.getText().toString();



            newData.setPhoneno(tmpPhone);
            newData.setcaptcha(tmpCaptcha);

            Log.d(TAG, "phone" + newData.getphoneNo());
            Log.d(TAG, "captcha" + newData.getcaptcha());



           // Log.d(TAG, "after die");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {


                Document document = Jsoup.connect("http://www.afreesms.com/worldwide/hong-kong")
                        .data("IL_IN_TAG", "1")
                        .data("countrycode", "852")
                        //.data("smsto", "" +  newData.getphoneNo()) //mobile phone no.
                        .data("smsto", "54990678") //mobile phone no.
                        .data("message", "12345678910") //message length
                        .data("msgLen", "149") //message length
                        .data("imgcode", "364156") //captcha
                        //.data("imgcode", "" + newData.getcaptcha()) //captcha
                        //.data("" + newData.getformName(), "" + newData.getformValue()) //two hidden long value (name and value)
                        .data("dab55db11b9916a5e86521ecb4dc01d95204bd", "8ae71be147d842efeb662fb8eb9c5d85135e") //two hidden long value (name and value)
                        .data("IL_IN_TAG", "1")
                        .post();

                //Log.d(TAG, "temp phone "+ newData.getphoneNo());
                //Log.d(TAG, "tmp captcha "+ newData.getcaptcha());
                //Log.d(TAG, "tmp formName "+ newData.getformName());
                //Log.d(TAG, "tmp formValue "+ newData.getformValue());



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
