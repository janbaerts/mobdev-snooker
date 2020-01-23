package com.janbaerts.android.snookerscoreboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.janbaerts.android.snookerscoreboard.models.GameEvent;
import com.janbaerts.android.snookerscoreboard.models.Player;
import com.janbaerts.android.snookerscoreboard.viewmodels.MatchViewModel;

public class MatchActivity extends AppCompatActivity {

    private TextView[] pointsScoredTextViews;
    private TextView framesScoredTextView;
    private TextView[] eventList;
    private Button foulButton;
    private Button undoButton;
    private ProgressBar progressBar;
    private TextView wipDescriptionTextView;

    private MatchViewModel match;

    private boolean isRiggedForFoul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        match = ViewModelProviders.of(this).get(MatchViewModel.class);

        loadPlayers();

        assignAllViews(3);
    }

    private void loadPlayers() {
        String firstPlayerId = this.getIntent().getStringExtra("firstPlayer");
        String secondPlayerId = this.getIntent().getStringExtra("secondPlayer");
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference database = firebaseFirestore.collection("players");

        Player[] players = new Player[2];
        database.whereEqualTo("email", firstPlayerId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                            players[0] = task.getResult().toObjects(Player.class).get(0);
                        else
                            loadingFailed(0);
                    }
                });
        database.whereEqualTo("email", secondPlayerId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                            players[1] = task.getResult().toObjects(Player.class).get(1);
                        else loadingFailed(1);
                    }
                });
    }

    private void loadingFailed(int player) {

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

        progressBar = findViewById(R.id.progressBar);

        foulButton = findViewById(R.id.foulButton);
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

        if (isRiggedForFoul)
            foulButton.setTextColor(Color.RED);
        else foulButton.setTextColor(Color.BLACK);
    }

    public void toggleFoul(View view) {
        if (isRiggedForFoul)
            isRiggedForFoul = false;
        else isRiggedForFoul = true;
        updateUI();
    }

    public void ballTapped(View view) {
        switch (view.getId()) {
            case R.id.redBallButton:
                System.out.println("Red ball button pressed.");
                break;
            case R.id.yellowBallButton:
                System.out.println("Yellow ball button pressed.");
                break;
            case R.id.greenBallButton:
                System.out.println("Green ball button pressed.");
                break;
        }
    }
}
