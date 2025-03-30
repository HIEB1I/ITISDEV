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


        //Retrieve all the data

        String productName = getIntent().getStringExtra("productName");
        String productQty_Val= getIntent().getStringExtra("productQty_Val");
        String productQty_Type = getIntent().getStringExtra("productQty_Type");
        String productDOI = getIntent().getStringExtra("productDOI");
        String productDOE = getIntent().getStringExtra("productDOE");
        String productStorage = getIntent().getStringExtra("productStorage");
        String productRemarks = getIntent().getStringExtra("productRemarks");

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
    }
}
