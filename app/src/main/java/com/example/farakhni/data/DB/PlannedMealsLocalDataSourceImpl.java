package com.example.farakhni.data.DB;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.farakhni.model.PlannedMeal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PlannedMealsLocalDataSourceImpl implements PlannedMealsLocalDataSource {
    private final PlannedMealDao plannedMealDao;
    private static PlannedMealsLocalDataSourceImpl instance;
    private final FirebaseFirestore firestore;
    private final FirebaseAuth auth;

    private PlannedMealsLocalDataSourceImpl(Context context) {
        AppDataBase db = AppDataBase.getInstance(context.getApplicationContext());
        this.plannedMealDao = db.getPlannedMealDAO();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public static synchronized PlannedMealsLocalDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new PlannedMealsLocalDataSourceImpl(context);
        }
        return instance;
    }

    @Override
    public LiveData<List<PlannedMeal>> getAllPlannedMeals() {
        return plannedMealDao.getAllPlannedMeals();
    }

    @Override
    public LiveData<List<PlannedMeal>> getPlannedMealsForDate(String date) {
        return plannedMealDao.getPlannedMealsForDate(date);
    }

    @Override
    public void insertPlannedMeal(PlannedMeal plannedMeal) {
        new Thread(() -> {
            try {
                plannedMealDao.insertPlannedMeal(plannedMeal);
                String userId = getCurrentUserId();
                if (userId == null) {
                    Log.e("PlannedMeals", "Cannot sync to Firestore: User not logged in");
                    return;
                }

                String docId = plannedMeal.getId() + "_" + plannedMeal.getScheduledDate();
                firestore.collection("users").document(userId)
                        .collection("planned_meals").document(docId)
                        .set(plannedMeal.toMap())
                        .addOnSuccessListener(aVoid -> Log.d("PlannedMeals", "Planned meal synced to Firestore: " + docId))
                        .addOnFailureListener(e -> Log.e("PlannedMeals", "Error syncing planned meal to Firestore: " + e.getMessage(), e));
            } catch (Exception e) {
                Log.e("PlannedMeals", "Error inserting planned meal locally or syncing to Firestore: " + e.getMessage(), e);
            }
        }).start();
    }

    @Override
    public void deletePlannedMeal(PlannedMeal plannedMeal) {
        new Thread(() -> {
            try {
                plannedMealDao.deletePlannedMeal(plannedMeal);
                String userId = getCurrentUserId();
                if (userId == null) {
                    Log.e("PlannedMeals", "Cannot delete from Firestore: User not logged in");
                    return;
                }

                String docId = plannedMeal.getId() + "_" + plannedMeal.getScheduledDate();
                firestore.collection("users").document(userId)
                        .collection("planned_meals").document(docId)
                        .delete()
                        .addOnSuccessListener(aVoid -> Log.d("PlannedMeals", "Planned meal deleted from Firestore: " + docId))
                        .addOnFailureListener(e -> Log.e("PlannedMeals", "Error deleting planned meal from Firestore: " + e.getMessage(), e));
            } catch (Exception e) {
                Log.e("PlannedMeals", "Error deleting planned meal locally or from Firestore: " + e.getMessage(), e);
            }
        }).start();
    }

    @Override
    public boolean isMealPlanned(String mealId, String date) {
        return plannedMealDao.isMealPlanned(mealId, date);
    }

    @Override
    public void syncPlannedMealsWithFirestore(OnSyncCompleteListener listener) {
        String userId = getCurrentUserId();
        if (userId == null) {
            Log.e("PlannedMeals", "Sync failed: User not logged in");
            listener.onSyncFailure(new IllegalStateException("User not logged in"));
            return;
        }

        try {
            firestore.collection("users").document(userId).collection("planned_meals")
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        new Thread(() -> {
                            try {
                                for (var doc : querySnapshot.getDocuments()) {
                                    PlannedMeal meal = PlannedMeal.fromMap(doc.getData());
                                    plannedMealDao.insertPlannedMeal(meal);
                                }
                                listener.onSyncSuccess();
                            } catch (Exception e) {
                                Log.e("PlannedMeals", "Error processing Firestore sync: " + e.getMessage(), e);
                                listener.onSyncFailure(e);
                            }
                        }).start();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("PlannedMeals", "Error fetching planned meals from Firestore: " + e.getMessage(), e);
                        listener.onSyncFailure(e);
                    });
        } catch (Exception e) {
            Log.e("PlannedMeals", "Error initiating Firestore sync: " + e.getMessage(), e);
            listener.onSyncFailure(e);
        }
    }

    private String getCurrentUserId() {
        return auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
    }
}