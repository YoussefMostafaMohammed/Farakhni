package com.example.farakhni.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

@Entity(tableName = "favorite_meals_table")
public class FavoriteMeal extends Meal implements Serializable {
    @PrimaryKey
    @NonNull
    @SerializedName("idMeal")
    private String id;
    private boolean isFavorite;
    private long lastModified;

    public FavoriteMeal() {
        super();
        id = "";
        lastModified = System.currentTimeMillis();
    }

    public FavoriteMeal(Meal meal) {
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
        this.setLastModified(meal.getLastModified());
    }

    @NonNull
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(@NonNull String id) {
        this.id = id;
        super.setId(id);
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
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
        map.put("isFavorite", isFavorite);
        map.put("lastModified", lastModified);
        return map;
    }

    public static FavoriteMeal fromMap(Map<String, Object> map) {
        FavoriteMeal favoriteMeal = new FavoriteMeal();
        favoriteMeal.setId((String) map.get("idMeal"));
        favoriteMeal.setName((String) map.get("strMeal"));
        favoriteMeal.setCategory((String) map.get("strCategory"));
        favoriteMeal.setArea((String) map.get("strArea"));
        favoriteMeal.setInstructions((String) map.get("strInstructions"));
        favoriteMeal.setMealThumb((String) map.get("strMealThumb"));
        favoriteMeal.setTags((String) map.get("strTags"));
        favoriteMeal.setYoutube((String) map.get("strYoutube"));
        favoriteMeal.setSource((String) map.get("strSource"));
        favoriteMeal.setFavorite(map.get("isFavorite") != null ? (Boolean) map.get("isFavorite") : false);
        favoriteMeal.setScheduled(map.get("isScheduled") != null ? (Boolean) map.get("isScheduled") : false);
        favoriteMeal.setLastModified(map.get("lastModified") != null ? ((Long) map.get("lastModified")) : System.currentTimeMillis());

        // Set ingredient and measure fields using setters from Meal class
        for (int i = 1; i <= 20; i++) {
            String ingredient = (String) map.get("strIngredient" + i);
            String measure = (String) map.get("strMeasure" + i);
            switch (i) {
                case 1:
                    favoriteMeal.setStrIngredient1(ingredient);
                    favoriteMeal.setStrMeasure1(measure);
                    break;
                case 2:
                    favoriteMeal.setStrIngredient2(ingredient);
                    favoriteMeal.setStrMeasure2(measure);
                    break;
                case 3:
                    favoriteMeal.setStrIngredient3(ingredient);
                    favoriteMeal.setStrMeasure3(measure);
                    break;
                case 4:
                    favoriteMeal.setStrIngredient4(ingredient);
                    favoriteMeal.setStrMeasure4(measure);
                    break;
                case 5:
                    favoriteMeal.setStrIngredient5(ingredient);
                    favoriteMeal.setStrMeasure5(measure);
                    break;
                case 6:
                    favoriteMeal.setStrIngredient6(ingredient);
                    favoriteMeal.setStrMeasure6(measure);
                    break;
                case 7:
                    favoriteMeal.setStrIngredient7(ingredient);
                    favoriteMeal.setStrMeasure7(measure);
                    break;
                case 8:
                    favoriteMeal.setStrIngredient8(ingredient);
                    favoriteMeal.setStrMeasure8(measure);
                    break;
                case 9:
                    favoriteMeal.setStrIngredient9(ingredient);
                    favoriteMeal.setStrMeasure9(measure);
                    break;
                case 10:
                    favoriteMeal.setStrIngredient10(ingredient);
                    favoriteMeal.setStrMeasure10(measure);
                    break;
                case 11:
                    favoriteMeal.setStrIngredient11(ingredient);
                    favoriteMeal.setStrMeasure11(measure);
                    break;
                case 12:
                    favoriteMeal.setStrIngredient12(ingredient);
                    favoriteMeal.setStrMeasure12(measure);
                    break;
                case 13:
                    favoriteMeal.setStrIngredient13(ingredient);
                    favoriteMeal.setStrMeasure13(measure);
                    break;
                case 14:
                    favoriteMeal.setStrIngredient14(ingredient);
                    favoriteMeal.setStrMeasure14(measure);
                    break;
                case 15:
                    favoriteMeal.setStrIngredient15(ingredient);
                    favoriteMeal.setStrMeasure15(measure);
                    break;
                case 16:
                    favoriteMeal.setStrIngredient16(ingredient);
                    favoriteMeal.setStrMeasure16(measure);
                    break;
                case 17:
                    favoriteMeal.setStrIngredient17(ingredient);
                    favoriteMeal.setStrMeasure17(measure);
                    break;
                case 18:
                    favoriteMeal.setStrIngredient18(ingredient);
                    favoriteMeal.setStrMeasure18(measure);
                    break;
                case 19:
                    favoriteMeal.setStrIngredient19(ingredient);
                    favoriteMeal.setStrMeasure19(measure);
                    break;
                case 20:
                    favoriteMeal.setStrIngredient20(ingredient);
                    favoriteMeal.setStrMeasure20(measure);
                    break;
            }
        }
        return favoriteMeal;
    }
}