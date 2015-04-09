package com.dnocode.lib.business.utility;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dnocode.lib.business.R;
import com.dnocode.lib.business.utils.AppInfo;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;

public class Teacher  {

    private static Teacher INSTANCE;

    private final static String sTag="Teacher";

    private final static int sHeightDefaultLessonCard =30;

    private  Context mCtx = null;
    /** contains lesson view that allows to recycle a view if another card need it*/
    private HashMap<Integer, View> mLessonCardsViewPool;
    /** contains lesson to show linked to component event or activity name as click on button ecc  */
    private HashMap<String, LessonCard> mSourceEventLessonCard;
    private int mLastViewInflatedId=-1;

    private TeacherDefaultListener mListener=new TeacherDefaultListener();

    private Teacher(Context... context) {
        if (context != null && context.length > 0) {
            this.mCtx = context[0];
        }
    }

    /** create singleton**/
    public static Teacher instance(Context... context) {

        return INSTANCE == null ? (INSTANCE = new Teacher(context.length > 0 ? context[0] : null)) : INSTANCE;
    }

    public boolean firstStart(){  return AppInfo.checkAppStart(mCtx)== AppInfo.AppStart.FIRST_TIME;}
    /**
     * add lesson view by resource layout id
     * @param resourceLayout
     */
    public LessonCard addLessonCard(int resourceLayout,int ... linkId) { return null;}

    /** default card**/
    public LessonCard addLessonCard(String title, String description, int iconResource,String hexColor) {

        WindowManager wm = (WindowManager) mCtx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        height= (height/100)* sHeightDefaultLessonCard;

        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);

        lp.gravity= Gravity.BOTTOM;

        RelativeLayout lessonView=new RelativeLayout(mCtx);

        lessonView.setId(R.id.default_lesson_card_Id);

        lessonView.setBackgroundColor(Color.parseColor(hexColor));

        lessonView.setLayoutParams(lp);

        TextView titleView=new TextView(mCtx);

        titleView.setText(title);

        TextView descriptionView=new TextView(mCtx);

        descriptionView.setText(title);

        lessonView.addView(titleView);

        lessonView.addView(descriptionView);

