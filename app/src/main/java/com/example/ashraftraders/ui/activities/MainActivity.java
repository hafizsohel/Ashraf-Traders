package com.example.ashraftraders.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ashraftraders.R;
import com.example.ashraftraders.databinding.ActivityMainBinding;
import com.example.ashraftraders.ui.fragments.DashboardFragment;
import com.example.ashraftraders.ui.fragments.ProductFragment;
import com.example.ashraftraders.ui.fragments.ProfileFragment;
import com.example.ashraftraders.ui.fragments.SalesFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.parseColor("#004D40"));
        getWindow().setNavigationBarColor(Color.WHITE);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new DashboardFragment())
                    .commit();
        }

        // gridButton is a button, not a fragment container - wire up its click here,
        // e.g. to open a categories/menu screen
        binding.gridButton.setOnClickListener(v -> {
        });

        binding.itemHome.setOnClickListener(v -> {
            loadFragment(new DashboardFragment());
        });

        binding.itemProfile.setOnClickListener(v -> {
            loadFragment(new ProfileFragment());
        });

        binding.itemProducts.setOnClickListener(v -> {
            loadFragment(new ProductFragment());
        });
        binding.itemOrders.setOnClickListener(v -> {
            loadFragment(new SalesFragment());
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {

            Fragment fragment = getSupportFragmentManager()
                    .findFragmentById(R.id.fragmentContainer);

            if (fragment instanceof DashboardFragment) {
                showBottomNavigation();
            } else {
                hideBottomNavigation();
            }
        });

    }
    // এই মেথডটি MainActivity-তে যুক্ত করুন যেন ফ্র্যাগমেন্ট এটি খুঁজে পায়
    public ActivityMainBinding getBinding() {
        return binding;
    }
    public void hideBottomNavigation() {
        binding.curvedBottomNavigation.setVisibility(View.GONE);
    }

    public void showBottomNavigation() {
        binding.curvedBottomNavigation.setVisibility(View.VISIBLE);
    }
    private void loadFragment(Fragment fragment) {

        Fragment current = getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer);

        if (current != null &&
                current.getClass().equals(fragment.getClass())) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,    // Enter
                        R.anim.no_anim,     // Exit
                        R.anim.no_anim,     // Pop Enter
                        R.anim.no_anim      // Pop Exit
                )
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
