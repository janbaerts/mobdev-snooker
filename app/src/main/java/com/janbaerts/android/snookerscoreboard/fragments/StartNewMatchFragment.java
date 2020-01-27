package com.janbaerts.android.snookerscoreboard.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.janbaerts.android.snookerscoreboard.R;

public class StartNewMatchFragment extends Fragment {


    public StartNewMatchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_new_match, container, false);
    }

}
