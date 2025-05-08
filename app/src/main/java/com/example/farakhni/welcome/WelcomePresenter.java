package com.example.farakhni.welcome;


import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.FirebaseUser;

import java.lang.ref.WeakReference;

public class WelcomePresenter implements WelcomeContract.Presenter {
    private WeakReference<WelcomeContract.View> viewRef;
    private final WelcomeModel model;

    public WelcomePresenter(WelcomeModel model) {
        this.model = model;
        this.viewRef = new WeakReference<>(null);
    }

    @Override
    public void attachView(WelcomeContract.View view) {
        this.viewRef = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        viewRef.clear();
    }

    @Override
    public void onLoginClicked() {
        WelcomeContract.View view = viewRef.get();
        if (view != null) {
            view.navigateToLogin();
        }
    }

    @Override
    public void onSignupClicked() {
        WelcomeContract.View view = viewRef.get();
        if (view != null) {
            view.navigateToSignup();
        }
    }

    @Override
    public void onGoogleSignInClicked() {
        WelcomeContract.View view = viewRef.get();
        if (view != null) {
            view.showProgress();
            view.launchGoogleSignInIntent();
        }
    }

    @Override
    public void onGuestClicked() {
        WelcomeContract.View view = viewRef.get();
        if (view != null) {
            view.navigateToApp();
        }
    }

    @Override
    public void handleGoogleSignInResult(Intent data) {
        WelcomeContract.View view = viewRef.get();
        if (view == null) return;

        try {
            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data)
                    .getResult(ApiException.class);
            String idToken = account.getIdToken();
            model.signInWithGoogle(idToken, new WelcomeModel.OnGoogleSignInListener() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    if (viewRef.get() != null) {
                        viewRef.get().hideProgress();
                        syncData(user.getUid());
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    if (viewRef.get() != null) {
                        viewRef.get().hideProgress();
                        showError("Google sign-in failed: " + mapErrorMessage(e));
                    }
                }
            });
        } catch (ApiException e) {
            view.hideProgress();
            showError("Google sign-in failed: " + mapErrorMessage(e));
        }
    }

    @Override
    public void syncData(String userId) {
        WelcomeContract.View view = viewRef.get();
        if (view == null) return;

        view.showProgress();
        model.syncData(userId, new WelcomeModel.OnSyncCompleteListener() {
            @Override
            public void onSyncSuccess() {
                if (viewRef.get() != null) {
                    viewRef.get().hideProgress();
                    viewRef.get().navigateToApp();
                }
            }

            @Override
            public void onSyncFailure(String errorMessage) {
                if (viewRef.get() != null) {
                    viewRef.get().hideProgress();
                    showError(errorMessage);
                    viewRef.get().navigateToApp();
                }
            }
        });
    }

    @Override
    public void cancelOperations() {
        model.cancelOperations();
    }

    private void showError(String message) {
        WelcomeContract.View view = viewRef.get();
        if (view != null) {
            view.showError(message);
        }
    }

    private void showMessage(String message) {
        WelcomeContract.View view = viewRef.get();
        if (view != null) {
            view.showMessage(message);
        }
    }

    private String mapErrorMessage(Exception e) {
        if (e.getMessage() == null) return "An error occurred";
        String msg = e.getMessage().toLowerCase();
        if (msg.contains("network")) {
            return "Network error, please check your connection";
        } else {
            return "Authentication failed";
        }
    }
}