package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.model.CreateOrderMethodSelectModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateOrderMethodSelectActivity extends MyBaseTranslucentActivity implements OnConnectServerStateListener<Integer> {

    @BindView(R.id.listView)
    ListView listView;
    ListAdapter listAdapter;
    List<PayMethodModel> listData = new ArrayList<PayMethodModel>();

    /**
     * 确认支付
     **/
    @BindView(R.id.btn_queryToPay)
    Button btn_queryToPay;

    /**
     * 背景
     **/
    @BindView(R.id.linearLayout_background)
    LinearLayout linearLayout_background;


    /**
     * 当前订单编号集合字段
     **/
    private String totalOrderIds;
    /**
     * 订单总金额
     **/
    private Double totalOrderPrices;
    /**
     * 当前界面根View
     **/
    private View rootView;
    /**
     * 请求码
     **/
    private final int RequestCode_CreateOrderMethodSelectActivity = 1;
    /**
     * 是否成功创建订单
     **/
    private boolean IfCreateOrderSuccess = false;
    /**
     * 根据这个标记来判断是从那种类型的页面进入当前页面的
     **/
    private int TypeOfComeFromView;

    private CreateOrderMethodSelectModel paramModel;

    private String currentSelectMethodName;

    private final String Name_PayMethodXianKuan = "现款订单";
    private final String Name_PayMethodZhangQi = "账期订单";

    private final String Desc_PayMethodZhangQi_OFF = "含有非账期订单不能使用此支付方式";
    private final String Desc_PayMethodZhangQi_ON = "";
    private final String Desc_PayMethodXianKuan = "";

    private int selectPosition;

    @Override
    public void initView() {
        rootView = View.inflate(mContext, R.layout.activity_create_order_method_select, null);
        setContentView(rootView);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        paramModel = (CreateOrderMethodSelectModel) getIntent()
                .getSerializableExtra(InformationCodeUtil.IntentPayMethodSelectActivityParamObject);
        if (paramModel == null) {
            previewToDestroy();
            return;
        }
        TypeOfComeFromView = paramModel.getTypeComeFromView();
        totalOrderIds = paramModel.getTotalOrderIds();
        totalOrderPrices = paramModel.getTotalPrices();

        listData.add(new PayMethodModel(R.mipmap.icon_create_order_xiankuan, Name_PayMethodXianKuan, Desc_PayMethodXianKuan));
        switch (TypeOfComeFromView) {
            case CreateOrderMethodSelectModel.TypeConfirmOrderGeneralCanPayCredit:
            case CreateOrderMethodSelectModel.TypeMyOrderCanPayCredit:
                listData.add(new PayMethodModel(R.mipmap.icon_create_order_zhangqi, Name_PayMethodZhangQi, Desc_PayMethodZhangQi_ON));
                break;
            default:
                listData.add(new PayMethodModel(R.mipmap.icon_create_order_zhangqi, Name_PayMethodZhangQi, Desc_PayMethodZhangQi_OFF));
        }

        listAdapter = new ListAdapter(mContext, listData);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!Desc_PayMethodZhangQi_OFF.equals(listData.get(position).getDesc())) {
                    for (int i = 0; i < listData.size(); i++) {
                        listData.get(i).setIfSelect(false);
                    }
                    selectPosition = position;
                    btn_queryToPay.setEnabled(true);
                    listData.get(position).setIfSelect(true);
                    listAdapter.notifyDataSetChanged();
                }

            }
        });
    }


    @OnClick({R.id.iv_closeActivity, R.id.btn_queryToPay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_closeActivity:
                previewToDestroy();
                break;
            case R.id.btn_queryToPay:
                queryToPay();
                break;
        }
    }

    /**
     * 去支付
     **/
    private void queryToPay() {
        PayMethodModel payMethod = listData.get(selectPosition);
        currentSelectMethodName = payMethod.getName();
        if (Name_PayMethodZhangQi.equals(currentSelectMethodName)) {
            switch (TypeOfComeFromView) {
                case CreateOrderMethodSelectModel.TypeConfirmOrderGeneralCanPayCredit:
                    createGeneralGoodsOrder(true, false);
                    break;
                case CreateOrderMethodSelectModel.TypeConfirmOrderZHCanPayCredit:
                    createGeneralGoodsOrder(true, true);
                    break;
            }

        } else if (Name_PayMethodXianKuan.equals(currentSelectMethodName)) {
            switch (TypeOfComeFromView) {
                case CreateOrderMethodSelectModel.TypeConfirmOrderGeneralCanPayCredit:
                case CreateOrderMethodSelectModel.TypeConfirmOrderGeneralCanNotPayCredit:
                    createGeneralGoodsOrder(false, false);
                    break;
                case CreateOrderMethodSelectModel.TypeConfirmOrderZHCanPayCredit:
                case CreateOrderMethodSelectModel.TypeConfirmOrderZHCanNotPayCredit:
                    createGeneralGoodsOrder(false, true);
                    break;
//             case CreateOrderMethodSelectModel.TypeConfirmOrderSport:
//                    toCreateSportGoodsOrder();
//                    break;
//                case CreateOrderMethodSelectModel.TypeConfirmOrderJF:
//                    toCreateJFGoodsOrder();
//                    break;
            }
        }
    }

    /**
     * 创建普通商品订单
     **/
    private void createGeneralGoodsOrder(boolean IfZhangQi, boolean IfZhGoods) {
        String methodName = InformationCodeUtil.methodNameCreateOrder;
        SoapObject requestSoapObject = new SoapObject(
                ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("customID", MyApplication
                .getmCustomModel(mContext).getDjLsh());
        requestSoapObject.addProperty("openKey", MyApplication
                .getmCustomModel(mContext).getOpenKey());
        requestSoapObject.addProperty("postShoppingCarts", paramModel.getParamPostShoppingCarts());
        requestSoapObject.addProperty("address", paramModel.getParamAddress());
        requestSoapObject.addProperty("realName", paramModel.getParamRealName());
        requestSoapObject.addProperty("phoneNum", paramModel.getParamPhoneNum());
        requestSoapObject.addProperty("payType", -1);//0在线支付, 1账期支付, 2线下支付, -1新接口
        if (IfZhangQi) {
            requestSoapObject.addProperty("zq", 1);//0-无账期订单，1-有账期订单
        } else {
            requestSoapObject.addProperty("zq", 0);//0-无账期订单，1-有账期订单
        }
        if (IfZhGoods) {
            requestSoapObject.addProperty("platformActionID", paramModel.getParamZHGoodsID());
            requestSoapObject.addProperty("platformActionType", 4);
            requestSoapObject.addProperty("buygoods", paramModel.getParamZHBuyGoods());//0：普通订单，4：组合订单
        } else {
            requestSoapObject.addProperty("platformActionID", 0);//普通订单请传0，组合订单请传组合ID
            requestSoapObject.addProperty("platformActionType", 0);//0：普通订单，4：组合订单
            requestSoapObject.addProperty("buygoods", "");//0：普通订单，4：组合订单
        }
        LogUtil.LogShitou(requestSoapObject.toString());
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, requestSoapObject, methodName, IfZhangQi);
        connectCustomServiceAsyncTask.initProgressDialog(true, "正在提交订单...");
        connectCustomServiceAsyncTask.execute();
    }

