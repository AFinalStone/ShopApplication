package com.shi.xianglixiangqin.activity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


/***
 * 
 * @author SHI 
 * 所有主题风格为Dialog的Activity的父类，设置其宽度等于手机屏幕宽度
 * 2016-2-1 11:41:42
 *
 */
public abstract class MyBaseTranslucentActivity extends MyBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//窗口对齐屏幕宽度
		Window win = this.getWindow();
//		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		win.setAttributes(lp);
	}


	
}
