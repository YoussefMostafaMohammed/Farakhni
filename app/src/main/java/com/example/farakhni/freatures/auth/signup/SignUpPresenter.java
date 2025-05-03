package com.example.farakhni.freatures.auth.signup;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.View view;
    private final SignUpContract.Model model;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SignUpPresenter(SignUpContract.View view) {
        this.view = view;
        this.model = new SignUpModel();
    }
    @Override
    public void doSignUp(String email, String password, String fullName) {
        view.showProgress();
        model.signUp(email, password, new SignUpContract.Model.OnSignUpListener() {
            @Override
            public void onSuccess(FirebaseUser user) {
                // 1) Push displayName into the FirebaseUser profile:
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(profileTask -> {
                            if (!profileTask.isSuccessful()) {
                                // optionally log or toast failure, but you can still proceed
                            }

                            // 2) Now store in Firestore (or wherever):
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("userName", fullName);
                            userData.put("email", email);

                            db.collection("users")
                                    .document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        view.hideProgress();
                                        view.onSignUpSuccess(user);
                                    })
                                    .addOnFailureListener(e -> {
                                        view.hideProgress();
                                        view.onSignUpFailure(e.getMessage());
                                    });
                        });
            }

            @Override
            public void onFailure(Exception e) {
                view.hideProgress();
                view.onSignUpFailure(e.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        view = null;  // Avoid memory leaks
    }


}