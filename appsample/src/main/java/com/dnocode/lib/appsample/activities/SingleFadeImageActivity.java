package com.dnocode.lib.appsample.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.dnocode.lib.appsample.R;
import com.dnocode.lib.widgets.enums.TransformationType;
import com.dnocode.lib.widgets.images.SingleSlideView;

/**
 * Created by dnocode on 02/04/15.
 */
public class SingleFadeImageActivity extends Activity {


    private static int sHeightWidth=0;
    private SingleSlideView mSlideSingleView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FrameLayout fl=new FrameLayout(this);
        mSlideSingleView =new SingleSlideView(this);
        mSlideSingleView.transformationType= TransformationType.circle;
        sHeightWidth= sHeightWidth==0? (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()) :sHeightWidth;
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(sHeightWidth, sHeightWidth, Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        mSlideSingleView.setLayoutParams(lp);
        fl.addView(mSlideSingleView);
        setContentView(fl);

    }


    @Override
    public void onAttachedToWindow() {

        super.onAttachedToWindow();
        String [] imagesUri=this.getResources().getStringArray(R.array.images_uri);
        mSlideSingleView.addSlide(imagesUri);
    }
}
