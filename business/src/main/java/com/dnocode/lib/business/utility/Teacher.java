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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dnocode.lib.business.R;
import com.dnocode.lib.business.utils.ObjectSerializer;
import com.google.gson.Gson;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

public class Teacher {

    private static Teacher INSTANCE;

    private final static String sTag = "Teacher";
    private final static int sHeightDefaultLessonCard = 45;
    private final static String sStoreKeySourceEventLessonCard = "SK_SE_LC";
    private final static String sStoreKeyLessonCardsViewReferences = "SK_LC_VR";
    private final static String sTutorialComplete = "T_C";
    private final static String sVoidCollision = "v_C";

    private Context mCtx = null;
    /**
     * contains lesson view that allows to recycle a view if another card need it
     */
    private HashMap<Integer, View> mLessonCardsViewPool;
    /**
     * contains references counter beetween view and lesson card*
     */
    private HashMap<String, Double> mLessonCardsTimesCounter;
    /**
     * contains  counter beetween lessoncard times to show*
     */
    private HashMap<String, Double> mLessonCardsViewReferences;
    /**
     * contains lesson to show linked to component event or activity name as click on button ecc
     */
    private HashMap<String, LessonCard> mSourceEventLessonCard;
    private int mLastViewInflatedId = -1;
    private TeacherDefaultListener mListener = new TeacherDefaultListener();
    private Boolean mTutorialComplete = false;

    private Teacher(Context context) {
        mCtx = context;
        deserializeIt();
    }
    /**
     * create singleton*
     */
    public static Teacher instance(Context... context) {
        return INSTANCE == null ? (INSTANCE = new Teacher(context.length > 0 ? context[0] : null)) : INSTANCE;
    }

    /**
     * show lesson that corresponding to that key
     * @param key
     */
    public void fireLessonKey(String key) { showLesson(getLessonCardBy(key + sVoidCollision), key + sVoidCollision);}

    public LessonCard getLessonCardBy(String cardKey) {return mSourceEventLessonCard.get(cardKey);}

    /**
     * add lesson view by resource layout id
     *
     * @param resourceLayout
     */
    public <T> LessonCard addLessonCard(int resourceLayout, T objectArgs, LessonCardViewRecyclerListener listener) {
        /**if isn`t the first execution for this view check if the references are all consumed if is it exit**/

        //todo
        return null;
    }

    public boolean areLessonsLearned() {return this.mTutorialComplete;}
    /** default card**/
    /**
     * todo possibilita` di passare piu` argomenti e per ogni esecuzione nella stessa view aggiornarli*
     */

    public LessonCard addLessonCard(LessonArgs ... args) {  return addLessonCard( R.style.TeachActionTextView, R.style.TeachDescriptionTextView,args); }

