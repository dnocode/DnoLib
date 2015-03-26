package com.dnocode.lib.appsample.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.dnocode.lib.appsample.R;
import com.dnocode.lib.appsample.models.Example;
import com.dnocode.lib.business.list.adapters.AutoBindAdapter;
import com.dnocode.lib.widgets.AutoBindingListView;

import java.util.ArrayList;

public class CarouselActivity extends ActionBarActivity {

    AutoBindingListView mListView;

    ArrayList<Example> mList=new ArrayList<Example>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_holder_pattern);




    }





}
