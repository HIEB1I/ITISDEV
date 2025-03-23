package com.mobdeve.sustainabite;


public class FoodItem {
    private int image;
    private String name;
    private String kcal;

    private String ingredients;
    private String procedures;

    public FoodItem(int image, String name, String kcal, String ingredients, String procedures) {
        this.image = image;
        this.name = name;
        this.kcal = kcal;
        this.ingredients = ingredients;
        this.procedures = procedures;
    }

    public int getImage() {
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


