package com.shi.xianglixiangqin.frament;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.activity.ConfirmOrderGeneralActivity;
import com.shi.xianglixiangqin.activity.GoodsDetailGeneralActivity;
import com.shi.xianglixiangqin.activity.MainActivity;
import com.shi.xianglixiangqin.activity.MyBaseActivity;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultMsgModel;
import com.shi.xianglixiangqin.model.ShoppingCartChildGoodsModel;
import com.shi.xianglixiangqin.model.ShoppingCartParentGoodsModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.PreferencesUtilMy;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.shi.xianglixiangqin.view.FragmentOkAndCancelDialog;
import com.shi.xianglixiangqin.view.FragmentViewDialog;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ZHU
 * @action 购物车
 * @date 2015-7-18 上午11:27:58
 */
public class MainShoppingCartFragment extends MyBaseFragment<MyBaseActivity> implements SwipeRefreshLayout.OnRefreshListener, OnClickListener, OnConnectServerStateListener<Integer> {

    /**
     * 全选所有物品控件
     **/
    @BindView(R.id.tv_titleLeft)
    TextView tv_titleLeft;
    /**
     * 批量删除购物车商品控件
     **/
    @BindView(R.id.tv_titleRight)
    TextView tv_titleRight;
    /**
     * 标题
     **/
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    /**
     * 购物车ListView
     **/
    @BindView(R.id.listView_ShoppingCart)
    public ListView listView_ShoppingCart;

    /**
     * 购物车ListView适配器
     **/
    private ShoppingCartAdapter mShoppingCartAdapter;
    private List<ShoppingCartParentGoodsModel> listData = new ArrayList<ShoppingCartParentGoodsModel>();
    private List<ShoppingCartParentGoodsModel> listCacheData = new ArrayList<ShoppingCartParentGoodsModel>();

