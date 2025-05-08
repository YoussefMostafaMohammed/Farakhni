package com.example.farakhni.freatures.mainapp.model;

import android.content.Context;

import androidx.room.Room;

import com.example.farakhni.data.DB.AppDataBase;
import com.example.farakhni.freatures.mainapp.AppContract;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppModel implements AppContract.Model {
    private final AppDataBase database;
    private final ExecutorService executorService;

    public AppModel(Context context) {
        this.database = AppDataBase.getInstance(context);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void syncData(OnSyncCompleteListener listener) {
        try {
            listener.onSyncSuccess();
        } catch (Exception e) {
            listener.onSyncFailure(e);
        }
    }

    @Override
    public void clearDatabase(OnClearDatabaseListener listener) {
        executorService.execute(() -> {
            try {
                // Clear all tables in the Room database
                database.clearAllTables();
                listener.onClearSuccess();
            } catch (Exception e) {
                listener.onClearFailure(e);
            }
        });
    }
}