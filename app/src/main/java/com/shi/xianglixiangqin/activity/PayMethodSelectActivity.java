package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.view.FragmentOkAndCancelDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayMethodSelectActivity extends MyBaseTranslucentActivity {

    @BindView(R.id.listView)
    ListView listView;
    ListAdapter listAdapter;
    List<PayMethodModel> listData = new ArrayList<>();

    /**
     * 确认支付
     **/
    @BindView(R.id.btn_queryToPay)
    Button btn_queryToPay;

//    /**
//     * 背景
//     **/
//    @BindView(R.id.linearLayout_background)
//    LinearLayout linearLayout_background;
    /**
     * 当前订单编号集合字段
     **/
    private String totalOrderIds;
    /**
     * 订单总金额
     **/
    private Double totalOrderPrices;
    /**是否从底部弹出**/
    private boolean IfFromBottom;
    /**
     * 当前界面根View
     **/
    private View rootView;
    /**
     * 请求码
     **/
    private final int RequestCode_PayMethodSelectActivity = 1;

    private String currentSelectMethodName;

    private final String Name_PayMethodYiZhiFu = "翼支付";
    private final String Name_PayMethodWChat = "微信扫码支付(信用卡)";
    private final String Name_PayMethodZFBao = "支付宝扫码支付(信用卡)";
    private final String Name_PayMethodXianXia = "线下支付";

    private final String Desc_PayMethodYiZhiFu = "中国电信旗下运营支付业务品牌";
    private final String Desc_PayMethodWChat = "生成二维码，通过微信扫码支付";
    private final String Desc_PayMethodZFBao = "生成二维码，通过支付宝扫码支付";
    private final String Desc_PayMethodXianXia = "联系卖家，线下付款，然后卖家发货";

    private int selectPosition;

    @Override
    public void initView() {
        Intent intent = getIntent();
        totalOrderIds = intent.getStringExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds);
        totalOrderPrices = intent.getDoubleExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalMoney, 0.00);
        IfFromBottom = intent.getBooleanExtra(InformationCodeUtil.IntentPayMethodSelectActivityIfFromBottom,true);
        if(IfFromBottom){
            rootView = View.inflate(mContext, R.layout.activity_pay_method_select_from_bottom, null);
        }else{
            rootView = View.inflate(mContext, R.layout.activity_pay_method_select, null);
        }
        setContentView(rootView);
        ButterKnife.bind(this);

    }

    @Override
    public void initData() {

//      listData.add(new PayMethodModel(R.drawable.icon_paymethod_yzfu,Name_PayMethodYiZhiFu,Desc_PayMethodYiZhiFu));
//      listData.add(new PayMethodModel(R.drawable.icon_pay_method_wchat,Name_PayMethodWChat,Desc_PayMethodWChat));
        listData.add(new PayMethodModel(R.drawable.icon_pay_method_zfbao, Name_PayMethodZFBao, Desc_PayMethodZFBao));
        listData.add(new PayMethodModel(R.drawable.icon_pay_method_xianxia, Name_PayMethodXianXia, Desc_PayMethodXianXia));

        listAdapter = new ListAdapter(mContext, listData);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < listData.size(); i++) {
                    listData.get(i).setIfSelect(false);
                }
                selectPosition = position;
                btn_queryToPay.setEnabled(true);
                listData.get(position).setIfSelect(true);
                listAdapter.notifyDataSetChanged();

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
                PayMethodModel payMethod = listData.get(selectPosition);
                currentSelectMethodName = payMethod.getName();
                if (Name_PayMethodYiZhiFu.equals(currentSelectMethodName)) {
                    toPayByYiZhiFu();
                } else if (Name_PayMethodZFBao.equals(currentSelectMethodName)) {
                    toPayByQRCode(0);
                } else if (Name_PayMethodWChat.equals(currentSelectMethodName)) {
                    toPayByQRCode(1);
                } else if (Name_PayMethodXianXia.equals(currentSelectMethodName)) {
                    showPayOrderByXianXia();
                }
                break;
        }
    }


    /**
     * 通过翼支付进行支付
     **/
    private void toPayByYiZhiFu() {
        Intent intent = new Intent(mContext, PayBankCardSelectActivity.class);
        intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalMoney, totalOrderPrices);
        intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds, totalOrderIds);
        startActivityForResult(intent, RequestCode_PayMethodSelectActivity);
    }

    /**
     * 通过二维码进行支付    flag(0代表支付宝，1代表微信)
     **/
    private void toPayByQRCode(int flag) {
        Intent intent = new Intent(mContext, PayMoneyByQRCodeActivity.class);
        intent.putExtra(InformationCodeUtil.IntentPayMoneyByQDCodeActivityFlag, flag);
        intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds, totalOrderIds);
        startActivityForResult(intent, RequestCode_PayMethodSelectActivity);
    }

   /**线下支付提醒对话框**/
    private void showPayOrderByXianXia() {
        final FragmentOkAndCancelDialog fragmentDialog = new FragmentOkAndCancelDialog();
        fragmentDialog.initView("温馨提示", "请联系卖家进行线下付款"+System.getProperty("line.separator")+"付款成功后，卖家将会自动发货", "取消", "确定"
                , new FragmentOkAndCancelDialog.OnButtonClickListener() {
                    @Override
                    public void OnOkClick() {
                        previewToDestroy();
                    }

                    @Override
                    public void OnCancelClick() {
                        previewToDestroy();
                    }
                });
        fragmentDialog.show(getSupportFragmentManager(), "fragmentDialog");
    }

    /**
     * 即将关闭当前页面时启动关闭动画
     **/
    void previewToDestroy() {
        finish();
        if(IfFromBottom)
        overridePendingTransition(R.anim.not_change, R.anim.out_to_bottom);
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
        if (RESULT_OK == resultCode) {
            setResult(RESULT_OK);
            previewToDestroy();
        }
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
            viewHolder.cb_ifSelect.setVisibility(View.VISIBLE);
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

//    /**
//     * 修改账期订单为待发货
//     **/
//    private void changeOrderStateToWaitToSend(String totalOrderId) {

//        String methodName = InformationCodeUtil.methodNameChangeOrderStateExt;
//        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE,
//                methodName);
//        requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
//        requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
//        requestSoapObject.addProperty("orderIds", totalOrderId);//接返回待支付订单信息
//        ConnectCustomServiceAsyncTask ConnectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
//                mContext, this, requestSoapObject, methodName);
//        ConnectCustomServiceAsyncTask.initProgressDialog(true, "账期支付中...");
//        ConnectCustomServiceAsyncTask.execute();
//
//
//    }
//
//
//    @Override
//    public void connectServiceSuccessful(String returnString, String methodName, Integer state, boolean IfPayByZhangQi) {
//
//        LogUtil.LogShitou("创建订单成功", returnString);
//        //创建普通商品订单成功
//        if (methodName == InformationCodeUtil.methodNameCreateOrder) {
//            try {
//                JSONObject json = new JSONObject(returnString);
//                int sign = json.getInt("Sign");
//                //创建订单是否成功
//                if (sign == 1) {
//                    IfPayMoneySuccess = true;
//                    totalOrderPrices = json.getDouble("Msg");
//                    totalOrderIds = json.getString("Tags");
//                    if (IfPayByZhangQi) {
//                        linearLayout_background.setVisibility(View.INVISIBLE);
//                        showPopWindowOfConfirmResult(true, "账期订单提交成功");
//                    } else {
//                    }
//
//                } else {
//                    showPopWindowOfConfirmResult(false, "订单提交失败," + json.getString("Msg"));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        // 创建活动订单成功{"DjLsh":378,"Msg":"操作成功!","Sign":1,"Tags":""}
//        if (methodName == InformationCodeUtil.methodNameAddOrderExt) {
//            JSONObject json = null;
//            try {
//                json = new JSONObject(returnString);
//                totalOrderIds = json.getString("Tags");
//                int sign = json.getInt("Sign");
//                if (sign == 1) {
//                    IfPayMoneySuccess = true;
//                    toPayMoneyView();
//                } else {
//                    showPopWindowOfConfirmResult(false, "订单提交失败," + json.getString("Msg"));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//
//    }
//
//
//    @Override
//    public void connectServiceFailed(String returnStrError, String methodName, Integer state, boolean whetherRefresh) {
//        if (methodName == InformationCodeUtil.methodNameCreateOrder
//                || methodName == InformationCodeUtil.methodNameAddOrderExt) {
//            showPopWindowOfConfirmResult(false, "网络异常，订单提交失败");
//            return;
//        }
//
//        if (methodName == InformationCodeUtil.methodNameChangeOrderStateExt) {
//            showPopWindowOfConfirmResult(false, "网络异常，账期支付失败");
//        }
//    }
//
//
//    @Override
//    public void connectServiceCancelled(String returnString, String methodName, Integer state, boolean whetherRefresh) {
//
//    }

//    /**
//     * 进入到付款界面
//     **/
//    private void toPayMoneyView() {
//
//        if (Name_PayMethodYiZhiFu.equals(currentSelectMethodName)) {
//            //翼支付
//            Intent intent = new Intent(mContext, PayBankCardSelectActivity.class);
//            intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalMoney, totalOrderPrices);
//            intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds, totalOrderIds);
//            startActivityForResult(intent, RequestCode_PayMethodSelectActivity);
//        } else if (Name_PayMethodZFBao.equals(currentSelectMethodName)) {
//            //支付宝
//            Intent intent = new Intent(mContext, PayMoneyByQRCodeActivity.class);
//            intent.putExtra(InformationCodeUtil.IntentPayMoneyByQDCodeActivityFlag, 0);
//            intent.putExtra(InformationCodeUtil.IntentPayBankCardSelectActivityCurrentTotalMoney, totalOrderPrices);
//            intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds, totalOrderIds);
//            startActivityForResult(intent, RequestCode_PayMethodSelectActivity);
//        } else if (Name_PayMethodWChat.equals(currentSelectMethodName)) {
//            //微信支付
//            Intent intent = new Intent(mContext, PayMoneyByQRCodeActivity.class);
//            intent.putExtra(InformationCodeUtil.IntentPayMoneyByQDCodeActivityFlag, 1);
//            intent.putExtra(InformationCodeUtil.IntentPayBankCardSelectActivityCurrentTotalMoney, totalOrderPrices);
//            intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds, totalOrderIds);
//            startActivityForResult(intent, RequestCode_PayMethodSelectActivity);
//        } else if (Name_PayMethodXianXia.equals(currentSelectMethodName)) {
//            //线下支付
//            showPayOrderByXianXia(true);
//        }
//
//    }
//    //账期支付进行账期支付
//    void showPayOrderByOnlineDialog(final String orderIds) {
//
//        final FragmentOkAndCancelDialog fragmentDailog = new FragmentOkAndCancelDialog();
//        fragmentDailog.initView("温馨提示", "已成功生成订单，确定进行账期支付吗？", "取消", "确定支付",
//                new FragmentOkAndCancelDialog.OnButtonClickListener() {
//
//                    @Override
//                    public void OnOkClick() {
//                        changeOrderStateToWaitToSend(orderIds);
//                    }
//
//                    @Override
//                    public void OnCancelClick() {
//                        Intent intent = new Intent(mContext, MyOrderActivity.class);
//                        mContext.startActivity(intent);
//                        previewToDestroy();
//                    }
//                });
//
//        fragmentDailog.show(getSupportFragmentManager(), "fragmentDialog");
//    }

}

