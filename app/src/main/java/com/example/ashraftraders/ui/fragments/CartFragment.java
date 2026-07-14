package com.example.ashraftraders.ui.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ashraftraders.R;
import com.example.ashraftraders.adapters.CartAdapter;
import com.example.ashraftraders.data.repository.CartRepository;
import com.example.ashraftraders.data.room.AppDatabase;
import com.example.ashraftraders.data.room.entity.CartEntity;
import com.example.ashraftraders.databinding.FragmentCartBinding;
import com.example.ashraftraders.viewmodel.CartViewModel;
import com.example.ashraftraders.viewmodel.CartViewModelFactory;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private CartViewModel viewModel;
    private CartAdapter adapter;

    public CartFragment() {
        super(R.layout.fragment_cart);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentCartBinding.bind(view);

        initViewModel();
        initToolbar();
        initRecyclerView();
        observeCart();
        setupClickListeners();
    }

    private void initViewModel() {

        CartRepository repository =
                new CartRepository(
                        AppDatabase.getInstance(requireContext()).cartDao());

        viewModel = new ViewModelProvider(
                this,
                new CartViewModelFactory(repository)
        ).get(CartViewModel.class);
    }

    private void initToolbar() {
        binding.toolbar.setNavigationOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void initRecyclerView() {

        adapter = new CartAdapter(new CartAdapter.CartListener() {

            @Override
            public void onIncrease(CartEntity item) {
                viewModel.increase(item);
            }

            @Override
            public void onDecrease(CartEntity item) {
                viewModel.decrease(item);
            }

            @Override
            public void onDelete(CartEntity item) {
                viewModel.remove(item.getId());
            }
        });

        binding.rvCart.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCart.setHasFixedSize(true);
        binding.rvCart.setAdapter(adapter);
    }

    private void observeCart() {

        viewModel.getCartItems().observe(getViewLifecycleOwner(),
                adapter::setCartItems);

        viewModel.getCartCount().observe(getViewLifecycleOwner(), count -> {

            if (count == null) count = 0;

            binding.tvItemCount.setText(count + " টি পণ্য");
        });

        viewModel.getGrandTotal().observe(getViewLifecycleOwner(), total -> {

            if (total == null) total = 0.0;

            binding.tvGrandTotal.setText(
                    "৳ " + String.format("%,.2f", total));
        });
    }

    private void setupClickListeners() {

        binding.btnCustomer.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new CustomerInfoFragment())
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