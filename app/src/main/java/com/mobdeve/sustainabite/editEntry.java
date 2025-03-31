package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class editEntry extends AppCompatActivity {
    private EditText addFoodName, remarks, quantityInput, editTextDOE, editTextDOI;
    private String productName, productQty_Val, productQty_Type,productDOI, productDOE, productStorage, productRemarks, foodId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_entry);

        // Initialize the EditTexts
        addFoodName = findViewById(R.id.editTextFoodname);
        remarks = findViewById(R.id.editTextUsername);
        quantityInput = findViewById(R.id.quantityInput);
        editTextDOE = findViewById(R.id.editTextDOE);
        editTextDOI = findViewById(R.id.editTextDOI);

        //not allowed to edit DOI.
        editTextDOI.setEnabled(false);

        // Get fragments by their ID
        QuantityEntryFragment quantityFragment = (QuantityEntryFragment) getSupportFragmentManager().findFragmentById(R.id.quantityEntryFragment);
        InputEntryFragment storageFragment = (InputEntryFragment) getSupportFragmentManager().findFragmentById(R.id.inputEntryFragment);

        // Create and apply the date handler
        DOE_date_handler doeDateHandler = new DOE_date_handler(this, editTextDOE);
        DOI_date_handler doiDateHandler = new DOI_date_handler(this, editTextDOI);

        // Find the button by its ID
        Button btnEditEntry = findViewById(R.id.btnEditEntry);

        //Find the ID of the button that submits
        Button submitBtn = findViewById(R.id.button2);

        //Retrieve all the data

        productName = getIntent().getStringExtra("productName");
        productQty_Val= getIntent().getStringExtra("productQty_Val");
        productQty_Type = getIntent().getStringExtra("productQty_Type");
        productDOI = getIntent().getStringExtra("productDOI");
        productDOE = getIntent().getStringExtra("productDOE");
        productStorage = getIntent().getStringExtra("productStorage");
        productRemarks = getIntent().getStringExtra("productRemarks");
        foodId = getIntent().getStringExtra("foodId");

        addFoodName.setText(productName);
        editTextDOI.setText(productDOI);
        editTextDOE.setText(productDOE);
        quantityInput.setText(productQty_Val);
        quantityFragment.setSelectedQuantity(productQty_Type);
        storageFragment.setSelectedStorage(productStorage);
        remarks.setText(productRemarks);


        // Set an OnClickListener to go back to the previous activity
        btnEditEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes this activity and returns to the previous one
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFood();
            }
        });


    }
    private void updateFood(){
        //check if foodId exists
        if (foodId == null || foodId.isEmpty()){
            Log.e("DBManager", "Invalid Food ID: Cannot update this one.");
            return;
        }

        String updatedFNAME = addFoodName.getText().toString().trim();
        String updatedFDOE = editTextDOI.getText().toString().trim();
        String updatedFDOI = editTextDOE.getText().toString().trim();
        Integer updatedFQuantity = 0;

        try {
            updatedFQuantity = Integer.parseInt(quantityInput.getText().toString().trim());
        }catch(Exception e){
            Log.e("EditEntry", "Invalid number input for quantity", e);
        }

        String updatedFQuanType = "";
        String updatedFSTORAGE = "";

        // Get fragments by their ID
        QuantityEntryFragment quantityFragment = (QuantityEntryFragment) getSupportFragmentManager().findFragmentById(R.id.quantityEntryFragment);
        InputEntryFragment storageFragment = (InputEntryFragment) getSupportFragmentManager().findFragmentById(R.id.inputEntryFragment);

        if (quantityFragment != null && storageFragment != null) {
            updatedFQuanType = quantityFragment.getSelectedQuantity();
            updatedFSTORAGE = storageFragment.getSelectedStorage();
        }

        String updatedFRemarks = remarks.getText().toString().trim();

        DBManager dbManager = new DBManager();

        Integer finalUpdatedFQuantity = updatedFQuantity;
        String finalUpdatedFQuanType = updatedFQuanType;
        String finalUpdatedFSTORAGE = updatedFSTORAGE;

        dbManager.updateFoodInFirestore(this, foodId, updatedFNAME, updatedFDOI, updatedFDOE,
                finalUpdatedFQuantity, finalUpdatedFQuanType, finalUpdatedFSTORAGE, updatedFRemarks, new DBManager.OnFoodUpdatedListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("EditEntry", "Food has been updated.");
                        // Send data back to the previous activity (if needed)
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedFoodName", updatedFNAME);
                        resultIntent.putExtra("updatedFoodDOE", updatedFDOE);
                        resultIntent.putExtra("updatedFoodDOI", updatedFDOI);
                        resultIntent.putExtra("updatedFoodQuantity", finalUpdatedFQuantity);
                        resultIntent.putExtra("updatedFoodQuantityType", finalUpdatedFQuanType);
                        resultIntent.putExtra("updatedFoodStorage", finalUpdatedFSTORAGE);
                        resultIntent.putExtra("updatedFoodRemarks", updatedFRemarks);
                        resultIntent.putExtra("updated", true); // notify update
                        setResult(RESULT_OK, resultIntent);

                        // Finish this activity and go back
                        finish();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("EditEntry", "Error in updating food..");
                    }
                });
    }
}
