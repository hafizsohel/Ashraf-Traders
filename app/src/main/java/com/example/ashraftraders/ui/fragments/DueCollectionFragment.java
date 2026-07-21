package com.example.ashraftraders.ui.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ashraftraders.adapters.DueAdapter;
import com.example.ashraftraders.data.model.DueModel;
import com.example.ashraftraders.data.repository.DueRepository;
import com.example.ashraftraders.databinding.FragmentDueCollectionBinding;
import com.example.ashraftraders.ui.activities.MainActivity;
import com.example.ashraftraders.viewmodel.DueViewModel;
import com.example.ashraftraders.viewmodel.DueViewModelFactory;
import com.example.ashraftraders.views.CollectDueBottomSheet;

public class DueCollectionFragment extends Fragment {

    private FragmentDueCollectionBinding binding;

    private DueViewModel viewModel;
    private DueAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDueCollectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModel();
        initRecyclerView();
        observeData();
        initSearch();
        setupToolbar();

        viewModel.loadDueList();

        adapter.setOnCollectClickListener(dueModel -> {

            CollectDueBottomSheet bottomSheet =
                    CollectDueBottomSheet.newInstance(dueModel);

            bottomSheet.setOnPaymentSuccessListener(() -> {

                // Due List Refresh
                viewModel.loadDueList();

                // চাইলে Dashboard Refresh-ও করতে পারো
                // dashboardViewModel.loadDashboardSummary();

            });

            bottomSheet.show(
                    getParentFragmentManager(),
                    "CollectDueBottomSheet"
            );

        });

    }
    private void setupToolbar() {

        binding.toolbar.setNavigationOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void initViewModel() {

        DueRepository repository = new DueRepository();

        DueViewModelFactory factory =
                new DueViewModelFactory(repository);

        viewModel = new ViewModelProvider(this, factory)
                .get(DueViewModel.class);

    }

    private void initRecyclerView() {

        adapter = new DueAdapter();

        binding.rvDueList.setLayoutManager(
                new LinearLayoutManager(requireContext()));

        binding.rvDueList.setAdapter(adapter);

    }

    private void observeData() {

        viewModel.dueList.observe(getViewLifecycleOwner(), dueModels -> {

            adapter.submitList(dueModels);

            double totalDue = 0;

            for (DueModel model : dueModels) {
                totalDue += model.getDueAmount();
            }

            binding.txtTotalDue.setText(
                    String.format("৳ %.0f", totalDue)
            );

        });

    }

    private void initSearch() {

        binding.searchView.setIconified(false);
        binding.searchView.clearFocus();
        binding.searchView.setQueryHint("নাম / ফোন / ইনভয়েস খুঁজুন");
        binding.searchView.setOnClickListener(v -> {
            binding.searchView.setIconified(false);
            binding.searchView.requestFocusFromTouch();
        });

        binding.searchView.setOnSearchClickListener(v -> {
            ((MainActivity) requireActivity()).hideBottomNavigation();
        });

        binding.searchView.setOnCloseListener(() -> {
            ((MainActivity) requireActivity()).showBottomNavigation();
            return false;
        });

        binding.searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ((MainActivity) requireActivity()).hideBottomNavigation();
            } else {
                ((MainActivity) requireActivity()).showBottomNavigation();
            }
        });

        binding.searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}