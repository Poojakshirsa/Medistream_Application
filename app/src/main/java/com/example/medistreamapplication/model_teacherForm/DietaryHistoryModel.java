package com.example.medistreamapplication.model_teacherForm;

public class DietaryHistoryModel {
    private String userId;
    private String dietType;
    private boolean breakfastDaily;
    private String numberOfMeals;
    private String eatingBetweenMeals;
    private boolean consumeFruits;
    private String fruitQuantity;
    private String eatingOutside;
    private String cookingOil;
    private boolean changeCookingOil;
    private boolean extraSalt;
    private String teaCoffeeCount; // New field
    private boolean nonVegetarian; // New field
    private String dietaryHabits;
    // Default constructor required for Firebase
    public DietaryHistoryModel() {}

    // Getter and Setter methods
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDietType() { return dietType; }
    public void setDietType(String dietType) { this.dietType = dietType; }

    public boolean isBreakfastDaily() { return breakfastDaily; }
    public void setBreakfastDaily(boolean breakfastDaily) { this.breakfastDaily = breakfastDaily; }

    public String getNumberOfMeals() { return numberOfMeals; }
    public void setNumberOfMeals(String numberOfMeals) { this.numberOfMeals = numberOfMeals; }

    public String getEatingBetweenMeals() { return eatingBetweenMeals; }
    public void setEatingBetweenMeals(String eatingBetweenMeals) { this.eatingBetweenMeals = eatingBetweenMeals; }

    public boolean isConsumeFruits() { return consumeFruits; }
    public void setConsumeFruits(boolean consumeFruits) { this.consumeFruits = consumeFruits; }

    public String getFruitQuantity() { return fruitQuantity; }
    public void setFruitQuantity(String fruitQuantity) { this.fruitQuantity = fruitQuantity; }

    public String getEatingOutside() { return eatingOutside; }
    public void setEatingOutside(String eatingOutside) { this.eatingOutside = eatingOutside; }

    public String getCookingOil() { return cookingOil; }
    public void setCookingOil(String cookingOil) { this.cookingOil = cookingOil; }

    public boolean isChangeCookingOil() { return changeCookingOil; }
    public void setChangeCookingOil(boolean changeCookingOil) { this.changeCookingOil = changeCookingOil; }

    public boolean isExtraSalt() { return extraSalt; }
    public void setExtraSalt(boolean extraSalt) { this.extraSalt = extraSalt; }

    public String getTeaCoffeeCount() {
        return teaCoffeeCount;
    }

    public void setTeaCoffeeCount(String teaCoffeeCount) {
        this.teaCoffeeCount = teaCoffeeCount;
    }

    public boolean isNonVegetarian() {
        return nonVegetarian;
    }

    public void setNonVegetarian(boolean nonVegetarian) {
        this.nonVegetarian = nonVegetarian;
    }

    public String getDietaryHabits() {
        return dietaryHabits;
    }

    public void setDietaryHabits(String dietaryHabits) {
        this.dietaryHabits = dietaryHabits;
    }
}
