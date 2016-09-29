package com.shuimunianhua.xianglixiangqin.receiver;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.shuimunianhua.xianglixiangqin.activity.PressLogoutActivity;

public class PressLogoutReceiver extends BroadcastReceiver {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onReceive(final Context context, Intent intent) {
            Intent intentPressLogou = new Intent(context,PressLogoutActivity.class);
            intentPressLogou.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentPressLogou);
    }

}

