package com.janbaerts.android.snookerscoreboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.janbaerts.android.snookerscoreboard.models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

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

        database = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if (user != null) {
            database.collection("players").whereEqualTo("email", user.getEmail())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            player = queryDocumentSnapshots.toObjects(Player.class).get(0);
                            welcomeTextView.setText(getResources().getString(R.string.welcome) + ", " + player.getFirstname());
                        }
                    });
        }

        System.out.println(firebaseAuth.getCurrentUser().toString());

        // TODO: Add add new player option.
        // TODO: View player statistics option (Master - Detail fragments requirement assignment).
        // TODO: Transition Portrait-Landscape.
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Current FirebaseUser = " + firebaseAuth.getCurrentUser().getEmail());
    }

    public void startNewGame(View view) {
//        for (int i = 0; i < players.size(); i++) {
//            database.collection("players").document(players.get(i).getEmail()).delete();
//        }
        Intent intent = new Intent(this, StartNewGameActivity.class);
        startActivity(intent);
    }

    public void createNewPlayer(View view) {
        Intent intent = new Intent(this, CreateNewPlayerActivity.class);
        startActivity(intent);
    }

    public void playerStatistics(View view) {
//        clearPlayersCollection();
//        String[] lastnames = {"Marge", "Bart", "Lisa", "Maggie", "Moe", "Seymour", "Monty", "Barney", "Apu", "Milhouse", "Edna", "Selma", "Patty",
//            "Moe", "Nelson", "Fox", "Dana"};
//        for (int i = 0; i < lastnames.length; i++) {
//            Player player = new Player("a", lastnames[i], lastnames[i].toLowerCase() + "@springfield.us");
//            database.collection("players").document(player.getEmail()).set(player);
//        }
        Intent intent = new Intent(this, PlayerStatisticsActivity.class);
        startActivity(intent);
    }

    public void signUpUser(View view) {
        Intent intent = new Intent(this, CreateNewPlayerActivity.class);
        startActivity(intent);
    }

    public void testMethod(View view) {

    }




    // FireStore experiments...
    private void addPlayers() {
        database.collection("players").document(lei.getEmail()).set(lei);
        database.collection("players").document(jan.getEmail()).set(jan);
        database.collection("players").document(ethan.getEmail()).set(ethan);
    }

    private void deleteAllPlayers() {
        database.collection("players").document(lei.getEmail()).delete();
        database.collection("players").document(jan.getEmail()).delete();
        database.collection("players").document(ethan.getEmail()).delete();
    }

    private void clearPlayersCollection() {
        players = new ArrayList<>();
        database.collection("players").get()
                .addOnCompleteListener((task) -> {
                    players.addAll(task.getResult().toObjects(Player.class));
                });
    }

    private void fillPlayerList() {
        StringBuilder sb = new StringBuilder();
        final List<Player> players = new ArrayList<Player>();
        database.collection("players")
                .document("janbaerts@hotmail.com").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        players.add(task.getResult().toObject(Player.class));
                    }
                });
    }

    private void getPlayerList() {
        TreeSet<Player> testingTreeSet = new TreeSet<>();
//        FireStoreDB.searchForPlayer(testingTreeSet, "Baerts");
    }
}
