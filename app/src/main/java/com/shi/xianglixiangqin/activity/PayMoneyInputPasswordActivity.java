package com.shi.xianglixiangqin.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.interfaceImpl.OnTextChangeListener;
import com.shi.xianglixiangqin.model.ParamPayMoneyModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayMoneyInputPasswordActivity extends MyBaseActivity implements OnConnectServerStateListener {


    @BindView(R.id.tv_bankCardBindPhoneDesc)
    TextView tv_bankCardBindPhoneDesc;

    @BindView(R.id.et_securityCode)
    EditText et_securityCode;

    @BindView(R.id.tv_getSecurityCode)
    TextView tv_getSecurityCode;

    @BindView(R.id.et_payPassword)
    EditText et_payPassword;

    @BindView(R.id.btn_payMoney)
    Button btn_payMoney;

    /**是否付款成功**/
    boolean IfPayMoneySuccess = false;
    private View rootView;

    private OnTextChangeListener textChangeListener = new OnTextChangeListener() {
        @Override
        public void afterTextChanged(Editable s) {
            if(!StringUtil.isEmpty(et_securityCode.getText().toString().trim())
                    && !StringUtil.isEmpty(et_payPassword.getText().toString().trim()))
            {
                btn_payMoney.setEnabled(true);
            }else{
                btn_payMoney.setEnabled(false);
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

    private ParamPayMoneyModel parModel;

    @Override
    public void initView() {
        rootView = View.inflate(mContext,R.layout.activity_pay_input_password,null);
        setContentView(rootView);
        ButterKnife.bind(this);
        et_securityCode.addTextChangedListener(textChangeListener);
        et_payPassword.addTextChangedListener(textChangeListener);
    }

    @Override
    public void initData() {
        parModel = (ParamPayMoneyModel) getIntent().getSerializableExtra(InformationCodeUtil.IntentPayMoneyInputPasswordActivityParamModel);
        String passWord = parModel.getPayPassword();
        if(!StringUtil.isEmpty(passWord)){
            et_payPassword.setText(parModel.getPayPassword());
        }
    }


    @OnClick({R.id.iv_closeActivity, R.id.tv_getSecurityCode, R.id.btn_payMoney, R.id.tv_toChangePayPassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_closeActivity:
                finish();
                break;
            case R.id.tv_getSecurityCode:
                getData(InformationCodeUtil.methodNameSmsPayApply);
                break;
            case R.id.tv_toChangePayPassword:
                toChangePayPassword();
                break;
            case R.id.btn_payMoney:
                getData(InformationCodeUtil.methodNameSmsPay);
                break;
        }
    }

    private void toChangePayPassword(){
        Intent intent = new Intent(mContext,PayPasswordChangeActivity.class);
        startActivity(intent);
    }


    private void getData(String methodName){
        /**C
         *	短信支付验证码申请
         *	(int customID, string openKey, string bankCardCode(银行卡号)
         *	, string bankCardName(持卡人), string bankCode(银行编码)
         *	, string areaCode(银行区域编码), string mobile(预留手机号), string certNo(身份证号))
         * **/
        if(InformationCodeUtil.methodNameSmsPayApply.equals(methodName)){
                tv_bankCardBindPhoneDesc.setVisibility(View.INVISIBLE);
                SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
                requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
                requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
                requestSoapObject.addProperty("bankCardCode", parModel.getBankCardCode());
                requestSoapObject.addProperty("bankCardName", parModel.getBankCardUserName());
                requestSoapObject.addProperty("bankCode", parModel.getBankCode());
                requestSoapObject.addProperty("areaCode", parModel.getAreaCode());
                requestSoapObject.addProperty("mobile", parModel.getBankCardBindMobile());
                requestSoapObject.addProperty("certNo", parModel.getBankCardUserCardID());
				ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
						(mContext, this, requestSoapObject, methodName);
				connectCustomServiceAsyncTask.execute();
        }

        /**C
         *	翼支付进行支付
         *(int customID, string openKey
         * , string bankCardCode(银行账号), string bankCardName(银行账户名)
         * , string bankCode(银行编码), string areaCode(银行区域编码)
         * , string mobile(预留手机号), string certNo(身份证号)
         * , string orderIds(订单id字符串，多个用逗号分隔)
         * , string pinCode(支付密码), string smsCode(短信验证码))
         * **/
        if(InformationCodeUtil.methodNameSmsPay.equals(methodName)){

                SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
                requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
                requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
                //银行卡号和持卡人姓名
                requestSoapObject.addProperty("bankCardCode", parModel.getBankCardCode());
                requestSoapObject.addProperty("bankCardName",  parModel.getBankCardUserName());
                //银行编号
                requestSoapObject.addProperty("bankCode", parModel.getBankCode());
                //银行区域号
                requestSoapObject.addProperty("areaCode", parModel.getAreaCode());
                //预留手机和身份证号
                requestSoapObject.addProperty("mobile", parModel.getBankCardBindMobile());
                requestSoapObject.addProperty("certNo", parModel.getBankCardUserCardID());
                //订单编号
                requestSoapObject.addProperty("orderIds", parModel.getOrderIds());
                //交易密码
				requestSoapObject.addProperty("pinCode", et_payPassword.getText().toString().trim());
				requestSoapObject.addProperty("smsCode", et_securityCode.getText().toString().trim());
                LogUtil.LogShitou(requestSoapObject.toString());
				ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
						mContext, this, requestSoapObject, methodName);
                connectCustomServiceAsyncTask.initProgressDialog(true,"正在付款...");
				connectCustomServiceAsyncTask.execute();
        }
    }



    @Override
    public void connectServiceSuccessful(String returnString, String methodName, Object state, boolean whetherRefresh) {
        LogUtil.LogShitou(returnString);
        //    {"DjLsh":-1,"Msg":"成功","Sign":1,"Tags":""}
        if (InformationCodeUtil.methodNameSmsPayApply.equals(methodName)) {
            try {
                JSONObject jsonObject = new JSONObject(returnString);
                int sign = jsonObject.getInt("Sign");
                if (sign == 1) {
                    tv_bankCardBindPhoneDesc.setVisibility(View.VISIBLE);
                    timeRemain = 60;
                    tv_getSecurityCode.setEnabled(false);
                    handler.sendEmptyMessage(MESSAGE_01);
                } else {
                    ToastUtil.show(mContext, jsonObject.getString("Msg"));
                    tv_bankCardBindPhoneDesc.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        {"DjLsh":-1,"Msg":"支付成功","Sign":1,"Tags":"","orderId":0}
        if (InformationCodeUtil.methodNameSmsPay.equals(methodName)) {
            try {
                JSONObject jsonObject = new JSONObject(returnString);
                int sign = jsonObject.getInt("Sign");
                if (sign == 1) {
                    IfPayMoneySuccess = true;
                    showPopWindowOfPayMoneyResult(true,"付款成功");
                }else{
                    showPopWindowOfPayMoneyResult(false,jsonObject.getString("Msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Object state, boolean whetherRefresh) {
        if (InformationCodeUtil.methodNameSmsPayApply.equals(methodName)) {
            ToastUtil.show(mContext,"网络异常，验证码获取失败");
        }
        if (InformationCodeUtil.methodNameSmsPay.equals(methodName)) {
            showPopWindowOfPayMoneyResult(false,"网络异常，支付失败");
        }
    }

    @Override
    public void connectServiceCancelled(String returnString, String methodName, Object state, boolean whetherRefresh) {

    }

    @Override
    public void onBackPressed() {
        previewToDestroy();
    }

    void previewToDestroy(){
        if(IfPayMoneySuccess){
            setResult(RESULT_OK);
        }
        finish();
    }

    //提交订单返回结果之后提醒
    void showPopWindowOfPayMoneyResult(final boolean flagIfSuccess,String msg){
        View view = View.inflate(mContext, R.layout.item_pop_result_of_pay_money, null);
        final PopupWindow pop = new PopupWindow(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, true);
        //订单是否提交成功
        ImageView iv_payMoneyMsg = (ImageView) view.findViewById(R.id.iv_payMoneyMsg);
        TextView tv_payMoneyMsg = (TextView) view.findViewById(R.id.tv_payMoneyMsg);

        //收货人信息，继续购物，去我的订单，重复提交控件
        LinearLayout linearLayout_toQuery = (LinearLayout) view.findViewById(R.id.linearLayout_toQuery);
        LinearLayout linearLayout_toBack = (LinearLayout) view.findViewById(R.id.linearLayout_toBack);

        if(flagIfSuccess){
            iv_payMoneyMsg.setImageResource(R.drawable.iv_confirm_order_success);
            tv_payMoneyMsg.setText(msg);

            linearLayout_toQuery.setVisibility(View.VISIBLE);
            linearLayout_toBack.setVisibility(View.GONE);
            linearLayout_toQuery.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    pop.dismiss();
                }
            });

        }else{
            iv_payMoneyMsg.setImageResource(R.drawable.iv_confirm_order_false);
            tv_payMoneyMsg.setText(msg);
            linearLayout_toQuery.setVisibility(View.GONE);
            linearLayout_toBack.setVisibility(View.VISIBLE);
            linearLayout_toBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    pop.dismiss();
                }
            });
        }


        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                if(flagIfSuccess){
                    previewToDestroy();
                }
            }
        });
        pop.showAtLocation(rootView, Gravity.TOP, 0, 0);
    }
}
