package com.example.farakhni.data.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.farakhni.model.User;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUser(User user);
    @Query("SELECT * FROM user_table WHERE userId = :userId")
    public User getUserById(String userId);
}