package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afinalstone.androidstudy.swiperefreshview.OnSwipeRefreshViewListener;
import com.afinalstone.androidstudy.swiperefreshview.SwipeRefreshListView;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.model.OrderViewModel;
import com.shi.xianglixiangqin.model.OrderViewProductModel;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtherOrderActivity extends MyBaseActivity implements OnSwipeRefreshViewListener{


    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.swipeRefreshListView)
    SwipeRefreshListView swipeRefreshListView;

    /**
     * listView数据集合
     **/
    List<OrderViewModel> listData = new ArrayList<OrderViewModel>();
    ;
    /**
     * gridView适配器
     **/
    private MyOrderAdapter orderAdapter;
    /**
     * 暂无此类订单
     **/
    private View viewListViewIsEmpty;

    @Override
    public void initView() {
        setContentView(R.layout.activity_other_order);
        ButterKnife.bind(this);

        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.title_otherOrder);
        iv_titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewListViewIsEmpty = View.inflate(mContext,R.layout.item_adapter_other_order_listview_empty,null);
        swipeRefreshListView.getListView().addHeaderView(viewListViewIsEmpty);
        orderAdapter = new MyOrderAdapter(mContext, listData);
        swipeRefreshListView.getListView().setAdapter(orderAdapter);
        swipeRefreshListView.setOnRefreshListener(this);
        swipeRefreshListView.IfOpenBottomRefresh(true);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onTopRefrushListener() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshListView.closeRefreshState();
            }
        },2000);
    }

    @Override
    public void onBottomRefrushListener() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshListView.closeRefreshState();
            }
        },2000);
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
                viewHolderChild.tv_goodsPrice.setText("¥ " + StringUtil.doubleToString(mProduct.getUnitMoney(),"0.00"));

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
//                            toPayMethodSelectActivity(""+mOrderViewModel.getDjLsh(),mOrderViewModel.getTotalMoney());
                        }
                    });
                    viewHolderParent.linearLayout_orderBottom.setVisibility(View.VISIBLE);
                    if (mOrderViewModel.getDelayPayDays() == 0) {
                        viewHolderParent.tv_orderCancel.setVisibility(View.VISIBLE);
                        viewHolderParent.tv_orderCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                showQueryCancelOrderDialog(""+mOrderViewModel.getDjLsh());
                            }
                        });
                    } else {
                        viewHolderParent.tv_orderCancel.setVisibility(View.GONE);
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


}
