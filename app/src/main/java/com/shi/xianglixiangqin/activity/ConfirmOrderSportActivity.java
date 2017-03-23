package com.shi.xianglixiangqin.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultBaseModel;
import com.shi.xianglixiangqin.json.JSONResultMsgModel;
import com.shi.xianglixiangqin.model.AddressModel;
import com.shi.xianglixiangqin.model.SelectPackageModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.shi.xianglixiangqin.view.FragmentOkAndCancelDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * @author ZHU
 * @action 确认订单界面
 * @date 2015-8-9 上午9:42:11
 */
public class ConfirmOrderSportActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer> {
    /**
     * 返回控件
     */
    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    /**
     * 页面标题
     */
    @BindView(R.id.tv_title)
    TextView tv_title;
    /**
     * 确认订单按钮
     **/
    @BindView(R.id.btn_submit)
    Button btn_submit;
    /**
     * 价格合计
     */
    @BindView(R.id.tv_totalPrices)
    TextView tv_totalPrices;

    /**
     * 收件人
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
     * 商品图片
     **/
    @BindView(R.id.iv_goodsImages)
    ImageView iv_goodsImages;
    /**
     * 商品名称
     **/
    @BindView(R.id.tv_goodsName)
    TextView tv_goodsName;
    /**
     * 商品套餐和颜色
     **/
    @BindView(R.id.tv_goodsPackageAndColor)
    TextView tv_goodsPackageAndColor;
    /**
     * 商品数量
     **/
    @BindView(R.id.tv_goodsNum)
    TextView tv_goodsNum;
    /**
     * 商品价格
     **/
    @BindView(R.id.tv_goodsPrice)
    TextView tv_goodsPrice;


    /**
     * 订单备注
     **/
    @BindView(R.id.et_orderRemark)
    EditText et_orderRemark;
    /**
     * 是否开发票
     **/
    @BindView(R.id.cb_confirmOrderIfOpenBilling)
    CheckBox cb_confirmOrderIfOpenBilling;
    /**
     * 发票号码
     **/
    @BindView(R.id.et_InvoiceNumberCode)
    EditText et_InvoiceNumberCode;
    /**
     * 飞币数量
     **/
    @BindView(R.id.tv_privilegeFlyCoin)
    TextView tv_privilegeFlyCoin;
    /**
     * 飞币备注
     **/
    @BindView(R.id.tv_privilegeFlyCoinDesc)
    TextView tv_privilegeFlyCoinDesc;

    private SelectPackageModel currentSelectPackageMode;
    /**
     * 当前商品总价格
     **/
    private Double totalOrderPrices = 0.00;
    /**
     * 当前服务器飞币数量
     **/
    private int totalFlyCoin_web = 0;
    /**
     * 付款方式选择
     **/
    private final int RequestCode_PayMethodSelectActivity = 2;
    /**
     * 收货地址选择
     **/
    private final int RequestCode_ShoppingAddressSelectActivity = 1;
    /**
     * 当前界面根View
     **/
    private View rootView;

