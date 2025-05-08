package com.example.farakhni.data.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.farakhni.model.User;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("SELECT * FROM user_table WHERE userId = :userId")
    User getUserById(String userId);

    @Query("DELETE FROM user_table WHERE userId = :userId")
    void deleteUserById(String userId);
}