    public LessonCard addLessonCard( int actionStyleId, int descStyleId,LessonArgs ... args) {

        Log.v(sTag, "default lesson card adding");
        /**if isn`t the first execution for this view check if the references are all consumed if is it exit**/
        mLessonCardsViewReferences = mLessonCardsViewReferences == null ? new HashMap() : mLessonCardsViewReferences;
        mLessonCardsTimesCounter = mLessonCardsTimesCounter == null ? new HashMap() : mLessonCardsTimesCounter;

        /**check if all lessonCard relate  to this view are showed  if it`s so return empty card **/
        if (mLessonCardsViewReferences.containsKey(R.layout.default_lesson_card)
          &&mLessonCardsViewReferences.get(R.layout.default_lesson_card) == 0) {

            Log.v(sTag, "default lesson card view references == 0 return empty card");
            return new LessonCard();
        }

        RelativeLayout lessonView = null;
        /**try to retrieve lessonView**/
        if (mLessonCardsViewPool != null && mLessonCardsViewPool.containsKey(R.id.LessonCardId)) {
            Log.v(sTag, "found view in lesson cards pool recycle it ");
            lessonView = (RelativeLayout) mLessonCardsViewPool.get(R.id.LessonCardId);
        }

        DefaultLessonViewHolder lessonViewHolder;
        /**create lesson view if doesn`t exist**/
        if (lessonView == null) {

            Log.v(sTag, "building new lesson view");
            WindowManager wm = (WindowManager) mCtx.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int height = size.y;
            height = (height / 100) * sHeightDefaultLessonCard;
            FrameLayout.LayoutParams mainWrapperLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            mainWrapperLp.gravity = Gravity.BOTTOM;
            LayoutInflater inflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            lessonView = (RelativeLayout) inflater.inflate(R.layout.default_lesson_card, null);
            lessonView.setLayoutParams(mainWrapperLp);
            lessonViewHolder = new DefaultLessonViewHolder();
            lessonViewHolder.descriptioTextView = (TextView) lessonView.findViewById(R.id.LessonCardDescriptionTextView);
            lessonViewHolder.actionTextView = (TextView) lessonView.findViewById(R.id.LessonCardActionTextView);
            lessonViewHolder.iconView = (ImageView) lessonView.findViewById(R.id.LessonCardIcon);
            lessonViewHolder.closeImageView = (ImageView) lessonView.findViewById(R.id.LessonCardCloseView);
            lessonViewHolder.actionTextView.setTextAppearance(mCtx, actionStyleId);
            lessonViewHolder.descriptioTextView.setTextAppearance(mCtx, descStyleId);
            lessonView.setTag(R.id.lesson_view_holder, lessonViewHolder);
        }

        LessonCard lessoncard= addLessonCard(lessonView, new LessonCardViewRecyclerListener() {
            @Override
            public void onRecycle(View view, LessonArgs args) {

                DefaultLessonViewHolder lessonViewHolder = (DefaultLessonViewHolder) view.getTag(R.id.lesson_view_holder);
                lessonViewHolder.descriptioTextView.setText(args.descriptionLabel);
                lessonViewHolder.actionTextView.setText(args.actionLabel);
                if (args.iconResource != -1){lessonViewHolder.iconView.setImageResource(args.iconResource);}
                view.setBackgroundColor(Color.parseColor(args.color));
            }
        },args);

        DefaultLessonCardCloseListener defaultListener = new DefaultLessonCardCloseListener();
        for(LessonArgs arg:args){  arg.mClickListeners.add(defaultListener);}
        return lessoncard;
    }

    /**
     * add view to  lessonCardsPool hashmap
     * create  by view_id  -> view or by custom linkid  -> view    *
     *
     * @param view
     */
    public LessonCard addLessonCard(View view,LessonCardViewRecyclerListener listener,LessonArgs ... args) {

        /**if isn`t the first execution for this view check if the references are all consumed if is it exit**/
        mLessonCardsViewReferences = mLessonCardsViewReferences == null ? new HashMap() : mLessonCardsViewReferences;
        if (mLessonCardsViewReferences.containsKey(R.layout.default_lesson_card)
            &&mLessonCardsViewReferences.get(R.layout.default_lesson_card) == 0) {
            return new LessonCard();
        }

        /**on first card attach listener**/
        if (mLessonCardsViewPool == null
           ||(mLessonCardsViewPool != null
           && mLessonCardsViewPool.size() == 0)) {
           ((Application) INSTANCE.mCtx.getApplicationContext()).registerActivityLifecycleCallbacks(mListener);
        }
        /**
         * the view will be insert in view pool
         *  id_view | linkid => view
         */
        if (view.getId() == View.NO_ID) {
            new InvalidParameterException("view must have id setted ");
            return null;
        }
        /**make object lesson card ready to store**/
        mLessonCardsViewPool = mLessonCardsViewPool == null ?
        new HashMap():
        mLessonCardsViewPool;
        mLessonCardsViewPool.put(view.getId(), view);

        /**creating lesson card that refers to view defined upon**/
        LessonCard lessonCard = new LessonCard(view.getId(),  listener,args);
        return lessonCard;
    }

