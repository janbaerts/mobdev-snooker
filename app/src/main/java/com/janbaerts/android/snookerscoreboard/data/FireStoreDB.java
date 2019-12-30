package com.janbaerts.android.snookerscoreboard.data;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.janbaerts.android.snookerscoreboard.models.Player;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class FireStoreDB {

//    public static Set<Player> searchForPlayer(final TreeSet<Player> players, String searchString) {
    public static Set<Player> searchForPlayer(final RecyclerView rv, final TreeSet<Player> players, String searchString) {
        CollectionReference allPlayers = FirebaseFirestore.getInstance().collection("players");
        allPlayers.whereEqualTo("lastname", searchString)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            StringBuilder sb = new StringBuilder();
                            for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                                players.add(ds.toObject(Player.class));
                                sb.append(ds.toObject(Player.class).toString()).append("\n");
                            }
                            rv.getAdapter().notifyDataSetChanged();
//                            System.out.println(sb.toString());
                        }
                    }
                });
        return players;
    }

}
