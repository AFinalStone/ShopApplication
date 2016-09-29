package com.shuimunianhua.xianglixiangqin.frament;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.activity.GoodsDetailGeneralActivity;
import com.shuimunianhua.xianglixiangqin.activity.MainActivity;
import com.shuimunianhua.xianglixiangqin.activity.MyOrderActivity;
import com.shuimunianhua.xianglixiangqin.activity.MyShopWebActivity;
import com.shuimunianhua.xianglixiangqin.activity.SearchGoodsActivity;
import com.shuimunianhua.xianglixiangqin.activity.SearchGoodsMyAgentActivity;
import com.shuimunianhua.xianglixiangqin.activity.SettingCenterActivity;
import com.shuimunianhua.xianglixiangqin.activity.ShopRecommendActivity;
import com.shuimunianhua.xianglixiangqin.activity.SportSaleBuyCrazyActivity;
import com.shuimunianhua.xianglixiangqin.activity.SportSaleBuyGroupActivity;
import com.shuimunianhua.xianglixiangqin.activity.SportSaleBuyTimeLimitedActivity;
import com.shuimunianhua.xianglixiangqin.adapter.FunctionTypeAdapter;
import com.shuimunianhua.xianglixiangqin.adapter.MyBaseAdapter;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultBaseModel;
import com.shuimunianhua.xianglixiangqin.model.FunctionTypeModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsGeneralModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsGroupModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsSportModel;
import com.shuimunianhua.xianglixiangqin.model.ShopInfoModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.ImagerLoaderUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.StringUtil;
import com.shuimunianhua.xianglixiangqin.util.TimeUtils;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;
import com.shuimunianhua.xianglixiangqin.view.FragmentCommonDialog;
import com.shuimunianhua.xianglixiangqin.view.ScrollListView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * @author SHI
 * @action 带你飞首页
 * @date 2015-7-18 上午11:24:43
 */

