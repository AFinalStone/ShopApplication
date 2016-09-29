package com.shuimunianhua.xianglixiangqin.interfaceImpl;


/****
 * 网络数据请求状态回调监听对象
 * @author SHI
 * 2016-2-18 15:26:05
 */
public interface OnConnectServerStateListener<T extends Object> {

	/**连接服务器，请求数据成功**/
	public void connectServiceSuccessful(String returnString, String methodName, T state, boolean whetherRefresh);
	
	/**连接服务器，请求数据失败**/
	public void connectServiceFailed(String methodName, T state, boolean whetherRefresh);
	
	/**用户取消网络请求**/
	public void connectServiceCancelled(String returnString, String methodName, T state, boolean whetherRefresh);
}
