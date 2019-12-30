package com.janbaerts.android.snookerscoreboard;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.janbaerts.android.snookerscoreboard.fragments.SelectPlayerFragment;
import com.janbaerts.android.snookerscoreboard.models.Player;
import com.janbaerts.android.snookerscoreboard.recyclerviews.SearchPlayerRecyclerViewAdapter;

import org.w3c.dom.Text;

public class StartNewGameActivity
        extends AppCompatActivity
        implements SelectPlayerFragment.OnFragmentInteractionListener {

    public static final String TAG = "StartNewGameActivity";

    private Player[] players = new Player[2];
    private TextView[] playerTextViews = new TextView[2];
    private boolean isShowingSelectingFragment = false;
    private int playerIndex;
    private int fragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new_game);
        playerIndex = -1;
        playerTextViews[0] = (TextView) findViewById(R.id.firstPlayerTextView);
        playerTextViews[1] = (TextView) findViewById(R.id.secondPlayerTextView);
    }

    public void selectPlayer(View view) {
        if (view.getId() == R.id.firstPlayerTextView)
            playerIndex = 0;
        else if (view.getId() == R.id.secondPlayerTextView)
            playerIndex = 1;

        if (playerIndex >= 0)
            showSelectPlayerFragment();
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

    @Override
    public void onFragmentInteraction(Uri uri) {

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
}
