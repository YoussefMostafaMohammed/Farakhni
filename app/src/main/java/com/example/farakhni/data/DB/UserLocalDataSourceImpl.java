package com.example.farakhni.data.DB;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.farakhni.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserLocalDataSourceImpl implements UserLocalDataSource {
    private final UserDAO dao;
    private static UserLocalDataSourceImpl instance;
    private final FirebaseFirestore firestore;
    private final FirebaseAuth auth;

    private UserLocalDataSourceImpl(Context context) {
        AppDataBase db = AppDataBase.getInstance(context.getApplicationContext());
        dao = db.getUserDAO();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public static synchronized UserLocalDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new UserLocalDataSourceImpl(context);
        }
        return instance;
    }

    @Override
    public void insertUser(User user) {
        new Thread(() -> {
            dao.insertUser(user);
            String userId = getCurrentUserId();
            if (userId == null) {
                Log.e("UserDataSource", "User not logged in, cannot sync to Firestore");
                return;
            }

            firestore.collection("users").document(userId)
                    .collection("user_data").document("profile")
                    .set(user)
                    .addOnSuccessListener(aVoid -> Log.d("UserDataSource", "User synced to Firestore: " + userId))
                    .addOnFailureListener(e -> Log.e("UserDataSource", "Error syncing user to Firestore: " + e.getMessage()));
        }).start();
    }

    @Override
    public LiveData<User> getUserById(String userId) {
        return new LiveData<User>() {
            @Override
            protected void onActive() {
                super.onActive();
                new Thread(() -> {
                    User user = dao.getUserById(userId);
                    postValue(user);
                }).start();
            }
        };
    }

    @Override
    public void syncUserWithFirestore(OnSyncCompleteListener listener) {
        String userId = getCurrentUserId();
        if (userId == null) {
            listener.onSyncFailure(new IllegalStateException("User not logged in"));
            return;
        }

        firestore.collection("users").document(userId).collection("user_data").document("profile")
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        User user = document.toObject(User.class);
                        new Thread(() -> {
                            dao.insertUser(user);
                            listener.onSyncSuccess();
                        }).start();
                    } else {
                        listener.onSyncSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("UserDataSource", "Error syncing from Firestore: " + e.getMessage());
                    listener.onSyncFailure(e);
                });
    }

    public void deleteUserById(String userId) {
        new Thread(() -> dao.deleteUserById(userId)).start();
    }

    private String getCurrentUserId() {
        return auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
    }
}