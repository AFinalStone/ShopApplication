package com.shi.xianglixiangqin.task;

import org.ksoap2.serialization.SoapObject;

import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.util.ProgressDialogUtil;
import com.shi.xianglixiangqin.util.StringUtil;

import android.app.Activity;
import android.os.AsyncTask;

/**
 * 连接网络线程任务父类
 * @author SHI
 * 2016年5月12日 10:49:51
 */
public abstract class BaseConnectServiceAsyncTask extends AsyncTask<SoapObject, SoapObject, SoapObject> {

	/**上下文对象**/
	protected Activity mContext;
	/**函数名称**/
	protected String methodName;
	protected final String RESULT = "Result";
	/**请求的参数对象**/
	protected SoapObject requestSoapObject;
	/**连接服务器请求数据状况监听接口对象**/
	protected OnConnectServerStateListener connectServerStateListener;
	/**状态**/
	protected Object status;
	/**返回的数据对象**/
	protected SoapObject returnSoapObject;
//	protected String returnString;
	/**是否是刷新消息**/
	protected boolean whetherRefresh = true;
	/**加载过程是否显示加载对话框**/
	protected boolean whetherShowProgressDialog = true;	
	/**加载对话框标题**/
	protected String strLoadingMsg = "请稍等...";	
	/**连接超时时间**/
	protected int connectOutTime = 10000;

	/***
	 * @param mContext 设备上下文
	 * @param connectServerStateListener 连接状态接听接口
	 * @param requestSoapObject 请求参数对象
	 * @param methodName 请求函数名称
	 * @param status 请求状态码
	 * @param whetherRefresh 请求是刷新或者加载，true为刷新，false为获取更多数据
	 */
	public BaseConnectServiceAsyncTask(Activity mContext,
			OnConnectServerStateListener connectServerStateListener,
			SoapObject requestSoapObject,String methodName,Object status,boolean whetherRefresh) {
		super();
		this.mContext = mContext;
		this.connectServerStateListener = connectServerStateListener;
		this.requestSoapObject = requestSoapObject;
		this.methodName = methodName;
		this.status = status;
		this.whetherRefresh = whetherRefresh;
	}
	
	/***
	 * @param mContext 设备上下文
	 * @param connectServerStateListener 连接状态接听接口
	 * @param requestSoapObject 请求参数对象
	 * @param methodName 请求函数名称
	 */
	public BaseConnectServiceAsyncTask(Activity mContext,
			OnConnectServerStateListener connectServerStateListener,
			SoapObject requestSoapObject,String methodName) {
		super();
		this.mContext = mContext;
		this.connectServerStateListener = connectServerStateListener;
		this.requestSoapObject = requestSoapObject;
		this.methodName = methodName;
	}
	
	/***
	 * @param mContext 设备上下文
	 * @param connectServerStateListener 连接状态接听接口
	 * @param requestSoapObject 请求参数对象
	 * @param methodName 请求函数名称
	 * @param status 请求状态码
	 */
	public BaseConnectServiceAsyncTask(Activity mContext,
			OnConnectServerStateListener connectServerStateListener,
			SoapObject requestSoapObject,String methodName,Object status) {
		super();
		this.mContext = mContext;
		this.connectServerStateListener = connectServerStateListener;
		this.requestSoapObject = requestSoapObject;
		this.methodName = methodName;
		this.status = status;
	}
	
	/***
	 * @param mContext 设备上下文
	 * @param connectServerStateListener 连接状态接听接口
	 * @param requestSoapObject 请求参数对象
	 * @param methodName 请求函数名称
	 * @param whetherRefresh 请求是刷新或者加载，true为刷新，false为获取更多数据
	 */
	public BaseConnectServiceAsyncTask(Activity mContext,
			OnConnectServerStateListener connectServerStateListener,
			SoapObject requestSoapObject,String methodName,boolean whetherRefresh) {
		super();
		this.mContext = mContext;
		this.connectServerStateListener = connectServerStateListener;
		this.requestSoapObject = requestSoapObject;
		this.methodName = methodName;
		this.whetherRefresh = whetherRefresh;
	}

	public BaseConnectServiceAsyncTask(Activity mContext,SoapObject requestSoapObject,String methodName) {
		super();
		this.mContext = mContext;
		this.requestSoapObject = requestSoapObject;
		this.methodName = methodName;
	}
	
	/**
	 * 初始化加载网络对话框
	 * @param whetherShowProgressDialog 是否显示网络加载对话框
	 */
	public void initProgressDialog(boolean whetherShowProgressDialog){
		this.whetherShowProgressDialog = whetherShowProgressDialog;
	}
	
	/**
	 * 初始化网络加载对话框
	 * @param whetherShowProgressDialog 是否显示网络加载对话框
	 * @param strLoadingMsg 加载网络对话框显示内容
	 */
	public void initProgressDialog(boolean whetherShowProgressDialog, String strLoadingMsg){
		this.whetherShowProgressDialog = whetherShowProgressDialog;
		this.strLoadingMsg = strLoadingMsg;
	}
	
	/**
	 * 设置连接超时时间
	 * @param connectOutTime 连接超时时间
	 */
	public void setConnectOutTime(int connectOutTime){
		this.connectOutTime = connectOutTime;
	}
	
	@Override
	protected void onPreExecute() {
		if(whetherShowProgressDialog){
			ProgressDialogUtil.startProgressDialog( mContext, strLoadingMsg, this);
		}
	}
	
	@Override
	protected void onCancelled(SoapObject resultSoapObject) {
		String strError = resultSoapObject.getPropertyAsString(ConnectServiceUtil.ErrorMsg);
		//请求成功,但是用户取消了操作
		if(StringUtil.isEmpty(strError)) {
			if(connectServerStateListener != null){
				String returnString = resultSoapObject.getPropertyAsString(methodName+RESULT);
				connectServerStateListener.connectServiceCancelled(returnString, methodName, status, whetherRefresh);
			}
		}else{
			if (connectServerStateListener != null){
				connectServerStateListener.connectServiceFailed(strError, methodName, status, whetherRefresh);
			}
		}
	}
	
	@Override
	protected void onPostExecute(SoapObject resultSoapObject) {
			String strError = resultSoapObject.getPropertyAsString(ConnectServiceUtil.ErrorMsg);

			if(StringUtil.isEmpty(strError)) {
				//请求成功
				if(connectServerStateListener != null){
					String returnString = resultSoapObject.getPropertyAsString(methodName+RESULT);
					connectServerStateListener.connectServiceSuccessful(returnString, methodName, status, whetherRefresh);
				}
			}else{
				//请求失败
				if (connectServerStateListener != null){
					connectServerStateListener.connectServiceFailed(strError, methodName, status, whetherRefresh);
				}
			}

	}
}
