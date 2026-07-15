package com.example.ashraftraders.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ashraftraders.R;
import com.example.ashraftraders.data.model.SalesSummaryModel;
import com.example.ashraftraders.data.repository.SalesRepository;
import com.example.ashraftraders.databinding.FragmentSalesBinding;
import com.example.ashraftraders.ui.activities.MainActivity;
import com.example.ashraftraders.viewmodel.SalesViewModel;
import com.example.ashraftraders.viewmodel.SalesViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SalesFragment extends Fragment {

    private FragmentSalesBinding binding;
    private SalesViewModel viewModel;

    public SalesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSalesBinding.inflate(getLayoutInflater());
        requireActivity().getWindow().setStatusBarColor(Color.parseColor("#0B6A5D"));


        initViewModel();
        observeSummary();

        binding.quickActionsLayout.btnNewSale.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new NewSaleFragment())
                    .addToBackStack(null)
                    .commit();
        });


        binding.toolbar.setNavigationOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showTodayDate();
    }

    private void showTodayDate() {

        SimpleDateFormat sdf =
                new SimpleDateFormat("dd MMMM, yyyy", new Locale("bn"));

        binding.tvDate.setText(sdf.format(new Date()));
    }

    private void initViewModel() {

        SalesRepository repository = new SalesRepository();

        viewModel = new ViewModelProvider(
                this,
                new SalesViewModelFactory(repository)
        ).get(SalesViewModel.class);

        viewModel.loadSummary();
    }
    private void observeSummary() {

        viewModel.getSummary().observe(getViewLifecycleOwner(), model -> {

            if (model == null)
                return;

            binding.tvTotalSale.setText(
                    "৳ " + String.format("%,.0f",
                            model.getTotalSale() == null ? 0 : model.getTotalSale()));

            binding.tvTotalOrder.setText(
                    (model.getTotalOrder() == null ? 0 : model.getTotalOrder()) + " টি");

            binding.tvTotalDue.setText(
                    "৳ " + String.format("%,.0f",
                            model.getTotalDue() == null ? 0 : model.getTotalDue()));

            binding.tvTotalProfit.setText(
                    "৳ " + String.format("%,.0f",
                            model.getTotalProfit() == null ? 0 : model.getTotalProfit()));
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {

            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}