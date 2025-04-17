package com.example.quizapp_sadik;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ResultActivity extends AppCompatActivity {

    TextView tvScore;
    ProgressBar progressBar;
    Button bLogout, bTry;
    int score = 0;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvScore = findViewById(R.id.tvScore);
        progressBar = findViewById(R.id.progressCircle);
        bLogout = findViewById(R.id.bLogout);
        bTry = findViewById(R.id.bTry);
        firebaseAuth = FirebaseAuth.getInstance();

        // Get score from intent
        score = getIntent().getIntExtra("score", 0);
        tvScore.setText(score + "%");

        // Spin progress bar for 2 seconds, then stop (hide)
        //anew Handler().postDelayed(() -> progressBar.setVisibility(ProgressBar.GONE), 2000);

        // Logout logic
        bLogout.setOnClickListener(v -> {
            firebaseAuth.signOut();
            Toast.makeText(getApplicationContext(), "Logged out!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ResultActivity.this, MainActivity.class));
            finish();
        });

        // Try again logic
        bTry.setOnClickListener(v -> {
            startActivity(new Intent(ResultActivity.this, Quiz1.class)); // replace with your quiz class
            finish();
        });
    }
}
