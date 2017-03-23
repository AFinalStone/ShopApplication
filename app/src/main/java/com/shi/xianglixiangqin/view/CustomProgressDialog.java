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
public class CustomProgressDialog extends Dialog {
	
	private Context context = null;
	private AsyncTask asyncTask = null;
	private static CustomProgressDialog customProgressDialog = null;

	public CustomProgressDialog(Context context) {
		super(context);
		this.context = context;
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static CustomProgressDialog createDialog(Context context) {
		stopCustomProgressDialog();
		customProgressDialog = new CustomProgressDialog(context,R.style.MyProgressDialogStyle);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressDialog.setContentView(R.layout.view_loading);
		TextView tv_loadingMsg = (TextView) customProgressDialog.findViewById(R.id.tv_loadingMsg);
		tv_loadingMsg.setText(R.string.str_loadingMsg);
		return customProgressDialog;
	}
	
	public static CustomProgressDialog createDialog(Context context, String loadingMsg) {
		stopCustomProgressDialog();
		customProgressDialog = new CustomProgressDialog(context,R.style.MyProgressDialogStyle);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressDialog.setContentView(R.layout.view_loading);
		TextView tv_loadingMsg = (TextView) customProgressDialog.findViewById(R.id.tv_loadingMsg);
		tv_loadingMsg.setText(loadingMsg);
		return customProgressDialog;
	}
	
	/***
	 * 
	 * @param mContext 设备上下文
	 * @param strLoadingmsg  资源id
	 * @return
	 */
	public static CustomProgressDialog createDialog(Context mContext, int strLoadingmsg) {
		stopCustomProgressDialog();
		customProgressDialog = new CustomProgressDialog(mContext,R.style.MyProgressDialogStyle);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressDialog.setContentView(R.layout.view_loading);
		TextView tv_loadingMsg = (TextView) customProgressDialog.findViewById(R.id.tv_loadingMsg);
		tv_loadingMsg.setText(strLoadingmsg);
		return customProgressDialog;
	}
	
	/**设置当前进度对话框启动时，在进行操作的后台线程对象引用**/
	public CustomProgressDialog setAsyncTask(AsyncTask asyncTask) {

		if(this.asyncTask != null){
			this.asyncTask = null;
		}
		this.asyncTask = asyncTask;
		return customProgressDialog;
		
	}

	/**如果当前存在进度对话框，则关闭对话框**/
	public static void stopCustomProgressDialog() {
		if(customProgressDialog != null){
			customProgressDialog.dismiss();
			customProgressDialog = null;
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
