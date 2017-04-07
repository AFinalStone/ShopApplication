package com.shi.xianglixiangqin.util;

import android.app.Activity;
import android.os.AsyncTask;

import com.shi.xianglixiangqin.view.CustomProgressBar;

/***
 * @action 提示框工具类
 * @author ZHU
 * @date 2015-8-16 下午8:34:23
 */
public class ProgressDialogUtil {
	/** 进度对话框 **/
	private static CustomProgressBar progressDialog;

	/** 显示进度对话框 **/
	public synchronized static void startProgressDialog(Activity mContext) {
		if( mContext.isFinishing()){
			return;
		}
		progressDialog = CustomProgressBar.createDialog(mContext);
		progressDialog.show();
	}
	
	public synchronized static void startProgressDialog(Activity mContext, String loadingMsg) {
		if(mContext.isFinishing()){
			return;
		}
		progressDialog = CustomProgressBar.createDialog(mContext,loadingMsg);
		progressDialog.show();
			
	}
	

	public static void startProgressDialog(Activity mContext, int strLoadingmsg) {
		if(mContext.isFinishing()){
			return;
		}
		progressDialog = CustomProgressBar.createDialog(mContext,strLoadingmsg);
		progressDialog.show();
	}
	
	public synchronized static void startProgressDialog(Activity mContext, String loadingMsg, AsyncTask asyncTask) {
		if(mContext.isFinishing()){
			return;
		}
		progressDialog = CustomProgressBar
				.createDialog(mContext,loadingMsg)
				.setAsyncTask(asyncTask);
		progressDialog.show();
	}

	/** 关闭进度对话框**/
	public synchronized static void stopProgressDialog() {
		progressDialog.stopCustomProgressDialog();
	}


}
