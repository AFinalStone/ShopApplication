package com.shuimunianhua.xianglixiangqin.activity;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import butterknife.ButterKnife;
import butterknife.Bind;

import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.service.UpdateAppService;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.view.FragmentViewDialog;

/**
 * 闪屏页面
 * @author SHI
 * @time 2016/8/8 17:50
 */
public class SplashActivity extends MyBaseActivity {

    /**
     * 加载进度条
     **/
    @Bind(R.id.iv_splash_loading)
    ImageView iv_splash_loading;
    /**
     * 逻辑业务控制器
     **/
    private SplashActivityController controller;

    @Override
    public void initView() {
       if(InformationCodeUtil.whetherIsDaiNiFei){
           setContentView(R.layout.activity_splash_dainifei);
           controller = new SplashActivityControllerImpDaiNiFei(SplashActivity.this);
       }else{
           setContentView(R.layout.activity_splash_jvhe);
           controller = new SplashActivityControllerImpJvHe(SplashActivity.this);
       }
        ButterKnife.bind(this);
        controller.init(getIntent());
    }

    @Override
    public void initData() {
        Animation translate = AnimationUtils.loadAnimation(this,
                R.anim.splash_loading);
        iv_splash_loading.setAnimation(translate);
    }

    public void startServiceForAppUpdate(String addressOfApkDownload) {
        //调用系统自带浏览器下载
//		Uri uri = Uri.parse(mReleaseModel.getAddress());  
//		Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);  
//		startActivity(downloadIntent); 
        //开启服务下载
        Intent intent = new Intent(mContext, UpdateAppService.class);
        intent.putExtra(InformationCodeUtil.IntentServiceAddressOfApkDownload, addressOfApkDownload);
        startService(intent);
    }

    public void showUpdateDialog(String newVersionName, String newVersionSize, String newVersionDesc, final boolean IfCanLoginApp) {
        final FragmentViewDialog updateDailog = new FragmentViewDialog();

        View view = View.inflate(mContext,R.layout.dialog_version_update_tip,null);
        TextView tv_newVersionName = (TextView) view.findViewById(R.id.tv_newVersionName);
        TextView tv_newVersionSize = (TextView) view.findViewById(R.id.tv_newVersionSize);
        TextView tv_newVersionDesc = (TextView) view.findViewById(R.id.tv_newVersionDesc);

        tv_newVersionName.setText(newVersionName);
        tv_newVersionSize.setText(newVersionSize);
        tv_newVersionDesc.setText(newVersionDesc);

        updateDailog.initView(view, "稍后再说", "马上更新", new FragmentViewDialog.OnButtonClickListener() {
            @Override
            public void OnOkClick() {
                controller.queryToDownApp();
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

        updateDailog.show(getSupportFragmentManager(), "fragmentDailog");

    }

    public void toGuideView() {
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
        finish();
    }

    public void toLoginView_DaiNiFei() {
        Intent intent = new Intent(mContext, LoginDaiNiFeiActivity.class);
        startActivity(intent);
        finish();
    }

    public void toLoginView_JvHe() {
		Intent intent = new Intent(mContext, LoginShuiMuNianHuaActivity.class);
		startActivity(intent);
		finish();
    }

    public void toMainView() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    //j极光推送相关数据
    public static boolean isForeground = false;
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static final String MESSAGE_RECEIVED_ACTION = "com.shuimunianhua.xianglixiangqin.MESSAGE_RECEIVED_ACTION";


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        JPushInterface.onResume(this);
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    public void onBackPressed() {
        IfOpenFinishActivityAnim(false);
        finish();
    }

}
