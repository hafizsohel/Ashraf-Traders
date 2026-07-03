package com.example.ashraftraders.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ashraftraders.R;
import com.example.ashraftraders.adapters.SummaryAdapter;
import com.example.ashraftraders.data.model.SummaryModel;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dashboard,
                container,
                false);

    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            getActivity().getWindow().setStatusBarColor(Color.parseColor("#00332A"));
        }

        RecyclerView rvTopSummary = view.findViewById(R.id.rvTopSummary);

        rvTopSummary.setLayoutManager(
                new GridLayoutManager(requireContext(), 2));

        ArrayList<SummaryModel> summaryList = new ArrayList<>();

        summaryList.add(new SummaryModel(
                R.drawable.ic_inventory,
                "Total Products",
                "1250",
                "+5%"));

        summaryList.add(new SummaryModel(
                R.drawable.ic_attach_money,
                "Today's Sales",
                "৳35,450",
                "+12%"));

        summaryList.add(new SummaryModel(
                R.drawable.ic_shopping_cart,
                "Today's Purchase",
                "৳18,250",
                "+4%"));


        summaryList.add(new SummaryModel(
                R.drawable.ic_inventory,
                "Low Stock",
                "18",
                "-2%"));

        SummaryAdapter adapter =
                new SummaryAdapter(summaryList);

        rvTopSummary.setAdapter(adapter);
    }
}