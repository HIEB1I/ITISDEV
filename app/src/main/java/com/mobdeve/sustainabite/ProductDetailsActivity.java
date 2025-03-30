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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);


        // Retrieve Data from Intent
        String productName = getIntent().getStringExtra("productName");
        String FID = getIntent().getStringExtra("foodId"); //Puts a FoodID on the intent
        String productQty_Val= getIntent().getStringExtra("productQty_Val");
        String productQty_Type = getIntent().getStringExtra("productQty_Type");
        String productDOI = getIntent().getStringExtra("productDOI");
        String productDOE = getIntent().getStringExtra("productDOE");
        String productStorage = getIntent().getStringExtra("productStorage");
        String productRemarks = getIntent().getStringExtra("productRemarks");
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



    public void goToEditEntry(){
        Intent intent = new Intent(this, editEntry.class);

        startActivity(intent);
    };
}
