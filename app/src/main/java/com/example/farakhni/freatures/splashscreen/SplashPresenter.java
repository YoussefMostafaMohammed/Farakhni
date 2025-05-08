package com.example.farakhni.freatures.splashscreen;

import com.example.farakhni.data.DB.FavoriteMealsLocalDataSource;
import com.example.farakhni.data.DB.PlannedMealsLocalDataSource;
import com.example.farakhni.data.DB.UserLocalDataSource;

import java.lang.ref.WeakReference;

public class SplashPresenter implements SplashContract.Presenter {
    private WeakReference<SplashContract.View> viewRef;
    private final SplashContract.Model model;
    private static final long SPLASH_DURATION_MS = 4075;

    public SplashPresenter(SplashContract.Model model) {
        this.model = model;
        this.viewRef = new WeakReference<>(null);
    }

    @Override
    public void attachView(SplashContract.View view) {
        this.viewRef = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        viewRef.clear();
    }

    @Override
    public void initialize() {
        SplashContract.View view = viewRef.get();
        if (view == null) return;

        String userId = model.getCurrentUserId();
        if (userId != null) {
            syncData(userId);
        } else {
            view.showSplashGif();
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                if (viewRef.get() != null) {
                    viewRef.get().navigateToWelcomeScreen();
                }
            }, SPLASH_DURATION_MS);
        }
    }

    private void syncData(String userId) {
        model.syncUserData(userId, new UserLocalDataSource.OnSyncCompleteListener() {
            @Override
            public void onSyncSuccess() {
                model.syncFavoriteMeals(new FavoriteMealsLocalDataSource.OnSyncCompleteListener() {
                    @Override
                    public void onSyncSuccess() {
                        model.syncPlannedMeals(new PlannedMealsLocalDataSource.OnSyncCompleteListener() {
                            @Override
                            public void onSyncSuccess() {
                                proceedToApp();
                            }

                            @Override
                            public void onSyncFailure(Exception e) {
                                proceedToApp();
                            }
                        });
                    }

                    @Override
                    public void onSyncFailure(Exception e) {
                        proceedToApp();
                    }
                });
            }

            @Override
            public void onSyncFailure(Exception e) {
                proceedToApp();
            }
        });
    }

    private void proceedToApp() {
        SplashContract.View view = viewRef.get();
        if (view != null) {
            view.navigateToAppActivity();
        }
    }
}