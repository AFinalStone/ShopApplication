package com.shuimunianhua.xianglixiangqin.task;

import org.ksoap2.serialization.SoapObject;

import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.util.ProgressDialogUtil;
import android.app.Activity;

/**
 * 连接商品服务器,异步方式连接
 * @author SHI
 * 2016年5月12日 10:49:28
 */
public class ConnectGoodsServiceAsyncTask extends BaseConnectServiceAsyncTask{

	public ConnectGoodsServiceAsyncTask(Activity mContext,
			OnConnectServerStateListener connectServerStateListener,
			SoapObject requestSoapObject, String methodName,
			boolean whetherRefresh) {
		super(mContext, connectServerStateListener, requestSoapObject, methodName,
				whetherRefresh);
	}

	public ConnectGoodsServiceAsyncTask(Activity mContext,
			OnConnectServerStateListener connectServerStateListener,
			SoapObject requestSoapObject, String methodName, Object status,
			boolean whetherRefresh) {
		super(mContext, connectServerStateListener, requestSoapObject, methodName,
				status, whetherRefresh);
	}

	public ConnectGoodsServiceAsyncTask(Activity mContext,
			OnConnectServerStateListener connectServerStateListener,
			SoapObject requestSoapObject, String methodName, Object status) {
		super(mContext, connectServerStateListener, requestSoapObject, methodName,
				status);
	}

	public ConnectGoodsServiceAsyncTask(Activity mContext,
			OnConnectServerStateListener connectServerStateListener,
			SoapObject requestSoapObject, String methodName) {
		super(mContext, connectServerStateListener, requestSoapObject, methodName);
	}

	@Override
	protected String doInBackground(SoapObject... params) {
		ConnectServiceUtil.connectGoodsService(requestSoapObject, methodName, new ConnectServiceUtil.ConnectServiceCallBack() {
			
			@Override
			public void callBack(SoapObject result) {
				if(result != null)
				returnString = result.getPropertyAsString(methodName+RESULT);
				if(whetherShowProgressDialog){
					ProgressDialogUtil.stopProgressDialog();
				}
			}
			
		}, connectOutTime);
		return returnString;
	}
	
}
