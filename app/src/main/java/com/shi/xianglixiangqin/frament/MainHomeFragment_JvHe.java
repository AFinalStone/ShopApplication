package com.shi.xianglixiangqin.frament;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.activity.GoodsDetailGeneralActivity;
import com.shi.xianglixiangqin.activity.IntegralShoppingActivity;
import com.shi.xianglixiangqin.activity.MainActivity;
import com.shi.xianglixiangqin.activity.MyOrderActivity;
import com.shi.xianglixiangqin.activity.SearchGoodsActivity;
import com.shi.xianglixiangqin.activity.SearchGoodsMyAgentActivity;
import com.shi.xianglixiangqin.activity.SettingCenterActivity;
import com.shi.xianglixiangqin.activity.ShopRecommendActivity;
import com.shi.xianglixiangqin.activity.ShopTrumpetListActivity;
import com.shi.xianglixiangqin.activity.SportSaleBuyCrazyActivity;
import com.shi.xianglixiangqin.activity.SportSaleBuyTimeLimitedActivity;
import com.shi.xianglixiangqin.adapter.FunctionTypeAdapter;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultBaseModel;
import com.shi.xianglixiangqin.model.FunctionTypeModel;
import com.shi.xianglixiangqin.model.GoodsGeneralModel;
import com.shi.xianglixiangqin.model.GoodsGroupModel;
import com.shi.xianglixiangqin.model.GoodsSportModel;
import com.shi.xianglixiangqin.model.ShopInfoModel;
import com.shi.xianglixiangqin.model.TrumpetNoticeModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.DensityUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.TimeUtil;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.shi.xianglixiangqin.view.FragmentOkAndCancelDialog;
import com.shi.xianglixiangqin.view.GoodsAgentConfirmPopWindow;
import com.shi.xianglixiangqin.view.MyTextSliderView;
import com.shi.xianglixiangqin.view.ScrollListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * @author ZHU
 * @action 带你飞首页
 * @date 2015-7-18 上午11:24:43
 */

