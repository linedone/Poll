package com.ust.poll.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.linedone.poll.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;
import com.ust.poll.MainActivity;
import com.ust.poll.model.Poll;
import com.ust.poll.ui.dialog.DialogHelper;
import com.ust.poll.util.Util;

import java.util.Arrays;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ken on 10/7/2015.
 */
public class NewPollFragment extends MainActivity.PlaceholderFragment {
    @Bind(R.id.txt_title)
    BootstrapEditText txt_title;
    @Bind(R.id.option1)
    BootstrapButton option1;
    @Bind(R.id.option2)
    BootstrapButton option2;
    @Bind(R.id.option3)
    BootstrapButton option3;
    @Bind(R.id.option4)
    BootstrapButton option4;

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

    @OnClick({ R.id.option1, R.id.option2, R.id.option3, R.id.option4 })
    public void fnOption(View view) {
        final BootstrapButton btn = (BootstrapButton)view;

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(this.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btn.setText(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @OnClick(R.id.btn_new_poll)
    public void fnNewPoll(View view) {
        DialogHelper.fnShowDialog(this.getContext());
        fnCreatePoll();
        //fnSendPushNotification();
    }

    private void fnCreatePoll() {
        final Context ctx = this.getContext();

        ParseObject pollObject = new ParseObject(Poll.TABLE_NAME);
        pollObject.put(Poll.TITLE, txt_title.getText().toString());
        pollObject.addAllUnique(Poll.OPTIONS, Arrays.asList(option1.getText(),
                option2.getText(), option3.getText(), option4.getText()));
        pollObject.put(Poll.END_AT, new Date());

        pollObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                DialogHelper.fnCloseDialog();
                if (e == null) {
                    Toast.makeText(ctx,
                            "Poll Successfully created.",
                            Toast.LENGTH_LONG).show();
                } else {
                    DialogHelper.getOkAlertDialog(ctx,
                            "Error in connecting server..", e.getMessage())
                            .show();
                }
            }
        });
    }

    private void fnSendPushNotification() {
        ParsePush push = new ParsePush();
        push.setChannel(Util.PARSE_CHANNEL);
        push.setMessage(txt_title.getText().toString());
//        push.setExpirationTime(1424841505);
        push.sendInBackground();
    }
}
