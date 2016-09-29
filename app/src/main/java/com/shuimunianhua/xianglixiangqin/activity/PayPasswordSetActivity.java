package com.shuimunianhua.xianglixiangqin.activity;

import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnTextChangeListener;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.StringUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 这个界面只会在用户第一次绑定银行卡的时候出现
 * @author SHI
 * @time 2016/9/18 19:17
 */
public class PayPasswordSetActivity extends MyBaseActivity implements OnConnectServerStateListener {

    @Bind(R.id.iv_titleLeft)
    ImageView iv_titleLeft;

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.et_newPayPassword)
    EditText et_newPayPassword;

    @Bind(R.id.et_newPayPasswordConfirm)
    EditText et_newPayPasswordConfirm;

    @Bind(R.id.btn_payPasswordSetQuery)
    Button btn_payPasswordSetQuery;

    private String  totalOrderIds;
    private String strPayPassword;

    /**是否付款成功**/
    boolean IfPayMoneySuccess = false;
    /**请求码**/
    private final int RequestCode_PayPasswordSetActivity = 1;

    private OnTextChangeListener textChangeListener = new OnTextChangeListener() {

        @Override
        public void afterTextChanged(Editable s) {
            if (!StringUtil.isEmpty(et_newPayPassword.getText().toString().trim())
                    && !StringUtil.isEmpty(et_newPayPasswordConfirm.getText().toString().trim())) {
                btn_payPasswordSetQuery.setEnabled(true);
            } else {
                btn_payPasswordSetQuery.setEnabled(false);
            }
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_pay_password_set);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.title_payPasswordSetActivity);
        et_newPayPassword.addTextChangedListener(textChangeListener);
        et_newPayPasswordConfirm.addTextChangedListener(textChangeListener);
    }

    @Override
    public void initData() {
        totalOrderIds = getIntent().getStringExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds);
    }


    @OnClick({R.id.iv_titleLeft, R.id.btn_payPasswordSetQuery})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.btn_payPasswordSetQuery:
                setPayPassWord();
                break;
        }
    }

    private boolean checkMsg(){
        strPayPassword = et_newPayPassword.getText().toString().trim();
        String payPasswordConfirm = et_newPayPasswordConfirm.getText().toString().trim();
        if(!strPayPassword.equals(payPasswordConfirm)){
            ToastUtils.show(mContext,"两次输入的密码不一致");
            return false;
        }
        return true;
    }

    /**(int customID, string openKey, string ePayPinCode)**/
    private void setPayPassWord(){
        if(checkMsg()){
            String methodName = InformationCodeUtil.methodNameSetEPayPinCode;
            SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
            requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
            requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
            requestSoapObject.addProperty("ePayPinCode", strPayPassword);
            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
                    (mContext, this, requestSoapObject, methodName);
            connectCustomServiceAsyncTask.execute();
        }
    }

    private void toPayNewBankCard(){
        Intent intent = new Intent(mContext,PayByNewBankCardActivity.class);
        intent.putExtra(InformationCodeUtil.IntentPayBankCardSelectActivityCurrentOrderIds,totalOrderIds);
        intent.putExtra(InformationCodeUtil.IntentPayPasswordSetActivityPayPassword,strPayPassword);
        startActivityForResult(intent,RequestCode_PayPasswordSetActivity);
    }

//    {"DjLsh":-1,"Msg":"支付密码设置成功","Sign":1,"Tags":""}
    @Override
    public void connectServiceSuccessful(String returnString, String methodName, Object state, boolean whetherRefresh) {
        LogUtil.LogShitou(returnString);
        try {
            JSONObject jsonObject = new JSONObject(returnString);
            int sign = jsonObject.getInt("Sign");
            if(sign == 1){
                toPayNewBankCard();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectServiceFailed(String methodName, Object state, boolean whetherRefresh) {

    }

    @Override
    public void connectServiceCancelled(String returnString, String methodName, Object state, boolean whetherRefresh) {

    }

    @Override
    public void onBackPressed() {
        finish();;
    }

    void previewToDestroy(){
        if(IfPayMoneySuccess){
            setResult(RESULT_OK);
        }
        IfOpenFinishActivityAnim(false);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                //用户付款成功
                IfPayMoneySuccess = true;
                break;
        }
        previewToDestroy();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
