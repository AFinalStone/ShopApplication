package com.shi.xianglixiangqin.pager;

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
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.activity.MyOrderActivity;
import com.shi.xianglixiangqin.activity.MyOrderDetailActivity;
import com.shi.xianglixiangqin.activity.PayMethodSelectActivity;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultBaseModel;
import com.shi.xianglixiangqin.model.OrderViewModel;
import com.shi.xianglixiangqin.model.OrderViewProductModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.SnackBarUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.TimeUtil;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.shi.xianglixiangqin.view.FragmentOkAndCancelDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/****
 * 我的订单Pager
 *
 * @author SHI
 *         2016年6月24日 16:46:28
 */
public class MyOrderGeneralPager extends BasePager<MyOrderActivity> implements OnConnectServerStateListener<Integer>, OnSwipeRefreshViewListener {

    /**
     * 团购中心gridView
     **/
    public SwipeRefreshListView swipeRefreshListView;
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

    private final int RequestCode_MyOrderGeneralPager = 1;

    public MyOrderGeneralPager(MyOrderActivity context) {
        super(context);
    }

    @Override
    public View initView() {
        if (view == null) {
            view = View.inflate(mActivity, R.layout.pager_my_order_general, null);
            viewListViewIsEmpty = View.inflate(mActivity,R.layout.item_adapter_my_order_listview_empty,null);
            swipeRefreshListView = (SwipeRefreshListView) view.findViewById(R.id.swipeRefreshListView);
            swipeRefreshListView.getListView().addHeaderView(viewListViewIsEmpty);
            myOrderAdapter = new MyOrderAdapter(mActivity, listData);
            swipeRefreshListView.getListView().setAdapter(myOrderAdapter);
            swipeRefreshListView.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if(listData.size() != 0)
                    toMyOrderDetailView(listData.get(position));
                }
            });
            swipeRefreshListView.setOnRefreshListener(this);
            swipeRefreshListView.IfOpenBottomRefresh(true);

            if (classID == 0) {
                initData();
            }
        }
        return view;
    }

    @Override
    public void initData() {
        if (!connectSuccessFlag) {
            connectSuccessFlag = true;
            swipeRefreshListView.openRefreshState();
        }
    }


    private void getData(boolean whthereRefrush) {

        if (whthereRefrush) {
            currentPageIndex = 1;
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

            ViewHolderParent viewHolderParent;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_adapter_my_order_general_listview_parent, parent, false);
                viewHolderParent = new ViewHolderParent();
                viewHolderParent.tv_orderNum = (TextView) convertView.findViewById(R.id.tv_orderNum);
                viewHolderParent.tv_orderStartTime = (TextView) convertView.findViewById(R.id.tv_orderStartTime);
                viewHolderParent.tv_shopName = (TextView) convertView.findViewById(R.id.tv_shopName);
                viewHolderParent.tv_paymentDays = (TextView) convertView.findViewById(R.id.tv_paymentDays);
                viewHolderParent.linearLayout_goodsList = (LinearLayout) convertView.findViewById(R.id.linearLayout_goodsList);
                viewHolderParent.tv_orderPrices = (TextView) convertView.findViewById(R.id.tv_orderPrices);
                viewHolderParent.tv_orderState = (TextView) convertView.findViewById(R.id.tv_orderState);
                viewHolderParent.tv_accountOrderFinishTime = (TextView) convertView.findViewById(R.id.tv_accountOrderFinishTime);

                viewHolderParent.tv_accountOrderFinishTime = (TextView) convertView.findViewById(R.id.tv_accountOrderFinishTime);
                viewHolderParent.tv_orderCancel = (TextView) convertView.findViewById(R.id.tv_orderCancel);
                viewHolderParent.btn_orderOperationAction = (Button) convertView.findViewById(R.id.btn_orderOperationAction);
                viewHolderParent.linearLayout_orderBottom = (LinearLayout) convertView.findViewById(R.id.linearLayout_orderBottom);
                convertView.setTag(viewHolderParent);
            } else {
                viewHolderParent = (ViewHolderParent) convertView.getTag();
                viewHolderParent.linearLayout_goodsList.removeAllViews();
            }
            final OrderViewModel mOrderViewModel = listData.get(position);

            //初始化订单号和下单时间
            viewHolderParent.tv_orderNum.setText("订单号：" + mOrderViewModel.getOrderNo());
            viewHolderParent.tv_orderStartTime.setText("下单时间：" + mOrderViewModel.getOrderTime());
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
                View view = View.inflate(mContext, R.layout.item_adapter_my_order_general_listview_child, null);
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
                if(mProduct.getJFUse() > 0){
                    viewHolderChild.tv_goodsPrice.setText("¥ " + StringUtil.doubleToString(mProduct.getJEUse()));
                }else{
                    viewHolderChild.tv_goodsPrice.setText("¥ " + StringUtil.doubleToString(mProduct.getUnitMoney()));
                }

                viewHolderParent.linearLayout_goodsList.addView(view);
            }
            //初始化总金额
            viewHolderParent.tv_orderPrices.setText("￥" + StringUtil.doubleToString(mOrderViewModel.getTotalMoney(), "0.00"));

            switch (mOrderViewModel.getOrderSign()) {
                //普通订单待支付和账期订单待付款
                case 1:
                    viewHolderParent.tv_orderState.setText("待支付");
                    viewHolderParent.tv_orderState.setVisibility(View.VISIBLE);
                    viewHolderParent.btn_orderOperationAction.setText("去支付");
                    viewHolderParent.btn_orderOperationAction.setEnabled(true);
                    viewHolderParent.btn_orderOperationAction.setVisibility(View.VISIBLE);
                    viewHolderParent.btn_orderOperationAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           toPayMethodSelectActivity(""+mOrderViewModel.getDjLsh(),mOrderViewModel.getTotalMoney());
                        }
                    });
                    if (mOrderViewModel.getDelayPayDays() == 0) {
                        viewHolderParent.linearLayout_orderBottom.setVisibility(View.VISIBLE);
                        viewHolderParent.tv_orderCancel.setVisibility(View.VISIBLE);
                        viewHolderParent.tv_orderCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showQueryCancelOrderDialog(""+mOrderViewModel.getDjLsh());
                            }
                        });
                    } else {
                        viewHolderParent.linearLayout_orderBottom.setVisibility(View.VISIBLE);
                        viewHolderParent.tv_orderCancel.setVisibility(View.GONE);
                        viewHolderParent.tv_accountOrderFinishTime.setVisibility(View.VISIBLE);
                        Date date_current = TimeUtil.getTimeDate(mOrderViewModel.getOrderTime());
                        Date date_finish = TimeUtil.add(date_current, mOrderViewModel.getDelayPayDays(), TimeUtil.DATE);
                        String strTimeFinish = TimeUtil.getTimeString(date_finish);
                        viewHolderParent.tv_accountOrderFinishTime.setText("到期时间:" + strTimeFinish);
                    }
                    break;
                case 3:
                    viewHolderParent.tv_orderState.setText("待发货");
                    viewHolderParent.tv_orderState.setVisibility(View.VISIBLE);
                    viewHolderParent.tv_orderCancel.setVisibility(View.GONE);
                    viewHolderParent.btn_orderOperationAction.setVisibility(View.INVISIBLE);
                    if (mOrderViewModel.getDelayPayDays() == 0) {
                        viewHolderParent.linearLayout_orderBottom.setVisibility(View.GONE);
                    }else{
                        viewHolderParent.linearLayout_orderBottom.setVisibility(View.VISIBLE);
                        viewHolderParent.tv_accountOrderFinishTime.setVisibility(View.VISIBLE);
                        Date date_current = TimeUtil.getTimeDate(mOrderViewModel.getOrderTime());
                        Date date_finish = TimeUtil.add(date_current, mOrderViewModel.getDelayPayDays(), TimeUtil.DATE);
                        String strTimeFinish = TimeUtil.getTimeString(date_finish);
                        viewHolderParent.tv_accountOrderFinishTime.setText("到期时间:" + strTimeFinish);
                    }
                    break;
                case 4:
                    viewHolderParent.tv_orderState.setText("待收货");
                    viewHolderParent.tv_orderState.setVisibility(View.VISIBLE);
                    viewHolderParent.tv_orderCancel.setVisibility(View.GONE);
                    viewHolderParent.btn_orderOperationAction.setText("确认收货");
                    viewHolderParent.btn_orderOperationAction.setEnabled(true);
                    viewHolderParent.btn_orderOperationAction.setVisibility(View.VISIBLE);
                    viewHolderParent.btn_orderOperationAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FragmentOkAndCancelDialog dialog = new FragmentOkAndCancelDialog();
                            dialog.initView("收货提示", "确认收货吗？", "取消", "确定", new FragmentOkAndCancelDialog.OnButtonClickListener() {
                                @Override
                                public void OnOkClick() {
                                    queryHaveReceiveGoods(mOrderViewModel);
                                }

                                @Override
                                public void OnCancelClick() {

                                }
                            });
                            dialog.show(mActivity.getSupportFragmentManager(),"dialog");
                        }
                    });
                    viewHolderParent.linearLayout_orderBottom.setVisibility(View.VISIBLE);
                    if (mOrderViewModel.getDelayPayDays() == 0) {
                        viewHolderParent.tv_accountOrderFinishTime.setVisibility(View.INVISIBLE);
                    }else {
                        viewHolderParent.tv_accountOrderFinishTime.setVisibility(View.VISIBLE);
                        Date date_current = TimeUtil.getTimeDate(mOrderViewModel.getOrderTime());
                        Date date_finish = TimeUtil.add(date_current, mOrderViewModel.getDelayPayDays(), TimeUtil.DATE);
                        String strTimeFinish = TimeUtil.getTimeString(date_finish);
                        viewHolderParent.tv_accountOrderFinishTime.setText("到期时间:" + strTimeFinish);
                    }
                    break;
                case -1:
                    viewHolderParent.tv_orderState.setText("此订单已被取消");
                    viewHolderParent.tv_orderState.setVisibility(View.VISIBLE);
                    viewHolderParent.tv_orderCancel.setVisibility(View.GONE);
                    viewHolderParent.btn_orderOperationAction.setVisibility(View.INVISIBLE);
                    if (mOrderViewModel.getDelayPayDays() == 0) {
                        viewHolderParent.linearLayout_orderBottom.setVisibility(View.GONE);
                    }else{
                        viewHolderParent.linearLayout_orderBottom.setVisibility(View.VISIBLE);
                        viewHolderParent.tv_accountOrderFinishTime.setVisibility(View.VISIBLE);
                        Date date_current = TimeUtil.getTimeDate(mOrderViewModel.getOrderTime());
                        Date date_finish = TimeUtil.add(date_current, mOrderViewModel.getDelayPayDays(), TimeUtil.DATE);
                        String strTimeFinish = TimeUtil.getTimeString(date_finish);
                        viewHolderParent.tv_accountOrderFinishTime.setText("到期时间:" + strTimeFinish);
                    }
                    break;
                case 9:
                    viewHolderParent.tv_orderState.setText("已完成");
                    viewHolderParent.tv_orderState.setVisibility(View.VISIBLE);
                    viewHolderParent.tv_orderCancel.setVisibility(View.GONE);
                    viewHolderParent.btn_orderOperationAction.setVisibility(View.INVISIBLE);
                    if (mOrderViewModel.getDelayPayDays() == 0) {
                        viewHolderParent.linearLayout_orderBottom.setVisibility(View.GONE);
                    }else {
                        viewHolderParent.linearLayout_orderBottom.setVisibility(View.VISIBLE);
                        viewHolderParent.tv_accountOrderFinishTime.setVisibility(View.VISIBLE);
                        Date date_current = TimeUtil.getTimeDate(mOrderViewModel.getOrderTime());
                        Date date_finish = TimeUtil.add(date_current, mOrderViewModel.getDelayPayDays(), TimeUtil.DATE);
                        String strTimeFinish = TimeUtil.getTimeString(date_finish);
                        viewHolderParent.tv_accountOrderFinishTime.setText("到期时间:" + strTimeFinish);
                    }
                    break;
            }
            return convertView;
        }


        private class ViewHolderParent {
            /**订单编号**/
            TextView tv_orderNum;
            /**订单开始时间**/
            TextView tv_orderStartTime;
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
            /**当前订单类型可操作的下一步动作**/
            Button btn_orderOperationAction;
            /**订单底部栏**/
            LinearLayout linearLayout_orderBottom;
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
    private void toPayMethodSelectActivity(String totalOrderIds,Double totalPrices) {

//        Intent mIntent = new Intent(mActivity, CreateOrderMethodSelectActivity.class);
//        int type;
//        if(IfContainUnCreditOrderItem){
//            type = CreateOrderMethodSelectModel.TypeMyOrderCanNotPayCredit;
//        }else{
//            type = CreateOrderMethodSelectModel.TypeMyOrderCanPayCredit;
//        }
//        CreateOrderMethodSelectModel paramSport = new CreateOrderMethodSelectModel(
//                1, totalPrices, totalOrderIds,type);
//
//        mIntent.putExtra(InformationCodeUtil.IntentPayMethodSelectActivityParamObject, paramSport);
//        mActivity.startActivityForResult(mIntent, RequestCode_MyOrderGeneralPager);
//        mActivity.overridePendingTransition(R.anim.in_from_bottom,R.anim.not_change);

        Intent intent = new Intent( mActivity, PayMethodSelectActivity.class);
        intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalMoney, totalPrices);
        intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds, totalOrderIds);
        mActivity.startActivityForResult(intent, RequestCode_MyOrderGeneralPager);
        mActivity.overridePendingTransition(R.anim.in_from_bottom, R.anim.not_change);
    }

    //确定取消订单对话框
    protected void showQueryCancelOrderDialog(final String totalOrderIds) {

        final FragmentOkAndCancelDialog fragmentOkAndCancelDialog =  new FragmentOkAndCancelDialog();
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
        fragmentOkAndCancelDialog.show(mActivity.getSupportFragmentManager(), "fragmentOkAndCancelDialog");
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


    /**确认收货**/
    private void queryHaveReceiveGoods(OrderViewModel mOrderViewModel) {

        String methodName = InformationCodeUtil.methodNameChangeOrderState;
        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mActivity).getDjLsh());
        requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mActivity).getOpenKey());
        requestSoapObject.addProperty("orderID", mOrderViewModel.getDjLsh());
        if(mOrderViewModel.getDelayPayDays() == 0){
            requestSoapObject.addProperty("state", 9);//普通订单变成已完成
        }else{
            requestSoapObject.addProperty("state", 1);//账期订单变成待支付
        }
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
                } else {
                    SnackBarUtil.show( swipeRefreshListView ,"暂无更多订单数据");
                }
            }
            if (listResult != null) {
                listData.addAll(listResult.getList());
            }
            if(swipeRefreshListView != null){
                myOrderAdapter.notifyDataSetChanged();
                swipeRefreshListView.closeRefreshState();
            }
            return;
        }

        if(InformationCodeUtil.methodNameChangeOrderState.equals(methodName)){
//            {"DjLsh":-1,"Msg":"操作成功","Sign":1,"Tags":""}
            try {
                JSONObject jsonObject = new JSONObject(returnString);
                int sign = jsonObject.getInt("Sign");
                String msg = jsonObject.getString("Msg");
                ToastUtil.show(mActivity,msg);
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
    public void connectServiceFailed(String returnStrError, String methodName, Integer state, boolean whetherRefresh) {
        if(InformationCodeUtil.methodNameGetOrders.equals(methodName)) {

            connectSuccessFlag = false;
            ToastUtil.show(mActivity, returnStrError);
            if (!whetherRefresh) {
                currentPageIndex--;
            }
            swipeRefreshListView.closeRefreshState();
        }

        if(InformationCodeUtil.methodNameChangeOrderState.equals(methodName)){
            ToastUtil.show(mActivity, returnStrError);
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








