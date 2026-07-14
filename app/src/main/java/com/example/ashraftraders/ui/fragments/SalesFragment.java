package com.example.ashraftraders.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ashraftraders.R;
import com.example.ashraftraders.databinding.FragmentSalesBinding;
import com.example.ashraftraders.ui.activities.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SalesFragment extends Fragment {

    private FragmentSalesBinding binding;

    public SalesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSalesBinding.inflate(getLayoutInflater());
        requireActivity().getWindow().setStatusBarColor(Color.parseColor("#0B6A5D"));

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}