public class MainHomeFragment_JvHeEx extends MyBaseFragment<MainActivity> implements
        OnConnectServerStateListener<Integer>, SwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener {

    /**
     * 标题
     **/
    @Bind(R.id.tv_title)
    TextView tv_title;
    /**
     * swipeRefreshLayout
     **/
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    //新控件
    @Bind(R.id.appBar_layout)
    AppBarLayout appBar_layout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    @Bind(R.id.ibn_backToTop)
    ImageButton ibn_backToTop;
    /**
     * 搜索内容EditText
     **/
    @Bind(R.id.et_searchContext)
    EditText et_searchContext;
    /**
     * 搜索栏
     **/
    @Bind(R.id.linearLayout_search)
    LinearLayout linearLayout_search;

    /**
     * 搜索按钮
     **/
    @Bind(R.id.tv_search)
    TextView tv_search;

    /**
     * 通知
     **/
    @Bind(R.id.tv_informMsg)
    TextView tv_informMsg;

    /**
     * 轮播图
     **/
    @Bind(R.id.sliderLayout_rollView)
    SliderLayout sliderLayout_rollView;
    List<String> listData_rollView = new ArrayList<String>();
    private List<TrumpetNoticeModel> listData_InforMsg;


    /**
     * 功能模块GridView
     **/
    @Bind(R.id.gridView_functionModel)
    GridView gridView_functionModel;
    private List<FunctionTypeModel> listData_FunctionType;
    private FunctionTypeAdapter functionTypeAdapter;

    private final String FunctionType_RecommendShop = "推荐店铺";
    private final String FunctionType_MyOrder = "我的订单";
    private final String FunctionType_SettingCenter = "设置中心";
    private final String FunctionType_AgentShopGoods = "一键代理";
    private final String FunctionType_MyAgentGoods = "我的代理";
    private final String FunctionType_MyShop = "我的店铺";

    private final String SportModel_BuyCrazy = "疯狂秒杀";
    private final String SportModel_BuyTimeLimite = "限时特卖";
    private final String SportModel_BuyGroup = "品牌团购";
    private final String SportModel_BuyNewCommend = "新品推荐";

    /**
     * 活动商品功能模块
     **/
    @Bind(R.id.layout_sportModel)
    View layout_sportModel;
    SportModelViewHolder sportModelViewHolder01, sportModelViewHolder02;


    /**
     * 特价商品ListView
     **/
    @Bind(R.id.linearLayout_specialsPricesGoods)
    LinearLayout linearLayout_specialsPricesGoods;
    @Bind(R.id.listView_specialsPricesGoods)
    ScrollListView listView_specialsPricesGoods;
    private List<GoodsGeneralModel> listDataSpecialsPricesGoods;
    private AdapterSpecialsPricesGoods adapterSpecialsPricesGoods;
    /**
     * 普通商品ListView
     **/
    @Bind(R.id.listView_generalGoods)
    ScrollListView listView_generalGoods;
    private List<GoodsGroupModel> listDataGeneralGoods;
    private AdapterGeneralGoods adapterGeneralGoods;

    private View rootView;
    public ShopInfoModel currentShopInfoModel;

    /**
     * 当前服务器时间
     **/
    private long time_current;
    /**
     * 为了让通知全部显示
     **/
    private int position_currentInformMsg;
    /**
     * 是否包含活动商品
     **/
    private boolean IfHaveSportGoods = false;
    private int MESSAGE_01 = 1;
    //使用Handler的延时效果，每隔一秒刷新一下适配器，以此产生倒计时效果
    private Handler handler_timeCurrent = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            time_current = time_current + 1000;
            updateTextView(sportModelViewHolder01);
            updateTextView(sportModelViewHolder02);
            handler_timeCurrent.sendEmptyMessageDelayed(MESSAGE_01, 1000);

        }
    };

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = View.inflate(mActivity, R.layout.fragment_home_jvhe_ex, null);
            ButterKnife.bind(this, rootView);
            tv_title.setText("首页");
            et_searchContext.setInputType(InputType.TYPE_NULL);

            swipeRefreshLayout.setProgressViewEndTarget(false, 100);

            //初始化轮播图
            sliderLayout_rollView.setPresetTransformer(SliderLayout.Transformer.Accordion);
            sliderLayout_rollView.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout_rollView.setCustomAnimation(new DescriptionAnimation());
            sliderLayout_rollView.setDuration(4000);
            sliderLayout_rollView.setPresetTransformer(SliderLayout.Transformer.Default);

            // 初始化功能模块数据
            listData_FunctionType = new ArrayList<FunctionTypeModel>();
            listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_recomend_shop, FunctionType_RecommendShop));
            listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_my_order, FunctionType_MyOrder));
            listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_setting_center, FunctionType_SettingCenter));
            listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_agent_shop_goods, FunctionType_AgentShopGoods));
            listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_my_agent_goods, FunctionType_MyAgentGoods));
            listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_my_shop, FunctionType_MyShop));
            functionTypeAdapter = new FunctionTypeAdapter(mActivity,
                    listData_FunctionType);
            gridView_functionModel.setAdapter(functionTypeAdapter);
            gridView_functionModel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    toOtherFunctionModelView(listData_FunctionType.get(position).getName());
                }
            });
            // 初始化活动模块快捷入口数据
            sportModelViewHolder01 = new SportModelViewHolder();
            sportModelViewHolder01.relativeLayout_sportModel = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_sportModel01);
            sportModelViewHolder01.linearLayout_timeLimited = (LinearLayout) rootView.findViewById(R.id.linearLayout_timeLimited01);
            sportModelViewHolder01.tv_hour = (TextView) rootView.findViewById(R.id.tv_hour01);
            sportModelViewHolder01.tv_minute = (TextView) rootView.findViewById(R.id.tv_minute01);
            sportModelViewHolder01.tv_second = (TextView) rootView.findViewById(R.id.tv_second01);
            sportModelViewHolder01.tv_sportOnGoing = (TextView) rootView.findViewById(R.id.tv_sportOnGoing01);
            sportModelViewHolder01.iv_goodsImages = (ImageView) rootView.findViewById(R.id.iv_goodsImages01);
            sportModelViewHolder01.tv_goodsPrice = (TextView) rootView.findViewById(R.id.tv_goodsPrice01);


            sportModelViewHolder02 = new SportModelViewHolder();
            sportModelViewHolder02.relativeLayout_sportModel = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_sportModel02);
            sportModelViewHolder02.linearLayout_timeLimited = (LinearLayout) rootView.findViewById(R.id.linearLayout_timeLimited02);
            sportModelViewHolder02.tv_hour = (TextView) rootView.findViewById(R.id.tv_hour02);
            sportModelViewHolder02.tv_minute = (TextView) rootView.findViewById(R.id.tv_minute02);
            sportModelViewHolder02.tv_second = (TextView) rootView.findViewById(R.id.tv_second02);
            sportModelViewHolder02.tv_sportOnGoing = (TextView) rootView.findViewById(R.id.tv_sportOnGoing02);
            sportModelViewHolder02.iv_goodsImages = (ImageView) rootView.findViewById(R.id.iv_goodsImages02);
            sportModelViewHolder02.tv_goodsPrice = (TextView) rootView.findViewById(R.id.tv_goodsPrice02);
            sportModelViewHolder01.relativeLayout_sportModel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, SportSaleBuyCrazyActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentSportSaleByActivityCurrentShopID
                            , currentShopInfoModel.getDjLsh());
                    mActivity.startActivity(intent);
                }
            });
            sportModelViewHolder02.relativeLayout_sportModel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, SportSaleBuyTimeLimitedActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentSportSaleByActivityCurrentShopID
                            , currentShopInfoModel.getDjLsh());
                    mActivity.startActivity(intent);
                }
            });
            //特价商品
            listDataSpecialsPricesGoods = new ArrayList<GoodsGeneralModel>();
            adapterSpecialsPricesGoods = new AdapterSpecialsPricesGoods(mActivity, listDataSpecialsPricesGoods);
            listView_specialsPricesGoods.setAdapter(adapterSpecialsPricesGoods);
            listView_specialsPricesGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Intent intent = new Intent(mActivity, GoodsDetailGeneralActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentGoodsID, listDataSpecialsPricesGoods.get(position).getDjLsh());
                    mActivity.startActivity(intent);
                }

            });

            //普通商品列表
            listDataGeneralGoods = new ArrayList<GoodsGroupModel>();
            adapterGeneralGoods = new AdapterGeneralGoods(mActivity, listDataGeneralGoods);
            listView_generalGoods.setAdapter(adapterGeneralGoods);
            swipeRefreshLayout.setColorSchemeColors(Color.RED
                    , Color.GREEN
                    , Color.BLUE
                    , Color.YELLOW
                    , Color.CYAN
                    , 0xFFFE5D14
                    , Color.MAGENTA);
            swipeRefreshLayout.setOnRefreshListener(this);

            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                    LogUtil.LogShitou("ScrollX,Y","scrollX:"+scrollX+"scrollY:"+scrollY);
