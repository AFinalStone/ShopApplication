package com.shuimunianhua.xianglixiangqin.activity;

import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.util.ActivityCollectorUtils;
import com.shuimunianhua.xianglixiangqin.view.FragmentCommonDialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/***
 * 
 * @author SHI 
 * 所有Activity的父类
 * 2016-2-1 11:41:42
 *
 */
public abstract class MyBaseActivity extends AppCompatActivity {
	/**当前Activity对象**/
	public FragmentActivity mContext = this;
	/** Standard activity result: operation succeeded. */
	public static final int RESULT_OK           = 1;
	/** Standard activity result: operation canceled. */
	public static final int RESULT_CANCELED    = -1;
	/**当前设备宽度**/
	public int displayDeviceWidth;
	/**当前设备高度**/
	public int displayDeviceHeight;
	/**返回键状态标记**/
	private boolean returnStatus = false;
	/**开启新的Activity时是否带动画**/
	private boolean overridePendingStatusNewActivity = true;
	/**关闭当前Activity时是否带动画**/
	private boolean overridePendingStatusFinish = true;
	/**初始化布局文件**/
	public abstract void initView();
	/**初始化页面数据**/
	public abstract void initData();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollectorUtils.addActivity(this);
		displayDeviceWidth = getResources().getDisplayMetrics().widthPixels;
		displayDeviceHeight = getResources().getDisplayMetrics().heightPixels;
		initView();
		initData();
	}

	/** 设置返回键状态 **/
	protected void setReturnStatu(boolean returnStatu) {
		this.returnStatus = returnStatu;
	}

	//显示是否退出应用的对话框
	protected void showQuitDialog() {

			final FragmentCommonDialog fragmentCommonDialog =  new FragmentCommonDialog();
			fragmentCommonDialog.initView("温馨提示", "确定退出程序吗？", "取消", "退出",
				new FragmentCommonDialog.OnButtonClickListener() {

				@Override
				public void OnOkClick() {
					//关闭应用程序时，不开启动画
					IfOpenFinishActivityAnim(false);
					ActivityCollectorUtils.finishAll();
				}

				@Override
				public void OnCancelClick() {
				}
			});
			fragmentCommonDialog.show(getSupportFragmentManager(), "fragmentCommonDialog");
	}
	
	//开启新的Activity是，开启切换动画
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		if(overridePendingStatusNewActivity){
			IfOpenFinishActivityAnim(false);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}
	}
	
	//开启新的Activity是，开启切换动画
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		if(overridePendingStatusNewActivity){
			IfOpenFinishActivityAnim(false);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}
	}
	
	//防止开启一次Activity,当前Activity退出时没有切换动画了
	@Override
	protected void onResume() {
		super.onResume();
		IfOpenFinishActivityAnim(true);
	}
	
	//关闭当前Activity时，开启切换动画
	@Override
	public void onBackPressed() {
		if(returnStatus){
			showQuitDialog();
		}else{
			if(overridePendingStatusFinish){
				overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			}
			super.onBackPressed();
		}
	}
	
	//关闭当前Activity时，开启切换动画
	@Override
	public void finish() {
		super.finish();
		if(overridePendingStatusFinish){
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
		}
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollectorUtils.removeActivity(this);
		ButterKnife.unbind(this);
	}
	
	/**
	 * 产生新的Activity时是否开启默认切换动画
	 */
	public void IfOpenStartNewActivityAnim(boolean IfOpenAnim){
		overridePendingStatusNewActivity = IfOpenAnim;
	}
	/**
	 * 结束当前Activity时是否开启默认切换动画
	 */
	public void IfOpenFinishActivityAnim(boolean IfOpenAnim){
		overridePendingStatusFinish = IfOpenAnim;
	}


	
}
