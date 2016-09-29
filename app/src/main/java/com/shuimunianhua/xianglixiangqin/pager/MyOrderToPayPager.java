package com.shuimunianhua.xianglixiangqin.pager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afinalstone.androidstudy.swiperefreshview.OnSwipeRefreshViewListener;
import com.afinalstone.androidstudy.swiperefreshview.SwipeRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.activity.MyOrderActivity;
import com.shuimunianhua.xianglixiangqin.activity.MyOrderDetailActivity;
import com.shuimunianhua.xianglixiangqin.activity.PayMethodSelectActivityEx;
import com.shuimunianhua.xianglixiangqin.adapter.MyBaseAdapter;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultBaseModel;
import com.shuimunianhua.xianglixiangqin.model.OrderViewModel;
import com.shuimunianhua.xianglixiangqin.model.OrderViewProductModel;
import com.shuimunianhua.xianglixiangqin.model.ParamPayMethodSelectModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.ImagerLoaderUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.StringUtil;
import com.shuimunianhua.xianglixiangqin.util.TimeUtils;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;
import com.shuimunianhua.xianglixiangqin.view.FragmentCommonDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/****
 * 我的待支付订单Pager
 *
 * @author SHI
 * 2016年6月24日 16:46:28
 */
public class MyOrderToPayPager extends BasePager<MyOrderActivity> implements OnConnectServerStateListener<Integer>, OnSwipeRefreshViewListener {

    /**
     * 团购中心gridView
     **/
    private SwipeRefreshListView swipeRefreshListView;

    private LinearLayout linearLayout_bottom;
    /**选择全部**/
    private ImageView iv_selectAll;
    private boolean IfSelectAll = false;
    /**支付订单**/
    private Button btn_orderToPay;
    /**
     * listView数据集合
     **/
    List<OrderViewModel> listData = new ArrayList<OrderViewModel>();
    ;
    /**
     * gridView适配器
     **/
    private MyOrderAdapter myOrderAdapter;
    /**
     * 暂无此类订单
     **/
    private View viewListViewIsEmpty;

    private int currentPageIndex = 1;

    private final int RequestCode_PayMethodSelectActivity = 1;

    public MyOrderToPayPager(MyOrderActivity context) {
        super(context);
    }