    public void initView() {
        // 初始化主页布局
        rootView = View.inflate( mContext, R.layout.activity_confirm_order_sport, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.confirmOrderGeneralActivity_title);
        et_InvoiceNumberCode.setEnabled(false);
        cb_confirmOrderIfOpenBilling.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                et_InvoiceNumberCode.setEnabled(isChecked);
                getTotalPrice();
            }
        });
    }


    @OnClick({R.id.iv_titleLeft, R.id.btn_submit, R.id.iv_managerReceiverAddress})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.iv_managerReceiverAddress:
                Intent mIntent = new Intent(mContext,
                        ReceiveAddressSelectActivity.class);
                startActivityForResult(mIntent, RequestCode_ShoppingAddressSelectActivity);
                break;
            case R.id.btn_submit:
                toCreateOrderMethodSelectActivity();
            default:
                break;
        }
    }

    public void initData() {

        currentSelectPackageMode = (SelectPackageModel) getIntent().getSerializableExtra(InformationCodeUtil.IntentConfirmOrderSportActivityPackageModel);
        if (currentSelectPackageMode == null) {
            finish();
            return;
        }

        ImagerLoaderUtil.getInstance(mContext).displayMyImage(
                currentSelectPackageMode.getmProductModel().getImages()
                        .get(0), iv_goodsImages);
        tv_goodsName.setText(""
                + currentSelectPackageMode.getmProductModel()
                .getGoodsName());
        // 设置商品套餐颜色和价格
        if (currentSelectPackageMode.getmPackageColorModel() != null) {
            tv_goodsPackageAndColor.setText(currentSelectPackageMode
                    .getmPackages().getPackageName()
                    + "/"
                    + currentSelectPackageMode.getmPackageColorModel()
                    .getColorName());
            tv_goodsPrice.setText("￥" + StringUtil.doubleToString(currentSelectPackageMode
                    .getmPackageColorModel().getPrice(), "0.00"));
        }
        tv_goodsNum.setText("×" + currentSelectPackageMode.getPurchaseQuantity());
        tv_privilegeFlyCoin.setText("" + 0);
        tv_privilegeFlyCoinDesc.setText("(该订单最高可抵" + currentSelectPackageMode.getmPackageColorModel().getFlyCoin() + "飞币，您有" + 0 + "飞币)");
        getData(InformationCodeUtil.methodNameGetAcceptFlyCoin);
        getData(InformationCodeUtil.methodNameGetAddrList);
    }

    public void getData(String methodName) {

        // 获取 我的收货地址
        if (methodName == InformationCodeUtil.methodNameGetAddrList) {
            SoapObject requestSoapObject = new SoapObject(
                    ConnectServiceUtil.NAMESPACE, methodName);
            requestSoapObject.addProperty("customID", MyApplication
                    .getmCustomModel(mContext).getDjLsh());
            requestSoapObject.addProperty("openKey", MyApplication
                    .getmCustomModel(mContext).getOpenKey());
            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                    mContext, this, requestSoapObject, methodName);
            connectCustomServiceAsyncTask.execute();
            return;
        }
        //获取我的飞币数量
        if (methodName == InformationCodeUtil.methodNameGetAcceptFlyCoin) {
            SoapObject requestSoapObject = new SoapObject(
                    ConnectServiceUtil.NAMESPACE, methodName);
            requestSoapObject.addProperty("customID", MyApplication
                    .getmCustomModel(mContext).getDjLsh());
            ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
                    mContext, this, requestSoapObject, methodName);
            connectGoodsServiceAsyncTask.initProgressDialog(false, "请稍等...");
            connectGoodsServiceAsyncTask.execute();
            return;
        }

    }


    private void toCreateOrderMethodSelectActivity() {

        String strCustomerName = tv_customerName.getText().toString();
        String strCustomerReceiverAddress = tv_customerReceiverAddress.getText().toString();
        String strCustomerPhone = tv_customerPhone.getText().toString();

        if (StringUtil.isEmpty(strCustomerName)) {
            ToastUtil.show(this, "请输入收货人姓名");
            return;
        }
        if (StringUtil.isEmpty(strCustomerReceiverAddress)) {
            ToastUtil.show(this, "收货地址不能为空");
            return;
        }
        if (StringUtil.isEmpty(strCustomerPhone)) {
            ToastUtil.show(this, "联系方式不能为空");
            return;
        }
        String sportPlatformActionID = "" + currentSelectPackageMode.getmProductModel().getPlatformActionID();
        int sportPlatformActionType = currentSelectPackageMode.getmProductModel().getPlatformActionType();
        String sportRemark = et_orderRemark.getText().toString();
        if (StringUtil.isEmpty(sportRemark)) {
            sportRemark = "";
        }

        String methodName = InformationCodeUtil.methodNameAddOrderExt;
        SoapObject requestSoapObject = new SoapObject(
                ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("customID", MyApplication
                .getmCustomModel(mContext).getDjLsh());
        requestSoapObject.addProperty("openKey", MyApplication
                .getmCustomModel(mContext).getOpenKey());
        requestSoapObject.addProperty("orderJson", getOrderInfoJsonString());
        requestSoapObject.addProperty("itemsJson", getItemJsonString());
        requestSoapObject.addProperty("platformActionID", sportPlatformActionID);
        requestSoapObject.addProperty("platformActionType", sportPlatformActionType);
        requestSoapObject.addProperty("remark", sportRemark);
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, requestSoapObject, methodName);
        connectCustomServiceAsyncTask.initProgressDialog(true, "正在提交订单...");
        connectCustomServiceAsyncTask.execute();

//        CreateOrderMethodSelectModel paramSport = new CreateOrderMethodSelectModel(1, totalOrderPrices,
//                getOrderInfoJsonString(), getItemJsonString(), sportPlatformActionID
//                , sportPlatformActionType, sportRemark, CreateOrderMethodSelectModel.TypeConfirmOrderSport);
//
//        Intent mIntent = new Intent(mContext, CreateOrderMethodSelectActivity.class);
//        mIntent.putExtra(InformationCodeUtil.IntentPayMethodSelectActivityParamObject, paramSport);
//        startActivityForResult(mIntent, RequestCode_PayMethodSelectActivity);
//        overridePendingTransition(R.anim.in_from_bottom, R.anim.not_change);
    }

    void initViewHeaderData(AddressModel mAddressModel) {
        tv_customerName.setText("" + mAddressModel.getRealName());
        tv_customerPhone.setText("" + mAddressModel.getPhoneNum());
        tv_customerReceiverAddress.setText("" + mAddressModel.getProvinceName()
                + mAddressModel.getCityName() + mAddressModel.getAreaName()
                + "\n" + mAddressModel.getAddress());
    }

    String getOrderInfoJsonString() {
        Gson gson = new Gson();
        OrderInfoModel mOrderInfoModel = new OrderInfoModel();
        mOrderInfoModel.setDjLsh(0);
        mOrderInfoModel.setUserID(MyApplication.getmCustomModel(mContext).getDjLsh());
        String strTotalPriceTextView = tv_totalPrices.getText()
                .toString();
        LogUtil.LogShitou(strTotalPriceTextView);
        mOrderInfoModel.setTotalMoney(totalOrderPrices);
        mOrderInfoModel.setAddress(tv_customerReceiverAddress.getText().toString());
        mOrderInfoModel.setRealName(tv_customerName.getText().toString());
        mOrderInfoModel.setPhoneNum(tv_customerPhone.getText().toString());
        mOrderInfoModel.setIfBilling(cb_confirmOrderIfOpenBilling.isChecked());
        mOrderInfoModel.setBillingName(et_InvoiceNumberCode.getText()
                .toString());
        mOrderInfoModel.setPostMethondID(2);
        mOrderInfoModel.setPostMethondName("普通快递");
//		mOrderInfoModel.setFlyCoin(currentTotalFlyCoin);
        mOrderInfoModel.setOrderSign(1);
        return gson.toJson(mOrderInfoModel);
    }

    String getItemJsonString() {
        Gson gson = new Gson();
        List<OrderItemModel> list = new ArrayList<OrderItemModel>();
        OrderItemModel mOrderItemModel = new OrderItemModel();
        mOrderItemModel.setDjLsh(-1);
        mOrderItemModel.setOrderID(0);
        mOrderItemModel.setGoodsID(currentSelectPackageMode.getmProductModel()
                .getDjLsh());
        mOrderItemModel.setGoodsName(currentSelectPackageMode.getmProductModel()
                .getGoodsName());
        mOrderItemModel.setPackageID(currentSelectPackageMode.getmPackages()
                .getGoodsID());
        mOrderItemModel.setPackageName(currentSelectPackageMode.getmPackages()
                .getPackageName());
        mOrderItemModel.setColorID(currentSelectPackageMode.getmPackageColorModel()
                .getDjLsh());
        mOrderItemModel.setColorName(currentSelectPackageMode
                .getmPackageColorModel().getColorName());
        mOrderItemModel.setUnitPrice(currentSelectPackageMode
                .getmPackageColorModel().getPrice());
        mOrderItemModel.setUnitTaxPrice(currentSelectPackageMode
                .getmPackageColorModel().getTaxPrice());
        mOrderItemModel.setBuyCount(currentSelectPackageMode.getPurchaseQuantity());
        mOrderItemModel.setImgUrl(currentSelectPackageMode.getmProductModel()
                .getImgUrl());
        list.add(mOrderItemModel);

        return gson.toJson(list);
    }

    /**
     * 获取当前商品总价格
     */
    void getTotalPrice() {
        //首先获取可抵用的最大飞币数量
        int totalPrivilegeFlyCoin = getCurrentPrivilegeTotalFlyCoin();
        //计算商品总价格
        Double orderPrices = 0.00;
        if (cb_confirmOrderIfOpenBilling.isChecked()) {
            orderPrices += currentSelectPackageMode.getmPackageColorModel()
                    .getTaxPrice() * currentSelectPackageMode.getPurchaseQuantity();
        } else {
            orderPrices += currentSelectPackageMode.getmPackageColorModel().getPrice()
                    * currentSelectPackageMode.getPurchaseQuantity();
        }

        totalOrderPrices = orderPrices;
        if (cb_confirmOrderIfOpenBilling.isChecked()) {
            tv_totalPrices.setText(StringUtil.doubleToString(
                    totalOrderPrices - totalPrivilegeFlyCoin, "0.00") + "(含税)");
        } else {
            tv_totalPrices.setText(StringUtil.doubleToString(
                    totalOrderPrices - totalPrivilegeFlyCoin, "0.00"));
        }

    }

    /**
     * 首先获取可抵用的最大飞币数量
     */
    int getCurrentPrivilegeTotalFlyCoin() {
        int totalPrivilegeFlyCoin = 0;
        if (cb_confirmOrderIfOpenBilling.isChecked()) {
            totalPrivilegeFlyCoin += currentSelectPackageMode.getmPackageColorModel().getTaxFlyCoin()
                    * currentSelectPackageMode.getPurchaseQuantity();
        } else {
            totalPrivilegeFlyCoin += currentSelectPackageMode.getmPackageColorModel().getFlyCoin()
                    * currentSelectPackageMode.getPurchaseQuantity();
        }
        tv_privilegeFlyCoinDesc.setText(
                "(该订单最高可抵" + totalPrivilegeFlyCoin + "飞币，您有" + totalFlyCoin_web + "飞币)");
        if (totalPrivilegeFlyCoin > totalFlyCoin_web) {
            totalPrivilegeFlyCoin = totalFlyCoin_web;
        }
        tv_privilegeFlyCoin.setText("" + totalPrivilegeFlyCoin);
        return totalPrivilegeFlyCoin;
    }



    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, Integer state, boolean whetherRefresh) {
        Gson gson = new Gson();
        if (methodName == InformationCodeUtil.methodNameGetAddrList) {
            List<AddressModel> returnList = null;
            try {
                JSONObject jsonObject = new JSONObject(returnString);
                String strMsg = jsonObject.getString("Msg");
                // 获取 我的收货地址 成功
                JSONResultBaseModel<AddressModel> mJSONAddrModel = gson.fromJson(
                        strMsg, new TypeToken<JSONResultBaseModel<AddressModel>>() {
                        }.getType());
                returnList = mJSONAddrModel.getList();

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (returnList != null && returnList.size() > 0) {
                initViewHeaderData(returnList.get(0));
            } else {
                showAddReceiverAddressDialog();
            }
        }
        if (methodName == InformationCodeUtil.methodNameGetAcceptFlyCoin) {
            try {
                JSONResultMsgModel result = gson.fromJson(returnString, JSONResultMsgModel.class);
                totalFlyCoin_web = Integer.parseInt(result.getMsg());
            } catch (Exception e) {
                e.printStackTrace();
            }
            getTotalPrice();
        }

        // 创建活动订单成功{"DjLsh":378,"Msg":"操作成功!","Sign":1,"Tags":""}
        if (methodName == InformationCodeUtil.methodNameAddOrderExt) {
            try {
                JSONObject json = new JSONObject(returnString);
                String totalOrderIds = json.getString("DjLsh");
                int sign = json.getInt("Sign");
                if (sign == 1) {
                    toPayMethodSelectActivity( totalOrderIds, totalOrderPrices);
                } else {
                    showPopWindowOfConfirmResult(false, "订单提交失败," + json.getString("Msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //提交返回结果提醒弹窗
    void showPopWindowOfConfirmResult(final boolean flagIfSuccess, String msg) {
        View view = View.inflate(mContext, R.layout.item_pop_result_of_confirm_order, null);
        final PopupWindow pop = new PopupWindow(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, true);
        //订单是否提交成功
        ImageView iv_confirmOrderResultDesc = (ImageView) view.findViewById(R.id.iv_confirmOrderResultDesc);
        TextView tv_confirmOrderResultDesc = (TextView) view.findViewById(R.id.tv_confirmOrderResultDesc);
        TextView tv_orderWillDelete = (TextView) view.findViewById(R.id.tv_orderWillDelete);

        //收货人信息，继续购物，去我的订单，重复提交控件
        LinearLayout linearLayout_toQuery = (LinearLayout) view.findViewById(R.id.linearLayout_toQuery);
        LinearLayout linearLayout_toBack = (LinearLayout) view.findViewById(R.id.linearLayout_toBack);

        if (flagIfSuccess) {

            iv_confirmOrderResultDesc.setImageResource(R.drawable.iv_confirm_order_success);
            tv_confirmOrderResultDesc.setText(msg);

            linearLayout_toQuery.setVisibility(View.VISIBLE);
            linearLayout_toBack.setVisibility(View.GONE);
            linearLayout_toQuery.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    pop.dismiss();
                }
            });


        } else {
            iv_confirmOrderResultDesc.setImageResource(R.drawable.iv_confirm_order_false);
            tv_confirmOrderResultDesc.setText(msg);
            tv_orderWillDelete.setVisibility(View.GONE);

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
                if (flagIfSuccess) {
                    finish();
                }
            }
        });
        pop.showAtLocation(rootView, Gravity.TOP, 0, 0);
    }

    /**
     * 进入到付款界面
     **/
    private void toPayMethodSelectActivity(String totalOrderIds, double totalOrderPrices) {
        Intent intent = new Intent(mContext, PayMethodSelectActivity.class);
        intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalMoney, totalOrderPrices);
        intent.putExtra(InformationCodeUtil.IntentPayMethodSelectActivityIfFromBottom, false);
        intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds, totalOrderIds);
        startActivityForResult(intent, RequestCode_PayMethodSelectActivity);
    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Integer state,
                                     boolean whetherRefresh) {
        if (methodName == InformationCodeUtil.methodNameGetAddrList) {
            showAddReceiverAddressDialog();
        } else if (methodName == InformationCodeUtil.methodNameAddOrderExt) {
            showPopWindowOfConfirmResult(false, "订单提交失败," + returnStrError);

        } else if (methodName == InformationCodeUtil.methodNameGetAcceptFlyCoin) {
            ToastUtil.show(mContext, returnStrError + "飞币数量获取失败");
            getTotalPrice();
        }
    }

    @Override
    public void connectServiceCancelled(String returnString,
                                        String methodName, Integer state, boolean whetherRefresh) {
    }


    //添加收货地址
    void showAddReceiverAddressDialog() {

        final FragmentOkAndCancelDialog fragmentDailog = new FragmentOkAndCancelDialog();
        fragmentDailog.initView("提示", "没有收货地址，请添加收货地址", "取消", "添加",
                new FragmentOkAndCancelDialog.OnButtonClickListener() {

                    @Override
                    public void OnOkClick() {
                        Intent mIntent = new Intent(ConfirmOrderSportActivity.this,
                                ReceiveAddressSelectActivity.class);
                        startActivityForResult(mIntent, RequestCode_ShoppingAddressSelectActivity);
                        fragmentDailog.dismiss();
                    }

                    @Override
                    public void OnCancelClick() {
                        fragmentDailog.dismiss();
                    }

                });
        fragmentDailog.show(getSupportFragmentManager(), "fragmentDailog");
    }


    /**
     * 如果是返回键，则关闭当前页面
     **/
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //用户选择了新的收货地址
        if (requestCode == RequestCode_ShoppingAddressSelectActivity) {
            if(RESULT_OK == resultCode){
                AddressModel mAddressModel = (AddressModel) data
                        .getSerializableExtra("selectAddressModel");
                initViewHeaderData(mAddressModel);
            }
        }
        //用户成功生成了订单
        if (requestCode == RequestCode_PayMethodSelectActivity) {
            IfOpenFinishActivityAnim(false);
            Intent intent = new Intent(mContext, MyOrderActivity.class);
            mContext.startActivity(intent);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /***
     * 订单详细
     *
     * @author ZHU
     */
    private class OrderItemModel {
        /**
         * 单据流水号
         **/
        private int DjLsh;
        /**
         * 订单号
         */
        private int OrderID;
        /**
         * 商品编号
         **/
        private int GoodsID;
        /**
         * 商品名称
         **/
        private String GoodsName;
        /**
         * 套餐编号
         **/
        private int PackageID;
        /**
         * 套餐名称
         **/
        private String PackageName;
        /**
         * 颜色编号
         **/
        private int ColorID;
        /**
         * 颜色名称
         **/
        private String ColorName;
        /**
         * 单价
         **/
        private double UnitPrice;
        /**
         * 含税单价
         **/
        private double UnitTaxPrice;
        /**
         * 购买数量
         **/
        private long BuyCount;
        /**
         * 延迟付款的天数
         **/
        private int DelayPayDays;
        /**
         * 图片路径
         **/
        private String ImgUrl;
        /**
         * 订单备注
         **/
        private String Remark;

        public int getDjLsh() {
            return DjLsh;
        }

        public void setDjLsh(int djLsh) {
            DjLsh = djLsh;
        }

        public int getOrderID() {
            return OrderID;
        }

        public void setOrderID(int orderID) {
            OrderID = orderID;
        }

        public int getGoodsID() {
            return GoodsID;
        }

        public void setGoodsID(int goodsID) {
            GoodsID = goodsID;
        }

        public String getGoodsName() {
            return GoodsName;
        }

        public void setGoodsName(String goodsName) {
            GoodsName = goodsName;
        }

        public int getPackageID() {
            return PackageID;
        }

        public void setPackageID(int packageID) {
            PackageID = packageID;
        }

        public String getPackageName() {
            return PackageName;
        }

        public void setPackageName(String packageName) {
            PackageName = packageName;
        }

        public int getColorID() {
            return ColorID;
        }

        public void setColorID(int colorID) {
            ColorID = colorID;
        }

        public String getColorName() {
            return ColorName;
        }

        public void setColorName(String colorName) {
            ColorName = colorName;
        }

        public double getUnitPrice() {
            return UnitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            UnitPrice = unitPrice;
        }

        public double getUnitTaxPrice() {
            return UnitTaxPrice;
        }

        public void setUnitTaxPrice(double unitTaxPrice) {
            UnitTaxPrice = unitTaxPrice;
        }

        public long getBuyCount() {
            return BuyCount;
        }

        public void setBuyCount(long buyCount) {
            BuyCount = buyCount;
        }

        public int getDelayPayDays() {
            return DelayPayDays;
        }

        public void setDelayPayDays(int delayPayDays) {
            DelayPayDays = delayPayDays;
        }

        public String getImgUrl() {
            return ImgUrl;
        }

        public void setImgUrl(String imgUrl) {
            ImgUrl = imgUrl;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }
    }

    /***
     * 用户订单模型类
     *
     * @author ZHU
     */
    public class OrderInfoModel {
        /**
         * 自动编号
         **/
        private int DjLsh;
        /**
         * 用户编号
         **/
        private int UserID;
        /**
         * 总金额
         **/
        private double TotalMoney;
        /**
         * 收货地址
         **/
        private String Address;
        /**
         * 真实姓名
         **/
        private String RealName;
        /**
         * 电话号码
         **/
        private String PhoneNum;
        /**
         * 是否开票
         **/
        private boolean IfBilling;
        /**
         * 发票抬头
         **/
        private String BillingName;
        /**
         * 支付方式编号
         **/
        private int PostMethondID;
        /**
         * 支付方式名称
         **/
        private String PostMethondName;
        /**
         * 订单状态
         **/
        private int OrderSign;
        /**
         * 订单号
         **/
        private String OrderNo;
        /**
         * 下单时间
         **/
        private String OrderTime;
        /**
         * 首个商品编号
         **/
        private int FirstGoodsID;
        /**
         * 首个商品名称
         **/
        private String FirstGoodsName;
        /**
         * 首个商品图片
         **/
        private String FirstImgUrl;
        /**
         * 首个套餐名称
         **/
        private String FirstPackageName;
        /**
         * 首个颜色名称
         **/
        private String FirstColorName;
        /**
         * 首个商品单价
         **/
        private double FirstGoodsUnitMoney;
        /**
         * 首个商品数量
         **/
        private int FirstGoodsBuyCount;
        /**
         * 延迟付款的天数
         **/
        private int DelayPayDays;
        /**
         * 订单备注
         **/
        private String Remark;

        /**
         * 快递名称
         **/
        private String PostName;
        /**
         * 快递编号
         **/
        private String PostNameNum;

        /**
         * 凭证图片
         **/
        private String Certifi;
        /**
         * 凭证状态(0未打款， 1已通过)
         **/
        private int CertifiState;

        /**
         * 使用飞币的数量
         **/
        private int FlyCoin;
        /**
         * 订单商品允许抵扣的最大飞币数
         **/
        private int AcceptFlyCoin;
        /**
         * 信用支付倒计时
         **/
        private int CountDown;
        /**
         * 账期是否付款
         **/
        private boolean IsAccountLimitPayed;

        /**
         * 订单是否被选中
         **/
        private boolean whetherSelect = false;

        public int getDjLsh() {
            return DjLsh;
        }

        public void setDjLsh(int djLsh) {
            DjLsh = djLsh;
        }

        public int getUserID() {
            return UserID;
        }

        public void setUserID(int userID) {
            UserID = userID;
        }

        public double getTotalMoney() {
            return TotalMoney;
        }

        public void setTotalMoney(double totalMoney) {
            TotalMoney = totalMoney;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getRealName() {
            return RealName;
        }

        public void setRealName(String realName) {
            RealName = realName;
        }

        public String getPhoneNum() {
            return PhoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            PhoneNum = phoneNum;
        }

        public boolean isIfBilling() {
            return IfBilling;
        }

        public void setIfBilling(boolean ifBilling) {
            IfBilling = ifBilling;
        }

        public String getBillingName() {
            return BillingName;
        }

        public void setBillingName(String billingName) {
            BillingName = billingName;
        }

        public int getPostMethondID() {
            return PostMethondID;
        }

        public void setPostMethondID(int postMethondID) {
            PostMethondID = postMethondID;
        }

        public String getPostMethondName() {
            return PostMethondName;
        }

        public void setPostMethondName(String postMethondName) {
            PostMethondName = postMethondName;
        }

        public int getOrderSign() {
            return OrderSign;
        }

        public void setOrderSign(int orderSign) {
            OrderSign = orderSign;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String orderNo) {
            OrderNo = orderNo;
        }

        public String getOrderTime() {
            return OrderTime;
        }

        public void setOrderTime(String OrderTime) {
            this.OrderTime = OrderTime;
        }

        public String getFirstGoodsName() {
            return FirstGoodsName;
        }

        public void setFirstGoodsName(String firstGoodsName) {
            FirstGoodsName = firstGoodsName;
        }

        public String getFirstImgUrl() {
            return FirstImgUrl;
        }

        public void setFirstImgUrl(String firstImgUrl) {
            FirstImgUrl = firstImgUrl;
        }

        public String getFirstPackageName() {
            return FirstPackageName;
        }

        public void setFirstPackageName(String firstPackageName) {
            FirstPackageName = firstPackageName;
        }

        public String getFirstColorName() {
            return FirstColorName;
        }

        public void setFirstColorName(String firstColorName) {
            FirstColorName = firstColorName;
        }

        public double getFirstGoodsUnitMoney() {
            return FirstGoodsUnitMoney;
        }

        public void setFirstGoodsUnitMoney(double firstGoodsUnitMoney) {
            FirstGoodsUnitMoney = firstGoodsUnitMoney;
        }

        public int getFirstGoodsBuyCount() {
            return FirstGoodsBuyCount;
        }

        public void setFirstGoodsBuyCount(int firstGoodsBuyCount) {
            FirstGoodsBuyCount = firstGoodsBuyCount;
        }

        public int getDelayPayDays() {
            return DelayPayDays;
        }

        public void setDelayPayDays(int delayPayDays) {
            DelayPayDays = delayPayDays;
        }

        public boolean getWhetherSelect() {
            return whetherSelect;
        }

        public void setWhetherSelect(boolean whetherSelect) {
            this.whetherSelect = whetherSelect;
        }

        public int getFlyCoin() {
            return FlyCoin;
        }

        public void setFlyCoin(int flyCoin) {
            FlyCoin = flyCoin;
        }

        public int getAcceptFlyCoin() {
            return AcceptFlyCoin;
        }

        public void setAcceptFlyCoin(int acceptFlyCoin) {
            AcceptFlyCoin = acceptFlyCoin;
        }

        public int getCountDown() {
            return CountDown;
        }

        public void setCountDown(int countDown) {
            CountDown = countDown;
        }

        public int getFirstGoodsID() {
            return FirstGoodsID;
        }

        public void setFirstGoodsID(int firstGoodsID) {
            FirstGoodsID = firstGoodsID;
        }

        public String getPostName() {
            return PostName;
        }

        public void setPostName(String postName) {
            PostName = postName;
        }

        public String getPostNameNum() {
            return PostNameNum;
        }

        public void setPostNameNum(String postNameNum) {
            PostNameNum = postNameNum;
        }

        public String getCertifi() {
            return Certifi;
        }

        public void setCertifi(String certifi) {
            Certifi = certifi;
        }

        public int getCertifiState() {
            return CertifiState;
        }

        public void setCertifiState(int certifiState) {
            CertifiState = certifiState;
        }

        public boolean isIsAccountLimitPayed() {
            return IsAccountLimitPayed;
        }

        public void setIsAccountLimitPayed(boolean isAccountLimitPayed) {
            IsAccountLimitPayed = isAccountLimitPayed;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

    }

}









