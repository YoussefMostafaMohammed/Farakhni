package com.example.farakhni.data.DB;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.farakhni.model.FavoriteMeal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FavoriteMealsLocalDataSourceImpl implements FavoriteMealsLocalDataSource {
    private final FavoriteMealDAO dao;
    private static FavoriteMealsLocalDataSourceImpl instance;
    private final LiveData<List<FavoriteMeal>> storedMeals;
    private final FirebaseFirestore firestore;
    private final FirebaseAuth auth;

    private FavoriteMealsLocalDataSourceImpl(Context context) {
        AppDataBase db = AppDataBase.getInstance(context.getApplicationContext());
        dao = db.getFavoriteMealDAO();
        storedMeals = dao.getFavoriteMealsForUser();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public static synchronized FavoriteMealsLocalDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new FavoriteMealsLocalDataSourceImpl(context);
        }
        return instance;
    }

    @Override
    public LiveData<List<FavoriteMeal>> getFavoriteMeals() {
        return storedMeals;
    }

    @Override
    public void insertMeal(FavoriteMeal meal) {
        new Thread(() -> {
            try {
                dao.insertMeal(meal);
                String userId = getCurrentUserId();
                if (userId == null) {
                    Log.e("FavoriteMeals", "Cannot sync to Firestore: User not logged in");
                    return;
                }

                // Ensure valid Firestore path: users/{userId}/favorite_meals/{mealId}
                firestore.collection("users").document(userId)
                        .collection("favorite_meals").document(meal.getId())
                        .set(meal.toMap())
                        .addOnSuccessListener(aVoid -> Log.d("FavoriteMeals", "Meal synced to Firestore: " + meal.getId()))
                        .addOnFailureListener(e -> Log.e("FavoriteMeals", "Error syncing meal to Firestore: " + e.getMessage(), e));
            } catch (Exception e) {
                Log.e("FavoriteMeals", "Error inserting meal locally or syncing to Firestore: " + e.getMessage(), e);
            }
        }).start();
    }

    @Override
    public void deleteMeal(FavoriteMeal meal) {
        new Thread(() -> {
            try {
                dao.deleteMeal(meal);
                String userId = getCurrentUserId();
                if (userId == null) {
                    Log.e("FavoriteMeals", "Cannot delete from Firestore: User not logged in");
                    return;
                }

                firestore.collection("users").document(userId)
                        .collection("favorite_meals").document(meal.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> Log.d("FavoriteMeals", "Meal deleted from Firestore: " + meal.getId()))
                        .addOnFailureListener(e -> Log.e("FavoriteMeals", "Error deleting meal from Firestore: " + e.getMessage(), e));
            } catch (Exception e) {
                Log.e("FavoriteMeals", "Error deleting meal locally or from Firestore: " + e.getMessage(), e);
            }
        }).start();
    }

    @Override
    public boolean isFavorite(String mealId) {
        return dao.isFavorite(mealId);
    }

    @Override
    public void syncFavoriteMealsWithFirestore(OnSyncCompleteListener listener) {
        String userId = getCurrentUserId();
        if (userId == null) {
            Log.e("FavoriteMeals", "Sync failed: User not logged in");
            listener.onSyncFailure(new IllegalStateException("User not logged in"));
            return;
        }

        try {
            firestore.collection("users").document(userId).collection("favorite_meals")
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        new Thread(() -> {
                            try {
                                for (var doc : querySnapshot.getDocuments()) {
                                    FavoriteMeal meal = FavoriteMeal.fromMap(doc.getData());
                                    dao.insertMeal(meal);
                                }
                                listener.onSyncSuccess();
                            } catch (Exception e) {
                                Log.e("FavoriteMeals", "Error processing Firestore sync: " + e.getMessage(), e);
                                listener.onSyncFailure(e);
                            }
                        }).start();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FavoriteMeals", "Error fetching favorite meals from Firestore: " + e.getMessage(), e);
                        listener.onSyncFailure(e);
                    });
        } catch (Exception e) {
            Log.e("FavoriteMeals", "Error initiating Firestore sync: " + e.getMessage(), e);
            listener.onSyncFailure(e);
        }
    }

    public void deleteAllFavoriteMeals() {
        new Thread(() -> {
            try {
                dao.deleteAllFavoriteMeals();
                String userId = getCurrentUserId();
                if (userId == null) {
                    Log.e("FavoriteMeals", "Cannot clear Firestore: User not logged in");
                    return;
                }
                firestore.collection("users").document(userId).collection("favorite_meals")
                        .get()
                        .addOnSuccessListener(querySnapshot -> {
                            for (var doc : querySnapshot.getDocuments()) {
                                doc.getReference().delete();
                            }
                            Log.d("FavoriteMeals", "All favorite meals cleared from Firestore");
                        })
                        .addOnFailureListener(e -> Log.e("FavoriteMeals", "Error clearing favorite meals from Firestore: " + e.getMessage(), e));
            } catch (Exception e) {
                Log.e("FavoriteMeals", "Error deleting all favorite meals: " + e.getMessage(), e);
            }
        }).start();
    }

    private String getCurrentUserId() {
        return auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
    }
}