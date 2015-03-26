package com.dnocode.lib.business.list.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
   /* private ArrayList<Annotation> mAnnotationsResult;*/
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

        Annotation[] annotations;
        ArrayList<Class<Annotation>> annotationsGotInMemory=new ArrayList<>();
        /**try to get it from memory**/
        /** get or init**/
        HashMap<String,ArrayList<Field>> annotationMap= mAnnotationsPool.containsKey(rootKey)?
                mAnnotationsPool.get(rootKey):
                mAnnotationsPool.put(rootKey,new HashMap<String, ArrayList<Field>>());

        annotationMap=annotationMap==null?mAnnotationsPool.get(rootKey):annotationMap;

        /**if no class target is indicated get  all class in memory **/
        List<Class<Annotation>> notFoundInMemory=new ArrayList<>();

        for(Class<Annotation> targetAnnotationClass : mTargetAnnotations){

            if(!annotationMap.containsKey(targetAnnotationClass.getSimpleName())){

                notFoundInMemory.add(targetAnnotationClass);
            }
        }

        if(notFoundInMemory.size()==0){return INSTANCE;}

        mTargetAnnotations=notFoundInMemory.toArray(new Class[0]);


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
    public <E extends  Annotation> ArrayList<E> pullOutAnnotations(){

        ArrayList<Annotation> resultAnnotations=new ArrayList<>();

        HashMap<String, ArrayList<Field>> annotationFieldsMap = mAnnotationsPool.containsKey(mSourceClass.getSimpleName()) ?

        mAnnotationsPool.get(mSourceClass.getSimpleName()) :

        null;

        if(annotationFieldsMap==null){return  null;}

            for( ArrayList<Field> fields :annotationFieldsMap.values()){

                 for (Field f : fields){

                     if(mTargetAnnotations.length>0){

                         for(Class<Annotation> targetAnnotationClass : mTargetAnnotations){

                             resultAnnotations.add(f.getAnnotation(targetAnnotationClass));
                         }

                     }else {
                             resultAnnotations.addAll(filterAnnotations(f.getAnnotations()));
                     }

                 }
            }

        return (ArrayList<E>) resultAnnotations;

       }



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



    /**meta methods**/
    /**return an array with the annotations filtered**/
    private List<Annotation> filterAnnotations(Annotation... annotations){

        List<Annotation> allowed=new ArrayList<>();

        for (Annotation annotation:annotations) {

            if(checkIfAnnotationIsAllowed(annotation)){allowed.add(annotation);}
        }

        return allowed;
    }

    /**check if the annotation is among those target**/
    private boolean checkIfAnnotationIsAllowed(Annotation annotation){

        if(mTargetAnnotations.length==0) {return true;}
        /** filter **/
        for (Class<Annotation> aClass : mTargetAnnotations) {

            if(annotation.annotationType()==aClass){ return true;}
        }
        return false;
    }



}

