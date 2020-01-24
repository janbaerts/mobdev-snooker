package com.janbaerts.android.snookerscoreboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.janbaerts.android.snookerscoreboard.data.FavouriteBall;
import com.janbaerts.android.snookerscoreboard.fragments.SelectPlayerFragment;
import com.janbaerts.android.snookerscoreboard.models.Player;
import com.janbaerts.android.snookerscoreboard.recyclerviews.SearchPlayerRecyclerViewAdapter;

// TODO: Beautify player select fragment!

public class StartNewMatchActivity
        extends AppCompatActivity
        implements SelectPlayerFragment.OnFragmentInteractionListener {

    public static final String TAG = "StartNewMatchActivity";

    private Player[] players = new Player[2];
    private TextView[] playerTextViews = new TextView[2];
    private NumberPicker numberPicker;

    private boolean isShowingSelectingFragment = false;
    private int playerIndex;
    private int fragmentId;
    private int maximumNumberOfFrames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_start_new_match);
//        else
//            setContentView(R.layout.activity_start_new_match_landscape);

        playerIndex = -1;
        maximumNumberOfFrames = 1;
        playerTextViews[0] = (TextView) findViewById(R.id.firstPlayerTextView);
        playerTextViews[1] = (TextView) findViewById(R.id.secondPlayerTextView);
        numberPicker = (NumberPicker) findViewById(R.id.maxFrameNumberPicker);
        setUpNumberPicker();
        getSupportActionBar().setTitle(R.string.title_activity_start_new_match);
    }

    public void selectPlayer(View view) {
        if (view.getId() == R.id.firstPlayerTextView)
            playerIndex = 0;
        else if (view.getId() == R.id.secondPlayerTextView)
            playerIndex = 1;

        if (playerIndex >= 0)
            showSelectPlayerFragment();
    }

    @Override
    public void tappedOnPlayer(View view) {
        savePlayer(view);
    }

    public void startMatch(View view) {
        Intent intent = new Intent(this, MatchActivity.class);

        // For testing purposes:
        players[0] = new Player("Jan", "Baerts", "jb@hotmail.com", FavouriteBall.BLUEBALL);
        players[1] = new Player("Ethan", "Baerts", "eb@hotmail.com", FavouriteBall.BLACKBALL);

        if (players[0] != null && players[1] != null) {
            intent.putExtra("firstPlayer", players[0].getEmail());
            intent.putExtra("secondPlayer", players[1].getEmail());
            intent.putExtra("maxNumberOfFrames", numberPicker.getDisplayedValues()[numberPicker.getValue()]);
            startActivity(intent);
        } else {
            Toast.makeText(this, getResources().getString(R.string.must_choose_2_players), Toast.LENGTH_SHORT);
        }
    }

    public void savePlayer(View view) {
        view.setBackgroundColor(Color.BLUE);
        String playerId = getPlayerIdFromTextView(view);
        RecyclerView rv = (RecyclerView) view.getParent();
        players[playerIndex] = ((SearchPlayerRecyclerViewAdapter)rv.getAdapter())
                .getPlayerFromPlayerList(playerId);
        System.out.println("Tapped player = " + players[playerIndex].toString());
        setPlayerNameLabel();
        hideSelectPlayerFragment();
    }


    public void showSelectPlayerFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SelectPlayerFragment selectPlayerFragment = new SelectPlayerFragment();

        System.out.println("In showSelectPlayerFragment with playerIndex = " + playerIndex);

        if (playerIndex == 0 && !isShowingSelectingFragment)
            fragmentTransaction.add(R.id.selectFirstPlayerFrameLayout, selectPlayerFragment, SelectPlayerFragment.TAG);
        else if (playerIndex == 1 && !isShowingSelectingFragment)
            fragmentTransaction.add(R.id.selectSecondPlayerFrameLayout, selectPlayerFragment, SelectPlayerFragment.TAG);
        fragmentTransaction.commit();
        isShowingSelectingFragment = true;
    }

    public void hideSelectPlayerFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(SelectPlayerFragment.TAG);
        if (fragment != null) {
            System.out.println("Deleting fragment.");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
        isShowingSelectingFragment = false;
    }

    public void setPlayerNameLabel() {
        playerTextViews[playerIndex].setText(players[playerIndex].getFullName());
    }

    public String getPlayerIdFromTextView(View view) {
        TextView tv = (TextView) view;
        String[] splitTextViewContent = tv.getText().toString().split(" ");
        int idIndex = splitTextViewContent.length - 1;
        int idLength = splitTextViewContent[idIndex].length();
        return splitTextViewContent[idIndex].substring(1, idLength - 1);
    }

    private void setUpNumberPicker() {
        final String[] numberPickerValues = getMaxFrameValues();
        numberPicker.setDisplayedValues(numberPickerValues);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(numberPickerValues.length - 1);
        numberPicker.setValue(1);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                maximumNumberOfFrames = Integer.parseInt(numberPickerValues[newVal]);
                System.out.println("In onValueChanged, is now " + maximumNumberOfFrames + ".");
            }
        });
    }

    private String[] getMaxFrameValues() {
        String[] values = new String[18];
        int counter = 0;
        for (int i = 1; i < 36; i += 2)
            values[counter++] = Integer.toString(i);
        return values;
    }
}
