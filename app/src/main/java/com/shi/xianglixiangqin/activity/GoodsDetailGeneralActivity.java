package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseRecycleAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultMsgModel;
import com.shi.xianglixiangqin.model.GoodsGeneralModel;
import com.shi.xianglixiangqin.rongyun.ConversationGeneralActivity;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.DensityUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.PreferencesUtilMy;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.shi.xianglixiangqin.view.GoodsAgentConfirmPopWindow;
import com.shi.xianglixiangqin.view.MyTextSliderView;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.rong.imlib.model.Conversation;

/**
 * @author SHI
 * @action 普通商品详情页
 * @date 2016年3月11日 16:11:48
 */
public class GoodsDetailGeneralActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer> {

//    /**
//     * 商品图片
//     **/
//    @BindView(R.id.viewPager_goodsImages)
//    ViewPager viewPager_goodsImages;
    /**
     * 商品图片
     **/
    @BindView(R.id.sliderLayout_goodsImages)
    SliderLayout sliderLayout_goodsImages;
    List<String> listData_goodsImages = new ArrayList<String>();
//    MyPagerAdapter myPagerAdapter;
    /**
     * 商品名称
     **/
    @BindView(R.id.tv_goodsName)
    TextView tv_goodsName;
    /**
     * 商品标题
     **/
    @BindView(R.id.relativeLayout_title)
    RelativeLayout relativeLayout_title;
    /**
     * 商品价格
     **/
    @BindView(R.id.tv_goodsPrice)
    TextView tv_goodsPrice;
    /**
     * 可使用的飞币
     **/
    @BindView(R.id.tv_flyCoinCanUsed)
    TextView tv_flyCoinCanUsed;
    /**
     * 已销售数量
     **/
    @BindView(R.id.tv_saleCount)
    TextView tv_saleCount;

    /**
     * 赠送的积分数量
     **/
    @BindView(R.id.tv_giveIntegral)
    TextView tv_giveIntegral;
    /**
     * 优惠组合
     **/
    @BindView(R.id.tv_combination)
    TextView tv_combination;
    @BindView(R.id.recycleView_combination)
    RecyclerView recycleView_combination;

    private List<GoodsGeneralModel> listData_CbGoods = new ArrayList<>();
    private RecycleAdapter recycleAdapter;
    /**
     * 商品介绍 商品参数 包装售后
     **/
    @BindView(R.id.rg_goodsGroup)
    RadioGroup rg_goodsGroup;
    /**
     * 商品介绍 商品参数 包装售后
     **/
    @BindView(R.id.webView_goodsDetail)
    WebView webView_goodsDetail;
    /**
     * 购物车商品数量
     **/
    @BindView(R.id.tv_shopCartNumber)
    TextView tv_shopCartNumber;

    /**
     * 当前商品ID
     **/
    int goodsID;


    /**
     * 当前商品详细信息
     **/
    public GoodsGeneralModel mGoodsGeneralModel;
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
    private View rootView;

