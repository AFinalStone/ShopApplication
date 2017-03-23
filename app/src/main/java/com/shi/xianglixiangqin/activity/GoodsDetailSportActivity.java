package com.shi.xianglixiangqin.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.model.GoodsGeneralModel;
import com.shi.xianglixiangqin.model.GoodsSportModel;
import com.shi.xianglixiangqin.rongyun.ConversationGeneralActivity;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.DensityUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.TimeUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.model.Conversation;

/**
 * @author SHI
 * @action 活动商品详情页面
 * @date 2015-7-29 下午8:35:55
 */
public class GoodsDetailSportActivity extends MyBaseActivity implements
        OnConnectServerStateListener<Integer>, OnClickListener {

    /**
     * 返回控件
     **/
    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    /**
     * 标题
     **/
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    /**
     * 商品图片
     **/
    @BindView(R.id.viewPager_goodsImages)
    ViewPager viewPager_goodsImages;
    List<String> listData_goodsImages = new ArrayList<>();
    MyPagerAdapter myPagerAdapter;
    /**
     * 聊天控件
     **/
    @BindView(R.id.iv_openConverSation)
    ImageView iv_openConverSation;
    /**
     * 商品名称
     **/
    @BindView(R.id.tv_goodsName)
    TextView tv_goodsName;
    /**
     * 商品价格
     **/
    @BindView(R.id.tv_goodsNewPrices)
    TextView tv_goodsNewPrices;
    /**
     * 商品原来价格
     **/
    @BindView(R.id.tv_goodsOriginalPrice)
    TextView tv_goodsOriginalPrice;
    /**
     * 可使用的飞币
     **/
    @BindView(R.id.tv_flyCoinCanUsed)
    TextView tv_flyCoinCanUsed;
    /**
     * 秒杀开始时间
     **/
    @BindView(R.id.tv_timeToBegin)
    TextView tv_timeToBegin;

    /**
     * 疯狂秒杀
     **/
    @BindView(R.id.linearLayout_sportMesKill)
    LinearLayout linearLayout_sportMesKill;

    /**
     * 疯狂秒杀活动状态描述
     **/
    @BindView(R.id.tv_sportStateDesc)
    TextView tv_sportStateDesc;

    /**
     * 团购中心
     **/
    @BindView(R.id.linearLayout_sportGroup)
    LinearLayout linearLayout_sportGroup;
    /**
     * 已参团数量和成团最低数量
     **/
    @BindView(R.id.tv_numHaveJoinGroup)
    TextView tv_numHaveJoinGroup;
    /**
     * 活动结束时间
     **/
    @BindView(R.id.tv_timeToFinish)
    TextView tv_timeToFinish;

    /**
     * 限时抢购
     **/
    @BindView(R.id.linearLayout_sportTimeLimited)
    LinearLayout linearLayout_sportTimeLimited;
    /**
     * 已抢数量
     **/
    @BindView(R.id.tv_numHaveRob)
    TextView tv_numHaveRob;
    /**
     * 活动距离结束的时间
     **/
    @BindView(R.id.tv_timeRemain)
    TextView tv_timeRemain;

    /**
     * 商品介绍 商品参数 包装售后
     **/
    @BindView(R.id.rg_goodsGroup)
    RadioGroup rg_goodsGroup;
    /**
     * 商品介绍 商品参数 包装售后
     **/
    @BindView(R.id.webview_goodsDetail)
    WebView webview_goodsDetail;
    /**
     * 购买活动商品
     **/
    @BindView(R.id.btn_justToBuy)
    Button btn_justToBuy;

    /**
     * 当前商品活动ID
     **/
    int GoodsPlatformActionID;
    /**
     * 当前商品活动类型ID
     **/
    int GoodsPlatformActionType;
    /**
     * 当前商品详细信息
     **/
    public GoodsSportModel mGoodsInfoActModel = new GoodsSportModel();
    public GoodsGeneralModel mGoodsGeneralModel;
    /**
     * 当倒计时剩余时间
     **/
    long time_current;

    // 这里很重要，使用Handler的延时效果，每隔一秒刷新一下适配器，以此产生倒计时效果
    Handler handler_timeCurrent = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            time_current = time_current + 1000;
            LogUtil.LogShitou("商品秒杀当前时间" + System.currentTimeMillis() / 1000);
            // 当前界面为疯狂秒杀
            if (InformationCodeUtil.PlatformActionType_MesKill == msg.what) {
                updateMesKillTextView(time_current);
                handler_timeCurrent.sendEmptyMessageDelayed(
                        InformationCodeUtil.PlatformActionType_MesKill, 1000);
                return;
            }
            // 当前界面为精品团购
            if (InformationCodeUtil.PlatformActionType_GroupCentre == msg.what) {
                updateGroupCentreTextView(time_current);
                handler_timeCurrent.sendEmptyMessageDelayed(
                        InformationCodeUtil.PlatformActionType_GroupCentre, 1000);
                return;
            }
            // 当前界面为限时抢购
            if (InformationCodeUtil.PlatformActionType_SaleByTimeLimited == msg.what) {
                updateTimeLimitedTextView(time_current);
                handler_timeCurrent.sendEmptyMessageDelayed(
                        InformationCodeUtil.PlatformActionType_SaleByTimeLimited, 1000);
                return;
            }
        }
    };
    /**
     * webview控件
     **/
    String htmlHeader = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
            + "<head>"
            + "<meta name=\"viewport\" content=\"width=device-width,height=device-height,inital-scale=1.0,minimum-scale=1.0,maximum-scale=3.0,user-scalable=yes\" />"
            + "<meta id=\"vp\" name=\"viewport\" content=\"width=device-width, user-scalable=yes,maximum-scale=3.0,initial-scale=1.0,minimum-scale=1.0\" />"
            + "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
            + "<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\" />"
            + "<meta name=\"format-detection\" content=\"telephone=no\" />"
            + "<style type='text/css'>"
            + "img{max-width:100%;height:auto;}"
            + "</style>"
            + "</head>"
            + "<body style='padding:35px 10px 0 10px;margin:0 0 0 0;line-height:25px;'>";

    String htmlRoot = "</body></html>";

    @Override
    public void initView() {
        setContentView(R.layout.activity_goods_detail_sport);
        ButterKnife.bind(mContext);
        iv_titleLeft.setVisibility(View.VISIBLE);
        iv_titleLeft.setOnClickListener(this);
        tv_title.setText("商品详情");
//		iv_goodsImages.setLayoutParams(new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.MATCH_PARENT, displayDeviceWidth));		
        btn_justToBuy.setOnClickListener(this);
        iv_openConverSation.setOnClickListener(this);
        rg_goodsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // String str = "style=/"width:100%;height:auto/"";
                // String str =
                // "<style>img{ max-width:80%; height:auto;	}</style>";
                switch (checkedId) {
                    // 获取商品详情描述
                    case R.id.rb_goodsDesc:
                        webview_goodsDetail.loadDataWithBaseURL(
                                null,
                                htmlHeader
                                        + mGoodsInfoActModel.getGoodsDesc()
                                        + htmlRoot, "text/html", "utf-8",
                                null);
//                        webview_goodsDetail.loadUrl("http://blog.csdn.net/buliuhu123/article/details/39802833");
                        break;
                    // 获取商品参数
                    case R.id.rb_goodsParameter:
                        webview_goodsDetail.loadDataWithBaseURL(
                                null,
                                htmlHeader
                                        + mGoodsInfoActModel.getGoodsSpec()
                                        + htmlRoot, "text/html", "utf-8",
                                null);
//                        webview_goodsDetail.loadUrl("https://github.com/AFinalStone?page=4&tab=stars");
                        break;
                    // 获取包装售后数据
                    case R.id.rb_goodsPackage:
                        webview_goodsDetail.loadDataWithBaseURL(
                                null,
                                htmlHeader
                                        + mGoodsInfoActModel.getPackage()
                                        + htmlRoot, "text/html", "utf-8",
                                null);
//                        webview_goodsDetail.loadUrl("https://github.com/AFinalStone?page=4&tab=stars");

                        break;

                    default:
                        break;
                }
            }
        });


        WebSettings webSetting = webview_goodsDetail.getSettings();
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setUseWideViewPort(true);

        webSetting.setDefaultZoom(ZoomDensity.FAR);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setPluginState(PluginState.ON);
        // 设置 缓存模式
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webSetting.setDomStorageEnabled(true);
        // 设置是否支持缩放
        webSetting.setBuiltInZoomControls(true);
        // 设置是否支持JavaScript
        webSetting.setJavaScriptEnabled(true);
        // 允许访问文件
        webSetting.setAllowFileAccess(true);
        // 支持缩放
        webSetting.setSupportZoom(true);
        // 隐藏WebView缩放按钮
        webSetting.setDisplayZoomControls(false);

    }

    @Override
    public void initData() {
        mGoodsGeneralModel = null;
        Intent intent = getIntent();
        GoodsPlatformActionID = intent.getIntExtra(InformationCodeUtil.IntentPlatformActionID, -1);
        GoodsPlatformActionType = intent.getIntExtra(InformationCodeUtil.IntentPlatformActionType, -1);
        myPagerAdapter = new MyPagerAdapter();
        viewPager_goodsImages.setAdapter(myPagerAdapter);
        getData(InformationCodeUtil.methodNameGetSingleActProduct);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mGoodsGeneralModel = null;
        GoodsPlatformActionID = intent.getIntExtra(InformationCodeUtil.IntentPlatformActionID, -1);
        GoodsPlatformActionType = intent.getIntExtra(InformationCodeUtil.IntentPlatformActionType, -1);
//		LogUtil.LogShitou("商品编号" + GoodsPlatformActionID);
        getData(InformationCodeUtil.methodNameGetSingleActProduct);
        super.onNewIntent(intent);
    }

    void getData(String methodName) {
        // 获取活动商品 详情
        if (InformationCodeUtil.methodNameGetSingleActProduct
                .equals(methodName)) {
            SoapObject requestSoapObject = new SoapObject(
                    ConnectServiceUtil.NAMESPACE, methodName);
            // int PlatformActionID, int PlatformActionType
            requestSoapObject.addProperty("platformActionID",
                    GoodsPlatformActionID);
            requestSoapObject.addProperty("platformActionType ",
                    GoodsPlatformActionType);
            ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
                    mContext, this, requestSoapObject, methodName);
            connectGoodsServiceAsyncTask.execute();
            return;
        }

        if (InformationCodeUtil.methodNameSPCount.equals(methodName)) {
            SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
            requestSoapObject.addProperty("spid", GoodsPlatformActionID);
            requestSoapObject.addProperty("dpid", MyApplication.getmCustomModel(mContext).getLoginShopID());
            requestSoapObject.addProperty("fuid", MyApplication.getmCustomModel(mContext).getDjLsh());
            requestSoapObject.addProperty("ftool", 2);
            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
                    (mContext, this, requestSoapObject, methodName);
            connectCustomServiceAsyncTask.initProgressDialog(false);
            connectCustomServiceAsyncTask.execute();
            return;
        }
    }

    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, Integer state, boolean whetherRefresh) {
        LogUtil.LogShitou("活动商品", returnString);
        // 获取活动商品 详情成功
        if (InformationCodeUtil.methodNameGetSingleActProduct
                .equals(methodName)) {
            try {
                Gson gson = new Gson();
                mGoodsInfoActModel = null;
                mGoodsInfoActModel = gson.fromJson(
                        returnString, GoodsSportModel.class);
                mGoodsInfoActModel
                        .setPlatformActionType(GoodsPlatformActionType);
                mGoodsGeneralModel = getOriginalGoodsModel(mGoodsInfoActModel);
                RefreshGoodsData();
                getData(InformationCodeUtil.methodNameSPCount);
            } catch (JsonSyntaxException e) {
                ToastUtil.show(mContext, "获取宝贝详情失败");
                finish();
            }
        }
    }

    // 活动商品对象转普通商品对象没有用到的活动商品对象字段
    // /**原来的价格**/
    //  double OriginalPrice;
    // /**最小成团数**/
    //  int MinQuantity;
    // /**最大成团数**/
    //  int MaxQuantity;
    // /**是否成团**/
    //  boolean IsGrouped;
    // /**开始时间**/
    //  String BeginTime;
    // /**结束时间**/
    //  String EndTime;
    // /**当前时间**/
    //  String PresentTime;
    // /**活动类型**/
    //  int PlatformActionID;
    // /**商品价格**/
    //  double Price;
    // /**活动商品参与数量**/
    //  int Quantity;
    // /**用户限购量**/
    //  int UserQuantity;

    /**
     * 类型转换函数
     *
     * @param mGoodsInfoActModel
     * @return
     */
    private GoodsGeneralModel getOriginalGoodsModel(GoodsSportModel mGoodsInfoActModel) {
        if (mGoodsInfoActModel == null) {
            return null;
        }
        GoodsGeneralModel mProductModel = new GoodsGeneralModel();
        mProductModel.setShopPhoneNum(mGoodsInfoActModel.getShopPhoneNum());
        mProductModel.setImages(mGoodsInfoActModel.getImages());
        mProductModel.setShopName(mGoodsInfoActModel.getShopName());
        mProductModel.setAgentID(mGoodsInfoActModel.getAgentID());
        mProductModel.setClassID(mGoodsInfoActModel.getClassID());
        mProductModel.setDjLsh(mGoodsInfoActModel.getDjLsh());
        mProductModel.setMinPrice(mGoodsInfoActModel.getPrice());
        mProductModel.setMaxPrice(mGoodsInfoActModel.getPrice());
        mProductModel.setFlyCoin(mGoodsInfoActModel.getFlyCoin());
        mProductModel.setGoodsName(mGoodsInfoActModel.getGoodsName());
        mProductModel.setGoodsDesc(mGoodsInfoActModel.getGoodsDesc());
        mProductModel.setGoodsSpec(mGoodsInfoActModel.getGoodsSpec());
        mProductModel.setPackage(mGoodsInfoActModel.getPackage());
        mProductModel.setShopID(mGoodsInfoActModel.getShopID());
        mProductModel.setUserID(mGoodsInfoActModel.getUserID());
        mProductModel.setPlatformActionID(mGoodsInfoActModel
                .getPlatformActionID());
        mProductModel.setPlatformActionType(mGoodsInfoActModel
                .getPlatformActionType());
        mProductModel.setUserQuantity(mGoodsInfoActModel.getUserQuantity());
        if (GoodsPlatformActionType == InformationCodeUtil.PlatformActionType_GroupCentre) {
            mProductModel.setStoneCount(mGoodsInfoActModel.getMaxQuantity());
        } else {
            mProductModel.setStoneCount(mGoodsInfoActModel.getQuantity());
        }
        return mProductModel;
    }

    /**
     * 收到网络数据，刷新商品详情界面
     **/
    void RefreshGoodsData() {
//		if (mGoodsInfoActModel.getImages() != null
//				&& mGoodsInfoActModel.getImages().size() > 0) {
//			ImagerLoaderUtil.getInstance(mContext).displayMyImage(
//					mGoodsInfoActModel.getImages().get(0), iv_goodsImages);
//		}
        listData_goodsImages.clear();
        listData_goodsImages.addAll(mGoodsInfoActModel.getImages());
        myPagerAdapter.notifyDataSetChanged();
        tv_goodsName.setText(mGoodsInfoActModel.getGoodsName());
        DecimalFormat dcmFmt = new DecimalFormat("0.00");
        String goodsNewPrice = dcmFmt.format(mGoodsInfoActModel.getPrice());
        String goodsOriginalPrice = dcmFmt.format(mGoodsInfoActModel
                .getOriginalPrice());
        tv_goodsNewPrices.setText("￥" + goodsNewPrice);
        tv_goodsOriginalPrice.setText("￥" + goodsOriginalPrice);
        tv_goodsOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_flyCoinCanUsed
                .setText("可用" + mGoodsInfoActModel.getFlyCoin() + "飞币");
        rg_goodsGroup.check(R.id.rb_goodsDesc);

        // 开始时间
        Date date_begin = TimeUtil.getTimeDate(mGoodsInfoActModel
                .getBeginTime());
        // 结束时间
        Date date_end = TimeUtil.getTimeDate(mGoodsInfoActModel.getEndTime());
        // 当前时间
        Date date_current = TimeUtil.getTimeDate(mGoodsInfoActModel
                .getPresentTime());

        LogUtil.LogShitou("", mGoodsInfoActModel.toString());
        switch (GoodsPlatformActionType) {

            case InformationCodeUtil.PlatformActionType_MesKill:
                tv_title.setText("整点秒杀");
                btn_justToBuy.setText(R.string.clickMesKill);
                btn_justToBuy.setVisibility(View.VISIBLE);
                linearLayout_sportMesKill.setVisibility(View.VISIBLE);
                tv_timeToBegin.setText(TimeUtil.getTimeString(date_begin,
                        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")) + " 开始");
                time_current = date_current.getTime();
                updateMesKillTextView(time_current);
                handler_timeCurrent.sendEmptyMessageDelayed(
                        InformationCodeUtil.PlatformActionType_MesKill, 500);
                break;
            case InformationCodeUtil.PlatformActionType_GroupCentre:
                tv_title.setText("精品团购");
                btn_justToBuy.setText(R.string.justToJoinGroup);
                btn_justToBuy.setVisibility(View.VISIBLE);
                linearLayout_sportGroup.setVisibility(View.VISIBLE);
                tv_numHaveJoinGroup.setText(mGoodsInfoActModel.getJoinNum()
                        + "件已参团{满" + mGoodsInfoActModel.getMinQuantity() + "成团}");
                if (date_end != null) {
                    tv_timeToFinish.setText(TimeUtil.getTimeString(date_end,
                            new SimpleDateFormat("M月dd日 HH:mm")) + "结束");
                }
                time_current = date_current.getTime();
                updateGroupCentreTextView(time_current);
                handler_timeCurrent.sendEmptyMessageDelayed(
                        InformationCodeUtil.PlatformActionType_GroupCentre, 500);
                break;
            case InformationCodeUtil.PlatformActionType_SaleByTimeLimited:
                tv_title.setText("限时抢购");
                btn_justToBuy.setText(R.string.justToRob);
                btn_justToBuy.setVisibility(View.VISIBLE);
                linearLayout_sportTimeLimited.setVisibility(View.VISIBLE);
                tv_numHaveRob.setText("已抢数量：" + mGoodsInfoActModel.getPurchaseNum());
                time_current = date_current.getTime();
                updateTimeLimitedTextView(time_current);
                handler_timeCurrent.sendEmptyMessageDelayed(
                        InformationCodeUtil.PlatformActionType_SaleByTimeLimited, 500);
                break;

            default:
                finish();
                break;
        }
    }

    /****
     * 刷新整点秒杀倒计时控件
     */
    public void updateMesKillTextView(long time_current) {

        long time_begin = TimeUtil.getTimeDate(mGoodsInfoActModel.getBeginTime()).getTime();
        long time_end = TimeUtil.getTimeDate(mGoodsInfoActModel.getEndTime())
                .getTime();
        // 活动截至时间还未到
        if (time_end > time_current) {
            long time_remains = time_begin - time_current;
            if (time_remains <= 0) {

                if (mGoodsInfoActModel.isIsRunning()) {
                    // 活动开始，可以秒杀
                    btn_justToBuy.setEnabled(true);
                    btn_justToBuy.setText(R.string.clickMesKill);
                    tv_sportStateDesc.setText("活动正在进行");
                } else {
                    // 活动已经结束,商品被抢光
                    btn_justToBuy.setEnabled(false);
                    btn_justToBuy.setText(R.string.haveFinishRob);
                    tv_sportStateDesc.setText("活动已经结束");
                }

            }else{
                // 活动即将开始
                btn_justToBuy.setEnabled(false);
                btn_justToBuy.setText(R.string.justToBegin);
                tv_sportStateDesc.setText("活动尚未开始");
            }

        } else {
            // 活动截至时间已经到了
            btn_justToBuy.setEnabled(false);
            btn_justToBuy.setText(R.string.haveFinish);
            tv_sportStateDesc.setText("活动已经结束");
        }

    }

    /**
     * 刷新精品团购中心数据
     *
     * @param time_current
     */
    void updateGroupCentreTextView(long time_current) {
        LogUtil.LogShitou("结束时间", mGoodsInfoActModel.getEndTime());
        long time_begin = TimeUtil.getTimeDate(
                mGoodsInfoActModel.getBeginTime()).getTime();
        long time_end = TimeUtil.getTimeDate(mGoodsInfoActModel.getEndTime())
                .getTime();

        if (time_begin > time_current) {// 活动开始时间还未到
            btn_justToBuy.setEnabled(false);
            btn_justToBuy.setText(R.string.justToBegin);
        } else {
            if (time_end > time_current) {
                // 团购是否正在进行
                if (mGoodsInfoActModel.isIsRunning()) {
                    btn_justToBuy.setEnabled(true);
                    btn_justToBuy.setText(R.string.justToJoinGroup);
                } else {
                    btn_justToBuy.setEnabled(false);
                    // 团购是否成功
                    if (mGoodsInfoActModel.isIsGrouped()) {
                        btn_justToBuy.setText(R.string.joinGroupSuccess);
                    } else {
                        btn_justToBuy.setText(R.string.joinGroupFailed);
                    }
                }
            } else {
                btn_justToBuy.setEnabled(false);
                // 团购是否成功
                if (mGoodsInfoActModel.isIsGrouped()) {
                    btn_justToBuy.setText(R.string.joinGroupSuccess);
                } else {
                    btn_justToBuy.setText(R.string.joinGroupFailed);
                }
            }
        }

    }

    /****
     * 刷新限时抢购倒计时控件
     */
    public void updateTimeLimitedTextView(long time_current) {
        long time_begin = TimeUtil.getTimeDate(
                mGoodsInfoActModel.getBeginTime()).getTime();
        long time_end = TimeUtil.getTimeDate(mGoodsInfoActModel.getEndTime())
                .getTime();
        tv_timeRemain.setText(TimeUtil.getTimeString(time_end,
                new SimpleDateFormat("截止时间：yyyy-MM-dd hh:mm:ss")));

        if (time_begin > time_current) {// 活动开始时间还未到
            btn_justToBuy.setEnabled(false);
            btn_justToBuy.setText(R.string.justToBegin);

        } else {// 活动开始时间已经到了

            long time_remain = time_end - time_current;
            if (time_remain <= 0) {// 活动已经结束，截止时间到了
                btn_justToBuy.setEnabled(false);
                btn_justToBuy.setText(R.string.haveFinish);
            }else{
                if (mGoodsInfoActModel.isIsRunning()) {
                    // 活动开始，可以秒杀
                    btn_justToBuy.setEnabled(true);
                    btn_justToBuy.setText(R.string.justToRob);
                } else {
                    // 活动已经结束,商品被抢光
                    btn_justToBuy.setEnabled(false);
                    btn_justToBuy.setText(R.string.haveFinishRob);
                }
            }


        }

    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Integer state,
                                     boolean whetherRefresh) {
        // 获取商品 详情
        if (InformationCodeUtil.methodNameGetSingleActProduct.equals(methodName)) {
            ToastUtil.show(mContext, "获取宝贝详情失败,请检查网络状况");
            finish();
        }
    }

    @Override
    public void connectServiceCancelled(String returnString,
                                        String methodName, Integer state, boolean whetherRefresh) {
        // 获取商品 详情
        if (InformationCodeUtil.methodNameGetSingleActProduct.equals(methodName)) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.btn_justToBuy:
                if (mGoodsGeneralModel != null) {
                    intent = new Intent(mContext, GoodsPackageActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentGoodsActivityCurrentGoodsModel, mGoodsGeneralModel);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_bottom, R.anim.not_change);
                }
                break;
            case R.id.iv_openConverSation:
                toConversationView();
                break;
            default:
                break;
        }
    }

    /**
     * 进入会话界面
     */
    void toConversationView() {

        if (mGoodsGeneralModel == null) {
            ToastUtil.show(mContext, "请先获取商品内容");
            return;
        }
        Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
        String targetId = new StringBuffer().append(mGoodsInfoActModel.getUserID()).toString();
        String title = mGoodsInfoActModel.getShopName();

        Uri data = Uri.parse("rong://" + mContext.getApplicationInfo().processName)
                .buildUpon()
                .appendPath("conversation").appendPath(conversationType.getName().toLowerCase())
                .appendQueryParameter(ConversationGeneralActivity.INTENT_TARGETID, targetId)
                .appendQueryParameter(ConversationGeneralActivity.INTENT_TITLE, title)
                .appendQueryParameter(
                        ConversationGeneralActivity.INTENT_GOODSTYPE, ConversationGeneralActivity.FLAGSPORTTYPEGOODS)
                .build();
        Intent intent = new Intent(this, ConversationGeneralActivity.class);
        intent.setData(data);
        Gson gson = new Gson();
        String jsonString = gson.toJson(mGoodsInfoActModel);
        intent.putExtra(ConversationGeneralActivity.INTENT_JSONGOODSDETAIL, jsonString);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        handler_timeCurrent.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return listData_goodsImages.size();
        }

        /**
         * 1. 根据position获取对应的view，给view添加到container
         * 2. 返回一个view（Viewpaer的每个界面的内容）
         * 采用的是Viewpager+自定义的view（对外提供自己长什么样子：view）
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            int length = DensityUtil.dip2px(mContext,60);
            imageView.setPadding(length,0,length,0);
            ImagerLoaderUtil.getInstance(mContext).displayMyImage(listData_goodsImages.get(position), imageView);
            container.addView(imageView);
            return imageView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

    }

}
