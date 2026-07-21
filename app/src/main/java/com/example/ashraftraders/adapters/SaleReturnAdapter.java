package com.example.ashraftraders.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ashraftraders.data.model.SaleReturnItemModel;
import com.example.ashraftraders.databinding.ItemSaleReturnProductBinding;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class SaleReturnAdapter extends RecyclerView.Adapter<SaleReturnAdapter.ViewHolder> {

    private final List<SaleReturnItemModel> list = new ArrayList<>();

    private OnReturnChangedListener listener;

    public interface OnReturnChangedListener {
        void onChanged(double totalAmount);
    }

    public void setOnReturnChangedListener(OnReturnChangedListener listener) {
        this.listener = listener;
    }

    public void submitList(List<SaleReturnItemModel> data) {

        list.clear();

        if (data != null) {
            list.addAll(data);
        }

        notifyDataSetChanged();

        calculateTotal();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemSaleReturnProductBinding binding =
                ItemSaleReturnProductBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SaleReturnItemModel model = list.get(position);

        holder.binding.txtProductName.setText(model.getProductName());

        holder.binding.txtSoldQty.setText("বিক্রি : " + model.getSaleQty());

        holder.binding.txtReturnedQty.setText("ফেরত : " + model.getReturnedQty());

        holder.binding.txtPrice.setText("৳ " + model.getUnitPrice());

        holder.binding.txtReturnQty.setText(String.valueOf((int) model.getReturnQty()));

        holder.binding.etReturnAmount.setText(
                String.valueOf(model.getReturnPrice())
        );

        // Plus Button
        holder.binding.btnPlus.setOnClickListener(v -> {

            if (model.getReturnQty() < model.getAvailableQty()) {

                model.setReturnQty(model.getReturnQty() + 1);

                model.setReturnPrice(
                        model.getReturnQty() * model.getUnitPrice()
                );

                holder.binding.txtReturnQty.setText(
                        String.valueOf((int) model.getReturnQty())
                );

                holder.binding.etReturnAmount.setText(
                        String.valueOf(model.getReturnPrice())
                );

                calculateTotal();

            }

        });

        // Minus Button
        holder.binding.btnMinus.setOnClickListener(v -> {

            if (model.getReturnQty() > 0) {

                model.setReturnQty(model.getReturnQty() - 1);

                model.setReturnPrice(
                        model.getReturnQty() * model.getUnitPrice()
                );

                holder.binding.txtReturnQty.setText(
                        String.valueOf((int) model.getReturnQty())
                );

                holder.binding.etReturnAmount.setText(
                        String.valueOf(model.getReturnPrice())
                );

                calculateTotal();

            }

        });

        holder.binding.etReturnAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().trim().isEmpty()) {

                    model.setReturnPrice(0);

                } else {

                    try {

                        double amount = Double.parseDouble(s.toString());

                        model.setReturnPrice(amount);

                    } catch (Exception e) {

                        model.setReturnPrice(0);

                    }

                }

                calculateTotal();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemSaleReturnProductBinding binding;

        public ViewHolder(ItemSaleReturnProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public List<SaleReturnItemModel> getSelectedItems() {

        List<SaleReturnItemModel> selectedItems = new ArrayList<>();

        for (SaleReturnItemModel item : list) {

            if (item.getReturnQty() > 0 || item.getReturnPrice() > 0) {
                selectedItems.add(item);
            }

        }

        return selectedItems;
    }
    public JsonArray getSelectedItemsAsJson() {

        JsonArray jsonArray = new JsonArray();

        for (SaleReturnItemModel item : list) {

            if (item.getReturnQty() <= 0) {
                continue;
            }

            JsonObject json = new JsonObject();

            json.addProperty("invoice_item_id", item.getInvoiceItemId());
            json.addProperty("product_id", item.getProductId());
            json.addProperty("return_qty", item.getReturnQty());
            json.addProperty("unit_price", item.getUnitPrice());
            json.addProperty("return_amount", item.getReturnPrice());

            jsonArray.add(json);
        }

        return jsonArray;
    }

    private void calculateTotal() {

        double total = 0;

        for (SaleReturnItemModel item : list) {
            total += item.getReturnPrice();
        }

        if (listener != null) {
            listener.onChanged(total);
        }
    }
}