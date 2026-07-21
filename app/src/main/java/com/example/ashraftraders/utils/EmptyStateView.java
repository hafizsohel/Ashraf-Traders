package com.example.ashraftraders.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.example.ashraftraders.R;
import com.example.ashraftraders.databinding.ViewEmptyStateBinding;

public class EmptyStateView extends FrameLayout {

    private final ViewEmptyStateBinding binding;

    public EmptyStateView(Context context) {
        this(context, null);
    }

    public EmptyStateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyStateView(Context context,
                          @Nullable AttributeSet attrs,
                          int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        binding = ViewEmptyStateBinding.inflate(
                LayoutInflater.from(context),
                this,
                true
        );

        if (attrs != null) {

            TypedArray a = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.EmptyStateView
            );

            int icon = a.getResourceId(
                    R.styleable.EmptyStateView_stateIcon,
                    R.drawable.ic_no_internet
            );

            String title = a.getString(
                    R.styleable.EmptyStateView_stateTitle
            );

            String message = a.getString(
                    R.styleable.EmptyStateView_stateMessage
            );

            String button = a.getString(
                    R.styleable.EmptyStateView_buttonText
            );

            binding.imgState.setImageResource(icon);

            if (title != null)
                binding.txtTitle.setText(title);

            if (message != null)
                binding.txtMessage.setText(message);

            if (button != null)
                binding.btnAction.setText(button);

            a.recycle();
        }

        hide();
    }

    public void setOnRetryClick(OnClickListener listener) {
        binding.btnAction.setOnClickListener(listener);
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }

    public void showLoading() {

        show();

        binding.progressBar.setVisibility(VISIBLE);
        binding.imgState.setVisibility(GONE);

        binding.txtTitle.setText("লোড হচ্ছে...");
        binding.txtMessage.setText("অনুগ্রহ করে অপেক্ষা করুন");

        binding.btnAction.setVisibility(GONE);
    }

    public void showState(@DrawableRes int image,
                          String title,
                          String message,
                          String buttonText) {

        show();

        binding.progressBar.setVisibility(GONE);

        binding.imgState.setVisibility(VISIBLE);
        binding.btnAction.setVisibility(VISIBLE);

        binding.imgState.setImageResource(image);
        binding.txtTitle.setText(title);
        binding.txtMessage.setText(message);
        binding.btnAction.setText(buttonText);
    }

    public void showNoInternet(OnClickListener retryListener) {

        showState(
                R.drawable.ic_no_internet,
                "ইন্টারনেট সংযোগ নেই",
                "ইন্টারনেট সংযোগ পরীক্ষা করে আবার চেষ্টা করুন।",
                "আবার চেষ্টা করুন"
        );

        setOnRetryClick(retryListener);
    }

    public void showNoData(@DrawableRes int image,
                           String title,
                           String message) {

        showState(
                image,
                title,
                message,
                ""
        );

        binding.btnAction.setVisibility(GONE);
    }

    public void showError(String title,
                          String message,
                          OnClickListener retryListener) {

        showState(
                R.drawable.ic_error,
                title,
                message,
                "আবার চেষ্টা করুন"
        );

        setOnRetryClick(retryListener);
    }

}