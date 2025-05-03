package com.example.farakhni.data.network;


import androidx.lifecycle.LiveData;
import com.example.farakhni.data.DB.UserLocalDataSource;
import com.example.farakhni.model.User;

public class UserRepositoryImpl implements UserRepository {
    private final UserLocalDataSource localDataSource;
    private static UserRepositoryImpl instance;

    private UserRepositoryImpl(UserLocalDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static synchronized UserRepositoryImpl getInstance(
            UserLocalDataSource localDataSource) {
        if (instance == null) {
            instance = new UserRepositoryImpl(localDataSource);
        }
        return instance;
    }

    @Override
    public void insertUser(User user) {
        localDataSource.insertUser(user);
    }

    @Override
    public LiveData<User> getUserById(String userId) {
        return localDataSource.getUserById(userId);
    }
}
