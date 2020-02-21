package com.janbaerts.android.snookerscoreboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.janbaerts.android.snookerscoreboard.data.MatchSettingsData;
import com.janbaerts.android.snookerscoreboard.fragments.MatchSettingsFragment;
import com.janbaerts.android.snookerscoreboard.fragments.PlayerDetailFragment;
import com.janbaerts.android.snookerscoreboard.fragments.SelectPlayerFragment;
import com.janbaerts.android.snookerscoreboard.models.Player;
import com.janbaerts.android.snookerscoreboard.viewmodels.MatchSettingsViewModel;

public class SelectPlayerActivity extends AppCompatActivity {

    public MatchSettingsViewModel matchSettingsViewModel;
    private FragmentManager fragmentManager;
    private MatchSettingsActivity matchSettingsActivity;

    private FrameLayout selectPlayerFragmentFrameLayout;
    private FrameLayout playerDetailFragmentFrameLayout;

    private SelectPlayerFragment selectPlayerFragment;
    private PlayerDetailFragment playerDetailFragment;

    private boolean doublePaneLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_player);

        fragmentManager = getSupportFragmentManager();
        matchSettingsViewModel = MatchSettingsData.matchSettingsViewModel;

        selectPlayerFragment = (SelectPlayerFragment)fragmentManager.findFragmentById(R.id.selectPlayerFragment);
        playerDetailFragment = (PlayerDetailFragment)fragmentManager.findFragmentById(R.id.playerDetailFragment);

        selectPlayerFragmentFrameLayout = findViewById(R.id.selectPlayerFragmentFrameLayout);
        playerDetailFragmentFrameLayout = findViewById(R.id.playerDetailFragmentFrameLayout);
        checkForDoublePaneLayout();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForDoublePaneLayout();
    }

    // Getters and setters -------------------------------------------------------------------------
    public boolean parentIsMatchSettingsActivity() {
        return matchSettingsViewModel != null;
    }

    // Initializers --------------------------------------------------------------------------------

    // Event handlers ------------------------------------------------------------------------------
    public void choosePlayer(View view) {
        Log.d("JB", "You tapped on a player in the RecyclerView.");
        Log.d("JB", ((TextView)view).getText().toString());
        SavePlayerInViewModel(view);
    }

    // Helper methods ------------------------------------------------------------------------------
    private void checkForDoublePaneLayout() {
        if (playerDetailFragment == null)
            doublePaneLayout = false;
        else doublePaneLayout = true;
    }

    private void SavePlayerInViewModel(View view) {
        String listItemText = ((TextView)view).getText().toString();
        String playerId = listItemText.substring(listItemText.lastIndexOf(' ') + 2, listItemText.lastIndexOf(')'));
        Log.d("JB", playerId);
        matchSettingsViewModel.setPlayer(getTappedPlayer(playerId), matchSettingsViewModel.getIndexOfPlayerBeingSelected());
        Log.i("JB", matchSettingsViewModel.getPlayers()[0].getFullName());
        this.setResult(RESULT_OK);
        finish();
    }

    private Player getTappedPlayer(String playerId) {
        for (int i = 0; i < matchSettingsViewModel.getQueryResult().size(); i++) {
            if (matchSettingsViewModel.getQueryResult().get(i).getEmail().equalsIgnoreCase(playerId))
                return matchSettingsViewModel.getQueryResult().get(i);
        }
        return null;
    }

}
