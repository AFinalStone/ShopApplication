package com.shi.xianglixiangqin.activity;

import android.content.Context;
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
import com.shi.xianglixiangqin.json.JSONResultMsgModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.DensityUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.SnackBarUtil;
import com.shi.xianglixiangqin.util.TimeUtil;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyTotalFlyCoinActivity extends MyBaseActivity implements OnConnectServerStateListener, OnSwipeRefreshViewListener {

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
    private TextView tv_myTotalFlyCoin;
    /**
     * 积分商品列表
     **/
    @BindView(R.id.swipeRefreshRecycleView)
    SwipeRefreshRecycleView swipeRefreshRecycleView;
    RecycleAdapter recycleAdapter;
    HeaderAndFooterRecyclerViewAdapter headerAdapter;
    List<FlyCoinExchangeRecordModel> listData = new ArrayList<>();

    /**
     * 当前页面索引
     **/
    private int currentPageIndex = 1;

    @Override
    public void initView() {
        setContentView(R.layout.activity_my_total_flycoin);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.title_myFlyCoinActivity);
    }

    @Override
    public void initData() {
        recycleAdapter = new RecycleAdapter(mContext, listData);
        headerAdapter = new HeaderAndFooterRecyclerViewAdapter(recycleAdapter);
        //添加头布局
        View viewHeader = View.inflate(mContext, R.layout.item_adapter_my_fly_coin_recycleview_header, null);
        tv_myTotalFlyCoin = (TextView) viewHeader.findViewById(R.id.tv_myTotalFlyCoin);
        viewHeader.setLayoutParams(new RecyclerView.LayoutParams(displayDeviceWidth, DensityUtil.dip2px(mContext, 115)));
        headerAdapter.addHeaderView(viewHeader);
        //设置管理器
        swipeRefreshRecycleView.getItemView().setLayoutManager(new LinearLayoutManager(mContext));
        swipeRefreshRecycleView.getItemView().setAdapter(headerAdapter);

        swipeRefreshRecycleView.IfOpenBottomRefresh(true);
        swipeRefreshRecycleView.setOnRefreshListener(this);
        swipeRefreshRecycleView.openRefreshState();
        getCurrentTotalFlyCoin();
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
        String methodName = InformationCodeUtil.methodNameGetFBlogs;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("pageindex", currentPageIndex);
        soapObject.addProperty("userid", MyApplication.getmCustomModel(mContext).getDjLsh());
        soapObject.addProperty("shopid", MyApplication.getmCustomModel(mContext).getCurrentBrowsingShopID());
        soapObject.addProperty("fbtype", -1);
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, soapObject, methodName, IfRefresh);
        connectCustomServiceAsyncTask.initProgressDialog(false);
        connectCustomServiceAsyncTask.execute();
    }

    /**
     * 获取当前总的飞币数量
     **/
    private void getCurrentTotalFlyCoin() {
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

    @Override
    public void connectServiceSuccessful(String returnString, String methodName, Object state, boolean whetherRefresh) {

        if (InformationCodeUtil.methodNameGetFBlogs.equals(methodName)) {
            LogUtil.LogShitou("飞币流水", returnString);
            List<FlyCoinExchangeRecordModel> listResult = null;
            try {
                Gson gson = new Gson();
                listResult = gson.fromJson(returnString, new TypeToken<List<FlyCoinExchangeRecordModel>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
                if (!whetherRefresh) {
                    currentPageIndex--;
                }
            }
            if (listResult == null | listResult.size() == 0) {
                if (whetherRefresh) {
                    SnackBarUtil.show( swipeRefreshRecycleView ,"暂无飞币流水");
                } else {
                    SnackBarUtil.show( swipeRefreshRecycleView ,"暂无更多飞币流水");
                }
            } else {
                listData.addAll(listResult);
            }
            headerAdapter.notifyDataSetChanged();
            swipeRefreshRecycleView.closeRefreshState();
            return;
        }

        if (InformationCodeUtil.methodNameGetAcceptFlyCoin.equals(methodName)) {
            LogUtil.LogShitou("我的飞币", returnString);
            Gson gson = new Gson();
            if (methodName == InformationCodeUtil.methodNameGetAcceptFlyCoin) {
                try {
                    JSONResultMsgModel result = gson.fromJson(returnString, JSONResultMsgModel.class);
                    int totalFlyCoin_user = Integer.parseInt(result.getMsg());
                    tv_myTotalFlyCoin.setText(new StringBuffer().append(totalFlyCoin_user).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Object state, boolean whetherRefresh) {
        if (InformationCodeUtil.methodNameGetFBlogs.equals(methodName)) {
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
    protected class RecycleAdapter extends MyBaseRecycleAdapter<RecycleAdapter.MyViewHolder, FlyCoinExchangeRecordModel> {

        public RecycleAdapter(Context context, List<FlyCoinExchangeRecordModel> listData) {
            super(context, listData);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = mLayoutInflater.inflate(R.layout.item_adapter_my_fly_coin_recycleview, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            FlyCoinExchangeRecordModel recordModel = listData.get(position);
            holder.tv_orderNum.setText("订单号:"+recordModel.oid);
            switch (recordModel.fbtype) {
                case 0:
                    // 获得积分
                    holder.tv_flyCoinChangeNum.setText("+" + recordModel.syfbnum);
                    holder.tv_goodsName.setTextColor(getResources().getColor(R.color.colorRed_FFE83821));
                    holder.tv_goodsName.setText("订单交易失败，飞币返还");
                    break;
                case 1:
                    //使用积分
                    holder.tv_flyCoinChangeNum.setText("-" + recordModel.syfbnum);
                    holder.tv_goodsName.setTextColor(getResources().getColor(R.color.colorBlack_FF323232));
                    holder.tv_goodsName.setText(recordModel.title);
                    break;
            }
            Date date = TimeUtil.getTimeString(recordModel.addtime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            String day = TimeUtil.getTimeString(date, new SimpleDateFormat("dd"));
            String month = TimeUtil.getTimeString(date, new SimpleDateFormat("MM"));
            holder.tv_recordDay.setText(day);
            holder.tv_recordMonth.setText(month+"月");
            holder.tv_remainFlyCoin.setText("剩余：" + recordModel.fbnum);
        }


        protected class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_recordDay)
            TextView tv_recordDay;
            @BindView(R.id.tv_recordMonth)
            TextView tv_recordMonth;
            @BindView(R.id.tv_orderNum)
            TextView tv_orderNum;
            @BindView(R.id.tv_goodsName)
            TextView tv_goodsName;
            @BindView(R.id.tv_flyCoinChangeNum)
            TextView tv_flyCoinChangeNum;
            @BindView(R.id.tv_remainFlyCoin)
            TextView tv_remainFlyCoin;

            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private class FlyCoinExchangeRecordModel {
        public String addtime;
        public int fbnum;
        public int fbtype;
        public int id;
        //订单号
        public String oid;
        public int shopid;
        public String shopname;
        public int syfbnum;
        public String title;
        public int uid;
        public String userlable;
    }

}
