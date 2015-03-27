package com.dnocode.lib.business.ext.carousel;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dnocode.lib.business.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.util.List;

import fr.rolandl.carousel.CarouselAdapter;
import fr.rolandl.carousel.CarouselItem;


public abstract   class PhotoCarouselAdapter extends CarouselAdapter<Photo>
{

    public PhotoCarouselAdapter(Context context, List<Photo> photos)
    {
        super(context, photos);
    }

    @Override
    public CarouselItem<Photo> getCarouselItem(Context context)
    {
        return new PhotoItem(context,photoLayoutResource());
    }

    private final class PhotoItem extends CarouselItem<Photo>
    {
        private ImageView mImageView;
        private Context mContext;

        public PhotoItem(Context context,int  resourceId)
        {
            super(context, resourceId);
            this.mContext = context;
        }

        @Override
        public void extractView(View view){mImageView =findImageView(view);}

        @Override
        public void update(Photo photo)
        {
           if(mImageView==null){return;}
            RequestCreator rc=null;
            Transformation transformation=transformation();
            Integer errorPlaceHolderReference= errorDrawableResource();
            errorPlaceHolderReference=photo.errorRef!=null?photo.errorRef:errorPlaceHolderReference;
            transformation=photo.trasformation!=null?photo.trasformation:transformation;

            if(photo.drawableRef!=null){ rc=Picasso.with(mContext).load(photo.drawableRef);}
            if(photo.uri!=null){ rc=Picasso.with(mContext).load(photo.uri);}

            if(transformation!=null){rc.transform(transformation);}

            if(errorPlaceHolderReference!=null){rc.error(errorPlaceHolderReference);}
            rc.into(mImageView);

        }

    }




    protected  Transformation transformation(){  return null;}
    protected abstract Integer photoLayoutResource();
    protected abstract Integer errorDrawableResource();
    protected abstract ImageView findImageView(View view);


}

