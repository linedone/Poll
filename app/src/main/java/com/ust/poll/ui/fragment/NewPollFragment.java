package com.ust.poll.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.ust.poll.MainActivity;
import com.linedone.poll.R;
import com.ust.poll.util.Util;
import com.parse.ParsePush;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CSH529 on 10/7/2015.
 */
public class NewPollFragment extends MainActivity.PlaceholderFragment {
    @Bind(R.id.txt_msg)
    BootstrapEditText txt_msg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_new, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.btn_new_poll)
    public void fnNewPoll(View view) {

        ParsePush push = new ParsePush();
        push.setChannel(Util.PARSE_CHANNEL);
        push.setMessage(txt_msg.getText().toString());
//        push.setExpirationTime(1424841505);
        push.sendInBackground();
    }
}
