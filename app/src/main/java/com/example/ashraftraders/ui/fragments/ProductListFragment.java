package com.example.ashraftraders.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
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
import com.example.ashraftraders.databinding.FragmentProductListBinding;
import com.example.ashraftraders.viewmodel.ProductViewModel;
import com.example.ashraftraders.viewmodel.ProductViewModelFactory;
import com.example.ashraftraders.views.AddProductBottomSheet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductListFragment extends Fragment {

    private FragmentProductListBinding binding;
    private ProductViewModel viewModel;
    private ProductAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater, container, false);
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
        viewModel.syncProductsFromServer();

        binding.btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Floating Action Button (FAB) ক্লিকের লজিক
        binding.fabAdd.setOnClickListener(v -> {
            if (!isNetworkAvailable()) {
                android.widget.Toast.makeText(requireContext(),
                        "ইন্টারনেট কানেকশন নেই! অফলাইনে নতুন পণ্য যোগ করা সম্ভব নয়।",
                        android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            AddProductBottomSheet bottomSheet = new AddProductBottomSheet(new AddProductBottomSheet.OnProductAddListener() {
                @Override
                public void onProductSubmit(ProductModel product) {
                    // [ম্যাজিক ফিক্স] ভিউমডেলের মাধ্যমে সার্ভার ও লোকাল রুমে ডেটা ইনসার্ট করা হচ্ছে
                    viewModel.addProductToServerAndRoom(product);

                    android.widget.Toast.makeText(requireContext(),
                            product.getProductName() + " পসফলভাবে সাবমিট করা হয়েছে! ",
                            android.widget.Toast.LENGTH_SHORT).show();
                }
            });

            bottomSheet.show(getChildFragmentManager(), "AddProductBottomSheet");
        });
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
            if (products == null || products.isEmpty()) return;
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
        binding.searchView.setIconifiedByDefault(false);
        binding.searchView.setIconified(false);
        binding.searchView.setQueryHint("Search Product");
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

    // [ফিক্স] এই মেথডটি আপনার ফ্রাগমেন্ট ক্লাসের ভেতরে যেকোনো জায়গায় বসিয়ে দিন
    private boolean isNetworkAvailable() {
        android.content.Context context = getContext();
        if (context == null) return false;
        android.net.ConnectivityManager connectivityManager = (android.net.ConnectivityManager)
                context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}