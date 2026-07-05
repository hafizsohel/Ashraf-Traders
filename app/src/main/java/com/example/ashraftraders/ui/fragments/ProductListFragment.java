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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ashraftraders.adapters.ProductAdapter;
import com.example.ashraftraders.data.model.DashboardModel;
import com.example.ashraftraders.databinding.FragmentProductListBinding;
import com.example.ashraftraders.viewmodel.DashboardViewModel;
import com.example.ashraftraders.viewmodel.ProductViewModel;

public class ProductListFragment extends Fragment {

    private FragmentProductListBinding binding;
    private ProductViewModel viewModel;
    private DashboardViewModel dashboardViewModel;
    private ProductAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProductListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getWindow()
                .setStatusBarColor(Color.parseColor("#00332A"));

        dashboardViewModel = new ViewModelProvider(this)
                .get(DashboardViewModel.class);

        dashboardViewModel.loadDashboardStats();

        observeDashboard();

        initRecyclerView();

        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);


        observeProducts();

        viewModel.loadProducts();

        binding.toolbar.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack());
    }

    private void initRecyclerView() {

        adapter = new ProductAdapter();

        binding.rvProducts.setLayoutManager(
                new LinearLayoutManager(requireContext()));

        binding.rvProducts.setHasFixedSize(true);

        binding.rvProducts.setAdapter(adapter);

    }

    private void observeProducts() {

        viewModel.getProducts().observe(getViewLifecycleOwner(), products -> {

            if (products != null) {

                adapter.setProducts(products);

            }

        });

    }
    private void observeDashboard() {

        dashboardViewModel.getDashboardData().observe(getViewLifecycleOwner(), list -> {

            if (list == null || list.isEmpty()) return;

            DashboardModel data = list.get(0);

            binding.tvTotalProduct.setText(
                    String.valueOf(data.getTotalProducts()));

            binding.tvBrand.setText(
                    String.valueOf(data.getTotalBrands()));

            binding.tvTotalPurchase.setText(
                    "৳" + String.format("%,.0f",
                            data.getTotalPurchase()));

            binding.tvTotalStock.setText(
                    String.valueOf(data.getLowStock())); // অথবা getTotalStock()

        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}