package com.example.ashraftraders.ui.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ashraftraders.R;
import com.example.ashraftraders.databinding.FragmentProfileBinding;
import com.example.ashraftraders.session.SessionManager;
import com.example.ashraftraders.ui.activities.LoginActivity;
import com.example.ashraftraders.ui.activities.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        requireActivity().getWindow().setStatusBarColor(Color.parseColor("#004041"));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpButton();

        SessionManager sessionManager = new SessionManager(requireContext());

        binding.tvFullName.setText(sessionManager.getFullName());
        binding.tvRoll.setText(sessionManager.getRole());



        binding.ivBack.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).openHome();
        });


        Glide.with(requireContext())
                .load(sessionManager.getProfileImage())
                .placeholder(R.drawable.ic_box)
                .error(R.drawable.ic_box)
                .circleCrop()
                .into(binding.imgProfile);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void setUpButton(){
        binding.menuLogout.setOnClickListener(v -> {

            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.dialog_logout);
            dialog.setCancelable(false);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));

                dialog.getWindow().setLayout(
                        (int) (getResources().getDisplayMetrics().widthPixels * 0.90),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            MaterialButton btnCancel = dialog.findViewById(R.id.btnCancel);
            MaterialButton btnLogout = dialog.findViewById(R.id.btnLogout);

            btnCancel.setOnClickListener(cancelView -> dialog.dismiss());

            btnLogout.setOnClickListener(logoutView -> {
                SessionManager sessionManager = new SessionManager(requireContext());
                sessionManager.logout();

                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
                dialog.dismiss();
            });

            if (dialog.getWindow() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            }
            dialog.show();
        });

        binding.menuShop.setOnClickListener(v -> {

            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.dialog_shop_info);

            dialog.setCancelable(true);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));

                dialog.getWindow().setLayout(
                        (int) (getResources().getDisplayMetrics().widthPixels * 0.90),
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }

            MaterialButton btnClose = dialog.findViewById(R.id.btnClose);
            btnClose.setOnClickListener(close -> dialog.dismiss());
            dialog.show();

        });
    }
}