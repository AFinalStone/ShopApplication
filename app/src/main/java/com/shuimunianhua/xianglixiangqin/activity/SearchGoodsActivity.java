package com.shuimunianhua.xianglixiangqin.activity;

import android.content.Intent;
import android.text.TextUtils;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.afinalstone.androidstudy.swiperefreshview.OnSwipeRefreshViewListener;
import com.afinalstone.androidstudy.swiperefreshview.SwipeRefreshListView;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.adapter.GoodsClassSearchAdapter;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultBaseModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsGeneralModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsPackageColorModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsPackageModel;
import com.shuimunianhua.xianglixiangqin.model.ShoppingCartChildGoodsModel;
import com.shuimunianhua.xianglixiangqin.model.ShoppingCartParentGoodsModel;
import com.shuimunianhua.xianglixiangqin.model.ShoppingCartWrapperModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.PreferencesUtilMy;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;
import com.shuimunianhua.xianglixiangqin.view.FragmentViewDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    @Bind(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    /**
     * 页面标题设置为店铺商品分类
     **/
    @Bind(R.id.tv_title)
    TextView tv_title;
    /**
     * 搜索内容
     **/
    @Bind(R.id.et_searchContext)
    EditText et_searchContext;
    /**
     * 搜索按钮
     **/
    @Bind(R.id.tv_search)
    TextView tv_search;
    /**
     * 筛选 按钮控件
     **/
    @Bind(R.id.tv_sorting)
    TextView tv_sorting;
    /**
     * 外围的Group控件
     **/
    @Bind(R.id.rg_sortType)
    RadioGroup rg_sortType;
    /**
     * 外围的Group控件
     **/
    @Bind(R.id.rb_defaultSort)
    RadioButton rb_defaultSort;
    /**
     * 搜索结果列表
     **/
    @Bind(R.id.swipeRefreshListView)
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
    @Bind(R.id.cb_selectAll)
    CheckBox cb_selectAll;
    /**
     * 批量代理按钮
     **/
    @Bind(R.id.btn_agentGoods)
    Button btn_agentGoods;
    /**
     * 加入购物车
     **/
    @Bind(R.id.btn_addToShopCart)
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
    private String currentFilterFilterIDS = "";
    private String currentFilterKeyWord = "";
    private int currentOrderIndex = defaultSortFlag;
    private int currentPageIndex = 1;
    private final int SizeOfSearchPage = 10;
    /**
     * 当前Activity是否是从底部弹出来的
     **/
    boolean isFromBottom;
    /**
     * 当前需要批量加价的商品ID
     **/
    private String goodsIDS;
    /**代理界面**/
    private FragmentViewDialog fvd;
    /**筛选页面请求码**/
    private int RequestCode_Filter = 1;

    public void initView() {
        setContentView(R.layout.activity_search_goods);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        currentFilterClassID = intent.getIntExtra(InformationCodeUtil.IntentSearchGoodsFilterClassID, -1);
        if (currentFilterClassID > 0) {
            tv_sorting.setText("筛选");
        }
        isFromBottom = intent.getBooleanExtra(InformationCodeUtil.IntentSearchGoodsInFromBottom, false);
        currentShopID = getIntent().getIntExtra(InformationCodeUtil.IntentSearchGoodsCurrentShopID,-1);

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
        iv_titleLeft.setOnClickListener(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText("产品搜索");
        tv_search.setOnClickListener(this);
        tv_sorting.setOnClickListener(this);
        btn_agentGoods.setOnClickListener(this);
        btn_addToShopCart.setOnClickListener(this);
        cb_selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < listData.size(); i++) {
                    listData.get(i).setWhetherSelect(isChecked);
                }
                shopClassSearchAdapter.notifyDataSetChanged();
            }
        });

        shopClassSearchAdapter = new GoodsClassSearchAdapter(this, listData);
        swipeRefreshListView.getListView().setAdapter(
                shopClassSearchAdapter);
        swipeRefreshListView.getListView().setOnItemClickListener(
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        GoodsGeneralModel mProductModel = listData
                                .get(position);
                        Intent intent = new Intent(
                                SearchGoodsActivity.this,
                                GoodsDetailGeneralActivity.class);
                        intent.putExtra(InformationCodeUtil.IntentGoodsID,
                                Integer.valueOf(mProductModel.getDjLsh()));
                        SearchGoodsActivity.this.startActivity(intent);

                    }
                });
        swipeRefreshListView.setOnRefreshListener(this);
    }

    public void initData() {
        rb_defaultSort.setChecked(true);
    }

    private void getData(String methodName, boolean IfRefrush) {

        if (methodName == InformationCodeUtil.methodNameDelegateShopAllGoods) {
            SoapObject soapObject = new SoapObject(
                    ConnectServiceUtil.NAMESPACE, methodName);
            soapObject.addProperty("customID",
                    MyApplication.getmCustomModel(mContext).getDjLsh());
            soapObject.addProperty("openKey",
                    MyApplication.getmCustomModel(mContext).getOpenKey());
            soapObject.addProperty("shopID", currentShopID);
            soapObject.addProperty("goodsIDS", goodsIDS);

            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                    mContext, this, soapObject, methodName);
            connectCustomServiceAsyncTask.execute();
            return;
        }
        if (methodName == InformationCodeUtil.methodNameGetGoodsList) {

            // 如果是请求刷新数据，则把pageIndex置为1
            if (IfRefrush) {
                currentPageIndex = 1;
                listData.clear();
                shopClassSearchAdapter.notifyDataSetChanged();
            } else {
                currentPageIndex++;
            }
            SoapObject soapObject = new SoapObject(
                    ConnectServiceUtil.NAMESPACE, methodName);
            soapObject.addProperty("pageSize", SizeOfSearchPage);
            soapObject.addProperty("pageIndex", currentPageIndex);
            soapObject.addProperty("classID", currentFilterClassID);
            soapObject.addProperty("filterIDS", currentFilterFilterIDS);
            soapObject.addProperty("shopID", currentShopID);
            soapObject.addProperty("keyWord", currentFilterKeyWord);
            soapObject.addProperty("orderIndex", currentOrderIndex);
            soapObject.addProperty("customID",
                    MyApplication.getmCustomModel(mContext).getDjLsh());
            soapObject.addProperty("cityCode",
                    MyApplication.getmCustomModel(mContext).getLocCityCode());

            ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
                    mContext, this, soapObject, methodName, IfRefrush);
            connectGoodsServiceAsyncTask.initProgressDialog(false);
            connectGoodsServiceAsyncTask.execute();
        }
    }

    /**
     * 筛选结束 根据筛选结果重新请求产品数据
     **/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            listData.clear();
            shopClassSearchAdapter.notifyDataSetChanged();
            currentFilterFilterIDS = data.getStringExtra(InformationCodeUtil.IntentFilterFirstActivityFilterClassID);
            currentFilterKeyWord = "";
            et_searchContext.setText("");
            swipeRefreshListView.openRefreshState();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_titleLeft:
                preViewToFinish();
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
            default:
                break;
        }
        // TODO
    }

    /**
     * 代理商品
     **/
    private void agentGoods() {
        if (listData == null || listData.size() == 0) {
            ToastUtils.show(mContext, "请至少选择一件需要代理的商品");
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
            ToastUtils.show(mContext, "请至少选择一件需要代理的商品");
        } else {
            showDialogAgentConfirm();
        }
    }

    /**弹出代理商品确认界面**/
    private void showDialogAgentConfirm() {
        fvd = new FragmentViewDialog();
        View view = View.inflate(mContext, R.layout.dialog_allgoods_agent_confirm, null);
        fvd.initView(view, "取消", "确定", new FragmentViewDialog.OnButtonClickListener() {
            @Override
            public void OnOkClick() {
                getData(InformationCodeUtil.methodNameDelegateShopAllGoods, true);
            }

            @Override
            public void OnCancelClick() {

            }
        });

        fvd.show(getSupportFragmentManager(), "fvd");
    }

    /**
     * 添加商品到服务器到购物车中
     **/
    public void addGoodsToShoppingCart() {
        if (listData == null || listData.size() == 0) {
            ToastUtils.show(mContext, "请至少选择一件需要加入购物车的商品");
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
                GoodsPackageColorModel tempPackageColor = tempGoods.getDefaultGoodsPackage().getDefaultPackageColor();
                ShoppingCartChildGoodsModel currentChildGoodsModel = new ShoppingCartChildGoodsModel
                        ( 0, tempGoods.getDjLsh(),tempGoods.getGoodsName(), tempGoods.getImgUrl()
                                ,tempPackage.getDjLsh(),tempPackage.getPackageName()
                                ,tempPackageColor.getDjLsh(),tempPackageColor.getColorName()
                                ,1 ,tempPackageColor.getPrice(),tempPackageColor.getTaxPrice()
                                ,tempPackageColor.getFlyCoin(),tempPackageColor.getTaxFlyCoin());
                List<ShoppingCartChildGoodsModel> tempList = new ArrayList<ShoppingCartChildGoodsModel>();
                tempList.add(currentChildGoodsModel);
                ShoppingCartParentGoodsModel currentParentGoodsModel = new ShoppingCartParentGoodsModel();
                currentParentGoodsModel.setShopID(tempGoods.getShopID());
                currentParentGoodsModel.setShoppingCarts(tempList);
                listShoppingCartParentGoodsModel.add(currentParentGoodsModel);

                //接口服务需要使用
                ShoppingCartWrapperModel shoppingCartWrapperModel = new ShoppingCartWrapperModel(
                        tempGoods.getDjLsh(),tempGoods.getDefaultGoodsPackage().getDjLsh()
                        ,tempGoods.getDefaultGoodsPackage().getDefaultPackageColor().getDjLsh(),1);
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
                (mContext, this, requestSoapObject , methodName, listShoppingCartParentGoodsModel);
        connectCustomServiceAsyncTask.execute();
        return;
    }

    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, List<ShoppingCartParentGoodsModel> state, boolean whetherRefresh) {
        // LogUtils.LogShitou("returnSoapObject", returnSoapObject.toString());
        if (methodName == InformationCodeUtil.methodNameGetGoodsList) {
            try {
                refrushListData(returnString, methodName, whetherRefresh);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        if (methodName == InformationCodeUtil.methodNameDelegateShopAllGoods) {
            try {
                JSONObject json = new JSONObject(returnString);
                int sign = json.getInt("Sign");
                if(sign == 1){
                    ToastUtils.show(mContext, "商品代理成功");
                    fvd.dismiss();
                    fvd = null;
                }else{
                    ToastUtils.show(mContext, json.getString("Msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ;
        }
        if (methodName == InformationCodeUtil.methodNameBatchAddShopCart) {
            try {
                JSONObject json = new JSONObject(returnString);
                int sign = json.getInt("Sign");
                if(sign == 1){
                    PreferencesUtilMy.addGoodsToShopCart(mContext,state);
                    ToastUtils.show(mContext, "商品成功加入购物车");
                }else{
                    ToastUtils.show(mContext, json.getString("Msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void refrushListData(String returnString,
                                 String methodName, boolean whetherRefresh) {
        // private void refrushListData(List<ProductModel> list, boolean
        // whetherRefresh) {

        // LogUtil.LogShitou("搜索结果", returnSoapObject.toString());
//		Object provinceSoapObject = (Object) returnSoapObject
//				.getProperty(methodName + "Result");
        Gson gson = new Gson();
        List<GoodsGeneralModel> list = null;
        try {

            JSONResultBaseModel<GoodsGeneralModel> mJSONMyAgent = gson
                    .fromJson(
                            returnString,
                            new TypeToken<JSONResultBaseModel<GoodsGeneralModel>>() {
                            }.getType());
            list = mJSONMyAgent.getList();
        } catch (Exception e) {
            if (!whetherRefresh) {
                currentPageIndex--;
            }
        }
        if (list == null || list.size() == 0) {
            if (whetherRefresh) {
                ToastUtils.show(mContext, "未搜到符合条件的商品");
            } else {
                ToastUtils.show(mContext, "暂无更多商品数据");
            }
        } else {

            for (int i = 0; i < list.size(); i++) {
                List<String> images = new ArrayList<String>();
                images.add(list.get(i).getImgUrl());
                list.get(i).setImages(images);
            }
            listData.addAll(list);
            shopClassSearchAdapter.notifyDataSetChanged();
        }
        if(swipeRefreshListView != null)
        swipeRefreshListView.closeRefreshState();
    }

    @Override
    public void connectServiceFailed(String methodName, List<ShoppingCartParentGoodsModel> state,
                                     boolean whetherRefresh) {

        if (methodName == InformationCodeUtil.methodNameGetGoodsList) {
            if (!whetherRefresh) {
                currentPageIndex--;
            }
            if(swipeRefreshListView != null)
            swipeRefreshListView.closeRefreshState();
            ToastUtils.show(this, "网络异常,获取数据失败");
            return;
        }
        if (methodName == InformationCodeUtil.methodNameDelegateShopAllGoods) {
            ToastUtils.show(this, "网络异常,商品代理失败");
            return;
        }
        if (methodName == InformationCodeUtil.methodNameBatchAddShopCart) {
            ToastUtils.show(this, "网络异常,商品加入购物车失败");
        }

    }

    @Override
    public void connectServiceCancelled(String returnString,
                                        String methodName, List<ShoppingCartParentGoodsModel> state, boolean whetherRefresh) {

        if (methodName == InformationCodeUtil.methodNameBatchAddShopCart) {
            try {
                JSONObject json = new JSONObject(returnString);
                int sign = json.getInt("Sign");
                if(sign == 1){
                    PreferencesUtilMy.addGoodsToShopCart(mContext,state);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onTopRefrushListener() {
        LogUtil.LogShitou("onTopRefrushListener", "被执行");
        getData(InformationCodeUtil.methodNameGetGoodsList, true);

    }

    @Override
    public void onBottomRefrushListener() {
        LogUtil.LogShitou("onBottomRefrushListener", "被执行");
        getData(InformationCodeUtil.methodNameGetGoodsList, false);

    }

    void preViewToFinish() {
        finish();
        if (isFromBottom) {
            overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
        }
    }

    @Override
    public void onBackPressed() {
        preViewToFinish();
    }
}
