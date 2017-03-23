package com.shi.xianglixiangqin.activity;


import android.os.Build;
import android.support.v4.app.FragmentTransaction;

import com.shi.xianglixiangqin.BuildConfig;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.frament.LoginFragment_DaiNiFei;
import com.shi.xianglixiangqin.frament.LoginFragment_DeGouKeJi;
import com.shi.xianglixiangqin.frament.LoginFragment_FengTiaoYuShun;
import com.shi.xianglixiangqin.frament.LoginFragment_General;
import com.shi.xianglixiangqin.frament.LoginFragment_HaiJiaoWangLuo;
import com.shi.xianglixiangqin.frament.LoginFragment_JvHe;
import com.shi.xianglixiangqin.frament.LoginFragment_DongMiKeJi;
import com.shi.xianglixiangqin.frament.LoginFragment_ShuiMuNianHua;
import com.shi.xianglixiangqin.frament.LoginFragment_XiangLiXiangQin;
import com.shi.xianglixiangqin.util.InformationCodeUtil;

/***
 * @author SHI
 *         登录页面 带你飞，聚合，批发系统共用一个登陆界面
 *         2016-10-10 14:53:43
 */
public class LoginActivity extends MyBaseActivity {


    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        setReturnStatus(true);
    }

    @Override
    public void initData() {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        if (InformationCodeUtil.AppName_DaiNiFei.equals(getResources().getText(R.string.app_name))) {
            LoginFragment_DaiNiFei mLoginFragment_DaiNiFei = new LoginFragment_DaiNiFei();
            transaction.replace(R.id.frameLayout, mLoginFragment_DaiNiFei, "mLoginFragment_DaiNiFei").commit();
            return;
        }

        if ("聚合批发系统".equals(getResources().getText(R.string.app_name))) {
            LoginFragment_JvHe mLoginFragment_JvHe = new LoginFragment_JvHe();
            transaction.replace(R.id.frameLayout, mLoginFragment_JvHe, "mLoginFragment_JvHe").commit();
            return;
        }
//        if (InformationCodeUtil.AppName_ShuiMuNianHua.equals(getResources().getText(R.string.app_name))) {
//            LoginFragment_ShuiMuNianHua mLoginFragment_ShuiMuNianHua = new LoginFragment_ShuiMuNianHua();
//            transaction.replace(R.id.frameLayout, mLoginFragment_ShuiMuNianHua, "mLoginFragment_ShuiMuNianHua").commit();
//            return;
//        }
//        if (InformationCodeUtil.AppName_DeGouKeJi.equals(getResources().getText(R.string.app_name))) {
//            LoginFragment_DeGouKeJi mLoginFragment_DeGouKeJi = new LoginFragment_DeGouKeJi();
//            transaction.replace(R.id.frameLayout, mLoginFragment_DeGouKeJi, "mLoginFragment_DeGouKeJi").commit();
//            return;
//        }
//        if ( InformationCodeUtil.AppName_FengTiaoYuShun.equals(getResources().getText(R.string.app_name))) {
//            LoginFragment_FengTiaoYuShun mLoginFragment_FengTiaoYuShun = new LoginFragment_FengTiaoYuShun();
//            transaction.replace(R.id.frameLayout, mLoginFragment_FengTiaoYuShun, "mLoginFragment_FengTiaoYuShun").commit();
//            return;
//        }
//        if (InformationCodeUtil.AppName_XiangLiXiangQin.equals(getResources().getText(R.string.app_name))) {
//            LoginFragment_XiangLiXiangQin mLoginFragment_XiangLiXiangQin = new LoginFragment_XiangLiXiangQin();
//            transaction.replace(R.id.frameLayout, mLoginFragment_XiangLiXiangQin, "mLoginFragment_XiangLiXiangQin").commit();
//            return;
//        }
//        if (InformationCodeUtil.AppName_DongMiKeJi.equals(getResources().getText(R.string.app_name))) {
//            LoginFragment_DongMiKeJi mLoginFragment_DongMiKeJi = new LoginFragment_DongMiKeJi();
//            transaction.replace(R.id.frameLayout, mLoginFragment_DongMiKeJi, "mLoginFragment_DongMiKeJi").commit();
//            return;
//        }
        switch (BuildConfig.LOGIN_MODEL_NUMBER){
            case 1:
                LoginFragment_XiangLiXiangQin mLoginFragment_XiangLiXiangQin = new LoginFragment_XiangLiXiangQin();
                transaction.replace(R.id.frameLayout, mLoginFragment_XiangLiXiangQin, "mLoginFragment_XiangLiXiangQin").commit();
                break;
            case 2:
                LoginFragment_ShuiMuNianHua mLoginFragment_ShuiMuNianHua = new LoginFragment_ShuiMuNianHua();
                transaction.replace(R.id.frameLayout, mLoginFragment_ShuiMuNianHua, "mLoginFragment_ShuiMuNianHua").commit();
                break;
            case 3:
                LoginFragment_DongMiKeJi mLoginFragment_DongMiKeJi = new LoginFragment_DongMiKeJi();
                transaction.replace(R.id.frameLayout, mLoginFragment_DongMiKeJi, "mLoginFragment_DongMiKeJi").commit();
                break;
            case 4:
                LoginFragment_FengTiaoYuShun mLoginFragment_FengTiaoYuShun = new LoginFragment_FengTiaoYuShun();
                transaction.replace(R.id.frameLayout, mLoginFragment_FengTiaoYuShun, "mLoginFragment_FengTiaoYuShun").commit();
                break;
            case 5:
                LoginFragment_DeGouKeJi mLoginFragment_DeGouKeJi = new LoginFragment_DeGouKeJi();
                transaction.replace(R.id.frameLayout, mLoginFragment_DeGouKeJi, "mLoginFragment_DeGouKeJi").commit();
                break;
            case 6:
                LoginFragment_HaiJiaoWangLuo mLoginFragment_HaiJiaoWangLuo = new LoginFragment_HaiJiaoWangLuo();
                transaction.replace(R.id.frameLayout, mLoginFragment_HaiJiaoWangLuo, "mLoginFragment_HaiJiaoWangLuo").commit();
                break;
            default:
                LoginFragment_General mLoginFragment_General = new LoginFragment_General();
                transaction.replace(R.id.frameLayout, mLoginFragment_General, "mLoginFragment_General").commit();
        }


    }
}










