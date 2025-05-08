package com.example.farakhni.freatures.splashscreen;

import com.example.farakhni.data.DB.FavoriteMealsLocalDataSource;
import com.example.farakhni.data.DB.PlannedMealsLocalDataSource;
import com.example.farakhni.data.DB.UserLocalDataSource;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.data.repositories.UserRepositoryImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashModel implements SplashContract.Model {
    private final UserRepositoryImpl userRepository;
    private final MealRepositoryImpl mealRepository;


    public SplashModel(UserRepositoryImpl userRepository, MealRepositoryImpl mealRepository) {
        this.userRepository = userRepository;
        this.mealRepository = mealRepository;
    }

    @Override
    public String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getUid() : null;
    }

    @Override
    public void syncUserData(String userId, UserLocalDataSource.OnSyncCompleteListener listener) {
        userRepository.syncUserWithFirestore(listener);
    }

    @Override
    public void syncFavoriteMeals(FavoriteMealsLocalDataSource.OnSyncCompleteListener listener) {
        mealRepository.syncFavoriteMealsWithFirestore(listener);
    }

    @Override
    public void syncPlannedMeals(PlannedMealsLocalDataSource.OnSyncCompleteListener listener) {
        mealRepository.syncPlannedMealsWithFirestore(listener);
    }
}