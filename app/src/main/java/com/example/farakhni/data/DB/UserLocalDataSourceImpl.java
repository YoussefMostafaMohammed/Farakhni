package com.example.farakhni.data.DB;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.farakhni.data.DB.AppDataBase;
import com.example.farakhni.data.DB.UserDAO;
import com.example.farakhni.model.User;

public class UserLocalDataSourceImpl implements UserLocalDataSource {
    private UserDAO dao;
    private static UserLocalDataSourceImpl instance;

    private UserLocalDataSourceImpl(Context context) {
        AppDataBase db = AppDataBase.getInstance(context.getApplicationContext());
        dao = db.getUserDAO();
    }

    public static synchronized UserLocalDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new UserLocalDataSourceImpl(context);
        }
        return instance;
    }

    @Override
    public void insertUser(User user) {
        new Thread(() -> dao.insertUser(user)).start();
    }

    @Override
    public LiveData<User> getUserById(String userId) {
        MutableLiveData<User> liveData = new MutableLiveData<>();
        new Thread(() -> {
            User user = dao.getUserById(userId);
            liveData.postValue(user);
        }).start();
        return liveData;
    }
}