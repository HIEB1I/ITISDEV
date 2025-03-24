package com.mobdeve.sustainabite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.content.Intent;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
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
        holder.itemName.setText(product.getName());
        holder.itemQty_Val.setText(product.getQty_Val());
        holder.itemQty_Type.setText(product.getQty_Type());
        holder.itemDOI.setText(product.getDOI());
        holder.itemDOE.setText(product.getDOE());
        //holder.itemStorage.setText(product.getStorage());
        //holder.itemRemarks.setText(product.getRemarks());
        holder.itemImage.setImageResource(product.getImageResource());

        // Navigate to Product Details on Click
        holder.itemFrame.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);
            intent.putExtra("productName", product.getName());
            intent.putExtra("productQty_Val", product.getQty_Val());
            intent.putExtra("productQty_Type", product.getQty_Type());
            intent.putExtra("productDOI", product.getDOI());
            intent.putExtra("productDOE", product.getDOE());
            intent.putExtra("productStorage", product.getStorage());
            intent.putExtra("productRemarks", product.getRemarks());
            intent.putExtra("productImage", product.getImageResource());
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

}
