package com.shi.xianglixiangqin.util;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2016/9/10 15:36
 */
public class ImagerLoaderUtilEx {

    private Picasso picasso;
    private Context context;
    private static ImagerLoaderUtilEx imagerLoaderUtilEx;

    public ImagerLoaderUtilEx(Context context) {
        this.context = context;
    }

    public synchronized static ImagerLoaderUtilEx getInstance(Context context){
        if(imagerLoaderUtilEx == null){
            imagerLoaderUtilEx = new ImagerLoaderUtilEx(context);
            imagerLoaderUtilEx.initImageLoader();
        }
        return imagerLoaderUtilEx;
    }

    public void displayMyImage(String imageUrl, ImageView imageView ){
        picasso.load(imageUrl).into(imageView);
    }

    public void displayMyImage(String imageUrl, ImageView imageView,Callback callback ){
        picasso.load(imageUrl).into(imageView, callback);
    }

    public void displayMyImage(String imageUrl, ImageView imageView, int i,Callback callback ){
        picasso.load(imageUrl).into(imageView, callback);
    }

    private void initImageLoader() {
        picasso = Picasso.with(context);
    }
}
