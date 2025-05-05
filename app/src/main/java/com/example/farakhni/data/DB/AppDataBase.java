    package com.example.farakhni.data.DB;

    import android.content.Context;
    import android.database.Cursor;
    import android.util.Log;
    import androidx.annotation.NonNull;
    import androidx.room.Database;
    import androidx.room.Room;
    import androidx.room.RoomDatabase;
    import androidx.sqlite.db.SupportSQLiteDatabase;

    import com.example.farakhni.model.FavoriteMeal;
    import com.example.farakhni.model.PlannedMeal;
    import com.example.farakhni.model.User;
    @Database(
            entities = {User.class, FavoriteMeal.class, PlannedMeal.class},
            version = 1,
            exportSchema = false
    )
    public abstract class AppDataBase extends RoomDatabase {
        private static volatile AppDataBase instance;
        public abstract FavoriteMealDAO getFavoriteMealDAO();
        public abstract UserDAO getUserDAO();
        public abstract PlannedMealDao getPlannedMealDAO();

        public static AppDataBase getInstance(Context context) {
            if (instance == null) {
                synchronized (AppDataBase.class) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                                        context.getApplicationContext(),
                                        AppDataBase.class,
                                        "AppDB"
                                )
                                .addCallback(new RoomDatabase.Callback() {
                                    @Override
                                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                        super.onCreate(db);
                                        Log.d("Database", "Database created");
                                    }
                                    @Override
                                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                        super.onOpen(db);
                                        Log.d("Database", "Database Opened");
                                    }
                                })
                                .allowMainThreadQueries()
                                .build();
                    }
                }
            }

            return instance;
        }

        public void forceCheckpoint() {
            try {
                SupportSQLiteDatabase db = getOpenHelper().getWritableDatabase();
                String query = "PRAGMA wal_checkpoint(full)";
                Cursor cursor = db.query(query, new String[0]); // Pass an empty array
                if (cursor != null) {
                    cursor.moveToFirst();
                    cursor.close();
                }
            } catch (Exception e) {
                Log.e("Database", "Error during checkpoint: " + e.getMessage());
            }
        }
    }