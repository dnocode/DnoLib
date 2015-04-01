package com.dnocode.lib.appsample.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import com.dnocode.lib.widgets.ShaoeSelectorView;

/**
 * Created by dino on 01/04/15.
 */
public class CustomViewActivity  extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FrameLayoutx f=new FrameLayoutx(this);

        ShaoeSelectorView xx=new ShaoeSelectorView(this);

        f.addView(xx);

        setContentView(f);
    }
}


class FrameLayoutx extends FrameLayout{


    public FrameLayoutx(Context context) {
        super(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.i("root","misurazione di sto cazzo");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
