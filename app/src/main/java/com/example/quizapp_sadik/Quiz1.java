package com.example.quizapp_sadik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;

public class Quiz1 extends AppCompatActivity {

    RadioGroup rg;
    RadioButton rb, rb1, rb2;
    Button bNext;
    TextView questionText;
    ImageView questionImage;
    String RepCorrect = "";
    int score = 0;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);

        rg = findViewById(R.id.rg);
        bNext = findViewById(R.id.bNext);
        questionText = findViewById(R.id.questionText); // new TextView ID
        questionImage = findViewById(R.id.questionImage); // new ImageView ID
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);

        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("geoQuiz").document("Quiz1");

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                questionText.setText(documentSnapshot.getString("Question"));
                rb1.setText(documentSnapshot.getString("Answer 1"));
                rb2.setText(documentSnapshot.getString("Answer 2"));
                RepCorrect = documentSnapshot.getString("RepCorrect");

                String imageUrl = documentSnapshot.getString("ImageURL");
                Glide.with(this).load(imageUrl).into(questionImage);
            } else {
                Toast.makeText(this, "Données non trouvées pour Quiz1", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Erreur lors du chargement", Toast.LENGTH_SHORT).show()
        );

        bNext.setOnClickListener(v -> {
            if (rg.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "Merci de choisir une réponse S.V.P !", Toast.LENGTH_SHORT).show();
            } else {
                rb = findViewById(rg.getCheckedRadioButtonId());
                if (rb.getText().toString().equals(RepCorrect)) {
                    score += 1;
                }
                Intent intent = new Intent(Quiz1.this, Quiz2.class);
                intent.putExtra("score", score);
                startActivity(intent);
                overridePendingTransition(R.anim.exit, R.anim.entry);
                finish();
            }
        });
    }
}
