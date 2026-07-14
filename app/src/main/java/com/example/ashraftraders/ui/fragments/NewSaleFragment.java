package com.example.ashraftraders.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ashraftraders.R;
import com.example.ashraftraders.adapters.SaleProductAdapter;
import com.example.ashraftraders.data.model.CartMapper;
import com.example.ashraftraders.data.repository.CartRepository;
import com.example.ashraftraders.data.repository.ProductRepository;
import com.example.ashraftraders.data.room.AppDatabase;
import com.example.ashraftraders.databinding.FragmentNewSaleBinding;
import com.example.ashraftraders.viewmodel.CartViewModel;
import com.example.ashraftraders.viewmodel.CartViewModelFactory;
import com.example.ashraftraders.viewmodel.ProductViewModel;
import com.example.ashraftraders.viewmodel.ProductViewModelFactory;

public class NewSaleFragment extends Fragment {

    private FragmentNewSaleBinding binding;

    private ProductViewModel productViewModel;
    private CartViewModel cartViewModel;

    private SaleProductAdapter adapter;

    public NewSaleFragment() {
        super(R.layout.fragment_new_sale);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentNewSaleBinding.bind(view);

        initViewModels();
        setupToolbar();
        setupRecyclerView();
        observeProducts();
        observeCart();
        setupClickListeners();

        productViewModel.syncProducts();
    }

    private void initViewModels() {

        ProductRepository productRepository =
                new ProductRepository(
                        AppDatabase.getInstance(requireContext()).productDao());

        productViewModel = new ViewModelProvider(
                this,
                new ProductViewModelFactory(productRepository)
        ).get(ProductViewModel.class);

        CartRepository cartRepository =
                new CartRepository(
                        AppDatabase.getInstance(requireContext()).cartDao());

        cartViewModel = new ViewModelProvider(
                this,
                new CartViewModelFactory(cartRepository)
        ).get(CartViewModel.class);
    }

    private void setupToolbar() {

        binding.toolbar.setNavigationOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void setupRecyclerView() {

        adapter = new SaleProductAdapter(product -> {

            cartViewModel.addToCart(
                    CartMapper.fromProduct(product)
            );

            Toast.makeText(requireContext(),
                    product.getProductName() + " কার্টে যোগ হয়েছে",
                    Toast.LENGTH_SHORT).show();
        });

        binding.rvProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvProducts.setHasFixedSize(true);
        binding.rvProducts.setAdapter(adapter);
    }

    private void observeProducts() {

        productViewModel.getProducts().observe(getViewLifecycleOwner(),
                products -> adapter.setProducts(products));
    }

    private void observeCart() {

        cartViewModel.getCartCount().observe(getViewLifecycleOwner(), count -> {

            if (count == null) count = 0;

            binding.txtCartCount.setText("কার্টে আছে " + count + " টি পণ্য");
        });

        cartViewModel.getGrandTotal().observe(getViewLifecycleOwner(), total -> {

            if (total == null) total = 0.0;

            binding.txtTotal.setText(
                    "মোট: ৳ " + String.format("%,.2f", total)
            );
        });
    }

    private void setupClickListeners() {

        binding.btnCart.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new CartFragment())
                        .addToBackStack(null)
                        .commit());

        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}