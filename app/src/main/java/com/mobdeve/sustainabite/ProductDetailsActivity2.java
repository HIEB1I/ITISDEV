package com.mobdeve.sustainabite;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailsActivity2 extends AppCompatActivity {

    private DBManager dbManager;
    private String UFID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        // Initialize DBManager
        dbManager = new DBManager();

        // Retrieve the food name (or ID) from the intent
        String foodName = getIntent().getStringExtra("FOOD_NAME");
        Log.d("UserPrefs", "SEARCH: " + foodName);

        if (foodName != null && dbManager != null) {
            dbManager.getProductById(foodName, new DBManager.SearchFoodLoadedListener() {
                @Override
                public void onSuccess(String FID, String FName, String FQuanType, String FQuantity, String FDOE, String FDOI, String FImage) {
                    // Bind Data to Views
                    ((TextView) findViewById(R.id.productName)).setText(FName);
                    ((TextView) findViewById(R.id.productQty_Val)).setText(FQuantity);
                    ((TextView) findViewById(R.id.productQty_Type)).setText(FQuanType);
                    ((TextView) findViewById(R.id.productDOI)).setText(DBManager.convertDate(FDOI));
                    ((TextView) findViewById(R.id.productDOE)).setText(DBManager.convertDate(FDOE));
UFID = FID;
                    Bitmap bitmap = DBManager.decodeBase64ToBitmap(FImage);
                    if (bitmap != null) {
                        ((ImageView) findViewById(R.id.productImage)).setImageBitmap(bitmap);
                    } else {
                        ((ImageView) findViewById(R.id.productImage)).setImageResource(R.drawable.banana);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(ProductDetailsActivity2.this, "Error fetching product: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "No Product ID Provided or DBManager is null", Toast.LENGTH_LONG).show();
        }

        // Back button
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Edit button
        ImageButton editButton = findViewById(R.id.btnEditEntry);
        editButton.setOnClickListener(v -> goToEditEntry());
    }


public void goToEditEntry(){
        Intent intent = new Intent(this, editEntry.class);

        // Retrieve food ID and other data from the current activity
        String FID = getIntent().getStringExtra("FOOD_ID"); // Ensure consistency
        intent.putExtra("FOOD_ID", FID);
        intent.putExtra("productName", ((TextView) findViewById(R.id.productName)).getText().toString());
        intent.putExtra("productQty_Val", ((TextView) findViewById(R.id.productQty_Val)).getText().toString());
        intent.putExtra("productQty_Type", ((TextView) findViewById(R.id.productQty_Type)).getText().toString());
        intent.putExtra("productDOI", ((TextView) findViewById(R.id.productDOI)).getText().toString());
        intent.putExtra("productDOE", ((TextView) findViewById(R.id.productDOE)).getText().toString());

        // Fetch additional data from Firestore if needed
        if (UFID != null) {
            dbManager.fetchSpecificFood(UFID, new DBManager.CheckFoodIDValidity(){
                @Override
                public void onSuccess(String storage, String remarks, String productImage) {
                    intent.putExtra("productStorage", storage);
                    intent.putExtra("productRemarks", remarks);
                    intent.putExtra("productImage", productImage);

                    Log.d("Firestore", "Storage: " + storage);
                    Log.d("Firestore", "Remarks: " + remarks);
                    Log.d("Firestore", "Image Resource: " + productImage);

                    startActivityForResult(intent, 1); // Start editEntry with expected result
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("FirestoreError", "Error fetching food details: " + e.getMessage());
                    startActivityForResult(intent, 1); // Start even if Firestore data fails
                }
            });
        } else {
            Log.e("goToEditEntry", "FID is null, proceeding without Firestore data.");
            startActivityForResult(intent, 1); // Start immediately if no Firestore lookup is needed
        }
    }

    /*NAVIGATIONS*/
    public void goHome(View view) {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
    }

    public void goFood(View view) {
        Intent intent = new Intent(this, foodManagement.class);
        startActivity(intent);
    }

    public void goCommunity(View view) {
        Intent intent = new Intent(this, community.class);
        startActivity(intent);
    }

    public void goProfile(View view) {
        Intent intent = new Intent(this, profile.class);
        startActivity(intent);
    }

    public void goToInputEntry(View view) {
        Intent intent = new Intent(this, inputEntry.class);
        startActivity(intent);
    }

    public void goToSort(View view) {
        Intent intent = new Intent(this, Sort.class);
        startActivity(intent);
    }
}