    @Override
    public View initView() {
        if (view == null) {
            view = View.inflate(mActivity, R.layout.pager_my_order_to_pay, null);
            viewListViewIsEmpty = View.inflate(mActivity,R.layout.item_adapter_my_order_listview_empty,null);
            swipeRefreshListView = (SwipeRefreshListView) view.findViewById(R.id.swipeRefreshListView);
            linearLayout_bottom = (LinearLayout) view.findViewById(R.id.linearLayout_bottom);
            iv_selectAll = (ImageView) view.findViewById(R.id.iv_selectAll);
            btn_orderToPay = (Button) view.findViewById(R.id.btn_orderToPay);
            iv_selectAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(IfSelectAll){
                        IfSelectAll = false;
                        iv_selectAll.setImageResource(R.drawable.icon_select_off_02);
                    }else{
                        iv_selectAll.setImageResource(R.drawable.icon_select_on_02);
                        IfSelectAll = true;
                    }
                    for (int i=0; i<listData.size(); i++){
                        listData.get(i).IfSelect = IfSelectAll;
                        myOrderAdapter.notifyDataSetChanged();
                    }

                }
            });

            btn_orderToPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //是否包含非账期订单
                    boolean IfContainUnCreditOrderItem = false;
                    String totalOrderIds = "";
                    Double totalOrderPrices = 0.00;
                    int totalOrderNum = 0;
                    for (int i = 0; i < listData.size(); i++) {
                        OrderViewModel mOrderViewModel = listData.get(i);
                        if(mOrderViewModel.IfSelect){
                            if(mOrderViewModel.getDelayPayDays() == 0){
                                IfContainUnCreditOrderItem = true;
                            }
                            if (totalOrderNum != 0) {
                                totalOrderIds += ",";
                            }
                            totalOrderNum += 1;
                            totalOrderPrices += mOrderViewModel.getTotalMoney();
                            totalOrderIds += mOrderViewModel.getDjLsh();
                          }
                        }
                        if(StringUtil.isEmpty(totalOrderIds)){
                            ToastUtils.show(mActivity,"请至少选择一个订单");
                            return;
                        }
                         toPayMethodSelectActivity(totalOrderIds, totalOrderPrices, totalOrderNum, IfContainUnCreditOrderItem);
                }

            });

            swipeRefreshListView.getListView().addHeaderView(viewListViewIsEmpty);
            myOrderAdapter = new MyOrderAdapter(mActivity, listData);
            swipeRefreshListView.getListView().setAdapter(myOrderAdapter);
            swipeRefreshListView.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    toMyOrderDetailView(listData.get(position));
                }
            });
            swipeRefreshListView.setOnRefreshListener(this);
        }
        return view;
    }

    @Override
    public void initData() {
        if (!connectSuccessFlag) {
            connectSuccessFlag = true;
            IfSelectAll = false;
            iv_selectAll.setImageResource(R.drawable.icon_select_off_02);
            swipeRefreshListView.openRefreshState();
        }
    }


    private void getData(boolean whthereRefrush) {

        if (whthereRefrush) {
            currentPageIndex = 1;
            linearLayout_bottom.setVisibility(View.GONE);
            listData.clear();
            swipeRefreshListView.getListView().removeHeaderView(viewListViewIsEmpty);
            myOrderAdapter.notifyDataSetChanged();
        } else {
            currentPageIndex++;
        }
        String methodName = InformationCodeUtil.methodNameGetOrders;
        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE,
                methodName);
        requestSoapObject.addProperty("customID", mActivity.mCustomModel.getDjLsh());
        requestSoapObject.addProperty("openKey", mActivity.mCustomModel.getOpenKey());
        requestSoapObject.addProperty("pageSize", 10);
        requestSoapObject.addProperty("pageIndex", currentPageIndex);
        requestSoapObject.addProperty("orderState", classID);//接返回待支付订单信息
        ConnectCustomServiceAsyncTask ConnectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mActivity, this, requestSoapObject, methodName, whthereRefrush);
        ConnectCustomServiceAsyncTask.initProgressDialog(false, "请稍等...");
        ConnectCustomServiceAsyncTask.execute();
    }

    /***
     * 我的订单适配器
     *
     * @author SHI
     *         2016-2-17 15:41:48
     */
    public class MyOrderAdapter extends MyBaseAdapter<OrderViewModel> {

        public MyOrderAdapter(Context mContext, List<OrderViewModel> listData) {
            super(mContext, listData);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolderParent viewHolderParent;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_adapter_my_order_topay_listview_parent, parent, false);
                viewHolderParent = new ViewHolderParent();
                viewHolderParent.tv_orderNum = (TextView) convertView.findViewById(R.id.tv_orderNum);
                viewHolderParent.tv_orderStartTime = (TextView) convertView.findViewById(R.id.tv_orderStartTime);
                viewHolderParent.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
                viewHolderParent.tv_shopName = (TextView) convertView.findViewById(R.id.tv_shopName);
                viewHolderParent.tv_paymentDays = (TextView) convertView.findViewById(R.id.tv_paymentDays);
                viewHolderParent.linearLayout_goodsList = (LinearLayout) convertView.findViewById(R.id.linearLayout_goodsList);
                viewHolderParent.tv_orderPrices = (TextView) convertView.findViewById(R.id.tv_orderPrices);
                viewHolderParent.tv_orderState = (TextView) convertView.findViewById(R.id.tv_orderState);
                viewHolderParent.tv_accountOrderFinishTime = (TextView) convertView.findViewById(R.id.tv_accountOrderFinishTime);

                viewHolderParent.tv_accountOrderFinishTime = (TextView) convertView.findViewById(R.id.tv_accountOrderFinishTime);
                viewHolderParent.tv_orderCancel = (TextView) convertView.findViewById(R.id.tv_orderCancel);
                viewHolderParent.btn_orderToPay = (Button) convertView.findViewById(R.id.btn_orderToPay);
                viewHolderParent.linearLayout_accountOrderState = (LinearLayout) convertView.findViewById(R.id.linearLayout_accountOrderState);
                convertView.setTag(viewHolderParent);
            } else {
                viewHolderParent = (ViewHolderParent) convertView.getTag();
                viewHolderParent.linearLayout_goodsList.removeAllViews();
            }
            final OrderViewModel mOrderViewModel = listData.get(position);

            //初始化订单号和下单时间
            viewHolderParent.tv_orderNum.setText("订单号：" + mOrderViewModel.getOrderNo());
            viewHolderParent.tv_orderStartTime.setText("下单时间：" + mOrderViewModel.getOrderTime());

            if(mOrderViewModel.IfSelect){
                viewHolderParent.iv_select.setImageResource(R.drawable.icon_select_on_02);
            }else{
                viewHolderParent.iv_select.setImageResource(R.drawable.icon_select_off_02);
            }

            viewHolderParent.iv_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOrderViewModel.IfSelect){
                        mOrderViewModel.IfSelect = false;
                        viewHolderParent.iv_select.setImageResource(R.drawable.icon_select_off_02);
                    }else{
                        mOrderViewModel.IfSelect = true;
                        viewHolderParent.iv_select.setImageResource(R.drawable.icon_select_on_02);
                    }
                }
            });
            //初始化店铺名和账期时间
            viewHolderParent.tv_shopName.setText(mOrderViewModel.getProductShopName());
            if(mOrderViewModel.getDelayPayDays() == 0){
                viewHolderParent.tv_paymentDays.setText("无账期");
            }else{
                viewHolderParent.tv_paymentDays.setText(mOrderViewModel.getDelayPayDays()+"天账期");
            }
            List<OrderViewProductModel> listProducts = mOrderViewModel.getProducts();
            for (int i=0; i<listProducts.size(); i++){
                OrderViewProductModel mProduct = listProducts.get(i);
                View view = View.inflate(mContext, R.layout.item_adapter_my_order_topay_listview_child, null);
                ViewHolderChild viewHolderChild = new ViewHolderChild();
                viewHolderChild.iv_goodsImages = (ImageView) view.findViewById(R.id.iv_goodsImages);
                viewHolderChild.tv_goodsName = (TextView) view.findViewById(R.id.tv_goodsName);
                viewHolderChild.tv_goodsPackageAndColor = (TextView) view.findViewById(R.id.tv_goodsPackageAndColor);
                viewHolderChild.tv_goodsNum = (TextView) view.findViewById(R.id.tv_goodsNum);
                viewHolderChild.tv_goodsPrice = (TextView) view.findViewById(R.id.tv_goodsPrice);

                ImagerLoaderUtil.getInstance(mContext).displayMyImage(mProduct.getImgUrl(), viewHolderChild.iv_goodsImages);
                viewHolderChild.tv_goodsName.setText(mProduct.getProductName());
                viewHolderChild.tv_goodsPackageAndColor.setText(mProduct.getPackageName() + "/" + mProduct.getColorName());
                viewHolderChild.tv_goodsNum.setText("×" + mProduct.getBuyCount());
                viewHolderChild.tv_goodsPrice.setText("¥ " + StringUtil.doubleToString(mProduct.getUnitMoney(),"0.00"));

                viewHolderParent.linearLayout_goodsList.addView(view);
            }

            //初始化总金额
            viewHolderParent.tv_orderPrices.setText("￥" + StringUtil.doubleToString(mOrderViewModel.getTotalMoney(), "0.00"));

            viewHolderParent.tv_orderCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showQueryCancelOrderDialog(""+mOrderViewModel.getDjLsh());
                }
            });
            viewHolderParent.tv_orderState.setText("待支付");
            viewHolderParent.btn_orderToPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOrderViewModel.getDelayPayDays() == 0){
                        toPayMethodSelectActivity(""+mOrderViewModel.getDjLsh(),mOrderViewModel.getTotalMoney(), 1, true);
                    }else{
                        toPayMethodSelectActivity(""+mOrderViewModel.getDjLsh(),mOrderViewModel.getTotalMoney(), 1, false);
                    }
                }
            });

            if (mOrderViewModel.getDelayPayDays() == 0) {
                viewHolderParent.tv_accountOrderFinishTime.setVisibility(View.INVISIBLE);
            } else {
                viewHolderParent.tv_accountOrderFinishTime.setVisibility(View.VISIBLE);
            }

            if (mOrderViewModel.getDelayPayDays() != 0) {
                Date date_current = TimeUtils.getTimeDate(mOrderViewModel.getOrderTime());
                Date date_finish = TimeUtils.add(date_current, mOrderViewModel.getDelayPayDays(), TimeUtils.DATE);
                String strTimeFinish = TimeUtils.getTimeString(date_finish);
                viewHolderParent.tv_accountOrderFinishTime.setText("到期时间:" + strTimeFinish);
            }

            return convertView;
        }


        private class ViewHolderParent {
            /**订单编号**/
            TextView tv_orderNum;
            /**订单开始时间**/
            TextView tv_orderStartTime;
            /**是否被选中**/
            ImageView iv_select;
            /**店铺名称**/
            TextView tv_shopName;
            /**账期时间**/
            TextView tv_paymentDays;
            /**商品详情列表**/
            LinearLayout linearLayout_goodsList;
            /**订单合计价格**/
            TextView tv_orderPrices;
            /**订单状态描述**/
            TextView tv_orderState;
            /**账期到期时间**/
            TextView tv_accountOrderFinishTime;
            /**取消订单**/
            TextView tv_orderCancel;
            /**去支付**/
            Button btn_orderToPay;
            /**账期状态描述外围控件**/
            LinearLayout linearLayout_accountOrderState;
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


    /**进入支付方式选择界面**/
    private void toPayMethodSelectActivity(String totalOrderIds,Double totalPrices, int totalOrderNum,boolean IfContainUnCreditOrderItem) {

        Intent mIntent = new Intent(mActivity, PayMethodSelectActivityEx.class);
        int type;
        if(IfContainUnCreditOrderItem){
            type = ParamPayMethodSelectModel.TypeMyOrderCanNotPayCredit;
        }else{
            type = ParamPayMethodSelectModel.TypeMyOrderCanPayCredit;
        }
        ParamPayMethodSelectModel paramSport = new ParamPayMethodSelectModel(
                totalOrderNum, totalPrices, totalOrderIds,type);

        mIntent.putExtra(InformationCodeUtil.IntentPayMethodSelectActivityParamObject, paramSport);
        mActivity.startActivityForResult(mIntent, RequestCode_PayMethodSelectActivity);
        mActivity.overridePendingTransition(R.anim.in_from_bottom,R.anim.not_change);
    }


    //确定取消订单对话框
    protected void showQueryCancelOrderDialog(final String totalOrderIds) {

        final FragmentCommonDialog fragmentCommonDialog =  new FragmentCommonDialog();
        fragmentCommonDialog.initView("提示", "确定取消此订单吗？", "再考虑考虑", "确定",
                new FragmentCommonDialog.OnButtonClickListener() {

                    @Override
                    public void OnOkClick() {
                        changeOrderStateToCancel(totalOrderIds);
                    }

                    @Override
                    public void OnCancelClick() {

                    }
                });
        fragmentCommonDialog.show(mActivity.getSupportFragmentManager(), "fragmentCommonDialog");
    }
    /**取消订单**/
    private void changeOrderStateToCancel(String totalOrderIds) {

        String methodName = InformationCodeUtil.methodNameChangeOrderState;
        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mActivity).getDjLsh());
        requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mActivity).getOpenKey());
        requestSoapObject.addProperty("orderID", totalOrderIds);
        requestSoapObject.addProperty("state", -1);//-1是把订单改变成取消状态
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mActivity, this, requestSoapObject, methodName);
        connectCustomServiceAsyncTask.execute();
    }

    //进入我的订单详情界面
    private void toMyOrderDetailView(OrderViewModel mOrderViewModel) {
        Intent intent = new Intent(mActivity, MyOrderDetailActivity.class);
        intent.putExtra(InformationCodeUtil.IntentMyOrderDetailActivityOrderViewModel,mOrderViewModel);
        mActivity.startActivity(intent);
    }

    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, Integer state, boolean whetherRefresh) {

        LogUtil.LogShitou("订单数据", returnString);
        if(InformationCodeUtil.methodNameGetOrders.equals(methodName)){
            JSONResultBaseModel<OrderViewModel> listResult = null;
            try {
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(returnString);
                listResult = gson.fromJson(jsonObject.getString("Msg"),
                        new TypeToken<JSONResultBaseModel<OrderViewModel>>() {
                        }.getType());
            } catch (Exception e) {
                e.printStackTrace();
                connectSuccessFlag = false;
                if (!whetherRefresh) {
                    currentPageIndex--;
                }
            }
            if (listResult == null || listResult.getList().size() == 0) {
                if (whetherRefresh) {
                    if(swipeRefreshListView.getListView().getHeaderViewsCount() == 0){
                        swipeRefreshListView.getListView().addHeaderView(viewListViewIsEmpty);
                    }
                    myOrderAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.show(mActivity, "暂无更多订单数据");
                }
            }
            if (listResult != null) {
                linearLayout_bottom.setVisibility(View.VISIBLE);
                listData.addAll(listResult.getList());
                myOrderAdapter.notifyDataSetChanged();
            }
            swipeRefreshListView.closeRefreshState();
            return;
        }

        if(InformationCodeUtil.methodNameChangeOrderState.equals(methodName)){
//            {"DjLsh":-1,"Msg":"操作成功","Sign":1,"Tags":""}
            try {
                JSONObject jsonObject = new JSONObject(returnString);
                int sign = jsonObject.getInt("Sign");
                String msg = jsonObject.getString("Msg");
                ToastUtils.show(mActivity,msg);
                if(sign == 1){
                    connectSuccessFlag = false;
                    initData();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void connectServiceFailed(String methodName, Integer state, boolean whetherRefresh) {
        if(InformationCodeUtil.methodNameGetOrders.equals(methodName)){

            connectSuccessFlag = false;
            ToastUtils.show(mActivity, "网络异常，数据加载失败");
            if (!whetherRefresh) {
                currentPageIndex--;
            }
            swipeRefreshListView.closeRefreshState();
        }
        if(InformationCodeUtil.methodNameChangeOrderState.equals(methodName)){
            ToastUtils.show(mActivity, "网络异常，操作失败");
        }

        }

    @Override
    public void connectServiceCancelled(String returnString,
                                        String methodName, Integer state, boolean whetherRefresh) {
        if(InformationCodeUtil.methodNameGetOrders.equals(methodName)){
            connectSuccessFlag = false;
            if (!whetherRefresh) {
                currentPageIndex--;
            }
            swipeRefreshListView.closeRefreshState();
        }
    }

    @Override
    public void onTopRefrushListener() {
        getData(true);
    }

    @Override
    public void onBottomRefrushListener() {
        getData(false);
    }


}







