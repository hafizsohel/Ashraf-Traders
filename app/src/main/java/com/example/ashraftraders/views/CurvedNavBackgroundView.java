package com.example.ashraftraders.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CurvedNavBackgroundView extends View {

    private final Path path = new Path();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CurvedNavBackgroundView(@NonNull Context context) {
        super(context);
        init();
    }

    public CurvedNavBackgroundView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurvedNavBackgroundView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);

        // -------------------------------------------------------------
        // কালো ব্যাকগ্রাউন্ড সরিয়ে একদম পরিষ্কার সাদা (Pure White) কালার দেওয়া হলো
        // -------------------------------------------------------------
      //  paint.setColor(Color.parseColor(android.R.color.background_dark));

        // অথবা চাইলে খুব হালকা অফ-হোয়াইট বা স্নো হোয়াইট ব্যবহার করতে পারেন:
         paint.setColor(Color.parseColor("#0B6A5D"));
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        path.reset();

        float w = getWidth();
        float h = getHeight();
        float center = w / 2f;
        float radius = 38f * getResources().getDisplayMetrics().density; // মাঝের কাট-আউটের রেডিয়াস

        // নেভিগেশন বারের আউটার শেপ ড্র করা
        path.moveTo(0, 0);
        path.lineTo(center - radius, 0);

        // মাঝখানের কার্ভ (ফ্লোটিং বাটনের জন্য খাঁজ)
        path.cubicTo(
                center - (radius / 2f), 0,
                center - (radius / 2f), radius,
                center, radius
        );
        path.cubicTo(
                center + (radius / 2f), radius,
                center + (radius / 2f), 0,
                center + radius, 0
        );

        path.lineTo(w, 0);
        path.lineTo(w, h);
        path.lineTo(0, h);
        path.close();

        canvas.drawPath(path, paint);
    }
}