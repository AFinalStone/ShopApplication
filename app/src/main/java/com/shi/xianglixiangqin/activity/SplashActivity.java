package com.shi.xianglixiangqin.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.shi.xianglixiangqin.BuildConfig;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.service.UpdateAppService;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.view.FragmentViewDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * 闪屏页面
 * @author SHI
 * @time 2016/8/8 17:50
 */
public class SplashActivity extends MyBaseActivity {

    /**
     * 加载进度条
     **/
    @BindView(R.id.iv_splash_loading)
    ImageView iv_splash_loading;
    /**
     * 逻辑业务控制器
     **/
    private SplashActivityController controller;

    @Override
    public void initView() {
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        if(InformationCodeUtil.AppName_DaiNiFei.equals(getResources().getText(R.string.app_name))){
            controller = new SplashActivityControllerImpDaiNiFei(SplashActivity.this);
        }else{
            controller = new SplashActivityControllerImpJvHeAndPiFa(SplashActivity.this);
        }
        controller.init();
    }

    @Override
    public void initData() {
        Animation translate = AnimationUtils.loadAnimation(this,
                R.anim.splash_loading);
        iv_splash_loading.setAnimation(translate);

    }

    public void startServiceForAppUpdate() {
//        /**调用系统自带浏览器直接下载文件**/
//		Uri uri = Uri.parse(mReleaseModel.getAddress());
//		Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
//		startActivity(downloadIntent);
//        /**调用系统自带浏览器进入下载界面**/
        String url_appInfo = "http://a.app.qq.com/o/simple.jsp?pkgname="+getApplicationInfo().processName;
//      String url_appInfo = "http://www.dainif.com/app/appInfo/appInfo.html?" +
//      "appName=" + getResources().getString(R.string.app_name);
        Uri uri = Uri.parse(url_appInfo);
		Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(downloadIntent);
        //开启服务下载
//      Intent intent = new Intent(mContext, UpdateAppService.class);
//      intent.putExtra(InformationCodeUtil.IntentServiceAddressOfApkDownload, addressOfApkDownload);
//      startService(intent);
    }

    public void showUpdateDialog(String newVersionName, String newVersionSize, String newVersionDesc
            , final boolean IfCanDownApp, String strOk, final boolean IfCanLoginApp,String strCancel) {
        final FragmentViewDialog updateDialog = new FragmentViewDialog();

        View view = View.inflate(mContext,R.layout.dialog_version_update_tip,null);
        TextView tv_newVersionName = (TextView) view.findViewById(R.id.tv_newVersionName);
        TextView tv_newVersionSize = (TextView) view.findViewById(R.id.tv_newVersionSize);
        TextView tv_newVersionDesc = (TextView) view.findViewById(R.id.tv_newVersionDesc);

        tv_newVersionName.setText(newVersionName);
        tv_newVersionSize.setText(newVersionSize);
        tv_newVersionDesc.setText(newVersionDesc);

        updateDialog.initView(view, strCancel, strOk, new FragmentViewDialog.OnButtonClickListener() {
            @Override
            public void OnOkClick() {

                if(IfCanDownApp){
                    controller.queryToDownApp();
                }else{
                    finish();
                }
            }

            @Override
            public void OnCancelClick() {
                if(IfCanLoginApp){
                    controller.cancelToDownApp();
                }else{
                    finish();
                }
            }
        });

        updateDialog.show(getSupportFragmentManager(), "FragmentViewDialog");
    }

    public void toGuideView() {
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
        finish();
    }

    public void toLoginView() {
		Intent intent = new Intent(mContext, LoginActivity.class);
		startActivity(intent);
		finish();
    }

    public void toMainView() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
