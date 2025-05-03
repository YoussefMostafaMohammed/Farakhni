package com.example.farakhni.firebase.signup;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpModel implements SignUpContract.Model {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public void signUp(String email, String password, OnSignUpListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess(mAuth.getCurrentUser());
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }
}
