package com.example.ashraftraders.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ashraftraders.data.room.entity.CartEntity;
import com.example.ashraftraders.databinding.ItemCartBinding;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    public interface CartListener {
        void onIncrease(CartEntity item);
        void onDecrease(CartEntity item);
        void onDelete(CartEntity item);
    }

    private final List<CartEntity> cartList = new ArrayList<>();
    private final CartListener listener;

    public CartAdapter(CartListener listener) {
        this.listener = listener;
    }

    public void setCartItems(List<CartEntity> items) {
        cartList.clear();

        if (items != null) {
            cartList.addAll(items);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemCartBinding binding = ItemCartBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CartEntity item = cartList.get(position);

        holder.binding.txtName.setText(item.getProductName());

        holder.binding.txtPrice.setText(
                "৳ " + String.format("%,.2f", item.getPrice())
        );

        holder.binding.txtQty.setText(
                String.valueOf(item.getQuantity())
        );

        double subTotal = item.getPrice() * item.getQuantity();

        holder.binding.txtTotal.setText(
                "৳ " + String.format("%,.2f", subTotal)
        );

       /* holder.binding.btnPlus.setOnClickListener(v -> {

            if (item.getQuantity() >= item.getStock()) {

                Toast.makeText(
                        holder.itemView.getContext(),
                        "স্টকে আর পণ্য নেই",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            listener.onIncrease(item);

        });*/
        holder.binding.btnPlus.setOnClickListener(v -> {
            listener.onIncrease(item);
        });

        holder.binding.btnMinus.setOnClickListener(v ->
                listener.onDecrease(item));

        holder.binding.btnDelete.setOnClickListener(v ->
                listener.onDelete(item));

        // Glide (যদি ImageUrl থাকে)
        /*
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_box)
                .into(holder.binding.imgProduct);
        */
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ItemCartBinding binding;

        public ViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}