    private void showLesson(LessonCard lessonCard,String discriminator) {

        /**conditions**/
        if (lessonCard == null) {return;}

        View viewToShow = lessonCard.getView();

        String id = viewToShow.getId() + "";


        /**check if references to view are at zero **/
        if (mLessonCardsViewReferences == null
                || mLessonCardsViewReferences.get(id) == null
                || mLessonCardsViewReferences.get(id) == 0
                || mLessonCardsTimesCounter.get(discriminator) == 0) {
            return;
        }
        /**check dependencies**/
        for (String dependencyLessonCardAnchor : lessonCard.mLessonCardsDependencies) {
            if (mLessonCardsTimesCounter.containsKey(dependencyLessonCardAnchor)
                && mLessonCardsTimesCounter.get(dependencyLessonCardAnchor) > 0) {
                return;
            }
        }

        /**map parameters on view**/
        int argsIndex=(int) (lessonCard.mArgs.size()- mLessonCardsTimesCounter.get(discriminator));
        LessonArgs currentArgs=lessonCard.mArgs.size()>argsIndex?lessonCard.mArgs.get(argsIndex):lessonCard.mArgs.get(0);
        if(currentArgs.condition!=null&&currentArgs.condition.visibilityIsAllowed()==false){return;}

        /**==============end conditions==========================*/
        /**test ok show it**/
        FrameLayout rootLayout = (FrameLayout) ((Activity)mCtx).findViewById(android.R.id.content);
        /**check if is a default LessonCard**/
        DefaultLessonViewHolder lessonViewHolder = lessonCard.getView().getTag(R.id.lesson_view_holder) != null ?
                (DefaultLessonViewHolder) lessonCard.getView().getTag(R.id.lesson_view_holder)
                : null;

        View closeButtonView = lessonViewHolder == null ? lessonCard.getView().findViewById(R.id.LessonCardCloseView) : lessonViewHolder.closeImageView;
        View actionView = lessonViewHolder == null ? lessonCard.getView().findViewById(R.id.LessonCardActionTextView) : lessonViewHolder.actionTextView;

        if(closeButtonView!=null)closeButtonView.setOnClickListener(null);
        if(actionView!=null)actionView.setOnClickListener(null);
        /** attachclick listener if it needs**/
        if(currentArgs.mClickListeners.size()>0) {
            /*****/
            closeButtonView.setOnClickListener(mListener);
            actionView.setOnClickListener(mListener);
            actionView.setTag(R.id.lesson_on_click_tag, lessonCard.mCardHook);
            closeButtonView.setTag(R.id.lesson_on_click_tag, lessonCard.mCardHook);
        }

            lessonCard.recyclerListener.onRecycle(viewToShow,currentArgs);


        FrameLayout mainTutorialWrapper  = (FrameLayout)((Activity) mCtx).findViewById(R.id.lesson_card_main_wrapper_id);

        if(mainTutorialWrapper==null){
            rootLayout.addView(injectInDefaultFrame(lessonCard.getView()));
        }else
        {

            if(!(mLastViewInflatedId+"").equals(lessonCard.getView().getId()+"")){
                mainTutorialWrapper.removeAllViews();
                mainTutorialWrapper.addView(lessonCard.getView());
            }
             mainTutorialWrapper.setVisibility(View.VISIBLE);
        }

        /** couters update**/
        mLessonCardsViewReferences.put(id, mLessonCardsViewReferences.get(id) - 1);
        mLessonCardsTimesCounter.put(discriminator, mLessonCardsTimesCounter.get(discriminator) - 1);
        serializeIt();
    }


