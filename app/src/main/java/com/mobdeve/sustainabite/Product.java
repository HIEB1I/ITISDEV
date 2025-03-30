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
    private int imageResource;

    public Product(String name, String fid,  int qty_val, String qty_type, String doi, String doe, String storage, String remarks, int imageResource) {
        this.name = name;
        this.fid = fid; //Added this so Food ID can be stored.
        this.qty_val = qty_val;
        this.qty_type = qty_type;
        this.doi = doi;
        this.doe = doe;
        this.storage = storage;
        this.remarks = remarks;
        this.imageResource = imageResource;
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

    public int getImageResource() {
        return imageResource;
    }
}
