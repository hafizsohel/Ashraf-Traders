package com.example.ashraftraders.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ashraftraders.R;
import com.example.ashraftraders.databinding.ActivityMainBinding;
import com.example.ashraftraders.ui.fragments.DashboardFragment;
import com.example.ashraftraders.ui.fragments.ProductFragment;
import com.example.ashraftraders.ui.fragments.ProfileFragment;
import com.example.ashraftraders.viewmodel.NavigationViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavigationViewModel viewModel;

    private View[] radialItems;
    private final int activeColor = Color.parseColor("#FBBF24");
    private final int inactiveColor = Color.parseColor("#94D3CB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(NavigationViewModel.class);
        getWindow().setStatusBarColor(Color.parseColor("#004D40"));
        getWindow().setNavigationBarColor(Color.parseColor("#004D40"));

        // ৫টি আইটেমের জন্য সিকোয়েন্স সাজানো
        radialItems = new View[]{
                binding.radialItem4,        // সাপোর্ট (Left)
                binding.radialItem,         // সেটিংস (Top-Left / Top)
                binding.radialItem2,        // নতুন আইটেম (Top-Center / Center)
                binding.radialItem3,        // রিপোর্ট (Mid-Right)
                binding.radialItemSetting   // নোটিফিকেশন (Right)
        };

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new DashboardFragment())
                    .commit();
        }

        setupNavigationListeners();
        observeViewModel();

        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (Boolean.TRUE.equals(viewModel.isRadialMenuOpen().getValue())) {
                            viewModel.closeRadialMenu();
                            return;
                        }

                        if (getSupportFragmentManager().findFragmentById(R.id.fragmentContainer)
                                instanceof DashboardFragment) {
                            finish();
                        } else {
                            viewModel.selectTab(NavigationViewModel.NavTab.DASHBOARD);
                            loadFragment(new DashboardFragment());
                        }
                    }
        });
    }

    private void setupNavigationListeners() {
        binding.itemHome.setOnClickListener(v -> {
            viewModel.selectTab(NavigationViewModel.NavTab.DASHBOARD);
            loadFragment(new DashboardFragment());
        });

        binding.itemSales.setOnClickListener(v -> {
            viewModel.selectTab(NavigationViewModel.NavTab.SALES);
            loadFragment(new ProductFragment());
        });

        binding.gridButton.setOnClickListener(v -> viewModel.toggleRadialMenu()
        );
        binding.menuOverlay.setOnClickListener(v -> viewModel.closeRadialMenu());

        // Radial Items Click Listeners
        binding.radialItem.setOnClickListener(v ->{
            viewModel.closeRadialMenu();
            loadFragment(new ProfileFragment());

        });
        binding.radialItem3.setOnClickListener(v -> {
            viewModel.closeRadialMenu();
            loadFragment(new ProductFragment());
        });
        binding.radialItem4.setOnClickListener(v -> viewModel.closeRadialMenu());
        binding.radialItemSetting.setOnClickListener(v ->{
            viewModel.closeRadialMenu();
            loadFragment(new ProfileFragment());
        });
    }

    private void observeViewModel() {
        viewModel.getSelectedTab().observe(this, tab -> {
            switch (tab) {
                case DASHBOARD:
                    updateTabUI(binding.imgHome, binding.txtHome);
                    break;
                case SALES:
                    updateTabUI(binding.imgSales, binding.txtSales);
                    break;
            }
        });

        viewModel.isRadialMenuOpen().observe(this, this::animateRadialMenu);
    }

    private void updateTabUI(ImageView selectedImg, TextView selectedTxt) {
        binding.imgHome.setColorFilter(inactiveColor);
        binding.imgSales.setColorFilter(inactiveColor);
        binding.txtHome.setTextColor(inactiveColor);
        binding.txtSales.setTextColor(inactiveColor);

        selectedImg.setColorFilter(activeColor);
        selectedTxt.setTextColor(activeColor);

        selectedImg.setScaleX(0.7f);
        selectedImg.setScaleY(0.7f);
        selectedImg.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(250)
                .setInterpolator(new AnticipateOvershootInterpolator(1.5f))
                .start();
    }

    private void animateRadialMenu(boolean isOpen) {
        if (isOpen) {
            binding.radialContainer.setVisibility(View.VISIBLE);
            binding.menuOverlay.setVisibility(View.VISIBLE);

            binding.menuOverlay.animate().alpha(1f).setDuration(300).start();

            // 1. Dome Scale Up
            binding.domeBackground.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .alpha(0.95f)
                    .setDuration(350)
                    .setInterpolator(new AnticipateOvershootInterpolator(0.8f))
                    .start();

            // 2. Elevate Center Button
            binding.gridButton.animate()
                    .scaleX(1.05f)
                    .scaleY(1.05f)
                    .translationY(-4f)
                    .setDuration(300)
                    .start();

        /*
           FINE-TUNED POSITIONS:
           - radiusX: 108dp (চওড়া)
           - radiusY: 48dp (কার্ভের ব্যালেন্স)
           - offsetY: 32dp (আইকনগুলোকে পরিমিত নিচে নামানোর জন্য)
        */
            float density = getResources().getDisplayMetrics().density;
            float radiusX = 120 * density;
            float radiusY = 90f * density;
            float offsetY = 40f * density;  // ২০f থেকে বাড়িয়ে ৩২f করায় সামান্য নিচে নেমে পারফেক্ট বসবে

            int total = radialItems.length;
            float startAngle = 180f; // একদম বামে (Left) ১৮০° থেকে শুরু
            float sweepAngle = 180f; // পুরো সেমি-সার্কেল (অর্ধবৃত্ত) ১৮০° কভার করবে
            float stepAngle = sweepAngle / (total - 1); // প্রতি আইকনের মাঝে সমান ৪৫° করে ব্যবধান হবে

            for (int i = 0; i < total; i++) {
                View item = radialItems[i];

                float angle = startAngle + (stepAngle * i);
                double rad = Math.toRadians(angle);

                float targetX = (float) (radiusX * Math.cos(rad));
                float targetY = (float) (radiusY * Math.sin(rad)) + offsetY;

                item.animate()
                        .translationX(targetX)
                        .translationY(targetY)
                        .alpha(1f)
                        .setDuration(350)
                        .setStartDelay(i * 20L)
                        .setInterpolator(new AnticipateOvershootInterpolator(1.0f))
                        .start();
            }
        } else {
            // Close Animation
            binding.menuOverlay.animate().alpha(0f).setDuration(250).withEndAction(() ->
                    binding.menuOverlay.setVisibility(View.GONE)
            ).start();

            binding.domeBackground.animate()
                    .scaleX(0.3f)
                    .scaleY(0.3f)
                    .alpha(0f)
                    .setDuration(250)
                    .start();

            binding.gridButton.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .translationY(0f)
                    .setDuration(250)
                    .start();

            for (View item : radialItems) {
                item.animate()
                        .translationX(0f)
                        .translationY(0f)
                        .alpha(0f)
                        .setDuration(200)
                        .start();
            }

            binding.radialContainer.postDelayed(() -> binding.radialContainer.setVisibility(View.GONE), 250);
        }
    }

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
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if (current != null && current.getClass().equals(fragment.getClass())) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.no_anim,
                        R.anim.no_anim,
                        R.anim.no_anim
                )
                .replace(R.id.fragmentContainer, fragment)
                .commit();

        showBottomNavigation();
    }

    public void openHome() {
        viewModel.selectTab(NavigationViewModel.NavTab.DASHBOARD);
        loadFragment(new DashboardFragment());
    }
}