    private View injectInDefaultFrame(View child) {

        //todo sett parent null
        FrameLayout mainTutorialWrapper=new FrameLayout(mCtx);

        if (child.getLayoutParams() instanceof FrameLayout.LayoutParams) {

            FrameLayout.LayoutParams layoutParamsChild = (FrameLayout.LayoutParams) child.getLayoutParams();
            FrameLayout.LayoutParams layoutParamsCloned = new FrameLayout.LayoutParams((ViewGroup.MarginLayoutParams) layoutParamsChild);
            layoutParamsCloned.gravity = layoutParamsChild.gravity;
            mainTutorialWrapper.setLayoutParams(layoutParamsCloned);
        }

        mainTutorialWrapper.setId(R.id.lesson_card_main_wrapper_id);
        mainTutorialWrapper.addView(child);
        mLastViewInflatedId = child.getId();

        return mainTutorialWrapper;
    }

    private void serializeIt() {

        ObjectSerializer.getInstance(mCtx, Context.MODE_PRIVATE).save(sStoreKeyLessonCardsViewReferences, mLessonCardsViewReferences);
        ObjectSerializer.getInstance(mCtx, Context.MODE_PRIVATE).save(sStoreKeySourceEventLessonCard, mLessonCardsTimesCounter);
        ObjectSerializer.getInstance(mCtx, Context.MODE_PRIVATE).commit(true);
    }

    private void deserializeIt() {

        if (mTutorialComplete) {
            return;
        }
        Boolean lessonsLearned = ObjectSerializer.getInstance(mCtx, Context.MODE_PRIVATE).load(sTutorialComplete, Boolean.class);
        if (lessonsLearned == Boolean.TRUE) {
            mTutorialComplete = true;
            return;
        }

        if (mLessonCardsViewReferences == null) {
            mLessonCardsViewReferences = ObjectSerializer.getInstance(mCtx, Context.MODE_PRIVATE).load(sStoreKeyLessonCardsViewReferences, HashMap.class);

            if (mLessonCardsViewReferences != null) {
                mTutorialComplete = true;

                for (Double counter : mLessonCardsViewReferences.values()) {

                    if (counter > 0) {
                        mTutorialComplete = false;
                        break;
                    }
                }

                if (lessonsLearned == null && mTutorialComplete) {
                    ObjectSerializer.getInstance(mCtx, Context.MODE_PRIVATE).save(sTutorialComplete, Boolean.TRUE);
                    ObjectSerializer.getInstance(mCtx, Context.MODE_PRIVATE).commit(true);
                    return;
                }
            }
            mLessonCardsViewReferences = mLessonCardsViewReferences == null ? new HashMap<String, Double>() : mLessonCardsViewReferences;
        }

        if (mLessonCardsTimesCounter == null) {
            mLessonCardsTimesCounter = ObjectSerializer.getInstance(mCtx, Context.MODE_PRIVATE).load(sStoreKeySourceEventLessonCard, HashMap.class);
            mLessonCardsTimesCounter = mLessonCardsTimesCounter == null ? new HashMap<String, Double>() : mLessonCardsTimesCounter;
        }
    }
    private class DefaultLessonCardCloseListener implements LessonCardOnClickListener {

        @Override
        public void onActionClick(LessonClickEvent event) { }
        @Override
        public void onCloseClick(LessonClickEvent event) {

            FrameLayout mainTutorialWrapper = (FrameLayout) ((Activity)mCtx).findViewById(R.id.lesson_card_main_wrapper_id);
            mainTutorialWrapper.setVisibility(View.GONE);


        }
    }

    /***INNER CLASSES**/
    public static class LessonArgs {

        int iconResource;
        String actionLabel;
        String descriptionLabel;
        String color;
        LessonCardViewVisibilityCondition condition;
        private ArrayList<LessonCardOnClickListener> mClickListeners=new ArrayList<>();

        public LessonArgs(int iconResource, String actionLabel, String descriptionLabel, String color, LessonCardViewVisibilityCondition ... condition) {

            this.iconResource = iconResource;
            this.descriptionLabel = descriptionLabel;
            this.actionLabel = actionLabel;
            this.color = color;
            if(condition.length>0){ this.condition=condition[0];}
        }

        public void addLessonCardListener(LessonCardOnClickListener listener) {

            this.mClickListeners.add(listener);
        }

