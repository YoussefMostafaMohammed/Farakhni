package com.example.farakhni.changepassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.farakhni.R;
import com.example.farakhni.firebase.login.LoginContract;
import com.example.farakhni.firebase.login.LoginPresenter;
import com.example.farakhni.login.LoginScreen;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordScreen extends AppCompatActivity implements LoginContract.View {
    TextView editCurrentPassword;
    TextView editNewPassword;
    TextView editConfirmPassword;
    View btnUpdatePassword;
    View btnBackToLogin;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password_screen);
        editCurrentPassword=findViewById(R.id.editCurrentPassword);
        editNewPassword=findViewById(R.id.editNewPassword);
        editConfirmPassword=findViewById(R.id.editConfirmPassword);
        btnUpdatePassword=findViewById(R.id.btnUpdatePassword);
        btnBackToLogin=findViewById(R.id.btnBackToLogin);
        String email = getIntent().getStringExtra("email");
        presenter=new LoginPresenter(this);
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordScreen.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendPasswordResetEmail(email);
                Toast.makeText(ChangePasswordScreen.this, "Check Email To Update your Password", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void onLoginFailure(String errorMessage) {

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

    }
}