package com.shi.xianglixiangqin.rongyun;

import android.content.Context;
import android.content.Intent;

import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;

import io.rong.imlib.RongIMClient;

/**
 * Created by SHI on 2016/6/27.
 */
public class MyConnectionStatusListener implements RongIMClient.ConnectionStatusListener {

    private Context context;

    public MyConnectionStatusListener(Context context) {
        this.context = context;
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {

        switch (connectionStatus) {

            case CONNECTED://连接成功。
                LogUtil.LogShitou("CONNECTED");

                break;
            case DISCONNECTED://断开连接。
                LogUtil.LogShitou("DISCONNECTED");

                break;
            case CONNECTING://连接中。

                LogUtil.LogShitou("CONNECTING");
                break;
            case NETWORK_UNAVAILABLE://网络不可用。

                LogUtil.LogShitou("NETWORK_UNAVAILABLE");
                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
//                LogUtil.LogShitou("用户第二次登陆被执行");
                Intent intent = new Intent(InformationCodeUtil.ReceiverFilterPressUserLogout);
                context.sendBroadcast(intent);
                break;

        }
    }
}
