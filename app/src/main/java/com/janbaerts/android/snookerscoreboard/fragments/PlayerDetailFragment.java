package com.janbaerts.android.snookerscoreboard.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.janbaerts.android.snookerscoreboard.R;
import com.janbaerts.android.snookerscoreboard.data.MatchSettingsData;
import com.janbaerts.android.snookerscoreboard.viewmodels.MatchSettingsViewModel;

import org.w3c.dom.Text;

public class PlayerDetailFragment extends Fragment {

    public static final String TAG = "PlayerDetailFragment";

    private StorageReference storage = FirebaseStorage.getInstance().getReference("images");
    private MatchSettingsViewModel matchSettingsViewModel;

    private ImageView playerImageView;
    private TextView playerNameTextView;
    private TextView playerUserNameTextView;
    private ProgressBar progressBar;

    public PlayerDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_detail, container, false);

        matchSettingsViewModel = MatchSettingsData.matchSettingsViewModel;

        playerImageView = view.findViewById(R.id.playerPictureImageView);
        playerNameTextView = view.findViewById(R.id.playerNameTextView);
        playerUserNameTextView = view.findViewById(R.id.playerUserNameTextView);
        progressBar = view.findViewById(R.id.progressBar);

        return view;
    }

    public void showPlayerStoredInViewModel() {
        playerNameTextView.setText(matchSettingsViewModel.getSelectedPlayer().getFullName());
        playerUserNameTextView.setText(matchSettingsViewModel.getSelectedPlayer().getEmail());
        setPlayerPicture();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (matchSettingsViewModel.getSelectedPlayer() != null)
            showPlayerStoredInViewModel();
    }

    private void setPlayerPicture() {
        StorageReference location = storage.child(matchSettingsViewModel.getSelectedPlayer().getEmail() + ".png");
        location.getBytes(1024 * 1024)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap playerPicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        playerImageView.setImageBitmap(playerPicture);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        playerImageView.setImageDrawable(MatchSettingsData.noPicture);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

}
