package com.janbaerts.android.snookerscoreboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.janbaerts.android.snookerscoreboard.data.FireStoreDB;
import com.janbaerts.android.snookerscoreboard.models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";


    private TextView playerList;
    private TextView testingTextView;
    private final FirebaseFirestore database;
    private Player lei;
    private Player jan;
    private Player ethan;
    private Player homer;

    private List<Player> players;

    public MainActivity() {
        this.database = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testingTextView = findViewById(R.id.testingTextView);

        // TODO: Add add new player option.
        // TODO: View player statistics option (Master - Detail fragments requirement assignment).
        // TODO: Transition Portrait-Landscape.
    }

    public void startNewGame(View view) {
//        for (int i = 0; i < players.size(); i++) {
//            database.collection("players").document(players.get(i).getEmail()).delete();
//        }
        Intent intent = new Intent(this, StartNewGameActivity.class);
        startActivity(intent);
    }

    public void createNewPlayer(View view) {

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
        if (players.size() > 0)
            testingTextView.setText(players.get(0).toString());
    }

    private void getPlayerList() {
        TreeSet<Player> testingTreeSet = new TreeSet<>();
//        FireStoreDB.searchForPlayer(testingTreeSet, "Baerts");
    }
}
