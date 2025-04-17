package com.example.quizapp_sadik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Quiz6 extends AppCompatActivity {

    RadioGroup rg;
    RadioButton rb;
    Button bNext;
    TextView questionText;
    ImageView questionImage;
    RadioButton rb1, rb2;
    String RepCorrect = "";
    int score = 0;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz6);

        rg = findViewById(R.id.rg);
        bNext = findViewById(R.id.bNext);
        questionText = findViewById(R.id.questionText);
        questionImage = findViewById(R.id.questionImage);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);

        score = getIntent().getIntExtra("score", 0); // score from previous

        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("geoQuiz").document("Quiz6");

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String question = documentSnapshot.getString("Question");
                String answer1 = documentSnapshot.getString("Answer 1");
                String answer2 = documentSnapshot.getString("Answer 2");
                RepCorrect = documentSnapshot.getString("RepCorrect");
                String imageUrl = documentSnapshot.getString("URL");

                questionText.setText(question);
                rb1.setText(answer1);
                rb2.setText(answer2);

                Glide.with(this).load(imageUrl).into(questionImage);
            } else {
                Toast.makeText(this, "Quiz6 not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show());

        bNext.setOnClickListener(v -> {
            if (rg.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "Merci de choisir une réponse S.V.P !", Toast.LENGTH_SHORT).show();
            } else {
                rb = findViewById(rg.getCheckedRadioButtonId());
                if (rb.getText().toString().equals(RepCorrect)) {
                    score += 1;
                }
                // Change Quiz7.class to the next activity or show score
                Intent intent = new Intent(Quiz6.this, ResultActivity.class);
                intent.putExtra("score", score);
                startActivity(intent);
                overridePendingTransition(R.anim.exit, R.anim.entry);
                finish();
            }
        });
    }
}
