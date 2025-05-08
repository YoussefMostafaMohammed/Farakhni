package com.example.farakhni.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Meal implements Serializable {

    @SerializedName("idMeal")
    private String id;

    @SerializedName("strMeal")
    private String name;

    @SerializedName("strCategory")
    private String category;

    @SerializedName("strArea")
    private String area;

    @SerializedName("strInstructions")
    private String instructions;

    @SerializedName("strMealThumb")
    private String mealThumb;

    @SerializedName("strTags")
    private String tags;

    @SerializedName("strYoutube")
    private String youtube;

    @SerializedName("strSource")
    private String source;

    private boolean isFavorite;
    private boolean isScheduled;
    private long lastModified;

    @SerializedName("strIngredient1") public String strIngredient1;
    @SerializedName("strIngredient2") public String strIngredient2;
    @SerializedName("strIngredient3") public String strIngredient3;
    @SerializedName("strIngredient4") public String strIngredient4;
    @SerializedName("strIngredient5") public String strIngredient5;
    @SerializedName("strIngredient6") public String strIngredient6;
    @SerializedName("strIngredient7") public String strIngredient7;
    @SerializedName("strIngredient8") public String strIngredient8;
    @SerializedName("strIngredient9") public String strIngredient9;
    @SerializedName("strIngredient10") public String strIngredient10;
    @SerializedName("strIngredient11") public String strIngredient11;
    @SerializedName("strIngredient12") public String strIngredient12;
    @SerializedName("strIngredient13") public String strIngredient13;
    @SerializedName("strIngredient14") public String strIngredient14;
    @SerializedName("strIngredient15") public String strIngredient15;
    @SerializedName("strIngredient16") public String strIngredient16;
    @SerializedName("strIngredient17") public String strIngredient17;
    @SerializedName("strIngredient18") public String strIngredient18;
    @SerializedName("strIngredient19") public String strIngredient19;
    @SerializedName("strIngredient20") public String strIngredient20;

    @SerializedName("strMeasure1") public String strMeasure1;
    @SerializedName("strMeasure2") public String strMeasure2;
    @SerializedName("strMeasure3") public String strMeasure3;
    @SerializedName("strMeasure4") public String strMeasure4;
    @SerializedName("strMeasure5") public String strMeasure5;
    @SerializedName("strMeasure6") public String strMeasure6;
    @SerializedName("strMeasure7") public String strMeasure7;
    @SerializedName("strMeasure8") public String strMeasure8;
    @SerializedName("strMeasure9") public String strMeasure9;
    @SerializedName("strMeasure10") public String strMeasure10;
    @SerializedName("strMeasure11") public String strMeasure11;
    @SerializedName("strMeasure12") public String strMeasure12;
    @SerializedName("strMeasure13") public String strMeasure13;
    @SerializedName("strMeasure14") public String strMeasure14;
    @SerializedName("strMeasure15") public String strMeasure15;
    @SerializedName("strMeasure16") public String strMeasure16;
    @SerializedName("strMeasure17") public String strMeasure17;
    @SerializedName("strMeasure18") public String strMeasure18;
    @SerializedName("strMeasure19") public String strMeasure19;
    @SerializedName("strMeasure20") public String strMeasure20;

    public Meal() {
        this.id = "";
        this.lastModified = System.currentTimeMillis();
    }

    public Meal(FavoriteMeal favMeal) {
        this.id = favMeal.getId();
        this.setName(favMeal.getName());
        this.setCategory(favMeal.getCategory());
        this.setArea(favMeal.getArea());
        this.setInstructions(favMeal.getInstructions());
        this.setMealThumb(favMeal.getMealThumb());
        this.setTags(favMeal.getTags());
        this.setYoutube(favMeal.getYoutube());
        this.setSource(favMeal.getSource());
        this.setStrIngredient1(favMeal.getStrIngredient1());
        this.setStrIngredient2(favMeal.getStrIngredient2());
        this.setStrIngredient3(favMeal.getStrIngredient3());
        this.setStrIngredient4(favMeal.getStrIngredient4());
        this.setStrIngredient5(favMeal.getStrIngredient5());
        this.setStrIngredient6(favMeal.getStrIngredient6());
        this.setStrIngredient7(favMeal.getStrIngredient7());
        this.setStrIngredient8(favMeal.getStrIngredient8());
        this.setStrIngredient9(favMeal.getStrIngredient9());
        this.setStrIngredient10(favMeal.getStrIngredient10());
        this.setStrIngredient11(favMeal.getStrIngredient11());
        this.setStrIngredient12(favMeal.getStrIngredient12());
        this.setStrIngredient13(favMeal.getStrIngredient13());
        this.setStrIngredient14(favMeal.getStrIngredient14());
        this.setStrIngredient15(favMeal.getStrIngredient15());
        this.setStrIngredient16(favMeal.getStrIngredient16());
        this.setStrIngredient17(favMeal.getStrIngredient17());
        this.setStrIngredient18(favMeal.getStrIngredient18());
        this.setStrIngredient19(favMeal.getStrIngredient19());
        this.setStrIngredient20(favMeal.getStrIngredient20());
        this.setStrMeasure1(favMeal.getStrMeasure1());
        this.setStrMeasure2(favMeal.getStrMeasure2());
        this.setStrMeasure3(favMeal.getStrMeasure3());
        this.setStrMeasure4(favMeal.getStrMeasure4());
        this.setStrMeasure5(favMeal.getStrMeasure5());
        this.setStrMeasure6(favMeal.getStrMeasure6());
        this.setStrMeasure7(favMeal.getStrMeasure7());
        this.setStrMeasure8(favMeal.getStrMeasure8());
        this.setStrMeasure9(favMeal.getStrMeasure9());
        this.setStrMeasure10(favMeal.getStrMeasure10());
        this.setStrMeasure11(favMeal.getStrMeasure11());
        this.setStrMeasure12(favMeal.getStrMeasure12());
        this.setStrMeasure13(favMeal.getStrMeasure13());
        this.setStrMeasure14(favMeal.getStrMeasure14());
        this.setStrMeasure15(favMeal.getStrMeasure15());
        this.setStrMeasure16(favMeal.getStrMeasure16());
        this.setStrMeasure17(favMeal.getStrMeasure17());
        this.setStrMeasure18(favMeal.getStrMeasure18());
        this.setStrMeasure19(favMeal.getStrMeasure19());
        this.setStrMeasure20(favMeal.getStrMeasure20());
        this.setFavorite(favMeal.isFavorite());
        this.setScheduled(favMeal.isScheduled());
        this.lastModified = System.currentTimeMillis();
    }

    public Meal(PlannedMeal plannedMeal) {
        this.id = plannedMeal.getMealId();
        this.setName(plannedMeal.getName());
        this.setCategory(plannedMeal.getCategory());
        this.setArea(plannedMeal.getArea());
        this.setInstructions(plannedMeal.getInstructions());
        this.setMealThumb(plannedMeal.getMealThumb());
        this.setTags(plannedMeal.getTags());
        this.setYoutube(plannedMeal.getYoutube());
        this.setSource(plannedMeal.getSource());
        this.setStrIngredient1(plannedMeal.getStrIngredient1());
        this.setStrIngredient2(plannedMeal.getStrIngredient2());
        this.setStrIngredient3(plannedMeal.getStrIngredient3());
        this.setStrIngredient4(plannedMeal.getStrIngredient4());
        this.setStrIngredient5(plannedMeal.getStrIngredient5());
        this.setStrIngredient6(plannedMeal.getStrIngredient6());
        this.setStrIngredient7(plannedMeal.getStrIngredient7());
        this.setStrIngredient8(plannedMeal.getStrIngredient8());
        this.setStrIngredient9(plannedMeal.getStrIngredient9());
        this.setStrIngredient10(plannedMeal.getStrIngredient10());
        this.setStrIngredient11(plannedMeal.getStrIngredient11());
        this.setStrIngredient12(plannedMeal.getStrIngredient12());
        this.setStrIngredient13(plannedMeal.getStrIngredient13());
        this.setStrIngredient14(plannedMeal.getStrIngredient14());
        this.setStrIngredient15(plannedMeal.getStrIngredient15());
        this.setStrIngredient16(plannedMeal.getStrIngredient16());
        this.setStrIngredient17(plannedMeal.getStrIngredient17());
        this.setStrIngredient18(plannedMeal.getStrIngredient18());
        this.setStrIngredient19(plannedMeal.getStrIngredient19());
        this.setStrIngredient20(plannedMeal.getStrIngredient20());
        this.setStrMeasure1(plannedMeal.getStrMeasure1());
        this.setStrMeasure2(plannedMeal.getStrMeasure2());
        this.setStrMeasure3(plannedMeal.getStrMeasure3());
        this.setStrMeasure4(plannedMeal.getStrMeasure4());
        this.setStrMeasure5(plannedMeal.getStrMeasure5());
        this.setStrMeasure6(plannedMeal.getStrMeasure6());
        this.setStrMeasure7(plannedMeal.getStrMeasure7());
        this.setStrMeasure8(plannedMeal.getStrMeasure8());
        this.setStrMeasure9(plannedMeal.getStrMeasure9());
        this.setStrMeasure10(plannedMeal.getStrMeasure10());
        this.setStrMeasure11(plannedMeal.getStrMeasure11());
        this.setStrMeasure12(plannedMeal.getStrMeasure12());
        this.setStrMeasure13(plannedMeal.getStrMeasure13());
        this.setStrMeasure14(plannedMeal.getStrMeasure14());
        this.setStrMeasure15(plannedMeal.getStrMeasure15());
        this.setStrMeasure16(plannedMeal.getStrMeasure16());
        this.setStrMeasure17(plannedMeal.getStrMeasure17());
        this.setStrMeasure18(plannedMeal.getStrMeasure18());
        this.setStrMeasure19(plannedMeal.getStrMeasure19());
        this.setStrMeasure20(plannedMeal.getStrMeasure20());
        this.setFavorite(plannedMeal.isFavorite());
        this.setScheduled(plannedMeal.isScheduled());
        this.lastModified = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getMealThumb() { return mealThumb; }
    public void setMealThumb(String mealThumb) { this.mealThumb = mealThumb; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getYoutube() { return youtube; }
    public void setYoutube(String youtube) { this.youtube = youtube; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public boolean isScheduled() { return isScheduled; }
    public void setScheduled(boolean scheduled) { isScheduled = scheduled; }

    public long getLastModified() { return lastModified; }
    public void setLastModified(long lastModified) { this.lastModified = lastModified; }

    public String getStrIngredient1() { return strIngredient1; }
    public void setStrIngredient1(String strIngredient1) { this.strIngredient1 = strIngredient1; }
    public String getStrIngredient2() { return strIngredient2; }
    public void setStrIngredient2(String strIngredient2) { this.strIngredient2 = strIngredient2; }
    public String getStrIngredient3() { return strIngredient3; }
    public void setStrIngredient3(String strIngredient3) { this.strIngredient3 = strIngredient3; }
    public String getStrIngredient4() { return strIngredient4; }
    public void setStrIngredient4(String strIngredient4) { this.strIngredient4 = strIngredient4; }
    public String getStrIngredient5() { return strIngredient5; }
    public void setStrIngredient5(String strIngredient5) { this.strIngredient5 = strIngredient5; }
    public String getStrIngredient6() { return strIngredient6; }
    public void setStrIngredient6(String strIngredient6) { this.strIngredient6 = strIngredient6; }
    public String getStrIngredient7() { return strIngredient7; }
    public void setStrIngredient7(String strIngredient7) { this.strIngredient7 = strIngredient7; }
    public String getStrIngredient8() { return strIngredient8; }
    public void setStrIngredient8(String strIngredient8) { this.strIngredient8 = strIngredient8; }
    public String getStrIngredient9() { return strIngredient9; }
    public void setStrIngredient9(String strIngredient9) { this.strIngredient9 = strIngredient9; }
    public String getStrIngredient10() { return strIngredient10; }
    public void setStrIngredient10(String strIngredient10) { this.strIngredient10 = strIngredient10; }
    public String getStrIngredient11() { return strIngredient11; }
    public void setStrIngredient11(String strIngredient11) { this.strIngredient11 = strIngredient11; }
    public String getStrIngredient12() { return strIngredient12; }
    public void setStrIngredient12(String strIngredient12) { this.strIngredient12 = strIngredient12; }
    public String getStrIngredient13() { return strIngredient13; }
    public void setStrIngredient13(String strIngredient13) { this.strIngredient13 = strIngredient13; }
    public String getStrIngredient14() { return strIngredient14; }
    public void setStrIngredient14(String strIngredient14) { this.strIngredient14 = strIngredient14; }
    public String getStrIngredient15() { return strIngredient15; }
    public void setStrIngredient15(String strIngredient15) { this.strIngredient15 = strIngredient15; }
    public String getStrIngredient16() { return strIngredient16; }
    public void setStrIngredient16(String strIngredient16) { this.strIngredient16 = strIngredient16; }
    public String getStrIngredient17() { return strIngredient17; }
    public void setStrIngredient17(String strIngredient17) { this.strIngredient17 = strIngredient17; }
    public String getStrIngredient18() { return strIngredient18; }
    public void setStrIngredient18(String strIngredient18) { this.strIngredient18 = strIngredient18; }
    public String getStrIngredient19() { return strIngredient19; }
    public void setStrIngredient19(String strIngredient19) { this.strIngredient19 = strIngredient19; }
    public String getStrIngredient20() { return strIngredient20; }
    public void setStrIngredient20(String strIngredient20) { this.strIngredient20 = strIngredient20; }

    public String getStrMeasure1() { return strMeasure1; }
    public void setStrMeasure1(String strMeasure1) { this.strMeasure1 = strMeasure1; }
    public String getStrMeasure2() { return strMeasure2; }
    public void setStrMeasure2(String strMeasure2) { this.strMeasure2 = strMeasure2; }
    public String getStrMeasure3() { return strMeasure3; }
    public void setStrMeasure3(String strMeasure3) { this.strMeasure3 = strMeasure3; }
    public String getStrMeasure4() { return strMeasure4; }
    public void setStrMeasure4(String strMeasure4) { this.strMeasure4 = strMeasure4; }
    public String getStrMeasure5() { return strMeasure5; }
    public void setStrMeasure5(String strMeasure5) { this.strMeasure5 = strMeasure5; }
    public String getStrMeasure6() { return strMeasure6; }
    public void setStrMeasure6(String strMeasure6) { this.strMeasure6 = strMeasure6; }
    public String getStrMeasure7() { return strMeasure7; }
    public void setStrMeasure7(String strMeasure7) { this.strMeasure7 = strMeasure7; }
    public String getStrMeasure8() { return strMeasure8; }
    public void setStrMeasure8(String strMeasure8) { this.strMeasure8 = strMeasure8; }
    public String getStrMeasure9() { return strMeasure9; }
    public void setStrMeasure9(String strMeasure9) { this.strMeasure9 = strMeasure9; }
    public String getStrMeasure10() { return strMeasure10; }
    public void setStrMeasure10(String strMeasure10) { this.strMeasure10 = strMeasure10; }
    public String getStrMeasure11() { return strMeasure11; }
    public void setStrMeasure11(String strMeasure11) { this.strMeasure11 = strMeasure11; }
    public String getStrMeasure12() { return strMeasure12; }
    public void setStrMeasure12(String strMeasure12) { this.strMeasure12 = strMeasure12; }
    public String getStrMeasure13() { return strMeasure13; }
    public void setStrMeasure13(String strMeasure13) { this.strMeasure13 = strMeasure13; }
    public String getStrMeasure14() { return strMeasure14; }
    public void setStrMeasure14(String strMeasure14) { this.strMeasure14 = strMeasure14; }
    public String getStrMeasure15() { return strMeasure15; }
    public void setStrMeasure15(String strMealastModifiedsure15) { this.strMeasure15 = strMeasure15; }
    public String getStrMeasure16() { return strMeasure16; }
    public void setStrMeasure16(String strMeasure16) { this.strMeasure16 = strMeasure16; }
    public String getStrMeasure17() { return strMeasure17; }
    public void setStrMeasure17(String strMeasure17) { this.strMeasure17 = strMeasure17; }
    public String getStrMeasure18() { return strMeasure18; }
    public void setStrMeasure18(String strMeasure18) { this.strMeasure18 = strMeasure18; }
    public String getStrMeasure19() { return strMeasure19; }
    public void setStrMeasure19(String strMeasure19) { this.strMeasure19 = strMeasure19; }
    public String getStrMeasure20() { return strMeasure20; }
    public void setStrMeasure20(String strMeasure20) { this.strMeasure20 = strMeasure20; }

    public List<Ingredient> getIngredientsList() {
        List<Ingredient> list = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            try {
                String ingField = (String) Meal.class.getDeclaredField("strIngredient" + i).get(this);
                String measField = (String) Meal.class.getDeclaredField("strMeasure" + i).get(this);

                if (ingField != null && !ingField.trim().isEmpty()) {
                    Ingredient ing = new Ingredient();
                    ing.setIngredient(ingField);
                    ing.setMeasure(measField != null ? measField : "");
                    list.add(ing);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idMeal", id);
        map.put("strMeal", name);
        map.put("strCategory", category);
        map.put("strArea", area);
        map.put("strInstructions", instructions);
        map.put("strMealThumb", mealThumb);
        map.put("strTags", tags);
        map.put("strYoutube", youtube);
        map.put("strSource", source);
        map.put("isFavorite", isFavorite);
        map.put("isScheduled", isScheduled);
        map.put("lastModified", lastModified);
        for (int i = 1; i <= 20; i++) {
            try {
                String ingField = (String) Meal.class.getDeclaredField("strIngredient" + i).get(this);
                String measField = (String) Meal.class.getDeclaredField("strMeasure" + i).get(this);
                map.put("strIngredient" + i, ingField != null ? ingField : "");
                map.put("strMeasure" + i, measField != null ? measField : "");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static Meal fromMap(Map<String, Object> map) {
        Meal meal = new Meal();
        meal.setId((String) map.get("idMeal"));
        meal.setName((String) map.get("strMeal"));
        meal.setCategory((String) map.get("strCategory"));
        meal.setArea((String) map.get("strArea"));
        meal.setInstructions((String) map.get("strInstructions"));
        meal.setMealThumb((String) map.get("strMealThumb"));
        meal.setTags((String) map.get("strTags"));
        meal.setYoutube((String) map.get("strYoutube"));
        meal.setSource((String) map.get("strSource"));
        meal.setFavorite(map.get("isFavorite") != null ? (Boolean) map.get("isFavorite") : false);
        meal.setScheduled(map.get("isScheduled") != null ? (Boolean) map.get("isScheduled") : false);
        meal.setLastModified(map.get("lastModified") != null ? ((Long) map.get("lastModified")) : System.currentTimeMillis());
        for (int i = 1; i <= 20; i++) {
            try {
                Meal.class.getDeclaredField("strIngredient" + i).set(meal, map.get("strIngredient" + i));
                Meal.class.getDeclaredField("strMeasure" + i).set(meal, map.get("strMeasure" + i));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return meal;
    }
}