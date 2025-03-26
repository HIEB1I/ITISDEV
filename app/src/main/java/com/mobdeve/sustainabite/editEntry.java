package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class editEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_entry);

        // Initialize the EditText for DOE & DOI
        EditText editTextDOE = findViewById(R.id.editTextDOE);
        EditText editTextDOI = findViewById(R.id.editTextDOI);

        // Create and apply the date handler
        DOE_date_handler doeDateHandler = new DOE_date_handler(this, editTextDOE);
        DOI_date_handler doiDateHandler = new DOI_date_handler(this, editTextDOI);

        // Find the button by its ID
        Button btnEditEntry = findViewById(R.id.btnEditEntry);

        // Set an OnClickListener to go back to the previous activity
        btnEditEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes this activity and returns to the previous one
            }
        });
    }
}
