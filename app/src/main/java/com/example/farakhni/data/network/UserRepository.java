package com.example.farakhni.data.network;

import androidx.lifecycle.LiveData;
import com.example.farakhni.model.User;

public interface UserRepository {
    void insertUser(User user);
    LiveData<User> getUserById(String userId);
}
