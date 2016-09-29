package com.shuimunianhua.xianglixiangqin.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.CreateQRImage;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayMoneyByQDCodeActivity extends MyBaseActivity implements OnConnectServerStateListener {



    @Bind(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    /**
     * 总金额
     **/
    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.tv_totalPrices)
    TextView tv_totalPrices;
    /**
     * 二维码
     **/
    @Bind(R.id.iv_QDCode)
    ImageView iv_QDCode;
    /**
     * 获取付款结果
     **/
    @Bind(R.id.btn_payByQDCodeFinish)
    Button btn_payByQDCodeFinish;

    /**
     * 当前订单信息
     **/
    String currentOrderIds;
    /**
     * 付款方式,0是支付宝，1是微信
     **/
    int payMethodFlag;

    /**
     * 二维码生成对象
     **/
    private CreateQRImage createQRImage = new CreateQRImage();
    /**是否付款成功**/
    boolean IfPayMoneySuccess = false;
    private View rootView;
    @Override
    public void initView() {
        rootView = View.inflate(mContext,R.layout.activity_pay_qdcode,null);
        setContentView(rootView);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.title_payMoneyByQDCodeActivity);
    }

    @OnClick({R.id.iv_titleLeft, R.id.btn_payByQDCodeFinish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                previewToDestroy();
                break;
//            case R.id.tv_titleRight:
//                toAliPayScan();
//                break;
            case R.id.btn_payByQDCodeFinish:
                getPayResult();
                break;
//            case R.id.iv_QDCode:
//                toWeChatScan();
//                break;
        }
    }

//    private void toWeChatScan() {
//        try {
//            //利用Intent打开微信
//            Uri uri = Uri.parse("weixin://");
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
//        } catch (Exception e) {
//            //若无法正常跳转，在此进行错误处理
//            Toast.makeText(mContext, "无法跳转到微信，请检查您是否安装了微信！", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void toAliPayScan() {
//        try {
//            //利用Intent打开支付宝
//            //支付宝跳过开启动画打开扫码和付款码的url scheme分别是alipayqr://platformapi/startapp?saId=10000007和
//            //alipayqr://platformapi/startapp?saId=20000056
//            Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007");
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
//        } catch (Exception e) {
//            //若无法正常跳转，在此进行错误处理
//            Toast.makeText(mContext, "无法跳转到支付宝，请检查您是否安装了支付宝！", Toast.LENGTH_SHORT).show();
//        }
//    }


    @Override
    public void initData() {
        Intent intent = getIntent();
        //获取当前需要付款的金额
        tv_totalPrices.setText("支付金额：￥ 0.00 (含手续费)");
        //获取当前要支付掉的订单号,使用 "," 分开
        currentOrderIds = intent.getStringExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds);
        payMethodFlag = intent.getIntExtra(InformationCodeUtil.IntentPayMoneyByQDCodeActivityFlag, 0);
        getQDCodeData();
    }



    /**
     * 获取支付二维码
     **/
    private void getQDCodeData() {

        String methodName = InformationCodeUtil.methodNameSaoMaHandler;
        SoapObject requestSoapObject = new SoapObject(
                ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("customID", MyApplication
                .getmCustomModel(mContext).getDjLsh());
        requestSoapObject.addProperty("openKey", MyApplication
                .getmCustomModel(mContext).getOpenKey());
        requestSoapObject.addProperty("orderTag", currentOrderIds);
        requestSoapObject.addProperty("payMethod", payMethodFlag);
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, requestSoapObject, methodName);
        connectCustomServiceAsyncTask.initProgressDialog(true,"生成二维码...");
        connectCustomServiceAsyncTask.execute();
    }

    /**
     * 获取支付结果
     **/
    private void getPayResult() {

        String methodName = InformationCodeUtil.methodNameGetSaoMaPayResult;
        SoapObject requestSoapObject = new SoapObject(
                ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("customID", MyApplication
                .getmCustomModel(mContext).getDjLsh());
        requestSoapObject.addProperty("openKey", MyApplication
                .getmCustomModel(mContext).getOpenKey());
        requestSoapObject.addProperty("orderTag", currentOrderIds);
        LogUtil.LogShitou(requestSoapObject.toString());
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, requestSoapObject, methodName);
        connectCustomServiceAsyncTask.execute();
    }

    @Override
    public void connectServiceSuccessful(String returnString, String methodName, Object state, boolean whetherRefresh) {
        LogUtil.LogShitou(returnString);

        //    {"DjLsh":-1,"Msg":"weixin:\/\/wxpay\/bizpayurl?pr=TxCgM2z","Sign":1,"Tags":"","orderId":0}
        if (InformationCodeUtil.methodNameSaoMaHandler.equals(methodName)) {

            try {
                JSONObject jsonObject = new JSONObject(returnString);
                String url = jsonObject.getString("Msg");
                String totalMoney = jsonObject.getString("Tags");
                tv_totalPrices.setText("支付金额：￥" + totalMoney + " (含手续费)");
                createQRImage.createQRImage(url, iv_QDCode);
                btn_payByQDCodeFinish.setEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        //    {"DjLsh":-1,"Msg":"支付结果处理中，请稍后...","Sign":1,"Tags":"","orderId":0}
        //    {"DjLsh":-1,"Msg":"支付成功","Sign":1,"Tags":"","orderId":0}
        if (InformationCodeUtil.methodNameGetSaoMaPayResult.equals(methodName)) {
            try {
                JSONObject jsonObject = new JSONObject(returnString);
                String msg = jsonObject.getString("Msg");
                if("支付成功".equals(msg)){
                    IfPayMoneySuccess = true;
                    showPopWindowOfPayMoneyResult(true,msg);
                }else{
                    showPopWindowOfPayMoneyResult(false,msg);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
    }

    @Override
    public void connectServiceFailed(String methodName, Object state, boolean whetherRefresh) {

        if (InformationCodeUtil.methodNameSaoMaHandler.equals(methodName)) {
            ToastUtils.show(mContext, "网络异常，扫码获取失败");
            return;
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
