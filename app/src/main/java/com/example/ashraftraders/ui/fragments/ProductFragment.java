package com.example.ashraftraders.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ashraftraders.adapters.ProductAdapter;
import com.example.ashraftraders.data.model.ProductModel;
import com.example.ashraftraders.data.repository.ProductRepository;
import com.example.ashraftraders.data.room.AppDatabase;
import com.example.ashraftraders.databinding.FragmentProductBinding;
import com.example.ashraftraders.viewmodel.ProductViewModel;
import com.example.ashraftraders.viewmodel.ProductViewModelFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductFragment extends Fragment {
    private FragmentProductBinding binding;
    private ProductViewModel viewModel;
    private ProductAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getWindow().setStatusBarColor(Color.parseColor("#0B6A5D"));

        ProductRepository repository = new ProductRepository(AppDatabase.getInstance(requireContext()).productDao());
        viewModel = new ViewModelProvider(this, new ProductViewModelFactory(repository)).get(ProductViewModel.class);

        initRecyclerView();
        setupSearchView();

        // [ম্যাজিক ফিক্স] ইন্টারনেট চেকিং বা কোনো প্রকার আর্টিফিশিয়াল লোডিং সব বাদ দেওয়া হলো
        if (binding.loadingOverlay != null) binding.loadingOverlay.setVisibility(View.GONE);
        // ডেটা অবজার্ভার এবং ব্যাকগ্রাউন্ড সিঙ্ক চালু
        observeProducts();
        viewModel.syncProducts();
        binding.btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

    }

    private void initRecyclerView() {
        adapter = new ProductAdapter();
        binding.rvProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvProducts.setHasFixedSize(true);
        binding.rvProducts.setNestedScrollingEnabled(false); // লেআউটের ঝাঁকুনি বন্ধ করার জন্য
        binding.rvProducts.setAdapter(adapter);
    }

    private void observeProducts() {
        viewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            // [ফ্লিঙ্কারিং ফিক্স] যদি ডেটা এখনো না আসে বা নাল থাকে, তবে স্ক্রিনের ভিউ টাচই করবে না।
            // এর ফলে এক্সএমএল বা পূর্বের স্ট্যাটিক ডেটা বিন্দুমাত্র লাফালাফি করার সুযোগ পাবে না।
            Log.d("UI", "Products = " + (products == null ? 0 : products.size()));

            if (products == null) {
                return;
            }
            adapter.updateData(products);
            updateUI(products);
        });
    }

    private void updateUI(@NonNull List<ProductModel> products) {
        adapter.setProducts(products);

        int totalProducts = products.size();
        int totalStock = 0;
        double totalPurchasePrice = 0.0;
        Set<String> uniqueBrands = new HashSet<>();

        for (ProductModel product : products) {
            totalStock += product.getStock();
            totalPurchasePrice += (product.getPurchasePrice() * product.getStock());

            if (product.getBrandName() != null && !product.getBrandName().trim().isEmpty()) {
                uniqueBrands.add(product.getBrandName().trim());
            }
        }

        // সব টেক্সট একসাথে একই ন্যানোসেকেন্ডে আপডেট হবে
        binding.tvTotalProduct.setText(String.valueOf(totalProducts));
        binding.tvTotalStock.setText(String.valueOf(totalStock));
        binding.tvBrand.setText(String.valueOf(uniqueBrands.isEmpty() ? 1 : uniqueBrands.size()));
        binding.tvTotalPurchase.setText("৳" + String.format("%,.0f", totalPurchasePrice));
    }


    private void setupSearchView() {
        binding.searchView.setQueryHint("Search Product");
        // Default এ focus থাকবে না
        binding.searchView.clearFocus();
        binding.searchView.setFocusable(false);
        binding.searchView.setIconifiedByDefault(false);
        binding.searchView.setIconified(true);
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.searchProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.searchProducts(newText);
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}