//                    LogUtil.LogShitou("oldScrollX,Y","oldScrollX:"+oldScrollX+"oldScrollY:"+oldScrollY);
                    if (scrollY - oldScrollY < -10) {
                        ibn_backToTop.setVisibility(View.VISIBLE);
                    }
                    if (scrollY - oldScrollY > 1 || scrollY <= 100) {
                        ibn_backToTop.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
        return rootView;
    }

    @Override
    public void initData() {
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.LogShitou("onHiddenChanged被执行");
        if (hidden) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        appBar_layout.addOnOffsetChangedListener(this);
        connectSuccessFlag = false;
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        handler_timeCurrent.removeCallbacksAndMessages(null);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//        LogUtil.LogShitou("当前高度",""+verticalOffset);
//        LogUtil.LogShitou("搜索栏高度",""+linearLayout_search.getHeight());
//        LogUtil.LogShitou("标题栏高度",""+toolbar.getHeight());
//        LogUtil.LogShitou("应用栏高度",""+appBar_layout.getHeight());
        toolbar.setVisibility(View.VISIBLE);
        if (verticalOffset == 0) {
            swipeRefreshLayout.setEnabled(true);
            tv_title.setTextColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
            toolbar.getBackground().setAlpha(0xFF);
            tv_search.getBackground().setAlpha(0);
            tv_search.setTextColor(Color.argb(0xff, 0xE8, 0x38, 0x21));
        } else {
            swipeRefreshLayout.setEnabled(false);
            if (verticalOffset > -0xFF) {
                int alpha = 0xFF * verticalOffset / linearLayout_search.getHeight();
                toolbar.getBackground().setAlpha(alpha);
                tv_title.setTextColor(Color.argb(alpha, 0xFF, 0xFF, 0xFF));
                tv_search.getBackground().setAlpha(-alpha);
                tv_search.setTextColor(Color.argb(-alpha, 0xFF, 0xFF, 0xFF));
            }
            if (linearLayout_search.getHeight() == -verticalOffset) {
                toolbar.setVisibility(View.INVISIBLE);
                tv_search.setTextColor(Color.argb(0xff, 0xFF, 0xFF, 0xFF));
                tv_search.getBackground().setAlpha(0xff);
            }
        }

    }

    @Override
    public void onRefresh() {
        getData(InformationCodeUtil.methodNameStoreHomeIndexExt);
    }

    private void getData(String methodName) {

        if (methodName == InformationCodeUtil.methodNameStoreHomeIndexExt) {
            SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
            soapObject.addProperty("shopID", mActivity.currentShopID);
            ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
                    mActivity, this, soapObject, methodName);
            connectGoodsServiceAsyncTask.initProgressDialog(false);
            connectGoodsServiceAsyncTask.execute();
        }

        if (methodName == InformationCodeUtil.methodNameDelegateShopAllGoods) {
            SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
            soapObject.addProperty("customID", MyApplication.getmCustomModel(mActivity).getDjLsh());
            soapObject.addProperty("openKey", MyApplication.getmCustomModel(mActivity).getOpenKey());
            soapObject.addProperty("shopID", mActivity.currentShopID);
            soapObject.addProperty("goodsIDS", "");
            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                    mActivity, this, soapObject, methodName);
            connectCustomServiceAsyncTask.execute();
        }

    }

    @OnClick({R.id.et_searchContext, R.id.tv_search, R.id.ibn_backToTop})
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ibn_backToTop:
                nestedScrollView.smoothScrollTo(0, 0);
                break;
            case R.id.et_searchContext:
            case R.id.tv_search:
                intent = new Intent(mActivity, SearchGoodsActivity.class);
                intent.putExtra(InformationCodeUtil.IntentSearchGoodsCurrentShopID, currentShopInfoModel.getDjLsh());
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.in_from_bottom, R.anim.not_change);
                break;
            default:
                break;
        }
    }


    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, Integer state, boolean whetherRefresh) {
        LogUtil.LogShitou("主页数据", returnString);

        if (methodName == InformationCodeUtil.methodNameStoreHomeIndexExt) {
            try {
                Gson gson = new Gson();
                currentShopInfoModel = gson.fromJson(returnString, ShopInfoModel.class);
                connectSuccessFlag = true;
                refreshTitleLogo(currentShopInfoModel);
                refreshTrumpet(currentShopInfoModel.getNotices(), gson);
                refreshSportGoods(currentShopInfoModel.getActs(), gson);
                refreshShopGeneralGoods(currentShopInfoModel.getModules(), gson);
                refreshRollView(currentShopInfoModel.getAdvList());
                swipeRefreshLayout.setRefreshing(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        if (methodName == InformationCodeUtil.methodNameDelegateShopAllGoods) {
            if (returnString.contains("代理成功")) {
                ToastUtils.show(mActivity, "成功代理本店所有商品");
            }
            return;
        }

    }

    @Override
    public void connectServiceFailed(String methodName, Integer state, boolean whetherRefresh) {
        if (methodName == InformationCodeUtil.methodNameStoreHomeIndexExt) {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtils.show(mActivity, "网络异常，主页数据请求失败");
            return;
        }

        if (methodName == InformationCodeUtil.methodNameDelegateShopAllGoods) {
            ToastUtils.show(mActivity, "网络异常，一键代理失败");
            return;
        }
    }

    @Override
    public void connectServiceCancelled(String returnString,
                                        String methodName, Integer state, boolean whetherRefresh) {
        if (methodName == InformationCodeUtil.methodNameDelegateShopAllGoods) {
            if (returnString.contains("代理成功")) {
                ToastUtils.show(mActivity, "成功代理本店所有商品");
            }
            return;
        }
    }

    private void refreshTitleLogo(ShopInfoModel shopInfoModel) {

        if (!StringUtil.isEmpty(shopInfoModel.getShopName())) {
            tv_title.setText(shopInfoModel.getShopName());
        } else {
            tv_title.setText("首页");
        }

    }


    /**
     * 刷新喇叭通知信息
     **/
    private void refreshTrumpet(String strNotices, Gson gson) {
        try {
            listData_InforMsg = gson.fromJson(strNotices, new TypeToken<List<TrumpetNoticeModel>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listData_InforMsg == null) {
            listData_InforMsg = new ArrayList<TrumpetNoticeModel>();
            listData_InforMsg.add(new TrumpetNoticeModel("通知", 1, "暂无通知"));
        }
        position_currentInformMsg = 0;
        tv_informMsg.setText(listData_InforMsg.get(position_currentInformMsg).getTitle());
    }

    /**
     * 刷新活动商品
     **/
    private void refreshSportGoods(String strActs, Gson gson) {
        LogUtil.LogShitou("活动商品", strActs);
        try {
            handler_timeCurrent.removeCallbacksAndMessages(null);
            IfHaveSportGoods = false;

            sportModelViewHolder01.linearLayout_timeLimited.setVisibility(View.INVISIBLE);
            sportModelViewHolder01.tv_sportOnGoing.setVisibility(View.INVISIBLE);
            sportModelViewHolder01.iv_goodsImages.setImageResource(R.drawable.icon_sport_model_buy_crazy_flag_nothing);
            sportModelViewHolder01.tv_goodsPrice.setVisibility(View.INVISIBLE);

            sportModelViewHolder02.linearLayout_timeLimited.setVisibility(View.INVISIBLE);
            sportModelViewHolder02.tv_sportOnGoing.setVisibility(View.INVISIBLE);
            sportModelViewHolder02.iv_goodsImages.setImageResource(R.drawable.icon_sport_model_buy_time_limit_flag_nothing);
            sportModelViewHolder02.tv_goodsPrice.setVisibility(View.INVISIBLE);

            //JSONObject负责解决获取JSON键值对
            JSONArray jsonArray = new JSONArray(strActs);
            //循环遍历活动商品数组内容
            for (int i = 0; i < jsonArray.length(); i++) {
                //使用泛型解析JSON数据，主要用到了TypeToken这个对象
                JSONResultBaseModel<GoodsSportModel> mJSONResultModel = gson.fromJson
                        (jsonArray.get(i).toString(), new TypeToken<JSONResultBaseModel<GoodsSportModel>>() {
                        }.getType());

                if ("整点秒杀".equals(mJSONResultModel.getTitle())) {
                    List<GoodsSportModel> list = mJSONResultModel.getList();
                    if (list != null) {
                        IfHaveSportGoods = true;
                        GoodsSportModel goodsSportModel = list.get(0);
                        time_current = TimeUtils.getTimeDate(goodsSportModel.getPresentTime()).getTime();
                        sportModelViewHolder01.modelTimeToBegin = goodsSportModel.getBeginTime();
                        sportModelViewHolder01.modelTimeToFinish = goodsSportModel.getEndTime();
                        sportModelViewHolder01.tv_goodsPrice.setText("" + (int) goodsSportModel.getPrice());
                        ImagerLoaderUtil.getInstance(mActivity).displayMyImage(goodsSportModel.getImages().get(0), sportModelViewHolder01.iv_goodsImages);
                    }
                }
                if ("限时抢购".equals(mJSONResultModel.getTitle())) {
                    List<GoodsSportModel> list = mJSONResultModel.getList();
                    if (list != null) {
                        IfHaveSportGoods = true;
                        GoodsSportModel goodsSportModel = list.get(0);
                        time_current = TimeUtils.getTimeDate(goodsSportModel.getPresentTime()).getTime();
                        sportModelViewHolder02.modelTimeToBegin = goodsSportModel.getBeginTime();
                        sportModelViewHolder02.modelTimeToFinish = goodsSportModel.getEndTime();
                        sportModelViewHolder02.tv_goodsPrice.setText("" + (int) goodsSportModel.getPrice());
                        ImagerLoaderUtil.getInstance(mActivity).displayMyImage(goodsSportModel.getImages().get(0), sportModelViewHolder02.iv_goodsImages);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(IfHaveSportGoods)
        handler_timeCurrent.sendEmptyMessageDelayed(MESSAGE_01, 800);
    }

    public void updateTextView(SportModelViewHolder holder) {

        if(holder.modelTimeToBegin == null || holder.modelTimeToFinish == null){
            holder.linearLayout_timeLimited.setVisibility(View.INVISIBLE);
            holder.tv_sportOnGoing.setVisibility(View.INVISIBLE);
            return;
        }

        long time_remainToBegin = (TimeUtils.getTimeDate(holder.modelTimeToBegin).getTime() - time_current) / 1000;
        long time_remainToFinish = (TimeUtils.getTimeDate(holder.modelTimeToFinish).getTime() - time_current) / 1000;

        if (time_remainToFinish <= 0) {
            holder.tv_hour.setText("00");
            holder.tv_minute.setText("00");
            holder.tv_second.setText("00");
            holder.linearLayout_timeLimited.setVisibility(View.VISIBLE);
            holder.tv_sportOnGoing.setVisibility(View.INVISIBLE);
            return;
        }
        if (time_remainToBegin <= 0) {
            holder.linearLayout_timeLimited.setVisibility(View.INVISIBLE);
            holder.tv_sportOnGoing.setVisibility(View.VISIBLE);
            return;
        }

        String str_hour, str_minute, str_second;
        long time_hour, time_minute, time_second;
        //秒
        time_second = time_remainToBegin % 60;
        //分钟
        time_minute = (time_remainToBegin / 60) % 60;
        //小时
        time_hour = time_remainToBegin / 3600;

        //秒
        if (time_second < 10) {
            str_second = new StringBuffer().append("0").append(time_second).toString();
        } else {
            str_second = new StringBuffer().append(time_second).toString();
        }
        //分钟
        if (time_minute < 10) {
            str_minute = new StringBuffer().append("0").append(time_minute).toString();
        } else {
            str_minute = new StringBuffer().append(time_minute).toString();
        }
        //小时
        if (time_hour < 10) {
            str_hour = new StringBuffer().append("0").append(time_hour).toString();
        } else {
            str_hour = new StringBuffer().append(time_hour).toString();
        }

        holder.tv_sportOnGoing.setVisibility(View.INVISIBLE);
        holder.linearLayout_timeLimited.setVisibility(View.VISIBLE);
        holder.tv_hour.setText(str_hour);
        holder.tv_minute.setText(str_minute);
        holder.tv_second.setText(str_second);
    }


    /**
     * 刷新普通商品
     **/
    private void refreshShopGeneralGoods(String strAllModel, Gson gson) {

        try {
            linearLayout_specialsPricesGoods.setVisibility(View.GONE);
            listDataGeneralGoods.clear();
            List<String> listData_generalGoods =
                    gson.fromJson(strAllModel, new TypeToken<List<String>>() {
                    }.getType());
            for (int i = 0; i < listData_generalGoods.size(); i++) {

                JSONResultBaseModel<GoodsGeneralModel> jSONResultModel =
                        gson.fromJson(listData_generalGoods.get(i)
                                , new TypeToken<JSONResultBaseModel<GoodsGeneralModel>>() {
                                }.getType());

                if ("特价商品".equals(jSONResultModel.getTitle())) {
                    refreshSpecialsPricesGoods(jSONResultModel);
                    continue;
                }
                if ("新品推荐".equals(jSONResultModel.getTitle())) {
                    continue;
                }

                refreshGeneralAllGoods(jSONResultModel);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 刷新普通商品
     **/
    private void refreshGeneralAllGoods(JSONResultBaseModel<GoodsGeneralModel> mJSONProductModel) {

        try {

            GoodsGroupModel productModelGroup = new GoodsGroupModel();
            productModelGroup.setClassID(mJSONProductModel.getID());
            productModelGroup.setProductModeType(mJSONProductModel.getTitle());
            List<GoodsGeneralModel> listGoods = mJSONProductModel.getList();
            if (listGoods != null && listGoods.size() != 0) {
                productModelGroup.setListProductModels(mJSONProductModel.getList());
                listDataGeneralGoods.add(productModelGroup);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapterGeneralGoods.notifyDataSetChanged();
    }


    /**
     * 刷新特价商品内容
     **/
    private void refreshSpecialsPricesGoods(JSONResultBaseModel<GoodsGeneralModel> mJSONProductModel) {
        try {
            listDataSpecialsPricesGoods.clear();
            listDataSpecialsPricesGoods.addAll(mJSONProductModel.getList());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        if(listDataSpecialsPricesGoods.size() > 0){
            linearLayout_specialsPricesGoods.setVisibility(View.VISIBLE);
        }
        adapterSpecialsPricesGoods.notifyDataSetChanged();
    }

    /**
     * 刷新轮播图内容
     **/
    private void refreshRollView(String strAdvList) {
        try {
            LogUtil.LogShitou("轮播图", strAdvList);
            listData_rollView.clear();
            sliderLayout_rollView.removeAllSliders();
            JSONArray jsonArray = new JSONObject(strAdvList).getJSONArray("List");
            //刷新轮播图
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject mJSONAdvModel = jsonArray.getJSONObject(i);
                String imageUrl = mJSONAdvModel.getString("ImgUrl");
                if (imageUrl != null) {
                    listData_rollView.add(imageUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listData_rollView.size() == 0) {
            sliderLayout_rollView.setVisibility(View.GONE);
        } else {
            sliderLayout_rollView.setVisibility(View.VISIBLE);
            for (int i = 0; i < listData_rollView.size(); i++) {
                TextSliderView textSliderView = new TextSliderView(mActivity);
                //初始化轮播图的Item
                textSliderView
                        .description("")
                        .image(listData_rollView.get(i))
                        .setScaleType(BaseSliderView.ScaleType.Fit);

                //添加额外信息,用于点击时候使用
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", "测试数据" + i);

                sliderLayout_rollView.addSlider(textSliderView);
            }
        }


    }


    public class AdapterGeneralGoods extends MyBaseAdapter<GoodsGroupModel> {

        public AdapterGeneralGoods(Context mContext, List listData) {
            super(mContext, listData);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_shop_generalgoods_listview, null);
                holder = new ViewHolder();
                holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
                holder.tv_moreNewGoodsData = (TextView) convertView.findViewById(R.id.tv_moreNewGoodsData);

                holder.relativeLayoutView0.RelativeLayout = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout1);
                holder.relativeLayoutView0.iv_specialPricesGoods = (ImageView) convertView.findViewById(R.id.iv_specialPricesGoods1);
                holder.relativeLayoutView0.tv_specialPricesGoodsName = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsName1);
                holder.relativeLayoutView0.tv_specialPricesGoodsPrices = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsPrices1);
                holder.relativeLayoutView0.tv_SaledCount = (TextView) convertView.findViewById(R.id.tv_seeDataDetail1);

                holder.relativeLayoutView1.RelativeLayout = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout2);
                holder.relativeLayoutView1.iv_specialPricesGoods = (ImageView) convertView.findViewById(R.id.iv_specialPricesGoods2);
                holder.relativeLayoutView1.tv_specialPricesGoodsName = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsName2);
                holder.relativeLayoutView1.tv_specialPricesGoodsPrices = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsPrices2);
                holder.relativeLayoutView1.tv_SaledCount = (TextView) convertView.findViewById(R.id.tv_seeDataDetail2);

                holder.relativeLayoutView2.RelativeLayout = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout3);
                holder.relativeLayoutView2.iv_specialPricesGoods = (ImageView) convertView.findViewById(R.id.iv_specialPricesGoods3);
                holder.relativeLayoutView2.tv_specialPricesGoodsName = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsName3);
                holder.relativeLayoutView2.tv_specialPricesGoodsPrices = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsPrices3);
                holder.relativeLayoutView2.tv_SaledCount = (TextView) convertView.findViewById(R.id.tv_seeDataDetail3);

                holder.relativeLayoutView3.RelativeLayout = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout4);
                holder.relativeLayoutView3.iv_specialPricesGoods = (ImageView) convertView.findViewById(R.id.iv_specialPricesGoods4);
                holder.relativeLayoutView3.tv_specialPricesGoodsName = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsName4);
                holder.relativeLayoutView3.tv_specialPricesGoodsPrices = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsPrices4);
                holder.relativeLayoutView3.tv_SaledCount = (TextView) convertView.findViewById(R.id.tv_seeDataDetail4);

                holder.relativeLayoutView4.RelativeLayout = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout5);
                holder.relativeLayoutView4.iv_specialPricesGoods = (ImageView) convertView.findViewById(R.id.iv_specialPricesGoods5);
                holder.relativeLayoutView4.tv_specialPricesGoodsName = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsName5);
                holder.relativeLayoutView4.tv_specialPricesGoodsPrices = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsPrices5);
                holder.relativeLayoutView4.tv_SaledCount = (TextView) convertView.findViewById(R.id.tv_seeDataDetail5);

                holder.relativeLayoutView5.RelativeLayout = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout6);
                holder.relativeLayoutView5.iv_specialPricesGoods = (ImageView) convertView.findViewById(R.id.iv_specialPricesGoods6);
                holder.relativeLayoutView5.tv_specialPricesGoodsName = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsName6);
                holder.relativeLayoutView5.tv_specialPricesGoodsPrices = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsPrices6);
                holder.relativeLayoutView5.tv_SaledCount = (TextView) convertView.findViewById(R.id.tv_seeDataDetail5);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final GoodsGroupModel currentProductModelGroup = listData.get(position);

            holder.tv_type.setText(currentProductModelGroup.getProductModeType());
            holder.tv_moreNewGoodsData.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SearchGoodsActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentSearchGoodsCurrentShopID, currentShopInfoModel.getDjLsh());
                    intent.putExtra(InformationCodeUtil.IntentSearchGoodsFilterClassID, currentProductModelGroup.getClassID());
                    mContext.startActivity(intent);
                }
            });

            if (currentProductModelGroup.getListProductModels() != null) {

                List<GoodsGeneralModel> listModels = currentProductModelGroup.getListProductModels();
                for (int i = 0; i < listModels.size() && i < 6; i++) {
                    final GoodsGeneralModel currentProductModel = listModels.get(i);
                    RelativeLayoutView currentRelativeLayoutView = holder.getRelativeLayoutView(i);
                    if (currentProductModel != null) {

                        currentRelativeLayoutView.RelativeLayout.setVisibility(View.VISIBLE);
                        ImagerLoaderUtil.getInstance(mContext).displayMyImage(
                                currentProductModel.getImgUrl(),
                                currentRelativeLayoutView.iv_specialPricesGoods);
                        currentRelativeLayoutView.tv_specialPricesGoodsName.setText(currentProductModel.getGoodsName());
                        currentRelativeLayoutView.tv_specialPricesGoodsPrices.setText("￥" + currentProductModel.getMinPrice());
                        currentRelativeLayoutView.tv_SaledCount.setText("已售" + currentProductModel.getSaledCount() + "件");
                        holder.getRelativeLayoutView(i).RelativeLayout.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, GoodsDetailGeneralActivity.class);
                                intent.putExtra(InformationCodeUtil.IntentGoodsID, currentProductModel.getDjLsh());
                                mContext.startActivity(intent);
                            }
                        });
                    } else {
                        holder.getRelativeLayoutView(i).RelativeLayout.setVisibility(View.GONE);
                    }
                }
            }

            return convertView;
        }

        private class ViewHolder {
            /**
             * 商品类型
             **/
            public TextView tv_type;
            /**
             * 更多
             **/
            public TextView tv_moreNewGoodsData;

            public RelativeLayoutView relativeLayoutView0 = new RelativeLayoutView();
            public RelativeLayoutView relativeLayoutView1 = new RelativeLayoutView();
            public RelativeLayoutView relativeLayoutView2 = new RelativeLayoutView();
            public RelativeLayoutView relativeLayoutView3 = new RelativeLayoutView();
            public RelativeLayoutView relativeLayoutView4 = new RelativeLayoutView();
            public RelativeLayoutView relativeLayoutView5 = new RelativeLayoutView();

            public RelativeLayoutView getRelativeLayoutView(int index) {
                RelativeLayoutView returnRelativeLayoutView = null;
                switch (index) {
                    case 0:
                        returnRelativeLayoutView = relativeLayoutView0;
                        break;
                    case 1:
                        returnRelativeLayoutView = relativeLayoutView1;
                        break;
                    case 2:
                        returnRelativeLayoutView = relativeLayoutView2;
                        break;
                    case 3:
                        returnRelativeLayoutView = relativeLayoutView3;
                        break;
                    case 4:
                        returnRelativeLayoutView = relativeLayoutView4;
                        break;
                    case 5:
                        returnRelativeLayoutView = relativeLayoutView5;
                        break;
                    default:
                        break;
                }
                return returnRelativeLayoutView;
            }
        }

        public class RelativeLayoutView {
            public RelativeLayout RelativeLayout;
            public ImageView iv_specialPricesGoods;
            public TextView tv_specialPricesGoodsName;
            public TextView tv_specialPricesGoodsPrices;
            public TextView tv_SaledCount;
        }
    }

    /**
     * 活动商品模块 快捷跳转到别的界面
     **/
    private void toOtherModelBuyView(String modelBuyName) {

        if (SportModel_BuyCrazy.equals(modelBuyName)) {

            return;
        }
        if (SportModel_BuyTimeLimite.equals(modelBuyName)) {
            Intent intent = new Intent(mActivity, SportSaleBuyTimeLimitedActivity.class);
            intent.putExtra(InformationCodeUtil.IntentSportSaleByActivityCurrentShopID
                    , currentShopInfoModel.getDjLsh());
            mActivity.startActivity(intent);

            return;
        }
        if (SportModel_BuyGroup.equals(modelBuyName)) {
            Intent intent = new Intent(mActivity, SportSaleBuyGroupActivity.class);
            intent.putExtra(InformationCodeUtil.IntentSportSaleByActivityCurrentShopID
                    , currentShopInfoModel.getDjLsh());
            mActivity.startActivity(intent);
            return;
        }
        if (SportModel_BuyNewCommend.equals(modelBuyName)) {
            return;
        }
    }

    /**
     * 功能模块  快捷跳转到别的界面
     **/
    private void toOtherFunctionModelView(String functionTypeName) {

        if (FunctionType_RecommendShop.equals(functionTypeName)) {
            Intent intent = new Intent(mActivity, ShopRecommendActivity.class);
            intent.putExtra(InformationCodeUtil.IntentShopRecommendActivityCurrentShopID, mActivity.currentShopID);
            mActivity.startActivity(intent);
            return;
        }
        if (FunctionType_MyOrder.equals(functionTypeName)) {
            Intent intent = new Intent(mActivity, MyOrderActivity.class);
            mActivity.startActivity(intent);
            return;
        }
        if (FunctionType_SettingCenter.equals(functionTypeName)) {
            Intent intent = new Intent(mActivity, SettingCenterActivity.class);
            mActivity.startActivity(intent);
            return;
        }
        if (FunctionType_AgentShopGoods.equals(functionTypeName)) {
            showQueryAgentShopGoodsDialog();
            return;
        }

        if (FunctionType_MyAgentGoods.equals(functionTypeName)) {
            Intent intent = new Intent(mActivity, SearchGoodsMyAgentActivity.class);
            mActivity.startActivity(intent);
            return;
        }
        if (FunctionType_MyShop.equals(functionTypeName)) {
            Intent intent = new Intent(mActivity, MyShopWebActivity.class);
            mActivity.startActivity(intent);
            return;
        }
    }

    protected void showQueryAgentShopGoodsDialog() {

        final FragmentCommonDialog fragmentCommonDialog = new FragmentCommonDialog();
        fragmentCommonDialog.initView("温馨提示", "确定代理当前店铺所有商品吗？", "取消", "确定",
                new FragmentCommonDialog.OnButtonClickListener() {

                    @Override
                    public void OnOkClick() {
                        getData(InformationCodeUtil.methodNameDelegateShopAllGoods);
                    }

                    @Override
                    public void OnCancelClick() {
                    }
                });

        fragmentCommonDialog.show(mActivity.getSupportFragmentManager(), "fragmentCommonDialog");
    }


    /*****
     * @author SHI 特价商品
     *         2016-2-17 15:34:42
     */
    public class AdapterSpecialsPricesGoods extends MyBaseAdapter<GoodsGeneralModel> {

        public AdapterSpecialsPricesGoods(Context mContext,
                                          List<GoodsGeneralModel> listData) {
            super(mContext, listData);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext,
                        R.layout.item_shop_specialspricesgoods_listview, null);
                holder = new ViewHolder();
                holder.iv_specialPricesGoods = (ImageView) convertView.findViewById(R.id.iv_specialPricesGoods);
                holder.tv_specialPricesGoodsTitle = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsTitle);
                holder.tv_specialPricesGoodsName = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsName);
                holder.tv_specialPricesGoodsDesc = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsDesc);
                holder.tv_specialPricesGoodsPrices = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsPrices);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            GoodsGeneralModel currentProductModel = listData.get(position);
            ImagerLoaderUtil.getInstance(mContext).displayMyImage(currentProductModel.getImgUrl(), holder.iv_specialPricesGoods);
            holder.tv_specialPricesGoodsName.setText(currentProductModel.getGoodsName());
            holder.tv_specialPricesGoodsDesc.setText(currentProductModel.getGoodsDesc());
            holder.tv_specialPricesGoodsPrices.setText("￥" + currentProductModel.getMinPrice());

            return convertView;
        }


        private class ViewHolder {
            /**
             * 特价商品图标
             **/
            public ImageView iv_specialPricesGoods;
            /**
             * 特价商品标题
             **/
            public TextView tv_specialPricesGoodsTitle;
            /**
             * 特价商品名称
             **/
            public TextView tv_specialPricesGoodsName;
            /**
             * 特价商品描述
             **/
            public TextView tv_specialPricesGoodsDesc;
            /**
             * 特价商品价格
             **/
            public TextView tv_specialPricesGoodsPrices;
        }

    }

