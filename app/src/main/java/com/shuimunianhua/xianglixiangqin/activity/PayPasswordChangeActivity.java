package com.shuimunianhua.xianglixiangqin.activity;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnTextChangeListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.StringUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 修改支付密码界面
 * @author SHI
 * @time 2016/9/18 14:55
 */
public class PayPasswordChangeActivity extends MyBaseActivity implements OnConnectServerStateListener {


    @Bind(R.id.iv_titleLeft)
    ImageView iv_titleLeft;

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.et_newPayPassword)
    EditText et_newPayPassword;

    @Bind(R.id.tv_currentAccountBindMobileDesc)
    TextView tv_currentAccountBindMobileDesc;

    @Bind(R.id.et_securityCode)
    EditText et_securityCode;

    @Bind(R.id.tv_getSecurityCode)
    TextView tv_getSecurityCode;

    @Bind(R.id.btn_payPasswordChangeQuery)
    Button btn_payPasswordChangeQuery;

    private OnTextChangeListener textChangeListener = new OnTextChangeListener() {
        @Override
        public void afterTextChanged(Editable s) {
            if(!StringUtil.isEmpty(et_newPayPassword.getText().toString().trim())
                    && !StringUtil.isEmpty(et_securityCode.getText().toString().trim()))
            {
                btn_payPasswordChangeQuery.setEnabled(true);
            }else{
                btn_payPasswordChangeQuery.setEnabled(false);
            }
        }
    };

    private int timeRemain;
    private int MESSAGE_01 = 1;
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            if (timeRemain > 0) {
                timeRemain--;
                tv_getSecurityCode.setText(timeRemain + " 秒后重新获取");
                handler.sendEmptyMessageDelayed(MESSAGE_01, 1000);
            }else{
                tv_getSecurityCode.setEnabled(true);
                tv_getSecurityCode.setText(R.string.getSecurityCode);
            }
            return false;
        }
    });

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    /**服务器返回的待核实验证码**/
    String strCheckSecurityCode;

    @Override
    public void initView() {
        setContentView(R.layout.activity_pay_password_change);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.title_payPasswordChange);
        et_newPayPassword.addTextChangedListener(textChangeListener);
        et_securityCode.addTextChangedListener(textChangeListener);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_getSecurityCode, R.id.btn_payPasswordChangeQuery})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_getSecurityCode:
                getData(InformationCodeUtil.methodNameGetSMSCode);
                break;
            case R.id.btn_payPasswordChangeQuery:

                if(!StringUtil.isEmpty(strCheckSecurityCode)
                   && strCheckSecurityCode.equals(et_securityCode.getText().toString().trim())){
                    getData(InformationCodeUtil.methodNameSetEPayPinCode);
                }else{
                    ToastUtils.show(mContext,"验证码输入有误");
                }

                break;
        }
    }

    public void getData(String methodName){

        if(InformationCodeUtil.methodNameGetSMSCode.equals(methodName)){
            SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
            requestSoapObject.addProperty("phoneNum", MyApplication.getmCustomModel(mContext).getPhoneNum());
            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
                    (mContext, this, requestSoapObject , methodName);
            connectCustomServiceAsyncTask.execute();
            return;
        }
        if(InformationCodeUtil.methodNameSetEPayPinCode.equals(methodName)){
            SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
            requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
            requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
            requestSoapObject.addProperty("ePayPinCode", et_newPayPassword.getText().toString().trim());
            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
                    (mContext, this, requestSoapObject, methodName);
            connectCustomServiceAsyncTask.execute();
        }

        }

    @Override
    public void connectServiceSuccessful(String returnString, String methodName, Object state, boolean whetherRefresh) {
        if(InformationCodeUtil.methodNameGetSMSCode.equals(methodName)){
            Gson gson = new Gson();
            try {
                JSONResultMsgModel mJSONBackResultModel = gson.fromJson(returnString, JSONResultMsgModel.class);
//		        GetSMSCodeResponse{GetSMSCodeResult={"DjLsh":-1,"Msg":"1820","Sign":1}; }
//	            GetSMSCodeResponse{GetSMSCodeResult={"DjLsh":-1,"Msg":"验证码超出同模板同号码天发送上限","Sign":0}; }
                if( mJSONBackResultModel.getSign() == 1){
                    strCheckSecurityCode = mJSONBackResultModel.getMsg();
                    ToastUtils.show(mContext,"请输入获取到的验证码");
                    tv_currentAccountBindMobileDesc.setVisibility(View.VISIBLE);
                    timeRemain = 60;
                    tv_getSecurityCode.setEnabled(false);
                    handler.sendEmptyMessage(MESSAGE_01);
                    return;
                }else{
                    ToastUtils.show(mContext, mJSONBackResultModel.getMsg());
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        if(InformationCodeUtil.methodNameSetEPayPinCode.equals(methodName)){
            try {
                JSONObject jsonObject = new JSONObject(returnString);
                int sign = jsonObject.getInt("Sign");
                if(sign == 1){
                    ToastUtils.show(mContext,"支付密码修改成功");
                    finish();
                }else{
                    ToastUtils.show(mContext,jsonObject.getString("Msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void connectServiceFailed(String methodName, Object state, boolean whetherRefresh) {
        if(InformationCodeUtil.methodNameGetSMSCode.equals(methodName)){
            ToastUtils.show(mContext, "网络异常,获取验证码失败");
        }

        if(InformationCodeUtil.methodNameSetEPayPinCode.equals(methodName)){
            ToastUtils.show(mContext, "网络异常,支付密码修改失败");
        }
    }

    @Override
    public void connectServiceCancelled(String returnString, String methodName, Object state, boolean whetherRefresh) {

    }
}
