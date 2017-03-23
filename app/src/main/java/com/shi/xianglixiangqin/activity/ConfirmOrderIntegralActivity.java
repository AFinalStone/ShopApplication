package com.shi.xianglixiangqin.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
import com.shi.xianglixiangqin.model.AddressModel;
import com.shi.xianglixiangqin.model.CreateOrderMethodSelectModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * @author ZHU
 * @action 确认订单界面
 * @date 2015-8-9 上午9:42:11
 */
public class ConfirmOrderIntegralActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer> {
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
     * 积分数量
     **/
    @BindView(R.id.tv_integralExChangeNum)
    TextView tv_integralExChangeNum;

    /**
     * 订单类型选择
     **/
    private final int RequestCode_PayMethodSelectActivity = 2;
    /**
     * 选择收货地址
     **/
    private final int RequestCode_ShoppingAddressSelectActivity = 1;


    CreateOrderMethodSelectModel param;
    /**
     * 当前商品总价格
     **/
    private Double totalOrderPrices = 0.00;

    /**
     * 当前界面根View
     **/
    private View rootView;

    public void initView() {
        // 初始化主页布局
        rootView = View.inflate(mContext, R.layout.activity_confirm_order_integral, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.confirmOrderGeneralActivity_title);
    }


    @OnClick({R.id.iv_titleLeft, R.id.btn_submit, R.id.iv_managerReceiverAddress})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.iv_managerReceiverAddress:
                Intent mIntent = new Intent(mContext, ReceiveAddressSelectActivity.class);
                startActivityForResult(mIntent, RequestCode_ShoppingAddressSelectActivity);
                break;
            case R.id.btn_submit:
                toCreateOrderMethodSelectActivity();
            default:
                break;
        }
    }

    public void initData() {
        Intent mIntent = getIntent();
        param = (CreateOrderMethodSelectModel) mIntent.getSerializableExtra(InformationCodeUtil.IntentPayMethodSelectActivityParamObject);
        String goodsImageUrl = mIntent.getStringExtra(InformationCodeUtil.IntentConfirmOrderIntegralActivityGoodsImageUrl);
        String goodsName = mIntent.getStringExtra(InformationCodeUtil.IntentConfirmOrderIntegralActivityGoodsName);
        double goodsPrices = mIntent.getDoubleExtra(InformationCodeUtil.IntentConfirmOrderIntegralActivityGoodsPrices, 0.00);
        long goodsJf = mIntent.getLongExtra(InformationCodeUtil.IntentConfirmOrderIntegralActivityGoodsJf, 0);

        ImagerLoaderUtil.getInstance(mContext).displayMyImage(
                goodsImageUrl, iv_goodsImages);
        tv_goodsName.setText(goodsName);
        tv_goodsPackageAndColor.setText(param.getParamJF_TCName() + "/" + param.getParamJF_YSName());
        tv_goodsNum.setText("×" + param.getParamJF_BuyNum());
        tv_goodsPrice.setText(StringUtil.doubleToString(goodsPrices, "0.00"));
        tv_integralExChangeNum.setText("" + goodsJf * param.getParamJF_BuyNum());
        totalOrderPrices = goodsPrices * param.getParamJF_BuyNum();
        tv_totalPrices.setText(StringUtil.doubleToString(totalOrderPrices, "0.00"));
        getData(InformationCodeUtil.methodNameGetAddrList);
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
//        param.setParamJF_SHUserName(strCustomerName);
//        param.setParamJF_SHAddress(strCustomerReceiverAddress);
//        param.setParamJF_SHUserPhone(strCustomerPhone);
//        param.setTypeComeFromView(CreateOrderMethodSelectModel.TypeConfirmOrderJF);
//        param.setParamJF_SHRemark(et_orderRemark.getText().toString());
//        Intent mIntent = new Intent(mContext, CreateOrderMethodSelectActivity.class);
//        mIntent.putExtra(InformationCodeUtil.IntentPayMethodSelectActivityParamObject, param);
//        startActivityForResult(mIntent, RequestCode_PayMethodSelectActivity);
//        overridePendingTransition( R.anim.in_from_bottom, R.anim.not_change);

        String methodName = InformationCodeUtil.methodNameAddJFOrder;
        SoapObject requestSoapObject = new SoapObject(
                ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
        requestSoapObject.addProperty("userid", MyApplication.getmCustomModel(mContext).getDjLsh());
        requestSoapObject.addProperty("shopid", param.getParamJF_ShopID());
        requestSoapObject.addProperty("gid", param.getParamJF_GoodsID());
        requestSoapObject.addProperty("buynum", param.getParamJF_BuyNum());
        requestSoapObject.addProperty("tcname", param.getParamJF_TCName());
        requestSoapObject.addProperty("ysname", param.getParamJF_YSName());
        requestSoapObject.addProperty("isalljf", param.getParamJF_BuyType());
        requestSoapObject.addProperty("shadds", strCustomerReceiverAddress);
        requestSoapObject.addProperty("shuname", strCustomerName);
        requestSoapObject.addProperty("shphone", strCustomerPhone);
        requestSoapObject.addProperty("fptt", "");
        requestSoapObject.addProperty("remark", et_orderRemark.getText().toString());
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, requestSoapObject, methodName);
        connectCustomServiceAsyncTask.execute();
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

    }

    void initViewHeaderData(AddressModel mAddressModel) {
        tv_customerName.setText("" + mAddressModel.getRealName());
        tv_customerPhone.setText("" + mAddressModel.getPhoneNum());
        tv_customerReceiverAddress.setText("" + mAddressModel.getProvinceName()
                + mAddressModel.getCityName() + mAddressModel.getAreaName()
                + "\n" + mAddressModel.getAddress());
    }


    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, Integer state, boolean whetherRefresh) {
        LogUtil.LogShitou(returnString);
        if (methodName == InformationCodeUtil.methodNameGetAddrList) {
            List<AddressModel> returnList = null;
            try {
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(returnString);
                String strMsg = jsonObject.getString("Msg");
                int Sign = jsonObject.getInt("Sign");
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

        //{"DjLsh":1280,"Msg":"兑换成功，请耐性等待发货","Sign":1,"Tags":"1280","orderId":1280}
        //{"DjLsh":1281,"Msg":"兑换成功，请付款差额","Sign":1,"Tags":"1281","orderId":1281}
        if (methodName == InformationCodeUtil.methodNameAddJFOrder) {
            try {
                JSONObject json = new JSONObject(returnString);
                String totalOrderIds = json.getString("DjLsh");
                int sign = json.getInt("Sign");
                if (sign == 1) {
                    if(param.getParamJF_BuyType() == 1){
                        showPopWindowOfConfirmResult(true, json.getString("Msg"));
                    }else{
                        toPayMethodSelectActivity(totalOrderIds, totalOrderPrices);
                    }
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
                    Intent intent = new Intent(mContext, MyOrderActivity.class);
                    mContext.startActivity(intent);
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
            return;
        }
        if (methodName == InformationCodeUtil.methodNameAddJFOrder) {
            showPopWindowOfConfirmResult(false, "订单提交失败," + returnStrError);
        }
    }

    @Override
    public void connectServiceCancelled(String returnString,
                                        String methodName, Integer state, boolean whetherRefresh) {
    }


    //添加收货地址
    void showAddReceiverAddressDialog() {

        final FragmentOkAndCancelDialog fragmentDialog = new FragmentOkAndCancelDialog();
        fragmentDialog.initView("提示", "没有收货地址，请添加收货地址", "取消", "添加",
                new FragmentOkAndCancelDialog.OnButtonClickListener() {

                    @Override
                    public void OnOkClick() {
                        Intent mIntent = new Intent(ConfirmOrderIntegralActivity.this,
                                ReceiveAddressManageActivity.class);
                        startActivityForResult(mIntent, RequestCode_ShoppingAddressSelectActivity);
                        fragmentDialog.dismiss();
                    }

                    @Override
                    public void OnCancelClick() {
                        fragmentDialog.dismiss();
                    }

                });
        fragmentDialog.show(getSupportFragmentManager(), "fragmentDialog");
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
            if (RESULT_OK == resultCode) {
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


}









