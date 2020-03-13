package com.janbaerts.android.snookerscoreboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.janbaerts.android.snookerscoreboard.data.FavouriteBall;
import com.janbaerts.android.snookerscoreboard.data.GameEventType;
import com.janbaerts.android.snookerscoreboard.data.Undo;
import com.janbaerts.android.snookerscoreboard.models.Ball;
import com.janbaerts.android.snookerscoreboard.models.Break;
import com.janbaerts.android.snookerscoreboard.models.Frame;
import com.janbaerts.android.snookerscoreboard.models.GameEvent;
import com.janbaerts.android.snookerscoreboard.models.Player;
import com.janbaerts.android.snookerscoreboard.viewmodels.MatchViewModel;

import org.w3c.dom.Text;

public class MatchActivity extends AppCompatActivity {

    private TextView[] pointsScoredTextViews;
    private TextView framesScoredTextView;
    private TextView[] eventList;
    private TextView[] playerNamesTextViews;
    private TextView remainingPointsTextView;
    private Button foulButton;
    private Button undoButton;
    private ProgressBar progressBar;
    private TextView wipDescriptionTextView;

    private MatchViewModel match;

    private boolean isRiggedForFoul;
    private boolean isBusyLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        match = ViewModelProviders.of(this).get(MatchViewModel.class);
        progressBar = findViewById(R.id.progressBar);
        assignAllViews(3);

        if (match == null) {
            Log.e("JB (in onCreate MatchActivity): ", "MatchViewModel not found.");
        } else {
            Log.d("JB", "MatchViewModel found.");
        }