public class MainHomeFragment_JvHe extends MyBaseFragment<MainActivity> implements
        OnConnectServerStateListener<Integer>, SwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener {

    /**
     * 标题
     **/
    @BindView(R.id.tv_title)
    TextView tv_title;
    /**
     * swipeRefreshLayout
     **/
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    //Toolbar控件
    @BindView(R.id.appBar_layout)
    AppBarLayout appBar_layout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    @BindView(R.id.ibn_backToTop)
    ImageButton ibn_backToTop;
    /**
     * 搜索内容EditText
     **/
    @BindView(R.id.et_searchContext)
    EditText et_searchContext;
    /**
     * 搜索栏
     **/
    @BindView(R.id.linearLayout_search)
    LinearLayout linearLayout_search;

    /**
     * 搜索按钮
     **/
    @BindView(R.id.tv_search)
    TextView tv_search;

    /**
     * 通知
     **/
    @BindView(R.id.tv_informMsg)
    TextView tv_informMsg;
    List<TrumpetNoticeModel> listData_InforMsg;
    /**
     * 轮播图
     **/
    @BindView(R.id.sliderLayout_rollView)
    SliderLayout sliderLayout_rollView;
    List<String> listData_rollView;


    /**
     * 功能模块GridView
     **/
    @BindView(R.id.gridView_functionModel)
    GridView gridView_functionModel;
    private List<FunctionTypeModel> listData_FunctionType;
    private FunctionTypeAdapter functionTypeAdapter;

    private final String FunctionType_RecommendShop = "推荐店铺";
    private final String FunctionType_MyOrder = "我的订单";
    private final String FunctionType_SettingCenter = "设置中心";
    private final String FunctionType_AgentShopGoods = "一键代理";
    private final String FunctionType_MyAgentGoods = "我的代理";
    private final String FunctionType_JFShopping = "积分商城";

    private final String SportModel_BuyCrazy = "疯狂秒杀";
    private final String SportModel_BuyTimeLimited = "限时抢购";
    private final String SportModel_BuyGroup = "品牌团购";
    private final String SportModel_BuyNewCommend = "新品推荐";

    /**
     * 活动商品模块
     **/
    SportModelViewHolder sportModelViewHolder01, sportModelViewHolder02;
    /**
     * 新品推荐外围的控件
     **/
    @BindView(R.id.relativeLayout_moreNewGoodsData)
    RelativeLayout relativeLayout_moreNewGoodsData;
    /**
     * 更多新品推荐
     **/
    @BindView(R.id.tv_moreNewGoodsData)
    TextView tv_moreNewGoodsData;
    /**
     * 新品推荐gridView
     **/
    @BindView(R.id.gridView_newGoodsPush)
    GridView gridView_newGoodsPush;
    /**
     * 新品推荐数据
     **/
    private List<GoodsGeneralModel> listData_newGoodsPush;
    /**
     * 新品推荐适配器
     **/
    AdapterNewGoodsPush adapterNewGoodsPush;

    /**
     * 特价商品ListView
     **/
    @BindView(R.id.linearLayout_specialsPricesGoods)
    LinearLayout linearLayout_specialsPricesGoods;
    @BindView(R.id.listView_specialsPricesGoods)
    ScrollListView listView_specialsPricesGoods;
    private List<GoodsGeneralModel> listDataSpecialsPricesGoods;
    private AdapterSpecialsPricesGoods adapterSpecialsPricesGoods;
    /**
     * 普通商品ListView
     **/
    @BindView(R.id.listView_generalGoods)
    ScrollListView listView_generalGoods;
    private List<GoodsGroupModel> listDataGeneralGoods;
    private AdapterGeneralGoods adapterGeneralGoods;

    public ShopInfoModel currentShopInfoModel;

    /**
     * 当前服务器时间
     **/
    private long time_current;
    /**
     * 是否包含活动商品
     **/
    private boolean IfHaveSportGoods = false;
    private int MESSAGE_01 = 1;
    View rootView;

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
        rootView = View.inflate(mActivity, R.layout.fragment_home_jvhe, null);
        unbinder = ButterKnife.bind(this, rootView);
        tv_title.setText("首页");
        et_searchContext.setInputType(InputType.TYPE_NULL);

        //初始化轮播图
        listData_rollView = new ArrayList<String>();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) sliderLayout_rollView.getLayoutParams();
        layoutParams.width = mActivity.displayDeviceWidth;
        layoutParams.height = mActivity.displayDeviceWidth * 3 / 8;
        sliderLayout_rollView.setLayoutParams(layoutParams);
        // 初始化功能模块数据
        listData_FunctionType = new ArrayList<FunctionTypeModel>();
        listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_recomend_shop, FunctionType_RecommendShop));
        listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_my_order, FunctionType_MyOrder));
        listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_setting_center, FunctionType_SettingCenter));
        listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_agent_shop_goods, FunctionType_AgentShopGoods));
        listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_my_agent_goods, FunctionType_MyAgentGoods));
        listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_my_shop, FunctionType_JFShopping));
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


        sportModelViewHolder02 = new SportModelViewHolder();
        sportModelViewHolder02.relativeLayout_sportModel = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_sportModel02);
        sportModelViewHolder02.linearLayout_timeLimited = (LinearLayout) rootView.findViewById(R.id.linearLayout_timeLimited02);
        sportModelViewHolder02.tv_hour = (TextView) rootView.findViewById(R.id.tv_hour02);
        sportModelViewHolder02.tv_minute = (TextView) rootView.findViewById(R.id.tv_minute02);
        sportModelViewHolder02.tv_second = (TextView) rootView.findViewById(R.id.tv_second02);
        sportModelViewHolder02.tv_sportOnGoing = (TextView) rootView.findViewById(R.id.tv_sportOnGoing02);
        sportModelViewHolder02.iv_goodsImages = (ImageView) rootView.findViewById(R.id.iv_goodsImages02);
        sportModelViewHolder01.relativeLayout_sportModel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SportSaleBuyCrazyActivity.class);
                intent.putExtra(InformationCodeUtil.IntentSportSaleByActivityCurrentShopID
                        , MyApplication.getmCustomModel(mActivity).getCurrentBrowsingShopID());
                mActivity.startActivity(intent);
            }
        });
        sportModelViewHolder02.relativeLayout_sportModel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SportSaleBuyTimeLimitedActivity.class);
                intent.putExtra(InformationCodeUtil.IntentSportSaleByActivityCurrentShopID
                        , MyApplication.getmCustomModel(mActivity).getCurrentBrowsingShopID());
                mActivity.startActivity(intent);
            }
        });

        //初始化新品推荐数据源
        listData_newGoodsPush = new ArrayList<GoodsGeneralModel>();
        adapterNewGoodsPush = new AdapterNewGoodsPush(mActivity, listData_newGoodsPush);
        gridView_newGoodsPush.setAdapter(adapterNewGoodsPush);
        gridView_newGoodsPush.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(mActivity, GoodsDetailGeneralActivity.class);
                intent.putExtra(InformationCodeUtil.IntentGoodsID, listData_newGoodsPush.get(position).getDjLsh());
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
        swipeRefreshLayout.setProgressViewEndTarget(false, DensityUtil.dip2px(mActivity, 150));

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

        appBar_layout.addOnOffsetChangedListener(this);
        return rootView;
    }

    @Override
    public void initData() {
        connectSuccessFlag = true;
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            swipeRefreshLayout.setRefreshing(false);
        } else {
            if (!connectSuccessFlag) {
                initData();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        handler_timeCurrent.removeCallbacksAndMessages(null);
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
        getData();
    }

    private void getData() {

        String methodName = InformationCodeUtil.methodNameStoreHomeIndexExt;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("shopID", MyApplication.getmCustomModel(mActivity).getCurrentBrowsingShopID());
        ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
                mActivity, this, soapObject, methodName);
        connectGoodsServiceAsyncTask.initProgressDialog(false);
        connectGoodsServiceAsyncTask.execute();

    }

    @OnClick({R.id.et_searchContext, R.id.tv_search, R.id.tv_informMsg, R.id.ibn_backToTop})
    public void onClick(View v) {
        Intent mIntent = null;
        switch (v.getId()) {
            case R.id.ibn_backToTop:
                nestedScrollView.smoothScrollTo(0, 0);
                break;
            case R.id.tv_informMsg:
                mIntent = new Intent(mActivity, ShopTrumpetListActivity.class);
                mIntent.putExtra(InformationCodeUtil.IntentShopTrumpetListInformMsg, (Serializable) listData_InforMsg);
                startActivity(mIntent);
                break;
            case R.id.et_searchContext:
            case R.id.tv_search:
                mIntent = new Intent(mActivity, SearchGoodsActivity.class);
                mIntent.putExtra(InformationCodeUtil.IntentSearchGoodsCurrentShopID, MyApplication.getmCustomModel(mActivity).getCurrentBrowsingShopID());
                mActivity.startActivity(mIntent);
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
                refreshTitleLogo(currentShopInfoModel);
                refreshTrumpet(currentShopInfoModel.getNotices(), gson);
                refreshSportGoods(currentShopInfoModel.getActs(), gson);
                refreshShopGeneralGoods(currentShopInfoModel.getModules(), gson);
                refreshRollView(currentShopInfoModel.getAdvList());
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            } catch (Exception e) {
                e.printStackTrace();
                connectSuccessFlag = false;
            }
            return;
        }

        if (methodName == InformationCodeUtil.methodNameDelegateShopAllGoods) {
            try {
                JSONObject jsonObject = new JSONObject(returnString);
                ToastUtil.show(mActivity, jsonObject.getString("Msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Integer state, boolean whetherRefresh) {
        if (methodName == InformationCodeUtil.methodNameStoreHomeIndexExt) {
            if (swipeRefreshLayout != null)
                swipeRefreshLayout.setRefreshing(false);
            connectSuccessFlag = false;
            ToastUtil.show(mActivity, returnStrError);
            return;
        }

        if (methodName == InformationCodeUtil.methodNameDelegateShopAllGoods) {
            ToastUtil.show(mActivity, returnStrError);
            return;
        }
    }

    @Override
    public void connectServiceCancelled(String returnString,
                                        String methodName, Integer state, boolean whetherRefresh) {
        if (methodName == InformationCodeUtil.methodNameDelegateShopAllGoods) {
            if (returnString.contains("代理成功")) {
                ToastUtil.show(mActivity, "成功代理本店所有商品");
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
        LogUtil.LogShitou("公告内容", strNotices);
        try {
            listData_InforMsg = gson.fromJson(strNotices, new TypeToken<List<TrumpetNoticeModel>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String strTrumpet = "";
        if (listData_InforMsg == null) {
            strTrumpet = "暂无通知";
        } else {
            for (int i = 0; i < listData_InforMsg.size(); i++) {
                strTrumpet += listData_InforMsg.get(i).getTitle() + "                  ";
            }
        }
        tv_informMsg.setText(strTrumpet);
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

            sportModelViewHolder02.linearLayout_timeLimited.setVisibility(View.INVISIBLE);
            sportModelViewHolder02.tv_sportOnGoing.setVisibility(View.INVISIBLE);
            sportModelViewHolder02.iv_goodsImages.setImageResource(R.drawable.icon_sport_model_buy_time_limit_flag_nothing);

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
                        time_current = TimeUtil.getTimeDate(goodsSportModel.getPresentTime()).getTime();
                        sportModelViewHolder01.modelTimeToBegin = goodsSportModel.getBeginTime();
                        sportModelViewHolder01.modelTimeToFinish = goodsSportModel.getEndTime();
                        ImagerLoaderUtil.getInstance(mActivity).displayMyImage(goodsSportModel.getImages().get(0), sportModelViewHolder01.iv_goodsImages);
                    }
                }
                if ("限时抢购".equals(mJSONResultModel.getTitle())) {
                    List<GoodsSportModel> list = mJSONResultModel.getList();
                    if (list != null) {
                        IfHaveSportGoods = true;
                        GoodsSportModel goodsSportModel = list.get(0);
                        time_current = TimeUtil.getTimeDate(goodsSportModel.getPresentTime()).getTime();
                        sportModelViewHolder02.modelTimeToBegin = goodsSportModel.getBeginTime();
                        sportModelViewHolder02.modelTimeToFinish = goodsSportModel.getEndTime();
                        ImagerLoaderUtil.getInstance(mActivity).displayMyImage(goodsSportModel.getImages().get(0), sportModelViewHolder02.iv_goodsImages);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (IfHaveSportGoods)
            handler_timeCurrent.sendEmptyMessageDelayed(MESSAGE_01, 800);
    }

    public void updateTextView(SportModelViewHolder holder) {

        if (holder.modelTimeToBegin == null || holder.modelTimeToFinish == null) {
            holder.linearLayout_timeLimited.setVisibility(View.INVISIBLE);
            holder.tv_sportOnGoing.setVisibility(View.INVISIBLE);
            return;
        }

        long time_remainToBegin = (TimeUtil.getTimeDate(holder.modelTimeToBegin).getTime() - time_current) / 1000;
        long time_remainToFinish = (TimeUtil.getTimeDate(holder.modelTimeToFinish).getTime() - time_current) / 1000;

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
        time_hour = time_remainToBegin / 3600 % 24;

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
            relativeLayout_moreNewGoodsData.setVisibility(View.GONE);
            linearLayout_specialsPricesGoods.setVisibility(View.GONE);
            listDataGeneralGoods.clear();
            listData_newGoodsPush.clear();
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
//                    LogUtil.LogShitou("新品推荐",jSONResultModel);
                    refreshNewProductsRecommendation(jSONResultModel);
                    continue;
                }

                refreshGeneralAllGoods(jSONResultModel);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 刷新新品推荐内容
     **/
    private void refreshNewProductsRecommendation(JSONResultBaseModel<GoodsGeneralModel> mJSONProductModel) {
        try {
            listData_newGoodsPush.clear();
            listData_newGoodsPush.addAll(mJSONProductModel.getList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listData_newGoodsPush.size() > 0) {
            relativeLayout_moreNewGoodsData.setVisibility(View.VISIBLE);
        }
        adapterNewGoodsPush.notifyDataSetChanged();
    }

    /**
     * 刷新特价商品内容
     **/
    private void refreshSpecialsPricesGoods(JSONResultBaseModel<GoodsGeneralModel> mJSONProductModel) {
        try {
            listDataSpecialsPricesGoods.clear();
            listDataSpecialsPricesGoods.addAll(mJSONProductModel.getList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listDataSpecialsPricesGoods.size() > 0) {
            linearLayout_specialsPricesGoods.setVisibility(View.VISIBLE);
        }
        adapterSpecialsPricesGoods.notifyDataSetChanged();
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
            sliderLayout_rollView.setVisibility(View.VISIBLE);
            MyTextSliderView textSliderView = new MyTextSliderView(mActivity);
            //初始化轮播图的Item
            textSliderView.image(R.mipmap.background_rollview_default)
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            sliderLayout_rollView.addSlider(textSliderView);
        } else {
            sliderLayout_rollView.setVisibility(View.VISIBLE);
            for (int i = 0; i < listData_rollView.size(); i++) {
                MyTextSliderView textSliderView = new MyTextSliderView(mActivity);
                //初始化轮播图的Item
                String imageUrl = listData_rollView.get(i);
                if(!StringUtil.isEmpty(imageUrl)){
                    textSliderView.image(imageUrl)
                            .setScaleType(BaseSliderView.ScaleType.Fit);
                }

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
            ViewParentHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_adapter_home_general_goods_listview_parent, null);
                holder = new ViewParentHolder();
                holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
                holder.tv_moreNewGoodsData = (TextView) convertView.findViewById(R.id.tv_moreNewGoodsData);
                holder.gridView = (GridView) convertView.findViewById(R.id.gridView);
                convertView.setTag(holder);
            } else {
                holder = (ViewParentHolder) convertView.getTag();
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
            AdapterGridView adapterGridView = new AdapterGridView(mActivity, currentProductModelGroup.getListProductModels());
            holder.gridView.setAdapter(adapterGridView);
            holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, GoodsDetailGeneralActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentGoodsID
                            , currentProductModelGroup.getListProductModels().get(position).getDjLsh());
                    mContext.startActivity(intent);
                }
            });
            return convertView;
        }

        private class ViewParentHolder {
            /**
             * 商品类型
             **/
            public TextView tv_type;
            /**
             * 更多
             **/
            public TextView tv_moreNewGoodsData;
            /**
             * 商品
             **/
            public GridView gridView;
        }

        private class AdapterGridView extends MyBaseAdapter<GoodsGeneralModel> {

            public AdapterGridView(Context mContext, List<GoodsGeneralModel> listData) {
                super(mContext, listData);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewChildHolder holder;
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.item_adapter_home_general_goods_listview_child, null);
                    holder = new ViewChildHolder();
                    holder.iv_generalGoods = (ImageView) convertView.findViewById(R.id.iv_generalGoods);
                    holder.tv_generalGoodsName = (TextView) convertView.findViewById(R.id.tv_generalGoodsName);
                    holder.tv_generalGoodsPrices = (TextView) convertView.findViewById(R.id.tv_generalGoodsPrices);
                    holder.tv_saleCount = (TextView) convertView.findViewById(R.id.tv_saleCount);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewChildHolder) convertView.getTag();
                }
                final GoodsGeneralModel currentGeneralModel = listData.get(position);
                ImagerLoaderUtil.getInstance(mContext).displayMyImage(currentGeneralModel.getImgUrl(),
                        holder.iv_generalGoods);
                holder.tv_generalGoodsName.setText(currentGeneralModel.getGoodsName());
                holder.tv_generalGoodsPrices.setText("￥" + (int) currentGeneralModel.getMinPrice());
                holder.tv_saleCount.setText("已售" + currentGeneralModel.getSaledCount() + "件");
                return convertView;
            }

            private class ViewChildHolder {
                /**
                 * 图片
                 **/
                public ImageView iv_generalGoods;
                /**
                 * 商品名称
                 **/
                public TextView tv_generalGoodsName;
                /**
                 * 价格
                 **/
                public TextView tv_generalGoodsPrices;
                /**
                 * 已售数量
                 **/
                public TextView tv_saleCount;
            }
        }

    }

    /**
     * 功能模块  快捷跳转到别的界面
     **/
    private void toOtherFunctionModelView(String functionTypeName) {

        if (FunctionType_RecommendShop.equals(functionTypeName)) {
            if (MyApplication.getmCustomModel(mActivity).getLoginShopID()
                    == MyApplication.getmCustomModel(mActivity).getCurrentBrowsingShopID()) {
                Intent intent = new Intent(mActivity, ShopRecommendActivity.class);
                mActivity.startActivity(intent);
            } else {
                FragmentOkAndCancelDialog fcd = new FragmentOkAndCancelDialog();
                fcd.initView("温馨提示", "确定返回首页吗?", "取消", "确定", new FragmentOkAndCancelDialog.OnButtonClickListener() {
                    @Override
                    public void OnOkClick() {
                        MyApplication.getmCustomModel(mActivity)
                                .setCurrentBrowsingShopID(MyApplication.getmCustomModel(mActivity).getLoginShopID());
                        initData();
                    }

                    @Override
                    public void OnCancelClick() {

                    }
                });
                fcd.show(mActivity.getSupportFragmentManager(), "FragmentOkAndCancelDialog");
            }
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
        if (FunctionType_JFShopping.equals(functionTypeName)) {
            mActivity.startActivity(new Intent(mActivity, IntegralShoppingActivity.class));
            return;
        }
    }

    protected void showQueryAgentShopGoodsDialog() {

        GoodsAgentConfirmPopWindow pop = new GoodsAgentConfirmPopWindow(mActivity,
                new GoodsAgentConfirmPopWindow.onAgentConfirmListener() {
                    @Override
                    public void agentGoods(int gowhere) {
                        toAgentGoods(gowhere);
                    }
                });
        pop.showAtLocation(rootView, Gravity.CENTER, 0, 0);

    }

    private void toAgentGoods(int gowhere) {
        String methodName = InformationCodeUtil.methodNameDelegateShopAllGoods;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("customID", MyApplication.getmCustomModel(mActivity).getDjLsh());
        soapObject.addProperty("openKey", MyApplication.getmCustomModel(mActivity).getOpenKey());
        soapObject.addProperty("shopID", MyApplication.getmCustomModel(mActivity).getCurrentBrowsingShopID());
        soapObject.addProperty("goodsIDS", "");
        soapObject.addProperty("gowhere", gowhere);

        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mActivity, this, soapObject, methodName);
        connectCustomServiceAsyncTask.initProgressDialog(true, "正在代理...");
        connectCustomServiceAsyncTask.execute();
    }


    /**
     * @author SHI
     *         2016-2-17 15:34:28
     *         新品推荐适配器
     */
    public class AdapterNewGoodsPush extends MyBaseAdapter<GoodsGeneralModel> {

        public AdapterNewGoodsPush(Context mContext, List<GoodsGeneralModel> listData) {
            super(mContext, listData);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_adapter_newgoods_push, null);
                holder = new ViewHolder();
                holder.iv_specialPricesGoods = (ImageView) convertView.findViewById(R.id.iv_specialPricesGoods);
                holder.tv_specialPricesGoodsName = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsName);
                holder.tv_specialPricesGoodsPrices = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsPrices);
                holder.btn_seeDataDetail = (Button) convertView.findViewById(R.id.btn_seeDataDetail);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final GoodsGeneralModel currentProductModel = listData.get(position);
            ImagerLoaderUtil.getInstance(mContext).displayMyImage(currentProductModel.getImgUrl(), holder.iv_specialPricesGoods);
            holder.tv_specialPricesGoodsName.setText(currentProductModel.getGoodsName());
            holder.tv_specialPricesGoodsPrices.setText("￥" + (int) currentProductModel.getMinPrice());
            holder.btn_seeDataDetail.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, GoodsDetailGeneralActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentGoodsID, currentProductModel.getDjLsh());
                    mActivity.startActivity(intent);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            /**
             * 特价商品图标
             **/
            ImageView iv_specialPricesGoods;
            /**
             * 特价商品名称
             **/
            TextView tv_specialPricesGoodsName;
            /**
             * 特价商品价格
             **/
            TextView tv_specialPricesGoodsPrices;
            /**
             * 查看详情
             **/
            Button btn_seeDataDetail;
        }

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
            holder.tv_specialPricesGoodsPrices.setText("￥" + (int) currentProductModel.getMinPrice());

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

    private class SportModelViewHolder {
        private RelativeLayout relativeLayout_sportModel;
        private LinearLayout linearLayout_timeLimited;
        private TextView tv_hour;
        private TextView tv_minute;
        private TextView tv_second;
        private ImageView iv_goodsImages;
        private TextView tv_sportOnGoing;
        private String modelTimeToBegin;
        private String modelTimeToFinish;
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

}









