package com.dnocode.lib.appsample.models;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dnocode.lib.appsample.R;
import com.dnocode.lib.business.list.annotations.Bind;

public class Example {

        @Bind(to = R.id.name,type = TextView.class)
        public String name;

        @Bind(to = {R.id.img1,R.id.img2},type ={ImageView.class, ImageButton.class})
        public int resourceImage;

        public Example(String name,int resourceId){

            this.name=name;
            this.resourceImage=resourceId;


        }

    }