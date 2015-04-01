package com.dnocode.lib.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by dino on 01/04/15.
 */
public class ShaoeSelectorView extends View {

    private int shapeColor;
    private boolean displayShapeName;
    private int shapeWidth = 100;
    private int shapeHeight = 100;
    private int textXOffset = 0;
    private int textYOffset = 30;
    private Paint paintShape;



    public ShaoeSelectorView(Context context) {
        this(context, null, 0);
    }

    public ShaoeSelectorView(Context context,AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShaoeSelectorView(Context context,AttributeSet attrs,int defStyle) {

        super(context,attrs,defStyle);

        init(attrs);

        setupPaint();
    }


    private void init(AttributeSet attrs){

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ShapeSelectorView, 0, 0);

        try {

             shapeColor = a.getColor(R.styleable.ShapeSelectorView_shapeColor, Color.BLACK);
             displayShapeName = a.getBoolean(R.styleable.ShapeSelectorView_displayShapeName, false);
        } finally {
            // TypedArray objects are shared and must be recycled.
            a.recycle();
        }


    }


    public int getShapeColor() {return shapeColor;}


    public void setDisplayingShapeName(boolean state) {
        this.displayShapeName = state;
        invalidate();
        requestLayout();
    }

    public void setShapeColor(int color) {
        this.shapeColor = color;
        invalidate();
        requestLayout();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, shapeWidth, shapeHeight, paintShape);

        if (displayShapeName) {
            canvas.drawText("Square", shapeWidth + textXOffset, shapeHeight + textXOffset, paintShape);
        }
    }

    private void setupPaint() {
        paintShape = new Paint();
        paintShape.setStyle(Paint.Style.FILL);
        paintShape.setColor(shapeColor);
        paintShape.setTextSize(30);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.i("root", "misurazione del figlio");
        // Defines the extra padding for the shape name text
        int textPadding = 10;
        int contentWidth = shapeWidth;

        // Resolve the width based on our minimum and the measure spec
        int minw = contentWidth + getPaddingLeft() + getPaddingRight();

        int w = resolveSizeAndState(minw, widthMeasureSpec, 0);

        // Ask for a height that would let the view get as big as it can
        int minh = shapeHeight + getPaddingBottom() + getPaddingTop();
        if (displayShapeName) {
            minh += textYOffset + textPadding;
        }
        int h = resolveSizeAndState(minh, heightMeasureSpec, 0);

        // Calling this method determines the measured width and height
        // Retrieve with getMeasuredWidth or getMeasuredHeight methods later
        setMeasuredDimension(w, h);
    }




}
