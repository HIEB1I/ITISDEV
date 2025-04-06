package com.mobdeve.sustainabite;

import java.io.Serializable;

public class FoodItem implements Serializable {
    private String id;
    private Integer imageResId;
    private String imageString;
    private String name;
    private String kcal;
    private String ingredients;
    private String procedures;
    private String UNum;

    // Constructor for drawable images
    public FoodItem(String id, int imageResId, String name, String kcal, String ingredients, String procedures, String UNum) {
        this.id = id;
        this.imageResId = imageResId;
        this.imageString = null;
        this.name = name;
        this.kcal = kcal;
        this.ingredients = ingredients;
        this.procedures = procedures;
        this.UNum = UNum;
    }

    // Constructor for Base64 images
    public FoodItem(String id, String imageString, String name, String kcal, String ingredients, String procedures, String UNum) {
        this.id = id;
        this.imageResId = null;
        this.imageString = imageString;
        this.name = name;
        this.kcal = kcal;
        this.ingredients = ingredients;
        this.procedures = procedures;
        this.UNum = UNum;
    }

    public String getId() { return id; }

    public Integer getImageResId() { return imageResId; }

    public String getImageString() { return imageString; }

    public String getName() { return name; }

    public String getKcal() { return kcal; }

    public String getIngredients() { return ingredients; }

    public String getProcedures() { return procedures; }

    public String getUNum() { return UNum; }

}
