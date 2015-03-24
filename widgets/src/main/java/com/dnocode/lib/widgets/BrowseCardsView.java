package com.dnocode.lib.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dnocode.lib.business.ext.picasso.PicassoRoundTransform;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;



/**
 * Created by dino on 23/03/15.
 */
public class BrowseCardsView  extends FrameLayout{

    int mTranformationRadius=-1;
    int mTransformationMargin=-1;
    int mMarginUncovered=10;
    int mCardheight=60;
    int mCardWidth=60;
    boolean mBrowseLeft=true;




    public BrowseCardsView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
    }

    public BrowseCardsView(Context context, AttributeSet attrs) throws IOException {
        super(context, attrs);

         mMarginUncovered= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,mMarginUncovered,context.getResources().getDisplayMetrics());
         mCardheight=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,mCardheight,context.getResources().getDisplayMetrics());
         mCardWidth=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,mCardWidth,context.getResources().getDisplayMetrics());
    }

    public void addCard(String url) throws IOException {



        FrameLayout.LayoutParams lp=new LayoutParams(mCardWidth, mCardheight);

        ImageView iv=new ImageView(getContext());

        iv.setScaleType(ImageView.ScaleType.FIT_XY);

        iv.setLayoutParams(lp);


        if(mBrowseLeft){

            lp.leftMargin=getChildCount()*mMarginUncovered;

        }else{

            int newSize=getChildCount();

            for(int i=0 ;i<getChildCount();i++){

                ((LayoutParams)getChildAt(i).getLayoutParams()).leftMargin=newSize*mMarginUncovered;

                newSize--;
            }

            //lp.leftMargin=newSize*mMarginUncovered;

        }

        addView(iv);


        RequestCreator rc= Picasso.with(getContext()).load(url).error(R.mipmap.ic_launcher);

        boolean wantsTransformation=mTranformationRadius!=-1&&mTranformationRadius!=-1;

        if(wantsTransformation) rc.transform(new PicassoRoundTransform(mTranformationRadius,mTransformationMargin));

        rc.into(iv);
    }


    public void applyTransformation(final int radius, final int margin){

        this.mTranformationRadius=radius;
        this.mTransformationMargin=margin;
    }


    @Override
    public void requestLayout() {
        Log.i("DINO","requestLayout width"+  getWidth());
        super.requestLayout();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i("DINO","onMeasure mode"+  MeasureSpec.getMode(widthMeasureSpec));
        Log.i("DINO","onMeasure size"+     MeasureSpec.getSize(widthMeasureSpec));


       super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("DINO","onDraw"+  getWidth());
        super.onDraw(canvas);
    }




    public static class PipeAnimation extends Animation {

    }
}