//    /**
//     * 创建活动商品订单
//     **/
//    private void toCreateSportGoodsOrder() {
//
//        String methodName = InformationCodeUtil.methodNameAddOrderExt;
//        SoapObject requestSoapObject = new SoapObject(
//                ConnectServiceUtil.NAMESPACE, methodName);
//        requestSoapObject.addProperty("customID", MyApplication
//                .getmCustomModel(mContext).getDjLsh());
//        requestSoapObject.addProperty("openKey", MyApplication
//                .getmCustomModel(mContext).getOpenKey());
//        requestSoapObject.addProperty("orderJson", paramModel.getParamOrderJson());
//        requestSoapObject.addProperty("itemsJson", paramModel.getParamItemsJson());
//        if (paramModel.getParamPlatformActionType() != 0) {
//            requestSoapObject.addProperty("platformActionID", paramModel.getParamPlatformActionID());
//            requestSoapObject.addProperty("platformActionType", paramModel.getParamPlatformActionType());
//        }
//        requestSoapObject.addProperty("remark", paramModel.getParamRemark());
//        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
//                mContext, this, requestSoapObject, methodName);
//        connectCustomServiceAsyncTask.initProgressDialog(true, "正在提交订单...");
//        connectCustomServiceAsyncTask.execute();
//    }

