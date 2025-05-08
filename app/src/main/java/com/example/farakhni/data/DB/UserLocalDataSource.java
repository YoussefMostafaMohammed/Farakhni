package com.example.farakhni.data.DB;

import androidx.lifecycle.LiveData;
import com.example.farakhni.model.User;

public interface UserLocalDataSource {
    void insertUser(User user);
    LiveData<User> getUserById(String userId);
    void syncUserWithFirestore( OnSyncCompleteListener listener);

    interface OnSyncCompleteListener {
        void onSyncSuccess();
        void onSyncFailure(Exception e);
    }
}