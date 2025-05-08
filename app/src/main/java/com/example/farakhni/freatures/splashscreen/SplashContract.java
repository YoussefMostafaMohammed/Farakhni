package com.example.farakhni.freatures.splashscreen;

import com.example.farakhni.data.DB.FavoriteMealsLocalDataSource;
import com.example.farakhni.data.DB.PlannedMealsLocalDataSource;
import com.example.farakhni.data.DB.UserLocalDataSource;

public interface SplashContract {
    interface View {
        void showSplashGif();
        void navigateToWelcomeScreen();
        void navigateToAppActivity();
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void initialize();
    }

    interface Model {
        String getCurrentUserId();
        void syncUserData(String userId, UserLocalDataSource.OnSyncCompleteListener listener);
        void syncFavoriteMeals(FavoriteMealsLocalDataSource.OnSyncCompleteListener listener);
        void syncPlannedMeals(PlannedMealsLocalDataSource.OnSyncCompleteListener listener);
    }
}