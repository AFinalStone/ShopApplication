package com.shuimunianhua.xianglixiangqin.task;

import android.app.Activity;

import org.ksoap2.serialization.SoapObject;

/**
 * 连接网络,同步方式连接
 * @author SHI
 * 2016年6月28日 13:46:01
 */
public class ConnectServiceSyncTask{

	/**上下文对象**/
	protected Activity mContext;
	/**函数名称**/
	protected String methodName;

	protected final String RESULT = "Result";
	/**请求的参数对象**/
	protected SoapObject requestSoapObject;
	/**返回的数据对象**/
	protected String returnString;
	/**连接超时时间**/
	protected int connectOutTime = 10000;

	/***
	 * @param mContext 设备上下文
	 * @param requestSoapObject 请求参数对象
	 * @param methodName 请求函数名称
	 */
	public ConnectServiceSyncTask(Activity mContext,
								  SoapObject requestSoapObject, String methodName) {
		super();
		this.mContext = mContext;
		this.requestSoapObject = requestSoapObject;
		this.methodName = methodName;
	}

	/**
	 * 设置连接超时时间
	 * @param connectOutTime 连接超时时间
	 */
	public void setConnectOutTime(int connectOutTime){
		this.connectOutTime = connectOutTime;
	}

	/**
	 * 连接用户数据服务器
	 * @return 请求成功，返回请求到的数据；请求失败，返回null
     */
	public  String connectCustomService(){

		ConnectServiceUtil.connectCustomService(requestSoapObject, methodName, new ConnectServiceUtil.ConnectServiceCallBack() {
			@Override
			public void callBack(SoapObject result) {
				if(result != null)
					returnString = result.getPropertyAsString(methodName+RESULT);
			}
		}, connectOutTime);
		return returnString;
	}

	/**
	 * 连接商品数据服务器
	 * @return 请求成功，返回请求到的数据；请求失败，返回null
	 */
	public String connectGoodsService(){

		ConnectServiceUtil.connectGoodsService(requestSoapObject, methodName, new ConnectServiceUtil.ConnectServiceCallBack() {
			@Override
			public void callBack(SoapObject result) {
				if(result != null)
					returnString = result.getPropertyAsString(methodName+RESULT);
			}
		}, connectOutTime);
		return returnString;
	}

}
