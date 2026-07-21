package com.example.ashraftraders.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class CurvedNavBackgroundView extends View {

    private final Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint tealPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Path whitePath = new Path();
    private final Path homeWavePath = new Path();
    private final Path centerMenuPath = new Path();

    public CurvedNavBackgroundView(Context context) {
        super(context);
        init();
    }

    public CurvedNavBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurvedNavBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setShadowLayer(25f, 0f, 10f, Color.parseColor("#0B6A5D"));

        tealPaint.setColor(Color.parseColor("#0B6A5D"));
        tealPaint.setStyle(Paint.Style.FILL);

        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        buildPaths((float) w, (float) h);
    }

    private void buildPaths(float w, float h) {
        float radius = h / 2f;

        whitePath.reset();
        whitePath.addRoundRect(0, 0, w, h, radius, radius, Path.Direction.CW);
        homeWavePath.reset();
        float tabWidth = w / 5f;
        float waveTailEndX = tabWidth * 1.35f;

        homeWavePath.moveTo(radius, 0);
        homeWavePath.lineTo(tabWidth * 0.55f, 0);

        float intermediateX = tabWidth * 1.02f;
        float intermediateY = h * 0.72f;

        homeWavePath.cubicTo(
                tabWidth * 0.82f, 0,
                tabWidth * 0.90f, h * 0.45f,
                intermediateX, intermediateY
        );

        homeWavePath.cubicTo(
                tabWidth * 1.10f, h * 0.90f,
                tabWidth * 1.22f, h,
                waveTailEndX, h
        );

        homeWavePath.lineTo(radius, h);
        homeWavePath.arcTo(0, h - (radius * 2), radius * 2, h, 90, 90, false);
        homeWavePath.lineTo(0, radius);
        homeWavePath.arcTo(0, 0, radius * 2, radius * 2, 180, 90, false);
        homeWavePath.close();

        // মাঝখানের শেপটিকে একদম গোল (Perfect Circle) করার অংশ
        centerMenuPath.reset();
        float centerX = w / 2f;
        float centerY = h / 2f;

        // ভিউ এর হাইটের ওপর ভিত্তি করে বৃত্তের ব্যাসার্ধ (Radius) নির্ধারণ
        float circleRadius = (h * 0.72f) / 2f;

        // বৃত্ত আঁকার জন্য addCircle ব্যবহার করা হয়েছে
        centerMenuPath.addCircle(centerX, centerY, circleRadius, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(whitePath, whitePaint);
        canvas.drawPath(homeWavePath, tealPaint);
        canvas.drawPath(centerMenuPath, tealPaint);
    }
}