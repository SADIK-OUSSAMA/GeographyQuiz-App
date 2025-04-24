package com.example.quizapp_sadik;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {

    Button bLogout, bTry, btnToLocal;
    ProgressBar progressBar;
    TextView tvScore;
    int score;
    final int totalQuestions = 10; // Set this according to your total quiz count

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize views
        tvScore = findViewById(R.id.tvScore);
        progressBar = findViewById(R.id.progressCircle);
        bLogout = findViewById(R.id.bLogout);
        bTry = findViewById(R.id.bTry);
        btnToLocal = findViewById(R.id.btnTolocal);

        // Get score from previous activity
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);

        // Calculate percentage
        int percentage = (100 * score) / totalQuestions;
        animateProgressBar(percentage);
        tvScore.setText(percentage + " %");

        // Logout Button
        bLogout.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Merci Pour votre Participation !", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Try Again Button
        bTry.setOnClickListener(v -> {
            Intent retryIntent = new Intent(ResultActivity.this, Quiz1.class);
            startActivity(retryIntent);
            finish();
        });

        // Map Button
        btnToLocal.setOnClickListener(v -> {
            Intent mapIntent = new Intent(ResultActivity.this, MapsActivity.class);
            startActivity(mapIntent);
        });
    }

    // Animate progress bar
    private void animateProgressBar(int percentage) {
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, percentage);
        animation.setDuration(1000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }
}
