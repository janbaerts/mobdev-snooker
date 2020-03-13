package com.janbaerts.android.snookerscoreboard.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.janbaerts.android.snookerscoreboard.R;
import com.janbaerts.android.snookerscoreboard.SelectPlayerActivity;
import com.janbaerts.android.snookerscoreboard.data.MatchSettingsData;
import com.janbaerts.android.snookerscoreboard.viewmodels.MatchSettingsViewModel;

public class MatchSettingsFragment extends Fragment {


    public static final String TAG = "MatchSettingsFragment";

    private FragmentManager fragmentManager;
    private TextView firstPlayerTextView;
    private TextView secondPlayerTextView;
    private NumberPicker maxFramesNumberPicker;
    private Button startNewMatchButton;
    private FrameLayout searchPlayerFrameLayout;

    private SelectPlayerFragment selectPlayerFragment;
    private boolean isShowingSelectPlayerFragment;
    private MatchSettingsViewModel viewModel;

    public MatchSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getFragmentManager();
        viewModel = MatchSettingsData.matchSettingsViewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_settings, container, false);

        firstPlayerTextView = view.findViewById(R.id.firstPlayerTextView);
        secondPlayerTextView = view.findViewById(R.id.secondPlayerTextView);
        maxFramesNumberPicker = view.findViewById(R.id.maxFrameNumberPicker);
        startNewMatchButton = view.findViewById(R.id.startNewMatchButton);
        searchPlayerFrameLayout = view.findViewById(R.id.searchPlayerFrameLayout);

        setUpNumberPicker();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    // Initializers --------------------------------------------------------------------------------
    private void setUpNumberPicker() {
        final String[] numberPickerValues = getMaxFrameValues();
        viewModel.setMaxNumberOfFrames(3);
        maxFramesNumberPicker.setDisplayedValues(numberPickerValues);
        maxFramesNumberPicker.setMinValue(0);
        maxFramesNumberPicker.setMaxValue(numberPickerValues.length - 1);
        maxFramesNumberPicker.setValue(1);
        maxFramesNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                viewModel.setMaxNumberOfFrames(Integer.parseInt(numberPickerValues[newVal]));
                System.out.println("In onValueChanged, is now " + viewModel.getMaxNumberOfFrames() + ".");
            }
        });
    }

    // Helper methods ------------------------------------------------------------------------------
    private String[] getMaxFrameValues() {
        String[] values = new String[18];
        int counter = 0;
        for (int i = 1; i < 36; i += 2)
            values[counter++] = Integer.toString(i);
        return values;
    }

    public void updateUI() {
        if (viewModel.getPlayers() != null) {
            if (viewModel.getPlayers()[0] != null)
                firstPlayerTextView.setText(viewModel.getPlayers()[0].getFullName());
            if (viewModel.getPlayers()[1] != null)
                secondPlayerTextView.setText(viewModel.getPlayers()[1].getFullName());
        }
    }

    public void toggleSearchPlayerFragment() {
        if (isShowingSelectPlayerFragment) {
            isShowingSelectPlayerFragment = false;
            fragmentManager.beginTransaction().remove(selectPlayerFragment).commit();
        } else {
            isShowingSelectPlayerFragment = true;
            fragmentManager.beginTransaction()
                    .add(R.id.searchPlayerFrameLayout, selectPlayerFragment, SelectPlayerFragment.TAG)
                    .commit();
        }
    }

}
