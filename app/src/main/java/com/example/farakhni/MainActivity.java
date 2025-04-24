package com.example.farakhni;

import static android.content.ContentValues.TAG;

import static java.sql.DriverManager.println;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.farakhni.model.Meal;
import com.example.farakhni.network.NetworkCallBack;
import com.example.farakhni.network.meal.MealsRemoteDataSourceImpl;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btnRegister = findViewById(R.id.btnSignup);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Signup Screen
                Intent intent = new Intent(MainActivity.this, SignupScreen.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Login Screen
                Intent intent = new Intent(MainActivity.this, LoginScreen.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        MealsRemoteDataSourceImpl.getInstance().makeNetworkCallgetRandomMeal( new NetworkCallBack<List<Meal>>() {
            @Override
            public void onSuccessResult(List<Meal> result) {
                Log.d(TAG, "Meals received: " + result.get(0).getInstructions());
            }

            @Override
            public void onFailureResult(String errorMessage) {
                Log.e(TAG, "Error: " + errorMessage);
            }
        });
    }
}