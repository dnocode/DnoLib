package com.dnocode.lib.business.ext.carousel;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.dnocode.lib.business.R;
import fr.rolandl.carousel.CarouselItem;

public final class PhotoItem extends CarouselItem<Photo>
    {

        private ImageView image;

        private TextView name;

        private Context context;

        public PhotoItem(Context context)
        {
            super(context, -1);
            this.context = context;
        }

        @Override
        public void extractView(View view)
        {
            image = (ImageView) view.findViewById(R.id.image);

        }

        @Override
        public void update(Photo photo)
        {

      /*      int dim= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,getResources().getDisplayMetrics());

            RequestCreator rc= Picasso.with(getContext()).load(photo.name).transform(new PicassoRoundTransform(dim,dim)).error(com.dnocode.lib.widgets.R.mipmap.ic_launcher);


            rc.into(image);*/
        }

    }