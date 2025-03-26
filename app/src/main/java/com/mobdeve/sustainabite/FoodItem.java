package com.mobdeve.sustainabite;

public class FoodItem {
    private Integer imageResId;
    private String imageString;
    private String name;
    private String kcal;
    private String ingredients;
    private String procedures;

    // Constructor for drawable images
    public FoodItem(int imageResId, String name, String kcal, String ingredients, String procedures) {
        this.imageResId = imageResId;
        this.imageString = null;
        this.name = name;
        this.kcal = kcal;
        this.ingredients = ingredients;
        this.procedures = procedures;
    }

    // Constructor for Base64 images
    public FoodItem(String imageString, String name, String kcal, String ingredients, String procedures) {
        this.imageResId = null;
        this.imageString = imageString;
        this.name = name;
        this.kcal = kcal;
        this.ingredients = ingredients;
        this.procedures = procedures;
    }

    public Integer getImageResId() { return imageResId; }

    public String getImageString() { return imageString; }

    public String getName() { return name; }

    public String getKcal() { return kcal; }

    public String getIngredients() { return ingredients; }

    public String getProcedures() { return procedures; }
}
