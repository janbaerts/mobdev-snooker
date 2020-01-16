package com.janbaerts.android.snookerscoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.janbaerts.android.snookerscoreboard.fragments.SelectPlayerFragment;

public class PlayerStatisticsActivity
        extends AppCompatActivity
        implements SelectPlayerFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_statistics);
    }

    @Override
    public void tappedOnPlayer(View view) {

    }
}
