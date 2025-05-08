package com.example.farakhni.freatures.auth.signup;

import com.google.firebase.auth.FirebaseUser;

public interface SignUpContract {
    interface View {
        void showProgress();
        void hideProgress();
        void showError(String errorMessage);
        void showMessage(String message);
        void onSignUpSuccess(FirebaseUser user);
        void onSignUpFailure(String errorMessage);
    }

    interface Presenter {
        void doSignUp(String email, String password, String confirmPassword, String fullName);
        void showError(String errorMessage);
        void showMessage(String message);
        void cancelOperations();
        void onDestroy();
    }

    interface Model {
        void signUp(String email, String password, OnSignUpListener listener);

        interface OnSignUpListener {
            void onSuccess(FirebaseUser user);
            void onFailure(Exception e);
        }
    }
}