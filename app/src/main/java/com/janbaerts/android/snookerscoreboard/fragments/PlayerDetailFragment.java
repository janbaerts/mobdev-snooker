package com.janbaerts.android.snookerscoreboard.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.janbaerts.android.snookerscoreboard.R;

public class PlayerDetailFragment extends Fragment {


    public static final String TAG = "PlayerDetailFragment";

    public PlayerDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_detail, container, false);
    }

}
