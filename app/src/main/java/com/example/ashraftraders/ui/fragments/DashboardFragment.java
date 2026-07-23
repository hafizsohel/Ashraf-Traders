package com.example.ashraftraders.ui.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ashraftraders.R;
import com.example.ashraftraders.adapters.SalesAdapter;
import com.example.ashraftraders.data.model.ProductModel;
import com.example.ashraftraders.data.model.SaleModel;
import com.example.ashraftraders.data.repository.ProductRepository;
import com.example.ashraftraders.data.repository.SalesRepository;
import com.example.ashraftraders.data.room.AppDatabase;
import com.example.ashraftraders.data.room.ProductDao;
import com.example.ashraftraders.databinding.FragmentDashboardBinding;
import com.example.ashraftraders.viewmodel.DashboardViewModel;
import com.example.ashraftraders.viewmodel.DashboardViewModelFactory;
import com.example.ashraftraders.viewmodel.ProductViewModel;
import com.example.ashraftraders.viewmodel.ProductViewModelFactory;
import com.example.ashraftraders.viewmodel.SalesViewModel;
import com.example.ashraftraders.viewmodel.SalesViewModelFactory;
import com.example.ashraftraders.views.AddProductBottomSheet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private SalesViewModel viewModel;
    private SalesAdapter adapter;
    private DashboardViewModel dViewModel;
    private ProductViewModel pViewModel;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProductRepository repository = new ProductRepository(AppDatabase.getInstance(requireContext()).productDao());
        pViewModel = new ViewModelProvider(this, new ProductViewModelFactory(repository)).get(ProductViewModel.class);

        statusBarColor();
        // রুম ডেটাবেজ ও অফলাইন ফ্যাক্টরি দিয়ে ভিউমডেল সফলভাবে তৈরি (নো-এরর গ্যারান্টি)
        AppDatabase db = AppDatabase.getInstance(requireContext());
        ProductDao dao = db.productDao();
        DashboardViewModelFactory factory = new DashboardViewModelFactory(dao);
        dViewModel = new ViewModelProvider(this, factory).get(DashboardViewModel.class);


        binding.quickActionsLayout.btnSale.setOnClickListener(v -> openFragment(new NewSaleFragment()));
        binding.tvSeeAllSale.setOnClickListener(v -> openFragment(new SalesListFragment()));
        binding.quickActionsLayout.btnDueCollection.setOnClickListener(v -> openFragment(new DueCollectionFragment()));
        binding.quickActionsLayout.btnSaleReturn.setOnClickListener(v->openFragment(new SaleReturnFragment()));
        //binding.quickActionsLayout.btnProductBuy.setOnClickListener(v -> openFragment(new ProductListFragment()));

        binding.quickActionsLayout.btnProductBuy.setOnClickListener(v -> {

            if (!isNetworkAvailable()) {
                android.widget.Toast.makeText(requireContext(),
                        "ইন্টারনেট কানেকশন নেই! অফলাইনে নতুন পণ্য যোগ করা সম্ভব নয়।",
                        android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            AddProductBottomSheet bottomSheet = new AddProductBottomSheet(new AddProductBottomSheet.OnProductAddListener() {
                @Override
                public void onProductSubmit(ProductModel product) {
                    // [ম্যাজিক ফিক্স] ভিউমডেলের মাধ্যমে সার্ভার ও লোকাল রুমে ডেটা ইনসার্ট করা হচ্ছে
                    pViewModel.addProductToServerAndRoom(product);
                    android.widget.Toast.makeText(requireContext(),
                            product.getProductName() + " পণ্য সফলভাবে সাবমিট করা হয়েছে! ",
                            android.widget.Toast.LENGTH_SHORT).show();
                }
            });

            bottomSheet.show(getChildFragmentManager(), "AddProductBottomSheet");
        });


        initRecycler();
        initViewModel();
        loadData();
        observeSummary();

        // binding.cardSale.setOnClickListener(v -> {});
    }

    private void initViewModel() {

        SalesRepository repository = new SalesRepository();

        viewModel = new ViewModelProvider(
                this,
                new SalesViewModelFactory(repository)
        ).get(SalesViewModel.class);
    }

    private boolean isNetworkAvailable() {
        android.content.Context context = getContext();
        if (context == null) return false;
        android.net.ConnectivityManager connectivityManager = (android.net.ConnectivityManager)
                context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void statusBarColor(){
        requireActivity().getWindow().setStatusBarColor(Color.parseColor("#0B6A5D"));
        Window window = requireActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(ContextCompat.getColor(requireContext(), R.color.charcoal_l1));

    }

    private void openFragment(Fragment fragment) {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
    private void observeSummary() {

        viewModel.getSummary().observe(getViewLifecycleOwner(), model -> {

            if (model == null)
                return;

            binding.tvTodaySale.setText(
                    "৳ " + String.format("%,.0f",
                            model.getTotalSale() == null ? 0 : model.getTotalSale()));

            binding.tvTotalOrders.setText(
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


        viewModel.getRecentSales().observe(getViewLifecycleOwner(), list -> {

            if (list == null) return;

            List<SaleModel> todaySales = new ArrayList<>();

            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new Date());

            for (SaleModel sale : list) {

                if (sale.getInvoiceDate() != null &&
                        sale.getInvoiceDate().startsWith(today)) {

                    todaySales.add(sale);
                }
            }

            adapter.submitList(todaySales);
        });

        dViewModel.getTotalProducts().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                binding.tvTotalProduct.setText(String.valueOf(total));
                Log.d("Products", "observeSummary: " + total);
            }
        });
/*
        dViewModel.getAllProducts().observe(getViewLifecycleOwner(), products -> {
            Log.d("Products", "observeSummary: "+products);

            if (products != null && !products.isEmpty()) {

                double totalPurchasePrice = 0.0;

                for (ProductEntity product : products) {
                    totalPurchasePrice += (product.getPurchasePrice() * product.getStock());
                }
                binding.tvTodayBuy.setText("৳ " + String.format("%,.0f", totalPurchasePrice));
            } else {
                binding.tvTodayBuy.setText("৳ 0");
            }
        });
*/
    }
    private void initRecycler() {

        adapter = new SalesAdapter();
        binding.rvRecentSales.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecentSales.setHasFixedSize(true);
        binding.rvRecentSales.setAdapter(adapter);
    }

    private void loadData() {
        viewModel.loadSummary();
        viewModel.loadRecentSales();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}