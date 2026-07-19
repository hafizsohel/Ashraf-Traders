package com.example.ashraftraders.adapters;


import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ashraftraders.data.model.SaleModel;
import com.example.ashraftraders.databinding.ItemRecentSaleBinding;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder> {
    private final List<SaleModel> list = new ArrayList<>();

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(SaleModel model);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<SaleModel> newList) {

        list.clear();

        if (newList != null)
            list.addAll(newList);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemRecentSaleBinding binding = ItemRecentSaleBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemRecentSaleBinding b;

        ViewHolder(ItemRecentSaleBinding binding) {

            super(binding.getRoot());

            b = binding;

        }

        void bind(SaleModel model) {

            DecimalFormat df = new DecimalFormat("#,##0");

            b.txtInvoice.setText(model.getInvoiceNo());

            b.txtProduct.setText(model.getProductSummary());

            b.txtCustomer.setText("Name: "+model.getCustomerName());

            b.txtQty.setText("Qty : " + df.format(model.getQty()));

            b.txtTotal.setText("৳ " + df.format(model.getTotal()));

            b.txtPaid.setText("Paid ৳ " + df.format(model.getPaid()));

            b.txtDue.setText("Due ৳ " + df.format(model.getDue()));

            if ("Paid".equalsIgnoreCase(model.getStatus())) {

                b.chipStatus.setText("Paid");

                b.chipStatus.setTextColor(Color.parseColor("#2E7D32"));

                b.chipStatus.setChipBackgroundColorResource(
                        android.R.color.holo_green_light
                );

            } else {

                b.chipStatus.setText("Due");

                b.chipStatus.setTextColor(Color.WHITE);

                b.chipStatus.setChipBackgroundColorResource(
                        android.R.color.holo_red_dark
                );

            }

            b.getRoot().setOnClickListener(v -> {

                if (listener != null) {

                    listener.onClick(model);

                }
            });
        }
    }
}