package com.janbaerts.android.snookerscoreboard.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.janbaerts.android.snookerscoreboard.R;
import com.janbaerts.android.snookerscoreboard.StartNewGameActivity;
import com.janbaerts.android.snookerscoreboard.data.FireStoreDB;
import com.janbaerts.android.snookerscoreboard.models.Player;
import com.janbaerts.android.snookerscoreboard.recyclerviews.SearchPlayerRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectPlayerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectPlayerFragment extends Fragment {

    public static final String TAG = "SelectPlayerFragment";

    private OnFragmentInteractionListener interactionListener;
    private List<Player> playerList = new ArrayList<>();
    private FirebaseFirestore db;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText searchPlayerEditText;
    private Button searchButton;

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
//        buildPlayerList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_player, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.playerListRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchPlayerRecyclerViewAdapter(playerList);
        recyclerView.setAdapter(adapter);
        searchPlayerEditText = (EditText) view.findViewById(R.id.searchPlayerEditText);
        // TODO: Add player clear button.
        searchButton = (Button) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchPlayerEditText.getText().length() != 0)
                    searchPlayersWithSearchString();
                else
                    ((StartNewGameActivity)getActivity()).hideSelectPlayerFragment();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // Rename method, update argument and hook method into UI event
    public void searchButtonPressed(String searchString) {
        if (interactionListener != null) {
            interactionListener.onFragmentInteraction(searchString);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String searchString);
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
            Player player = new Player(firstname, lastname, email);
            playerList.add(player);
        }
    }
}
