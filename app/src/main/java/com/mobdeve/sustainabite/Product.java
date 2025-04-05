package com.mobdeve.sustainabite;

public class Product {
    private String name;
    private String fid; // Added Food ID so this can be stored
    private int qty_val;
    private String qty_type;
    private String doi;
    private String doe;
    private String storage;
    private String remarks;
    private String imageString; //changed to string to accomodate for bitmap.

    public Product(String name, String fid,  int qty_val, String qty_type, String doi, String doe, String storage, String remarks, String imageString) {
        this.name = name;
        this.fid = fid; //Added this so Food ID can be stored.
        this.qty_val = qty_val;
        this.qty_type = qty_type;
        this.doi = doi;
        this.doe = doe;
        this.storage = storage;
        this.remarks = remarks;
        this.imageString = imageString;
    }

    // No-argument constructor for Firestore deserialization
    public Product() {
        // Empty constructor needed for Firestore
    }

    public String getName() {
        return name;
    }

    public String getFid() {
        return fid;
    }

    public int getQty_Val() {
        return qty_val;
    }

    public String getQty_Type() {
        return qty_type;
    }

    public String getDOI() {
        return doi;
    }

    public String getDOE() {
        return doe;
    }

    public String getStorage() {
        return storage;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getImageString() {
        return imageString;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setFoodId(String fid) {
        this.fid = fid;
    }

    public void setQty_Val(int qty_val) {
        this.qty_val = qty_val;
    }

    public void setQty_Type(String qty_type) {
        this.qty_type = qty_type;
    }

    public void setDOI(String doi) {
        this.doi = doi;
    }

    public void setDOE(String doe) {
        this.doe = doe;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }
}
