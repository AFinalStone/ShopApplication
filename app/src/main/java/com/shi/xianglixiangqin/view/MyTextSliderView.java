package com.shi.xianglixiangqin.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.shi.xianglixiangqin.R;

/**
 * Created by SHI on 2017/1/4 16:52
 */
public class MyTextSliderView extends TextSliderView {


    public MyTextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = super.getView();
        //为了消除轮播图底部的半透明黑色状态栏
        View bottomBar = v.findViewById(R.id.description_layout);
        bottomBar.setBackgroundColor(Color.TRANSPARENT);
        return v;
    }
}
