package com.example.farakhni.firebase.login;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private final LoginContract.Model model;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        this.model = new LoginModel();
    }

    @Override
    public void doLogin(String email, String password) {
        if (view != null) view.showProgress();
        model.login(email, password, new LoginContract.Model.OnLoginListener() {
            @Override
            public void onLoginSuccess(FirebaseUser user) {
                if (view != null) {
                    view.hideProgress();
                    view.onLoginSuccess(user);
                }
            }
            @Override
            public void onLoginFailure(Exception e) {
                if (view != null) {
                    view.hideProgress();
                    view.onLoginFailure(e.getMessage());
                }
            }
        });
    }

    @Override
    public void startGoogleSignIn() {
        if (view != null) view.launchGoogleSignInIntent();
    }

    @Override
    public void handleGoogleSignInResult(Intent data) {
        if (view != null) view.showProgress();
        Task<GoogleSignInAccount> task = GoogleSignIn
                .getSignedInAccountFromIntent(data);
        try {
            String idToken = task.getResult(ApiException.class).getIdToken();
            model.signInWithGoogle(idToken, new LoginContract.Model.OnLoginListener() {
                @Override
                public void onLoginSuccess(FirebaseUser user) {
                    if (view != null) {
                        view.hideProgress();
                        view.onLoginSuccess(user);
                    }
                }
                @Override
                public void onLoginFailure(Exception e) {
                    if (view != null) {
                        view.hideProgress();
                        view.onLoginFailure(e.getMessage());
                    }
                }
            });
        } catch (ApiException e) {
            if (view != null) {
                view.hideProgress();
                view.onLoginFailure("Google sign-in failed: " + e.getMessage());
            }
        }
    }

    @Override
    public void verifyEmail(String email) {
        if (view != null) view.showProgress();
        model.verifyEmail(email, new LoginContract.Model.OnEmailVerificationListener() {
            @Override
            public void onEmailVerified(boolean exists) {
                if (view != null) {
                    view.hideProgress();
                    view.onEmailVerified(exists);
                }
            }
            @Override
            public void onEmailFailure(Exception e) {
                if (view != null) {
                    view.hideProgress();
                    view.onEmailVerificationFailed(
                            "wrong email"
                    );
                }
            }
        });
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        if (view != null) view.showProgress();
        model.sendPasswordResetEmail(email, new LoginContract.Model.OnResetPasswordListener() {
            @Override
            public void onPasswordResetSuccess() {
                if (view != null) {
                    view.hideProgress();
                    view.onResetEmailSent();
                }
            }
            @Override
            public void onPasswordResetFailure(Exception exception) {
                if (view != null) {
                    view.hideProgress();
                    view.onResetEmailFailed(exception.getMessage());
                }
            }
        });
    }

    @Override
    public void updatePassword(String currentPassword, String newPassword) {
        if (view != null) view.showProgress();
        model.updatePassword(currentPassword, newPassword, new LoginContract.Model.OnUpdatePasswordListener() {
            @Override
            public void onPasswordChangeSuccess() {
                if (view != null) {
                    view.hideProgress();
                    view.onPasswordChangeSuccess();
                }
            }
            @Override
            public void onPasswordChangeFailure(Exception e) {
                if (view != null) {
                    view.hideProgress();
                    view.onPasswordChangeFailure(e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        view = null;  // Avoid memory leaks
    }
}
