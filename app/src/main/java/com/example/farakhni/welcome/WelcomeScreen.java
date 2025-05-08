package com.example.farakhni.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.farakhni.R;
import com.example.farakhni.freatures.auth.login.view.LoginActivity;
import com.example.farakhni.freatures.auth.signup.view.SignupActivity;
import com.example.farakhni.freatures.mainapp.view.AppActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeScreen extends AppCompatActivity implements WelcomeContract.View {
    private GoogleSignInClient googleClient;
    private WelcomeContract.Presenter presenter;
    private static final int RC_SIGN_IN = 9001;
    private Button btnRegister;
    private Button btnLogin;
    private Button btnGoogle;
    private Button btnGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            presenter = new WelcomePresenter(new WelcomeModel(this));
            presenter.attachView(this);
            presenter.syncData(user.getUid());
            return;
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        btnRegister = findViewById(R.id.btnSignup);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnGuest = findViewById(R.id.btnGuest);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleClient = GoogleSignIn.getClient(this, gso);
        presenter = new WelcomePresenter(new WelcomeModel(this));
        presenter.attachView(this);

        btnRegister.setOnClickListener(v -> presenter.onSignupClicked());
        btnLogin.setOnClickListener(v -> presenter.onLoginClicked());
        btnGoogle.setOnClickListener(v -> presenter.onGoogleSignInClicked());
        btnGuest.setOnClickListener(v -> presenter.onGuestClicked());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void showProgress() {
        runOnUiThread(() -> {
            if (btnLogin != null) btnLogin.setEnabled(false);
            if (btnRegister != null) btnRegister.setEnabled(false);
            if (btnGoogle != null) btnGoogle.setEnabled(false);
            if (btnGuest != null) btnGuest.setEnabled(false);
        });
    }

    @Override
    public void hideProgress() {
        runOnUiThread(() -> {
            if (btnLogin != null) btnLogin.setEnabled(true);
            if (btnRegister != null) btnRegister.setEnabled(true);
            if (btnGoogle != null) btnGoogle.setEnabled(true);
            if (btnGuest != null) btnGuest.setEnabled(true);
        });
    }

    @Override
    public void showError(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void showMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(WelcomeScreen.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToSignup() {
        Intent intent = new Intent(WelcomeScreen.this, SignupActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToApp() {
        Intent intent = new Intent(WelcomeScreen.this, AppActivity.class);
        startActivity(intent);
        finish();
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
        presenter.detachView();
        presenter.cancelOperations();
        super.onDestroy();
    }
}