//    /**
//     * 创建积分商品订单
//     **/
//    private void toCreateJFGoodsOrder() {
//
//        String methodName = InformationCodeUtil.methodNameAddJFOrder;
//        SoapObject requestSoapObject = new SoapObject(
//                ConnectServiceUtil.NAMESPACE, methodName);
//        requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
//        requestSoapObject.addProperty("userid", MyApplication.getmCustomModel(mContext).getDjLsh());
//        requestSoapObject.addProperty("shopid", paramModel.getParamJF_ShopID());
//        requestSoapObject.addProperty("gid", paramModel.getParamJF_GoodsID());
//        requestSoapObject.addProperty("buynum", paramModel.getParamJF_BuyNum());
//        requestSoapObject.addProperty("tcname", paramModel.getParamJF_TCName());
//        requestSoapObject.addProperty("ysname", paramModel.getParamJF_YSName());
//        requestSoapObject.addProperty("isalljf", paramModel.getParamJF_BuyType());
//        requestSoapObject.addProperty("shadds", paramModel.getParamJF_SHAddress());
//        requestSoapObject.addProperty("shuname", paramModel.getParamJF_SHUserName());
//        requestSoapObject.addProperty("shphone", paramModel.getParamJF_SHUserPhone());
//        requestSoapObject.addProperty("fptt", "");
//        requestSoapObject.addProperty("remark", paramModel.getParamJF_SHRemark());
//        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
//                mContext, this, requestSoapObject, methodName);
//        connectCustomServiceAsyncTask.execute();
//    }

    @Override
    public void connectServiceSuccessful(String returnString, String methodName, Integer state, boolean IfPayByZhangQi) {

        LogUtil.LogShitou("创建订单成功", returnString);
        //创建普通商品订单成功
        if (methodName == InformationCodeUtil.methodNameCreateOrder) {
            try {
                JSONObject json = new JSONObject(returnString);
                int sign = json.getInt("Sign");
                //创建订单是否成功
                if (sign == 1) {
                    IfCreateOrderSuccess = true;
                    totalOrderPrices = json.getDouble("Msg");
                    totalOrderIds = json.getString("Tags");
                    if (IfPayByZhangQi) {
                        linearLayout_background.setVisibility(View.INVISIBLE);
                        showPopWindowOfConfirmResult(true, "账期订单提交成功");
                    } else {
                        toPayMethodSelectActivity();
                    }

                } else {
                    showPopWindowOfConfirmResult(false, "订单提交失败," + json.getString("Msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 创建活动订单成功{"DjLsh":378,"Msg":"操作成功!","Sign":1,"Tags":""}
//        if (methodName == InformationCodeUtil.methodNameAddOrderExt) {
//            try {
//                JSONObject json = new JSONObject(returnString);
//                totalOrderIds = json.getString("DjLsh");
//                int sign = json.getInt("Sign");
//                if (sign == 1) {
//                    IfCreateOrderSuccess = true;
//                    toPayMethodSelectActivity();
//                } else {
//                    showPopWindowOfConfirmResult(false, "订单提交失败," + json.getString("Msg"));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

        //{"DjLsh":1280,"Msg":"兑换成功，请耐性等待发货","Sign":1,"Tags":"1280","orderId":1280}
        //{"DjLsh":1281,"Msg":"兑换成功，请付款差额","Sign":1,"Tags":"1281","orderId":1281}
//        if (methodName == InformationCodeUtil.methodNameAddJFOrder) {
//            try {
//                JSONObject json = new JSONObject(returnString);
//                totalOrderIds = json.getString("DjLsh");
//                int sign = json.getInt("Sign");
//                if (sign == 1) {
//                    IfCreateOrderSuccess = true;
//                    toPayMethodSelectActivity();
//                } else {
//                    showPopWindowOfConfirmResult(false, "订单提交失败," + json.getString("Msg"));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }


    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Integer state, boolean whetherRefresh) {
        showPopWindowOfConfirmResult(false, returnStrError + "，订单提交失败");
    }


    @Override
    public void connectServiceCancelled(String returnString, String methodName, Integer state, boolean whetherRefresh) {

    }

    /**
     * 进入到付款界面
     **/
    private void toPayMethodSelectActivity() {
        Intent intent = new Intent(mContext, PayMethodSelectActivity.class);
        intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalMoney, totalOrderPrices);
        intent.putExtra(InformationCodeUtil.IntentPayMethodSelectActivityIfFromBottom, false);
        intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds, totalOrderIds);
        startActivityForResult(intent, RequestCode_CreateOrderMethodSelectActivity);
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
        if (IfCreateOrderSuccess) {
            setResult(RESULT_OK);
            Intent intent = new Intent(mContext, MyOrderActivity.class);
            mContext.startActivity(intent);
            finish();
        } else {
            finish();
            overridePendingTransition(R.anim.not_change, R.anim.out_to_bottom);
        }

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
        previewToDestroy();
        super.onActivityResult(requestCode, resultCode, data);
    }


    private class ListAdapter extends MyBaseAdapter<PayMethodModel> {

        public ListAdapter(Context mContext, List<PayMethodModel> listData) {
            super(mContext, listData);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_adapter_pay_method_select, null);
                viewHolder.iv_payMethod = (ImageView) convertView.findViewById(R.id.iv_payMethod);
                viewHolder.tv_payMethodName = (TextView) convertView.findViewById(R.id.tv_payMethodName);
                viewHolder.tv_payMethodDesc = (TextView) convertView.findViewById(R.id.tv_payMethodDesc);
                viewHolder.cb_ifSelect = (CheckBox) convertView.findViewById(R.id.cb_ifSelect);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            PayMethodModel payMethodModel = listData.get(position);
            viewHolder.iv_payMethod.setImageResource(payMethodModel.getImageResID());
            viewHolder.tv_payMethodName.setText(payMethodModel.getName());
            viewHolder.tv_payMethodDesc.setText(payMethodModel.getDesc());

            viewHolder.cb_ifSelect.setChecked(payMethodModel.getIfSelect());
            if (Desc_PayMethodZhangQi_OFF.equals(payMethodModel.getDesc())) {
                convertView.setAlpha(0.5f);
                viewHolder.cb_ifSelect.setVisibility(View.INVISIBLE);
            } else {
                convertView.setAlpha(1f);
                viewHolder.cb_ifSelect.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView iv_payMethod;
            TextView tv_payMethodName;
            TextView tv_payMethodDesc;
            CheckBox cb_ifSelect;
        }
    }

    private class PayMethodModel {
        private int imageResID;
        private String Name;
        private String Desc;
        private boolean IfSelect = false;

        public int getImageResID() {
            return imageResID;
        }

        public void setImageResID(int imageResID) {
            this.imageResID = imageResID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String desc) {
            Desc = desc;
        }

        public boolean getIfSelect() {
            return IfSelect;
        }

        public void setIfSelect(boolean ifSelect) {
            IfSelect = ifSelect;
        }

        public PayMethodModel(int imageResID, String name, String desc) {
            this.imageResID = imageResID;
            Name = name;
            Desc = desc;
        }

        public PayMethodModel() {
        }
    }

}

