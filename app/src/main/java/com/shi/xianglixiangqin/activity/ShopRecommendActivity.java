package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.shi.xianglixiangqin.model.GoodsGeneralModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 资源圈 推荐店铺
 *
 * @author SHI
 * @time 2016/12/5 10:55
 */
public class ShopRecommendActivity extends MyBaseActivity implements OnConnectServerStateListener, SwipeRefreshLayout.OnRefreshListener {

    /**
     * 左侧返回
     **/
    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    /**
     * 页面标题
     **/
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<ShopRecommendModel> listData = new ArrayList<ShopRecommendModel>();
    private MyShopRecommendAdapter myShopRecommendAdapter;
    private View listViewIsEmpty;

    @Override
    public void initView() {
        setContentView(R.layout.activity_shop_recommend);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.shopRecommendActivity_title);

        listViewIsEmpty = View.inflate(mContext, R.layout.item_adapter_list_view_empty, null);
        ((ImageView) listViewIsEmpty.findViewById(R.id.iv_empty)).setImageResource(R.drawable.icon_shop_recommend_is_empty);
        if (listView.getHeaderViewsCount() == 0) {
            listView.addHeaderView(listViewIsEmpty);
        }
        myShopRecommendAdapter = new MyShopRecommendAdapter(mContext, listData);
        listView.setAdapter(myShopRecommendAdapter);
        swipeRefreshLayout.setColorSchemeColors(Color.RED
                , Color.GREEN
                , Color.BLUE
                , Color.YELLOW
                , Color.CYAN
                , 0xFFFE5D14
                , Color.MAGENTA);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void initData() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }


    private void getData() {
        String methodName = InformationCodeUtil.methodNameGetAttentionShops;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
        soapObject.addProperty("enterShopID", MyApplication.getmCustomModel(mContext).getCurrentBrowsingShopID());
        ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
                mContext, this, soapObject, methodName);
        connectGoodsServiceAsyncTask.initProgressDialog(false);
        connectGoodsServiceAsyncTask.execute();
    }

    private void enterShopRecord(int shopId) {
        String methodName = InformationCodeUtil.methodNameEnterzyqShop;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("shopid", shopId);
        soapObject.addProperty("uid", MyApplication.getmCustomModel(mContext).getDjLsh());
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, soapObject, methodName);
        connectCustomServiceAsyncTask.initProgressDialog(false);
        connectCustomServiceAsyncTask.execute();
    }

    @Override
    public void connectServiceSuccessful(String returnString, String methodName, Object state, boolean whetherRefresh) {

        LogUtil.LogShitou(returnString);
        if (InformationCodeUtil.methodNameGetAttentionShops.equals(methodName)) {
            List<ShopRecommendModel> listTemp = null;
            try {
                Gson gson = new Gson();
                listTemp = gson.fromJson(returnString, new TypeToken<List<ShopRecommendModel>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (listTemp != null) {
                listView.removeHeaderView(listViewIsEmpty);
                listData.clear();
                listData.addAll(listTemp);
                myShopRecommendAdapter.notifyDataSetChanged();
            } else {
                ToastUtil.show(mContext, "该店铺还未设置推荐店铺");
                if (listView.getHeaderViewsCount() == 0) {
                    listView.addHeaderView(listViewIsEmpty);
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Object state, boolean whetherRefresh) {
        if (InformationCodeUtil.methodNameGetAttentionShops.equals(methodName)) {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.show(mContext, "网络异常，数据获取失败");
        }

    }

    @Override
    public void connectServiceCancelled(String returnString, String methodName, Object state, boolean whetherRefresh) {

    }

    @Override
    public void onRefresh() {
        getData();
    }


    @OnClick(R.id.iv_titleLeft)
    public void onClick() {
        finish();
    }


    private class MyShopRecommendAdapter extends MyBaseAdapter<ShopRecommendModel> {

        public MyShopRecommendAdapter(Context mContext, List<ShopRecommendModel> listData) {
            super(mContext, listData);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_adapter_shop_recommend_listview_parent, null);
                viewHolder.tv_shopName = (TextView) convertView.findViewById(R.id.tv_shopName);
                viewHolder.btn_IntoShop = (Button) convertView.findViewById(R.id.btn_IntoShop);
                viewHolder.tv_shopDesc = (TextView) convertView.findViewById(R.id.tv_shopDesc);
                viewHolder.linearLayout_specialPricesGoods = (LinearLayout) convertView.findViewById(R.id.linearLayout_specialPricesGoods);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                viewHolder.linearLayout_specialPricesGoods.removeAllViews();
            }
            final ShopRecommendModel shopModel = listData.get(position);
            viewHolder.tv_shopName.setText(shopModel.ShopName);
            if (shopModel.Remark != null) {
                SpannableString sps = new SpannableString("简介：" + shopModel.Remark);
                sps.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack_FF323232)), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.tv_shopDesc.setText(sps);
            } else {
                viewHolder.tv_shopDesc.setText("暂无简介");
            }
            viewHolder.btn_IntoShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enterShopRecord(shopModel.ID);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentShopRecommendActivityCurrentShopID, shopModel.ID);
                    startActivity(intent);
                    finish();
                }
            });
            if (shopModel.SpecialProducts != null) {
                LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(ShopRecommendActivity.this.displayDeviceWidth / 3, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayout.setMargins(2, 5, 2, 5);
                for (int i = 0; i < shopModel.SpecialProducts.size(); i++) {
                    GoodsGeneralModel goodsModel = shopModel.SpecialProducts.get(i);
                    View view = View.inflate(mContext, R.layout.item_adapter_shop_recommend_listview_child, null);
                    ImageView iv_goodsImages = (ImageView) view.findViewById(R.id.iv_goodsImages);
                    ImagerLoaderUtil.getInstance(mContext).displayMyImage(goodsModel.getImgUrl(), iv_goodsImages);
                    ((TextView) view.findViewById(R.id.tv_goodsName)).setText(goodsModel.getGoodsName());
                    ((TextView) view.findViewById(R.id.tv_goodsPrice)).setText("¥" + StringUtil.doubleToString(goodsModel.getMinPrice(), "0.00"));
                    viewHolder.linearLayout_specialPricesGoods.addView(view, linearLayout);
                }
            }

            return convertView;
        }

        private class ViewHolder {
            public TextView tv_shopName;
            public Button btn_IntoShop;
            public TextView tv_shopDesc;
            public LinearLayout linearLayout_specialPricesGoods;
        }
    }

    private class ShopRecommendModel {
        /**
         * 标识
         **/
        public int ID;
        /**
         * 店铺名称
         **/
        public String ShopName;
        /**
         * 店铺简介
         **/
        public String Remark;
        /**
         * 特价商品集合
         **/
        public List<GoodsGeneralModel> SpecialProducts;
    }

}
