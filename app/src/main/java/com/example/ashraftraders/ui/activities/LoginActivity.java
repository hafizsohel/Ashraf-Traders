package com.example.ashraftraders.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ashraftraders.databinding.ActivityLoginBinding;
import com.example.ashraftraders.session.SessionManager;
import com.example.ashraftraders.ui.fragments.DashboardFragment;
import com.example.ashraftraders.utils.InternetUtil;
import com.example.ashraftraders.viewmodel.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SessionManager session;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        getWindow().setStatusBarColor(Color.parseColor("#024940"));
        session = new SessionManager(this);

        if (session.isLogin()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding.btnLogin.setOnClickListener(v -> {
            if (!InternetUtil.isConnected(this)) {

                Toast.makeText(
                        this,
                        "ইন্টারনেট সংযোগ নেই!",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            viewModel.login(
                    binding.etUsername.getText().toString().trim(), binding.etPassword.getText().toString().trim()
            );

        });

        viewModel.getLoginSuccess().observe(this, user -> {
            session.saveLogin(user.getId(), user.getUsername(), user.getRole());

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        viewModel.getError().observe(this, message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show());

    }
}