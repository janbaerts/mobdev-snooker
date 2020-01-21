package com.janbaerts.android.snookerscoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.janbaerts.android.snookerscoreboard.models.Player;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";


    private TextView playerList;
    private Player lei;
    private Player jan;
    private Player ethan;
    private Player homer;

    TextView welcomeTextView;

    Button newMatchButton;
    Button signUpButton;
    Button playerStatisticsButton;
    Button loginButton;
    ProgressBar progressBar;

    private List<Player> players;

    private FirebaseFirestore database;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private Player player;

    public MainActivity() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeTextView = findViewById(R.id.welcomeTextView);

        newMatchButton = findViewById(R.id.newMatchButton);
        signUpButton = findViewById(R.id.signUpButton);
        playerStatisticsButton = findViewById(R.id.playerStatisticsButton);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);

        database = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: View player statistics option (Master - Detail fragments requirement assignment).
        // TODO: Transition Portrait-Landscape.
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserState();
    }

    public void startNewGame(View view) {
        Intent intent = new Intent(this, StartNewMatchActivity.class);
        startActivity(intent);
    }

    public void playerStatistics(View view) {
        Intent intent = new Intent(this, PlayerStatisticsActivity.class);
        startActivity(intent);
    }

    public void signUpUser(View view) {
        Intent intent = new Intent(this, CreateNewPlayerActivity.class);
        startActivity(intent);
    }

    public void loginUser(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void logOutUser(View view) {
        firebaseAuth.signOut();
        welcomeTextView.setText(R.string.welcome_message);
        checkUserState();
    }

    private void checkUserState() {
        progressBar.setVisibility(View.VISIBLE);
        user = firebaseAuth.getCurrentUser();

        if (user != null) {
            database.collection("players").whereEqualTo("email", user.getEmail())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            player = queryDocumentSnapshots.toObjects(Player.class).get(0);
                            welcomeTextView.setText(getResources().getString(R.string.welcome) + ", " + player.getFirstname());
                            progressBar.setVisibility(View.INVISIBLE);
                            setUI(true);
                        }
                    });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            setUI(false);
        }
    }

    public void setUI(boolean loggedIn) {
        if (!loggedIn) {
            loginButton.setText(R.string.log_in);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginUser(v);
                }
            });
            newMatchButton.setEnabled(false);
            playerStatisticsButton.setEnabled(false);
            signUpButton.setVisibility(View.VISIBLE);
        } else {
            loginButton.setText(R.string.log_out);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logOutUser(v);
                }
            });
            newMatchButton.setEnabled(true);
            playerStatisticsButton.setEnabled(true);
            signUpButton.setVisibility(View.INVISIBLE);
        }
    }
}
