package com.shi.xianglixiangqin.task;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 访问WebService的工具类,
 */
public class ConnectServiceUtil {
    //真实服务器
    /**
     * 与用户有关的操作
     **/
    public static final String CUSTOM_SERVICE = "http://wcf.dainif.com/Wcf/CustomService.svc";
    /**
     * 与用户无关的操作
     **/
    public static final String GOODS_SERVICE = "http://wcf.dainif.com/Wcf/GoodsService.svc";

    //测试服务器1(内网)
//	public static final String CUSTOM_SERVICE = "http://192.168.1.199:9000/Wcf/CustomService.svc";
//	public static final String GOODS_SERVICE = "http://192.168.1.199:9000/Wcf/GoodsService.svc";

////	测试服务器2(内网)
//	public static final String CUSTOM_SERVICE = "http://192.168.1.188:9000/Wcf/CustomService.svc";
//	public static final String GOODS_SERVICE = "http://192.168.1.188:9000/Wcf/GoodsService.svc";

//	//测试服务器3
//	public static final String  CUSTOM_SERVICE = "http://192.168.1.200:9003/Wcf/CustomService.svc";
//	public static final String  GOODS_SERVICE = "http://192.168.1.200:9003/Wcf/GoodsService.svc";//

    //测试服务器3——邹旭电脑
//	public static final String CUSTOM_SERVICE = "http://192.168.1.200:8002/CustomService.svc";
//	public static final String  GOODS_SERVICE = "http://192.168.1.200:8002/GoodsService.svc";

    /**
     * 含有3个线程的线程池
     **/
    private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
    /**
     * 命名空间
     **/
    public static final String NAMESPACE = "http://tempuri.org/";
    // SOAP Action
    private static String soapAction = "http://tempuri.org/ICustomService/AddCustom";
    /**
     * 与用户有关的命名空间
     **/
    private static final String NAMESPACE_CUSTOM_SERVICE = "http://tempuri.org/ICustomService/";
    /**
     * 与用户无关的命名空间
     **/
    private static final String NAMESPACE_GOODS_SERVICE = "http://tempuri.org/IGoodsService/";
    /**
     * 是否允许调试
     **/
    private final static boolean allowDebug = false;

    public final static String ErrorMsg = "ErrorMsg";


    /**
     * 与商品有关的操作
     *
     * @param soapObject     参数对象
     * @param methodName
     * @param connectOutTime 连接超时时间
     */
    public static SoapObject connectGoodsService(SoapObject soapObject,
                                                 final String methodName, int connectOutTime) {
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
        SoapObject resultSoapObject = new SoapObject();
        try {
            httpTransportSE_connectGoodsService.call(NAMESPACE_GOODS_SERVICE + methodName, soapEnvelope);
            if (soapEnvelope.getResponse() != null) {
                // 获取服务器响应返回的SoapObject
                resultSoapObject = (SoapObject) soapEnvelope.bodyIn;
                resultSoapObject.addProperty(ErrorMsg, "");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            resultSoapObject.addProperty(ErrorMsg, "网络异常,请检查设备网络");
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            resultSoapObject.addProperty(ErrorMsg, "请求超时");
        } catch (Exception e) {
            e.printStackTrace();
            resultSoapObject.addProperty(ErrorMsg, "服务器异常，请稍候重新尝试");
        } finally {
            return resultSoapObject;
        }
    }


    /**
     * 与用户有关操作
     *
     * @param soapObject     参数对象
     * @param methodName     方法
     * @param connectOutTime 连接超时时间
     */
    public static SoapObject connectCustomService(SoapObject soapObject,
                                                  final String methodName, int connectOutTime) {

        HttpTransportSE httpTransportSE_connectCustomService = new HttpTransportSE(CUSTOM_SERVICE, connectOutTime);
        // 实例化SoapSerializationEnvelope，传入WebService的SOAP协议的版本号
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER10);
        // 设置是否调用的是.Net开发的WebService
        // soapEnvelope.bodyOut =soapObject;
        soapEnvelope.dotNet = true;
        httpTransportSE_connectCustomService.debug = allowDebug;
        soapEnvelope.setOutputSoapObject(soapObject);
        SoapObject resultSoapObject = new SoapObject();
        try {
            httpTransportSE_connectCustomService.call(NAMESPACE_CUSTOM_SERVICE + methodName,
                    soapEnvelope);
            if (soapEnvelope.getResponse() != null) {
                // 获取服务器响应返回的SoapObject
                resultSoapObject = (SoapObject) soapEnvelope.bodyIn;
                resultSoapObject.addProperty(ErrorMsg, "");
            }
		} catch (UnknownHostException e){
            resultSoapObject.addProperty(ErrorMsg,"网络异常,请检查设备网络");
        } catch (SocketTimeoutException e){
			resultSoapObject.addProperty(ErrorMsg,"请求超时");
		} catch (Exception e) {
            resultSoapObject.addProperty(ErrorMsg, "服务器异常，请稍候重新尝试");
        } finally {
            return resultSoapObject;
        }

    }

}