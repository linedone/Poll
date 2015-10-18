package com.ust.poll.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ust.poll.MainActivity;
import com.linedone.poll.R;

/**
 * Created by Ken on 10/7/2015.
 */
public class PollResultFragment extends MainActivity.PlaceholderFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_result, container, false);
        return rootView;
    }
}
