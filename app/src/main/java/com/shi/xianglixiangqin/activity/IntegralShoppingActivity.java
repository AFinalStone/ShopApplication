package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.SnackBarUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntegralShoppingActivity extends MyBaseActivity implements OnConnectServerStateListener, OnSwipeRefreshViewListener {


    /**返回**/
    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    /**标题**/
    @BindView(R.id.tv_title)
    public TextView tv_title;
    /**总积分数量**/
    private TextView tv_totalIntegral;
    /**
     * 积分商品列表
     **/
    @BindView(R.id.swipeRefreshRecycleView)
    SwipeRefreshRecycleView swipeRefreshRecycleView;
    RecycleAdapter recycleAdapter;
    HeaderAndFooterRecyclerViewAdapter headerAdapter;
    List<GoodsIntegralModel> listData = new ArrayList<>();

    /**
     * 当前页面索引
     **/
    private int currentPageIndex;

    @Override
    public void initView() {
        setContentView(R.layout.activity_integral_shopping);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText("积分商城");
    }

    @Override
    public void initData() {
        currentPageIndex = 1;

        recycleAdapter = new RecycleAdapter(mContext, listData);
        headerAdapter = new HeaderAndFooterRecyclerViewAdapter(recycleAdapter);
        //添加头布局
        View viewHeader = View.inflate(mContext, R.layout.item_adapter_integral_shopping_recycleview_header, null);
        tv_totalIntegral = (TextView) viewHeader.findViewById(R.id.tv_totalIntegral);
        viewHeader.findViewById(R.id.btn_toIntegralExchangeRecord)
        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, IntegralExchangeRecordActivity.class));
            }
        });

        viewHeader.findViewById(R.id.iv_headerImage).setLayoutParams(
                new LinearLayout.LayoutParams(displayDeviceWidth,displayDeviceWidth*28/75));
        headerAdapter.addHeaderView(viewHeader);
        //设置GridView管理器
        swipeRefreshRecycleView.getItemView().setLayoutManager(new GridLayoutManager(mContext, 2));
        swipeRefreshRecycleView.getItemView().setAdapter(headerAdapter);

        swipeRefreshRecycleView.IfOpenBottomRefresh(true);
        swipeRefreshRecycleView.setOnRefreshListener(this);
        swipeRefreshRecycleView.openRefreshState();
        getCurrentShopTotalIntegral();
    }


    private void getData(boolean IfRefresh) {
        if (IfRefresh) {
            listData.clear();
            headerAdapter.notifyDataSetChanged();
            currentPageIndex = 1;
        } else {
            currentPageIndex++;
        }
        String methodName = InformationCodeUtil.methodNameGetJFSPPage;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("pageindex", currentPageIndex);
        soapObject.addProperty("shopid", MyApplication.getmCustomModel(mContext).getCurrentBrowsingShopID());
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, soapObject, methodName, IfRefresh);
        connectCustomServiceAsyncTask.initProgressDialog(false);
        connectCustomServiceAsyncTask.execute();
    }

    /**获取当前店铺的积分**/
    private void getCurrentShopTotalIntegral(){
        String methodName = InformationCodeUtil.methodNameGetMyJFShop;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("userid", MyApplication.getmCustomModel(mContext).getDjLsh());
        soapObject.addProperty("shopid", MyApplication.getmCustomModel(mContext).getCurrentBrowsingShopID());
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, soapObject, methodName);
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
    public void connectServiceSuccessful(String returnString, String methodName, Object state, boolean whetherRefresh) {
        if(InformationCodeUtil.methodNameGetJFSPPage.equals(methodName)){
            List<GoodsIntegralModel> listResult = null;
            try {
                Gson gson = new Gson();
                listResult = gson.fromJson(returnString, new TypeToken<List<GoodsIntegralModel>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
                if(!whetherRefresh){
                    currentPageIndex--;
                }
            }

            if (listResult == null|| listResult.size() == 0) {
                if (whetherRefresh) {
                    ToastUtil.show(mContext, "暂无积分商品");
                } else {
                    SnackBarUtil.show(swipeRefreshRecycleView, "暂无更多积分商品");
                }
            } else {
                listData.addAll(listResult);
            }
            headerAdapter.notifyDataSetChanged();
            swipeRefreshRecycleView.closeRefreshState();
            return;
        }

        if(InformationCodeUtil.methodNameGetMyJFShop.equals(methodName)){
            try {
                JSONObject jsonObject = new JSONObject(returnString);
                long totalJf = jsonObject.getLong("jFTotalNow");
                tv_totalIntegral.setText(""+totalJf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Object state, boolean whetherRefresh) {
        if(InformationCodeUtil.methodNameGetJFSPPage.equals(methodName)){
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
    protected class RecycleAdapter extends MyBaseRecycleAdapter<RecycleAdapter.MyViewHolder, GoodsIntegralModel> {

        public RecycleAdapter(Context context, List<GoodsIntegralModel> listData) {
            super(context, listData);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = mLayoutInflater.inflate(R.layout.item_adapter_integral_shopping_recycleview, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final GoodsIntegralModel goodsIntegralModel = listData.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GoodsDetailIntegralActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentGoodsID, goodsIntegralModel.gid);
                    intent.putExtra(InformationCodeUtil.IntentGoodsJfPrices, goodsIntegralModel.minjf);
                    mContext.startActivity(intent);
                }
            });
            ImagerLoaderUtil.getInstance(mContext).displayMyImage(goodsIntegralModel.pcimgsrc, holder.iv_goodsImage);
            holder.tv_goodsName.setText(goodsIntegralModel.gname);
            holder.tv_goodsPrices.setText(goodsIntegralModel.minjf+"积分");
        }


        protected class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.iv_goodsImage)
            ImageView iv_goodsImage;
            @BindView(R.id.tv_goodsName)
            TextView tv_goodsName;
            @BindView(R.id.tv_goodsPrices)
            TextView tv_goodsPrices;

            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private class GoodsIntegralModel {
        public String etime;
        public int gid;
        public String gname;
        public int isrun;
        public int isshow;
        public double maxje;
        public long maxjf;
        public double minje;
        public long minjf;
        public String pcimgsrc;
        public int shopid;
        public String stime;
    }


}
