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



        //Log.d("Filter", "Filters received: " + filterName + filterDOI +filterDOE);

        // Retrieve Data from Intent
        String productName = getIntent().getStringExtra("productName");
        String FID = getIntent().getStringExtra("foodId"); //Puts a FoodID on the intent
        String productQty_Val= getIntent().getStringExtra("productQty_Val");
        String productQty_Type = getIntent().getStringExtra("productQty_Type");
        String productDOI = getIntent().getStringExtra("productDOI");
        String productDOE = getIntent().getStringExtra("productDOE");

        String productImageBase64 = getIntent().getStringExtra("productImage");
        Log.d("FirestoreID", "The product that you selected has an ID of: " + FID); //Check if this is the correct FoodID




        // Bind Data to Views
        ((TextView) findViewById(R.id.productName)).setText(productName);
        ((TextView) findViewById(R.id.productQty_Val)).setText(productQty_Val);
        ((TextView) findViewById(R.id.productQty_Type)).setText(productQty_Type);
        ((TextView) findViewById(R.id.productDOI)).setText(DBManager.convertDate(productDOI));
        ((TextView) findViewById(R.id.productDOE)).setText(DBManager.convertDate(productDOE));
        //((TextView) findViewById(R.id.productStorage)).setText(productStorage);
        //((TextView) findViewById(R.id.productRemarks)).setText(productRemarks);

        Bitmap bitmap = DBManager.decodeBase64ToBitmap(productImageBase64);
            if (bitmap != null){
                ((ImageView)  findViewById(R.id.productImage)).setImageBitmap(bitmap);
            }else{
                ((ImageView) findViewById(R.id.productImage)).setImageResource(R.drawable.noimage);

            }

        //((ImageView) findViewById(R.id.productImage)).setImageResource(productImage);



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
                        // goToEditEntry();
                    }
                });
    }


// this allows the user to go to the edit page and also sends data over to there
  /*  public void goToEditEntry(){
        Intent intent = new Intent(this, editEntry.class);
        intent.putExtra("foodId", getIntent().getStringExtra("foodId"));
        intent.putExtra("productName", getIntent().getStringExtra("productName"));
        intent.putExtra("productQty_Val", getIntent().getStringExtra("productQty_Val"));
        intent.putExtra("productQty_Type", getIntent().getStringExtra("productQty_Type"));
        intent.putExtra("productDOI", getIntent().getStringExtra("productDOI"));
        intent.putExtra("productDOE", getIntent().getStringExtra("productDOE"));
        intent.putExtra("productImage", getIntent().getStringExtra("productImage"));


        String FID = getIntent().getStringExtra("foodId");

        //specifically for storage and remarks
        dbManager.fetchSpecificFood(FID, new DBManager.CheckFoodIDValidity(){
            @Override

            public void onSuccess(String storage, String remarks ,String productImage){
                intent.putExtra("productStorage", storage);
                intent.putExtra("productRemarks", remarks);
                intent.putExtra("productImage", productImage);
                Log.d("Firestore", "Storage: " + storage);
                Log.d("Firestore", "Remarks: " + remarks);
                Log.d("Firestore", "Image Resource: " + productImage);
                startActivityForResult(intent,1); // start the activity, with a result expected
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("FirestoreError", "Error fetching food details: " + e.getMessage());
                startActivityForResult(intent, 1); // Start even if Firestore data fails

            }


        });

    };
    //this method will refresh the data to ensure that the latest details are there
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data!=null){
            //Obtain the new values

            String updatedProductName = data.getStringExtra("updatedFoodName");
            String updatedProductDOI = data.getStringExtra("updatedFoodDOI");
            String updatedProductDOE = data.getStringExtra("updatedFoodDOE");
            int updatedProductQuantity = data.getIntExtra("updatedFoodQuantity", 0);
            String updatedQuantityType = data.getStringExtra("updatedFoodQuantityType");
            String updatedProductImage = data.getStringExtra("updatedFoodImage");


            // Bind Updated Data to Views
            ((TextView) findViewById(R.id.productName)).setText(updatedProductName);
            ((TextView) findViewById(R.id.productQty_Val)).setText(String.valueOf(updatedProductQuantity));
            ((TextView) findViewById(R.id.productQty_Type)).setText(updatedQuantityType);
            ((TextView) findViewById(R.id.productDOI)).setText(DBManager.convertDate(updatedProductDOI));
            ((TextView) findViewById(R.id.productDOE)).setText(DBManager.convertDate(updatedProductDOE));


            ImageView productImageView = findViewById(R.id.productImage);

            //put updated product image if it's not null
            if (updatedProductImage!= null && !updatedProductImage.isEmpty()){
                Bitmap bitmap = DBManager.decodeBase64ToBitmap(updatedProductImage);
                if (bitmap != null){
                    productImageView.setImageBitmap(bitmap);
                }else{
                    productImageView.setImageResource(R.drawable.noimage);
                }
            }
            Log.d("ProductDetailsActivity", "Updated Name: " + updatedProductName);
        }
        setResult(RESULT_OK);
    }
*/

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
