package com.shuimunianhua.xianglixiangqin.activity;

import android.content.Intent;

import com.shuimunianhua.xianglixiangqin.util.ActivityCollectorUtils;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.PreferencesUtilMy;
import com.shuimunianhua.xianglixiangqin.view.FragmentCommonDialog;

/**
 * Created by SHI on 2016/6/27.
 */
public class PressLogoutActivity extends MyBaseActivity{

    @Override
    public void initView() {
        FragmentCommonDialog fDialog = new FragmentCommonDialog();
        String line = System.getProperties().getProperty("line.separator");
        fDialog.initView("下线通知", "您的账号在别处登陆，您被迫下线。"+line+"若非本人操作，请尽快修改密码。", "确定", "重新登陆", new FragmentCommonDialog.OnButtonClickListener() {
            @Override
            public void OnOkClick() {
                toLoginView();
            }

            @Override
            public void OnCancelClick() {
                //关闭应用程序时，不开启动画
                ActivityCollectorUtils.finishAll();
            }
        });
        fDialog.show(getSupportFragmentManager(),"fragmentCommonDailog");

    }

    @Override
    public void initData() {
    }

    /**注销账号，到登陆界面**/
    private void toLoginView(){
        PreferencesUtilMy.clearCustomModel(mContext);
        ActivityCollectorUtils.finishAll();
        if(InformationCodeUtil.whetherIsDaiNiFei){
            Intent intent = new Intent(mContext,
                    LoginDaiNiFeiActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(mContext,
                    LoginShuiMuNianHuaActivity.class);
            startActivity(intent);
        }
    }
}
