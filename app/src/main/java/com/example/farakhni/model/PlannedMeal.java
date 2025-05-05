package com.example.farakhni.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.io.Serializable;

@Entity(
        tableName = "planned_meals_table",
        primaryKeys = {"id", "scheduledDate"}
)
public class PlannedMeal extends Meal implements Serializable {
    @NonNull
    private String id;
    @NonNull
    private String scheduledDate; // Stored as YYYY-MM-DD

    public PlannedMeal() {
        this.id = "";
        this.scheduledDate = "";
    }

    public PlannedMeal(Meal meal) {
        super();
        this.id = meal.getId();
        this.setName(meal.getName());
        this.setCategory(meal.getCategory());
        this.setArea(meal.getArea());
        this.setInstructions(meal.getInstructions());
        this.setMealThumb(meal.getMealThumb());
        this.setTags(meal.getTags());
        this.setYoutube(meal.getYoutube());
        this.setSource(meal.getSource());
        this.setStrIngredient1(meal.getStrIngredient1());
        this.setStrIngredient2(meal.getStrIngredient2());
        this.setStrIngredient3(meal.getStrIngredient3());
        this.setStrIngredient4(meal.getStrIngredient4());
        this.setStrIngredient5(meal.getStrIngredient5());
        this.setStrIngredient6(meal.getStrIngredient6());
        this.setStrIngredient7(meal.getStrIngredient7());
        this.setStrIngredient8(meal.getStrIngredient8());
        this.setStrIngredient9(meal.getStrIngredient9());
        this.setStrIngredient10(meal.getStrIngredient10());
        this.setStrIngredient11(meal.getStrIngredient11());
        this.setStrIngredient12(meal.getStrIngredient12());
        this.setStrIngredient13(meal.getStrIngredient13());
        this.setStrIngredient14(meal.getStrIngredient14());
        this.setStrIngredient15(meal.getStrIngredient15());
        this.setStrIngredient16(meal.getStrIngredient16());
        this.setStrIngredient17(meal.getStrIngredient17());
        this.setStrIngredient18(meal.getStrIngredient18());
        this.setStrIngredient19(meal.getStrIngredient19());
        this.setStrIngredient20(meal.getStrIngredient20());
        this.setStrMeasure1(meal.getStrMeasure1());
        this.setStrMeasure2(meal.getStrMeasure2());
        this.setStrMeasure3(meal.getStrMeasure3());
        this.setStrMeasure4(meal.getStrMeasure4());
        this.setStrMeasure5(meal.getStrMeasure5());
        this.setStrMeasure6(meal.getStrMeasure6());
        this.setStrMeasure7(meal.getStrMeasure7());
        this.setStrMeasure8(meal.getStrMeasure8());
        this.setStrMeasure9(meal.getStrMeasure9());
        this.setStrMeasure10(meal.getStrMeasure10());
        this.setStrMeasure11(meal.getStrMeasure11());
        this.setStrMeasure12(meal.getStrMeasure12());
        this.setStrMeasure13(meal.getStrMeasure13());
        this.setStrMeasure14(meal.getStrMeasure14());
        this.setStrMeasure15(meal.getStrMeasure15());
        this.setStrMeasure16(meal.getStrMeasure16());
        this.setStrMeasure17(meal.getStrMeasure17());
        this.setStrMeasure18(meal.getStrMeasure18());
        this.setStrMeasure19(meal.getStrMeasure19());
        this.setStrMeasure20(meal.getStrMeasure20());
        this.setFavorite(meal.isFavorite());
        this.setScheduled(meal.isScheduled());
    }

    @Ignore
    public PlannedMeal(@NonNull String userId, @NonNull String mealId, @NonNull String scheduledDate) {
        this.id = mealId;
        this.scheduledDate = scheduledDate;
    }

    @NonNull
    public String getMealId() {
        return id;
    }

    public void setMealId(@NonNull String mealId) {
        this.id = mealId;
    }

    @NonNull
    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(@NonNull String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
}
