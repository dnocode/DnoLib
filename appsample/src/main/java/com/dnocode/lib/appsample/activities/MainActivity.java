package com.dnocode.lib.appsample.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dnocode.lib.business.utility.Teacher;

public class MainActivity extends ActionBarActivity  implements View.OnClickListener{

    private final int sLessonCardId=32323;
    private LinearLayout mMainContainer;
    public static boolean holderActivityLessonCondition=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mMainContainer=new LinearLayout(this);
        mMainContainer.setOrientation(LinearLayout.VERTICAL);
        addLink(HolderPatternActivity.class);
        addLink(CarouselActivity.class);
        addLink(BrowseCardActivity.class);
        addLink(SingleFadeImageActivity.class);
        setContentView(mMainContainer);


       /**define lessons array in sequence**/
        Teacher.LessonArgs[]  lessonsSameOnSameView=new Teacher.LessonArgs[2];
        /**first lesson**/
        lessonsSameOnSameView[0]=new Teacher.LessonArgs(-1, "CarouselActivity", "Images slide", "black");
        /**second lesson with visibility condition**/
        lessonsSameOnSameView[1]=new Teacher.LessonArgs(-1, "CarouselActivity", "Images slide2", "green", new Teacher.LessonCardViewVisibilityCondition() {
            @Override
            public boolean visibilityIsAllowed() {
                return holderActivityLessonCondition;
            }
        });

        /**add lesson click listener**/
        lessonsSameOnSameView[1].addLessonCardListener(new Teacher.LessonCardOnClickListener() {
            @Override
            public void onActionClick(Teacher.LessonClickEvent event) {Log.i("lessonCard","action clicked");}
            @Override
            public void onCloseClick(Teacher.LessonClickEvent event) {Log.i("lessonCard","action clsoe clicked");  }
        });


        /**check if all lesson are learned**/
        if(Teacher.instance(this).areLessonsLearned()==false) {

                    Teacher.instance(this)
                    /**add lesson show on singleFadeImageActivityShowed**/
                    .addLessonCard(new Teacher.LessonArgs(-1, "SingleFadeImageActivity", "immagine singola scorrevole", "red"))
                    .showOnActivityStart(SingleFadeImageActivity.class)
                    /** dependency mean that this lesson depends of carouselActivity lesson **/
                    .addDependency(CarouselActivity.class);
                    /**add second lessons array | more lessons in the same view **/
                    Teacher.instance(this)
                    .addLessonCard(lessonsSameOnSameView)
                    .showOnActivityStart(CarouselActivity.class).printLog();

        }




   }

    private void addLink(Class target){

        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btn=new Button(this);
        btn.setText(target.getSimpleName().toLowerCase());
        btn.setOnClickListener(this);
        btn.setTag(target);
        btn.setLayoutParams(lp);
        mMainContainer.addView(btn);

    }

    @Override
    public void onClick(View v) {
        Intent i=new Intent(this, (Class<?>) v.getTag());
        this.startActivity(i);
    }
}
