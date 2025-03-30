package com.mobdeve.sustainabite;

import android.graphics.Bitmap;

public class FoodHome {
    private Bitmap image;
    private String name;
    private String kcal;
    private String procedure;
    private String ingredients;

    // ✅ Ensure this constructor matches how you're adding objects to the list
    public FoodHome(Bitmap image, String name, String kcal, String procedure, String ingredients) {
        this.image = image;
        this.name = name;
        this.kcal = kcal;
        this.procedure = procedure;
        this.ingredients = ingredients;
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

    public String getProcedure() {
        return procedure;
    }

    public String getIngredients() {
        return ingredients;
    }
}
