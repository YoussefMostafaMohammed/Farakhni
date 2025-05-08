package com.example.farakhni.freatures.auth.login;

import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.lang.ref.WeakReference;

public class LoginPresenter implements LoginContract.Presenter {
    private final WeakReference<LoginContract.View> viewRef;
    private final LoginContract.Model model;

    public LoginPresenter(LoginContract.View view) {
        this.viewRef = new WeakReference<>(view);
        this.model = new LoginModel();
    }

    @Override
    public void doLogin(String email, String password) {
        LoginContract.View view = viewRef.get();
        if (view == null) return;

        if (email.isEmpty() || password.isEmpty()) {
            showError("All fields are required");
            return;
        }

        view.showProgress();
        model.login(email, password, new LoginContract.Model.OnLoginListener() {
            @Override
            public void onLoginSuccess(FirebaseUser user) {
                if (viewRef.get() != null) {
                    viewRef.get().hideProgress();
                    viewRef.get().onLoginSuccess(user);
                }
            }

            @Override
            public void onLoginFailure(Exception e) {
                if (viewRef.get() != null) {
                    viewRef.get().hideProgress();
                    showError(mapErrorMessage(e));
                }
            }
        });
    }

    @Override
    public void startGoogleSignIn() {
        LoginContract.View view = viewRef.get();
        if (view != null) view.launchGoogleSignInIntent();
    }

    @Override
    public void handleGoogleSignInResult(Intent data) {
        LoginContract.View view = viewRef.get();
        if (view == null) return;

        view.showProgress();
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            String idToken = task.getResult(ApiException.class).getIdToken();
            model.signInWithGoogle(idToken, new LoginContract.Model.OnLoginListener() {
                @Override
                public void onLoginSuccess(FirebaseUser user) {
                    if (viewRef.get() != null) {
                        viewRef.get().hideProgress();
                        viewRef.get().onLoginSuccess(user);
                    }
                }

                @Override
                public void onLoginFailure(Exception e) {
                    if (viewRef.get() != null) {
                        viewRef.get().hideProgress();
                        showError("Google sign-in failed: " + mapErrorMessage(e));
                    }
                }
            });
        } catch (ApiException e) {
            if (viewRef.get() != null) {
                viewRef.get().hideProgress();
                showError("Google sign-in failed: " + mapErrorMessage(e));
            }
        }
    }

    @Override
    public void verifyEmail(String email) {
        LoginContract.View view = viewRef.get();
        if (view == null) return;

        if (email.isEmpty()) {
            showError("Email is required");
            return;
        }

        view.showProgress();
        model.verifyEmail(email, new LoginContract.Model.OnEmailVerificationListener() {
            @Override
            public void onEmailVerified(boolean exists) {
                if (viewRef.get() != null) {
                    viewRef.get().hideProgress();
                    viewRef.get().onEmailVerified(exists);
                }
            }

            @Override
            public void onEmailFailure(Exception e) {
                if (viewRef.get() != null) {
                    viewRef.get().hideProgress();
                    showError("Invalid email address");
                }
            }
        });
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        LoginContract.View view = viewRef.get();
        if (view == null) return;

        view.showProgress();
        model.sendPasswordResetEmail(email, new LoginContract.Model.OnResetPasswordListener() {
            @Override
            public void onPasswordResetSuccess() {
                if (viewRef.get() != null) {
                    viewRef.get().hideProgress();
                    viewRef.get().onResetEmailSent();
                }
            }

            @Override
            public void onPasswordResetFailure(Exception e) {
                if (viewRef.get() != null) {
                    viewRef.get().hideProgress();
                    showError(mapErrorMessage(e));
                }
            }
        });
    }

    @Override
    public void updatePassword(String currentPassword, String newPassword) {
        LoginContract.View view = viewRef.get();
        if (view == null) return;

        view.showProgress();
        model.updatePassword(currentPassword, newPassword, new LoginContract.Model.OnUpdatePasswordListener() {
            @Override
            public void onPasswordChangeSuccess() {
                if (viewRef.get() != null) {
                    viewRef.get().hideProgress();
                    viewRef.get().onPasswordChangeSuccess();
                }
            }

            @Override
            public void onPasswordChangeFailure(Exception e) {
                if (viewRef.get() != null) {
                    viewRef.get().hideProgress();
                    showError(mapErrorMessage(e));
                }
            }
        });
    }

    @Override
    public void showError(String errorMessage) {
        LoginContract.View view = viewRef.get();
        if (view != null) {
            view.showError(errorMessage);
        }
    }

    @Override
    public void showMessage(String message) {
        LoginContract.View view = viewRef.get();
        if (view != null) {
            view.showMessage(message);
        }
    }

    @Override
    public void cancelOperations() {
        // Cancel any pending Firebase tasks if necessary
    }

    @Override
    public void onDestroy() {
        // No need to set view to null with WeakReference
    }

    private String mapErrorMessage(Exception e) {
        if (e.getMessage() == null) return "An error occurred";
        String msg = e.getMessage().toLowerCase();
        if (msg.contains("invalid credential") || msg.contains("password is invalid")) {
            return "Invalid email or password";
        } else if (msg.contains("no user")) {
            return "No account found with this email";
        } else if (msg.contains("network")) {
            return "Network error, please check your connection";
        } else {
            return "Authentication failed";
        }
    }
}