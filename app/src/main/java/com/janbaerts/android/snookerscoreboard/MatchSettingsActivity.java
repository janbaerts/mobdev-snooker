package com.janbaerts.android.snookerscoreboard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.janbaerts.android.snookerscoreboard.data.FavouriteBall;
import com.janbaerts.android.snookerscoreboard.data.MatchSettingsData;
import com.janbaerts.android.snookerscoreboard.fragments.MatchSettingsFragment;
import com.janbaerts.android.snookerscoreboard.fragments.PlayerDetailFragment;
import com.janbaerts.android.snookerscoreboard.models.Player;
import com.janbaerts.android.snookerscoreboard.viewmodels.MatchSettingsViewModel;

public class MatchSettingsActivity extends AppCompatActivity {

    public MatchSettingsViewModel viewModel;
    private FragmentManager fragmentManager;

    private FrameLayout matchSettingsFrameLayout;

    public MatchSettingsFragment matchSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_settings);

        Log.d("JB", "In onCreate of MatchSettingsActivity.");

        viewModel = MatchSettingsData.matchSettingsViewModel;

        fragmentManager = getSupportFragmentManager();
        matchSettingsFrameLayout = findViewById(R.id.matchSettingsFrameLayout);
        matchSettingsFragment = ((MatchSettingsFragment)fragmentManager.findFragmentById(R.id.matchSettingsFragment));
    }

    @Override
    protected void onResume() {
        Log.d("JB", MatchSettingsData.matchSettingsViewModel.toString());
        super.onResume();
    }

    // Event handlers ------------------------------------------------------------------------------
    public void searchPlayer(View view) {
        Log.d("JB", "In searchPlayer.");
        setIndexOfPlayerBeingSelected(view);
//        matchSettingsFragment.searchPlayer();
        Intent intent = new Intent(this, SelectPlayerActivity.class);
        if (viewModel.getPlayers()[0] != null)
            Log.d("JB", "About to start selectPlayer, current player 1 = " + viewModel.getPlayers()[0].getFullName());
        startActivityForResult(intent, 8);
    }

    public void startMatch(View view) {
        Intent intent = new Intent(this, MatchActivity.class);
        Player[] players = viewModel.getPlayers();

        // For testing:
//        players[0] = new Player("Jan", "Baerts", "jb@hotmail.com", FavouriteBall.BLUEBALL);
//        players[1] = new Player("Ethan", "Baerts", "eb@hotmail.com", FavouriteBall.BLACKBALL);
        // To avoid manually having to choose players.

        if (players[0] != null && players[1] != null) {
            intent.putExtra("firstPlayer", players[0].getEmail());
            intent.putExtra("secondPlayer", players[1].getEmail());
            intent.putExtra("maxNumberOfFrames", viewModel.getMaxNumberOfFrames() + "");
            startActivity(intent);
        } else {
            Toast.makeText(this, getResources().getString(R.string.must_choose_2_players), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("JB", "In onActivityResult.");
        updateUI();
    }

    // Helper methods ------------------------------------------------------------------------------
    private void setIndexOfPlayerBeingSelected(View view) {
        viewModel.setIndexOfPlayerBeingSelected(-1);
        if (view.getId() == R.id.firstPlayerTextView)
            viewModel.setIndexOfPlayerBeingSelected(0);
        if (view.getId() == R.id.secondPlayerTextView)
            viewModel.setIndexOfPlayerBeingSelected(1);
    }

    public void updateUI() {
        matchSettingsFragment.updateUI();
    }
}