        public void dispatchEvent(LessonClickEvent evt){

            for (LessonCardOnClickListener listener : mClickListeners){

                if(evt.whatIsIt==LessonClickEvent.ACTION_EVT){listener.onActionClick(evt); }
                if(evt.whatIsIt==LessonClickEvent.CLOSE_EVT){listener.onCloseClick(evt); }
            }
        }
    }

    public interface LessonCardViewVisibilityCondition{ boolean visibilityIsAllowed();}
    public interface LessonCardViewRecyclerListener {

        /** * indicate how to map arguments to view **/
        void onRecycle(View view, LessonArgs args);
    }

    public interface LessonCardOnClickListener {

        void onActionClick(LessonClickEvent event);
        void onCloseClick(LessonClickEvent event);
    }


    public class LessonClickEvent extends EventObject {

        public static  final int CLOSE_EVT=10;
        public static final int ACTION_EVT=01;
        public int whatIsIt;
        /**
         * Constructs a new instance of this class.
         * @param source the object which fired the event.
         */
        public LessonClickEvent(View source) {
            super(source);
            whatIsIt=source.getId()==R.id.LessonCardActionTextView?ACTION_EVT:CLOSE_EVT;
        }

        @Override
        public View getSource() {
            return (View) super.getSource();
        }
    }

    private static class DefaultLessonViewHolder {

        public TextView descriptioTextView, actionTextView;
        public ImageView iconView, closeImageView;
    }

    /**
     * model that rappresents
     * the view and rules
     * to show
     */
    public class LessonCard implements Serializable {

        private transient boolean mExit = false;
        private transient int mCardViewLink = -1;
        private transient  String mCardHook ="";
        private transient ArrayList <LessonArgs> mArgs;
        private transient LessonCardViewRecyclerListener recyclerListener;
        private final ArrayList<String> mLessonCardsDependencies = new ArrayList();
        private double mTimes = 1;


        public LessonCard() {
            mExit = true;
        }

        public LessonCard(int lessonViewAnchor, LessonCardViewRecyclerListener recyclerListener,LessonArgs ...  args) {
            this.mCardViewLink = lessonViewAnchor;
            this.mArgs = new ArrayList(Arrays.asList(args)) ;
            this.recyclerListener = recyclerListener;

        }

        private boolean activateLessonCard(String cardKey) {

            mSourceEventLessonCard = mSourceEventLessonCard == null ? new HashMap() : mSourceEventLessonCard;
            mLessonCardsTimesCounter = mLessonCardsTimesCounter == null ? new HashMap() : mLessonCardsTimesCounter;
            mCardHook=cardKey;
            mSourceEventLessonCard.put(cardKey, this);

            if (mExit || (mLessonCardsTimesCounter.get(cardKey) != null)) {return true;}

            mTimes=mTimes<mArgs.size()?mArgs.size():mTimes;
            mLessonCardsTimesCounter.put(cardKey,mTimes);
            mLessonCardsViewReferences.put(mCardViewLink + "", mLessonCardsViewReferences.containsKey(mCardViewLink + "") ?
            mLessonCardsViewReferences.get(mCardViewLink + "") + mTimes : mTimes);
            serializeIt();
            return false;
        }

        /**
         * means that this the lesson card view has to be showed
         * on activity start
         *
         * @param activityClazz
         * @return
         */
        public <T extends Activity> LessonCard showOnActivityStart(Class<T> activityClazz) {
          activateLessonCard(activityClazz.getName());
          return this;
        }

        public  LessonCard showOnKeyFire(String key) {
            activateLessonCard(key+sVoidCollision);
            return this;
        }

        /**
         * means that this lesson card view has to be showed
         * on button click
         *
         * @param view
         * @return
         */
        public LessonCard showWhenClickOn(View view) {

            if (view.getId() == View.NO_ID) {
                Log.e(sTag, "view must have id setted");
                return this;
            }
            if (activateLessonCard(view.getId() + "")) {
                return this;
            }
            view.setClickable(true);
            view.setOnClickListener(mListener);
            return this;
        }


