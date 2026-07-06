package com.example.ashraftraders.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ashraftraders.R;
import com.example.ashraftraders.data.room.AppDatabase;
import com.example.ashraftraders.data.room.ProductDao;
import com.example.ashraftraders.databinding.FragmentDashboardBinding;
import com.example.ashraftraders.viewmodel.DashboardViewModelFactory;
import com.example.ashraftraders.viewmodel.ProductViewModelFactory;
import com.example.ashraftraders.viewmodel.DashboardViewModel;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getWindow().setStatusBarColor(Color.parseColor("#00332A"));

        // রুম ডেটাবেজ ও অফলাইন ফ্যাক্টরি দিয়ে ভিউমডেল সফলভাবে তৈরি (নো-এরর গ্যারান্টি)
        AppDatabase db = AppDatabase.getInstance(requireContext());
        ProductDao dao = db.productDao();
        DashboardViewModelFactory factory = new DashboardViewModelFactory(dao);
        viewModel = new ViewModelProvider(this, factory).get(DashboardViewModel.class);

        // স্ট্যাটিক কার্ড ডিজাইন লোড করা
        setupDashboardUIStaticElements();

        // অফলাইন ডেটা লিসেন ও শো করা
        observeOfflineDashboard();

        binding.cardTotalProduct.setOnClickListener(v ->
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new ProductListFragment())
                        .addToBackStack(null)
                        .commit()
        );
    }

    private void setupDashboardUIStaticElements() {
        // Total Product
        binding.layoutTotalProduct.txtTitle.setText("মোট পণ্য");
        binding.layoutTotalProduct.imgSummary.setImageResource(R.drawable.ic_box);
        binding.layoutTotalProduct.iconContainer.setCardBackgroundColor(Color.parseColor("#E0F2F1"));

        // Brand
        binding.layoutTodaySales.txtTitle.setText("ব্র্যান্ড");
        binding.layoutTodaySales.imgSummary.setImageResource(R.drawable.ic_tag);
        binding.layoutTodaySales.iconContainer.setCardBackgroundColor(Color.parseColor("#E8EAF6"));

        // Purchase
        binding.layoutTodayPurchase.txtTitle.setText("মোট ক্রয়");
        binding.layoutTodayPurchase.imgSummary.setImageResource(R.drawable.ic_taka);
        binding.layoutTodayPurchase.iconContainer.setCardBackgroundColor(Color.parseColor("#FFF3E0"));

        // Low Stock
        binding.layoutLowStock.txtTitle.setText("লো স্টক");
        binding.layoutLowStock.imgSummary.setImageResource(R.drawable.ic_warning);
        binding.layoutLowStock.iconContainer.setCardBackgroundColor(Color.parseColor("#FCE4EC"));
    }

    private void observeOfflineDashboard() {

        // ১. মোট পণ্য রিয়েল-টাইম অফলাইন অবজার্ভার
        viewModel.getTotalProducts().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                binding.layoutTotalProduct.txtValue.setText(String.valueOf(total));
            }
        });

        // ২. মোট ব্র্যান্ড রিয়েল-টাইম অফলাইন অবজার্ভার
        viewModel.getTotalBrands().observe(getViewLifecycleOwner(), brands -> {
            if (brands != null) {
                binding.layoutTodaySales.txtValue.setText(String.valueOf(brands));
            }
        });

        // ৩. মোট ক্রয়মূল্য রিয়েল-টাইম অফলাইন অবজার্ভার
        viewModel.getTotalPurchase().observe(getViewLifecycleOwner(), totalPurchase -> {
            if (totalPurchase != null) {
                binding.layoutTodayPurchase.txtValue.setText(
                        "৳" + String.format("%,.0f", totalPurchase));
            }
        });

        // ৪. লো স্টক রিয়েল-টাইম অফলাইন অবজার্ভার
        viewModel.getLowStockCount().observe(getViewLifecycleOwner(), lowStock -> {
            if (lowStock != null) {
                binding.layoutLowStock.txtValue.setText(String.valueOf(lowStock));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}