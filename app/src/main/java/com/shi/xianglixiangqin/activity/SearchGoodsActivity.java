package com.shi.xianglixiangqin.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afinalstone.androidstudy.swiperefreshview.OnSwipeRefreshViewListener;
import com.afinalstone.androidstudy.swiperefreshview.SwipeRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.GoodsClassSearchAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultBaseModel;
import com.shi.xianglixiangqin.model.GoodsGeneralModel;
import com.shi.xianglixiangqin.model.GoodsPackageModel;
import com.shi.xianglixiangqin.model.GoodsPackageStandardModel;
import com.shi.xianglixiangqin.model.ShoppingCartChildGoodsModel;
import com.shi.xianglixiangqin.model.ShoppingCartParentGoodsModel;
import com.shi.xianglixiangqin.model.ShoppingCartWrapperModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.PreferencesUtilMy;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.shi.xianglixiangqin.view.GoodsAgentConfirmPopWindow;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * @author SHI
 * @action 商品搜索详情界面(带你飞和聚合批发系统共用)
 * @date 2016-2-18 10:44:53
 * 需要店铺currentShopID本界面需要使用),店铺用户currentShopUserID(为了下个界面商品分类界面使用)
 */
public class SearchGoodsActivity extends MyBaseActivity implements
        OnClickListener, OnConnectServerStateListener<List<ShoppingCartParentGoodsModel>>,
        OnSwipeRefreshViewListener {
    /**
     * 后退控件
     **/
    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    /**
     * 页面标题设置为店铺商品分类
     **/
    @BindView(R.id.tv_title)
    TextView tv_title;
    /**
     * 页面标题设置为店铺商品分类
     **/
    @BindView(R.id.iv_titleRight)
    ImageView iv_titleRight;
    /**
     * 搜索内容
     **/
    @BindView(R.id.et_searchContext)
    EditText et_searchContext;
    /**
     * 搜索按钮
     **/
    @BindView(R.id.tv_search)
    TextView tv_search;
    /**
     * 筛选 按钮控件
     **/
    @BindView(R.id.tv_sorting)
    TextView tv_sorting;
    /**
     * 外围的Group控件
     **/
    @BindView(R.id.rg_sortType)
    RadioGroup rg_sortType;
    /**
     * 外围的Group控件
     **/
    @BindView(R.id.rb_defaultSort)
    RadioButton rb_defaultSort;
    /**
     * 搜索结果列表
     **/
    @BindView(R.id.swipeRefreshListView)
    SwipeRefreshListView swipeRefreshListView;
    /**
     * 数据源
     **/
    private List<GoodsGeneralModel> listData = new ArrayList<GoodsGeneralModel>();
    /**
     * 适配器
     **/
    private GoodsClassSearchAdapter shopClassSearchAdapter;
    /**
     * 选择全部
     **/
    @BindView(R.id.cb_selectAll)
    CheckBox cb_selectAll;
    /**
     * 批量代理按钮
     **/
    @BindView(R.id.btn_agentGoods)
    Button btn_agentGoods;
    /**
     * 加入购物车
     **/
    @BindView(R.id.btn_addToShopCart)
    Button btn_addToShopCart;


    // 网络请求相关
    private final int defaultSortFlag = 0;
    private final int salesSortFlag = 1;
    private final int priceSortFlag = 2;
    /**
     * 搜索商品需要用到的字段
     **/
    private int currentShopID;
    private int currentFilterClassID;
    private String currentFilterFilterWereJson = "";
    private String currentFilterKeyWord = "";
    private int currentOrderIndex = defaultSortFlag;
    private int currentPageIndex = 1;
    private final int SizeOfSearchPage = 10;
    /**
     * 当前需要批量加价的商品ID
     **/
    private String goodsIDS;
    /**
     * 筛选页面请求码
     **/
    private int RequestCode_Filter = 1;

    private View rootView;

    public void initView() {
        rootView = View.inflate(mContext, R.layout.activity_search_goods, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        iv_titleRight.setVisibility(View.VISIBLE);
        iv_titleRight.setImageResource(R.drawable.icon_to_shopcart_02);
        tv_title.setText(R.string.SearchGoods_title);
        rg_sortType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    // 综合排序
                    case R.id.rb_defaultSort:
                        listData.clear();
                        shopClassSearchAdapter.notifyDataSetChanged();
                        currentOrderIndex = defaultSortFlag;
                        swipeRefreshListView.openRefreshState();
                        break;
                    // 销量排序
                    case R.id.rb_salesSort:
                        listData.clear();
                        shopClassSearchAdapter.notifyDataSetChanged();
                        currentOrderIndex = salesSortFlag;
                        swipeRefreshListView.openRefreshState();
                        break;
                    // 价格排序
                    case R.id.rb_priceSort:
                        listData.clear();
                        shopClassSearchAdapter.notifyDataSetChanged();
                        currentOrderIndex = priceSortFlag;
                        swipeRefreshListView.openRefreshState();
                        break;
                    default:
                        break;
                }
            }
        });
        cb_selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < listData.size(); i++) {
                    listData.get(i).setWhetherSelect(isChecked);
                }
                shopClassSearchAdapter.notifyDataSetChanged();
            }
        });
    }

    public void initData() {

        Intent intent = getIntent();
        currentFilterClassID = intent.getIntExtra(InformationCodeUtil.IntentSearchGoodsFilterClassID, -1);
        currentShopID = getIntent().getIntExtra(InformationCodeUtil.IntentSearchGoodsCurrentShopID, -1);
        if (currentFilterClassID > 0) {
            tv_sorting.setText(R.string.SearchGoods_tvSorting);
        }
        shopClassSearchAdapter = new GoodsClassSearchAdapter(this, listData);
        swipeRefreshListView.getListView().setAdapter(shopClassSearchAdapter);
        swipeRefreshListView.getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                GoodsGeneralModel mProductModel = listData.get(position);
                Intent intent = new Intent(SearchGoodsActivity.this, GoodsDetailGeneralActivity.class);
                intent.putExtra(InformationCodeUtil.IntentGoodsID, Integer.valueOf(mProductModel.getDjLsh()));
                startActivity(intent);
            }
        });
        swipeRefreshListView.setOnRefreshListener(this);
        swipeRefreshListView.IfOpenBottomRefresh(true);
        rb_defaultSort.setChecked(true);
    }

    private void getData(boolean IfRefresh) {

        String methodName = InformationCodeUtil.methodNameGetGoodsList;
        if (IfRefresh) {
            listData.clear();
            shopClassSearchAdapter.notifyDataSetChanged();
            currentPageIndex = 1;
        } else {
            currentPageIndex++;
        }
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("pageSize", SizeOfSearchPage);
        soapObject.addProperty("pageIndex", currentPageIndex);
        soapObject.addProperty("classID", currentFilterClassID);
        soapObject.addProperty("filterIDS", "");
        soapObject.addProperty("shopID", currentShopID);
        soapObject.addProperty("keyWord", currentFilterKeyWord);
        soapObject.addProperty("orderIndex", currentOrderIndex);
        soapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
        if (InformationCodeUtil.AppName_DaiNiFei.equals(getResources().getText(R.string.app_name))) {
            soapObject.addProperty("cityCode", MyApplication.getmCustomModel(mContext).getLocCityCode());
        }
        soapObject.addProperty("wherejson", currentFilterFilterWereJson);
        LogUtil.LogShitou(soapObject.toString());
        ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
                mContext, this, soapObject, methodName, IfRefresh);
        connectGoodsServiceAsyncTask.initProgressDialog(false);
        connectGoodsServiceAsyncTask.execute();
    }

    /**
     * 筛选结束 根据筛选结果重新请求产品数据
     **/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            listData.clear();
            shopClassSearchAdapter.notifyDataSetChanged();
            currentFilterFilterWereJson = data.getStringExtra(InformationCodeUtil.IntentFilterActivitySelectFilter);
            swipeRefreshListView.openRefreshState();
        }
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_sorting, R.id.tv_search
            , R.id.btn_agentGoods, R.id.btn_addToShopCart, R.id.iv_titleRight})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            // 筛选按钮
            case R.id.tv_sorting:
                if ("筛选".equals(tv_sorting.getText())) {
                    intent = new Intent(this, FilterFirstActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentFilterFirstActivityFilterClassID, currentFilterClassID);
                    startActivityForResult(intent, RequestCode_Filter);
                } else {
                    intent = new Intent(this, SearchGoodsClassTypeActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentSearchGoodsCurrentShopID, currentShopID);
                    startActivity(intent);
                }
                break;
            // 搜索按钮
            case R.id.tv_search:
                currentFilterKeyWord = et_searchContext.getText().toString().trim();
                swipeRefreshListView.openRefreshState();
                break;
            // 代理商品
            case R.id.btn_agentGoods:
                agentGoods();
                break;
            // 把所选商品加入购物车
            case R.id.btn_addToShopCart:
                addGoodsToShoppingCart();
                break;
            // 进入购物车
            case R.id.iv_titleRight:
                intent = new Intent(mContext, ShoppingCartActivity.class);
                intent.putExtra(InformationCodeUtil.IntentSearchGoodsCurrentShopID, currentShopID);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 代理商品
     **/
    private void agentGoods() {

        if (listData == null || listData.size() == 0) {
            ToastUtil.show(mContext, "请至少选择一件需要代理的商品");
            return;
        }
        StringBuffer str = new StringBuffer("");
        int startIndex = -1;
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).whetherSelect) {
                if (startIndex == -1) {
                    startIndex = i;
                }
                if (i != startIndex) {
                    str.append(",");
                }
                str.append(listData.get(i).getDjLsh());
            }
        }
        goodsIDS = str.toString();
        if (TextUtils.isEmpty(goodsIDS)) {
            ToastUtil.show(mContext, "请至少选择一件需要代理的商品");
        } else {
            showDialogAgentConfirm();
        }
    }

    /**
     * 弹出代理商品确认界面
     **/
    private void showDialogAgentConfirm() {
        GoodsAgentConfirmPopWindow pop = new GoodsAgentConfirmPopWindow(mContext,
                new GoodsAgentConfirmPopWindow.onAgentConfirmListener() {
                    @Override
                    public void agentGoods(int gowhere) {
                        toAgentGoods(gowhere);
                    }
                });
        pop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
    }

    /**
     * 代理商品
     **/
    public void toAgentGoods(int gowhere) {

        String methodName = InformationCodeUtil.methodNameDelegateShopAllGoods;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
        soapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
        soapObject.addProperty("shopID", MyApplication.getmCustomModel(mContext).getCurrentBrowsingShopID());
        soapObject.addProperty("goodsIDS", currentShopID);
        soapObject.addProperty("gowhere", gowhere);
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
                (mContext, this, soapObject, methodName);
        connectCustomServiceAsyncTask.initProgressDialog(true, "正在代理...");
        connectCustomServiceAsyncTask.execute();
    }

    /**
     * 添加商品到服务器到购物车中
     **/
    public void addGoodsToShoppingCart() {
        if (listData == null || listData.size() == 0) {
            ToastUtil.show(mContext, "请至少选择一件需要加入购物车的商品");
            return;
        }

        List<ShoppingCartWrapperModel> listShoppingCartWrapperModel = new ArrayList<ShoppingCartWrapperModel>();
        List<ShoppingCartParentGoodsModel> listShoppingCartParentGoodsModel = new ArrayList<ShoppingCartParentGoodsModel>();
        //再把当前选中的套餐添加到购物车数据中
        for (int i = 0; i < listData.size(); i++) {
            GoodsGeneralModel tempGoods = listData.get(i);
            if (tempGoods.whetherSelect && 0 != tempGoods.getDefaultGoodsPackage().getDefaultPackageColor().getStoneCount()) {

                //本地缓存
                GoodsPackageModel tempPackage = tempGoods.getDefaultGoodsPackage();
                GoodsPackageStandardModel tempPackageColor = tempGoods.getDefaultGoodsPackage().getDefaultPackageColor();
                ShoppingCartChildGoodsModel currentChildGoodsModel = new ShoppingCartChildGoodsModel
                        (0, tempGoods.getDjLsh(), tempGoods.getGoodsName(), tempGoods.getImgUrl()
                                , tempPackage.getDjLsh(), tempPackage.getPackageName()
                                , tempPackageColor.getDjLsh(), tempPackageColor.getColorName()
                                , 1, tempPackageColor.getPrice(), tempPackageColor.getTaxPrice()
                                , tempPackageColor.getFlyCoin(), tempPackageColor.getTaxFlyCoin(), 0.00);
                List<ShoppingCartChildGoodsModel> tempList = new ArrayList<ShoppingCartChildGoodsModel>();
                tempList.add(currentChildGoodsModel);
                ShoppingCartParentGoodsModel currentParentGoodsModel = new ShoppingCartParentGoodsModel();
                currentParentGoodsModel.setShopID(tempGoods.getShopID());
                currentParentGoodsModel.setShoppingCarts(tempList);
                listShoppingCartParentGoodsModel.add(currentParentGoodsModel);

                //接口服务需要使用
                ShoppingCartWrapperModel shoppingCartWrapperModel = new ShoppingCartWrapperModel(
                        tempGoods.getDjLsh(), tempGoods.getDefaultGoodsPackage().getDjLsh()
                        , tempGoods.getDefaultGoodsPackage().getDefaultPackageColor().getDjLsh(), 1);
                listShoppingCartWrapperModel.add(shoppingCartWrapperModel);
            }
        }
        Gson gson = new Gson();
        String methodName = InformationCodeUtil.methodNameBatchAddShopCart;
        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
        requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
        requestSoapObject.addProperty("shopCartWappers", gson.toJson(listShoppingCartWrapperModel));

        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
                (mContext, this, requestSoapObject, methodName, listShoppingCartParentGoodsModel);
        connectCustomServiceAsyncTask.execute();
        return;
    }

    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, List<ShoppingCartParentGoodsModel> state, boolean whetherRefresh) {
        // LogUtils.LogShitou("returnSoapObject", returnSoapObject.toString());
        if (methodName == InformationCodeUtil.methodNameGetGoodsList) {
            try {
                refreshListData(returnString, methodName, whetherRefresh);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        if (methodName == InformationCodeUtil.methodNameDelegateShopAllGoods) {
            try {
                JSONObject json = new JSONObject(returnString);
                ToastUtil.show(mContext, json.getString("Msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
        if (methodName == InformationCodeUtil.methodNameBatchAddShopCart) {
            try {
                JSONObject json = new JSONObject(returnString);
                int sign = json.getInt("Sign");
                if (sign == 1) {
                    PreferencesUtilMy.addGoodsToShopCart(mContext, state);
                    ToastUtil.show(mContext, "商品成功加入购物车");
                } else {
                    ToastUtil.show(mContext, json.getString("Msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshListData(String returnString,
                                 String methodName, boolean whetherRefresh) {
        Gson gson = new Gson();
        List<GoodsGeneralModel> list = null;
        try {
            JSONResultBaseModel<GoodsGeneralModel> mJSONMyAgent = gson
            .fromJson(returnString,new TypeToken<JSONResultBaseModel<GoodsGeneralModel>>() {}.getType());
            list = mJSONMyAgent.getList();
        } catch (Exception e) {
            if (!whetherRefresh) {
                currentPageIndex--;
            }
        }
        if (list == null || list.size() == 0) {
            if (whetherRefresh) {
                ToastUtil.show(mContext, "未搜到符合条件的商品");
            } else {
                ToastUtil.show(mContext, "暂无更多商品数据");
            }
        } else {
            cb_selectAll.setChecked(false);
            for (int i = 0; i < list.size(); i++) {
                List<String> images = new ArrayList<String>();
                images.add(list.get(i).getImgUrl());
                list.get(i).setImages(images);
            }
            listData.addAll(list);
        }
        if (swipeRefreshListView != null) {
            shopClassSearchAdapter.notifyDataSetChanged();
            swipeRefreshListView.closeRefreshState();
        }
    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, List<ShoppingCartParentGoodsModel> state,
                                     boolean whetherRefresh) {

        if (methodName == InformationCodeUtil.methodNameGetGoodsList) {
            if (!whetherRefresh) {
                currentPageIndex--;
            }
            if (swipeRefreshListView != null)
                swipeRefreshListView.closeRefreshState();
            ToastUtil.show(this, returnStrError);
            return;
        }
        if (methodName == InformationCodeUtil.methodNameDelegateShopAllGoods) {
            ToastUtil.show(this, returnStrError);
            return;
        }
        if (methodName == InformationCodeUtil.methodNameBatchAddShopCart) {
            ToastUtil.show(this, returnStrError);
        }

    }

    @Override
    public void connectServiceCancelled(String returnString,
                                        String methodName, List<ShoppingCartParentGoodsModel> state, boolean whetherRefresh) {

        if (methodName == InformationCodeUtil.methodNameBatchAddShopCart) {
            try {
                JSONObject json = new JSONObject(returnString);
                int sign = json.getInt("Sign");
                if (sign == 1) {
                    PreferencesUtilMy.addGoodsToShopCart(mContext, state);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
    public void onBackPressed() {
        finish();
    }
}
