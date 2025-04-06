package com.mobdeve.sustainabite;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private List<Product> fullProductList; //backup in case something happens to old productList
    private List<Product> filteredProducts;
    private String foodId;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
        this.fullProductList = new ArrayList<>(productList); //backup in case something happens to old productList
        this.filteredProducts = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = productList.get(position);

        Log.d("FirestoreID", "Product ID: " + product.getFid());

        // Add null or empty check before calling convertDate
        String itemDOI = product.getDOI();
        String itemDOE = product.getDOE();

        holder.itemName.setText(product.getName());
        holder.itemQty_Val.setText(String.valueOf(product.getQty_Val())); // Since this is an integer, have to modify this code.
        holder.itemQty_Type.setText(product.getQty_Type());
        holder.itemDOI.setText(itemDOI != null && !itemDOI.isEmpty() ? DBManager.convertDate(itemDOI) : "Invalid Date");
        holder.itemDOE.setText(itemDOE != null && !itemDOE.isEmpty() ? DBManager.convertDate(itemDOE) : "Invalid Date");
        //holder.itemStorage.setText(product.getStorage());
        //holder.itemRemarks.setText(product.getRemarks());


        if (isExpired(itemDOE)) {
            holder.expiredLayout.setVisibility(View.VISIBLE);  // Show "Expired" text
            Log.d("ProductAdapter", "Item is expired: " + product.getName());
        } else {
            holder.expiredLayout.setVisibility(View.INVISIBLE);  // Hide "Expired" text
            Log.d("ProductAdapter", "Item is not expired: " + product.getName());
        }

        String imageString = product.getImageString();
        if (imageString!= null && !imageString.isEmpty()){
            Bitmap bitmap = DBManager.decodeBase64ToBitmap(imageString);
            if (bitmap != null){
                holder.itemImage.setImageBitmap(bitmap);
            }else{
                holder.itemImage.setImageResource(R.drawable.noimage);
            }
        }
        // Navigate to Product Details on Click
        holder.itemFrame.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);
            intent.putExtra("foodId", product.getFid());
            intent.putExtra("productName", product.getName());
            intent.putExtra("productQty_Val", String.valueOf(product.getQty_Val()));
            intent.putExtra("productQty_Type", product.getQty_Type());
            intent.putExtra("productDOI", product.getDOI());
            intent.putExtra("productDOE", product.getDOE());
            intent.putExtra("productStorage", product.getStorage());
            intent.putExtra("productRemarks", product.getRemarks());
            intent.putExtra("productImage", product.getImageString());
            view.getContext().startActivity(intent);
        });


    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemQty_Val, itemQty_Type, itemDOI, itemDOE;
        ImageView itemImage;
        View itemFrame;
        LinearLayout expiredLayout;

        public ProductViewHolder(View itemView) {
            super(itemView);
            itemFrame = itemView.findViewById(R.id.itemFrame); // Reference to FrameLayout
            itemName = itemView.findViewById(R.id.itemName);
            itemQty_Val = itemView.findViewById(R.id.itemQty_Val);
            itemQty_Type = itemView.findViewById(R.id.itemQty_Type);
            itemDOI = itemView.findViewById(R.id.itemDOI);
            itemDOE = itemView.findViewById(R.id.itemDOE);
            //itemStorage = itemView.findViewById(R.id.itemStorage);
            //itemRemarks = itemView.findViewById(R.id.itemRemarks);
            itemImage = itemView.findViewById(R.id.itemImage);
            // Initialize the "Expired" overlay layout
            expiredLayout = itemView.findViewById(R.id.LinearLayout_textExpired);
        }
    }

    // Helper function to check if the item is expired
    private boolean isExpired(String expiryDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault()); // Format of the date in the TextView
        try {
            // Parse the expiry date string into a Date object
            Date expiryDate = sdf.parse(expiryDateString);

            // Get the current date
            Date currentDate = new Date();

            // Log the current date and expiry date for debugging
            Log.d("ExpiryCheck", "Current Date: " + sdf.format(currentDate) + " | Expiry Date: " + expiryDateString);

            // Compare expiry date with current date
            boolean isExpired = expiryDate != null && expiryDate.before(currentDate); // If the expiry date is before today's date, it is expired

            // Log whether the item is expired
            Log.d("ExpiryCheck", "Is Expired: " + isExpired);

            return isExpired;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ExpiryCheck", "Error parsing date: " + expiryDateString);
            return false; // Default to not expired if date parsing fails
        }
    }
}

