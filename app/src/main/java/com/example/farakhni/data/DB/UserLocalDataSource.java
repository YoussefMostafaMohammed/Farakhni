package com.example.farakhni.data.DB;

import androidx.lifecycle.LiveData;
import com.example.farakhni.model.User;

public interface UserLocalDataSource {
    void insertUser(User user);
    LiveData<User> getUserById(String userId);
}
