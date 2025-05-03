package com.example.farakhni.freatures.auth.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.farakhni.data.DB.AppDataBase;
import com.example.farakhni.R;
import com.example.farakhni.freatures.mainapp.view.AppActivity;
import com.example.farakhni.model.User;
import com.example.farakhni.freatures.auth.signup.view.SignupScreen;
import com.example.farakhni.freatures.auth.login.LoginContract;
import com.example.farakhni.freatures.auth.login.LoginPresenter;
import com.google.firebase.auth.FirebaseUser;

public class LoginScreen extends AppCompatActivity  implements LoginContract.View{
    private Button txtSignUp;
    private Button btnLogin;
    private TextView editEmail;
    private TextView editPassword;
    private Button txtForgotPassword;
    private LoginContract.Presenter presenter;
    String email;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);
        txtSignUp = findViewById(R.id.txtSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        editEmail=findViewById(R.id.editEmail);
        editPassword=findViewById(R.id.editPassword);
        txtForgotPassword=findViewById(R.id.txtForgotPassword);
        presenter = new LoginPresenter(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 email = editEmail.getText().toString().trim();
                 password   = editPassword.getText().toString().trim();
                 if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginScreen.this, "Error: All fields are required.", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    presenter.doLogin(email, password);
                }
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, SignupScreen.class);
                startActivity(intent);
                finish();
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editEmail.getText().toString().trim();
                presenter.verifyEmail(email);
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
        AppDataBase db = AppDataBase.getInstance(this);
        User localUser = new User();
        localUser.setUserId(user.getUid());
        localUser.setUsername( user.getDisplayName());
        new Thread(() -> db.getUserDAO().insertUser(localUser)).start();
        Intent intent = new Intent(LoginScreen.this, AppActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(String errorMessage) {
        Toast.makeText(LoginScreen.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmailVerified(boolean exists) {
        email = editEmail.getText().toString().trim();
        presenter.sendPasswordResetEmail(email);
        Toast.makeText(LoginScreen.this, "Check Email To Update your Password", Toast.LENGTH_SHORT).show();
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
        //do nothing
    }

    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}