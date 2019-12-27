package com.janbaerts.android.snookerscoreboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.janbaerts.android.snookerscoreboard.models.Player;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView playerList;
    private final FirebaseFirestore database;
    private Player lei;
    private Player jan;
    private Player ethan;

    public MainActivity() {
        this.database = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerList = findViewById(R.id.tv_player_list);

//        lei = new Player("Lei", "Wang", "lw@hotmail.com");
//        jan = new Player("Jan", "Baerts", "janbaerts@hotmail.com");
//        ethan = new Player("Ethan", "Baerts", "eb@hotmail.com");

//        deleteAllPlayers();
//        addPlayers();
//        fillPlayerList();
    }

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
        playerList.setText(players.get(0).toString());
    }
}
