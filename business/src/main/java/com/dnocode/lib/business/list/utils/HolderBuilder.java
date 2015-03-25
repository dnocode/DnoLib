package com.dnocode.lib.business.list.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dnocode.lib.business.list.annotations.Bind;
import com.squareup.picasso.RequestCreator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author  dino
 * this object its just an helper of implements
 * View holder pattern
 * @param <E>
 */
public  abstract class HolderBuilder<E> {

    private final static int COMPONENTS_KEY=1001;
    protected ViewComponentsHolder mHolder;
    protected E mModel;
    protected  HashMap<Integer,Object> mParameters;
    protected Context mContext;
    private View mLastObject;
    private int  mLastObjectRef;

    public View build(ViewGroup parent,View view,E model,HashMap<Integer,Object> ... parameters)  {return this.build(parent,view,model,-1,parameters);}

     public View build(ViewGroup parent,View view,E model,int layoutResource,HashMap<Integer,Object> ... parameters)  {
         try {
             mContext=parent.getContext();
             /** get view if doesn`t exist**/
             view=view == null? createView(parent,layoutResource):view;
             /**check if there is a tag**/
             boolean isTagged=view.getTag()!=null;
             /**if wasn`t already tagged instance static view otherwise get view from tag**/
             mHolder= isTagged==false?new ViewComponentsHolder(): (ViewComponentsHolder) view.getTag();
             mModel=model;
             mParameters=parameters.length>0?parameters[0]:null;

             if(!isTagged){
                 /**if wasn`t tagged assign gui components of static view to pojo static holder**/
                 bindHolderToView(mHolder, (ViewGroup) view);
                 view.setTag(mHolder);
             }
             reset();
             update(mModel);
             if(!isTagged){setEventsHandlers();}
             setEventsHandlersVolatile();
             return view;

             }catch (Exception e ){

             String tag= (((Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName());
             Log.e(tag,e.getMessage());

             return null;
         }
     }

    /**
     * add components to holder
     *
     * @param holder
     * @param view
     */
    protected void bindHolderToView(ViewComponentsHolder holder, ViewGroup view) {

        ArrayList<Bind>  annotations =  RuntimeAnnotations.read().from(mModel.getClass()).pullOutAnnotations();

        boolean  clazzWasAnnoted=false;

        for(Annotation bAnnotation : annotations){

            if(bAnnotation instanceof Bind){

                int ids[]=((Bind) bAnnotation).to();

                clazzWasAnnoted=true;

                for(int id :ids){
                    holder.takeIt(view.findViewById(id));

                }
            }
        }
        /**auto mapping is disabled ispection viewgroup hierarchy**/
        if(!clazzWasAnnoted) {

            for (int i = 0; i < view.getChildCount(); i++) {

                View child = view.getChildAt(i);

                if (child instanceof ViewGroup) {

                    if (((ViewGroup) child).getChildCount() > 0) {

                        bindHolderToView(holder, view);
                    }
                }

                holder.takeIt(child);
            }

        }
    }

    protected <T extends View> T findViewById(int refId,Class<T> clazz){

         if(refId==mLastObjectRef&&mLastObject!=null){ return (T) mLastObject;}

        T component=mHolder.get(refId,clazz);

        if(component!=null){
            mLastObject=component;
            mLastObjectRef=refId;
        }
        return component;

    }

    /** created View inflating view from xml or ovverride and create it programatically **/
    protected View createView(ViewGroup parent,int layoutResource){

        LayoutInflater  mInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return mInflater.inflate(layoutResource, parent, false);
    }

    /**
    protected  void bindHolderToView(T holder, View v){}
    /**object to reset on before draw**/
    protected  void reset(){};

    /**update ui with current model
     * use metod findView for retrieve view**/
    protected   void update(E model){

        ArrayList<Field> annotatedFields=RuntimeAnnotations.read(Bind.class).from(model.getClass()).pullOutFields();

        try {

            for (Field field : annotatedFields) {

                field.setAccessible(true);
                Object value=field.get(model);
                Bind bindAnnotation=field.getAnnotation(Bind.class);
                int[] idsComponents= bindAnnotation.to();
                Class[] componentsTypes= bindAnnotation.type();
                int index=0;
                for(int id:idsComponents){

                    View view=findViewById(id, componentsTypes[index]);
                    if(bindAnnotation.skipAutoBinding()){ binding(view,beforeBinding(view,value));}
                    else{
                        internalBinding(view, value);
                    }
                    afterBinding(view,value);
                    index++;
                }
            }
        } catch (IllegalAccessException e) {

        e.printStackTrace();
    }


    };

    /**
     * this method
     * map model on ui
     * right now it support
     * TextView
     * ImageView
     * ImageButton
     *
     * @param view
     * @param value
     */
    private  void internalBinding(View view,Object value){

        if(view instanceof  TextView ){ ((TextView)view).setText((CharSequence) value);}

        if(view instanceof  ImageView){

             if(value instanceof  Integer){   ((ImageView)view).setImageResource((Integer) value); }
             if(value instanceof RequestCreator){ ((RequestCreator) value).into((ImageView) view); }
        }

        if(view instanceof  ImageButton){

            if(value instanceof  String){   ((ImageButton)view).setImageResource((Integer) value); }
            if(value instanceof RequestCreator){  ((RequestCreator) value).into((ImageButton) view) ;}
        }

    }

    protected Object beforeBinding(View view,Object value){ return value;}

    protected  void binding(View view,Object value){}


    protected void afterBinding(View view,Object value){}

    /**setting events on ui components**/
    protected void setEventsHandlers(){}

    /**set events is called any time**/
    protected void setEventsHandlersVolatile(){}

    /**
     * usefull class for retrieve without reinflating components
     */
    private static class ViewComponentsHolder{

        private HashMap<Integer,View> mTextViewList=new HashMap<>();
        private HashMap<Integer,View> mButtonViewList=new HashMap<>();
        private HashMap<Integer,View> mPanelViewList=new HashMap<>();
        private HashMap<Integer,View> mImageViewList=new HashMap<>();
        private HashMap<Integer,View> mGenericViewList=new HashMap<>();

        public  ViewComponentsHolder(){}


        /**
         * put components in hashmap list
         * key id
         * components
         * subdivide components for type this
         * allow to retrieve it fast
         * @param component
         */
        public void takeIt(View component){

            if(component.getId()== View.NO_ID){return;}

            if(component instanceof ViewGroup){
                mPanelViewList.put(component.getId(),component);
                return;
            }
            if(component instanceof TextView){
                mTextViewList.put(component.getId(),component);
                return;
            }
            if(component instanceof Button){
                mButtonViewList.put(component.getId(),component);
                return;
            }
            if(component instanceof ImageButton){
                mButtonViewList.put(component.getId(),component);
                return;
            }
            if(component instanceof ImageView){
                mImageViewList.put(component.getId(),component);
                return;
            }
            mGenericViewList.put(component.getId(),component);
        }

        public <T extends View> T  get(int id,Class<T> ... types){

            Class type= types[0];
            HashMap<Integer,View> hashReference=mGenericViewList;
            if(type==ViewGroup.class){   hashReference=mPanelViewList;}
            if(type==TextView.class){ hashReference=mTextViewList;}
            if(type==Button.class || type==ImageButton.class ){hashReference=mButtonViewList;}
            if(type==ImageView.class){ hashReference=mImageViewList; }
            return (T)hashReference.get(id);
        }


    }

}
