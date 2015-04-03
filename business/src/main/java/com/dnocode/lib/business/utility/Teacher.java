package com.dnocode.lib.business.utility;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.HashMap;


public class Teacher  {

    private static Teacher INSTANCE;

    private  Context mCtx = null;
    /** contains lesson view to show more lesson*/
    private HashMap<Integer, View> mLessonCardsViewPool;
    /** contains lesson to show linked to component event or activity name as click on button ecc  */
    private HashMap<String, LessonCard> mSourceEventLessonCard;

    private TeacherDefaultListener mListener=new TeacherDefaultListener();

    private Teacher(Context... context) {
        if (context != null && context.length > 0) {
            this.mCtx = context[0];
        }
    }

    /** create singleton**/
    public static Teacher addLesson(Context... context) {

        return INSTANCE == null ? (INSTANCE = new Teacher(context.length > 0 ? context[0] : null)) : INSTANCE;
    }


    /**
     * add lesson view by resource layout id
     * @param resourceLayout
     */
    public void card(int resourceLayout,int ... linkId) {

        //todo
    }

    /** default card**/
    public void card(String title, String description, int iconResource,String hexColor,int ... linkId) {

        //todo
    }

    /**
     * add view to  lessonCardsPool liked by view id or by linkId if is indicated
     *
     * @param view
     * @param linkId
     */
    public void card(View view,boolean isTransient,int ... linkId) {

        if (mLessonCardsViewPool == null || (mLessonCardsViewPool != null && mLessonCardsViewPool.size() > 0)) {

            ((Application) INSTANCE.mCtx.getApplicationContext()).registerActivityLifecycleCallbacks(mListener);
        }

        if(view.getId()==View.NO_ID&&linkId.length==0){new InvalidParameterException("view must have id setted or indicate linkid"); return;}

        mLessonCardsViewPool = mLessonCardsViewPool ==null?new HashMap(): mLessonCardsViewPool;

        mLessonCardsViewPool.put(view.getId(), view);

        //create  lesson card and add   save on preference

    }


    private void removeCard(View card) {

        mLessonCardsViewPool.remove(card.getId());

        if (mLessonCardsViewPool == null || (mLessonCardsViewPool != null && mLessonCardsViewPool.size() == 0)) {

            ((Application) INSTANCE.mCtx.getApplicationContext()).unregisterActivityLifecycleCallbacks(mListener);
        }


    }


    private  class TeacherDefaultListener implements Application.ActivityLifecycleCallbacks,View.OnClickListener {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

      mCtx=activity;

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

        @Override
        public void onClick(View v) {

        }
    }






    private void showLesson(LessonCard lv){

        /**checks whether the dependencies are satisfied**/
        //and execute it

    }


    /**
     *  model that rappresent
     *  the view and rules
     *  to show
     */
    private class LessonCard implements Serializable{

        private int mCardViewLink;
        private int mTimes=1;
        private boolean mIsTransient=false;
        private int[] mCardsDependencies;


        public LessonCard(View lessonFace,int times,boolean isTransient,int ... lessonDependencies){

            this.mCardViewLink=lessonFace.getId();
            this.mTimes=times;
            this.mIsTransient=isTransient;
            this.mCardsDependencies=lessonDependencies;

        }


        public LessonCard showWhenStart(Activity activity){
        //todo

         return this;
        }
        public LessonCard showWhenClickOn(View view){//todo
            return this;

         }



        public LessonCard executeFor(int times){


            return this;

        }
        /**
         * means that this LessonCard depend of indicated lesson
         * @param cardId
         * @return
         */
        public LessonCard addDependency(int cardId){return this;}


        public View getView(){ return mLessonCardsViewPool.get(mCardViewLink);}




    }
}
