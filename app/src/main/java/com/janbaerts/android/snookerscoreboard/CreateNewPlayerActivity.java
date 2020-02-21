package com.janbaerts.android.snookerscoreboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.janbaerts.android.snookerscoreboard.data.FavouriteBall;
import com.janbaerts.android.snookerscoreboard.fragments.SelectPictureDialogFragment;
import com.janbaerts.android.snookerscoreboard.models.Player;
import com.janbaerts.android.snookerscoreboard.viewmodels.CreateNewPlayerViewModel;

public class CreateNewPlayerActivity extends AppCompatActivity
        implements SelectPictureDialogFragment.SelectedPictureListener {

    EditText firstnameEditText;
    EditText lastnameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText verifyPasswordEditText;
    EditText[] allEditTexts;
    TextView infoTextView;
    ProgressBar progressBar;
    FavouriteBall favouriteBall = FavouriteBall.REDBALL;
    ImageView favouriteBallImageView;

    CreateNewPlayerViewModel viewModel;
    FirebaseFirestore database;
    FirebaseAuth firebaseAuth;

    // LifecycleHooks
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_player);
        viewModel = ViewModelProviders.of(this).get(CreateNewPlayerViewModel.class);
        firstnameEditText = findViewById(R.id.firstnameEditText);
        lastnameEditText = findViewById(R.id.lastnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        verifyPasswordEditText = findViewById(R.id.verifyPasswordEditText);
        fillAllEditTexts();
        infoTextView = findViewById(R.id.infoTextView);
        progressBar = findViewById(R.id.progressBar);
        favouriteBallImageView = findViewById(R.id.favouriteBallImageView);

        database = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreFields();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveFields();
    }

    // ViewModel methods
    private void fillAllEditTexts() {
        allEditTexts = new EditText[5];
        allEditTexts[0] = firstnameEditText;
        allEditTexts[1] = lastnameEditText;
        allEditTexts[2] = emailEditText;
        allEditTexts[3] = passwordEditText;
        allEditTexts[4] = verifyPasswordEditText;
    }

    private void restoreFields() {
        for (int i = 0; i < allEditTexts.length; i++) {
            if (viewModel.getFields() != null)
                allEditTexts[i].setText(viewModel.getFields()[i]);
        }
        if (viewModel.getFavouriteBall().getResource() != 0) {
            favouriteBallImageView.setImageDrawable(getResources().getDrawable(viewModel.getFavouriteBall().getResource(), null));
            favouriteBall = viewModel.getFavouriteBall();
        }

    }

    private void saveFields() {
        for (int i = 0; i < allEditTexts.length; i++) {
            viewModel.setField(i, allEditTexts[i].getText().toString());
        }
        viewModel.setFavouriteBall(favouriteBall);
    }

    // Eventhandlers
    public void imageTapped(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        SelectPictureDialogFragment dialogFragment = new SelectPictureDialogFragment();
        dialogFragment.show(transaction, "SelectPictureDialogFragment");
    }

    public void createPlayerTapped(View view) {
        String result = validateInputFields();
        System.out.println(validateInputFields());
        if (result.equals("")) {
            tryToPersistNewPlayer();
        } else {
            infoTextView.setText(result);
        }
    }

    private String validateInputFields() {
        StringBuilder sb = new StringBuilder();

        if (firstnameEditText.getText().toString().equals(""))
            sb.append(getResources().getString(R.string.firstname_required) + "\n");

        if (lastnameEditText.getText().toString().equals(""))
            sb.append(getResources().getString(R.string.lastname_required) + "\n");

        if (emailEditText.getText().toString().equals("")) {
            sb.append(getResources().getString(R.string.email_required));
        } else if (!emailEditText.getText().toString().contains("@") || !emailEditText.getText().toString().contains("."))
            sb.append(getResources().getString(R.string.email_not_valid) + "\n");

        if (passwordEditText.getText().toString().length() < 6)
            sb.append(getResources().getString(R.string.password_too_short));
        else if (!passwordEditText.getText().toString().equals(verifyPasswordEditText.getText().toString()))
            sb.append(getResources().getString(R.string.passwords_do_not_match));

        return sb.toString();
    }

    private void tryToPersistNewPlayer() {
        progressBar.setVisibility(View.VISIBLE);
        infoTextView.setText(getResources().getString(R.string.checking_player_id));
        database.collection("players")
                .whereEqualTo("email", emailEditText.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().getDocuments().size() != 0) {
                        infoTextView.setTextColor(Color.RED);
                        infoTextView.setText(R.string.player_already_exists);
                    } else {
                        Player player = new Player(firstnameEditText.getText().toString(),
                                lastnameEditText.getText().toString(),
                                emailEditText.getText().toString(), favouriteBall);
                        savePlayerToDatabase(player);
                    }
                });
    }

    private void savePlayerToDatabase(Player player) {
        infoTextView.setText(getResources().getString(R.string.player_being_created));
        database.collection("players").document(emailEditText.getText().toString())
                .set(player)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        infoTextView.setTextColor(Color.GREEN);
                        infoTextView.setText(getResources().getString(R.string.player_created));
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateNewPlayerActivity.this, getResources().getString(R.string.player_creation_failure),
                                Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(player.getEmail(), passwordEditText.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        infoTextView.setTextColor(Color.BLUE);
                        infoTextView.setText(getResources().getString(R.string.player_signed_in));
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();
                    }
                });
    }

    @Override
    public void onPictureTapped(int selectedColor) {
        Log.i("JAN", "Favourite ball = " + FavouriteBall.values()[selectedColor].toString());
        this.favouriteBall = FavouriteBall.values()[selectedColor];
        favouriteBallImageView.setImageDrawable(getResources().getDrawable(favouriteBall.getResource(), null));
    }

}
