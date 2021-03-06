package com.janbaerts.android.snookerscoreboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.janbaerts.android.snookerscoreboard.data.AppConstants;
import com.janbaerts.android.snookerscoreboard.data.MatchSettingsData;
import com.janbaerts.android.snookerscoreboard.models.Player;
import com.janbaerts.android.snookerscoreboard.viewmodels.MatchSettingsViewModel;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private TextView playerList;
    private Player lei;
    private Player jan;
    private Player ethan;
    private Player homer;

    TextView welcomeTextView;

    ImageView playerPictureImageView;
    Button firstButton;
    Button secondButton;
    ProgressBar progressBar;

    private List<Player> players;

    private FirebaseFirestore database;
    private FirebaseAuth firebaseAuth;
    private StorageReference storage;
    private FirebaseUser user;
    private Player player;

    public MainActivity() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeTextView = findViewById(R.id.welcomeTextView);

        playerPictureImageView = findViewById(R.id.playerPictureImageView);
        firstButton = findViewById(R.id.firstButton);
        secondButton = findViewById(R.id.secondButton);
        progressBar = findViewById(R.id.progressBar);

        MatchSettingsData.matchSettingsViewModel = ViewModelProviders.of(this).get(MatchSettingsViewModel.class);
        MatchSettingsData.noPicture = getDrawable(R.drawable.no_picture);

        database = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference("images");
        getFirebaseInstanceId();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserState();
    }

/// Eventhandlers -------------------------------------------------------------------------------
    public void startNewMatch(View view) {
        Intent intent = new Intent(this, MatchSettingsActivity.class);
        startActivity(intent);
    }

    public void registerUser(View view) {
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

    public void recordNewPictureForPlayer(View view) {
        if (user != null) {
            Intent getPictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (getPictureIntent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(getPictureIntent, AppConstants.REQUEST_IMAGE_CAPTURE);
        } else {
            Log.e("JB", "User must be logged in.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap)extras.get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 53, baos);
            savePictureToFirebase(baos.toByteArray());

            playerPictureImageView.setImageBitmap(image);
        }
    }

    /// Helper methods -----------------------------------------------------------------------------
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
                            setUI(true);
                            loadPlayerPicture();
                        }
                    });

        } else {
            progressBar.setVisibility(View.INVISIBLE);
            setUI(false);
        }
    }

    public void setUI(boolean loggedIn) {
        if (!loggedIn) {
            firstButton.setText(R.string.log_in);
            firstButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginUser(v);
                }
            });
            secondButton.setText(getString(R.string.register));
            secondButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { registerUser(v); }
            });
            playerPictureImageView.setImageDrawable(getDrawable(R.drawable.no_picture));
        } else {
            firstButton.setText(R.string.new_match);
            firstButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNewMatch(v);
                }
            });
            secondButton.setText(R.string.log_out);
            secondButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { logOutUser(v); }
            });
        }
    }

    private void loadPlayerPicture() {
        progressBar.setVisibility(View.VISIBLE);
        try {
            StorageReference location = storage.child(user.getEmail() + ".png");
            location.getBytes(1024 * 1024)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap playerPicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            playerPictureImageView.setImageBitmap(playerPicture);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        } catch (Exception e) {
            Log.e("JB", e.getMessage());
        }
    }

    private void savePictureToFirebase(byte[] image) {
        StorageReference newPictureReference = storage.child(user.getEmail() + ".png");
        UploadTask uploadTask = newPictureReference.putBytes(image);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("JB", e.getMessage());
            }
        })
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("JB", "Upload successful.");
                loadPlayerPicture();
            }
        });
    }

    private void getFirebaseInstanceId() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            Log.i("JB Instance ID = ", token);
                            System.out.println("Instance ID = " + token);
                        }
                    }
                });
    }

}