        /**
         * number of times that the lesson has to be repeated
         *
         * @param times
         * @return
         */
        public LessonCard executeFor(int times) {

            if (mExit) { return this;}
            this.mTimes = times;
            return this;
        }

        public <T extends Activity> LessonCard addDependency(Class<T> activityClass) {
            if (mExit) {return this;}
            addDependencyInternal(activityClass.getName(),false);
            return this;
        }

        public LessonCard addDependency(View view) {

            if (mExit) { return this;}

            addDependencyInternal(view.getId() + "",false);
            return this;
        }

        public LessonCard addDependency(String cardKey) {
            addDependencyInternal(cardKey,true);
            return this;
        }

          private LessonCard addDependencyInternal(String cardKey,boolean voidCollision) {

            if (mExit) {return this;}
            mLessonCardsDependencies.add(voidCollision?cardKey+sVoidCollision:cardKey);
            return this;
        }

        public LessonCard addLessonArgs(LessonArgs args) {

            if (mExit) {
                Log.e(sTag,"LessonArgs");
                return this;
            }
           this.mArgs.add(args);
            return this;
        }

        public View getView() {
            return mLessonCardsViewPool.get(mCardViewLink);
        }

        public void printLog() {

            Log.i(sTag, "===========LESSON-CARDS COUNTERr===================");
            for (Map.Entry lessonCardCounterRow : mLessonCardsTimesCounter.entrySet()) {
                Log.i(sTag, lessonCardCounterRow.getKey() + "=>" + lessonCardCounterRow.getValue());
            }
            Log.i(sTag, "======================================================");
            Log.i(sTag, "===========SOURCEVIEWSFIRED-> LESSON_CARD ===================");
            for (Map.Entry lessonCardCounterRow : mSourceEventLessonCard.entrySet()) {
                Log.i(sTag, lessonCardCounterRow.getKey() + "=>" + lessonCardCounterRow.getValue());
            }
            Log.i(sTag, "======================================================");
            Log.i(sTag, "===========VIEW REFERENCES COUNTER ===================");

            for (Map.Entry lessonCardCounterRow : mLessonCardsViewReferences.entrySet()) {
                Log.i(sTag, lessonCardCounterRow.getKey() + "=>" + lessonCardCounterRow.getValue());
            }
            Log.i(sTag, "======================================================");
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    private class TeacherDefaultListener implements Application.ActivityLifecycleCallbacks, View.OnClickListener {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            mCtx = activity;
        }
        @Override
        public void onActivityStarted(Activity activity) {
            LessonCard lessonCard = mSourceEventLessonCard.get(activity.getClass().getName());
            showLesson(lessonCard,activity.getClass().getName());
        }

        @Override
        public void onActivityResumed(Activity activity) {}
        @Override
        public void onActivityPaused(Activity activity) {}
        @Override
        public void onActivityStopped(Activity activity) { }
        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
        @Override
        public void onActivityDestroyed(Activity activity) {  }

        @Override
        public void onClick(View v) {

            LessonCard lessonCard = mSourceEventLessonCard.get(v.getId());
            if (lessonCard != null) {
                showLesson(lessonCard,v.getId() + "");
                return;
            }

            String cardHook = (String) v.getTag(R.id.lesson_on_click_tag);

            lessonCard=mSourceEventLessonCard.get(cardHook);

            int argsIndex=(int)(lessonCard.mArgs.size()- mLessonCardsTimesCounter.get(cardHook)-1);
            LessonArgs currentArgs=lessonCard.mArgs.size()>argsIndex?lessonCard.mArgs.get(argsIndex):lessonCard.mArgs.get(0);
            if (v.getId() == R.id.LessonCardCloseView||v.getId() == R.id.LessonCardActionTextView) {currentArgs.dispatchEvent(new LessonClickEvent(v)); }
        }
    }
}
