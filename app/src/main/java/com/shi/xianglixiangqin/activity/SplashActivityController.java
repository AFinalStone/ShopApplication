package com.shi.xianglixiangqin.activity;


import android.content.Intent;
import android.os.SystemClock;

import com.google.gson.Gson;
import com.shi.xianglixiangqin.BuildConfig;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.model.AppVersionModel;
import com.shi.xianglixiangqin.model.CustomModel;
import com.shi.xianglixiangqin.util.FileUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.PreferencesUtil;
import com.shi.xianglixiangqin.util.SystemUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;

/****
 * 闪屏页逻辑控制层
 *
 * @author SHI 2016-2-25 14:28:35
 */
public abstract class SplashActivityController implements OnConnectServerStateListener<Integer> {

    /**
     * 当前应用版本号
     **/
    float currentAppVersionCode;
    /**
     * 应用信息bean
     **/
    AppVersionModel appVersionBean_New;
    /**
     * 闪屏页面
     **/
    SplashActivity mSplashActivity;
    /**
     * 开始时间
     **/
    long startTime;
    /**
     * 休眠时间
     **/
    final long sleepTime = 5000;
    /**
     * 当前登录用户对象
     **/
    CustomModel mCustomModel;

    public void init() {
        startTime = System.currentTimeMillis();
        currentAppVersionCode = SystemUtil
                .getCurrentAppVersionCode(mSplashActivity);
        getServerAppVersionData();
    }

    void getServerAppVersionData() {

        String urlVersionDesc = BuildConfig.URL_UPDATES;
        OkHttpUtils.get()
                .url(urlVersionDesc)
                .build()
                .connTimeOut(3000)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String result) {
                        if (result != null) {
                            IfUpdateApp(result);
                        } else {
                            cancelToDownApp();
                        }
                        LogUtil.LogShitou("onResponse");
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        cancelToDownApp();
                        LogUtil.LogShitou("onError");
                    }
                });

    }

    /**
     * 是否更新应用
     **/
    public void IfUpdateApp(String appVersionDesc) {
        Gson gson = new Gson();
        try {
            appVersionBean_New = gson.fromJson(appVersionDesc,
                    AppVersionModel.class);
            if (appVersionBean_New.getVersionCode() > currentAppVersionCode) {
                if (BuildConfig.AUTO_UPDATES) {
                    mSplashActivity.showUpdateDialog(appVersionBean_New.getVersionName()
                            , appVersionBean_New.getVersionSize(), appVersionBean_New.getVersionDesc()
                            , true, "马上更新", appVersionBean_New.getSign(), "稍后更新");
                } else {
                    mSplashActivity.showUpdateDialog(appVersionBean_New.getVersionName()
                            , appVersionBean_New.getVersionSize(), appVersionBean_New.getVersionDesc() + "\n\n赶快去应用宝下载更新吧!"
                            , false, "确定", appVersionBean_New.getSign(), "取消");
                }
            } else {
                cancelToDownApp();
            }

        } catch (Exception e) {
            cancelToDownApp();
        }

    }

    void queryToDownApp() {
        mSplashActivity.startServiceForAppUpdate();
        mSplashActivity.finish();
    }

    void cancelToDownApp() {

        boolean flagIsFirstLogin = PreferencesUtil.getBoolean(mSplashActivity,
                InformationCodeUtil.KeyFirstOpenApp, true);

        if (flagIsFirstLogin) {
            toGuideView();
        } else {
            expiredVerificationLogin();
        }
    }

    /**
     * 跳转到登录界面
     **/
    public void toLoginView() {
        //如果开始登录，则把本地下载的app删除掉
        try {
            String filePath = FileUtil.getDownFilePath(InformationCodeUtil.NameOfCurrentVersion);
            File file = new File(filePath);
            FileUtil.removeDirectory(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        final long lengTime = endTime - startTime;
        if (lengTime < sleepTime) {
            new Thread() {
                public void run() {
                    SystemClock.sleep(sleepTime - lengTime);
                    mSplashActivity.toLoginView();
                }

                ;
            }.start();
        } else {
            mSplashActivity.toLoginView();
        }
    }

    /**跳转到登录界面**/

    /**
     * 跳转到引导界面
     **/
    public void toGuideView() {
        //如果开始登录，则把本地下载的app删除掉
        try {
            String filePath = FileUtil.getDownFilePath(InformationCodeUtil.NameOfCurrentVersion);
            File file = new File(filePath);
            FileUtil.removeDirectory(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        final long lengTime = endTime - startTime;
        if (lengTime < sleepTime) {
            new Thread() {
                public void run() {
                    SystemClock.sleep(sleepTime - lengTime);
                    mSplashActivity.toGuideView();
                }

                ;
            }.start();
        } else {
            mSplashActivity.toGuideView();
        }
    }

    public void toMainView() {
        //如果开始登录，则把本地下载的app删除掉
        try {
            String filePath = FileUtil.getDownFilePath(InformationCodeUtil.NameOfCurrentVersion);
            File file = new File(filePath);
            FileUtil.removeDirectory(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        final long lengTime = endTime - startTime;
        if (lengTime < sleepTime) {
            new Thread() {
                public void run() {
                    SystemClock.sleep(sleepTime - lengTime);
                    mSplashActivity.toMainView();
                }

                ;
            }.start();
        } else {
            mSplashActivity.toMainView();
        }
    }

    abstract void expiredVerificationLogin();

}
