package com.example.ashraftraders.ui.fragments;

import static android.opengl.ETC1.isValid;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ashraftraders.R;
import com.example.ashraftraders.data.model.customer.CustomerModel;
import com.example.ashraftraders.data.model.customer.CustomerRequest;
import com.example.ashraftraders.data.repository.CustomerRepository;
import com.example.ashraftraders.databinding.FragmentCustomerInfoBinding;
import com.example.ashraftraders.viewmodel.CustomerViewModel;
import com.example.ashraftraders.viewmodel.CustomerViewModelFactory;

public class CustomerInfoFragment extends Fragment {

    private FragmentCustomerInfoBinding binding;
    private CustomerViewModel viewModel;

    private CustomerModel selectedCustomer;

    public CustomerInfoFragment() {
        super(R.layout.fragment_customer_info);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentCustomerInfoBinding.bind(view);

        initViewModel();
        setupToolbar();
        observeCustomer();
        setupPhoneSearch();
        setupBackPressed();
        setupButton();
    }

    private void initViewModel() {

        CustomerRepository repository = new CustomerRepository();

        viewModel = new ViewModelProvider(
                this,
                new CustomerViewModelFactory(repository)
        ).get(CustomerViewModel.class);
    }

    private void setupToolbar() {

        binding.toolbar.setNavigationOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void setupBackPressed() {

        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        requireActivity()
                                .getSupportFragmentManager()
                                .popBackStack();
                    }
                });
    }

    private void setupPhoneSearch() {

        binding.edtPhone.addTextChangedListener(new TextWatcher() {

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

                if (s.length() == 11) {

                    showLoading();

                    viewModel.searchCustomer(
                            s.toString()
                    );

                } else {

                    clearForm();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void observeCustomer() {

        viewModel.getCustomer().observe(getViewLifecycleOwner(), customer -> {

            hideLoading();

            selectedCustomer = customer;

            if (customer == null) {

                clearForm();

                binding.edtName.requestFocus();

                Toast.makeText(requireContext(),
                        "নতুন কাস্টমার",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            fillCustomer(customer);

            Toast.makeText(requireContext(),
                    "Customer Found",
                    Toast.LENGTH_SHORT).show();

        });

        viewModel.getSavedCustomer().observe(getViewLifecycleOwner(), customer -> {

            hideLoading();

            if (customer == null)
                return;

            selectedCustomer = customer;

            openPayment(customer.getId());

        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {

            hideLoading();

            if (error != null) {

                Toast.makeText(requireContext(),
                        error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillCustomer(CustomerModel customer) {

        binding.edtName.setText(customer.getCustomerName());

        binding.edtFatherName.setText(customer.getFatherName());

        binding.edtGranterName.setText(customer.getGuarantorName());

        binding.edtGranterPhone.setText(customer.getGuarantorPhone());

        binding.edtAddress.setText(customer.getAddress());

        binding.edtNote.setText(customer.getNote());
    }
    private void setupButton() {

        binding.btnNextToPayment.setOnClickListener(v -> {

            if (!isValid())
                return;

            showLoading();

            if (selectedCustomer != null) {

                hideLoading();

                openPayment(selectedCustomer.getId());

                return;
            }

            viewModel.saveCustomer(buildRequest());

        });

    }
    private boolean isValid() {

        String phone = binding.edtPhone.getText().toString().trim();
        String name = binding.edtName.getText().toString().trim();
        String address = binding.edtAddress.getText().toString().trim();

        if (phone.length() != 11) {

            binding.edtPhone.setError("সঠিক মোবাইল নম্বর দিন");
            binding.edtPhone.requestFocus();
            return false;
        }

        if (name.isEmpty()) {

            binding.edtName.setError("নাম লিখুন");
            binding.edtName.requestFocus();
            return false;
        }

        if (address.isEmpty()) {

            binding.edtAddress.setError("ঠিকানা লিখুন");
            binding.edtAddress.requestFocus();
            return false;
        }

        return true;
    }
    private CustomerRequest buildRequest() {

        CustomerRequest request = new CustomerRequest();

        request.setCustomerName(
                binding.edtName.getText().toString().trim());

        request.setPhone(
                binding.edtPhone.getText().toString().trim());

        request.setFatherName(
                binding.edtFatherName.getText().toString().trim());

        request.setGuarantorName(
                binding.edtGranterName.getText().toString().trim());

        request.setGuarantorPhone(
                binding.edtGranterPhone.getText().toString().trim());

        request.setAddress(
                binding.edtAddress.getText().toString().trim());

        request.setNote(
                binding.edtNote.getText().toString().trim());

        request.setCustomerType("Regular");
        request.setCreditLimit(0.0);
        request.setCurrentDue(0.0);
        request.setPreviousDue(0.0);
        request.setEmail("");

        return request;
    }
    private void openPayment(Long customerId) {

        Bundle bundle = new Bundle();
        bundle.putLong("customer_id", customerId);

        PaymentFragment fragment = new PaymentFragment();
        fragment.setArguments(bundle);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }


    private void clearForm() {

        selectedCustomer = null;

        binding.edtName.setText("");

        binding.edtFatherName.setText("");

        binding.edtGranterName.setText("");

        binding.edtGranterPhone.setText("");

        binding.edtAddress.setText("");

        binding.edtNote.setText("");
    }
    private void showLoading() {

        binding.btnNextToPayment.setEnabled(false);

        binding.btnNextToPayment.setText("অনুগ্রহ করে অপেক্ষা করুন...");
    }
    private void hideLoading() {

        binding.btnNextToPayment.setEnabled(true);

        binding.btnNextToPayment.setText("পেমেন্টে যান  ➔");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}