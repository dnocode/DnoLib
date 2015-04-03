package com.dnocode.lib.widgets.enums;

/**
 * Created by dnocode on 02/04/15.
 */
public enum TransformationType {
    empty,
    circle,
    square;


    public static TransformationType fromOrdinal(int ordinal){
        int idx=0;
        for (TransformationType t :values()){
            if(idx==ordinal){return t;}
            idx++;
        }

        return null;
    }

}
