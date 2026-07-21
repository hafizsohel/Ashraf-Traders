package com.example.ashraftraders.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ashraftraders.data.model.ProductModel;
import com.example.ashraftraders.databinding.ItemProductRowBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<ProductModel> productList = new ArrayList<>();
    // এই মেথডটি নতুন ডাটা পাওয়ার সাথে সাথে লিস্ট রিফ্রেশ করবে
    public void updateData(List<ProductModel> newProducts) {
        this.productList.clear();
        this.productList.addAll(newProducts);
        notifyDataSetChanged(); // এটি রিয়েল-টাইম ভিউ আপডেট করতে বাধ্য করবে
    }

    public void setProducts(List<ProductModel> list) {
        productList.clear();

        if (list != null) {
            productList.addAll(list);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType) {

        ItemProductRowBinding binding = ItemProductRowBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder,
                                 int position) {

        ProductModel product = productList.get(position);

        holder.binding.tvProductCode.setText(String.valueOf(product.getProduct_code()));

        holder.binding.tvProductName.setText(product.getProductName());

        holder.binding.tvBrandName.setText(product.getBrandName());

        holder.binding.tvPrice.setText(
                "৳ " + String.format("%,.0f",
                        product.getPurchasePrice()));

        holder.binding.tvStock.setText(
                String.valueOf(product.getStock()));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        ItemProductRowBinding binding;

        public ProductViewHolder(ItemProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}