       return  addLessonCard(lessonView,false);

    }

    /**
     * add view to  lessonCardsPool hashmap
     * create  by view_id  -> view or by custom linkid  -> view    *
     * @param view
     * @param linkId
     */
    public LessonCard addLessonCard(View view,boolean isTransient,int ... linkId) {

        /**on first card attach listener**/
        if (mLessonCardsViewPool == null || (mLessonCardsViewPool != null && mLessonCardsViewPool.size() == 0)) {

            ((Application) INSTANCE.mCtx.getApplicationContext()).registerActivityLifecycleCallbacks(mListener);
        }

        /**
         * the view will be insert in view pool
         *  id_view | linkid => view
         */
        if(view.getId()==View.NO_ID&&linkId.length==0){

            new InvalidParameterException("view must have id setted or indicate linkId");

            return null;
        }

        mLessonCardsViewPool = mLessonCardsViewPool ==null?new HashMap(): mLessonCardsViewPool;
        mLessonCardsViewPool.put(mLessonCardsViewPool.size() + 1, view);

        /**creating lesson card that refers to view defined upon**/
        LessonCard lessonCard=new LessonCard(mLessonCardsViewPool.size(),isTransient);

        return lessonCard;
    }

    /**
     *
     * @param activityClass
     * @return
     */
    public <T extends  Activity> LessonCard getLessonCardBy(Class<T> activityClass){   return   mSourceEventLessonCard.get(activityClass.getName()); }
     /**
     * sourceView view associated with lessoncard
     * could be Button or any kind of clickable view
     * @param sourceView
     * @return
     */
    public LessonCard getLessonCardBy(View sourceView){

        if(sourceView.getId()==View.NO_ID){

            new InvalidParameterException("view must have id setted or indicate linkid");

            return null;
        }

        return   mSourceEventLessonCard.get(sourceView.getId());
    }

    public void removeCard(View sourceView) {

        if (sourceView.getId() == View.NO_ID) {

            new InvalidParameterException("view must have id setted or indicate linkid");

            return;
        }

        removeCard(sourceView.getId() + "");
    }

    public void removeCard(Class<Activity> activityClass) {removeCard(activityClass.getName()); }

    public void removeCard(String key){

        mSourceEventLessonCard.remove(key);

        if ( mSourceEventLessonCard.size() == 0) {

            ((Application) INSTANCE.mCtx.getApplicationContext()).unregisterActivityLifecycleCallbacks(mListener);
        }
    }


    private View injectInDefaultFrame(View child){

        FrameLayout mainTutorialWrapper=new FrameLayout(child.getContext());

        if(child.getLayoutParams() instanceof  FrameLayout.LayoutParams){

            FrameLayout.LayoutParams layoutParamsChild= (FrameLayout.LayoutParams) child.getLayoutParams();

            FrameLayout.LayoutParams layoutParamsCloned=new FrameLayout.LayoutParams((ViewGroup.MarginLayoutParams)layoutParamsChild);

            layoutParamsCloned.gravity=layoutParamsChild.gravity;

            mainTutorialWrapper.setLayoutParams(layoutParamsCloned);


         /*   layoutParamsChild.width= ViewGroup.LayoutParams.MATCH_PARENT;

            layoutParamsChild.height= ViewGroup.LayoutParams.MATCH_PARENT;*/
        }

        mainTutorialWrapper.setId(R.id.main_wrapper_lesson_card_id);



        if(mLastViewInflatedId==child.getId()){

            View view=((Activity) mCtx).findViewById(R.id.main_wrapper_lesson_card_id);

            view=view==null?child:view;

            if(view!=null&&view.getParent()!=null) ((ViewGroup)view.getParent()).removeView(view);
        }

        mLastViewInflatedId=child.getId();

        mainTutorialWrapper.removeAllViews();

        mainTutorialWrapper.addView(child);

        return mainTutorialWrapper;
    }

    private void showLesson(LessonCard lessonCard,Activity activity,String lessonAnchor){

        if(lessonCard==null){return;}
        //check dependencies
        for (String dependencyLessonCardAnchor : lessonCard.mLessonCardsDependencies){

            if(mSourceEventLessonCard.containsKey(dependencyLessonCardAnchor)){return;}
        }
        FrameLayout rootLayout = (FrameLayout)activity.findViewById(android.R.id.content);

        rootLayout.addView(injectInDefaultFrame(lessonCard.getView()));

        lessonCard.mTimes--;

        if(lessonCard.mTimes==0){  removeCard(lessonAnchor);    }
        //persist on finish

    }


    private  class TeacherDefaultListener implements Application.ActivityLifecycleCallbacks,View.OnClickListener {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
      mCtx=activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {

        LessonCard lessonCard=mSourceEventLessonCard.get(activity.getClass().getName());

        showLesson(lessonCard,activity,activity.getClass().getName());
    }

    @Override
    public void onActivityResumed(Activity activity) {}
    @Override
    public void onActivityPaused(Activity activity) {}
    @Override
    public void onActivityStopped(Activity activity) {

    }
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }
    @Override
    public void onActivityDestroyed(Activity activity) { }

    @Override
    public void onClick(View v) {

            LessonCard lessonCard=mSourceEventLessonCard.get(v.getId());

            showLesson(lessonCard, (Activity) v.getContext(),v.getId()+"");
        }
    }



    public  interface LessonCardListener{

        void  onActionClick(LessonCard lessonCard);

        void  onCloseClick(LessonCard lessonCard);
    }

    /**
     *  model that rappresents
     *  the view and rules
     *  to show
     */
    public class LessonCard implements Serializable{

        private transient int mCardViewLink=-1;
        private int mTimes=1;
        private boolean mIsTransient=false;
        private final ArrayList<String> mLessonCardsDependencies=new ArrayList<String>();

        public LessonCard(int lessonViewAnchor,boolean isTransient){

            this.mCardViewLink=lessonViewAnchor;

            this.mIsTransient=isTransient;
        }

        /**
         * means that this the lesson card view has to be showed
         * on activity start
         * @param activityClazz
         * @return
         */
        public  <T extends  Activity> LessonCard showOnActivityStart(Class<T> activityClazz){

            mSourceEventLessonCard=mSourceEventLessonCard==null?new HashMap<String,LessonCard>():mSourceEventLessonCard;

            mSourceEventLessonCard.put(activityClazz.getName(),this);

            return this;

        }

        /**
         * means that this lesson card view has to be showed
         * on button click
         * @param view
         * @return
         */
        public LessonCard showWhenClickOn(View view){

            if(view.getId()==View.NO_ID){

                Log.e(sTag,"view must have id setted");

                return this;
            }

            view.setClickable(true);

            view.setOnClickListener(mListener);

            mSourceEventLessonCard=mSourceEventLessonCard==null?new HashMap<String,LessonCard>():mSourceEventLessonCard;

            mSourceEventLessonCard.put(view.getId()+"", this);

            return this;
        }


        /**
         *number of times that the lesson has to be repeated
         * @param times
         * @return
         */
        public LessonCard executeFor(int times){

            this.mTimes=times;

            return this;
        }

        private LessonCard addDependency(String cardId){

            mLessonCardsDependencies.add(cardId);

            return this;

        }




        public <T extends  Activity>LessonCard addDependency(Class<T> activityClass){

            mLessonCardsDependencies.add(activityClass.getName());

            return this;
        }

        public  LessonCard addDependency(View view){

            mLessonCardsDependencies.add(view.getId()+"");

            return this;

        }


        public View getView(){ return mLessonCardsViewPool.get(mCardViewLink); }

    }
}