//    private class AdapterSportBuyModel extends MyBaseAdapter<SportBuyModel> {
//
//        public AdapterSportBuyModel(Context mContext, List<SportBuyModel> listData) {
//            super(mContext, listData);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            SportModelViewHolder viewHolder;
//            if (convertView == null) {
//                convertView = View.inflate(mContext, R.layout.item_adapter_buy_goods_model, null);
//                viewHolder = new SportModelViewHolder();
//                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
//                viewHolder.tv_modelNameByChina = (TextView) convertView.findViewById(R.id.tv_modelNameByChina);
//                viewHolder.tv_modelNameByEnglish = (TextView) convertView.findViewById(R.id.tv_modelNameByEnglish);
//                viewHolder.linearLayout_timeLimited = (LinearLayout) convertView.findViewById(R.id.linearLayout_timeLimited);
//                viewHolder.tv_hour = (TextView) convertView.findViewById(R.id.tv_hour);
//                viewHolder.tv_minute = (TextView) convertView.findViewById(R.id.tv_minute);
//                viewHolder.tv_second = (TextView) convertView.findViewById(R.id.tv_second);
//                viewHolder.tv_sportOnGoing = (TextView) convertView.findViewById(R.id.tv_sportOnGoing);
//                viewHolder.iv_goodsImages = (ImageView) convertView.findViewById(R.id.iv_goodsImages);
//                viewHolder.tv_goodsPrice = (TextView) convertView.findViewById(R.id.tv_goodsPrice);
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (SportModelViewHolder) convertView.getTag();
//            }
//
//            SportBuyModel buyModel = null;
//            viewHolder.iv_icon.setImageResource(buyModel.getModelIcon());
//            ImagerLoaderUtil.getInstance(mContext).displayMyImage(buyModel.getImageUrl(), viewHolder.iv_goodsImages);
//            viewHolder.tv_modelNameByChina.setText(buyModel.getModelNameByChina());
//            viewHolder.tv_modelNameByEnglish.setText(buyModel.getModelNameByEnglish());
//            viewHolder.tv_goodsPrice.setText("" + buyModel.getModelGoodsPrices());
//            updateTextView(viewHolder, buyModel);
//            return convertView;
//        }
//
//
//        /****
//         * 刷新倒计时控件
//         */

