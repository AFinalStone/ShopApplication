package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultBaseModel;
import com.shi.xianglixiangqin.json.JSONResultMsgModel;
import com.shi.xianglixiangqin.model.AddressModel;
import com.shi.xianglixiangqin.model.CreateOrderMethodSelectModel;
import com.shi.xianglixiangqin.model.ShoppingCartPostModel;
import com.shi.xianglixiangqin.model.ShoppingCartChildGoodsModel;
import com.shi.xianglixiangqin.model.ShoppingCartParentGoodsModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.shi.xianglixiangqin.view.FragmentOkAndCancelDialog;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmOrderGeneralActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer> {

    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_totalOrdersPrices)
    TextView tv_totalOrdersPrices;

    @BindView(R.id.tv_totalPrivilegePrice)
    TextView tv_totalPrivilegePrice;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.listView)
    ListView listView;
    List<MyViewHolderValue> listData = new ArrayList<MyViewHolderValue>();
    MyListViewAdapter myListViewAdapter;
    /**
     * 客户联系方式
     **/
    TextView tv_customerPhone;
    /**
     * 客户姓
     **/
    TextView tv_customerName;
    /**
     * 获取地址
     **/
    TextView tv_customerReceiverAddress;
    /**
     * 客户地址管理栏
     **/
    ImageView iv_managerReceiverAddress;

    /**
     * 当前用户所拥有的飞币数量
     **/
    private int totalFlyCoin_web = 0;

    /**
     * 订单类型择界面
     **/
    private final int RequestCode_CreateOrderMethodSelectActivity = 2;
    /**
     * 地址选择界面
     **/
    private final int RequestCode_ShoppingAddressSelectActivity = 1;

    /**当前要提交的订单总价格**/
    private double totalOrdersPrices = 0.00;
    /**如果是组合商品，则是当前组合商品的组合商品ID**/
    private int currentZHGoodsID = 0;

    @Override
    public void initView() {
        setContentView(R.layout.activity_confirm_order_general);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.confirmOrderGeneralActivity_title);

        // 初始化ListView的Header布局
        View listViewHeader = View.inflate(mContext, R.layout.layout_order_listview_header, null);
        tv_customerPhone = (TextView) listViewHeader.findViewById(R.id.tv_customerPhone);
        tv_customerName = (TextView) listViewHeader.findViewById(R.id.tv_customerName);
        tv_customerReceiverAddress = (TextView) listViewHeader.findViewById(R.id.tv_customerReceiverAddress);
        iv_managerReceiverAddress = (ImageView) listViewHeader.findViewById(R.id.iv_managerReceiverAddress);
        iv_managerReceiverAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, ReceiveAddressSelectActivity.class);
                startActivityForResult(mIntent, RequestCode_ShoppingAddressSelectActivity);
            }
        });
        listView.addHeaderView(listViewHeader);

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        currentZHGoodsID = intent.getIntExtra(InformationCodeUtil.IntentConfirmOrderGeneralActivityZHGoodsID, -1);
        List<ShoppingCartParentGoodsModel> listTempData = (List<ShoppingCartParentGoodsModel>)intent
                .getSerializableExtra(InformationCodeUtil.IntentConfirmOrderGeneralActivityGoodsList);
        for (int i = 0; i < listTempData.size(); i++) {
            MyViewHolderValue value = new MyViewHolderValue(listTempData.get(i));
            listData.add(value);
        }
        myListViewAdapter = new MyListViewAdapter(mContext,listData);
        listView.setAdapter(myListViewAdapter);

        getCustomerFlyCoinNum();
        getCustomerReceiverAddressData();
    }

    /**
     * 初始化用户收货地址数据
     **/
    void initViewHeaderData(AddressModel mAddressModel) {
        tv_customerName.setText("" + mAddressModel.getRealName());
        tv_customerPhone.setText("" + mAddressModel.getPhoneNum());
        tv_customerReceiverAddress.setText("" + mAddressModel.getProvinceName()
                + mAddressModel.getCityName() + mAddressModel.getAreaName()
                + "\n" + mAddressModel.getAddress());
    }


    @OnClick({R.id.iv_titleLeft, R.id.btn_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_titleLeft:
                previewToDestroy();
                break;
            case R.id.btn_submit:
                toCreateOrderMethodSelectActivity();
                break;
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
        //是否包含非账期订单
        boolean IfContainUnCreditOrderItem = false;
        List<ShoppingCartPostModel> listModel = new ArrayList<ShoppingCartPostModel>();
        for (int i = 0; i < listData.size(); i++) {
            MyViewHolderValue value = listData.get(i);
            ShoppingCartParentGoodsModel parentModel = value.getParentGoodsModel();
            if(parentModel.IfSelect){
                if(parentModel.getDelayPayDays() == 0){
                    IfContainUnCreditOrderItem = true;
                }
                StringBuilder sb = new StringBuilder();
                int Index = -1;
                for (int j = 0; j < parentModel.getShoppingCarts().size(); j++) {
                    if (Index != -1) {
                        sb.append(",");
                    }
                    Index = j;
                    sb.append(parentModel.getShoppingCarts().get(j).getId());
                }
                ShoppingCartPostModel model = new ShoppingCartPostModel(
                        parentModel.getShopID(), sb.toString(), value.getIfInvoice(), ""+value.getInvoice(), 2, ""+value.getMark());
                listModel.add(model);
            }
        }

        if(listModel.size() == 0){
            ToastUtil.show(mContext,"请至少选择一个订单");
            return;
        }

        try {
            Gson gson = new Gson();
            int type;
            if(IfContainUnCreditOrderItem){
                if(currentZHGoodsID == -1){
                    type = CreateOrderMethodSelectModel.TypeConfirmOrderGeneralCanNotPayCredit;
                }else{
                    type = CreateOrderMethodSelectModel.TypeConfirmOrderZHCanNotPayCredit;
                }
            }else{
                if(currentZHGoodsID == -1){
                    type = CreateOrderMethodSelectModel.TypeConfirmOrderGeneralCanPayCredit;
                }else{
                    type = CreateOrderMethodSelectModel.TypeConfirmOrderZHCanPayCredit;
                }
            }
            CreateOrderMethodSelectModel paramSport = new CreateOrderMethodSelectModel( listModel.size(), totalOrdersPrices,
                    gson.toJson(listModel), strCustomerName, strCustomerReceiverAddress
                    , strCustomerPhone, currentZHGoodsID, gson.toJson(listData.get(0).getParentGoodsModel().getShoppingCarts()), type);
            Intent mIntent = new Intent(mContext, CreateOrderMethodSelectActivity.class);
            mIntent.putExtra(InformationCodeUtil.IntentPayMethodSelectActivityParamObject, paramSport);
            startActivityForResult(mIntent, RequestCode_CreateOrderMethodSelectActivity);
            overridePendingTransition(R.anim.in_from_bottom,R.anim.not_change);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户默认收获地址
     **/
    public void getCustomerReceiverAddressData() {

        String methodName = InformationCodeUtil.methodNameGetAddrList;
        SoapObject requestSoapObject = new SoapObject(
                ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("customID", MyApplication
                .getmCustomModel(mContext).getDjLsh());
        requestSoapObject.addProperty("openKey", MyApplication
                .getmCustomModel(mContext).getOpenKey());
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, requestSoapObject, methodName);
        connectCustomServiceAsyncTask.execute();

    }

    /**
     * 获取用户的飞币数量
     **/
    private void getCustomerFlyCoinNum() {

        String methodName = InformationCodeUtil.methodNameGetAcceptFlyCoin;
        SoapObject requestSoapObject = new SoapObject(
                ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("customID", MyApplication
                .getmCustomModel(mContext).getDjLsh());
        ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
                mContext, this, requestSoapObject, methodName);
        connectGoodsServiceAsyncTask.initProgressDialog(false);
        connectGoodsServiceAsyncTask.execute();
    }




    //添加收货地址
    void showAddReceiverAddressDialog() {

        final FragmentOkAndCancelDialog fragmentDailog = new FragmentOkAndCancelDialog();
        fragmentDailog.initView("提示", "没有收货地址，请添加收货地址", "取消", "添加",
                new FragmentOkAndCancelDialog.OnButtonClickListener() {

                    @Override
                    public void OnOkClick() {
                        Intent mIntent = new Intent(mContext,
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
     * 获取当前商品总价格
     */
    private void getCurrentOrderTotalPrice() {
        //首先获取可抵用的最大飞币数量
        int totalPrivilegeFlyCoin = getCurrentPrivilegeTotalFlyCoin();
        //计算商品总价格和优惠金额
        Double orderPrices = 0.00;
        Double orderPrivilegePrices = 0.00;
        for (int i = 0; i < listData.size(); i++) {
            MyViewHolderValue myValueModel = listData.get(i);
            ShoppingCartParentGoodsModel mParentModel = myValueModel.getParentGoodsModel();
            if (mParentModel.IfSelect) {
                Double singleOrderPrices = 0.00;
                Double singleOrderPrivilegePrices = 0.00;
                List<ShoppingCartChildGoodsModel> listChildModel = mParentModel.getShoppingCarts();

                if(myValueModel.getIfInvoice()){
                    for (int j = 0; j < listChildModel.size(); j++) {
                        ShoppingCartChildGoodsModel mChildGoodsModel = listChildModel.get(j);
                        singleOrderPrices += mChildGoodsModel.getUnitTaxPrice() * mChildGoodsModel.getQuantity();
                        singleOrderPrivilegePrices += mChildGoodsModel.getYhje() * mChildGoodsModel.getQuantity();
                    }
                }else{
                    for (int j = 0; j < listChildModel.size(); j++) {
                        ShoppingCartChildGoodsModel mChildGoodsModel = listChildModel.get(j);
                        singleOrderPrices += mChildGoodsModel.getUnitPrice() * mChildGoodsModel.getQuantity();
                        singleOrderPrivilegePrices += mChildGoodsModel.getYhje() * mChildGoodsModel.getQuantity();
                    }
                }
                orderPrices += singleOrderPrices;
                orderPrivilegePrices += singleOrderPrivilegePrices;
            }
        }
        totalOrdersPrices = orderPrices - totalPrivilegeFlyCoin - orderPrivilegePrices;
        tv_totalOrdersPrices.setText(StringUtil.doubleToString(totalOrdersPrices,"0.00"));
        if(orderPrivilegePrices == 0){
            tv_totalPrivilegePrice.setText("");
        }else{
            tv_totalPrivilegePrice.setText("(已优惠"+StringUtil.doubleToString(orderPrivilegePrices,"0.00")+")");
        }
        myListViewAdapter.notifyDataSetChanged();
    }


    /** 首先获取可抵用的最大飞币数量 */
    private int getCurrentPrivilegeTotalFlyCoin() {

        int totalPrivilegeFlyCoin = 0;

        for(int i=0; i<listData.size(); i++){
            MyViewHolderValue myValueModel = listData.get(i);
            ShoppingCartParentGoodsModel mParentModel = myValueModel.getParentGoodsModel();
            if(mParentModel.IfSelect){
                int singleFlyCoin = 0;
                List<ShoppingCartChildGoodsModel>  listChildModel = mParentModel.getShoppingCarts();
                if(myValueModel.getIfInvoice()){
                    for (int j=0; j<listChildModel.size(); j++){
                        singleFlyCoin += listChildModel.get(j).getTaxFlyCoin();
                    }
                }else{
                    for (int j=0; j<listChildModel.size(); j++){
                        singleFlyCoin += listChildModel.get(j).getFlyCoin();
                    }
                }
                totalPrivilegeFlyCoin += singleFlyCoin;
            }
        }

        if(totalPrivilegeFlyCoin > totalFlyCoin_web){
            totalPrivilegeFlyCoin = totalFlyCoin_web;
        }
        return totalPrivilegeFlyCoin;
    }

    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, Integer state, boolean whetherRefresh) {

        LogUtil.LogShitou(returnString);

        if (methodName == InformationCodeUtil.methodNameGetAcceptFlyCoin) {
            try {
                Gson gson = new Gson();
                JSONResultMsgModel result = gson.fromJson(returnString, JSONResultMsgModel.class);
                totalFlyCoin_web = Integer.parseInt(result.getMsg());
            } catch (Exception e) {
                e.printStackTrace();
            }
            getCurrentOrderTotalPrice();
        }

        // 获取 我的收货地址 成功
        if (methodName == InformationCodeUtil.methodNameGetAddrList) {
            List<AddressModel> returnList = null;
            try {
                Gson gson = new Gson();
                JSONResultMsgModel resultMsg = gson.fromJson(returnString, JSONResultMsgModel.class);
                JSONResultBaseModel<AddressModel> mJSONAddrModel = gson.fromJson(
                        resultMsg.getMsg(), new TypeToken<JSONResultBaseModel<AddressModel>>() {
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
            return;
        }

    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Integer state,
                                     boolean whetherRefresh) {
        if (methodName == InformationCodeUtil.methodNameGetAddrList) {
            showAddReceiverAddressDialog();
        } else if (methodName == InformationCodeUtil.methodNameGetAcceptFlyCoin) {
            ToastUtil.show(mContext, "获取抵扣飞币数量失败，请检查网络");
        }
    }

    @Override
    public void connectServiceCancelled(String returnString, String methodName, Integer state, boolean whetherRefresh) {

    }

    /**
     * 即将关闭当前页面时启动关闭动画
     **/
    void previewToDestroy() {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                //用户成功生成了订单
                if (requestCode == RequestCode_CreateOrderMethodSelectActivity) {
                    IfOpenFinishActivityAnim(false);
                    Intent intent = new Intent(mContext, MyOrderActivity.class);
                    mContext.startActivity(intent);
                    finish();
                }
                //用户选择了新的收货地址
                if (requestCode == RequestCode_ShoppingAddressSelectActivity) {
                    AddressModel mAddressModel = (AddressModel) data
                            .getSerializableExtra("selectAddressModel");
                    initViewHeaderData(mAddressModel);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 如果是返回键，则关闭当前页面
     **/
    @Override
    public void onBackPressed() {
        previewToDestroy();
    }


    public class MyViewHolderValue {

        private ShoppingCartParentGoodsModel parentGoodsModel;
        /**
         * 配送方式
         **/
        private String dispathMethod = "";

        /**
         * 发票
         **/
        private String invoice = "";

        /**
         * 是否开发票
         **/
        private boolean IfInvoice = false;

        /**
         * 备注
         **/
        private String mark = "";

        public MyViewHolderValue(ShoppingCartParentGoodsModel parentGoodsModel) {
            this.parentGoodsModel = parentGoodsModel;
        }

        public ShoppingCartParentGoodsModel getParentGoodsModel() {
            return parentGoodsModel;
        }

        public void setParentGoodsModel(ShoppingCartParentGoodsModel parentGoodsModel) {
            this.parentGoodsModel = parentGoodsModel;
        }

        public String getDispathMethod() {
            return dispathMethod;
        }

        public void setDispathMethod(String dispathMethod) {
            this.dispathMethod = dispathMethod;
        }

        public String getInvoice() {
            return invoice;
        }

        public void setInvoice(String invoice) {
            this.invoice = invoice;
        }

        public boolean getIfInvoice() {
            return IfInvoice;
        }

        public void setIfInvoice(boolean ifInvoice) {
            IfInvoice = ifInvoice;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

    }


    private class MyListViewAdapter extends MyBaseAdapter<MyViewHolderValue> {

        private Integer index_orderRemark = -1;
        private Integer index_InvoiceNumberCode = -1;

        public MyListViewAdapter(Context mContext, List<MyViewHolderValue> listData) {
            super(mContext, listData);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolderParent viewHolderParent;
            if (convertView==null){
                convertView=View.inflate(mContext,R.layout.item_adapter_confirm_order_parent,null);

                viewHolderParent = new ViewHolderParent();
                viewHolderParent.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);

                viewHolderParent.tv_shopName = (TextView) convertView.findViewById(R.id.tv_shopName);
                viewHolderParent.tv_paymentDays = (TextView) convertView.findViewById(R.id.tv_paymentDays);
                viewHolderParent.linearLayout_goodsList = (LinearLayout) convertView.findViewById(R.id.linearLayout_goodsList);
                viewHolderParent.et_orderRemark = (EditText) convertView.findViewById(R.id.et_orderRemark);
                viewHolderParent.tv_dispatchByExpress = (TextView) convertView.findViewById(R.id.tv_dispatchByExpress);
                viewHolderParent.cb_confirmOrderIfOpenBilling = (CheckBox) convertView.findViewById(R.id.cb_confirmOrderIfOpenBilling);
                viewHolderParent.et_InvoiceNumberCode = (EditText) convertView.findViewById(R.id.et_InvoiceNumberCode);
                viewHolderParent.tv_privilegeFlyCoin = (TextView) convertView.findViewById(R.id.tv_privilegeFlyCoin);
                viewHolderParent.tv_privilegeFlyCoinDesc = (TextView) convertView.findViewById(R.id.tv_privilegeFlyCoinDesc);

                viewHolderParent.et_orderRemark.setTag(position);
                viewHolderParent.et_InvoiceNumberCode.setTag(position);
                viewHolderParent.et_orderRemark.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            index_orderRemark = (Integer) v.getTag();
                        }
                        return false;
                    }
                });
                viewHolderParent.et_InvoiceNumberCode.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            index_InvoiceNumberCode = (Integer) v.getTag();
                        }
                        return false;
                    }
                });

                viewHolderParent.et_orderRemark.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s != null && !"".equals(s.toString())) {
                            int position = (Integer) viewHolderParent.et_orderRemark.getTag();
                            listData.get(position).setMark(s.toString());
                        }
                    }
                });

                viewHolderParent.et_InvoiceNumberCode.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s != null && !"".equals(s.toString())) {
                            int position = (Integer) viewHolderParent.et_InvoiceNumberCode.getTag();
                            listData.get(position).setInvoice(s.toString());
                        }
                    }
                });

                convertView.setTag(viewHolderParent);
            } else{
                viewHolderParent = (ViewHolderParent) convertView.getTag();
                viewHolderParent.et_orderRemark.setTag(position);
                viewHolderParent.et_InvoiceNumberCode.setTag(position);
                viewHolderParent.linearLayout_goodsList.removeAllViews();
            }
            //店铺信息
            final MyViewHolderValue myViewHolderValue = listData.get(position);
            final ShoppingCartParentGoodsModel mParentModel = myViewHolderValue.getParentGoodsModel();
            getCurrentOrderTotalPrice();
            //订单是否被选中
            if (mParentModel.IfSelect) {
                viewHolderParent.iv_select.setImageResource(R.drawable.icon_select_on_02);
            } else {
                viewHolderParent.iv_select.setImageResource(R.drawable.icon_select_off_02);
            }
            viewHolderParent.iv_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mParentModel.IfSelect){
                        viewHolderParent.iv_select.setImageResource(R.drawable.icon_select_off_02);
                        mParentModel.IfSelect = false;
                    }else{
                        viewHolderParent.iv_select.setImageResource(R.drawable.icon_select_on_02);
                        mParentModel.IfSelect = true;
                    }
                    getCurrentOrderTotalPrice();
                }
            });
            viewHolderParent.tv_shopName.setText(mParentModel.getShopName());
            int delayDay = mParentModel.getDelayPayDays();
            if (delayDay == 0) {
                viewHolderParent.tv_paymentDays.setText("无账期");
            } else {
                viewHolderParent.tv_paymentDays.setText(delayDay + "天账期");
            }

            final List<ShoppingCartChildGoodsModel> listChilds = mParentModel.getShoppingCarts();
            for (int i = 0; i < listChilds.size(); i++) {

                ShoppingCartChildGoodsModel childModelData = listChilds.get(i);

                View view = View.inflate(mContext, R.layout.item_adapter_confirm_order_child, null);
                ViewHolderChild viewHolderChild = new ViewHolderChild();
                viewHolderChild.iv_goodsImages = (ImageView) view.findViewById(R.id.iv_goodsImages);
                viewHolderChild.tv_goodsName = (TextView) view.findViewById(R.id.tv_goodsName);
                viewHolderChild.tv_goodsPackageAndColor = (TextView) view.findViewById(R.id.tv_goodsPackageAndColor);
                viewHolderChild.tv_goodsNum = (TextView) view.findViewById(R.id.tv_goodsNum);
                viewHolderChild.tv_goodsPrice = (TextView) view.findViewById(R.id.tv_goodsPrice);

                ImagerLoaderUtil.getInstance(mContext).displayMyImage(childModelData.getImgUrl(), viewHolderChild.iv_goodsImages);
                viewHolderChild.tv_goodsName.setText(childModelData.getProductName());
                viewHolderChild.tv_goodsPackageAndColor.setText(childModelData.getPackageName() + "/" + childModelData.getColorName());
                viewHolderChild.tv_goodsNum.setText("×" + childModelData.getQuantity());
                if(myViewHolderValue.getIfInvoice()){
                    viewHolderChild.tv_goodsPrice.setText("¥ " + StringUtil.doubleToString(childModelData.getUnitTaxPrice(),"0.00"));
                }else{
                    viewHolderChild.tv_goodsPrice.setText("¥ " + StringUtil.doubleToString(childModelData.getUnitPrice(),"0.00"));
                }

                viewHolderParent.linearLayout_goodsList.addView(view);
            }
            //备注
            if (StringUtil.isEmpty(myViewHolderValue.getMark())) {
                viewHolderParent.et_orderRemark.setText("");
            } else {
                viewHolderParent.et_orderRemark.setText(myViewHolderValue.getMark());
            }
            //配送方式
            myViewHolderValue.setDispathMethod("普通快递");
            viewHolderParent.tv_dispatchByExpress.setText("普通快递");
            //发票抬头
            if (StringUtil.isEmpty(myViewHolderValue.getInvoice())) {
                viewHolderParent.et_InvoiceNumberCode.setText("");
            } else {
                viewHolderParent.et_InvoiceNumberCode.setText(myViewHolderValue.getInvoice());
            }

            //是否开具发票
            viewHolderParent.cb_confirmOrderIfOpenBilling.setChecked(myViewHolderValue.getIfInvoice());
            viewHolderParent.et_InvoiceNumberCode.setEnabled(myViewHolderValue.getIfInvoice());

            viewHolderParent.cb_confirmOrderIfOpenBilling.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    myViewHolderValue.setIfInvoice(isChecked);
                    myListViewAdapter.notifyDataSetChanged();
                }
            });
            //设置飞币数量
            if(myViewHolderValue.getIfInvoice()){
                int totalFlyCoinTax = 0;
                for (int i=0; i<listChilds.size();i++){
                    totalFlyCoinTax += listChilds.get(i).getTaxFlyCoin()*listChilds.get(i).getQuantity();
                }
                viewHolderParent.tv_privilegeFlyCoinDesc.setText(
                        "(该订单最高可抵"+totalFlyCoinTax+"飞币，您有"+totalFlyCoin_web+"飞币)");
                if(totalFlyCoinTax > totalFlyCoin_web ){
                    totalFlyCoinTax = totalFlyCoin_web;
                }
                viewHolderParent.tv_privilegeFlyCoin.setText(""+totalFlyCoinTax);
            }else{
                int totalFlyCoin = 0;
                for (int i=0; i<listChilds.size();i++){
                    totalFlyCoin += listChilds.get(i).getFlyCoin()*listChilds.get(i).getQuantity();
                }
                viewHolderParent.tv_privilegeFlyCoinDesc.setText(
                        "(该订单最高可抵"+totalFlyCoin+"飞币，您有"+totalFlyCoin_web+"飞币)");
                if(totalFlyCoin > totalFlyCoin_web ){
                    totalFlyCoin = totalFlyCoin_web;
                }
                viewHolderParent.tv_privilegeFlyCoin.setText(""+totalFlyCoin);

            }
            viewHolderParent.et_orderRemark.clearFocus();
            viewHolderParent.et_InvoiceNumberCode.clearFocus();
            if (index_orderRemark != -1 && index_orderRemark == position)  {
                viewHolderParent.et_orderRemark.requestFocus();
            }
            if (index_InvoiceNumberCode != -1 && index_InvoiceNumberCode == position) {
                viewHolderParent.et_InvoiceNumberCode.requestFocus();
            }
            return convertView;
        }

        private class ViewHolderParent {
            /**
             * 商品图片
             */
            private ImageView iv_select;
            /**
             * 商品名称
             */
            private TextView tv_shopName;
            /**
             * 账期时间
             **/
            private TextView tv_paymentDays;

            /**
             * 店铺商品列表
             **/
            private LinearLayout linearLayout_goodsList;

            /**
             * 订单备注
             **/
            private EditText et_orderRemark;

            /**
             * 订单备注
             **/
            private TextView tv_dispatchByExpress;

            /**
             * 是否开具发票
             **/
            private CheckBox cb_confirmOrderIfOpenBilling;

            /**
             * 发票抬头
             **/
            private EditText et_InvoiceNumberCode;

            /**
             * 飞币数量
             **/
            private TextView tv_privilegeFlyCoin;
            /**
             * 飞币数量备注
             **/
            private TextView tv_privilegeFlyCoinDesc;
        }

        private class ViewHolderChild {
            /**
             * 商品图片
             */
            private ImageView iv_goodsImages;
            /**
             * 商品名称
             **/
            private TextView tv_goodsName;
            /**
             * 套餐/颜色
             **/
            private TextView tv_goodsPackageAndColor;
            /**
             * 商品价格
             **/
            private TextView tv_goodsPrice;
            /**
             * 商品数量
             **/
            private TextView tv_goodsNum;
        }
    }


}




