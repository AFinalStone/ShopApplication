package com.shuimunianhua.xianglixiangqin.activity;


import android.content.Intent;

import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;

/****
 * 闪屏页逻辑控制层
 * 
 * @author SHI 2016-2-25 14:28:35
 */
public abstract class SplashActivityController implements OnConnectServerStateListener<Integer>{

	abstract void init(Intent intent);

	abstract void getServerAppVersionData();

	/** 是否更新应用 **/
	abstract void IfUpdateApp(String appVersionDesc);

	abstract void queryToDownApp();

	abstract void cancelToDownApp();
	
	/**跳转到登录界面**/
	abstract void toLoginView();
	
	/**跳转到登录界面**/
	abstract void toGuideView();

	abstract void toMainView();

	abstract void expiredVerificationLogin();

}
