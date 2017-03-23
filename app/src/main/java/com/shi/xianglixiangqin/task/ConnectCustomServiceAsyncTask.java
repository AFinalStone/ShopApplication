package com.shi.xianglixiangqin.task;

import org.ksoap2.serialization.SoapObject;

import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.util.ProgressDialogUtil;

import android.app.Activity;

/**
 * 连接用户服务器，异步方式连接
 * 连接用户服务器，异步方式连接
 *
 * @author SHI
 *         2016年5月12日 10:49:06
 */
public class ConnectCustomServiceAsyncTask extends BaseConnectServiceAsyncTask {

    public ConnectCustomServiceAsyncTask(Activity mContext,
                                         OnConnectServerStateListener connectServerStateListener,
                                         SoapObject requestSoapObject, String methodName,
                                         boolean whetherRefresh) {
        super(mContext, connectServerStateListener, requestSoapObject, methodName,
                whetherRefresh);
    }

    public ConnectCustomServiceAsyncTask(Activity mContext,
                                         OnConnectServerStateListener connectServerStateListener,
                                         SoapObject requestSoapObject, String methodName, Object status,
                                         boolean whetherRefresh) {
        super(mContext, connectServerStateListener, requestSoapObject, methodName,
                status, whetherRefresh);
    }

    public ConnectCustomServiceAsyncTask(Activity mContext,
                                         OnConnectServerStateListener connectServerStateListener,
                                         SoapObject requestSoapObject, String methodName, Object status) {
        super(mContext, connectServerStateListener, requestSoapObject, methodName,
                status);
    }

    public ConnectCustomServiceAsyncTask(Activity mContext,
                                         OnConnectServerStateListener connectServerStateListener,
                                         SoapObject requestSoapObject, String methodName) {
        super(mContext, connectServerStateListener, requestSoapObject, methodName);
    }

    public ConnectCustomServiceAsyncTask(Activity mContext, SoapObject requestSoapObject, String methodName) {
        super(mContext, requestSoapObject, methodName);
    }

    @Override
    protected SoapObject doInBackground(SoapObject... params) {
        SoapObject resultSoapObject = ConnectServiceUtil.connectCustomService(requestSoapObject, methodName, connectOutTime);
        if (whetherShowProgressDialog) {
            ProgressDialogUtil.stopProgressDialog();
        }
        return resultSoapObject;
    }
//	ConnectServiceUtil.connectCustomService(requestSoapObject, methodName, new ConnectServiceUtil.ConnectServiceCallBack() {
//		@Override
//		public void callBack(SoapObject result) {
//			if(result != null)
//				returnString = result.getPropertyAsString(methodName+RESULT);
//			if(whetherShowProgressDialog){
//				ProgressDialogUtil.stopProgressDialog();
//			}
//		}
//	}, connectOutTime);
//	return returnString;
}
