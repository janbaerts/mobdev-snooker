package com.janbaerts.android.snookerscoreboard.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.janbaerts.android.snookerscoreboard.R;
import com.janbaerts.android.snookerscoreboard.SelectPlayerActivity;
import com.janbaerts.android.snookerscoreboard.data.FavouriteBall;
import com.janbaerts.android.snookerscoreboard.models.Player;
import com.janbaerts.android.snookerscoreboard.recyclerviews.SearchPlayerRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectPlayerFragment extends Fragment {

    public static final String TAG = "SelectPlayerFragment";

//    private OnFragmentInteractionListener interactionListener;
    private List<Player> playerList = new ArrayList<>();
    private FirebaseFirestore db;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText searchPlayerEditText;
    private Button searchButton;

    public SelectPlayerActivity selectPlayerActivity;

    public SelectPlayerFragment() { }

    public static SelectPlayerFragment newInstance() {
        SelectPlayerFragment fragment = new SelectPlayerFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        selectPlayerActivity = (SelectPlayerActivity)getActivity();
//        buildPlayerList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_player, container, false);
        recyclerView = view.findViewById(R.id.playerListRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchPlayerRecyclerViewAdapter(playerList);
        recyclerView.setAdapter(adapter);
        searchPlayerEditText = view.findViewById(R.id.searchPlayerEditText);
        // TODO: Add player clear button.
        searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchPlayerEditText.getText().length() != 0)
                    searchPlayersWithSearchString();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void searchPlayersWithSearchString() {
        // TODO: Enable case insensitive queries.
        String[] searchStrings = searchPlayerEditText.getText().toString().split(" ");
        queryFirebase(searchStrings);
    }

    public void queryFirebase(String[] searchStrings) {
        CollectionReference playerDatabase = db.collection("players");
        String[] searchFields = {"firstname", "lastname", "email"};
        playerList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < searchStrings.length; j++) {
                playerDatabase.whereEqualTo(searchFields[i], searchStrings[j])
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                StringBuilder sb = new StringBuilder();
                                for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                                    if (!playerList.contains(ds.toObject(Player.class)))
                                        playerList.add(ds.toObject(Player.class));
                                    sb.append(ds.toObject(Player.class).toString()).append("\n");
                                }
                                System.out.println(sb.toString());
                                System.out.println("Playerlist size = " + playerList.size());
                                adapter = new SearchPlayerRecyclerViewAdapter(playerList);
                                recyclerView.setAdapter(adapter);
                                selectPlayerActivity.matchSettingsViewModel.setQueryResult(playerList);
                                System.out.println(recyclerView.getAdapter().getItemCount());
                            }
                        });
            }
        }
    }

    public void buildPlayerList() {
        String firstname = "Player";
        playerList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String lastname = "" + i;
            String email = firstname + lastname + "@ssb.com";
            Player player = new Player(firstname, lastname, email, FavouriteBall.REDBALL);
            playerList.add(player);
        }
    }
}
