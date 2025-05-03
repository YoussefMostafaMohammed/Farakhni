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

import com.example.farakhni.data.DB.AppDataBase;
import com.example.farakhni.freatures.auth.login.LoginContract;
import com.example.farakhni.freatures.auth.login.LoginPresenter;
import com.example.farakhni.freatures.auth.login.view.LoginScreen;
import com.example.farakhni.freatures.mainapp.view.AppActivity;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.network.ingredient.IngredientsRemoteDataSourceImpl;
import com.example.farakhni.freatures.auth.signup.view.SignupScreen;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class WelcomeScreen extends AppCompatActivity implements LoginContract.View {
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
            startActivity(new Intent(this, AppActivity.class));
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
                Intent intent = new Intent(WelcomeScreen.this, SignupScreen.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreen.this, LoginScreen.class);
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

            @Override
            public void onLoading() {

            }

            @Override
            public void onNetworkError(String errorMessage) {

            }

            @Override
            public void onEmptyData() {

            }

            @Override
            public void onProgress(int progress) {

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
                Intent intent = new Intent(WelcomeScreen.this, AppActivity.class);
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
        Intent intent = new Intent(WelcomeScreen.this, AppActivity.class);
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
        AppDataBase.getInstance(this).forceCheckpoint();
    }
    @Override
    protected void onPause() {
        super.onPause();
        AppDataBase.getInstance(this).forceCheckpoint();
    }
}