package com.shi.xianglixiangqin.activity;

import java.util.Date;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shi.xianglixiangqin.model.OrderViewModel;
import com.shi.xianglixiangqin.model.OrderViewProductModel;
import com.shi.xianglixiangqin.util.TimeUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultMsgModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.view.FragmentOkAndCancelDialog;

/***
 * 我的订单詳情
 *
 * @author ZHU
 */
public class MyOrderDetailActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer> {
    /**
     * 返回图片控件
     **/
    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    /**
     * 标题文本控件
     **/
    @BindView(R.id.tv_title)
    TextView tv_title;

    /**
     * 订单编号
     **/
    @BindView(R.id.tv_orderNum)
    TextView tv_orderNum;
    /**
     * 下单时间
     **/
    @BindView(R.id.tv_orderStartTime)
    TextView tv_orderStartTime;
    /**
     * 收货人姓名
     **/
    @BindView(R.id.tv_customerName)
    TextView tv_customerName;
    /**
     * 联系方式
     **/
    @BindView(R.id.tv_customerPhone)
    TextView tv_customerPhone;
    /**
     * 收货地址
     **/
    @BindView(R.id.tv_customerReceiverAddress)
    TextView tv_customerReceiverAddress;
    /**
     * 订单备注
     **/
    @BindView(R.id.tv_orderRemark)
    TextView tv_orderRemark;
    /**
     * 订单配送方式
     **/
    @BindView(R.id.tv_deliveryMethod)
    TextView tv_deliveryMethod;
    /**
     * 是否开具发票
     **/
    @BindView(R.id.tv_IfBilling)
    TextView tv_IfBilling;
    /**
     * 发票内容
     **/
    @BindView(R.id.tv_invoiceContent)
    TextView tv_invoiceContent;
    /**
     * 积分抵扣数量
     **/
    @BindView(R.id.tv_IntegralPay)
    TextView tv_IntegralPay;

    /**
     * 飞币抵扣数量
     **/
    @BindView(R.id.tv_flyCoinPay)
    TextView tv_flyCoinPay;
    /**
     * 优惠金额
     **/
    @BindView(R.id.tv_discountPrices)
    TextView tv_discountPrices;


    /**
     * 订单配送:
     **/
    @BindView(R.id.tv_deliveryCompanyName_C)
    TextView tv_deliveryCompanyName_C;
    /**
     * 订单配送公司
     **/
    @BindView(R.id.tv_deliveryCompanyName)
    TextView tv_deliveryCompanyName;
    /**
     * 快递单号:
     **/
    @BindView(R.id.tv_orderDeliveryNum_C)
    TextView tv_orderDeliveryNum_C;
    /**
     * 快递单号
     **/
    @BindView(R.id.tv_orderDeliveryNum)
    TextView tv_orderDeliveryNum;
    /**
     * 订单商品数量
     **/
    @BindView(R.id.tv_orderGoodsTotalNumber)
    TextView tv_orderGoodsTotalNumber;
    /**
     * 订单商品总金额
     **/
    @BindView(R.id.tv_orderTotalPrices)
    TextView tv_orderTotalPrices;
    /**
     * 订单账期到期时间
     **/
    @BindView(R.id.tv_accountOrderFinishTime)
    TextView tv_accountOrderFinishTime;
    /**
     * 取消订单
     **/
    @BindView(R.id.tv_orderCancel)
    TextView tv_orderCancel;
    /**
     * 订单可以进行的操作
     **/
    @BindView(R.id.tv_orderOperationAction)
    TextView tv_orderOperationAction;

    /**
     * 商品列表
     **/
    @BindView(R.id.listView)
    ListView listView;
    /**
     * 订单详情适配器
     **/
    private OrderItemAdapter mOrderItemAdapter;
    /**
     * 当前订单信息
     **/
    private OrderViewModel mOrderViewModel;
    List<OrderViewProductModel> listData;

    /**
     * 待支付
     **/
    private final int RequestCode_MyOrderDetailActivity = 1;


