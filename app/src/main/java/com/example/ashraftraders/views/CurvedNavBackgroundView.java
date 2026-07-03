package com.example.ashraftraders.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom Bottom Navigation background with the exact premium organic curve.
 * Designed to work beautifully when 'Products' tab is shifted slightly to the right in XML.
 */
public class CurvedNavBackgroundView extends View {

    private final Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint darkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Path whitePath = new Path();
    private final Path darkPath = new Path();

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

        // Exact soft premium shadow layer
        whitePaint.setShadowLayer(25f, 0f, 10f, Color.parseColor("#1C000000"));

        // Accurate dark teal green color
        darkPaint.setColor(Color.parseColor("#003E33"));
        darkPaint.setStyle(Paint.Style.FILL);

        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        buildPaths((float) w, (float) h);
    }

    private void buildPaths(float w, float h) {
        float radius = h / 2f;

        // 1. White Pill Background
        whitePath.reset();
        whitePath.addRoundRect(0, 0, w, h, radius, radius, Path.Direction.CW);

        // 2. Beautiful Organic Dark Wave Path
        darkPath.reset();

        // Based on 5 tabs layout
        float tabWidth = w / 5f;
        float waveTailEndX = tabWidth * 1.35f;

        // Start path right after top-left corner
        darkPath.moveTo(radius, 0);

        // Top edge of Home tab
        darkPath.lineTo(tabWidth * 0.55f, 0);

        // Premium S-curve fluid transition
        float intermediateX = tabWidth * 1.02f;
        float intermediateY = h * 0.72f;

        darkPath.cubicTo(
                tabWidth * 0.82f, 0,
                tabWidth * 0.90f, h * 0.45f,
                intermediateX, intermediateY
        );

        darkPath.cubicTo(
                tabWidth * 1.10f, h * 0.90f,
                tabWidth * 1.22f, h,
                waveTailEndX, h
        );

        // Ground line closing back
        darkPath.lineTo(radius, h);

        // Bottom-left corner arc
        darkPath.arcTo(0, h - (radius * 2), radius * 2, h, 90, 90, false);

        // Left vertical line and top-left arc
        darkPath.lineTo(0, radius);
        darkPath.arcTo(0, 0, radius * 2, radius * 2, 180, 90, false);

        darkPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(whitePath, whitePaint);
        canvas.drawPath(darkPath, darkPaint);
    }
}