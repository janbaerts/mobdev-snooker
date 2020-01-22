package com.janbaerts.android.snookerscoreboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.TextView;

import com.janbaerts.android.snookerscoreboard.models.GameEvent;
import com.janbaerts.android.snookerscoreboard.viewmodels.MatchViewModel;

public class MatchActivity extends AppCompatActivity {

    private TextView[] pointsScoredTextViews;
    private TextView framesScoredTextView;
    private TextView[] eventList;
    private MatchViewModel match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        match = ViewModelProviders.of(this).get(MatchViewModel.class);
        assignAllViews(3);

    }

    private void assignAllViews(int listLength) {
        eventList = new TextView[listLength];
        eventList[0] = findViewById(R.id.gameEventListOne);
        eventList[1] = findViewById(R.id.gameEventListTwo);
        eventList[2] = findViewById(R.id.gameEventListThree);
        fillEventList();

        framesScoredTextView = findViewById(R.id.framesScoredTextView);

        pointsScoredTextViews = new TextView[2];
        pointsScoredTextViews[0] = findViewById(R.id.firstPlayerScoreTextView);
        pointsScoredTextViews[1] = findViewById(R.id.secondPlayerScoreTextView);
    }

    private void fillEventList() {
        GameEvent[] requestedList = match.getCurrentFrame().getLastEvents(this.eventList.length);
        if (requestedList != null) {
            for (int i = 0; i < requestedList.length; i++) {
                eventList[i].setText(requestedList[i].toString());
            }
        }
    }

    public void updateUI() {
        fillEventList();
        framesScoredTextView.setText(match.getScore());
        for (int i = 0; i < 2; i++)
            pointsScoredTextViews[i].setText(match.getCurrentFrame().getScore(i));
    }
}
