package com.example.farakhni.freatures.auth.login;

import android.content.Intent;
import com.google.firebase.auth.FirebaseUser;

public interface LoginContract {

    interface View {
        void showProgress();
        void hideProgress();
        void onLoginSuccess(FirebaseUser user);
        void onLoginFailure(String errorMessage);
        void launchGoogleSignInIntent();
        void onEmailVerified(boolean exists);
        void onEmailVerificationFailed(String errorMessage);
        void onResetEmailSent();
        void onResetEmailFailed(String errorMessage);
        void onPasswordChangeSuccess();
        void onPasswordChangeFailure(String errorMessage);
    }

    interface Presenter {
        void doLogin(String email, String password);
        void startGoogleSignIn();
        void handleGoogleSignInResult(Intent data);
        void verifyEmail(String email);
        void sendPasswordResetEmail(String email);
        void updatePassword(String currentPassword, String newPassword);
        void onDestroy();
    }

    interface Model {
        void login(String email, String pwd, OnLoginListener listener);
        void signInWithGoogle(String idToken, OnLoginListener listener);
        void verifyEmail(String email, OnEmailVerificationListener listener);
        void sendPasswordResetEmail(String email, OnResetPasswordListener listener);
        void updatePassword(String currentPassword,
                            String newPassword,
                            OnUpdatePasswordListener listener);
        interface OnLoginListener {
            void onLoginSuccess(FirebaseUser user);
            void onLoginFailure(Exception e);
        }

        interface OnEmailVerificationListener {
            void onEmailVerified(boolean exists);
            void onEmailFailure(Exception e);
        }

        interface OnResetPasswordListener {
            void onPasswordResetSuccess();
            void onPasswordResetFailure(Exception e);
        }

        interface OnUpdatePasswordListener {
            void onPasswordChangeSuccess();
            void onPasswordChangeFailure(Exception e);
        }
    }
}
