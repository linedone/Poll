package com.ust.poll;

import android.app.Application;

import com.linedone.poll.R;
import com.ust.poll.util.Util;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

/**
 * Created by CSH529 on 10/7/2015.
 */
public class PollApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        String app_key = getString(R.string.parse_app_id);
        String client_key = getString(R.string.parse_client_key);
        Parse.initialize(this, app_key, client_key);

        // use for push notification
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground(Util.PARSE_CHANNEL);
    }
}
