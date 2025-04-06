package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;


public class editEntry extends AppCompatActivity {
    private EditText addFoodName, remarks, quantityInput, editTextDOE, editTextDOI;
    private String productName, productQty_Val, productQty_Type,productDOI, productDOE, productStorage, productRemarks, productImageResource, foodId;
    private ImageView foodImageView;
    private ProductAdapter productAdapter;
    private Bitmap selectedBitmap;
    private static final int PICK_IMAGE_REQUEST = 1;

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

        foodImageView = findViewById(R.id.ic_gallery);

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
/*
        productName = getIntent().getStringExtra("productName");
        productQty_Val= getIntent().getStringExtra("productQty_Val");
        productQty_Type = getIntent().getStringExtra("productQty_Type");
        productDOI = getIntent().getStringExtra("productDOI");
        productDOE = getIntent().getStringExtra("productDOE");
        productStorage = getIntent().getStringExtra("productStorage");
        productRemarks = getIntent().getStringExtra("productRemarks");
        foodId = getIntent().getStringExtra("foodId");
        productImageResource = getIntent().getStringExtra("productImage");

        Log.d("EditEntry", "Image Resource: " + productImageResource);

        addFoodName.setText(productName);
        editTextDOI.setText(productDOI);
        editTextDOE.setText(productDOE);
        quantityInput.setText(productQty_Val);
        quantityFragment.setSelectedQuantity(productQty_Type);
        storageFragment.setSelectedStorage(productStorage);
        remarks.setText(productRemarks);

        //Load image
        if (productImageResource!= null && !productImageResource.isEmpty()){
            Bitmap bitmap = DBManager.decodeBase64ToBitmap(productImageResource);
            if (bitmap != null){
                foodImageView.setImageBitmap(bitmap);
            }else{
                foodImageView.setImageResource(R.drawable.banana);
            }
        }

 */
        foodId = getIntent().getStringExtra("foodId");
        Log.d("editEntry", "FoodId: " + foodId);

        if (foodId != null && !foodId.isEmpty()) {
            // Fetch food details from Firestore
            DBManager dbManager = new DBManager();
            dbManager.fetchFoodDetailsFromFirestore(this, foodId, new DBManager.OnFoodDetailsFetchedListener() {
                @Override
                public void onSuccess(Product product) {
                    // Set the fields with the fetched data
                    addFoodName.setText(product.getName());
                    editTextDOE.setText(product.getDOE());
                    editTextDOI.setText(product.getDOI());
                    quantityInput.setText(String.valueOf(product.getQty_Val()));
                    quantityFragment.setSelectedQuantity(product.getQty_Type());
                    storageFragment.setSelectedStorage(product.getStorage());
                    remarks.setText(product.getRemarks());

                    // Set image (if any)
                    if (product.getImageString() != null && !product.getImageString().isEmpty()) {
                        Bitmap bitmap = DBManager.decodeBase64ToBitmap(product.getImageString());
                        if (bitmap != null) {
                            foodImageView.setImageBitmap(bitmap);
                        } else {
                            foodImageView.setImageResource(R.drawable.banana);
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.e("editEntry", "Error fetching food details", e);
                }
            });
        }


        foodImageView.getLayoutParams().width = 300;
        foodImageView.getLayoutParams().height = 300;


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

        // Pressing image leads you to gallery
        foodImageView.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v){
               openGallery();
           }
        });


    }
//OPENS THE GALLERY
    private void openGallery(){
        Log.d("InputEntry", "Gallery button clicked!"); // Debugging
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //Code for adding the image to the box
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&  data.getData() != null){
                Uri imageUri = data.getData();
                try {
                    selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    foodImageView.setImageBitmap(selectedBitmap);
                    foodImageView.getLayoutParams().width = 300;
                    foodImageView.getLayoutParams().height = 300;
                    foodImageView.requestLayout();

                    Log.d("editEntry", "Image selected and displayed");

                } catch (IOException e) {
                    Log.e("editEntry", "Error loading image", e);
                }
        } else {
            Log.d("editEntry", "No image selected or result canceled");
        }
    }

    private void updateFood(){
        //check if foodId exists
        if (foodId == null || foodId.isEmpty()){
            Log.e("DBManager", "Invalid Food ID: Cannot update this one.");
            return;
        }

        String updatedFNAME = addFoodName.getText().toString().trim();
        String updatedFDOI = editTextDOI.getText().toString().trim();
        String updatedFDOE = editTextDOE.getText().toString().trim();
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
        String updatedFImage = (selectedBitmap != null) ? bitmapToBase64(selectedBitmap) : null;

        Product updatedProduct = new Product(
                updatedFNAME,
                getIntent().getStringExtra("foodId"),  // Assuming this ID is passed from ProductDetailsActivity
                updatedFQuantity,
                updatedFQuanType,
                updatedFDOI,
                updatedFDOE,
                updatedFSTORAGE,
                updatedFRemarks,
                updatedFImage
        );

        dbManager.updateFoodInFirestore(this, foodId, updatedFNAME, updatedFDOI, updatedFDOE,
                finalUpdatedFQuantity, finalUpdatedFQuanType, finalUpdatedFSTORAGE, updatedFRemarks, updatedFImage, new DBManager.OnFoodUpdatedListener() {
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
                        resultIntent.putExtra("updatedFoodImage", updatedFImage);
                        resultIntent.putExtra("updatedProduct", updatedProduct);
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
    // Convert Bitmap to Base64 String
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