    public void initView() {
        setContentView(R.layout.activity_my_order_detail);
        ButterKnife.bind(this);
        tv_title.setText(R.string.MyOrderDetail_title);
        iv_titleLeft.setVisibility(View.VISIBLE);
    }


    @OnClick({R.id.iv_titleLeft})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }

    public void initData() {
        mOrderViewModel = (OrderViewModel) getIntent().getSerializableExtra(InformationCodeUtil.IntentMyOrderDetailActivityOrderViewModel);

        if (mOrderViewModel == null) {
            finish();
            return;
        }

        //订单编号，下单时间备注
        tv_orderNum.setText("订单号：" + mOrderViewModel.getOrderNo());
        tv_orderStartTime.setText("下单时间：" + mOrderViewModel.getOrderTime());
        //用户名，手机号，收货地址
        tv_customerName.setText(mOrderViewModel.getRealName());
        tv_customerPhone.setText(mOrderViewModel.getPhoneNum());
        tv_customerReceiverAddress.setText(mOrderViewModel.getAddress());
        //配送方式,是否开具发票
        tv_deliveryMethod.setText(mOrderViewModel.getPostMethondName());
        if (mOrderViewModel.getIfBilling()) {
            tv_IfBilling.setText("是");
            if (StringUtil.isEmpty(mOrderViewModel.getBillingName())) {
                tv_invoiceContent.setText("");
            } else {
                tv_invoiceContent.setText(mOrderViewModel.getBillingName());
            }
        } else {
            tv_IfBilling.setText("否");
            tv_invoiceContent.setText("");
        }
        //具体商品列表详情
        listData = mOrderViewModel.getProducts();
        mOrderItemAdapter = new OrderItemAdapter(this, listData);
        listView.setAdapter(mOrderItemAdapter);
        //备注
        if (StringUtil.isEmpty(mOrderViewModel.getRemark()) || "null".equals(mOrderViewModel.getRemark())) {
            tv_orderRemark.setText("");
        } else {
            tv_orderRemark.setText(mOrderViewModel.getRemark());
        }
        //快递公司和快递单号
        String strDeliveryCompanyName = mOrderViewModel.getPostName();
        String strOrderDeliveryNum = mOrderViewModel.getPostNameNum();
        if (StringUtil.isEmpty(strDeliveryCompanyName)) {
            tv_deliveryCompanyName_C.setVisibility(View.INVISIBLE);
            tv_deliveryCompanyName.setVisibility(View.INVISIBLE);
        } else {
            tv_deliveryCompanyName.setText(strDeliveryCompanyName);
        }
        if (StringUtil.isEmpty(strOrderDeliveryNum)) {
            tv_orderDeliveryNum_C.setVisibility(View.INVISIBLE);
            tv_orderDeliveryNum.setVisibility(View.INVISIBLE);
        } else {
            tv_orderDeliveryNum.setText(strOrderDeliveryNum);
        }

        //飞币抵扣的数量
        tv_IntegralPay.setText(new StringBuffer().append(mOrderViewModel.getJFUseTotal()).toString());
        tv_flyCoinPay.setText("￥" + StringUtil.doubleToString((double) mOrderViewModel.getFlyCoin(), "0.00"));
        tv_discountPrices.setText("￥" + StringUtil.doubleToString((double) mOrderViewModel.getDiscountMoney(), "0.00"));
        //总的商品个数
        int orderGoodsTotalNumber = 0;
        for (int i = 0; i < listData.size(); i++) {
            orderGoodsTotalNumber += listData.get(i).getBuyCount();
        }
        tv_orderGoodsTotalNumber.setText("共" + orderGoodsTotalNumber + "件商品，合计金额：");
        tv_orderTotalPrices.setText("￥" + StringUtil.doubleToString(mOrderViewModel.getTotalMoney(), "0.00"));

        switch (mOrderViewModel.getOrderSign()) {
            case 1:
                tv_orderOperationAction.setText("  去支付  ");
                tv_orderOperationAction.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        toPayMethodSelectActivity();
                    }

                });
                if (mOrderViewModel.getDelayPayDays() == 0) {
                    tv_orderCancel.setVisibility(View.VISIBLE);
                    tv_orderCancel.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showQueryCancelOrderDialog("" + mOrderViewModel.getDjLsh());
                        }
                    });
                }
                break;
            case 3:
                tv_orderOperationAction.setText("待发货");
                tv_orderOperationAction.setEnabled(false);
                break;
            case 4:
                tv_orderOperationAction.setText("确认收货");
                tv_orderOperationAction.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        queryHaveReceiveGoods();
                    }

                });
                break;
            case -1:
                tv_orderOperationAction.setText("此订单已被取消");
                tv_orderOperationAction.setEnabled(false);
                break;
            case 9:
                tv_orderOperationAction.setText("已完成");
                tv_orderOperationAction.setEnabled(false);
                break;
            default:
                LogUtil.LogShitou("编码ID", mOrderViewModel.getOrderSign() + "");
                tv_orderOperationAction.setVisibility(View.INVISIBLE);
                break;
        }
        if (mOrderViewModel.getDelayPayDays() == 0) {
            tv_accountOrderFinishTime.setVisibility(View.INVISIBLE);
        } else {
            tv_accountOrderFinishTime.setVisibility(View.VISIBLE);
            Date date_current = TimeUtil.getTimeDate(mOrderViewModel.getOrderTime());
            Date date_finish = TimeUtil.add(date_current, mOrderViewModel.getDelayPayDays(), TimeUtil.DATE);
            String strTimeFinish = TimeUtil.getTimeString(date_finish);
            tv_accountOrderFinishTime.setText("到期时间:" + strTimeFinish);
        }
    }


    /**
     * 进入支付方式选择界面
     **/
    private void toPayMethodSelectActivity() {
        Intent intent = new Intent(mContext, PayMethodSelectActivity.class);
        intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalMoney, mOrderViewModel.getTotalMoney());
        intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds, "" + mOrderViewModel.getDjLsh());
        startActivityForResult(intent, RequestCode_MyOrderDetailActivity);
        overridePendingTransition(R.anim.in_from_bottom, R.anim.not_change);
    }

    //确定取消订单对话框
    protected void showQueryCancelOrderDialog(final String totalOrderIds) {

        final FragmentOkAndCancelDialog fragmentOkAndCancelDialog = new FragmentOkAndCancelDialog();
        fragmentOkAndCancelDialog.initView("提示", "确定取消此订单吗？", "再考虑考虑", "确定",
                new FragmentOkAndCancelDialog.OnButtonClickListener() {

                    @Override
                    public void OnOkClick() {
                        changeOrderStateToCancel(totalOrderIds);
                    }

                    @Override
                    public void OnCancelClick() {

                    }
                });
        fragmentOkAndCancelDialog.show(getSupportFragmentManager(), "fragmentOkAndCancelDialog");
    }

    /**
     * 取消订单
     **/
    private void changeOrderStateToCancel(String totalOrderIds) {

        String methodName = InformationCodeUtil.methodNameChangeOrderState;
        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
        requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
        requestSoapObject.addProperty("orderID", totalOrderIds);
        requestSoapObject.addProperty("state", -1);//-1是把订单改变成取消状态
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, requestSoapObject, methodName, true);
        connectCustomServiceAsyncTask.execute();
    }

