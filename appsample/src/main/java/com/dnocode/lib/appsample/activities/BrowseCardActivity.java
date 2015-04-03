package com.dnocode.lib.appsample.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.dnocode.lib.appsample.R;
import com.dnocode.lib.widgets.images.BrowseCardsView;

import java.io.IOException;

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
