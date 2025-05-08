package com.example.farakhni.welcome;

import android.content.Context;

import com.example.farakhni.data.DB.FavoriteMealsLocalDataSource;
import com.example.farakhni.data.DB.PlannedMealsLocalDataSource;
import com.example.farakhni.data.DB.UserLocalDataSource;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.data.repositories.UserRepositoryImpl;
import com.example.farakhni.freatures.auth.login.LoginModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WelcomeModel {
    private final LoginModel loginModel;
    private final UserRepositoryImpl userRepository;
    private final MealRepositoryImpl mealRepository;
    private final ExecutorService executorService;

    public WelcomeModel(Context context) {
        this.loginModel = new LoginModel();
        this.userRepository = UserRepositoryImpl.getInstance(
                com.example.farakhni.data.DB.UserLocalDataSourceImpl.getInstance(context));
        this.mealRepository = MealRepositoryImpl.getInstance(context);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void signInWithGoogle(String idToken, OnGoogleSignInListener listener) {
        loginModel.signInWithGoogle(idToken, new LoginModel.OnLoginListener() {
            @Override
            public void onLoginSuccess(FirebaseUser user) {
                listener.onSuccess(user);
            }

            @Override
            public void onLoginFailure(Exception e) {
                listener.onFailure(e);
            }
        });
    }

    public void syncData(String userId, OnSyncCompleteListener listener) {
        executorService.execute(() -> {
            userRepository.syncUserWithFirestore(new UserLocalDataSource.OnSyncCompleteListener() {
                @Override
                public void onSyncSuccess() {
                    mealRepository.syncFavoriteMealsWithFirestore(new FavoriteMealsLocalDataSource.OnSyncCompleteListener() {
                        @Override
                        public void onSyncSuccess() {
                            mealRepository.syncPlannedMealsWithFirestore(new PlannedMealsLocalDataSource.OnSyncCompleteListener() {
                                @Override
                                public void onSyncSuccess() {
                                    listener.onSyncSuccess();
                                }

                                @Override
                                public void onSyncFailure(Exception e) {
                                    listener.onSyncFailure("Failed to sync planned meals: " + mapErrorMessage(e));
                                }
                            });
                        }

                        @Override
                        public void onSyncFailure(Exception e) {
                            listener.onSyncFailure("Failed to sync favorite meals: " + mapErrorMessage(e));
                        }
                    });
                }

                @Override
                public void onSyncFailure(Exception e) {
                    listener.onSyncFailure("Failed to sync user data: " + mapErrorMessage(e));
                }
            });
        });
    }

    public void cancelOperations() {
        executorService.shutdownNow();
    }

    private String mapErrorMessage(Exception e) {
        if (e.getMessage() == null) return "An error occurred";
        String msg = e.getMessage().toLowerCase();
        if (msg.contains("network")) {
            return "Network error, please check your connection";
        } else {
            return "Operation failed";
        }
    }

    public interface OnGoogleSignInListener {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception e);
    }

    public interface OnSyncCompleteListener {
        void onSyncSuccess();
        void onSyncFailure(String errorMessage);
    }
}