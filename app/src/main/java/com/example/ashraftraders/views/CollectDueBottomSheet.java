package com.example.ashraftraders.views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.ashraftraders.data.model.DueModel;
import com.example.ashraftraders.data.repository.CollectDueRepository;
import com.example.ashraftraders.databinding.BottomSheetCollectDueBinding;
import com.example.ashraftraders.session.SessionManager;
import com.example.ashraftraders.viewmodel.CollectDueViewModel;
import com.example.ashraftraders.viewmodel.CollectDueViewModelFactory;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

public class CollectDueBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetCollectDueBinding binding;

    private CollectDueViewModel viewModel;

    private DueModel dueModel;
    private SessionManager sessionManager;

    private OnPaymentSuccessListener listener;

    public interface OnPaymentSuccessListener {
        void onSuccess();
    }

    public void setOnPaymentSuccessListener(OnPaymentSuccessListener listener) {
        this.listener = listener;
    }

    public static CollectDueBottomSheet newInstance(DueModel model) {

        CollectDueBottomSheet sheet = new CollectDueBottomSheet();

        Bundle bundle = new Bundle();

        bundle.putLong("invoice_id", model.getInvoiceId());
        bundle.putString("invoice_no", model.getInvoiceNo());
        bundle.putString("customer_name", model.getCustomerName());
        bundle.putString("phone", model.getPhone());
        bundle.putDouble("due_amount", model.getDueAmount());
        bundle.putString("invoice_date", model.getInvoiceDate());

        sheet.setArguments(bundle);

        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = BottomSheetCollectDueBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModel();

        getBundleData();

        loadData();

        setupAmountPreview();

        observeViewModel();

        clickListener();

        sessionManager = new SessionManager(requireContext());
    }

    private void initViewModel() {

        CollectDueRepository repository = new CollectDueRepository();

        CollectDueViewModelFactory factory =
                new CollectDueViewModelFactory(repository);

        viewModel = new ViewModelProvider(this, factory)
                .get(CollectDueViewModel.class);
    }

    private void getBundleData() {

        dueModel = new DueModel();

        if (getArguments() == null) return;

        dueModel.setInvoiceId(getArguments().getLong("invoice_id"));
        dueModel.setInvoiceNo(getArguments().getString("invoice_no"));
        dueModel.setCustomerName(getArguments().getString("customer_name"));
        dueModel.setPhone(getArguments().getString("phone"));
        dueModel.setDueAmount(getArguments().getDouble("due_amount"));
        dueModel.setInvoiceDate(getArguments().getString("invoice_date"));
    }

    private void loadData() {

        binding.txtCustomer.setText(dueModel.getCustomerName());

        binding.txtPhone.setText(dueModel.getPhone());

        binding.txtInvoice.setText("Invoice : " + dueModel.getInvoiceNo());

        binding.txtCurrentDue.setText(
                String.format(Locale.getDefault(),
                        "৳ %.0f",
                        dueModel.getDueAmount()));

        binding.txtCurrentDuePreview.setText(
                String.format(Locale.getDefault(),
                        "৳ %.0f",
                        dueModel.getDueAmount()));

        binding.txtReceivePreview.setText("৳ 0");

        binding.txtRemainingDue.setText(
                String.format(Locale.getDefault(),
                        "৳ %.0f",
                        dueModel.getDueAmount()));
    }

    private void setupAmountPreview() {

        final double currentDue = dueModel.getDueAmount();

        binding.etAmount.addTextChangedListener(new TextWatcher() {

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

                double receive = 0;

                try {

                    if (!s.toString().trim().isEmpty()) {
                        receive = Double.parseDouble(s.toString());
                    }

                } catch (Exception ignored) {
                }

                if (receive > currentDue) {
                    receive = currentDue;
                }

                double remaining = currentDue - receive;

                binding.txtReceivePreview.setText(
                        String.format(Locale.getDefault(),
                                "৳ %.0f",
                                receive));

                binding.txtRemainingDue.setText(
                        String.format(Locale.getDefault(),
                                "৳ %.0f",
                                remaining));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }
    private void observeViewModel() {

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {

            if (loading == null) return;

            binding.btnReceive.setEnabled(!loading);

            if (loading) {
                binding.btnReceive.setText("Processing...");
            } else {
                binding.btnReceive.setText("পেমেন্ট গ্রহণ করুন");
            }

        });

        viewModel.getMessage().observe(getViewLifecycleOwner(), message -> {

            if (message == null || message.isEmpty()) return;

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

        });

        viewModel.getPaymentResult().observe(getViewLifecycleOwner(), success -> {

            if (Boolean.TRUE.equals(success)) {

                if (listener != null) {
                    listener.onSuccess();
                }

                dismiss();

            }

        });

    }

    private void clickListener() {

        binding.btnReceive.setOnClickListener(v -> validateAndCollect());

    }

    private void validateAndCollect() {

        String amountText = binding.etAmount.getText().toString().trim();
        String remarks = binding.etRemarks.getText().toString().trim();

        if (amountText.isEmpty()) {

            binding.etAmount.setError("আদায়ের পরিমাণ লিখুন");
            binding.etAmount.requestFocus();
            return;

        }

        double receiveAmount;

        try {

            receiveAmount = Double.parseDouble(amountText);

        } catch (Exception e) {

            binding.etAmount.setError("সঠিক পরিমাণ লিখুন");
            binding.etAmount.requestFocus();
            return;

        }

        if (receiveAmount <= 0) {

            binding.etAmount.setError("পরিমাণ অবশ্যই ০ এর বেশি হতে হবে");
            binding.etAmount.requestFocus();
            return;

        }

        if (receiveAmount > dueModel.getDueAmount()) {

            binding.etAmount.setError("বকেয়ার চেয়ে বেশি নেওয়া যাবে না");
            binding.etAmount.requestFocus();
            return;

        }

        viewModel.collectDuePayment(
                dueModel.getInvoiceId(),
                receiveAmount,
                remarks,
                sessionManager.getUserId()  // অথবা getUserId()
        );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}