package com.example.learnandroidui.customcomponents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class PhotoSpiral extends ViewGroup {
    public PhotoSpiral(Context context) {
        super(context);
    }

    public PhotoSpiral(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        View firstChild = getChildAt(0);
        int size = firstChild.getMeasuredWidth() + firstChild.getMeasuredHeight();
        int width = resolveSize(size, widthMeasureSpec);
        int height = resolveSize(size, heightMeasureSpec);

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View firstChild = getChildAt(0);
        final int childWidth = firstChild.getMeasuredWidth();
        final int childHeight = firstChild.getMeasuredHeight();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int x = 0, y = 0;
            switch(i) {
                case 1:
                    x = childWidth;
                    y = 0;
                    break;
                case 2:
                    x = childHeight;
                    y = childWidth;
                    break;
                case 3:
                    x = 0;
                    y = childHeight;
                    break;
            }
            child.layout(x,y,x + child.getMeasuredWidth(), y+ child.getMeasuredHeight());
        }
    }
}
