package com.dnocode.lib.appsample.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;

import com.dnocode.lib.appsample.R;
import com.dnocode.lib.business.ext.carousel.Photo;
import com.dnocode.lib.business.ext.carousel.PhotoCarouselAdapter;
import com.dnocode.lib.business.ext.picasso.CircleTransform;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import fr.rolandl.carousel.Carousel;

public class CarouselActivity extends ActionBarActivity {

    Carousel mCarousel;
    Carousel mCarousel2;
    String baseUrl="http://resources1.news.com.au/images/2011/07/22/1226099/494485-diving-funny-faces.jpg";
    long firstId=100006032921701l;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_carousel_pattern);
        mCarousel = (Carousel) findViewById(R.id.carouselView);
        mCarousel2 = (Carousel) findViewById(R.id.carouselView2);

        final List<Photo> photos1 = new ArrayList<>();
        photos1.add(new Photo(R.drawable.record));
        photos1.add(new Photo(R.drawable.recycle_bin));
        photos1.add(new Photo(R.drawable.refresh));
        photos1.add(new Photo(R.drawable.rewind));
        photos1.add(new Photo(R.drawable.attachment));

        final List<Photo> photos2 = new ArrayList<>();



        photos2.add(new Photo("http://resources1.news.com.au/images/2011/07/22/1226099/494485-diving-funny-faces.jpg"));
        photos2.add(new Photo("http://resources1.news.com.au/images/2011/07/22/1226099/493997-diving-funny-faces.jpg"));
        photos2.add(new Photo("http://resources3.news.com.au/images/2011/07/22/1226099/493735-diving-funny-faces.jpg"));
        photos2.add(new Photo("http://resources1.news.com.au/images/2011/07/22/1226099/494461-diving-funny-faces.jpg"));



        PhotoCarouselAdapter pca= new PhotoCarouselAdapter(this, photos1) {
            @Override
            protected Integer photoLayoutResource() {return R.layout.carousel_photo_big;}
            @Override
            protected Integer errorDrawableResource() { return null; }
            @Override
            protected ImageView findImageView(View view) { return (ImageView) view.findViewById(R.id.photoImage);}
        };


        PhotoCarouselAdapter pca2= new PhotoCarouselAdapter(this, photos2) {
            @Override
            protected Integer photoLayoutResource() {return R.layout.carousel_photo;}

            @Override
            protected Integer errorDrawableResource() {return R.drawable.recycle_bin;}

            @Override
            protected ImageView findImageView(View view) {
                 return (ImageView) view.findViewById(R.id.photoImage);
            }

            @Override
            protected Transformation transformation() {
                return new CircleTransform();
            }
        };



        mCarousel2.setAdapter(pca2);


        mCarousel.setAdapter(pca);



        pca.notifyDataSetChanged();
        pca2.notifyDataSetChanged();
    }





}
