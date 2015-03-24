package com.dnocode.lib.business.ext.carousel;

import android.content.Context;

import java.util.List;

import fr.rolandl.carousel.CarouselAdapter;
import fr.rolandl.carousel.CarouselItem;

/**
 * Created by dino on 24/03/15.
 */
public  class PhotoCarouselAdapter extends CarouselAdapter<Photo>
{

    public PhotoCarouselAdapter(Context context, List<Photo> photos)
    {


        super(context, photos);
    }

    @Override
    public CarouselItem<Photo> getCarouselItem(Context context)
    {
        return new PhotoItem(context);
    }

}