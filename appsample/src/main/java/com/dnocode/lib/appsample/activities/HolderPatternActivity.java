package com.dnocode.lib.appsample.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.dnocode.lib.appsample.R;
import com.dnocode.lib.appsample.models.Example;
import com.dnocode.lib.business.list.adapters.AutoBindAdapter;

import java.util.ArrayList;

public class HolderPatternActivity extends ActionBarActivity {

    ListView mListView;

    ArrayList<Example> mList=new ArrayList<Example>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_holder_pattern);

        mListView= (ListView) findViewById(R.id.listview_example);
        mList.add(new Example("primo",R.drawable.record));
        mList.add(new Example("secondo",R.drawable.recycle_bin));
        mList.add(new Example("terzo",R.drawable.address_book));
        mList.add(new Example("quarto",R.drawable.phone));
        mList.add(new Example("quinto",R.drawable.sound));

        mListView.setAdapter(new AutoBindAdapter(mList,R.layout.element_example));


    }





}
