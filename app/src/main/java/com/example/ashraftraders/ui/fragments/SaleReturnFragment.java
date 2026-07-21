package com.example.ashraftraders.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.ashraftraders.adapters.SaleReturnAdapter;
import com.example.ashraftraders.databinding.FragmentSaleReturnBinding;
import com.example.ashraftraders.session.SessionManager;
import com.example.ashraftraders.ui.activities.MainActivity;
import com.example.ashraftraders.viewmodel.SaleReturnViewModel;
import com.google.gson.JsonArray;

public class SaleReturnFragment extends Fragment {

    private FragmentSaleReturnBinding binding;
    private SaleReturnViewModel viewModel;
    private SessionManager sessionManager;

    private long invoiceId = -1;
    private SaleReturnAdapter adapter;

    public SaleReturnFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SaleReturnViewModel.class);
        sessionManager = new SessionManager(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSaleReturnBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        adapter = new SaleReturnAdapter();

        binding.rvProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvProducts.setAdapter(adapter);
        observeViewModel();
        initClickListener();


        return binding.getRoot();
    }

    private void initClickListener() {
        binding.btnSearch.setOnClickListener(v -> {

            String invoiceNo = binding.etInvoiceNo
                    .getText()
                    .toString()
                    .trim();

            if (invoiceNo.isEmpty()) {

                binding.etInvoiceNo.setError("Enter Invoice No");
                return;

            }

            viewModel.searchInvoice(invoiceNo);

        });
        binding.btnReturn.setOnClickListener(v -> {

            JsonArray items = adapter.getSelectedItemsAsJson();

            if (items.size() == 0) {
                Toast.makeText(requireContext(),
                        "কমপক্ষে একটি পণ্য নির্বাচন করুন",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String remarks = binding.etRemarks.getText().toString().trim();

            viewModel.saveSaleReturn(
                    invoiceId,
                    items,
                    "Cash",
                    remarks,
                    sessionManager.getUserId()
            );
        });
    }

    private void observeViewModel() {

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading ->
                binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE));

        viewModel.getSuccess().observe(getViewLifecycleOwner(), success -> {

            if (Boolean.TRUE.equals(success)) {

                Toast.makeText(requireContext(),
                        "Sale returned successfully",
                        Toast.LENGTH_SHORT).show();

            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {

            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }

        });

        viewModel.getItems().observe(getViewLifecycleOwner(), list -> {

            adapter.submitList(list);

            binding.cardInvoice.setVisibility(View.VISIBLE);

            binding.btnReturn.setVisibility(View.VISIBLE);

        });

        viewModel.getInvoice().observe(getViewLifecycleOwner(), invoice -> {

            if (invoice == null) return;

            invoiceId = invoice.getId();

            binding.txtInvoiceNo.setText("Invoice : " + invoice.getInvoiceNo());

            binding.txtCustomer.setText("Customer : " + invoice.getCustomerName());

            binding.txtDate.setText("Date : " + invoice.getInvoiceDate());

            binding.txtTotal.setText("Total : ৳ " + invoice.getGrandTotal());

        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).hideBottomNavigation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        ((MainActivity) requireActivity()).showBottomNavigation();
    }
}