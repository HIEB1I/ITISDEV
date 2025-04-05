package com.mobdeve.sustainabite;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private String foodId;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
        this.fullProductList = new ArrayList<>(productList); //backup in case something happens to old productList
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

        holder.itemName.setText(product.getName());
        holder.itemQty_Val.setText(String.valueOf(product.getQty_Val())); // Since this is an integer, have to modify this code.
        holder.itemQty_Type.setText(product.getQty_Type());
        holder.itemDOI.setText(DBManager.convertDate(product.getDOI()));
        holder.itemDOE.setText(DBManager.convertDate(product.getDOE()));
        //holder.itemStorage.setText(product.getStorage());
        //holder.itemRemarks.setText(product.getRemarks());

        String imageString = product.getImageString();
        if (imageString!= null && !imageString.isEmpty()){
            Bitmap bitmap = DBManager.decodeBase64ToBitmap(imageString);
            if (bitmap != null){
                holder.itemImage.setImageBitmap(bitmap);
            }else{
                holder.itemImage.setImageResource(R.drawable.banana);
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
            //intent.putExtra("productStorage", product.getStorage());
            //intent.putExtra("productRemarks", product.getRemarks());
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
        View itemFrame; // Added itemFrame

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
        }
    }

    // RESETS THE LIST
    public void resetList() {
       productList = new ArrayList<>(fullProductList);
       notifyDataSetChanged();
    }

    // FILTERS FORT SORT.JAVA

        public void sortByName(String filter, List<Product> fullProducts) {
            List<Product> filteredList = new ArrayList<>();

            Log.d("SortFilter", "Filter: " + filter);
            Log.d("SortFilter", "Full product list size: " + fullProducts.size());

            for (Product product : fullProducts) {
                Log.d("SortFilter", "Comparing: " + product.getName() + " with filter: " + filter);

                if (product.getName().toLowerCase().trim().startsWith(filter.toLowerCase().trim())) {
                    filteredList.add(product);
                }
            }
            //  Replace productList reference entirely (don’t just modify contents)
            this.productList = new ArrayList<>(filteredList);

            // Update the product list in the adapter
            productList.clear();
            productList.addAll(filteredList);
            notifyDataSetChanged();
            Log.d("SortFilter", "Filtered list size: " + filteredList.size());
        }

        public void sortByDOI(String filterDOI, List<Product> fullProducts){
            List<Product> filteredList = new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy", Locale.getDefault()); // Date format

            Log.d("SortFilter", "DOI Filter: " + filterDOI);
            Log.d("SortFilter", "Full product list size: " + fullProducts.size());

            for (Product product : fullProducts) {
                Log.d("SortFilter", "Comparing DOI: " + product.getDOI() + " with filter: " + filterDOI);

                try {
                    // Parse the product's DOI and the user's input DOI
                    Date productDOI = dateFormat.parse(product.getDOI());
                    Date inputtedDOI = dateFormat.parse(filterDOI);

                    // If the dates match, add the product to the filtered list
                    if (productDOI != null && inputtedDOI != null && productDOI.equals(inputtedDOI)) {
                        filteredList.add(product);
                    }
                } catch (Exception e) {
                    Log.e("SortFilter", "Error parsing DOI", e);
                }
            }

            // Replace productList reference entirely (don’t just modify contents)
            this.productList = new ArrayList<>(filteredList);

            // Update the product list in the adapter
            productList.clear();
            productList.addAll(filteredList);
            notifyDataSetChanged();
            Log.d("SortFilter", "Filtered list size: " + filteredList.size());
        }

        public void sortByDOE(String filterDOE, List<Product> fullProducts){
            List<Product> filteredList = new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy", Locale.getDefault()); // Date format

            Log.d("SortFilter", "DOE Filter: " + filterDOE);
            Log.d("SortFilter", "Full product list size: " + fullProducts.size());

            for (Product product : fullProducts) {
                Log.d("SortFilter", "Comparing DOE: " + product.getDOI() + " with filter: " + filterDOE);

                try {
                    // Parse the product's DOE and the user's input DOE
                    Date productDOE = dateFormat.parse(product.getDOE());
                    Date inputtedDOE = dateFormat.parse(filterDOE);

                    // If the dates match, add the product to the filtered list
                    if (productDOE != null && inputtedDOE != null && productDOE.equals(inputtedDOE)) {
                        filteredList.add(product);
                    }
                } catch (Exception e) {
                    Log.e("SortFilter", "Error parsing DOE", e);
                }
            }

            // Replace productList reference entirely (don’t just modify contents)
            this.productList = new ArrayList<>(filteredList);

            // Update the product list in the adapter
            productList.clear();
            productList.addAll(filteredList);
            notifyDataSetChanged();
            Log.d("SortFilter", "Filtered list size: " + filteredList.size());
        }
    }

