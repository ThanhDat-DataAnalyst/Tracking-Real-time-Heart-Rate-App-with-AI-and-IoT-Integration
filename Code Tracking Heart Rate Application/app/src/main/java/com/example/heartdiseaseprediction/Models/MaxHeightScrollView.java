package com.example.heartdiseaseprediction.Models;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.example.heartdiseaseprediction.R;

public class MaxHeightScrollView extends ScrollView {
    private int maxHeight;

    public MaxHeightScrollView(Context context) {
        this(context, null);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray styledAttrs =
                context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView);
        try {
            maxHeight = styledAttrs.getDimensionPixelSize(R.styleable.MaxHeightScrollView_mhs_maxHeight, 0);
        } finally {
            styledAttrs.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (maxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}