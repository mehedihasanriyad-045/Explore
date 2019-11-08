package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;


@SuppressLint("AppCompatCustomView")
public class likeCheck extends CheckBox {


    public likeCheck(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setButtonDrawable(new StateListDrawable());
    }
    @Override
    public void setChecked(boolean t){
        if(t)
        {
            this.setBackgroundResource(R.drawable.lselect);
        }
        else
        {
            this.setBackgroundResource(R.drawable.ldeselect);
        }
        super.setChecked(t);
    }
}