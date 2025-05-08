package com.example.farakhni.freatures.auth.signup.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.farakhni.R;
import com.example.farakhni.freatures.auth.login.view.LoginActivity;
import com.example.farakhni.freatures.auth.signup.SignUpContract;
import com.example.farakhni.freatures.auth.signup.SignUpPresenter;
import com.example.farakhni.freatures.mainapp.view.AppActivity;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity implements SignUpContract.View {
    private TextView editFullName;
    private TextView editSignupEmail;
    private TextView editSignupPassword;
    private TextView editConfirmPassword;
    private ProgressBar progressBar;
    private SignUpContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_screen);
        Button txtLogin = findViewById(R.id.txtLogin);
        Button btnSignup = findViewById(R.id.btnSignup);
        editFullName = findViewById(R.id.editFullName);
        editSignupEmail = findViewById(R.id.editSignupEmail);
        editSignupPassword = findViewById(R.id.editSignupPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        progressBar = findViewById(R.id.progressBar);
        presenter = new SignUpPresenter(this);

        btnSignup.setOnClickListener(v -> {
            String fullName = editFullName.getText().toString().trim();
            String email = editSignupEmail.getText().toString().trim();
            String password = editSignupPassword.getText().toString().trim();
            String confirmPassword = editConfirmPassword.getText().toString().trim();
            presenter.doSignUp(email, password, confirmPassword, fullName);
        });

        txtLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        findViewById(R.id.btnSignup).setEnabled(false);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        findViewById(R.id.btnSignup).setEnabled(true);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignUpSuccess(FirebaseUser user) {
        Intent intent = new Intent(SignupActivity.this, AppActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSignUpFailure(String errorMessage) {
        presenter.showError(errorMessage);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.cancelOperations();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}