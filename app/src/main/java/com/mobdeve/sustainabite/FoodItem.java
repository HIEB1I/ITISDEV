package com.mobdeve.sustainabite;


import android.graphics.Bitmap;

public class FoodItem {
    private Bitmap image;
    private String name;
    private String kcal;

    private String ingredients;
    private String procedures;

    public FoodItem(Bitmap image, String name, String kcal, String ingredients, String procedures) {
        this.image = image;
        this.name = name;
        this.kcal = kcal;
        this.ingredients = ingredients;
        this.procedures = procedures;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getKcal() {
        return kcal;
    }

    public String getIngredients() { return ingredients; }

    public String getProcedures() { return procedures; }

}


