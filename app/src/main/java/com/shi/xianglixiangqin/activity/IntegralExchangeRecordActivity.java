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
import com.shi.xianglixiangqin.adapter.MyBaseRecycleAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.SnackBarUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntegralExchangeRecordActivity extends MyBaseActivity implements OnConnectServerStateListener, OnSwipeRefreshViewListener {

    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.swipeRefreshRecycleView)
    SwipeRefreshRecycleView swipeRefreshRecycleView;
    RecycleAdapter recycleAdapter;
    List<IntegralExchangeRecordModel> listData = new ArrayList<>();
    /**
     * 当前页面索引
     **/
    private int currentPageIndex;

    @Override
    public void initView() {
        setContentView(R.layout.activity_integral_exchange_record);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.title_integralExchangeRecordActivity);
    }

    @Override
    public void initData() {
        recycleAdapter = new RecycleAdapter(mContext, listData);
        swipeRefreshRecycleView.getItemView().setLayoutManager(new LinearLayoutManager(mContext));
        swipeRefreshRecycleView.getItemView().setAdapter(recycleAdapter);
        swipeRefreshRecycleView.IfOpenBottomRefresh(true);
        swipeRefreshRecycleView.setOnRefreshListener(this);
        //请求数据
        swipeRefreshRecycleView.openRefreshState();
    }

    private void getData(boolean IfRefresh) {
        if (IfRefresh) {
            listData.clear();
            recycleAdapter.notifyDataSetChanged();
            currentPageIndex = 1;
        } else {
            currentPageIndex++;
        }
        String methodName = InformationCodeUtil.methodNameGetJFLogPage;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("pageindex", currentPageIndex);
        soapObject.addProperty("userid", MyApplication.getmCustomModel(mContext).getDjLsh());
        soapObject.addProperty("shopid", MyApplication.getmCustomModel(mContext).getCurrentBrowsingShopID());
        soapObject.addProperty("jftype", -1);
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, soapObject, methodName, IfRefresh);
        connectCustomServiceAsyncTask.initProgressDialog(false);
        connectCustomServiceAsyncTask.execute();
    }

    @OnClick(R.id.iv_titleLeft)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
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

    @Override
    public void connectServiceSuccessful(String returnString, String methodName, Object state, boolean whetherRefresh) {
        if (InformationCodeUtil.methodNameGetJFLogPage.equals(methodName)) {
            LogUtil.LogShitou("积分流水", returnString);
            List<IntegralExchangeRecordModel> listResult = null;
            try {
                Gson gson = new Gson();
                listResult = gson.fromJson(returnString, new TypeToken<List<IntegralExchangeRecordModel>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
                if(whetherRefresh)
                    currentPageIndex--;
            }
            if (listResult == null || listResult.size() == 0) {
                if (whetherRefresh) {
                    ToastUtil.show(mContext, "暂无兑换商品");
                } else {
                    SnackBarUtil.show(swipeRefreshRecycleView, "暂无更多兑换商品");
                }
            } else {
                listData.addAll(listResult);
            }
            recycleAdapter.notifyDataSetChanged();
            swipeRefreshRecycleView.closeRefreshState();
            return;
        }
    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Object state, boolean whetherRefresh) {
        if (InformationCodeUtil.methodNameGetJFLogPage.equals(methodName)) {
            if (!whetherRefresh) {
                currentPageIndex--;
            }
            swipeRefreshRecycleView.closeRefreshState();
        }
    }

    @Override
    public void connectServiceCancelled(String returnString, String methodName, Object state, boolean whetherRefresh) {

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

            View view = mLayoutInflater.inflate(R.layout.item_adapter_integral_exchange_record, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final IntegralExchangeRecordModel recordModel = listData.get(position);
            ImagerLoaderUtil.getInstance(mContext).displayMyImage(recordModel.pcimgsrc, holder.iv_goodsImages);
            holder.tv_goodsName.setText(recordModel.gname);
            holder.tv_goodsJfPrices.setText(new StringBuffer().append(recordModel.jfnum).toString());
            holder.tv_goodsPrices.setText(StringUtil.doubleToString(recordModel.jenum));
            holder.tv_recordTime.setText(recordModel.addtime);
        }


        protected class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.iv_goodsImages)
            ImageView iv_goodsImages;
            @BindView(R.id.tv_goodsName)
            TextView tv_goodsName;
            @BindView(R.id.tv_goodsJfPrices)
            TextView tv_goodsJfPrices;
            @BindView(R.id.tv_goodsPrices)
            TextView tv_goodsPrices;
            @BindView(R.id.tv_recordTime)
            TextView tv_recordTime;

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
