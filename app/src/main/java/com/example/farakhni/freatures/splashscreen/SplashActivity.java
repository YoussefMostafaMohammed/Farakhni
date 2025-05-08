package com.example.farakhni.freatures.splashscreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.farakhni.R;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.data.repositories.UserRepositoryImpl;
import com.example.farakhni.freatures.mainapp.view.AppActivity;
import com.example.farakhni.welcome.WelcomeScreen;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private SplashContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserRepositoryImpl userRepository = UserRepositoryImpl.getInstance(
                com.example.farakhni.data.DB.UserLocalDataSourceImpl.getInstance(this));
        MealRepositoryImpl mealRepository = MealRepositoryImpl.getInstance(this);
        presenter = new SplashPresenter(new SplashModel(userRepository, mealRepository));
        presenter.attachView(this);
        presenter.initialize();
    }

    @Override
    public void showSplashGif() {
        setContentView(R.layout.activity_splash_screen);
        ImageView splashView = findViewById(R.id.iv_splash_gif);
        Glide.with(this)
                .asGif()
                .load(R.raw.splashscreen)
                .into(splashView);
    }

    @Override
    public void navigateToWelcomeScreen() {
        startActivity(new Intent(SplashActivity.this, WelcomeScreen.class));
        finish();
    }

    @Override
    public void navigateToAppActivity() {
        startActivity(new Intent(SplashActivity.this, AppActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}