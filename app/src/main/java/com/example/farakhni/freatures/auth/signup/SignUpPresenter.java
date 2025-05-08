package com.example.farakhni.freatures.auth.signup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.farakhni.data.DB.UserLocalDataSourceImpl;
import com.example.farakhni.data.repositories.UserRepositoryImpl;
import com.example.farakhni.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class SignUpPresenter implements SignUpContract.Presenter {
    private final WeakReference<SignUpContract.View> viewRef;
    private final SignUpContract.Model model;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final UserRepositoryImpl userRepository;

    public SignUpPresenter(SignUpContract.View view) {
        this.viewRef = new WeakReference<>(view);
        this.model = new SignUpModel();
        this.userRepository = UserRepositoryImpl.getInstance(
                UserLocalDataSourceImpl.getInstance(((AppCompatActivity)view).getApplicationContext()));
    }

    @Override
    public void doSignUp(String email, String password, String confirmPassword, String fullName) {
        SignUpContract.View view = viewRef.get();
        if (view == null) return;

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty()) {
            showError("All fields are required");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Confirm password does not match password");
            return;
        }

        view.showProgress();
        model.signUp(email, password, new SignUpContract.Model.OnSignUpListener() {
            @Override
            public void onSuccess(FirebaseUser user) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(profileTask -> {
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("userName", fullName);
                            userData.put("email", email);

                            db.collection("users")
                                    .document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        User localUser = new User();
                                        localUser.setUserId(user.getUid());
                                        localUser.setUsername(fullName);
                                        userRepository.insertUser(localUser);

                                        if (viewRef.get() != null) {
                                            viewRef.get().hideProgress();
                                            viewRef.get().onSignUpSuccess(user);
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        if (viewRef.get() != null) {
                                            viewRef.get().hideProgress();
                                            showError(mapErrorMessage(e));
                                        }
                                    });
                        });
            }

            @Override
            public void onFailure(Exception e) {
                if (viewRef.get() != null) {
                    viewRef.get().hideProgress();
                    showError(mapErrorMessage(e));
                }
            }
        });
    }

    @Override
    public void showError(String errorMessage) {
        SignUpContract.View view = viewRef.get();
        if (view != null) {
            view.showError(errorMessage);
        }
    }

    @Override
    public void showMessage(String message) {
        SignUpContract.View view = viewRef.get();
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
        if (msg.contains("email already in use")) {
            return "This email is already registered";
        } else if (msg.contains("weak password")) {
            return "Password is too weak, use at least 6 characters";
        } else if (msg.contains("network")) {
            return "Network error, please check your connection";
        } else {
            return "Sign-up failed";
        }
    }
}