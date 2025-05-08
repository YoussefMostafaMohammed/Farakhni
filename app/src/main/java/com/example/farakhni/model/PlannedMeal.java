package com.example.farakhni.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

@Entity(
        tableName = "planned_meals_table",
        primaryKeys = {"id", "scheduledDate"}
)
public class PlannedMeal extends Meal implements Serializable {
    @NonNull
    @SerializedName("idMeal")
    private String id;
    @NonNull
    private String scheduledDate; // Stored as YYYY-MM-DD
    private long lastModified;

    public PlannedMeal() {
        this.id = "";
        this.scheduledDate = "";
        this.lastModified = System.currentTimeMillis();
    }

    public PlannedMeal(@NonNull String mealId, @NonNull String scheduledDate) {
        this.id = mealId;
        this.scheduledDate = scheduledDate;
        this.lastModified = System.currentTimeMillis();
    }

    public PlannedMeal(Meal meal, @NonNull String scheduledDate) {
        super();
        this.setId(meal.getId());
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
        this.scheduledDate = scheduledDate;
        this.lastModified = meal.getLastModified();
    }

    @Ignore
    public PlannedMeal(@NonNull String userId, @NonNull String mealId, @NonNull String scheduledDate) {
        this.id = mealId;
        this.scheduledDate = scheduledDate;
        this.lastModified = System.currentTimeMillis();
    }

    @NonNull
    public String getMealId() {
        return id;
    }

    public void setMealId(@NonNull String mealId) {
        this.id = mealId;
        super.setId(mealId);
    }

    @NonNull
    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(@NonNull String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("idMeal", id);
        map.put("scheduledDate", scheduledDate);
        map.put("lastModified", lastModified);
        return map;
    }

    public static PlannedMeal fromMap(Map<String, Object> map) {
        PlannedMeal plannedMeal = new PlannedMeal();
        plannedMeal.setId((String) map.get("idMeal"));
        plannedMeal.setName((String) map.get("strMeal"));
        plannedMeal.setCategory((String) map.get("strCategory"));
        plannedMeal.setArea((String) map.get("strArea"));
        plannedMeal.setInstructions((String) map.get("strInstructions"));
        plannedMeal.setMealThumb((String) map.get("strMealThumb"));
        plannedMeal.setTags((String) map.get("strTags"));
        plannedMeal.setYoutube((String) map.get("strYoutube"));
        plannedMeal.setSource((String) map.get("strSource"));
        plannedMeal.setFavorite(map.get("isFavorite") != null ? (Boolean) map.get("isFavorite") : false);
        plannedMeal.setScheduled(map.get("isScheduled") != null ? (Boolean) map.get("isScheduled") : false);
        plannedMeal.setScheduledDate((String) map.get("scheduledDate"));
        plannedMeal.setLastModified(map.get("lastModified") != null ? ((Long) map.get("lastModified")) : System.currentTimeMillis());

        // Set ingredient and measure fields using setters from Meal class
        for (int i = 1; i <= 20; i++) {
            String ingredient = (String) map.get("strIngredient" + i);
            String measure = (String) map.get("strMeasure" + i);
            switch (i) {
                case 1:
                    plannedMeal.setStrIngredient1(ingredient);
                    plannedMeal.setStrMeasure1(measure);
                    break;
                case 2:
                    plannedMeal.setStrIngredient2(ingredient);
                    plannedMeal.setStrMeasure2(measure);
                    break;
                case 3:
                    plannedMeal.setStrIngredient3(ingredient);
                    plannedMeal.setStrMeasure3(measure);
                    break;
                case 4:
                    plannedMeal.setStrIngredient4(ingredient);
                    plannedMeal.setStrMeasure4(measure);
                    break;
                case 5:
                    plannedMeal.setStrIngredient5(ingredient);
                    plannedMeal.setStrMeasure5(measure);
                    break;
                case 6:
                    plannedMeal.setStrIngredient6(ingredient);
                    plannedMeal.setStrMeasure6(measure);
                    break;
                case 7:
                    plannedMeal.setStrIngredient7(ingredient);
                    plannedMeal.setStrMeasure7(measure);
                    break;
                case 8:
                    plannedMeal.setStrIngredient8(ingredient);
                    plannedMeal.setStrMeasure8(measure);
                    break;
                case 9:
                    plannedMeal.setStrIngredient9(ingredient);
                    plannedMeal.setStrMeasure9(measure);
                    break;
                case 10:
                    plannedMeal.setStrIngredient10(ingredient);
                    plannedMeal.setStrMeasure10(measure);
                    break;
                case 11:
                    plannedMeal.setStrIngredient11(ingredient);
                    plannedMeal.setStrMeasure11(measure);
                    break;
                case 12:
                    plannedMeal.setStrIngredient12(ingredient);
                    plannedMeal.setStrMeasure12(measure);
                    break;
                case 13:
                    plannedMeal.setStrIngredient13(ingredient);
                    plannedMeal.setStrMeasure13(measure);
                    break;
                case 14:
                    plannedMeal.setStrIngredient14(ingredient);
                    plannedMeal.setStrMeasure14(measure);
                    break;
                case 15:
                    plannedMeal.setStrIngredient15(ingredient);
                    plannedMeal.setStrMeasure15(measure);
                    break;
                case 16:
                    plannedMeal.setStrIngredient16(ingredient);
                    plannedMeal.setStrMeasure16(measure);
                    break;
                case 17:
                    plannedMeal.setStrIngredient17(ingredient);
                    plannedMeal.setStrMeasure17(measure);
                    break;
                case 18:
                    plannedMeal.setStrIngredient18(ingredient);
                    plannedMeal.setStrMeasure18(measure);
                    break;
                case 19:
                    plannedMeal.setStrIngredient19(ingredient);
                    plannedMeal.setStrMeasure19(measure);
                    break;
                case 20:
                    plannedMeal.setStrIngredient20(ingredient);
                    plannedMeal.setStrMeasure20(measure);
                    break;
            }
        }
        return plannedMeal;
    }
}