    /**
     * 购物车底部栏
     **/
    @BindView(R.id.linearLayout_bottom)
    LinearLayout linearLayout_bottom;
    /**
     * 购物车商品总价格TextView
     **/
    @BindView(R.id.tv_totalPrices)
    TextView tv_TotalPrices;
    /**
     * 提交数据文本控件
     **/
    @BindView(R.id.btn_submit)
    Button btn_submit;
    /**
     * 购物车数据为空
     **/
    private View viewListViewIsEmpty;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {

        tv_title.setText(R.string.shoppingCart);
        tv_titleLeft.setText("全选");
        tv_titleRight.setText("批量删除");


        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.RED
                , Color.GREEN
                , Color.BLUE
                , Color.YELLOW
                , Color.CYAN
                , 0xFFFE5D14
                , Color.MAGENTA);
        viewListViewIsEmpty = View.inflate(mActivity, R.layout.item_adapter_shopcart_list_view_empty, null);
        viewListViewIsEmpty.findViewById(R.id.btn_gotoHome).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.putExtra(InformationCodeUtil.IntentMainActivityCheckID,
                        R.id.linearLayout_home);
                startActivity(intent);
            }
        });
        if (listView_ShoppingCart.getHeaderViewsCount() == 0) {
            listView_ShoppingCart.addHeaderView(viewListViewIsEmpty);
        }
        mShoppingCartAdapter = new ShoppingCartAdapter(mActivity, listData);
        listView_ShoppingCart.setAdapter(mShoppingCartAdapter);
    }

    @Override
    public void initData() {
        connectSuccessFlag = true;
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getShopCartData();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            swipeRefreshLayout.setRefreshing(false);
        }else{
            if(!connectSuccessFlag){
                initData();
            }
        }
    }


    @OnClick({R.id.tv_titleLeft, R.id.tv_titleRight, R.id.btn_submit})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_titleLeft:
                if (listData != null && listData.size() > 0) {
                    String strFlag = tv_titleLeft.getText().toString();
                    boolean flagIfSelect = false;
                    if ("全选".equals(strFlag)) {
                        tv_titleLeft.setText(R.string.cancelSelectAll);
                        flagIfSelect = true;
                    } else {
                        tv_titleLeft.setText(R.string.tv_selectAllShoppingCart);
                        flagIfSelect = false;
                    }
                    for (int i = 0; i < listData.size(); i++) {
                        ShoppingCartParentGoodsModel parentGoodsModel = listData.get(i);
                        parentGoodsModel.IfSelect = flagIfSelect;
                        for (int j = 0; j < parentGoodsModel.getShoppingCarts().size(); j++) {
                            parentGoodsModel.getShoppingCarts().get(j).IfSelect = flagIfSelect;
                        }
                    }
                    mShoppingCartAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.tv_titleRight:
                List<ShoppingCartChildGoodsModel> listChild = new ArrayList<ShoppingCartChildGoodsModel>();
                for (int i = 0; i < listData.size(); i++) {
                    ShoppingCartParentGoodsModel parentGoodsModel = listData.get(i);
                    for (int j = 0; j < parentGoodsModel.getShoppingCarts().size(); j++) {
                        ShoppingCartChildGoodsModel childModel = parentGoodsModel
                                .getShoppingCarts().get(j);
                        if (childModel.IfSelect) {
                            listChild.add(childModel);
                        }
                    }
                }
                showDeleteDialog(listChild);
                break;

            case R.id.btn_submit:
                toConfirmOrderActivity();
                break;
        }
    }


    /**
     * 进入提交订单界面
     **/
    private void toConfirmOrderActivity() {
        List<ShoppingCartParentGoodsModel> list = new ArrayList<ShoppingCartParentGoodsModel>();
        for (int i = 0; i < listData.size(); i++) {
            ShoppingCartParentGoodsModel mParentModel = listData.get(i);
            ShoppingCartParentGoodsModel submitParentModel = new ShoppingCartParentGoodsModel();

            int j = 0;
            for (; j < mParentModel.getShoppingCarts().size(); j++) {
                ShoppingCartChildGoodsModel mChildModel = mParentModel.getShoppingCarts().get(j);
                if (mChildModel.IfSelect) {
                    submitParentModel.getShoppingCarts().add(mChildModel);
                }
            }
            if (submitParentModel.getShoppingCarts().size() != 0) {
                submitParentModel.IfSelect = true;
                submitParentModel.setDelayPayDays(mParentModel.getDelayPayDays());
                submitParentModel.setShopName(mParentModel.getShopName());
                submitParentModel.setShopID(mParentModel.getShopID());
                list.add(submitParentModel);
            }
        }
        if (list.size() == 0) {
            Toast.makeText(mActivity, "先选中几件商品再提交吧!", Toast.LENGTH_SHORT).show();
        } else {
//			EventBus.getDefault().postSticky(new ConfirmOrderActivityEvent<List<ShoppingCartParentGoodsModel>>(list));
            Intent intent = new Intent(mActivity, ConfirmOrderGeneralActivity.class);
            intent.putExtra(InformationCodeUtil.IntentConfirmOrderGeneralActivityGoodsList, (Serializable) list);
            mActivity.startActivity(intent);
        }
    }

    /**
     * 获取购物车商品
     **/
    private void getShopCartData() {

        if(InformationCodeUtil.AppName_DaiNiFei.equals(getResources().getText(R.string.app_name))) {
            String methodName = InformationCodeUtil.methodNameLoadShopCart;
            SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE
                    , methodName);
            requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mActivity).getDjLsh());
            requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mActivity).getOpenKey());
            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
                    (mActivity, this, requestSoapObject, methodName);
            connectCustomServiceAsyncTask.initProgressDialog(false);
            connectCustomServiceAsyncTask.execute();
        }else{
            String methodName = InformationCodeUtil.methodNameLoadShopCartExt;
            SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE
                    , methodName);
            requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mActivity).getDjLsh());
            requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mActivity).getOpenKey());
            requestSoapObject.addProperty("enterShopID", MyApplication.getmCustomModel(mActivity).getCurrentBrowsingShopID());
            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
                    (mActivity, this, requestSoapObject, methodName);
            connectCustomServiceAsyncTask.initProgressDialog(false);
            connectCustomServiceAsyncTask.execute();
        }

        tv_TotalPrices.setText("0.00");
        tv_titleLeft.setText(R.string.tv_selectAllShoppingCart);
    }

    /**
     * 修改购物车商品状况
     **/
    private void changeShopCatrData(ShoppingCartChildGoodsModel mCurrentGoodsModel) {
        final String methodName = InformationCodeUtil.methodNameChangeProductNum;
        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE
                , methodName);
        requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mActivity).getDjLsh());
        requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mActivity).getOpenKey());
        requestSoapObject.addProperty("ID", mCurrentGoodsModel.getId());
        requestSoapObject.addProperty("Quantity", mCurrentGoodsModel.getQuantity());
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
                (mActivity, this, requestSoapObject, methodName, mCurrentGoodsModel.getQuantity());
        connectCustomServiceAsyncTask.execute();
    }

    /**
     * 删除购物车商品数据
     **/
    private void deleteShopCartData(String ids) {
        LogUtil.LogShitou("商品序列" + ids);
        final String methodName = InformationCodeUtil.methodNameBatchDelShopCart;
        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE
                , methodName);
        requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mActivity).getDjLsh());
        requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mActivity).getOpenKey());
        requestSoapObject.addProperty("Ids", ids);
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
                (mActivity, this, requestSoapObject, methodName);
        connectCustomServiceAsyncTask.execute();
    }


    @Override
    public void onRefresh() {
        getShopCartData();
    }

    @Override
    public void connectServiceSuccessful(String returnString, String methodName, Integer state, boolean whetherRefresh) {
//        {"DjLsh":-1,"Msg":"[{\"DelayPayDays\":0,\"ShopID\":1561,\"ShopName\":\"全能批发王\",\"ShoppingCarts\":[]}]","Sign":1,"Tags":"","orderId":0}
        LogUtil.LogShitou("购物车数据", returnString);
        swipeRefreshLayout.setRefreshing(false);
        Gson gson = new Gson();

        //获取购物车数据成功
        if (InformationCodeUtil.methodNameLoadShopCart.equals(methodName) || InformationCodeUtil.methodNameLoadShopCartExt.equals(methodName)) {
            List<ShoppingCartParentGoodsModel> tempShopData = null;
            listData.clear();
            try {
                JSONResultMsgModel mJsonResult = gson.fromJson(returnString, JSONResultMsgModel.class);
                tempShopData = gson.fromJson(mJsonResult.getMsg(), new TypeToken<List<ShoppingCartParentGoodsModel>>() {
                }.getType());

            } catch (Exception e) {

            }

            if (tempShopData != null) {
                listData.addAll(tempShopData);
                for (int i = 0; i < listData.size(); i++) {
                    listData.get(i).IfSelect = false;
                }
            }
        }


        //修改或者删除购物车某个商品
        if (InformationCodeUtil.methodNameChangeProductNum == methodName
                || InformationCodeUtil.methodNameBatchDelShopCart == methodName) {

            try {
                JSONResultMsgModel mJsonResult = gson.fromJson(returnString, JSONResultMsgModel.class);
                if (mJsonResult.getSign() == 1) {
                    ToastUtil.show(mActivity, "" + mJsonResult.getMsg());
                    PreferencesUtilMy.saveShopCartAllGoods(mActivity, listData);
                } else {
                    ToastUtil.show(mActivity, "" + mJsonResult.getMsg());
                    listData.clear();
                    listData.addAll(listCacheData);
                }
            } catch (Exception e) {
                listData.clear();
                listData.addAll(listCacheData);
            }
        }

        if (listData.size() == 0) {
            if (listView_ShoppingCart.getHeaderViewsCount() == 0) {
                listView_ShoppingCart.addHeaderView(viewListViewIsEmpty);
            }
            PreferencesUtilMy.clearShopCartAllGoods(mActivity);
            tv_titleRight.setVisibility(View.INVISIBLE);
            tv_titleLeft.setVisibility(View.INVISIBLE);
            linearLayout_bottom.setVisibility(View.GONE);
        } else {
            PreferencesUtilMy.saveShopCartAllGoods(mActivity, listData);
            tv_titleLeft.setVisibility(View.VISIBLE);
            linearLayout_bottom.setVisibility(View.VISIBLE);
            listView_ShoppingCart.removeHeaderView(viewListViewIsEmpty);
        }
        mShoppingCartAdapter.notifyDataSetChanged();
    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Integer state, boolean whetherRefresh) {

        if (InformationCodeUtil.methodNameLoadShopCart == methodName) {
            ToastUtil.show(mActivity, returnStrError);
            listData.clear();
            listData.addAll(listCacheData);
            if(swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
            return;
        }

        if (InformationCodeUtil.methodNameBatchDelShopCart == methodName) {
            ToastUtil.show(mActivity, returnStrError);
            listData.clear();
            listData.addAll(listCacheData);
            return;
        }

        if (InformationCodeUtil.methodNameChangeProductNum == methodName) {
            ToastUtil.show(mActivity, returnStrError);
            listData.clear();
            listData.addAll(listCacheData);
        }

    }

    @Override
    public void connectServiceCancelled(String returnString, String methodName, Integer state, boolean whetherRefresh) {

        if (InformationCodeUtil.methodNameBatchDelShopCart == methodName
                || InformationCodeUtil.methodNameChangeProductNum == methodName) {

            try {
                Gson gson = new Gson();
                JSONResultMsgModel mJsonResult = gson.fromJson(returnString, JSONResultMsgModel.class);
                if (mJsonResult.getSign() == 1) {
                    PreferencesUtilMy.saveShopCartAllGoods(mActivity, listData);
                } else {
                    listData.clear();
                    listData.addAll(listCacheData);
                }
            } catch (Exception e) {
                listData.clear();
                listData.addAll(listCacheData);
            }

            if (listData.size() == 0) {
                if (listView_ShoppingCart.getHeaderViewsCount() == 0) {
                    listView_ShoppingCart.addHeaderView(viewListViewIsEmpty);
                }
                PreferencesUtilMy.clearShopCartAllGoods(mActivity);
                tv_titleRight.setVisibility(View.INVISIBLE);
                tv_titleLeft.setVisibility(View.INVISIBLE);
                linearLayout_bottom.setVisibility(View.GONE);
            } else {
                PreferencesUtilMy.saveShopCartAllGoods(mActivity, listData);
                tv_titleLeft.setVisibility(View.VISIBLE);
                linearLayout_bottom.setVisibility(View.VISIBLE);
                listView_ShoppingCart.removeHeaderView(viewListViewIsEmpty);
            }
            mShoppingCartAdapter.notifyDataSetChanged();
        }

    }


    /***
     * 购物车适配器
     *
     * @author SHI
     *         2016-2-17 15:44:41
     */
    public class ShoppingCartAdapter extends MyBaseAdapter<ShoppingCartParentGoodsModel> {

        public ShoppingCartAdapter(Context mContext,
                                  List<ShoppingCartParentGoodsModel> listData) {
            super(mContext, listData);
        }

        private class ViewHolderParent {
            /**
             * 商品是否选中
             **/
            private ImageView iv_select;
            /**
             * 商品标题
             **/
            private TextView tv_shopName;
            /**
             * 套餐/颜色
             **/
            private TextView tv_paymentDays;
            /**
             * 商品列表详情
             **/
            private LinearLayout linearLayout_goodsList;
        }

        private class ViewHolderChild {
            /**
             * 商品是否选中
             **/
            private ImageView iv_select;
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
            private TextView tv_goodsPackageColor;
            /**
             * 商品价格
             **/
            private TextView tv_goodsPrice;
            /**
             * 修改商品数量
             **/
            private LinearLayout linearLayout_changeGoodsNum;
            /**
             * 商品数量
             **/
            private TextView tv_goodsNum;
            /**
             * 删除按钮
             **/
            private ImageButton ib_deleteGoods;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            final ViewHolderParent viewHolderParent;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_adapter_shopcart_list_view_parent, null);
                viewHolderParent = new ViewHolderParent();
                viewHolderParent.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
                viewHolderParent.tv_shopName = (TextView) convertView.findViewById(R.id.tv_shopName);
                viewHolderParent.tv_paymentDays = (TextView) convertView.findViewById(R.id.tv_paymentDays);
                viewHolderParent.linearLayout_goodsList = (LinearLayout) convertView.findViewById(R.id.linearLayout_goodsList);

                convertView.setTag(viewHolderParent);
            } else {
                viewHolderParent = (ViewHolderParent) convertView.getTag();
                viewHolderParent.linearLayout_goodsList.removeAllViews();
            }
            /**当前ListView的Item**/
            final ShoppingCartParentGoodsModel mParentGoodsModel = listData.get(position);
            final List<ShoppingCartChildGoodsModel> listChilds = mParentGoodsModel.getShoppingCarts();

            viewHolderParent.tv_shopName.setText(mParentGoodsModel.getShopName());
            if (mParentGoodsModel.getDelayPayDays() == 0) {
                viewHolderParent.tv_paymentDays.setText("无账期");
            } else {
                viewHolderParent.tv_paymentDays.setText(mParentGoodsModel.getDelayPayDays() + "天账期");
            }
            //设置每行商品是否选中
//            for (int i = 0; i < listChilds.size(); i++) {
//                listChilds.get(i).IfSelect = mParentGoodsModel.IfSelect;
//            }
            if (mParentGoodsModel.IfSelect) {
                viewHolderParent.iv_select.setImageResource(R.drawable.icon_select_on_02);
            } else {
                viewHolderParent.iv_select.setImageResource(R.drawable.icon_select_off_02);
            }
            viewHolderParent.iv_select.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mParentGoodsModel.IfSelect = !mParentGoodsModel.IfSelect;
                    for (int i = 0; i < listChilds.size(); i++) {
                        listChilds.get(i).IfSelect = mParentGoodsModel.IfSelect;
                    }
                    mShoppingCartAdapter.notifyDataSetChanged();
                }
            });


            for (int i = 0; i < listChilds.size(); i++) {

                final ShoppingCartChildGoodsModel mChildGoodsModel = listChilds.get(i);

                final ViewHolderChild viewHolderChild = new ViewHolderChild();

                View view = View.inflate(mContext, R.layout.item_adapter_shopcart_list_view_child, null);
                viewHolderParent.linearLayout_goodsList.addView(view);

                viewHolderChild.iv_goodsImages = (ImageView) view.findViewById(R.id.iv_goodsImages);
                viewHolderChild.tv_goodsName = (TextView) view.findViewById(R.id.tv_goodsName);
                viewHolderChild.tv_goodsPackageColor = (TextView) view.findViewById(R.id.tv_goodsPackageColor);
                viewHolderChild.tv_goodsPrice = (TextView) view.findViewById(R.id.tv_goodsPrice);
                viewHolderChild.iv_select = (ImageView) view.findViewById(R.id.iv_select);
                viewHolderChild.tv_goodsNum = (TextView) view.findViewById(R.id.tv_goodsNum);
                viewHolderChild.linearLayout_changeGoodsNum = (LinearLayout) view.findViewById(R.id.linearLayout_changeGoodsNum);
                viewHolderChild.ib_deleteGoods = (ImageButton) view.findViewById(R.id.ib_deleteGoods);


                //设置商品购买数量
                viewHolderChild.tv_goodsNum.setText("" + mChildGoodsModel.getQuantity());
                //设置每行商品是否选中
                if (mChildGoodsModel.IfSelect) {
                    viewHolderChild.iv_select.setImageResource(R.drawable.icon_select_on_02);
                } else {
                    viewHolderChild.iv_select.setImageResource(R.drawable.icon_select_off_02);
                }
                //商品是否选中响应
                viewHolderChild.iv_select.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mChildGoodsModel.IfSelect) {
                            mChildGoodsModel.IfSelect = false;
                            mParentGoodsModel.IfSelect = false;
                            mShoppingCartAdapter.notifyDataSetChanged();
                        } else {
                            mChildGoodsModel.IfSelect = true;
                            int i;
                            for (i = 0; i < listChilds.size(); i++) {
                                if (listChilds.get(i).IfSelect != true) {
                                    break;
                                }
                            }
                            if (i == listChilds.size()) {
                                mParentGoodsModel.IfSelect = true;
                            }
                            mShoppingCartAdapter.notifyDataSetChanged();
                        }
                    }

                });
                //设置商品名称和图片
                viewHolderChild.tv_goodsName.setText("" + mChildGoodsModel.getProductName());
                ImagerLoaderUtil.getInstance(mContext).displayMyImage(mChildGoodsModel.getImgUrl(), viewHolderChild.iv_goodsImages);
                viewHolderChild.iv_goodsImages.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, GoodsDetailGeneralActivity.class);
                        intent.putExtra(InformationCodeUtil.IntentGoodsID, mChildGoodsModel.getProductId());
                        mActivity.startActivity(intent);
                    }
                });
                //设置商品价格和颜色
                DecimalFormat dcmFmt = new DecimalFormat("0.00");
                viewHolderChild.tv_goodsPrice.setText("¥ " + dcmFmt.format(mChildGoodsModel.getUnitPrice()));
                viewHolderChild.tv_goodsPackageColor.setText(mChildGoodsModel.getPackageName() + " / " + mChildGoodsModel.getColorName());
                //商品数量点击响应
                viewHolderChild.linearLayout_changeGoodsNum.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showChangeGoodsNum(mChildGoodsModel, viewHolderChild);
                    }
                });

                viewHolderChild.ib_deleteGoods.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        List<ShoppingCartChildGoodsModel> listChild = new ArrayList<ShoppingCartChildGoodsModel>();
                        listChild.add(mChildGoodsModel);
                        showDeleteDialog(listChild);
                    }
                });
            }

            getCurrentShoppingCartTotalPrice();

            return convertView;
        }

        private void showChangeGoodsNum(final ShoppingCartChildGoodsModel mChildGoodsModel, final ViewHolderChild viewHolderChild) {
            View view = View.inflate(mContext, R.layout.dialog_shoppingcart_changenum, null);
            final FragmentViewDialog fdialog = new FragmentViewDialog();

            fdialog.initView(view);

            final EditText shoppingCartNumber = (EditText) view.findViewById(R.id.shoppingcart_goods_number);
            shoppingCartNumber.setText("" + mChildGoodsModel.getQuantity());
            ImageView btnSub = (ImageView) view.findViewById(R.id.shoppingcart_goods_sub);
            ImageView btnAdd = (ImageView) view.findViewById(R.id.shoppingcart_goods_add);
            ImageView btnOk = (ImageView) view.findViewById(R.id.btn_ok);
            ImageView btnCancel = (ImageView) view.findViewById(R.id.btn_cancel);


            btnSub.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    subDialogProductNumber(viewHolderChild, shoppingCartNumber);
                }
            });
            btnAdd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDialogProductNumber(viewHolderChild, shoppingCartNumber);
                }
            });

            btnOk.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String num = shoppingCartNumber.getText().toString();
                    int number = 1;
                    try {
                        number = Integer.parseInt(num);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (number >= 1) {
                        listCacheData.clear();
                        listCacheData.addAll(listData);
                        mChildGoodsModel.setQuantity(number);
                        changeShopCatrData(mChildGoodsModel);
                        fdialog.dismiss();
                    }

                }
            });
            btnCancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    fdialog.dismiss();
                }
            });

            fdialog.show(getFragmentManager(), "fragmentDailog");
        }


        private void addDialogProductNumber(ViewHolderChild viewHolderChild, EditText shoppingCartNumber) {
            String num = shoppingCartNumber.getText().toString();
            int number = 1;
            try {
                number = Integer.parseInt(num);
                number++;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            shoppingCartNumber.setText("" + number);
        }

        private void subDialogProductNumber(ViewHolderChild viewHolderChild, EditText shoppingCartNumber) {
            String num = shoppingCartNumber.getText().toString();
            int number = 1;
            try {
                number = Integer.parseInt(num);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (number > 1)
                number--;
            shoppingCartNumber.setText("" + number);
        }
    }


    /**
     * 批量删除购物车商品
     **/
    private void showDeleteDialog(final List<ShoppingCartChildGoodsModel> listChild) {

        final FragmentOkAndCancelDialog fragmentDailog = new FragmentOkAndCancelDialog();
        fragmentDailog.initView("提示", "确认删除?", "取消", "确定",
                new FragmentOkAndCancelDialog.OnButtonClickListener() {

                    @Override
                    public void OnOkClick() {

                        //产生要删除条目的ID字符串，并删除对应的数据对象
                        listCacheData.clear();
                        listCacheData.addAll(listData);
                        int index = -1;
                        StringBuilder sb = new StringBuilder();
                        int i;
                        for (i = 0; i < listChild.size(); i++) {
                            ShoppingCartChildGoodsModel model = listChild.get(i);
                            if (index != -1) {
                                sb.append(",");
                            }
                            sb.append(model.getId());
                            index = i;
                            for (int j = 0; j < listData.size(); j++) {
                                listData.get(j).getShoppingCarts().remove(model);
                            }
                        }

                        List<ShoppingCartParentGoodsModel> listWillRemoveParentGoodsModel = new ArrayList<ShoppingCartParentGoodsModel>();
                        for (i = 0; i < listData.size(); i++) {
                            if (listData.get(i).getShoppingCarts().size() == 0) {
                                listWillRemoveParentGoodsModel.add(listData.get(i));
                            }
                        }
                        for (i = 0; i < listWillRemoveParentGoodsModel.size(); i++) {
                            listData.remove(listWillRemoveParentGoodsModel.get(i));
                        }
                        //开始调用网络删除商品
                        deleteShopCartData(sb.toString());
                    }

                    @Override
                    public void OnCancelClick() {
                    }
                });
        fragmentDailog.show(getFragmentManager(), "fragmentDailog");
    }


    /**
     * 获取当前购物车商品总价格
     */
    private void getCurrentShoppingCartTotalPrice() {
        Double totalPrices = 0.00;
        for (int i = 0; i < listData.size(); i++) {
            ShoppingCartParentGoodsModel model = listData.get(i);
            List<ShoppingCartChildGoodsModel> list = model.getShoppingCarts();
            for (int j = 0; j < list.size(); j++) {
                ShoppingCartChildGoodsModel mChildGoodsModel = list.get(j);
                if (mChildGoodsModel.IfSelect) {
                    totalPrices += mChildGoodsModel.getUnitPrice() * mChildGoodsModel.getQuantity();
                }
            }
        }
        if (totalPrices == 0) {
            tv_titleRight.setVisibility(View.INVISIBLE);
        } else {
            tv_titleRight.setVisibility(View.VISIBLE);
        }
        tv_TotalPrices.setText("￥" + StringUtil.doubleToString(totalPrices, "0.00"));
    }

}









