package com.dnocode.lib.appsample.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;

import com.dnocode.lib.appsample.R;
import com.dnocode.lib.business.ext.carousel.Photo;
import com.dnocode.lib.business.ext.carousel.PhotoCarouselAdapter;
import com.dnocode.lib.business.ext.picasso.CircleTransform;
import com.dnocode.lib.widgets.BrowseCardsView;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.rolandl.carousel.Carousel;

public class BrowseCardActivity extends ActionBarActivity {

    BrowseCardsView mBrowseCardsView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browse_card_pattern);
        mBrowseCardsView= (BrowseCardsView) findViewById(R.id.browseCardsView);


        try {


              mBrowseCardsView.addCard("http://resources1.news.com.au/images/2011/07/22/1226099/494485-diving-funny-faces.jpg");
              mBrowseCardsView.addCard("http://resources1.news.com.au/images/2011/07/22/1226099/493997-diving-funny-faces.jpg");
              mBrowseCardsView.addCard("http://resources3.news.com.au/images/2011/07/22/1226099/493735-diving-funny-faces.jpg");
              mBrowseCardsView.addCard("http://resources1.news.com.au/images/2011/07/22/1226099/494461-diving-funny-faces.jpg");

        } catch (IOException e) {

            e.printStackTrace();
        }


    }





}
