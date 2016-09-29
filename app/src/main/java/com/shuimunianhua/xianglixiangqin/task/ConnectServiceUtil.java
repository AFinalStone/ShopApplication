package com.shuimunianhua.xianglixiangqin.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * 访问WebService的工具类,
 */
public class ConnectServiceUtil {
	//真实服务器
//	/** 与用户有关的操作 **/
	public static final String CUSTOM_SERVICE = "http://wcf.dainif.com/Wcf/CustomService.svc";
	/** 与用户无关的操作 **/
	public static final String GOODS_SERVICE = "http://wcf.dainif.com/Wcf/GoodsService.svc";

	//测试服务器1(内网)
//	public static final String CUSTOM_SERVICE = "http://192.168.0.200:8001/Wcf/CustomService.svc";
//	public static final String GOODS_SERVICE = "http://192.168.0.200:8001/Wcf/GoodsService.svc";

	//测试服务器2(内网)
//	public static final String CUSTOM_SERVICE = "http://192.168.1.100:8001/Wcf/CustomService.svc";
//	public static final String GOODS_SERVICE = "http://192.168.1.100:8001/Wcf/GoodsService.svc";

	//测试服务器3
//	public static final String CUSTOM_SERVICE = "http://115.28.9.25:8001/Wcf/CustomService.svc";
//	public static final String GOODS_SERVICE = "http://115.28.9.25:8001/Wcf/GoodsService.svc";
	
	/**含有3个线程的线程池**/
	private static final ExecutorService executorService = Executors
			.newFixedThreadPool(3);
	// SOAP Action
	private static String soapAction = "http://tempuri.org/ICustomService/AddCustom";
	/**命名空间**/
	public static final String NAMESPACE = "http://tempuri.org/";
	/** 与用户有关的命名空间 **/
	private static final String NAMESPACE_CUSTOM_SERVICE = "http://tempuri.org/ICustomService/";
	/** 与用户无关的命名空间 **/
	private static final String NAMESPACE_GOODS_SERVICE = "http://tempuri.org/IGoodsService/";
	/**是否允许调试**/
	private final static boolean allowDebug = false;
	
	
	/**
	 * 与商品有关的操作
	 * @param soapObject 参数对象
	 * @param methodName
	 * @param connectServiceCallBack
	 * @param connectOutTime 连接超时时间
	 */
	public static void connectGoodsService(SoapObject soapObject, 
			final String methodName, final ConnectServiceCallBack connectServiceCallBack, int connectOutTime) {
		// 创建HttpTransportSE对象，传递WebService服务器地址
		
		HttpTransportSE httpTransportSE_connectGoodsService = new HttpTransportSE(GOODS_SERVICE, connectOutTime);
		// 实例化SoapSerializationEnvelope，传入WebService的SOAP协议的版本号
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		// 设置是否调用的是.Net开发的WebService
		// soapEnvelope.bodyOut =soapObject;
		soapEnvelope.dotNet = true;
		httpTransportSE_connectGoodsService.debug = allowDebug;
		soapEnvelope.setOutputSoapObject(soapObject);
		//返回结果
		SoapObject resultSoapObject = null;
		try {
			httpTransportSE_connectGoodsService.call(NAMESPACE_GOODS_SERVICE + methodName,
					soapEnvelope);
			if (soapEnvelope.getResponse() != null) {
				// 获取服务器响应返回的SoapObject
				resultSoapObject = (SoapObject) soapEnvelope.bodyIn;
				connectServiceCallBack.callBack(resultSoapObject);
			}
		} catch (Exception e) {
			connectServiceCallBack.callBack(
					resultSoapObject);				
			e.printStackTrace();
		}
	}
	

	/**
	 * 与用户有关操作
	 * @param soapObject 参数对象
	 * @param methodName 方法
	 * @param connectServiceCallBack 返回接口
	 * @param connectOutTime 连接超时时间
	 */
	public static void connectCustomService(SoapObject soapObject,
			final String methodName, final ConnectServiceCallBack connectServiceCallBack, int connectOutTime) {
		
		HttpTransportSE httpTransportSE_connectCustomService = new HttpTransportSE( CUSTOM_SERVICE, connectOutTime);
		// 实例化SoapSerializationEnvelope，传入WebService的SOAP协议的版本号
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		// 设置是否调用的是.Net开发的WebService
		// soapEnvelope.bodyOut =soapObject;
		soapEnvelope.dotNet = true;
		httpTransportSE_connectCustomService.debug = allowDebug;
		soapEnvelope.setOutputSoapObject(soapObject);
		SoapObject resultSoapObject = null;
		try {
			httpTransportSE_connectCustomService.call(NAMESPACE_CUSTOM_SERVICE + methodName,
					soapEnvelope);
			if (soapEnvelope.getResponse() != null) {
				// 获取服务器响应返回的SoapObject
				resultSoapObject = (SoapObject) soapEnvelope.bodyIn;
				connectServiceCallBack.callBack(resultSoapObject);				
			}
		} catch (Exception e) {
			connectServiceCallBack.callBack(resultSoapObject);				
			e.printStackTrace();
		}
	}

	
	/***
	 * @action 回调接口
	 * @author SHI
	 * 2016-2-3 10:48:22
	 */
	public interface ConnectServiceCallBack {
		public void callBack(SoapObject result);
	}

}