//
//    /**
//     * 活动商品模块
//     **/
//    private class SportBuyModel {
//        /**
//         * 模块图标
//         **/
//        private int modelIcon;
//        /**
//         * 模块中文名
//         **/
//        private String modelNameByChina;
//        /**
//         * 模块英文名
//         **/
//        private String modelNameByEnglish;
//        /**
//         * 开始时间
//         **/
//        private String modelTimeToBegin;
//        /**
//         * 结束时间
//         **/
//        private String modelTimeToFinish;
//        /**
//         * 产品url
//         **/
//        private String imageUrl;
//        /**
//         * 产品价格
//         **/
//        private int modelGoodsPrices;
//
//        public int getModelIcon() {
//            return modelIcon;
//        }
//
//        public void setModelIcon(int modelIcon) {
//            this.modelIcon = modelIcon;
//        }
//
//        public String getModelNameByChina() {
//            return modelNameByChina;
//        }
//
//        public void setModelNameByChina(String modelNameByChina) {
//            this.modelNameByChina = modelNameByChina;
//        }
//
//        public String getModelNameByEnglish() {
//            return modelNameByEnglish;
//        }
//
//        public void setModelNameByEnglish(String modelNameByEnglish) {
//            this.modelNameByEnglish = modelNameByEnglish;
//        }
//
//        public String getModelTimeToBegin() {
//            return modelTimeToBegin;
//        }
//
//        public void setModelTimeToBegin(String modelTimeToBegin) {
//            this.modelTimeToBegin = modelTimeToBegin;
//        }
//
//        public String getImageUrl() {
//            return imageUrl;
//        }
//
//        public void setImageUrl(String imageUrl) {
//            this.imageUrl = imageUrl;
//        }
//
//        public int getModelGoodsPrices() {
//            return modelGoodsPrices;
//        }
//
//        public void setModelGoodsPrices(int modelGoodsPrices) {
//            this.modelGoodsPrices = modelGoodsPrices;
//        }
//
//        public String getModelTimeToFinish() {
//            return modelTimeToFinish;
//        }
//
//        public void setModelTimeToFinish(String modelTimeToFinish) {
//            this.modelTimeToFinish = modelTimeToFinish;
//        }
//
//        public SportBuyModel(int modelIcon, String modelNameByChina
//                , String modelNameByEnglish, String modelTimeToBegin
//                , String modelTimeToFinish, String imageUrl
//                , int modelGoodsPrices) {
//            this.modelNameByChina = modelNameByChina;
//            this.modelIcon = modelIcon;
//            this.modelNameByEnglish = modelNameByEnglish;
//            this.modelTimeToBegin = modelTimeToBegin;
//            this.modelTimeToFinish = modelTimeToFinish;
//            this.imageUrl = imageUrl;
//            this.modelGoodsPrices = modelGoodsPrices;
//        }
//    }

    //    }
    private class SportModelViewHolder {
        private RelativeLayout relativeLayout_sportModel;
        private LinearLayout linearLayout_timeLimited;
        private TextView tv_hour;
        private TextView tv_minute;
        private TextView tv_second;
        private ImageView iv_goodsImages;
        private TextView tv_goodsPrice;
        private TextView tv_sportOnGoing;
        private String modelTimeToBegin;
        private String modelTimeToFinish;
    }

    /**
     * 通知对象
     **/
    private class TrumpetNoticeModel {
        private String Content;
        private int ID;
        private String Title;

        public TrumpetNoticeModel(String content, int ID, String title) {
            Content = content;
            this.ID = ID;
            Title = title;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String content) {
            Content = content;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }
    }


}









