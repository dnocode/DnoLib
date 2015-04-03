package com.dnocode.lib.widgets.images;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.dnocode.lib.business.ext.picasso.CircleTransform;
import com.dnocode.lib.widgets.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.IOException;

/**
 * component that browse images as cards deck
 */
public class BrowseCardsView  extends FrameLayout implements View.OnClickListener{


    int errorResource= R.mipmap.ic_launcher;
    //overlapping size
    int mMarginUncovered=10;
    //default card width
    int mCardheight=60;
    //default card height
    int mCardWidth=60;
    //overlapping side
    boolean mBrowseLeft=true;
    boolean mCircularTransformation=true;
    Transformation mTransformation=null;


    public BrowseCardsView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
    }

    public BrowseCardsView(Context context, AttributeSet attrs) throws IOException {
        super(context, attrs);

         mMarginUncovered= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,mMarginUncovered,context.getResources().getDisplayMetrics());
         mCardheight=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,mCardheight,context.getResources().getDisplayMetrics());
         mCardWidth=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,mCardWidth,context.getResources().getDisplayMetrics());

        setOnClickListener(this);
    }

    public void addCard(String url) throws IOException {


        FrameLayout.LayoutParams lp=new LayoutParams(mCardWidth, mCardheight);

        final ImageView iv=new ImageView(getContext());

        iv.setScaleType(ImageView.ScaleType.FIT_XY);

        iv.setLayoutParams(lp);

        if(mBrowseLeft){  lp.leftMargin=getChildCount()*mMarginUncovered; }
         else{

            int newSize=getChildCount();

            for(int i=0 ;i<getChildCount();i++){

                ((LayoutParams)getChildAt(i).getLayoutParams()).leftMargin=newSize*mMarginUncovered;

                newSize--;
            }
        }

        addView(iv);

        RequestCreator rc= Picasso.with(getContext()).load(url).error(errorResource);
        if(mCircularTransformation){mTransformation=new CircleTransform();}
        if(mTransformation!=null) rc.transform(mTransformation);

        rc.into(iv);
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


    @Override
    public void onClick(View v) {


        if(getChildCount()>0){

            View view=getChildAt(getChildCount()-1);


            getLayoutParams().width=mCardWidth*2;

            view.animate().translationX(mCardWidth-mMarginUncovered)
                    .setDuration(500)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {


                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

invalidate();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).start();


        }

    }
}
