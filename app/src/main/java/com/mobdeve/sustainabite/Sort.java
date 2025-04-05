package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class Sort extends AppCompatActivity {

    private EditText editTextFoodName, editTextDOI, editTextDOE;
    private ImageView deleteName, deleteDateInput, deleteDateExpiry;
    private Switch expiryTagSwitch;
    private Button showResultsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort);

        //uncomment evrything later
        // Initialize the EditTexts
        editTextFoodName = findViewById(R.id.editTextFoodname);
        editTextDOE = findViewById(R.id.editTextDOE);
        editTextDOI = findViewById(R.id.editTextDOI);

        //Initialize delete buttons
        deleteName = findViewById(R.id.deleteName);
        deleteDateInput = findViewById(R.id.deleteDateInput);
        deleteDateExpiry = findViewById(R.id.deleteDateExpiry);

        //Initialize expiry tag switch
        expiryTagSwitch = findViewById(R.id.expiryTagSwitch);

        //Initialize the button that starts the sort.
        showResultsButton = findViewById(R.id.button2);

        // Create and apply the date handler
        DOE_date_handler doeDateHandler = new DOE_date_handler(this, editTextDOE);
        DOI_date_handler doiDateHandler = new DOI_date_handler(this, editTextDOI);

        // Find the button by its ID
        Button btnAddEntry = findViewById(R.id.btnSort);

        // Set an OnClickListener to go back to the previous activity
        btnAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes this activity and returns to the previous one
            }
        });
        //Hide all the delete buttons
        deleteName.setVisibility(View.GONE);
        deleteDateInput.setVisibility(View.GONE);
        deleteDateExpiry.setVisibility(View.GONE);

        //text watchers
        setupTextWatcher(editTextFoodName, deleteName, editTextDOE, editTextDOI);
        setupTextWatcher(editTextDOE, deleteDateExpiry, editTextFoodName, editTextDOI);
        setupTextWatcher(editTextDOI, deleteDateInput, editTextDOE, editTextFoodName);

        // Set delete button click listeners
        deleteName.setOnClickListener(v -> editTextFoodName.setText(""));
        deleteDateInput.setOnClickListener(v -> editTextDOI.setText(""));
        deleteDateExpiry.setOnClickListener(v -> editTextDOE.setText(""));

        //Results for the result button.
        showResultsButton.setOnClickListener(v -> {
            String filterName = editTextFoodName.getText().toString().trim();
            String filterDOI = editTextDOI.getText().toString().trim();
            String filterDOE = editTextDOE.getText().toString().trim();

            Intent intent = new Intent();
            intent.putExtra("filterName", filterName);
            intent.putExtra("filterDOI", filterDOI); // Add DOI filter to intent
            intent.putExtra("filterDOE", filterDOE); // Add DOE filter to intent
            Log.d("SortFilter", "Sending filterName: " + filterName); // Log filterName
            Log.d("SortFilter", "Sending filterDOI: " + filterDOI); // Log DOI
            Log.d("SortFilter", "Sending filterDOE: " + filterDOE); // Log DOE

            setResult(RESULT_OK, intent);
            finish();
        });

    }


//set up a textWatcher method for the edit texts and the delete buttons. this also disables others
    private void setupTextWatcher(EditText editText, ImageView deleteButton, EditText... others){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    deleteButton.setVisibility(View.VISIBLE);
                    disableOtherEditTexts(editText, others);

                }else{
                    deleteButton.setVisibility(View.GONE);
                    enableAllEditTexts();
                }
            }
        });
    }

    // Disable all EditTexts except the one currently being edited
    private void disableOtherEditTexts(EditText active, EditText... others) {
        for (EditText editText : others) {
            editText.setEnabled(false);
            editText.setBackgroundColor(getResources().getColor(R.color.gray, null)); // change background
            editText.setAlpha(0.5f); // Reduce opacity
        }
    }

    // Re-enable all EditTexts when a field is cleared
    private void enableAllEditTexts() {
        editTextFoodName.setEnabled(true);
        editTextDOI.setEnabled(true);
        editTextDOE.setEnabled(true);

        // restore original hints
        editTextDOI.setHint("mm/dd/yyyy");
        editTextDOE.setHint("mm/dd/yyyy");

        // restore white textbox background
        editTextFoodName.setBackgroundResource(R.drawable.white_textbox);
        editTextDOI.setBackgroundResource(R.drawable.white_textbox);
        editTextDOE.setBackgroundResource(R.drawable.white_textbox);

        // set text color to black
        editTextFoodName.setTextColor(getResources().getColor(R.color.black));
        editTextDOI.setTextColor(getResources().getColor(R.color.black));
        editTextDOE.setTextColor(getResources().getColor(R.color.black));

        // set hint color to gray
        editTextDOI.setHintTextColor(getResources().getColor(R.color.gray));
        editTextDOE.setHintTextColor(getResources().getColor(R.color.gray));
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

}
