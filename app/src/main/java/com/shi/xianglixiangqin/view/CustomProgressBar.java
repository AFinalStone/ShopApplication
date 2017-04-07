package com.shi.xianglixiangqin.view;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.TextView;

import com.shi.xianglixiangqin.R;

/***
 * @action 等待框
 * @author SHI
 * 2016-2-3 09:20:29
 */
public class CustomProgressBar extends Dialog {
	
	private Context context = null;
	private AsyncTask asyncTask = null;
	private static CustomProgressBar customProgressBar = null;

	public CustomProgressBar(Context context) {
		super(context);
		this.context = context;
	}

	public CustomProgressBar(Context context, int theme) {
		super(context, theme);
	}

	public static CustomProgressBar createDialog(Context context) {
		stopCustomProgressDialog();
		customProgressBar = new CustomProgressBar(context,R.style.MyProgressDialogStyle);
		customProgressBar.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressBar.setContentView(R.layout.view_loading);
		TextView tv_loadingMsg = (TextView) customProgressBar.findViewById(R.id.tv_loadingMsg);
		tv_loadingMsg.setText(R.string.str_loadingMsg);
		return customProgressBar;
	}
	
	public static CustomProgressBar createDialog(Context context, String loadingMsg) {
		stopCustomProgressDialog();
		customProgressBar = new CustomProgressBar(context,R.style.MyProgressDialogStyle);
		customProgressBar.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressBar.setContentView(R.layout.view_loading);
		TextView tv_loadingMsg = (TextView) customProgressBar.findViewById(R.id.tv_loadingMsg);
		tv_loadingMsg.setText(loadingMsg);
		return customProgressBar;
	}
	
	/***
	 * 
	 * @param mContext 设备上下文
	 * @param strLoadingmsg  资源id
	 * @return
	 */
	public static CustomProgressBar createDialog(Context mContext, int strLoadingmsg) {
		stopCustomProgressDialog();
		customProgressBar = new CustomProgressBar(mContext,R.style.MyProgressDialogStyle);
		customProgressBar.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressBar.setContentView(R.layout.view_loading);
		TextView tv_loadingMsg = (TextView) customProgressBar.findViewById(R.id.tv_loadingMsg);
		tv_loadingMsg.setText(strLoadingmsg);
		return customProgressBar;
	}
	
	/**设置当前进度对话框启动时，在进行操作的后台线程对象引用**/
	public CustomProgressBar setAsyncTask(AsyncTask asyncTask) {

		if(this.asyncTask != null){
			this.asyncTask = null;
		}
		this.asyncTask = asyncTask;
		return customProgressBar;
		
	}

	/**如果当前存在进度对话框，则关闭对话框**/
	public static void stopCustomProgressDialog() {
		if(customProgressBar != null){
			customProgressBar.dismiss();
			customProgressBar = null;
		}
	}
	
	@Override
	public void onBackPressed() {
		if (asyncTask != null) {
			asyncTask.cancel(true);
			asyncTask = null;
		}
		super.onBackPressed();
	}

}
