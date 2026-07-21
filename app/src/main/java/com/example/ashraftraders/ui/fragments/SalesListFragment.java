package com.example.ashraftraders.ui.fragments;

import android.annotation.SuppressLint;
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
import com.example.ashraftraders.adapters.SalesAdapter;
import com.example.ashraftraders.data.repository.SalesRepository;
import com.example.ashraftraders.databinding.FragmentSalesListBinding;
import com.example.ashraftraders.viewmodel.SalesViewModel;
import com.example.ashraftraders.viewmodel.SalesViewModelFactory;

public class SalesListFragment extends Fragment {

    private FragmentSalesListBinding binding;
    private SalesViewModel viewModel;
    private SalesAdapter adapter;

    public SalesListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSalesListBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModel();
        initRecycler();
        observeSales();
        clickEvents();
        searchViewSetup();

        viewModel.loadRecentSales();
        @SuppressLint("RestrictedApi") SearchView.SearchAutoComplete searchAutoComplete = binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setHint("Search Customers");
        searchAutoComplete.setHintTextColor(Color.GRAY);
        searchAutoComplete.setTextColor(Color.GRAY);


    }

    private void initViewModel() {

        SalesRepository repository = new SalesRepository();

        viewModel = new ViewModelProvider(
                this,
                new SalesViewModelFactory(repository)
        ).get(SalesViewModel.class);
    }
    private void searchViewSetup(){
        binding.searchView.setIconifiedByDefault(false);
        binding.searchView.setFocusable(true);
        binding.searchView.setFocusableInTouchMode(true);
        binding.searchView.clearFocus();
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

    }

    private void initRecycler() {

        adapter = new SalesAdapter();

        binding.rvSales.setLayoutManager(
                new LinearLayoutManager(requireContext()));

        binding.rvSales.setAdapter(adapter);
    }

    private void observeSales() {

        viewModel.getRecentSales().observe(getViewLifecycleOwner(), list -> {

            if (list != null) {
                adapter.submitList(list);
            }

        });
    }

    private void clickEvents() {

        binding.btnBack.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()
        );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}