//    private void getData(String methodName) {
//
//        if (InformationCodeUtil.methodNameChangeOrderState.equals(methodName)) {
//            SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
//            requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
//            requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
//            requestSoapObject.addProperty("orderID", mOrderViewModel.getDjLsh());
//            requestSoapObject.addProperty("state", 1);//普通订单变成已完成，账期订单变成待支付
//            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
//                    mContext, this, requestSoapObject, methodName, false);
//            connectCustomServiceAsyncTask.execute();
//            return;
//        }
//    }

    /**确认收货**/
    private void queryHaveReceiveGoods() {

        String methodName = InformationCodeUtil.methodNameChangeOrderState;
        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
        requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
        requestSoapObject.addProperty("orderID", mOrderViewModel.getDjLsh());
        if(mOrderViewModel.getDelayPayDays() == 0){
            requestSoapObject.addProperty("state", 9);//普通订单变成已完成
        }else{
            requestSoapObject.addProperty("state", 1);//账期订单变成待支付
        }
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, requestSoapObject, methodName);
        connectCustomServiceAsyncTask.execute();
    }


    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, Integer state, boolean IfCancelOrder) {
//		String  provinceSoapObject =  returnSoapObject.getPropertyAsString(methodName+"Result");
//		LogUtil.LogShitou("provinceSoapObject", provinceSoapObject.toString());
        Gson gson = new Gson();
        if (InformationCodeUtil.methodNameChangeOrderState == methodName) {
            try {
                JSONResultMsgModel mJSONResultMsgModel = gson.fromJson(returnString, JSONResultMsgModel.class);

                ToastUtil.show(mContext, mJSONResultMsgModel.getMsg());

                if (mJSONResultMsgModel.getSign() == 1) {
                    tv_orderOperationAction.setEnabled(false);
                    if (IfCancelOrder) {
                        tv_orderCancel.setEnabled(false);
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            return;
        }

    }


    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Integer state,
                                     boolean whetherRefresh) {
        if (InformationCodeUtil.methodNameChangeOrderState == methodName) {
            ToastUtil.show(mContext, returnStrError);
            return;
        }
    }


    @Override
    public void connectServiceCancelled(String returnString,
                                        String methodName, Integer state, boolean whetherRefresh) {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                tv_orderOperationAction.setEnabled(false);
                LogUtil.LogShitou("付款成功", "被执行");
                break;
            case RESULT_CANCELED:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /***
     * 订单详情适配器
     *
     * @author SHI
     *         2016-2-17 15:42:24
     */
    public class OrderItemAdapter extends MyBaseAdapter<OrderViewProductModel> {

        public OrderItemAdapter(Context mContext, List<OrderViewProductModel> listData) {
            super(mContext, listData);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_adapter_my_order_detail_listview, parent, false);
                holder = new ViewHolder();
                holder.iv_goodsImages = (ImageView) convertView.findViewById(R.id.iv_goodsImages);
                holder.tv_goodsName = (TextView) convertView.findViewById(R.id.tv_goodsName);
                holder.tv_goodsPackageAndColor = (TextView) convertView.findViewById(R.id.tv_goodsPackageAndColor);
                holder.tv_goodsQuantity = (TextView) convertView.findViewById(R.id.tv_goodsQuantity);
                holder.tv_goodsPrice = (TextView) convertView.findViewById(R.id.tv_goodsPrice);
                convertView.setTag(holder);        // 给View添加一个格外的数据
            } else {
                holder = (ViewHolder) convertView.getTag(); // 把数据取出来
            }

            OrderViewProductModel mOrderViewProductModel = listData.get(position);

            ImagerLoaderUtil.getInstance(mContext).displayMyImage(mOrderViewProductModel.getImgUrl(), holder.iv_goodsImages);
            holder.tv_goodsName.setText(mOrderViewProductModel.getProductName());
            holder.tv_goodsPackageAndColor.setText(mOrderViewProductModel.getPackageName() + "/" + mOrderViewProductModel.getColorName());
            if(mOrderViewProductModel.getJFUse() > 0){
                holder.tv_goodsPrice.setText("￥" + StringUtil.doubleToString(mOrderViewProductModel.getJEUse()));
            }else{
                holder.tv_goodsPrice.setText("￥" + StringUtil.doubleToString(mOrderViewProductModel.getUnitMoney()));
            }
            holder.tv_goodsQuantity.setText("× " + mOrderViewProductModel.getBuyCount());

            return convertView;
        }

        private class ViewHolder {
            /**
             * 商品图片
             **/
            public ImageView iv_goodsImages;
            /**
             * 商品标题
             **/
            public TextView tv_goodsName;
            /**
             * 商品套餐信息
             **/
            public TextView tv_goodsPackageAndColor;
            /**
             * 购买数量
             **/
            public TextView tv_goodsQuantity;
            /**
             * 商品价格
             **/
            public TextView tv_goodsPrice;
        }
    }
}
