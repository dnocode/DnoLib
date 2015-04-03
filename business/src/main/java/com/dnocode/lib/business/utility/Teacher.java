package com.dnocode.lib.business.utility;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

/**
 * Created by dnocode on 03/04/15.
 */
public class Teacher implements Application.ActivityLifecycleCallbacks {

    private static Teacher INSTANCE;
    private  Context mCtx=null;
    private HashMap<Integer,View> mCardsViewPool;
    private HashMap<Integer,LessonCard> mSourceLessonCard;

    private Teacher(Context ...context){ if(context!=null&&context.length>0){ this.mCtx=context[0];}}

    public static void learn(Context ... context){ INSTANCE=INSTANCE==null?new Teacher(context.length>0?context[0]:null):INSTANCE;}


    public void card(int resourceLayout){ }

    public void card(String title){ }

    public void card(View view){

        if(mCardsViewPool ==null||(mCardsViewPool !=null&& mCardsViewPool.size()>0)){

            ((Application)INSTANCE.mCtx.getApplicationContext()).registerActivityLifecycleCallbacks(this);
        }

    }


    private void removeCard(View card){

        mCardsViewPool.remove(card.getId());

        if(mCardsViewPool ==null||(mCardsViewPool !=null&& mCardsViewPool.size()==0)){

            ((Application)INSTANCE.mCtx.getApplicationContext()).unregisterActivityLifecycleCallbacks(this);
        }


    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

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


    /**
     *  model that rappresent
     *  the view and rules
     *  to show 
     */
    private static class LessonCard{

        public int cardViewLink;
        public int times=1;
        boolean isTransient=false;
        public int[] cardsDependencies;

    }
}
