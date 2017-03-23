package com.shi.xianglixiangqin.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.CreateQRImageUtil;
import com.shi.xianglixiangqin.util.DensityUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.ProgressDialogUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayMoneyByQRCodeActivity extends MyBaseActivity implements OnConnectServerStateListener {



    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    /**标题**/
    @BindView(R.id.tv_title)
    TextView tv_title;
    /**
     * 总金额
     **/
    @BindView(R.id.tv_totalPrices)
    TextView tv_totalPrices;
    /**
     * 二维码
     **/
    @BindView(R.id.iv_QDCode)
    ImageView iv_QDCode;
    /**
     * 获取付款结果
     **/
    @BindView(R.id.btn_payByQDCode)
    Button btn_payByQDCode;
    /**
     * 网页内容
     **/
    @BindView(R.id.webView)
    WebView webView;
    /**关闭当前网页**/
    @BindView(R.id.iv_closeActivity)
    ImageView iv_closeActivity;
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
    private CreateQRImageUtil createQRImageUtil = new CreateQRImageUtil();
    /**是否付款成功**/
    private String payInfUrl;
    private View rootView;
    private WebSettings webSetting;
    @Override
    public void initView() {
        rootView = View.inflate(mContext,R.layout.activity_pay_zfbao,null);
        setContentView(rootView);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.title_payMoneyByQDCodeActivity);
        initWebView();
    }

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

    private void initWebView() {
        // 设置webView不自动加载图片
        webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        // 设置可以访问文件
        webSetting.setAllowFileAccess(true);
        // 设置支持缩放
        webSetting.setBuiltInZoomControls(true);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // webSettings.setDatabaseEnabled(true);

        // 使用localStorage则必须打开
        webSetting.setDomStorageEnabled(true);

        webSetting.setGeolocationEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
    }

    class MyWebViewClient extends WebViewClient {

        // 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("platformapi/startapp")) {
                try {
//                  Uri intentUrl = Uri.parse(url);
                    Intent intent;
                    intent = Intent.parseUri(url,
                            Intent.URI_INTENT_SCHEME);
                    intent.addCategory("android.intent.category.BROWSABLE");
                    intent.setComponent(null);
                    // intent.setSelector(null);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                view.loadUrl(url);
            }
            // 如果不需要其他对点击链接事件的处理返回true，否则返回false
            return true;
        }
        //在加载网页内容的时候显示进度条
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            ProgressDialogUtil.startProgressDialog(mContext, R.string.str_loadingMsg);
        }
        //加载网页内容结束时关闭进度条
        @Override
        public void onPageFinished(WebView view, String url) {
            ProgressDialogUtil.stopProgressDialog();
            if(webView == null){
                return;
            }
            if (!webView.getSettings().getLoadsImagesAutomatically()) {
                webView.getSettings().setLoadsImagesAutomatically(true);
            }
            // Toast.makeText(ShopWebActivity.this, "无店铺信息",
            // Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            ToastUtil.show(mContext, "数据请求异常");
            finish();
        }

    }

    @OnClick({R.id.iv_titleLeft ,R.id.btn_payByQDCode, R.id.iv_closeActivity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
            case R.id.iv_closeActivity:
                previewToDestroy();
                break;
            case R.id.btn_payByQDCode:
                toPayByZFBao();
                break;
        }
    }

    private void toPayByZFBao() {
        LogUtil.LogShitou("付款链接",payInfUrl);
        iv_closeActivity.setVisibility(View.VISIBLE);
        webView.setVisibility(View.VISIBLE);
        btn_payByQDCode.setVisibility(View.INVISIBLE);
        webView.loadUrl(payInfUrl);
//        try {
//            //  {"DjLsh":-1,"Msg":"https://qr.alipay.com/bax03544xfci2aigeeu62067","Sign":1,"Tags":"0.01","orderId":0}
//
//            //利用Intent打开支付宝
//            //支付宝跳过开启动画打开扫码和付款码的url scheme分别是alipayqr://platformapi/startapp?saId=10000007和
//            //alipayqr://platformapi/startapp?saId=20000056
////            Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007");
//            Uri uri = Uri.parse(payInfUrl);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
//
//        } catch (Exception e) {
//            //若无法正常跳转，在此进行错误处理
//            Toast.makeText(mContext, "无法跳转到支付宝，请检查您是否安装了支付宝！", Toast.LENGTH_SHORT).show();
//        }
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
      //  {"DjLsh":-1,"Msg":"https://qr.alipay.com/bax03544xfci2aigeeu62067","Sign":1,"Tags":"0.01","orderId":0}
        //    {"DjLsh":-1,"Msg":"weixin:\/\/wxpay\/bizpayurl?pr=TxCgM2z","Sign":1,"Tags":"","orderId":0}
        if (InformationCodeUtil.methodNameSaoMaHandler.equals(methodName)) {

            try {
                JSONObject jsonObject = new JSONObject(returnString);
                String url = jsonObject.getString("Msg");
                String totalMoney = jsonObject.getString("Tags");
                tv_totalPrices.setText("支付金额：￥" + totalMoney + " (含手续费)");
                int length = DensityUtil.dip2px(mContext,300);
                createQRImageUtil.createQRImage(url, iv_QDCode,length,length);
                payInfUrl = url;
                btn_payByQDCode.setEnabled(true);
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
//                    IfPayMoneySuccess = true;
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
    public void connectServiceFailed(String returnStrError, String methodName, Object state, boolean whetherRefresh) {

        if (InformationCodeUtil.methodNameSaoMaHandler.equals(methodName)) {
            ToastUtil.show(mContext, "网络异常，扫码获取失败");
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
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        previewToDestroy();
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
