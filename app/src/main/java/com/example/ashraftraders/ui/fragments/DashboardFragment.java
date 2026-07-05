package com.example.ashraftraders.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ashraftraders.R;
import com.example.ashraftraders.data.model.DashboardModel;
import com.example.ashraftraders.databinding.FragmentDashboardBinding;
import com.example.ashraftraders.viewmodel.DashboardViewModel;

import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getWindow()
                .setStatusBarColor(Color.parseColor("#00332A"));

        viewModel = new ViewModelProvider(this)
                .get(DashboardViewModel.class);

        observeDashboard();

        viewModel.loadDashboardStats();

        binding.cardTotalProduct.setOnClickListener(v ->
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new ProductListFragment())
                        .addToBackStack(null)
                        .commit()
        );
    }

    private void observeDashboard() {

        viewModel.getDashboardData().observe(getViewLifecycleOwner(), list -> {
            Log.d("Dashboard", "List = " + list);
            if (list == null || list.isEmpty()) return;

            DashboardModel data = list.get(0);

            // Total Product
            binding.layoutTotalProduct.txtTitle.setText("মোট পণ্য");
            binding.layoutTotalProduct.txtValue.setText(String.valueOf(data.getTotalProducts()));
            binding.layoutTotalProduct.imgSummary.setImageResource(R.drawable.ic_box);
            binding.layoutTotalProduct.iconContainer.setCardBackgroundColor(
                    Color.parseColor("#E0F2F1"));

            // Brand
            binding.layoutTodaySales.txtTitle.setText("ব্র্যান্ড");
            binding.layoutTodaySales.txtValue.setText(String.valueOf(data.getTotalBrands()));
            binding.layoutTodaySales.imgSummary.setImageResource(R.drawable.ic_tag);
            binding.layoutTodaySales.iconContainer.setCardBackgroundColor(
                    Color.parseColor("#E8EAF6"));

            // Purchase
            binding.layoutTodayPurchase.txtTitle.setText("মোট ক্রয়");
            binding.layoutTodayPurchase.txtValue.setText(
                    "৳" + String.format("%,.0f", data.getTotalPurchase()));
            binding.layoutTodayPurchase.imgSummary.setImageResource(R.drawable.ic_taka);
            binding.layoutTodayPurchase.iconContainer.setCardBackgroundColor(
                    Color.parseColor("#FFF3E0"));

            // Low Stock
            binding.layoutLowStock.txtTitle.setText("লো স্টক");
            binding.layoutLowStock.txtValue.setText(String.valueOf(data.getLowStock()));
            binding.layoutLowStock.imgSummary.setImageResource(R.drawable.ic_warning);
            binding.layoutLowStock.iconContainer.setCardBackgroundColor(
                    Color.parseColor("#FCE4EC"));

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}