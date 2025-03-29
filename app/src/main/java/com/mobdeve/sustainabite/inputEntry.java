package com.mobdeve.sustainabite;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class inputEntry extends AppCompatActivity {

    private EditText quantityInput, editTextDOE, editTextDOI, addFoodName, quantityType, storage, remarks;
    private Button btnAddFood;

    private DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_entry);

        dbManager = new DBManager();

        // Initialize the EditText for DOE & DOI
        editTextDOE = findViewById(R.id.editTextDOE);
        editTextDOI = findViewById(R.id.editTextDOI);
        addFoodName = findViewById(R.id.editTextFoodname);
        remarks = findViewById(R.id.editTextUsername);
        quantityInput = findViewById(R.id.quantityInput);



        // Create and apply the date handler
        DOE_date_handler doeDateHandler = new DOE_date_handler(this, editTextDOE);
        DOI_date_handler doiDateHandler = new DOI_date_handler(this, editTextDOI);

        // Find the button by its ID
        Button btnAddEntry = findViewById(R.id.btnAddEntry);

        // Set an OnClickListener to go back to the previous activity
        btnAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes this activity and returns to the previous one
            }
        });

        // Button that will add the data to firebase when clicked.
        Button btnAddFood = findViewById(R.id.button2);
        btnAddFood.setOnClickListener(v -> addFood());
    }

    //this one adds food to firestore: calls addFoodToFirestore from DBManager
    private void addFood(){
        String FDOI = editTextDOI.getText().toString().trim();
        String FDOE = editTextDOE.getText().toString().trim();
        String FNAME = addFoodName.getText().toString().trim();
        Integer FQuantity = 0;
        try{
            FQuantity = Integer.parseInt(quantityInput.getText().toString().trim());
        }catch(NumberFormatException e){
            Log.e("InputEntry", "Invalid number input for quantity", e);
        }
        String FQuanType = "";
        String FSTORAGE = "";

        // Get fragments by their ID
        QuantityEntryFragment quantityFragment = (QuantityEntryFragment) getSupportFragmentManager().findFragmentById(R.id.quantityEntryFragment);
        InputEntryFragment storageFragment = (InputEntryFragment) getSupportFragmentManager().findFragmentById(R.id.inputEntryFragment);

        if (quantityFragment != null && storageFragment != null) {
            FQuanType = quantityFragment.getSelectedQuantity();
            FSTORAGE = storageFragment.getSelectedStorage();

            Log.d("InputEntry", "Quantity: " + FQuanType);
            Log.d("InputEntry", "Storage: " + FSTORAGE);
        }

        String FRemarks = remarks.getText().toString().trim();

        dbManager.addFoodToFirestore(FNAME, FDOI, FDOE, FQuantity, FQuanType, FSTORAGE, FRemarks);

    }
}