    @Override
    public void initView() {
        rootView = View.inflate(mContext, R.layout.activity_goods_detail_general, null);
        setContentView(rootView);
        ButterKnife.bind(mContext);
        rg_goodsGroup
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            // 获取商品详情描述
                            case R.id.rb_goodsDesc:
                                webView_goodsDetail.loadDataWithBaseURL(null,
                                        htmlHeader + mGoodsGeneralModel.getGoodsDesc()
                                                + htmlRoot, "text/html", "utf-8",
                                        null);
                                break;
                            // 获取商品参数
                            case R.id.rb_goodsParameter:
                                webView_goodsDetail.loadDataWithBaseURL(null,
                                        htmlHeader + mGoodsGeneralModel.getGoodsSpec()
                                                + htmlRoot, "text/html", "utf-8",
                                        null);
                                break;
                            // 获取包装售后数据
                            case R.id.rb_goodsPackage:
                                webView_goodsDetail.loadDataWithBaseURL(null,
                                        htmlHeader + mGoodsGeneralModel.getPackage()
                                                + htmlRoot, "text/html", "utf-8",
                                        null);
                                break;

                            default:
                                break;
                        }
                    }
                });

        WebSettings webSetting = webView_goodsDetail.getSettings();
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setUseWideViewPort(true);

        webSetting.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setPluginState(WebSettings.PluginState.ON);
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


        recycleView_combination.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.HORIZONTAL));

        recycleView_combination.setItemAnimator(new DefaultItemAnimator());

        recycleAdapter = new RecycleAdapter(mContext, listData_CbGoods);

        recycleView_combination.setAdapter(recycleAdapter);
    }

    @Override
    public void initData() {
        mGoodsGeneralModel = null;
        goodsID = getIntent().getIntExtra(InformationCodeUtil.IntentGoodsID, -1);
//        myPagerAdapter = new MyPagerAdapter();
//        viewPager_goodsImages.setAdapter(myPagerAdapter);
        getData(InformationCodeUtil.methodNameGetGoodsItem);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mGoodsGeneralModel = null;
        goodsID = intent.getIntExtra(InformationCodeUtil.IntentGoodsID, -1);
        getData(InformationCodeUtil.methodNameGetGoodsItem);
        super.onNewIntent(intent);
    }


    void getData(String methodName) {
        // 获取商品 详情
        if (InformationCodeUtil.methodNameGetGoodsItem.equals(methodName)) {
            SoapObject requestSoapObject = new SoapObject(
                    ConnectServiceUtil.NAMESPACE, methodName);
            requestSoapObject.addProperty(InformationCodeUtil.IntentGoodsID,
                    goodsID);
            requestSoapObject.addProperty("customID ", MyApplication
                    .getmCustomModel(mContext).getDjLsh());
            ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
                    mContext, this, requestSoapObject, methodName);
            connectGoodsServiceAsyncTask.execute();
            return;
        }
        if (InformationCodeUtil.methodNameSPCount.equals(methodName)) {
            SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
            requestSoapObject.addProperty("spid", goodsID);
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
    protected void onResume() {
//        mGoodsGeneralModel = MyApplication.getmGoodsGeneralModel();
        int ShoppingCartNum = PreferencesUtilMy.getShopCartAllGoodsNum(mContext);
        if (ShoppingCartNum == 0) {
            tv_shopCartNumber.setVisibility(View.INVISIBLE);
        } else {
            tv_shopCartNumber.setVisibility(View.VISIBLE);
            tv_shopCartNumber.setText("" + ShoppingCartNum);
        }
        super.onResume();
    }

    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, Integer state, boolean whetherRefresh) {
        LogUtil.LogShitou(returnString);
        // 获取商品 详情
        if (InformationCodeUtil.methodNameGetGoodsItem.equals(methodName)) {
            try {
                Gson gson = new Gson();
                mGoodsGeneralModel = gson.fromJson(returnString,
                        GoodsGeneralModel.class);
                RefreshGoodsData();
                getData(InformationCodeUtil.methodNameSPCount);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                ToastUtil.show(mContext, "获取宝贝详情失败");
                finish();
            }
            return;
        }

        if (methodName == InformationCodeUtil.methodNameDelegateShopAllGoods) {
            try {
                JSONObject jsonObject = new JSONObject(returnString);
                ToastUtil.show(mContext, jsonObject.getString("Msg"));
                int sign = jsonObject.getInt("Sign");
                if (sign == 1) {
                    mGoodsGeneralModel.setIsAgented(1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 收到网络数据，刷新商品详情界面
     **/
    void RefreshGoodsData() {

        listData_goodsImages.clear();
        listData_goodsImages.addAll(mGoodsGeneralModel.getImages());
        listData_CbGoods.addAll(mGoodsGeneralModel.getZgoods());
        //初始化商品图片
        for (int i = 0; i < listData_goodsImages.size(); i++) {
            MyTextSliderView textSliderView = new MyTextSliderView(mContext);
            String imageUrl = listData_goodsImages.get(i);
            if (!StringUtil.isEmpty(imageUrl)) {
                textSliderView.image(imageUrl)
                        .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);
            }
            sliderLayout_goodsImages.addSlider(textSliderView);
        }

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) relativeLayout_title.getLayoutParams();
        if (listData_CbGoods.size() == 0) {
            params.setMargins(0, 0, 0, DensityUtil.dip2px(mContext, 342));
            relativeLayout_title.setLayoutParams(params);
            tv_combination.setVisibility(View.GONE);
            recycleView_combination.setVisibility(View.GONE);
        } else {
            params.setMargins(0, 0, 0, DensityUtil.dip2px(mContext, 493));
            relativeLayout_title.setLayoutParams(params);
            tv_combination.setVisibility(View.VISIBLE);
            recycleView_combination.setVisibility(View.VISIBLE);
            recycleAdapter.notifyDataSetChanged();
        }
        tv_goodsName.setText(mGoodsGeneralModel.getGoodsName());
        DecimalFormat dcmFmt = new DecimalFormat("0.00");
        String goodsMinPrice = dcmFmt.format(mGoodsGeneralModel.getMinPrice());
        String goodsMaxPrice = dcmFmt.format(mGoodsGeneralModel.getMaxPrice());
        tv_goodsPrice.setText("￥" + goodsMinPrice + " - " + goodsMaxPrice);
        tv_flyCoinCanUsed.setText("可用" + mGoodsGeneralModel.getFlyCoin() + "飞币");
        tv_saleCount.setText("销售数量：" + mGoodsGeneralModel.getSaledCount() + " 个");
        tv_giveIntegral.setText("赠送积分：" + mGoodsGeneralModel.getMaybeGetJF());
        rg_goodsGroup.check(R.id.rb_goodsDesc);
    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Integer state,
                                     boolean whetherRefresh) {
        // 获取商品 详情
        if (InformationCodeUtil.methodNameGetGoodsItem.equals(methodName)) {
            ToastUtil.show(mContext, returnStrError);
            finish();
            return;
        }
        if (InformationCodeUtil.methodNameDelegateShopAllGoods.equals(methodName)) {
            ToastUtil.show(mContext, returnStrError);
        }
    }

    @Override
    public void connectServiceCancelled(String returnString, String methodName,
                                        Integer state, boolean whetherRefresh) {
        if (InformationCodeUtil.methodNameGetGoodsItem.equals(methodName)) {
            finish();
        }
    }


    /**
     * 弹出代理商品确认界面
     **/
    private void toGoodsAgentConfirm() {
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
        soapObject.addProperty("goodsIDS", mGoodsGeneralModel.getDjLsh());
        soapObject.addProperty("gowhere", gowhere);
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
                (mContext, this, soapObject, methodName);
        connectCustomServiceAsyncTask.initProgressDialog(true, "正在代理...");
        connectCustomServiceAsyncTask.execute();
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
        String targetId = new StringBuffer().append(mGoodsGeneralModel.getUserID()).toString();
        String title = mGoodsGeneralModel.getShopName();

        Uri data = Uri.parse("rong://" + mContext.getApplicationInfo().processName)
                .buildUpon()
                .appendPath("conversation").appendPath(conversationType.getName().toLowerCase())
                .appendQueryParameter(ConversationGeneralActivity.INTENT_TARGETID, targetId)
                .appendQueryParameter(ConversationGeneralActivity.INTENT_TITLE, title)
                .appendQueryParameter(
                        ConversationGeneralActivity.INTENT_GOODSTYPE, ConversationGeneralActivity.FLAGGENERALTYPEGOODS)
                .build();
        Intent intent = new Intent(this, ConversationGeneralActivity.class);
        intent.setData(data);
        Gson gson = new Gson();
        String jsonString = gson.toJson(mGoodsGeneralModel);
        intent.putExtra(ConversationGeneralActivity.INTENT_JSONGOODSDETAIL, jsonString);
        startActivity(intent);

    }

    /**
     * 微信好友分享参数
     **/
    void showShare() {
        ShareSDK.initSDK(this);

        OnekeyShare oks = new OnekeyShare();
        String strShopName = MyApplication.getmCustomModel(mContext)
                .getShopName();
        String strGoodsName = mGoodsGeneralModel.getGoodsName();
        if (TextUtils.isEmpty(strShopName)) {
            oks.setTitle(" ");
        } else {
            oks.setTitle(strShopName);
        }
        if (TextUtils.isEmpty(strGoodsName)) {
            oks.setTitle(" ");
        } else {
            oks.setText(strGoodsName);
        }

        oks.setImageUrl(mGoodsGeneralModel.getImages().get(0));
        // oks.setImagePath(Environment.getExternalStorageDirectory() +
        // FILE_NAME);// 确保SDcard下面存在此张图片
        // url：仅在微信（包括好友和朋友圈）中使用
        // http://m.dainif.com/WeiXinAPI/Agent/GoodsShow.aspx?DjLsh=%i&ShopID=%i",self.currFullProduct.djLsh,shopID
        oks.setUrl("http://m.dainif.com/WeiXinAPI/Agent/GoodsShow.aspx?DjLsh="
                + mGoodsGeneralModel.getDjLsh() + "&ShopID="
                + MyApplication.getmCustomModel(mContext).getShopID());
        // http://m.dainif.com/WeiXinAPI/Agent/GoodsShow.aspx?DjLsh=5104&ShopID=796测试
        // oks.setShareContentCustomizeCallback(new
        // ShareContentCustomizeCallback() {
        // @Override
        // public void onShare(Platform platform,
        // cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
        // if ("WechatMoments".equals(platform.getName())) {
        // Bitmap imageData = BitmapFactory.decodeResource(
        // getResources(), R.drawable.weidian);
        // paramsToShare.setImageData(imageData);
        // }
        // if ("Wechat".equals(platform.getName())) {
        // Bitmap imageData = BitmapFactory.decodeResource(
        // getResources(), R.drawable.weidian);
        // paramsToShare.setImageData(imageData);
        // }
        // }
        // });
        // 启动分享GUI
        oks.show(this);
    }

    @OnClick({R.id.iv_back, R.id.iv_shareGoods, R.id.linearLayout_toGoodsShopCart, R.id.btn_addShoppingCart, R.id.btn_agentGoods, R.id.linearLayout_openConverSation})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_shareGoods:
                if (mGoodsGeneralModel.getIsAgented() == 1) {
                    IfOpenStartNewActivityAnim(false);
                    showShare();
                } else {
                    ToastUtil.show(mContext, "您尚未代理此商品,请先代理");
                }
                break;
            case R.id.linearLayout_toGoodsShopCart:
                intent = new Intent(mContext, ShoppingCartActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_addShoppingCart:
                if (mGoodsGeneralModel != null) {
                    intent = new Intent(mContext, GoodsPackageActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentGoodsActivityCurrentGoodsModel, mGoodsGeneralModel);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_bottom, R.anim.not_change);
                }
                break;
            case R.id.btn_agentGoods:
                toGoodsAgentConfirm();
                break;
            case R.id.linearLayout_openConverSation:
                toConversationView();
                break;
            default:
                break;
        }
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
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            int length = DensityUtil.dip2px(mContext, 60);
            imageView.setPadding(length, 0, length, 0);
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

    /**
     * 适配器
     **/
    protected class RecycleAdapter extends MyBaseRecycleAdapter<RecycleAdapter.MyViewHolder, GoodsGeneralModel> {

        public RecycleAdapter(Context context, List<GoodsGeneralModel> listData) {
            super(context, listData);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.item_adapter_goods_combination, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GoodsCombinationSelectActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentGoodsID, goodsID);
                    intent.putExtra(InformationCodeUtil.IntentGoodsActivityCurrentGoodsModel, mGoodsGeneralModel);
                    startActivity(intent);
                }
            });
            GoodsGeneralModel model = listData.get(position);
            ImagerLoaderUtil.getInstance(mContext).displayMyImage(model.getImgName(), holder.iv_goodsImage);
            holder.tv_goodsPrice.setText("优惠 " + StringUtil.doubleToString(model.getMaxYhje(), "0.00"));
        }


        protected class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView iv_goodsImage;
            TextView tv_goodsPrice;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.iv_goodsImage = (ImageView) itemView.findViewById(R.id.iv_goodsImage);
                this.tv_goodsPrice = (TextView) itemView.findViewById(R.id.tv_goodsPrice);
            }
        }
    }

}
