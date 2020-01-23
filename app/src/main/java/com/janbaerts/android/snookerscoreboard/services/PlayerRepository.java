package com.janbaerts.android.snookerscoreboard.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.janbaerts.android.snookerscoreboard.models.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerRepository implements PlayerRepositoryInterface {

    private static final PlayerRepository ourInstance = new PlayerRepository();
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference database;
    private List<Player> allPlayers;

    private Player firstPlayer;
    private Player secondPlayer;

    public static PlayerRepository getInstance() {
        return ourInstance;
    }

    private PlayerRepository() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        database = firebaseFirestore.collection("players");
        refresh();
    }

    public List<Player> getAllPlayers(boolean refresh) {
        if (refresh) {
            allPlayers = new ArrayList<>();
            database.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot qs : task.getResult()) {
                            allPlayers.add(qs.toObject(Player.class));
                        }
                    } else {
                        Log.e("JAN", "Error getting players from Firebase.");
                    }
                }
            });
        }
        return allPlayers;
    }

    public Player findPlayer(String id) {
        for (Player p : allPlayers) {
            if (p.getEmail().equalsIgnoreCase(id))
                return p;
        }
        return null;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public void setSecondPlayer(Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public void refresh() {
        getAllPlayers(true);
    }

}
