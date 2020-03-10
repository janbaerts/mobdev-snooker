package com.janbaerts.android.snookerscoreboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavAction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.janbaerts.android.snookerscoreboard.data.AppConstants;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    CheckBox rememberMeCheckBox;

    Button loginButton;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        progressBar = findViewById(R.id.progressBar);

        loginButton = findViewById(R.id.loginButton);

        firebaseAuth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences(AppConstants.PREFERENCES_FILE, MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.getBoolean(AppConstants.REMEMBER_USERNAME, false)) {
            rememberMeCheckBox.setChecked(true);
            String userName = preferences.getString(AppConstants.USERNAME, "");
            emailEditText.setText(userName);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rememberMeCheckBox.isChecked())
            saveUserNameInPreferences();
    }

    public void loginUser(View view) {
        saveUserNameInPreferences();
        Toast.makeText(this, "Signing in.", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.player_signed_in), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();
                    }
                });
    }

    public void onRememberMeToggled(View view) {
        preferences.edit().putBoolean(AppConstants.REMEMBER_USERNAME, rememberMeCheckBox.isChecked()).apply();
        preferences.edit().remove(AppConstants.USERNAME).apply();
    }

    public void saveUserNameInPreferences() {
        preferences.edit().putString(AppConstants.USERNAME, emailEditText.getText().toString()).apply();
    }

    public void triggerSaveUserName(View view) {
        saveUserNameInPreferences();
    }
}
