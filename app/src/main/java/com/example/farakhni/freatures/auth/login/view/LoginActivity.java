package com.example.farakhni.freatures.auth.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.farakhni.R;
import com.example.farakhni.data.DB.FavoriteMealsLocalDataSource;
import com.example.farakhni.data.DB.PlannedMealsLocalDataSource;
import com.example.farakhni.data.DB.UserLocalDataSource;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.data.repositories.UserRepositoryImpl;
import com.example.farakhni.freatures.auth.login.LoginContract;
import com.example.farakhni.freatures.auth.login.LoginPresenter;
import com.example.farakhni.freatures.auth.signup.view.SignupActivity;
import com.example.farakhni.freatures.mainapp.view.AppActivity;
import com.example.farakhni.model.User;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private Button txtSignUp;
    private Button btnLogin;
    private TextView editEmail;
    private TextView editPassword;
    private Button txtForgotPassword;
    private LoginContract.Presenter presenter;
    private UserRepositoryImpl userRepository;
    private MealRepositoryImpl mealRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);
        txtSignUp = findViewById(R.id.txtSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        presenter = new LoginPresenter(this);
        userRepository = UserRepositoryImpl.getInstance(
                com.example.farakhni.data.DB.UserLocalDataSourceImpl.getInstance(this));
        mealRepository = MealRepositoryImpl.getInstance(this);

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            presenter.doLogin(email, password);
        });

        txtSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();
        });

        txtForgotPassword.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            presenter.verifyEmail(email);
        });
    }

    @Override
    public void showProgress() {
        btnLogin.setEnabled(false);
    }

    @Override
    public void hideProgress() {
        btnLogin.setEnabled(true);
    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onLoginSuccess(FirebaseUser user) {
        User localUser = new User();
        localUser.setUserId(user.getUid());
        localUser.setUsername(user.getDisplayName());
        userRepository.insertUser(localUser);
        syncData();
    }

    private void syncData() {
        userRepository.syncUserWithFirestore(new UserLocalDataSource.OnSyncCompleteListener() {
            @Override
            public void onSyncSuccess() {
                mealRepository.syncFavoriteMealsWithFirestore(new FavoriteMealsLocalDataSource.OnSyncCompleteListener() {
                    @Override
                    public void onSyncSuccess() {
                        mealRepository.syncPlannedMealsWithFirestore(new PlannedMealsLocalDataSource.OnSyncCompleteListener() {
                            @Override
                            public void onSyncSuccess() {
                                proceedToApp();
                            }

                            @Override
                            public void onSyncFailure(Exception e) {
                                presenter.showError("Failed to sync planned meals");
                                proceedToApp();
                            }
                        });
                    }

                    @Override
                    public void onSyncFailure(Exception e) {
                        presenter.showError("Failed to sync favorite meals");
                        proceedToApp();
                    }
                });
            }

            @Override
            public void onSyncFailure(Exception e) {
                presenter.showError("Failed to sync user data");
                proceedToApp();
            }
        });
    }

    private void proceedToApp() {
        Intent intent = new Intent(LoginActivity.this, AppActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailure(String errorMessage) {
        presenter.showError(errorMessage);
    }

    @Override
    public void onEmailVerified(boolean exists) {
        String email = editEmail.getText().toString().trim();
        presenter.sendPasswordResetEmail(email);
    }

    @Override
    public void onEmailVerificationFailed(String errorMessage) {
        presenter.showError(errorMessage);
    }

    @Override
    public void onResetEmailSent() {
        presenter.showMessage("Check Email To Update your Password");
    }

    @Override
    public void onResetEmailFailed(String errorMessage) {
        presenter.showError(errorMessage);
    }

    @Override
    public void onPasswordChangeSuccess() {
        presenter.showMessage("Password updated successfully");
    }

    @Override
    public void onPasswordChangeFailure(String errorMessage) {
        presenter.showError(errorMessage);
    }

    @Override
    public void launchGoogleSignInIntent() {
        // Implement Google Sign-In if needed
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