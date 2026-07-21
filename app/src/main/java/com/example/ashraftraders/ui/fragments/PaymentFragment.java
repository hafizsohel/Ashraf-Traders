package com.example.ashraftraders.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;
import com.example.ashraftraders.R;
import com.example.ashraftraders.data.model.invoice.InvoiceItemRequest;
import com.example.ashraftraders.data.model.invoice.InvoiceRequest;
import com.example.ashraftraders.data.repository.CartRepository;
import com.example.ashraftraders.data.repository.InvoiceRepository;
import com.example.ashraftraders.data.room.AppDatabase;
import com.example.ashraftraders.data.room.entity.CartEntity;
import com.example.ashraftraders.databinding.FragmentPaymentBinding;
import com.example.ashraftraders.viewmodel.CartViewModel;
import com.example.ashraftraders.viewmodel.CartViewModelFactory;
import com.example.ashraftraders.viewmodel.InvoiceViewModel;
import com.example.ashraftraders.viewmodel.InvoiceViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class PaymentFragment extends Fragment {

    private FragmentPaymentBinding binding;
    private CartViewModel cartViewModel;
    private InvoiceViewModel invoiceViewModel;
    private double grandTotal = 0;
    private long customerId;
    private List<CartEntity> cartItems = new ArrayList<>();

    public PaymentFragment() {
        super(R.layout.fragment_payment);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentPaymentBinding.bind(view);

        if (getArguments() != null) {
            customerId = getArguments().getLong("customer_id", 0);
        }

        initViewModel();
        setupToolbar();
        observeCart();
        setupBackPressed();
        setupPaymentMethod();
        setupPaidAmountListener();
        setupCompleteSale();
        observeInvoice();
    }

    private void initViewModel() {

        CartRepository cartRepository = new CartRepository(
                AppDatabase.getInstance(requireContext()).cartDao()
        );

        cartViewModel = new ViewModelProvider(
                this,
                new CartViewModelFactory(cartRepository)
        ).get(CartViewModel.class);

        InvoiceRepository invoiceRepository = new InvoiceRepository();

        invoiceViewModel = new ViewModelProvider(
                this,
                new InvoiceViewModelFactory(invoiceRepository)
        ).get(InvoiceViewModel.class);
    }

    private void setupToolbar() {

        binding.toolbar.setNavigationOnClickListener(v -> {

            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });
    }


    private void observeCart() {

        calculateDue();
        cartViewModel.getGrandTotal()
                .observe(getViewLifecycleOwner(), total -> {

                    if (total == null)
                        total = 0.0;

                    grandTotal = total;

                    binding.tvSummaryTotal.setText(
                            "৳ " + String.format("%,.2f", total)
                    );

                    binding.edtPaidAmount.setText(
                            String.valueOf(total)
                    );
                });

        cartViewModel.getCartItems().observe(getViewLifecycleOwner(), items -> {

            if (items != null) {
                cartItems = items;
            }

        });

    }

    private void setupBackPressed() {

        requireActivity()
                .getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(),
                        new OnBackPressedCallback(true) {
                            @Override
                            public void handleOnBackPressed() {

                                requireActivity()
                                        .getSupportFragmentManager()
                                        .popBackStack();
                            }
                        });
    }

    private void setupPaymentMethod() {

        // Initial State
        if (binding.rbCash.isChecked()) {

            binding.edtPaidAmount.setEnabled(false);
            binding.edtPaidAmount.setFocusable(false);
            binding.edtPaidAmount.setFocusableInTouchMode(false);

            binding.edtPaidAmount.setText(
                    String.format("%.2f", grandTotal)
            );

        } else {

            binding.edtPaidAmount.setEnabled(true);
            binding.edtPaidAmount.setFocusable(true);
            binding.edtPaidAmount.setFocusableInTouchMode(true);
        }

        binding.rgPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.rbCash) {

                binding.edtPaidAmount.setEnabled(false);
                binding.edtPaidAmount.setFocusable(false);
                binding.edtPaidAmount.setFocusableInTouchMode(false);

                binding.edtPaidAmount.setText(
                        String.format("%.2f", grandTotal)
                );

            } else {

                binding.edtPaidAmount.setEnabled(true);
                binding.edtPaidAmount.setFocusable(true);
                binding.edtPaidAmount.setFocusableInTouchMode(true);
                binding.edtPaidAmount.requestFocus();
            }

            calculateDue();
        });
    }

    private void setupPaidAmountListener() {

        binding.edtPaidAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {

                calculateDue();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void calculateDue() {

        double paid = 0;

        String text = binding.edtPaidAmount
                .getText()
                .toString()
                .trim();

        if (!text.isEmpty()) {

            paid = Double.parseDouble(text);

        }

        double due = grandTotal - paid;

        if (due < 0)
            due = 0;

        updateSummary(paid, due);

    }
    private void updateSummary(double paid,
                               double due) {

        binding.tvDueAmount.setText(
                "৳ " + String.format("%,.2f", due)
        );

        binding.tvSummaryTotal.setText(
                "৳ " + String.format("%,.2f", grandTotal)
        );
        binding.tvTotal.setText(
                "৳ " + String.format("%,.2f", grandTotal)
        );

        binding.tvSummaryPaid.setText(
                "৳ " + String.format("%,.2f", paid)
        );

        binding.tvSummaryDue.setText(
                "৳ " + String.format("%,.2f", due)
        );

    }
    private void setupCompleteSale() {

        binding.btnCompleteSale.setOnClickListener(v -> {

            if (cartItems.isEmpty()) {

                Toast.makeText(requireContext(),
                        "কার্ট খালি",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            invoiceViewModel.saveInvoice(
                    buildInvoiceRequest()
            );

        });

    }
    private InvoiceRequest buildInvoiceRequest() {

        InvoiceRequest request = new InvoiceRequest();

        request.setCustomerId(customerId);

        request.setPaymentType(

                binding.rbCash.isChecked()
                        ? "Cash"
                        : "Due"
        );

        request.setSubtotal(grandTotal);

        request.setDiscount(0.0);

        double paid = getPaidAmount();

        request.setPaidAmount(paid);

        request.setDueAmount(grandTotal - paid);

        request.setNote(

                binding.edtNote
                        .getText()
                        .toString()
                        .trim()
        );

        request.setItems(
                buildInvoiceItems()
        );

        return request;
    }
    private List<InvoiceItemRequest> buildInvoiceItems() {

        List<InvoiceItemRequest> list = new ArrayList<>();

        for (CartEntity cart : cartItems) {

            InvoiceItemRequest item = new InvoiceItemRequest();

            item.setProductId(
                    (long) cart.getId()
            );

            item.setPrice(
                    cart.getPrice()
            );

            item.setQuantity(
                    cart.getQuantity()
            );

            item.setDiscount(0.0);

            item.setSubtotal(

                    cart.getPrice()
                            * cart.getQuantity()
            );

            list.add(item);

        }

        return list;
    }
    private double getPaidAmount() {

        String text = binding.edtPaidAmount
                .getText()
                .toString()
                .trim();

        if (text.isEmpty()) {

            return 0;
        }

        return Double.parseDouble(text);

    }
    private void observeInvoice() {

        invoiceViewModel.getLoading().observe(getViewLifecycleOwner(), this::showLoading);

        invoiceViewModel.getSuccess().observe(getViewLifecycleOwner(), success -> {
            Log.d("SALE_FLOW", "Success = " + success);
            if (!Boolean.TRUE.equals(success))
                return;
            Log.d("SALE_FLOW", "OPEN DASHBOARD");

            showLoading(false);
            cartViewModel.clearCart();
            Toast.makeText(requireContext(),
                    "বিক্রয় সফল হয়েছে",
                    Toast.LENGTH_SHORT).show();

            openSuccessScreen();

        });
        invoiceViewModel.getInvoiceId().observe(getViewLifecycleOwner(), id -> {

            if (id == null)
                return;

            Toast.makeText(requireContext(),
                    "Invoice #" + id + " Saved",
                    Toast.LENGTH_SHORT).show();

            cartViewModel.clearCart();

        });

        invoiceViewModel.getError().observe(getViewLifecycleOwner(), error -> {

            if (error == null)
                return;

            Toast.makeText(requireContext(),
                    error,
                    Toast.LENGTH_SHORT).show();

        });

    }

    private void showLoading(boolean loading) {

        binding.btnCompleteSale.setEnabled(!loading);

        if (loading) {
            binding.btnCompleteSale.setText("Saving...");
        } else {

            binding.btnCompleteSale.setText("বিক্রয় সম্পন্ন করুন");
        }
    }
    private void openSuccessScreen() {

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new DashboardFragment())
                .commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}