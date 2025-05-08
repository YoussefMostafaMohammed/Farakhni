package com.example.farakhni.welcome;

public interface WelcomeContract {
    interface View {
        void showProgress();
        void hideProgress();
        void showError(String message);
        void showMessage(String message);
        void navigateToLogin();
        void navigateToSignup();
        void navigateToApp();
        void launchGoogleSignInIntent();
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void onLoginClicked();
        void onSignupClicked();
        void onGoogleSignInClicked();
        void onGuestClicked();
        void handleGoogleSignInResult(android.content.Intent data);
        void syncData(String userId);
        void cancelOperations();
    }
}