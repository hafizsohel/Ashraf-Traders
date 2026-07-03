package com.example.ashraftraders.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomBottomBar extends View {

    private Paint whitePaint;
    private Paint greenPaint;
    private Paint shadowPaint;

    private Path whitePath;
    private Path greenPath;

    private float w;
    private float h;

    public CustomBottomBar(Context context) {
        super(context);
        init();
    }

    public CustomBottomBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomBottomBar(Context context,
                           @Nullable AttributeSet attrs,
                           int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setColor(Color.WHITE);

        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setColor(Color.WHITE);
        shadowPaint.setShadowLayer(
                24f,
                0f,
                6f,
                0x33000000
        );

        greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        greenPaint.setStyle(Paint.Style.FILL);
        greenPaint.setColor(Color.parseColor("#004D40"));
        greenPaint.setPathEffect(new CornerPathEffect(60));

        whitePath = new Path();
        greenPath = new Path();
    }

    @Override
    protected void onSizeChanged(int width,
                                 int height,
                                 int oldw,
                                 int oldh) {

        super.onSizeChanged(width, height, oldw, oldh);

        w = width;
        h = height;

        buildPaths();
    }

    private void buildPaths() {

        whitePath.reset();
        greenPath.reset();

        // পরের অংশে এখানে Bezier curve যোগ হবে
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        canvas.drawPath(whitePath, shadowPaint);
        canvas.drawPath(whitePath, whitePaint);

        canvas.drawPath(greenPath, greenPaint);
    }

}