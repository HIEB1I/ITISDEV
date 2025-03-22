package com.mobdeve.sustainabite;

public class Product {
    private String name;
    private String quantity;
    private int imageResource;

    public Product(String name, String quantity, int imageResource) {
        this.name = name;
        this.quantity = quantity;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public int getImageResource() {
        return imageResource;
    }
}
