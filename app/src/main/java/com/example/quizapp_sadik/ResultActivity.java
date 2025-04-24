package com.example.quizapp_sadik;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {

    Button bLogout, bTry, btnToLocal, bCamera;
    ProgressBar progressBar;
    TextView tvScore;
    int score;
    final int totalQuestions = 10; // Set this according to your total quiz count

    String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
        bCamera = findViewById(R.id.bCamera);

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

        // Camera Button
        bCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            } else {
                dispatchTakePictureIntent();
            }
        });
    }

    // Animate progress bar
    private void animateProgressBar(int percentage) {
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, percentage);
        animation.setDuration(1000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Failed to create image file", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.quizapp_sadik.fileprovider",  // <- replace with your app's package name if different
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
