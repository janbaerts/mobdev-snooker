package com.janbaerts.android.snookerscoreboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.janbaerts.android.snookerscoreboard.models.Player;

import java.util.concurrent.CancellationException;

public class CreateNewPlayerActivity extends AppCompatActivity {

    EditText firstnameEditText;
    EditText lastnameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText verifyPasswordEditText;
    TextView infoTextView;
    FirebaseFirestore database;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_player);
        firstnameEditText = findViewById(R.id.firstnameEditText);
        lastnameEditText = findViewById(R.id.lastnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        verifyPasswordEditText = findViewById(R.id.verifyPasswordEditText);
        infoTextView = findViewById(R.id.infoTextView);
        database = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
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
                                emailEditText.getText().toString());
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
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateNewPlayerActivity.this, getResources().getString(R.string.player_creation_failure),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        firebaseAuth.createUserWithEmailAndPassword(player.getEmail(), passwordEditText.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        infoTextView.setTextColor(Color.BLUE);
                        infoTextView.setText(getResources().getString(R.string.player_signed_in));
                        finish();
                    }
                });
    }

}
