package com.dnocode.lib.business.ext.carousel;

import com.squareup.picasso.Transformation;

import java.io.Serializable;

/**
 * simple class
 * rappresents an image in
 * carousel
 * uri  url if need to dowload the image from remote
 * errorRef  resource id to show if loading fail
 * height height of imageview
 *
 */
public final class Photo  implements Serializable
    {

        String uri;
        Integer drawableRef=null;
        Integer errorRef=null;
        boolean sizing=false;
        Transformation trasformation;


        public Photo(String photoUri){  this.uri=photoUri;}

        public Photo(int drawableRefId){ this.drawableRef=drawableRefId;}


    }