package com.example.ashraftraders.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ashraftraders.data.model.DueModel;
import com.example.ashraftraders.databinding.ItemDueCollectionBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DueAdapter extends RecyclerView.Adapter<DueAdapter.ViewHolder> {

    private final List<DueModel> list = new ArrayList<>();
    private final List<DueModel> fullList = new ArrayList<>();

    private OnCollectClickListener listener;

    public interface OnCollectClickListener {
        void onCollect(DueModel dueModel);
    }

    public void setOnCollectClickListener(OnCollectClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<DueModel> newList) {
        list.clear();
        fullList.clear();

        if (newList != null) {
            list.addAll(newList);
            fullList.addAll(newList);
        }

        notifyDataSetChanged();
    }

    public void filter(String keyword) {

        list.clear();

        if (keyword == null || keyword.trim().isEmpty()) {

            list.addAll(fullList);

        } else {

            keyword = keyword.toLowerCase(Locale.getDefault());

            for (DueModel item : fullList) {

                if ((item.getCustomerName() != null &&
                        item.getCustomerName().toLowerCase(Locale.getDefault()).contains(keyword))

                        ||

                        (item.getPhone() != null &&
                                item.getPhone().toLowerCase(Locale.getDefault()).contains(keyword))

                        ||

                        (item.getInvoiceNo() != null &&
                                item.getInvoiceNo().toLowerCase(Locale.getDefault()).contains(keyword))) {

                    list.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemDueCollectionBinding binding =
                ItemDueCollectionBinding.inflate(
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

        private final ItemDueCollectionBinding binding;

        public ViewHolder(ItemDueCollectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(DueModel model) {

            binding.txtCustomer.setText(model.getCustomerName());
            binding.txtPhone.setText(model.getPhone());
            binding.txtInvoice.setText("Invoice : " + model.getInvoiceNo());
            binding.txtDue.setText("৳ " +
                    String.format(Locale.getDefault(), "%.0f", model.getDueAmount()));
            binding.txtDate.setText(model.getInvoiceDate());
            binding.btnCollect.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCollect(model);
                }
            });
        }
    }
}