        if (match.getHasToBeInitialized()) {
            initializeMatchViewModel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isBusyLoading)
            updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.match_menu, menu);
        return true;
    }

    public void updateUI() {
        remainingPointsTextView.setText(String.format("%d %s", match.getCurrentFrame().getMaximumRemainingPoints(match.getCurrentBreak()),
                getString(R.string.points_remaining)));
        fillEventList();
        framesScoredTextView.setText(match.getScore());
        for (int i = 0; i < 2; i++) {
            pointsScoredTextViews[i].setText("" + match.getCurrentFrame().getScore(i));
            playerNamesTextViews[i].setText(match.getPlayers()[i].getDisplayName(match.getPlayers()[(i + 1) % 2]));
        }

        if (isRiggedForFoul)
            foulButton.setTextColor(Color.RED);
        else foulButton.setTextColor(Color.BLACK);

        pointsScoredTextViews[match.getPlayerAtTableIndex()].setTextColor(Color.RED);
        playerNamesTextViews[match.getPlayerAtTableIndex()].setTextColor(Color.RED);
        pointsScoredTextViews[match.getPlayerInSeatIndex()].setTextColor(Color.BLACK);
        playerNamesTextViews[match.getPlayerInSeatIndex()].setTextColor(Color.BLACK);
    }

    public void toggleFoul(View view) {
        if (isRiggedForFoul) {
            isRiggedForFoul = false;
            foulButton.setTextColor(Color.BLACK);
        } else {
            isRiggedForFoul = true;
            foulButton.setTextColor(Color.RED);
        }
    }

    public void ballTapped(View view) {
        GameEvent gameEvent = new GameEvent();
        Ball ball = getBallFromView(view);
        Player playerAtTable = match.getPlayers()[match.getPlayerAtTableIndex()];
        Player playerInSeat = match.getPlayers()[match.getPlayerInSeatIndex()];
        gameEvent.setBall(ball);
        gameEvent.setEventIndex(match.getCurrentFrame().getEvents().size() + 1);

        if (isRiggedForFoul) {

            match.getCurrentFrame().addPoints(match.getPlayerInSeatIndex(), ball.getFoulPoints());
            gameEvent.setPlayer(playerInSeat);
            gameEvent.setPlayerIndex(match.getPlayerInSeatIndex());
            gameEvent.setOtherPlayerName(playerAtTable.getDisplayName(playerInSeat));

            match.getCurrentFrame().pushBreak(match.getCurrentBreak());
            if (match.getCurrentBreak().getTotalPoints() > 0) {
                gameEvent.setType(GameEventType.END_OF_BREAK_FOUL);
                gameEvent.setEndedBreak(match.getCurrentFrame().getLastBreak());
            } else {
                gameEvent.setType(GameEventType.FOUL);
            }

            match.nextTurn();

        } else {

            gameEvent.setPlayer(playerAtTable);
            gameEvent.setPlayerIndex(match.getPlayerAtTableIndex());
            gameEvent.setType(GameEventType.POT);
            gameEvent.setEndedBreak(match.getCurrentBreak());
            match.getCurrentBreak().pushPot(ball);
            match.getCurrentFrame().addPoints(match.getPlayerAtTableIndex(), ball.getPoints());
            removeBallsIfNecessary(ball);

        }

        match.getCurrentFrame().pushEvent(gameEvent);
        System.out.println(match.getCurrentBreak().toString());
        isRiggedForFoul = false;
        updateUI();
    }

    public void undoTapped(View view) {
        if (match.getCurrentFrame().getEvents() == null || match.getCurrentFrame().getEvents().size() == 0)
            return;

        System.out.println(match.getCurrentBreak().toString());
        GameEvent gameEvent = match.getCurrentFrame().popEvent();
        Undo undo = new Undo(gameEvent, match.getCurrentFrame());
        match.setCurrentBreak(undo.undo());
        if (match.getCurrentBreak() == null)
            match.setCurrentBreak(new Break(match.getPlayers()[match.getPlayerAtTableIndex()]));
        if (match.getCurrentBreak() != null)
            System.out.println(match.getCurrentBreak().toString());
        updateUI();
    }

    public void endOfTurnTapped(View view) {
        GameEvent gameEvent = new GameEvent();
        gameEvent.setEventIndex(match.getCurrentFrame().getEvents().size() + 1);
        System.out.println(match.getCurrentBreak().toString());

        if (match.getCurrentBreak().getTotalPoints() > 0) {
            gameEvent.setType(GameEventType.END_OF_BREAK);
            gameEvent.setPlayer(match.getPlayers()[match.getPlayerAtTableIndex()]);
            match.getCurrentFrame().pushBreak(match.getCurrentBreak());
            gameEvent.setEndedBreak(match.getCurrentFrame().getLastBreak());
        }
        else {
            gameEvent.setType(GameEventType.END_OF_TURN);
            gameEvent.setPlayer(match.getPlayers()[match.getPlayerAtTableIndex()]);
        }

        match.getCurrentFrame().pushEvent(gameEvent);
        match.setCurrentBreak(new Break(match.getPlayers()[match.getPlayerAtTableIndex()]));
        match.nextTurn();
        updateUI();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addBall:
                match.getCurrentFrame().addBall();
                updateUI();
                return true;
            case R.id.removeBall:
                match.getCurrentFrame().removeBall();
                updateUI();
                return true;
            case R.id.endFrame:
                Player frameLeader = match.getFrameLeader();
                if (frameLeader != null) {
                    confirmEndOfFrame(frameLeader);
                } else {
                    Toast.makeText(this, getString(R.string.cannot_end_frame), Toast.LENGTH_SHORT);
                }
            default:
                return false;

        }
    }

    private void confirmEndOfFrame(Player frameLeader) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_end_of_frame);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Player matchWinner = match.endFrame(frameLeader);
                updateUI();
                if (matchWinner != null) {
                    endMatch();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close dialog.
            }
        });
        builder.show();
    }

    private void endMatch() {
        Toast.makeText(this, "GAME OVER", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    private Ball getBallFromView(View view) {
        switch (view.getId()) {
            case R.id.redBallButton:
                return Ball.RED;
            case R.id.yellowBallButton:
                return Ball.YELLOW;
            case R.id.greenBallButton:
                return Ball.GREEN;
            case R.id.brownBallButton:
                return Ball.BROWN;
            case R.id.blueBallButton:
                return Ball.BLUE;
            case R.id.pinkBallButton:
                return Ball.PINK;
            case R.id.blackBallButton:
                return Ball.BLACK;
            case R.id.cueBallButton:
                return Ball.WHITE;
        }
        return null;
    }

    private void removeBallsIfNecessary(Ball ball) {
        if (match.getCurrentFrame().getRemainingBalls() > 6) {
            if (ball == Ball.RED)
                match.getCurrentFrame().removeBall();
        } else {
            match.getCurrentFrame().removeBall();
        }
    }

    private void initializeMatchViewModel() {
        isBusyLoading = true;
        if (match.getHasToBeInitialized())
            match.initialize(Integer.parseInt(this.getIntent().getStringExtra("maxNumberOfFrames")));

        String firstPlayerId = this.getIntent().getStringExtra("firstPlayer");
        String secondPlayerId = this.getIntent().getStringExtra("secondPlayer");
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference database = firebaseFirestore.collection("players");


        Log.i("JAN", "MaxNumberOfFrames = " + match.getMaxNumberOfFrames());

        Player[] players = new Player[2];
        progressBar.setVisibility(View.VISIBLE);
        database.whereEqualTo("email", firstPlayerId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            players[0] = task.getResult().getDocuments().get(0).toObject(Player.class);
                            database.whereEqualTo("email", secondPlayerId).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                players[1] = task.getResult().getDocuments().get(0).toObject(Player.class);
//                                                assignAllViews(3);
                                                match.setPlayers(players);
                                                match.setCurrentBreak(new Break(players[match.getMatchStarter()]));
                                                match.getCurrentFrame().pushBreak(match.getCurrentBreak());
                                                updateUI();
                                            } else {
                                                loadingFailed(1);
                                            }
                                            progressBar.setVisibility(View.INVISIBLE);
                                            if (players[0] != null)
                                                Toast.makeText(MatchActivity.this,
                                                        getResources().getString(R.string.players_loaded), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            loadingFailed(0);
                        }
                        isBusyLoading = false;
                    }
                });

    }

    private void loadingFailed(int player) {
        Toast.makeText(this, "Couldn't load players, please check your internet connection.", Toast.LENGTH_SHORT).show();
    }

    private void assignAllViews(int listLength) {
        remainingPointsTextView = findViewById(R.id.remainingPointsTextView);

        eventList = new TextView[listLength];
        eventList[0] = findViewById(R.id.gameEventListOne);
        eventList[1] = findViewById(R.id.gameEventListTwo);
        eventList[2] = findViewById(R.id.gameEventListThree);

        framesScoredTextView = findViewById(R.id.framesScoredTextView);

        pointsScoredTextViews = new TextView[2];
        pointsScoredTextViews[0] = findViewById(R.id.firstPlayerScoreTextView);
        pointsScoredTextViews[1] = findViewById(R.id.secondPlayerScoreTextView);

        playerNamesTextViews = new TextView[2];
        playerNamesTextViews[0] = findViewById(R.id.firstPlayerNameTextView);
        playerNamesTextViews[1] = findViewById(R.id.secondPlayerNameTextView);

        foulButton = findViewById(R.id.foulButton);

    }

    private void fillEventList() {
        GameEvent[] requestedList = match.getCurrentFrame().getLastEvents(this.eventList.length);

        for (TextView tv : eventList)
            tv.setText("");

        if (requestedList != null) {
            for (int i = 0; i < requestedList.length; i++) {
                eventList[i].setText(requestedList[i].toString());
            }
        }
    }
}
