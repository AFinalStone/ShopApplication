package com.shi.xianglixiangqin.frament;


import android.content.Intent;

import com.google.gson.Gson;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.activity.UserGetSecurityCodeActivity;
import com.shi.xianglixiangqin.activity.LoginActivity;
import com.shi.xianglixiangqin.activity.MainActivity;
import com.shi.xianglixiangqin.activity.UserLoginPasswordChangeActivity;
import com.shi.xianglixiangqin.activity.UserRegisterActivity;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultMsgModel;
import com.shi.xianglixiangqin.model.CustomModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.PreferencesUtilMy;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.ksoap2.serialization.SoapObject;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by SHI on 2016/7/27 10:37
 */
public abstract  class LoginBaseFragment extends MyBaseFragment<LoginActivity>  implements OnConnectServerStateListener<String> {


    protected int RequestCode_RegisterView = 1;
    protected int RequestCode_PasswordRetrieveView = 2;

    public void toMainView() {
        Intent intent = new Intent(mActivity,MainActivity.class);
        startActivity(intent);
        mActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        mActivity.finish();
    }

    public void toRegisterView() {
        Intent intent = new Intent(mActivity,UserGetSecurityCodeActivity.class);
        intent.putExtra(InformationCodeUtil.IntentGetSecurityCodeIntentObject, UserRegisterActivity.class);
        startActivityForResult(intent,RequestCode_RegisterView);
        mActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void toPasswordRetrieveView() {
        Intent intent = new Intent(mActivity,UserGetSecurityCodeActivity.class);
        intent.putExtra(InformationCodeUtil.IntentGetSecurityCodeIntentObject, UserLoginPasswordChangeActivity.class);
        startActivityForResult(intent,RequestCode_PasswordRetrieveView);
        mActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    /**
     * 登录聚合
     **/
    public void login_JvHe(String strUserNumber, String strUserPassword, String strUserCodeOfEnterShop) {
//        String strUserNumber = et_userNumber.getText().toString();
//        String strUserPassword = et_userPassword.getText().toString();
//        String strUserCodeOfEnterShop = et_userLoginShopID.getText().toString();

        String methodName = InformationCodeUtil.methodNameDealerLoginExt;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("phoneNum", strUserNumber);
        soapObject.addProperty("passWord", strUserPassword);
        soapObject.addProperty("code", strUserCodeOfEnterShop);
        ConnectCustomServiceAsyncTask ConnectCustomServiceAsyncTask =
                new ConnectCustomServiceAsyncTask(
                        mActivity, this, soapObject, methodName, strUserCodeOfEnterShop);
        ConnectCustomServiceAsyncTask.execute();
    }

    /**登录带你飞**/
    public void login_DaiNiFei(String strUserNumber, String strUserPassword) {
//        String strUserNumber = et_userNumber.getText().toString();
//        String strUserPassword = et_userPassword.getText().toString();

        String methodName = InformationCodeUtil.methodNameLogin;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("phoneNum", strUserNumber);
        soapObject.addProperty("passWord", strUserPassword);
        ConnectCustomServiceAsyncTask ConnectCustomServiceAsyncTask =
                new ConnectCustomServiceAsyncTask(
                        mActivity, this, soapObject, methodName);
        ConnectCustomServiceAsyncTask.execute();
    }

    public void SetJPushIMCode(){

        String registrationID =  JPushInterface.getRegistrationID(mActivity);
        Set<String> tagSet = new LinkedHashSet<>();
        tagSet.add(registrationID);
        JPushInterface.setTags(mActivity,tagSet,null);
        String methodName = InformationCodeUtil.methodNameUpdateLoginSB;
        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
            requestSoapObject.addProperty("from", 0);
            requestSoapObject.addProperty("token", registrationID);
            requestSoapObject.addProperty("djlsh", MyApplication.getmCustomModel(mActivity).getDjLsh());
            requestSoapObject.addProperty("JGkey", getResources().getString(R.string.jpush_appkey_value));
            requestSoapObject.addProperty("JGSecret", getResources().getString(R.string.jpush_appsecret_value));
            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
                    (mActivity, this, requestSoapObject, methodName);
            connectCustomServiceAsyncTask.initProgressDialog(false);
            connectCustomServiceAsyncTask.execute();

    }

    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, String state, boolean whetherRefresh) {
        if(InformationCodeUtil.methodNameLogin.equals(methodName)){
            Gson gson = new Gson();
            try {
                CustomModel mCustomModel= gson.fromJson(returnString,CustomModel.class);
                mCustomModel.setLoginShopID(-1);
//                PreferencesUtilMy.saveCustomModel(mActivity, mCustomModel);
                MyApplication.setmCustomModel(mActivity, mCustomModel);
                SetJPushIMCode();
                ToastUtil.show(mActivity,"登录成功");
                toMainView();
            } catch (Exception e) {
                ToastUtil.show(mActivity,"密码错误或账号冻结，请重新尝试");
                e.printStackTrace();
            }
            return;
        }

        if(InformationCodeUtil.methodNameDealerLoginExt.equals(methodName)){
            LogUtil.LogShitou(returnString);
            Gson gson = new Gson();
            try {
                JSONResultMsgModel jSONResult = gson.fromJson(returnString, JSONResultMsgModel.class);
                if (jSONResult.getSign() == 1) {
                    CustomModel mCustomModel = gson.fromJson(jSONResult.getMsg(), CustomModel.class);
                    mCustomModel.setLoginShopID(Integer.parseInt(state));
                    mCustomModel.setCurrentBrowsingShopID(Integer.parseInt(state));
//                    PreferencesUtilMy.saveCustomModel(mActivity, mCustomModel);
                    MyApplication.setmCustomModel( mActivity, mCustomModel);
                    SetJPushIMCode();
                    ToastUtil.show(mActivity, "登录成功");
                    MobclickAgent.onProfileSignIn(mCustomModel.getPhoneNum());
                    toMainView();
                } else {
                    ToastUtil.show(mActivity, jSONResult.getMsg());
                }
            } catch (Exception e) {
                ToastUtil.show(mActivity, "数据异常，请稍后再试");
                e.printStackTrace();
            }
        }
        if(InformationCodeUtil.methodNameUpdateLoginSB.equals(methodName)){
            LogUtil.LogShitou("推送服务", returnString);
        }

    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, String state, boolean whetherRefresh) {
        ToastUtil.show(mActivity, returnStrError);
    }


    @Override
    public void connectServiceCancelled(String returnString,
                                        String methodName, String state, boolean whetherRefresh) {

    }
}
