package com.dnocode.lib.business.list.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author dino
 * simple singleton for mantain in ram
 * annotation after first reading
 */
public class RuntimeAnnotations {

    /** object class choosen **/
    private Class mSourceClass;
    /** annotations choosen**/
    private Class<Annotation>[] mTargetAnnotations;
    /** annotations retrieved **/ /** /todo tofix**/
    private ArrayList<Annotation> mAnnotationsResult;
    /** hold in memory hash map  classname=>annotationName=>fields **/
    private  HashMap<String,HashMap<String, ArrayList<Field>>> mAnnotationsPool =new HashMap<>();

    private static RuntimeAnnotations INSTANCE = new RuntimeAnnotations();

    public static  <E extends Annotation > RuntimeAnnotations read(Class<E> ... targetAnnotation) {

        INSTANCE.mTargetAnnotations=(Class<Annotation>[]) targetAnnotation;
        return INSTANCE;
    }

    /**
     * this method
     * get annotations from
     * memory if there were loaded
     * before or by reflection
     * if is the first time
     *
     * @param clazz
     *
     * @return
     */
    public  RuntimeAnnotations from(Class clazz) {

        mSourceClass=clazz;
        String rootKey=clazz.getSimpleName();
        mAnnotationsResult=new ArrayList();
        Annotation[] annotations;
        ArrayList<Class<Annotation>> annotationsGotInMemory=new ArrayList<>();
        /**try to get it from memory**/
        /** get or init**/
        HashMap<String,ArrayList<Field>> annotationMap= mAnnotationsPool.containsKey(rootKey)?
                mAnnotationsPool.get(rootKey):
                mAnnotationsPool.put(rootKey,new HashMap<String, ArrayList<Field>>());

        annotationMap=annotationMap==null?mAnnotationsPool.get(rootKey):annotationMap;

        /**if no class target is indicated get  all class in memory **/
        int counter=mTargetAnnotations.length==0?annotationMap.size():mTargetAnnotations.length;

         String [] annotationInMemory=annotationMap.keySet().toArray(new String[]{});

            for (int i=0;i<counter;i++){

                /**gets field about that annotation**/

                String key=mTargetAnnotations.length==0?annotationInMemory[i]:mTargetAnnotations[i].getSimpleName();

                ArrayList<Field> fieldsReferenceList=annotationMap.get(key);

                if(fieldsReferenceList!=null) {

                    if(mTargetAnnotations.length!=0)
                    annotationsGotInMemory.add(mTargetAnnotations[i]);

                    for (Field fieldInMemory : fieldsReferenceList) {

                        mAnnotationsResult.addAll(Arrays.asList( fieldInMemory.getAnnotations()));
                    }
                }
            }

        if(annotationsGotInMemory!=null&&annotationsGotInMemory.size()==mTargetAnnotations.length){return INSTANCE;}
        /**if annotation got in memory === targer return **/
        for(Field field : clazz.getDeclaredFields()){

            annotations=field.getDeclaredAnnotations();
            /**loop fields annotations**/
                for(Annotation a :annotations){
                    boolean isValid=false;
                    if(mTargetAnnotations.length>0) {
                    /** filter **/
                    for (Class<Annotation> aClass : mTargetAnnotations) {

                         if(a.annotationType()==aClass&&
                           annotationsGotInMemory.contains(aClass)==false){
                           isValid=true;
                          break;
                      }
                    }
                     if(isValid==false){continue;}
                    }

                    mAnnotationsResult.add(a);

                    String annotationKey= a.annotationType().getSimpleName();
                    HashMap<String,ArrayList<Field>> annotationMapReference= mAnnotationsPool.get(rootKey);
                    ArrayList<Field> fieldsReferenceList=annotationMapReference.containsKey(annotationKey)?
                    annotationMapReference.get(annotationKey):
                    annotationMapReference.put(annotationKey,new ArrayList<Field>());
                    if(fieldsReferenceList==null){ fieldsReferenceList=annotationMapReference.get(annotationKey); }
                    fieldsReferenceList.add(field);
                }
        }

        return INSTANCE;

    }

    /**pull out all annotations**/
    public <E extends  Annotation> ArrayList<E> pullOutAnnotations(){  return (ArrayList<E>) mAnnotationsResult;}

    /**
     *
     * @return annotated fields whit annotations inside
     */
    public  ArrayList<Field> pullOutFields(){

        if(!mAnnotationsPool.containsKey(mSourceClass.getSimpleName())){return null;}

        ArrayList<Field> fields=new ArrayList<>();

        HashMap<String,ArrayList<Field>> annotationsFieldMap= mAnnotationsPool.get(mSourceClass.getSimpleName());

        for(Class<Annotation> targetAnnotation :mTargetAnnotations){

            fields.addAll(annotationsFieldMap.get(targetAnnotation.getSimpleName()));
        }

        return fields;
    }

    public void unload(Class ... clazzs){

        if(clazzs.length>0){

            for(Class clazz:clazzs) mAnnotationsPool.remove(clazz.getSimpleName());

            return;
        }
        mAnnotationsPool.clear();
    }

}

