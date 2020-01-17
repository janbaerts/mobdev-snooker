package com.janbaerts.android.snookerscoreboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.janbaerts.android.snookerscoreboard.models.Player;

import java.util.concurrent.CancellationException;

public class CreateNewPlayerActivity extends AppCompatActivity {

    EditText firstnameEditText;
    EditText lastnameEditText;
    EditText emailEditText;
    TextView infoTextView;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_player);
        firstnameEditText = findViewById(R.id.firstnameEditText);
        lastnameEditText = findViewById(R.id.lastnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        infoTextView = findViewById(R.id.infoTextView);
        database = FirebaseFirestore.getInstance();
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
                .set(player);
        infoTextView.setTextColor(Color.GREEN);
        infoTextView.setText(getResources().getString(R.string.player_created));
    }

}
