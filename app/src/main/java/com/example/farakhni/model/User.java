package com.example.farakhni.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey
    @NonNull
    @SerializedName("userId")
    private String userId;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    @SerializedName("userName")
    private String username;

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }
}