package com.shi.xianglixiangqin.activity;

import android.content.Intent;

import com.shi.xianglixiangqin.util.ActivityCollectorUtil;
import com.shi.xianglixiangqin.util.PreferencesUtilMy;
import com.shi.xianglixiangqin.view.FragmentOkAndCancelDialog;

import java.util.LinkedHashSet;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by SHI on 2016/6/27.
 */
public class PressLogoutActivity extends MyBaseActivity {

    @Override
    public void initView() {
        FragmentOkAndCancelDialog fDialog = new FragmentOkAndCancelDialog();
        String line = System.getProperties().getProperty("line.separator");
         
        JPushInterface.setTags(mContext,new LinkedHashSet<String>(),null);
        PreferencesUtilMy.clearCustomModel(mContext);
        fDialog.initView("下线通知", "您的账号在别处登陆，您被迫下线。" + line + "若非本人操作，请尽快修改密码。", "确定", "重新登陆", new FragmentOkAndCancelDialog.OnButtonClickListener() {
            @Override
            public void OnOkClick() {
                toLoginView();
            }

            @Override
            public void OnCancelClick() {
                //关闭应用程序时，不开启动画
                ActivityCollectorUtil.finishAll();
            }
        });
        fDialog.show(getSupportFragmentManager(), "fragmentCommonDailog");
    }

    @Override
    public void initData() {
    }

    /**
     * 注销账号，到登陆界面
     **/
    private void toLoginView() {
        PreferencesUtilMy.clearCustomModel(mContext);
        ActivityCollectorUtil.finishAll();
        Intent intent = new Intent(mContext,
                LoginActivity.class);
        startActivity(intent);

    }
}
