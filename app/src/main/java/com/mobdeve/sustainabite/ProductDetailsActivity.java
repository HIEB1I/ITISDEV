package com.mobdeve.sustainabite;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProductDetailsActivity extends AppCompatActivity {

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);


        dbManager = new DBManager();


        // Retrieve Data from Intent
        String productName = getIntent().getStringExtra("productName");
        String FID = getIntent().getStringExtra("foodId"); //Puts a FoodID on the intent
        String productQty_Val= getIntent().getStringExtra("productQty_Val");
        String productQty_Type = getIntent().getStringExtra("productQty_Type");
        String productDOI = getIntent().getStringExtra("productDOI");
        String productDOE = getIntent().getStringExtra("productDOE");

        int productImage = getIntent().getIntExtra("productImage", 0);
        Log.d("FirestoreID", "The product that you selected has an ID of: " + FID); //Check if this is the correct FoodID




        // Bind Data to Views
        ((TextView) findViewById(R.id.productName)).setText(productName);
        ((TextView) findViewById(R.id.productQty_Val)).setText(productQty_Val);
        ((TextView) findViewById(R.id.productQty_Type)).setText(productQty_Type);
        ((TextView) findViewById(R.id.productDOI)).setText(DBManager.convertDate(productDOI));
        ((TextView) findViewById(R.id.productDOE)).setText(DBManager.convertDate(productDOE));
        //((TextView) findViewById(R.id.productStorage)).setText(productStorage);
        //((TextView) findViewById(R.id.productRemarks)).setText(productRemarks);
        ((ImageView) findViewById(R.id.productImage)).setImageResource(productImage);



        // Find the button by its ID
        Button btnBack = findViewById(R.id.btnBack);

        // Set an OnClickListener to go back to the previous activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes this activity and returns to the previous one
            }
        });

        //Find Image button by ID
        ImageButton editButton = findViewById(R.id.btnEditEntry);

        //onClickListener for editButton
        editButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToEditEntry();
                    }
                });
    }


// this allows the user to go to the edit page and also sends data over to there
    public void goToEditEntry(){
        Intent intent = new Intent(this, editEntry.class);
        intent.putExtra("foodId", getIntent().getStringExtra("foodId"));
        intent.putExtra("productName", getIntent().getStringExtra("productName"));
        intent.putExtra("productQty_Val", getIntent().getStringExtra("productQty_Val"));
        intent.putExtra("productQty_Type", getIntent().getStringExtra("productQty_Type"));
        intent.putExtra("productDOI", getIntent().getStringExtra("productDOI"));
        intent.putExtra("productDOE", getIntent().getStringExtra("productDOE"));
        intent.putExtra("productImage", getIntent().getIntExtra("productImage", 0));


        String FID = getIntent().getStringExtra("foodId");

        //specifically for storage and remarks
        dbManager.fetchSpecificFood(FID, new DBManager.CheckFoodIDValidity(){
            @Override

            public void onSuccess(String storage, String remarks){
                intent.putExtra("productStorage", storage);
                intent.putExtra("productRemarks", remarks);
                Log.d("Firestore", "Storage: " + storage);
                Log.d("Firestore", "Remarks: " + remarks);
                startActivity(intent); // start the activity
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("FirestoreError", "Error fetching food details: " + e.getMessage());
                startActivity(intent); // Start even if Firestore data fails

            }
        });

    };
}
