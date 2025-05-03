package com.example.farakhni.firebase.signup;

import com.google.firebase.auth.FirebaseUser;

public interface SignUpContract {
    interface View {
        void showProgress();
        void hideProgress();
        void onSignUpSuccess(FirebaseUser user);
        void onSignUpFailure(String errorMessage);
    }
    interface Presenter {
        void doSignUp(String email, String password,String fullName);
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
