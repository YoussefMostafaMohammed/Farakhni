package com.example.farakhni.freatures.splashscreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.farakhni.WelcomeScreen;
import com.example.farakhni.R;
import com.example.farakhni.freatures.mainapp.view.AppActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private static final long SPLASH_DURATION_MS = 4075;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User already signed in → skip straight to main
            startActivity(new Intent(this, AppActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_splash_screen);
        ImageView splashView = findViewById(R.id.iv_splash_gif);
        Glide.with(this)
                .asGif()
                .load(R.raw.splashscreen)
                .into(splashView);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this, WelcomeScreen.class));
            finish();
        }, SPLASH_DURATION_MS);
    }
}
