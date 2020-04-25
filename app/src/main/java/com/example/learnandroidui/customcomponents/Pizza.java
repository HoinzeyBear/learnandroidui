package com.example.learnandroidui.customcomponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.learnandroidui.R;

public class Pizza extends View {

    private Paint paint;
    private int numWedges = 5;
    private int strokeWidth = 5;
    private int colorId = Color.RED;

    public Pizza(Context context) {
        super(context);
        init(context, null);
    }

    public Pizza(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Pizza(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        if(attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Pizza);
            strokeWidth = array.getDimensionPixelSize(R.styleable.Pizza_stroke_width, strokeWidth);
            numWedges = array.getInteger(R.styleable.Pizza_num_wedges, numWedges);
            colorId = array.getColor(R.styleable.Pizza_color, colorId);
            array.recycle();
        }

        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(colorId);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int cx = width / 2 + getPaddingLeft();
        int cy = height / 2 + getPaddingTop();
        float radius = (Math.min(cx,cy) - paint.getStrokeWidth()) / 2;

        canvas.drawCircle(cx, cy, radius, paint);
        drawPizzaSlices(canvas,cx,cy,radius);
    }

    private void drawPizzaSlices(Canvas canvas, float cx, float cy, float radius){
        final float degrees = 360f / numWedges;
        canvas.save();
        for (int i = 0; i < numWedges; i++) {
            canvas.rotate(degrees, cx, cy);
            canvas.drawLine(cx ,cy ,cx , cy-radius, paint);
        }
        canvas.restore();
    }
}
