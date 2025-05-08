package com.example.farakhni.freatures.auth.login;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;

public class LoginModel implements LoginContract.Model {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public void login(String email, String password, OnLoginListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        listener.onLoginSuccess(mAuth.getCurrentUser());
                    } else {
                        listener.onLoginFailure(task.getException() != null ? task.getException() : new Exception("Login failed"));
                    }
                });
    }

    @Override
    public void signInWithGoogle(String idToken, OnLoginListener listener) {
        AuthCredential cred = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(cred)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        listener.onLoginSuccess(mAuth.getCurrentUser());
                    } else {
                        listener.onLoginFailure(task.getException() != null ? task.getException() : new Exception("Google sign-in failed"));
                    }
                });
    }

    @Override
    public void verifyEmail(String email, OnEmailVerificationListener listener) {
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            boolean exists = task.getResult().getSignInMethods() != null && !task.getResult().getSignInMethods().isEmpty();
                            listener.onEmailVerified(exists);
                        } else {
                            listener.onEmailFailure(task.getException() != null ? task.getException() : new Exception("Email verification failed"));
                        }
                    }
                });
    }

    @Override
    public void sendPasswordResetEmail(String email, OnResetPasswordListener listener) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onPasswordResetSuccess();
                    } else {
                        listener.onPasswordResetFailure(task.getException() != null ? task.getException() : new Exception("Password reset failed"));
                    }
                });
    }

    @Override
    public void updatePassword(String currentPassword, String newPassword, OnUpdatePasswordListener listener) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            listener.onPasswordChangeFailure(new IllegalStateException("No user is currently signed in"));
            return;
        }
        AuthCredential creds = com.google.firebase.auth.EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
        user.reauthenticate(creds)
                .addOnCompleteListener(reauthTask -> {
                    if (reauthTask.isSuccessful()) {
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        listener.onPasswordChangeSuccess();
                                    } else {
                                        listener.onPasswordChangeFailure(updateTask.getException() != null ? updateTask.getException() : new Exception("Password update failed"));
                                    }
                                });
                    } else {
                        listener.onPasswordChangeFailure(reauthTask.getException() != null ? reauthTask.getException() : new Exception("Reauthentication failed"));
                    }
                });
    }
}