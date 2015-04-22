package com.dnocode.lib.widgets.images;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.dnocode.lib.business.ext.picasso.CircleTransform;
import com.dnocode.lib.business.ext.picasso.CropSquareTransformation;
import com.dnocode.lib.widgets.R;
import com.dnocode.lib.widgets.enums.TransformationType;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by dnocode on 02/04/15.
 */
public class SingleSlideView extends ImageView {


    private final ArrayList<RequestCreator> mRequestCreatorList=new ArrayList();
    private  int mCurrentRc =0;
    private  static Animation.AnimationListener sAnimatorListener;
    private static Callback sPicassoCallback;
    private static  AlphaAnimation sAnimationFadeIn,sAnimationFadeOut;
    public int active =1500;
    public int duration =800;
    public TransformationType transformationType;
    public int drawableError=-1;

    public SingleSlideView(Context context) {
        super(context, null, 0);
    }

    public SingleSlideView(Context context,AttributeSet attrs) {
        super(context,attrs,0);
    }

    public SingleSlideView(Context context,AttributeSet attrs,int defStyle) {
        super(context,attrs,defStyle);
        init(attrs);
    }

    @Override
    protected void onAttachedToWindow() {super.onAttachedToWindow();}

    @Override
    protected void onDetachedFromWindow() {
       reset();
      super.onDetachedFromWindow();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.currentImageIndex=mCurrentRc;
        return ss;
    }


    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mCurrentRc=ss.currentImageIndex;
    }


    public void init(AttributeSet attrs){

        TypedArray a = getContext().getTheme().obtainStyledAttributes( attrs, R.styleable.SingleSlideView,0, 0);
        try {

            duration = a.getInt(R.styleable.SingleSlideView_android_duration,1000);
             int valuesResId = a.getResourceId(R.styleable.SingleSlideView_imagesEntries, -1);

            if (valuesResId != -1) {
                int[] values = a.getResources().getIntArray(valuesResId);
               for (int ref:values){addSlide(ref);}
            }
            transformationType = TransformationType.fromOrdinal(a.getInt(R.styleable.SingleSlideView_transform, TransformationType.empty.ordinal()));
            if(transformationType != TransformationType.empty){}

        } finally {
            a.recycle();
        }

    }


    public static class SavedState extends BaseSavedState {
        int currentImageIndex;

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentImageIndex);
        }

        @SuppressWarnings("hiding")
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        private SavedState(Parcel in) {
            super(in);
            currentImageIndex = in.readInt();

        }
    }
    @SuppressLint("NewApi")
        private void imageSliding(){

        if(mRequestCreatorList.isEmpty()){return;}
        if (sAnimatorListener == null) { sAnimatorListener =new SingleSlideAnimationListener(); }
        if(sAnimationFadeOut==null) {
            sAnimationFadeOut = new AlphaAnimation(1f, 0f);
            sAnimationFadeOut.setDuration(duration / 2);
            sAnimationFadeOut.setStartOffset(active);
            sAnimationFadeIn = new AlphaAnimation(0f, 1f);
            sAnimationFadeIn.setDuration(duration / 2);
            sAnimationFadeIn.setAnimationListener(sAnimatorListener);
            sAnimationFadeOut.setAnimationListener(sAnimatorListener);
        }
        if(sPicassoCallback==null){
            sPicassoCallback=new Callback() {
                @SuppressLint("NewApi")
                @Override
                public void onSuccess() {
                   if(mRequestCreatorList.size()>1){ startAnimation(sAnimationFadeIn); }
                    mCurrentRc = mCurrentRc ==mRequestCreatorList.size()-1?0: mCurrentRc +1;
                }
                @Override
                public void onError() { Log.e(getClass().getSimpleName(),"error on image loading");}
            };
        }

         mRequestCreatorList.get(mCurrentRc).into(this,sPicassoCallback );
    }


    private void reset(){

        sAnimatorListener=null;
        sAnimationFadeIn=null;
        sAnimationFadeOut=null;
        sPicassoCallback=null;

    }
    public void clear(){

        mRequestCreatorList.clear();
        mCurrentRc =0;
    }

   public void addSlide(String ... url ) {

      for(String uri : url) {

          RequestCreator requestCreator = Picasso.with(getContext()).load(uri);
          addSlide(requestCreator);
      }
    }

    public void addSlide(int resource ) {

        RequestCreator requestCreator= Picasso.with(getContext()).load(resource);
        addSlide(requestCreator);
    }


    public void  addSlide(RequestCreator  rc)  {

       reset();
        rc.error(drawableError);
        Transformation cTransformation=null;
        if(transformationType!=null&&transformationType != TransformationType.empty)
        {
            switch (transformationType){
                case circle:
                    cTransformation=new CircleTransform();
                    break;
                case square:
                    cTransformation=new CropSquareTransformation();
                    break;
            }
         }
        if(cTransformation!=null) {rc.transform(cTransformation);}
        mRequestCreatorList.add(rc);
        imageSliding();

    }


     class SingleSlideAnimationListener implements Animation.AnimationListener{


         @Override
         public void onAnimationStart(Animation animation) {}

         @Override
         public void onAnimationEnd(Animation animation) {

                    if(animation==sAnimationFadeIn){
                        startAnimation(sAnimationFadeOut);
                    }else{
                       imageSliding();
                    }
        }

         @Override
         public void onAnimationRepeat(Animation animation) {}
     }


}
