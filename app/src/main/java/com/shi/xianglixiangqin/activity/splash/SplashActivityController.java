package com.shi.xianglixiangqin.activity.splash;


import android.os.SystemClock;

import com.google.gson.Gson;
import com.sea_monster.common.Md5;
import com.shi.xianglixiangqin.BuildConfig;
import com.shi.xianglixiangqin.bean.ApkVersionDataBean;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.model.CustomModel;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.Md5Util;
import com.shi.xianglixiangqin.util.PreferencesUtil;
import com.shi.xianglixiangqin.util.StringUtil;
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
    int currentAppVersionCode;

    String url_apkWebHtml = "http://a.app.qq.com/o/simple.jsp?pkgname=" + BuildConfig.APPLICATION_ID;
    String flagBegin = "{\"appId\":";
    String flagEnd = ",\"isNew\"";
    boolean flagMustUpdate = false;
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
        currentAppVersionCode = SystemUtil.getCurrentAppVersionCode(mSplashActivity);
        loadApkVersionData();
    }

    public void loadApkVersionData() {
        OkHttpUtils.get()
                .url(url_apkWebHtml)
                .build()
                .connTimeOut(5000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        cancelToDownApp();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            IfUpdateApp(response);
                        } else {
                            cancelToDownApp();
                        }
                    }
                });
    }

    /**
     * 是否更新应用
     **/
    public void IfUpdateApp(String appVersionDesc) {
        //抓网页数据，进行数据解析
        try {
            int positionStart = appVersionDesc.indexOf(flagBegin);
            int positionEnd = appVersionDesc.indexOf(flagEnd);
            String strInfo = appVersionDesc.substring(positionStart, positionEnd);
            LogUtil.LogShitou("ApkVersionDataBean", strInfo);
            Gson gson = new Gson();
            ApkVersionDataBean apkVersionDataBean = gson.fromJson(strInfo, ApkVersionDataBean.class);
            //我们的app描述介绍中如果有立即更新字段，强制用户更新软件，
            if (apkVersionDataBean.getNewFeature().contains("立即更新")) {
                flagMustUpdate = true;
            }
            //不需要更新
            if (apkVersionDataBean.getVersionCode() <= currentAppVersionCode) {
                cancelToDownApp();
                return;
            }
            //强制更新
            if (flagMustUpdate) {
                PreferencesUtil.putBoolean(mSplashActivity, InformationCodeUtil.KeyShowUpdateDialog, true);
                mSplashActivity.showMustUpdateDialog(apkVersionDataBean);
                return;
            }
            //检查用户是否勾选了 “不再提示” 按钮
            if (PreferencesUtil.getBoolean(mSplashActivity, InformationCodeUtil.KeyShowUpdateDialog, true)) {
                mSplashActivity.showUpdateDialog(apkVersionDataBean);
                return;
            }
            //用户要求不提示更新对话框
            cancelToDownApp();
        } catch (Exception e) {
            cancelToDownApp();
        }

    }

    void queryToDownApp(ApkVersionDataBean apkBean) {
        //获取安装包下载地址
        final String addressOfApkDownload = apkBean.getApkUrl();
        //获取安装包名称    并获取下载SD卡位置
        final String apkName = SystemUtil.getCurrentAppPackageName(mSplashActivity) + ".apk";
        final String apkFiles = SystemUtil.getExtendCachFiles(mSplashActivity).getAbsolutePath();
        LogUtil.LogShitou("apkName", apkName);
        LogUtil.LogShitou("apkFiles", apkFiles);
        String apkPath = apkFiles+System.getProperty("file.separator")+apkName;
        if(haveDownApp(apkPath, apkBean.getApkMd5()))
        {
            mSplashActivity.install(new File(apkPath));
            return;
        }
        //OkHttp
        OkHttpUtils.get()
                .url(addressOfApkDownload)
                .build()
                .execute(new FileCallBack(apkFiles, apkName) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        mSplashActivity.showMessage(e.getMessage());
                        cancelToDownApp();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        mSplashActivity.install(response);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        mSplashActivity.showDowningProgress((int) progress / 1024, (int) total / 1024);
                    }
                });
    }

    /**是否已经下载过更新文件了**/
    public boolean haveDownApp(String path, String md5AppCurrent){

        File file = new File(path);
        if(!file.exists()){
            LogUtil.LogShitou("文件不存在");
            return false;
        }
        String md5AppHaveDown = Md5Util.getMd5ByFile(file);
        if(StringUtil.isEmpty(md5AppHaveDown)){
            return false;
        }
        if(!md5AppHaveDown.equals(md5AppCurrent)){
            return false;
        }
        return true;
    }

    /**不进行文件下载**/
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
        long endTime = System.currentTimeMillis();
        final long lengthTime = endTime - startTime;
        if (lengthTime < sleepTime) {
            new Thread() {
                public void run() {
                    SystemClock.sleep(sleepTime - lengthTime);
                    mSplashActivity.toLoginView();
                }
            }.start();
        } else {
            mSplashActivity.toLoginView();
        }
    }

    /**
     * 跳转到引导界面
     **/
    public void toGuideView() {
        long endTime = System.currentTimeMillis();
        final long lengthTime = endTime - startTime;
        if (lengthTime < sleepTime) {
            new Thread() {
                public void run() {
                    SystemClock.sleep(sleepTime - lengthTime);
                    mSplashActivity.toGuideView();
                }
            }.start();
        } else {
            mSplashActivity.toGuideView();
        }
    }

    /**跳转到主页**/
    public void toMainView() {
        long endTime = System.currentTimeMillis();
        final long lengthTime = endTime - startTime;
        if (lengthTime < sleepTime) {
            new Thread() {
                public void run() {
                    SystemClock.sleep(sleepTime - lengthTime);
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
