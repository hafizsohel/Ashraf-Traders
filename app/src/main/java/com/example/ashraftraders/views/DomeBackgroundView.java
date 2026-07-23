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

public class DomeBackgroundView extends View {

    private final Path path = new Path();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DomeBackgroundView(@NonNull Context context) {
        super(context);
        init();
    }

    public DomeBackgroundView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DomeBackgroundView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#EAF0F0")); // সাদা/হালকা গ্রে ব্যাকগ্রাউন্ড
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        path.reset();

        float w = getWidth();
        float h = getHeight();

        // বাম মাথা থেকে শুরু
        path.moveTo(0, h);

        // আরও বড় ও উঁচুমুখী কার্ভ তৈরি (h * 0.02f দিয়ে কার্ভটি উপরে বিস্তৃত করা হয়েছে)
        path.cubicTo(
                0, h * 0.02f,
                w, h * 0.02f,
                w, h
        );

        path.lineTo(0, h);
        path.close();

        canvas.drawPath(path, paint);
    }
}