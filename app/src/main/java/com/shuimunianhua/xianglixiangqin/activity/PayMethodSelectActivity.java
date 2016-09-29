package com.shuimunianhua.xianglixiangqin.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.model.ParamPayMethodSelectModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.StringUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;
import com.shuimunianhua.xianglixiangqin.view.FragmentCommonDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayMethodSelectActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer> {

    @Bind(R.id.tv_title)
    TextView tv_title;
    /**关闭当前界面**/
    @Bind(R.id.iv_closeActivity)
    ImageView iv_closeActivity;
    /**总价格**/
    @Bind(R.id.tv_totalPrices)
    TextView tv_totalPrices;
    /**在线支付**/
    @Bind(R.id.cb_payMethodByOnline)
    CheckBox cb_payMethodByOnline;
    /**信用支付**/
    @Bind(R.id.cb_payMethodByCredit)
    CheckBox cb_payMethodByCredit;
    /**确认支付**/
    @Bind(R.id.btn_queryToPay)
    Button btn_queryToPay;
    /**确认支付**/
    @Bind(R.id.relativeLayout_background)
    RelativeLayout relativeLayout_background;



    /**订单总数量**/
    private int totalOrderNum;
    /**当前订单编号集合ziduan **/
    private String totalOrderIds;
    /**订单总金额**/
    private Double totalOrderPrices;
    /**当前界面根View**/
    private View rootView;
    /**请求码**/
    private final int RequestCode_PayMethodSelectActivity = 1;
    //创建订单是否成功，付款是否成功
    private boolean IfCreateOrderSuccess = false;
    private boolean IfPayMoneySuccess = false;
    /**根据这个标记来判断是从那种类型的页面进入当前页面的**/
    private int TypeOfComefromView;

    private ParamPayMethodSelectModel paramModel;

    @Override
    public void initView() {
        rootView = View.inflate(mContext, R.layout.activity_pay_method_select, null);
        setContentView(rootView);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        paramModel = (ParamPayMethodSelectModel) getIntent()
                .getSerializableExtra(InformationCodeUtil.IntentPayMethodSelectActivityParamObject);
        if(paramModel == null || paramModel.getTotalOrderNum() == 0){
            previewToDestroy();
            return;
        }
        totalOrderNum = paramModel.getTotalOrderNum();
        TypeOfComefromView = paramModel.getTypeComeFromView();
        totalOrderIds = paramModel.getTotalOrderIds();
        totalOrderPrices = paramModel.getTotalPrices();

        cb_payMethodByOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb_payMethodByCredit.setChecked(false);
                }
            }
        });
        cb_payMethodByCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    switch (TypeOfComefromView){
                        case ParamPayMethodSelectModel.TypeConfirmOrderGeneralCanPayCredit:
                        case ParamPayMethodSelectModel.TypeMyOrderCanPayCredit:
                            cb_payMethodByOnline.setChecked(false);
                            break;
                        case ParamPayMethodSelectModel.TypeConfirmOrderSport:
                            ToastUtils.show(mContext,"活动订单不能使用信用支付");
                            cb_payMethodByCredit.setChecked(false);
                            break;
                        case ParamPayMethodSelectModel.TypeMyOrderCanNotPayCredit:
                        case ParamPayMethodSelectModel.TypeConfirmOrderGeneralCanNotPayCredit:
                            ToastUtils.show(mContext,"订单列表含有非账期订单，不能使用信用支付");
                            cb_payMethodByCredit.setChecked(false);
                            break;
                    }
                }


            }
        });
        tv_totalPrices.setText("已选择"+totalOrderNum+"个订单，合计：¥ "+StringUtil.doubleToString(totalOrderPrices,"0.00"));
    }


    @OnClick({R.id.iv_closeActivity, R.id.btn_queryToPay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_closeActivity:
                previewToDestroy();
                break;
            case R.id.btn_queryToPay:
                toPayMoney();
                break;
        }
    }

    /**去支付**/
    private void toPayMoney(){

        if(!cb_payMethodByOnline.isChecked() && !cb_payMethodByCredit.isChecked()){
            ToastUtils.show(mContext,"请先选择支付方式");
            return;
        }
        if( cb_payMethodByOnline.isChecked()){
            switch (TypeOfComefromView){
                case ParamPayMethodSelectModel.TypeConfirmOrderGeneralCanPayCredit:
                case ParamPayMethodSelectModel.TypeConfirmOrderGeneralCanNotPayCredit:
                    toCreateGeneralGoodsOrder(0);
                     break;
                case ParamPayMethodSelectModel.TypeConfirmOrderSport:
                    toCreateSportGoodsOrder();
                    break;
                case ParamPayMethodSelectModel.TypeMyOrderCanPayCredit:
                case ParamPayMethodSelectModel.TypeMyOrderCanNotPayCredit:
                    Intent intent = new Intent(mContext,PayMoneyOldActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentPayMoneyByBankCardActivityCurrentTotalMoney, totalOrderPrices);
                    intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds, totalOrderIds);
                    startActivityForResult(intent, RequestCode_PayMethodSelectActivity);
                    overridePendingTransition(R.anim.in_from_bottom,R.anim.not_change);
                    break;
            }
            return;
        }
        if(cb_payMethodByCredit.isChecked()){
            switch (TypeOfComefromView){
                case ParamPayMethodSelectModel.TypeConfirmOrderGeneralCanPayCredit:
                    toCreateGeneralGoodsOrder(1);
                    break;
                case ParamPayMethodSelectModel.TypeMyOrderCanPayCredit:
                    changeOrderStateToWaitToSend(totalOrderIds);
                    break;
            }
        }
    }

    /**修改账期订单为待发货**/
    private void changeOrderStateToWaitToSend(String totalOrderId) {

        String methodName = InformationCodeUtil.methodNameChangeOrderStateExt;
        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE,
                methodName);
        requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
        requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
        requestSoapObject.addProperty("orderIds", totalOrderId);//接返回待支付订单信息
        ConnectCustomServiceAsyncTask ConnectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, requestSoapObject, methodName);
        ConnectCustomServiceAsyncTask.initProgressDialog(true,"信用支付中...");
        ConnectCustomServiceAsyncTask.execute();

    }

    /**创建普通商品订单  IfPayMethodByCredit:  0 在线支付， 1信用支付**/
    private void toCreateGeneralGoodsOrder(int IfPayMethodByCredit){
        String methodName = InformationCodeUtil.methodNameCreateOrder;
        SoapObject requestSoapObject = new SoapObject(
                ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("customID", MyApplication
                .getmCustomModel(mContext).getDjLsh());
        requestSoapObject.addProperty("openKey", MyApplication
                .getmCustomModel(mContext).getOpenKey());
        requestSoapObject.addProperty("postShoppingCarts",paramModel.getParamPostShoppingCarts());
        requestSoapObject.addProperty("address",paramModel.getParamAddress());
        requestSoapObject.addProperty("realName",paramModel.getParamRealName());
        requestSoapObject.addProperty("phoneNum",paramModel.getParamPhoneNum());
        requestSoapObject.addProperty("payType",0);

        if(IfPayMethodByCredit == 0){
            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                    mContext, this, requestSoapObject, methodName, false);
            connectCustomServiceAsyncTask.initProgressDialog(true,"正在提交订单...");
            connectCustomServiceAsyncTask.execute();
        }else{
            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                    mContext, this, requestSoapObject, methodName, true);
            connectCustomServiceAsyncTask.initProgressDialog(true,"正在提交订单...");
            connectCustomServiceAsyncTask.execute();
        }
    }

    private void toCreateSportGoodsOrder(){

            String methodName = InformationCodeUtil.methodNameAddOrderExt;
            SoapObject requestSoapObject = new SoapObject(
                    ConnectServiceUtil.NAMESPACE, methodName);
            requestSoapObject.addProperty("customID", MyApplication
                    .getmCustomModel(mContext).getDjLsh());
            requestSoapObject.addProperty("openKey", MyApplication
                    .getmCustomModel(mContext).getOpenKey());
            requestSoapObject.addProperty("orderJson", paramModel.getParamOrderJson());
            requestSoapObject.addProperty("itemsJson", paramModel.getParamItemsJson());
            if (paramModel.getParamPlatformActionType() != 0) {
                requestSoapObject.addProperty("platformActionID", paramModel.getParamPlatformActionID());
                requestSoapObject.addProperty("platformActionType", paramModel.getParamPlatformActionType());
            }
            requestSoapObject.addProperty("remark", paramModel.getParamRemark());
            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                    mContext, this, requestSoapObject, methodName);
            connectCustomServiceAsyncTask.initProgressDialog(true,"正在提交订单...");
            connectCustomServiceAsyncTask.execute();
    }


    @Override
    public void connectServiceSuccessful(String returnString, String methodName, Integer state, boolean IfPayByCredit) {

        LogUtil.LogShitou("创建订单成功",returnString);
        //创建普通商品订单成功
        if (methodName == InformationCodeUtil.methodNameCreateOrder) {
            try {
                JSONObject json = new JSONObject(returnString);
                int sign = json.getInt("Sign");
                //创建订单是否成功
                if(sign == 1){
                    IfCreateOrderSuccess = true;
                    totalOrderPrices = json.getDouble("Msg");
                    totalOrderIds = json.getString("Tags");
                    tv_totalPrices.setText("已选择"+totalOrderNum+"个订单，合计：¥ "+StringUtil.doubleToString(totalOrderPrices,"0.00"));
//                    if(state == 1){
//                        //获取订单号，使用账期支付把订单变成待发货状态
//                        relativeLayout_background.setVisibility(View.INVISIBLE);
//                        showPopuWindowOfConfirmResult(true, "信用支付成功");
//                    }else{
//                        Intent intent = new Intent(mContext,PayMoneyOldActivity.class);
//                        intent.putExtra(InformationCodeUtil.IntentPayMoneyByBankCardActivityCurrentTotalMoney, totalOrderPrices);
//                        intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds, totalOrderIds);
//                        startActivityForResult(intent, RequestCode_PayMethodSelectActivity);
//                        overridePendingTransition(R.anim.in_from_bottom,R.anim.not_change);
//                    }
                    if(IfPayByCredit){
                        //获取订单号，使用账期支付把订单变成待发货状态
                        relativeLayout_background.setVisibility(View.INVISIBLE);
                        showPayOrderByOnlineDialog(totalOrderIds);
                    }else{
                        Intent intent = new Intent(mContext,PayMoneyOldActivity.class);
                        intent.putExtra(InformationCodeUtil.IntentPayMoneyByBankCardActivityCurrentTotalMoney, totalOrderPrices);
                        intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds, totalOrderIds);
                        startActivityForResult(intent, RequestCode_PayMethodSelectActivity);
                        overridePendingTransition(R.anim.in_from_bottom,R.anim.not_change);
                    }

                }else{
                    showPopuWindowOfConfirmResult(false, "信用支付失败,"+json.getString("Msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 创建活动订单成功{"DjLsh":378,"Msg":"操作成功!","Sign":1,"Tags":""}
        if (methodName == InformationCodeUtil.methodNameAddOrderExt) {
            JSONObject json = null;
            try {
                json = new JSONObject(returnString);
                totalOrderIds = json.getString("Tags");
                int sign = json.getInt("Sign");
                if (sign == 1) {
                      IfCreateOrderSuccess = true;
                      relativeLayout_background.setVisibility(View.INVISIBLE);
                      showPopuWindowOfConfirmResult(true,"订单提交成功");
//                    totalOrderPrices = json.getDouble("Msg");
//                    tv_totalPrices.setText("已选择"+totalOrderNum+"个订单，合计：¥ "+StringUtil.doubleToString(totalOrderPrices,"0.00"));
//                    Intent intent = new Intent(mContext,PayMoneyOldActivity.class);
//                    intent.putExtra(InformationCodeUtil.IntentPayMoneyByBankCardActivityCurrentTotalMoney, totalOrderPrices);
//                    intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds, totalOrderIds);
//                    startActivityForResult(intent, RequestCode_PayMethodSelectActivity);
//                    overridePendingTransition(R.anim.in_from_bottom,R.anim.not_change);

                } else {
                    showPopuWindowOfConfirmResult(false,"订单提交失败,"+json.getString("Msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        //使用账期支付把账期订单修改为待发货状态
        if (methodName == InformationCodeUtil.methodNameChangeOrderStateExt) {
            try {
                Gson gson = new Gson();
                JSONResultMsgModel mJSONResultMsgModel = gson.fromJson(returnString, JSONResultMsgModel.class);
                switch (TypeOfComefromView){
                    case ParamPayMethodSelectModel.TypeConfirmOrderGeneralCanPayCredit:
                    case ParamPayMethodSelectModel.TypeConfirmOrderSport:
                    case ParamPayMethodSelectModel.TypeConfirmOrderGeneralCanNotPayCredit:

                        if(mJSONResultMsgModel.getSign() == 1){
                            relativeLayout_background.setVisibility(View.INVISIBLE);
                            showPopuWindowOfConfirmResult(true, mJSONResultMsgModel.getMsg());
                        }else{
                            showPopuWindowOfConfirmResult(false, mJSONResultMsgModel.getMsg());
                        }
                        break;
                    default:
                        if(mJSONResultMsgModel.getSign() == 1){
                            IfPayMoneySuccess = true;
                            previewToDestroy();
                        }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        }

    //账期支付进行信用支付
    void showPayOrderByOnlineDialog(final String orderIds) {

        final FragmentCommonDialog fragmentDailog = new FragmentCommonDialog();
        fragmentDailog.initView("系统提示", "成功生成订单，确定进行信用支付吗？", "取消", "信用支付",
                new FragmentCommonDialog.OnButtonClickListener() {

                    @Override
                    public void OnOkClick() {
                        changeOrderStateToWaitToSend(orderIds);
                    }

                    @Override
                    public void OnCancelClick() {
                        Intent intent = new Intent(mContext, MyOrderActivity.class);
                        mContext.startActivity(intent);
                        previewToDestroy();
                    }
                });
        fragmentDailog.show(getSupportFragmentManager(), "fragmentDailog");
    }

    @Override
    public void connectServiceFailed(String methodName, Integer state, boolean whetherRefresh) {
        if (methodName == InformationCodeUtil.methodNameCreateOrder
                || methodName == InformationCodeUtil.methodNameAddOrderExt) {
            showPopuWindowOfConfirmResult(false, "网络异常，订单提交失败");
            return;
        }
        if (methodName == InformationCodeUtil.methodNameChangeOrderStateExt) {
            showPopuWindowOfConfirmResult(false, "网络异常，账期支付失败");
        }
    }



    @Override
    public void connectServiceCancelled(String returnString, String methodName, Integer state, boolean whetherRefresh) {

    }



    //提交订单返回结果之后提醒
    void showPopuWindowOfConfirmResult(final boolean flagIfSuccess,String msg){
        View view = View.inflate(mContext, R.layout.item_pop_result_of_confirm_order, null);
        final PopupWindow pop = new PopupWindow(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, true);
        //订单是否提交成功
        ImageView iv_confirmOrderResultDesc = (ImageView) view.findViewById(R.id.iv_confirmOrderResultDesc);
        TextView tv_confirmOrderResultDesc = (TextView) view.findViewById(R.id.tv_confirmOrderResultDesc);
        TextView tv_orderWillDelete = (TextView) view.findViewById(R.id.tv_orderWillDelete);

        //收货人信息，继续购物，去我的订单，重复提交控件
        LinearLayout linearLayout_continueToShopping = (LinearLayout) view.findViewById(R.id.linearLayout_continueToShopping);
        LinearLayout linearLayout_continueToMyOrder = (LinearLayout) view.findViewById(R.id.linearLayout_continueToMyOrder);
        LinearLayout linearLayout_continueToConfirmOrder = (LinearLayout) view.findViewById(R.id.linearLayout_continueToConfirmOrder);

        if(flagIfSuccess){

            iv_confirmOrderResultDesc.setImageResource(R.drawable.iv_confirm_order_success);
            tv_confirmOrderResultDesc.setText(msg);

            linearLayout_continueToShopping.setVisibility(View.VISIBLE);
            linearLayout_continueToMyOrder.setVisibility(View.VISIBLE);
            linearLayout_continueToConfirmOrder.setVisibility(View.GONE);
            linearLayout_continueToShopping.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                    pop.dismiss();

                }
            });
            linearLayout_continueToMyOrder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MyOrderActivity.class);
                    mContext.startActivity(intent);
                    pop.dismiss();
                }
            });


        }else{
            iv_confirmOrderResultDesc.setImageResource(R.drawable.iv_confirm_order_false);
            tv_confirmOrderResultDesc.setText(msg);
            tv_orderWillDelete.setVisibility(View.GONE);
            linearLayout_continueToShopping.setVisibility(View.GONE);
            linearLayout_continueToMyOrder.setVisibility(View.GONE);
            linearLayout_continueToConfirmOrder.setVisibility(View.VISIBLE);
            linearLayout_continueToConfirmOrder.setOnClickListener(new View.OnClickListener() {

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


    /**
     * 即将关闭当前页面时启动关闭动画
     **/
    void previewToDestroy() {
        if(IfCreateOrderSuccess){
            setResult(RESULT_CANCELED);
        }
        if(IfPayMoneySuccess){
            setResult(RESULT_OK);
        }
        finish();
        overridePendingTransition(R.anim.not_change,R.anim.out_to_bottom);
    }

    /**
     * 如果是返回键，则关闭当前页面
     **/
    @Override
    public void onBackPressed() {
        previewToDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                //用户付款成功
                if (requestCode == RequestCode_PayMethodSelectActivity) {
                    IfPayMoneySuccess = true;
                    previewToDestroy();
                }
                break;
            case RESULT_CANCELED:
                //用户已经生成了订单
                if (requestCode == RequestCode_PayMethodSelectActivity) {
                    if(IfCreateOrderSuccess){
                        previewToDestroy();
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
