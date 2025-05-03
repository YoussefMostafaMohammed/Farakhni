package com.example.farakhni;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.farakhni.DB.FavoriteMealDataBase;
import com.example.farakhni.firebase.login.LoginContract;
import com.example.farakhni.firebase.login.LoginPresenter;
import com.example.farakhni.login.LoginScreen;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.network.NetworkCallBack;
import com.example.farakhni.network.ingredient.IngredientsRemoteDataSourceImpl;
import com.example.farakhni.signup.SignupScreen;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginContract.View {
    private GoogleSignInClient googleClient;
    private LoginContract.Presenter presenter;

    private static final int RC_SIGN_IN = 9001;
    Button btnRegister;
    Button btnLogin;
    Button btnGoogle;
    Button btnGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Already signed in
            startActivity(new Intent(this, AppScreen.class));
            finish();
            return;
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
         btnRegister = findViewById(R.id.btnSignup);
         btnLogin = findViewById(R.id.btnLogin);
         btnGoogle=findViewById(R.id.btnGoogle);
         btnGuest=findViewById(R.id.btnGuest);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleClient = GoogleSignIn.getClient(this, gso);
        presenter  = new LoginPresenter(this);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupScreen.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginScreen.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        IngredientsRemoteDataSourceImpl.getInstance().makeNetworkCall(new NetworkCallBack<List<Ingredient>>() {
            @Override
            public void onSuccessResult(List<Ingredient> result) {
                Log.d(TAG, "Meals received: " + result.get(0).getIngredient());
            }
            @Override
            public void onFailureResult(String errorMessage) {
                Log.e(TAG, "Error: " + errorMessage);
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startGoogleSignIn();
            }
        });

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AppScreen.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void onLoginSuccess(FirebaseUser user) {
        Intent intent = new Intent(MainActivity.this, AppScreen.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(String errorMessage) {
        Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmailVerified(boolean exists) {

    }

    @Override
    public void onEmailVerificationFailed(String errorMessage) {

    }

    @Override
    public void onResetEmailSent() {

    }

    @Override
    public void onResetEmailFailed(String errorMessage) {

    }

    @Override
    public void onPasswordChangeSuccess() {

    }

    @Override
    public void onPasswordChangeFailure(String errorMessage) {

    }

    @Override
    public void launchGoogleSignInIntent() {
        Intent intent = googleClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            presenter.handleGoogleSignInResult(data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FavoriteMealDataBase.getInstance(this).forceCheckpoint();
    }
    @Override
    protected void onPause() {
        super.onPause();
        FavoriteMealDataBase.getInstance(this).forceCheckpoint();
    }
}