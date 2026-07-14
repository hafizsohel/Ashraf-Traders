package com.example.ashraftraders.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ashraftraders.data.model.ProductModel;
import com.example.ashraftraders.databinding.ItemProductBinding;

import java.util.ArrayList;
import java.util.List;

public class SaleProductAdapter extends RecyclerView.Adapter<SaleProductAdapter.ViewHolder> {

    private final List<ProductModel> productList = new ArrayList<>();
    private final OnAddClickListener listener;

    public interface OnAddClickListener {
        void onAddClick(ProductModel product);
    }

    public SaleProductAdapter(OnAddClickListener listener) {
        this.listener = listener;
    }

    public void setProducts(List<ProductModel> products) {
        productList.clear();

        if (products != null) {
            productList.addAll(products);
        }

        notifyDataSetChanged();
    }

    public void updateData(List<ProductModel> products) {
        setProducts(products);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductModel product = productList.get(position);

        holder.binding.txtProductName.setText(product.getProductName());

        holder.binding.txtStock.setText("স্টক: " + product.getStock());

        holder.binding.txtPrice.setText(
                "৳ " + String.format("%,.0f", product.getPurchasePrice())
        );

        // যদি Image URL থাকে
        /*
        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_box)
                .error(R.drawable.ic_box)
                .into(holder.binding.imgProduct);
        */

        holder.binding.btnAdd.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ItemProductBinding binding;

        public ViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}