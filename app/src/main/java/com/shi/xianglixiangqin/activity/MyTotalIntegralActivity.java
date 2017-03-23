package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afinalstone.androidstudy.swiperefreshview.OnSwipeRefreshViewListener;
import com.afinalstone.androidstudy.swiperefreshview.SwipeRefreshRecycleView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.shi.xianglixiangqin.adapter.MyBaseRecycleAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.DensityUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.SnackBarUtil;
import com.shi.xianglixiangqin.util.TimeUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyTotalIntegralActivity extends MyBaseActivity implements OnConnectServerStateListener, OnSwipeRefreshViewListener {

    /**
     * 返回
     **/
    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    /**
     * 标题
     **/
    @BindView(R.id.tv_title)
    public TextView tv_title;
    /**
     * 总积分数量
     **/
    private TextView tv_myTotalIntegral;
    /**
     * 积分商品列表
     **/
    @BindView(R.id.swipeRefreshRecycleView)
    SwipeRefreshRecycleView swipeRefreshRecycleView;
    RecycleAdapter recycleAdapter;
    HeaderAndFooterRecyclerViewAdapter headerAdapter;
    List<IntegralExchangeRecordModel> listData = new ArrayList<>();

    /**
     * 当前页面索引
     **/
    private int currentPageIndex = 1;

    @Override
    public void initView() {
        setContentView(R.layout.activity_my_total_integral);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText("积分流水");
    }

    @Override
    public void initData() {
        recycleAdapter = new RecycleAdapter(mContext, listData);
        headerAdapter = new HeaderAndFooterRecyclerViewAdapter(recycleAdapter);
        //添加头布局
        View viewHeader = View.inflate(mContext, R.layout.item_adapter_my_integral_recycleview_header, null);
        tv_myTotalIntegral = (TextView) viewHeader.findViewById(R.id.tv_myTotalIntegral);
        viewHeader.findViewById(R.id.btn_useMyIntegral)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, IntegralShoppingActivity.class));
                    }
                });
        viewHeader.setLayoutParams(new RecyclerView.LayoutParams(displayDeviceWidth, DensityUtil.dip2px(mContext, 156)));
        headerAdapter.addHeaderView(viewHeader);
        //设置管理器
        swipeRefreshRecycleView.getItemView().setLayoutManager(new LinearLayoutManager(mContext));
        swipeRefreshRecycleView.getItemView().setAdapter(headerAdapter);

        swipeRefreshRecycleView.IfOpenBottomRefresh(true);
        swipeRefreshRecycleView.setOnRefreshListener(this);
        swipeRefreshRecycleView.openRefreshState();
        getCurrentShopTotalIntegral();
    }

    @OnClick(R.id.iv_titleLeft)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }

    private void getData(boolean IfRefresh) {
        if (IfRefresh) {
            listData.clear();
            headerAdapter.notifyDataSetChanged();
            currentPageIndex = 1;
        } else {
            currentPageIndex++;
        }
        String methodName = InformationCodeUtil.methodNameGetJFLogPage;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("pageindex", currentPageIndex);
        soapObject.addProperty("userid", MyApplication.getmCustomModel(mContext).getDjLsh());
        soapObject.addProperty("shopid", MyApplication.getmCustomModel(mContext).getCurrentBrowsingShopID());
        soapObject.addProperty("jftype", 1);
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, soapObject, methodName, IfRefresh);
        connectCustomServiceAsyncTask.initProgressDialog(false);
        connectCustomServiceAsyncTask.execute();
    }

    /**
     * 获取当前店铺的积分
     **/
    private void getCurrentShopTotalIntegral() {
        String methodName = InformationCodeUtil.methodNameGetMyJFShop;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("userid", MyApplication.getmCustomModel(mContext).getDjLsh());
        soapObject.addProperty("shopid", MyApplication.getmCustomModel(mContext).getCurrentBrowsingShopID());
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, soapObject, methodName);
        connectCustomServiceAsyncTask.initProgressDialog(false);
        connectCustomServiceAsyncTask.execute();
    }

    @Override
    public void connectServiceSuccessful(String returnString, String methodName, Object state, boolean whetherRefresh) {

        if (InformationCodeUtil.methodNameGetJFLogPage.equals(methodName)) {
            LogUtil.LogShitou("积分流水当前分页数", ""+currentPageIndex);
            List<IntegralExchangeRecordModel> listResult = null;
            try {
                Gson gson = new Gson();
                listResult = gson.fromJson(returnString, new TypeToken<List<IntegralExchangeRecordModel>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
                if(!whetherRefresh)
                 currentPageIndex--;
            }
            if (listResult == null | listResult.size() == 0) {
                if (whetherRefresh) {
                    SnackBarUtil.show( swipeRefreshRecycleView ,"暂无积分流水");
                } else {
                    SnackBarUtil.show( swipeRefreshRecycleView ,"暂无更多积分流水");
                }
            } else {
                listData.addAll(listResult);
            }
            headerAdapter.notifyDataSetChanged();
            swipeRefreshRecycleView.closeRefreshState();
            return;
        }

        if (InformationCodeUtil.methodNameGetMyJFShop.equals(methodName)) {
            LogUtil.LogShitou("我的积分", returnString);
            try {
                JSONObject jsonObject = new JSONObject(returnString);
                long totalJf = jsonObject.getLong("jFTotalNow");
                tv_myTotalIntegral.setText("" + totalJf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Object state, boolean whetherRefresh) {
        if (InformationCodeUtil.methodNameGetJFSPPage.equals(methodName)) {
            if (!whetherRefresh) {
                currentPageIndex--;
            }
            swipeRefreshRecycleView.closeRefreshState();
        }

    }

    @Override
    public void connectServiceCancelled(String returnString, String methodName, Object state, boolean whetherRefresh) {
    }

    @Override
    public void onTopRefrushListener() {
        getData(true);
    }

    @Override
    public void onBottomRefrushListener() {
        getData(false);
    }

    /**
     * 适配器
     **/
    protected class RecycleAdapter extends MyBaseRecycleAdapter<RecycleAdapter.MyViewHolder, IntegralExchangeRecordModel> {

        public RecycleAdapter(Context context, List<IntegralExchangeRecordModel> listData) {
            super(context, listData);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = mLayoutInflater.inflate(R.layout.item_adapter_my_integral_recycleview, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            IntegralExchangeRecordModel recordModel = listData.get(position);
            holder.tv_shopName.setText(recordModel.shopname);
            switch (recordModel.jftype) {
                case 0:
                    // 获得积分
                    holder.tv_integralChangeNum.setText("+"+recordModel.jfnum);
                    break;
                case 1:
                    //使用积分
                    holder.tv_integralChangeNum.setText("-"+recordModel.jfnum);
                    break;
                case 2:
            }
//            holder.tv_recordTime.setText(TimeUtil.getTimeString());
            holder.tv_recordTime.setText(recordModel.addtime);
            holder.tv_remainIntegral.setText("余："+recordModel.myhavejf);
        }


        protected class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_shopName)
            TextView tv_shopName;
            @BindView(R.id.tv_integralChangeNum)
            TextView tv_integralChangeNum;
            @BindView(R.id.tv_recordTime)
            TextView tv_recordTime;
            @BindView(R.id.tv_remainIntegral)
            TextView tv_remainIntegral;

            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private class IntegralExchangeRecordModel {
        public String addtime;
        public int buynum;
        public int gid;
        public String gname;
        public int id;
        public double jenum;
        public long jfnum;
        public int jftype;
        public long myhavejf;
        public String ono;
        public String pcimgsrc;
        public String pcname;
        public String pname;
        public String remark;
        public int shopid;
        public String shopname;
        public int userid;
        public String username;
    }

}
