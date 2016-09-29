package com.shuimunianhua.xianglixiangqin.activity;

import java.text.DecimalFormat;

import org.ksoap2.serialization.SoapObject;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.PreferencesUtilMy;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import butterknife.ButterKnife;
import butterknife.Bind;
import io.rong.imlib.model.Conversation;

import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.model.GoodsGeneralModel;
import com.shuimunianhua.xianglixiangqin.rongyun.ConversationGeneralActivity;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.ImagerLoaderUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.view.FragmentViewDialog;

/**
 * @author SHI
 * @action 普通商品详情页
 * @date 2016年3月11日 16:11:48
 */
public class GoodsDetailGeneralActivity extends MyBaseActivity implements
        OnConnectServerStateListener<Integer>, OnClickListener {

    /**
     * 返回控件
     **/
    @Bind(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    /**
     * 标题
     **/
    @Bind(R.id.tv_title)
    TextView tv_title;
    /**
     * 右侧店铺详情
     **/
    @Bind(R.id.tv_titleRight)
    TextView tv_titleRight;
    /**
     * 商品图片
     **/
    @Bind(R.id.iv_goodsImages)
    ImageView iv_goodsImages;
    /**
     * 商品名称
     **/
    @Bind(R.id.tv_goodsName)
    TextView tv_goodsName;
    /**
     * 商品价格
     **/
    @Bind(R.id.tv_goodsPrice)
    TextView tv_goodsPrice;
    /**
     * 可使用的飞币
     **/
    @Bind(R.id.tv_flyCoinCanUsed)
    TextView tv_flyCoinCanUsed;
    /**
     * 分享商品
     **/
    @Bind(R.id.iv_shareGoods)
    ImageView iv_shareGoods;
    /**
     * 选择套餐
     **/
    @Bind(R.id.relativeLayout_selectPackageType)
    RelativeLayout relativeLayout_selectPackageType;
    /**
     * 商品介绍 商品参数 包装售后
     **/
    @Bind(R.id.rg_goodsGroup)
    RadioGroup rg_goodsGroup;
    /**
     * 商品介绍 商品参数 包装售后
     **/
    @Bind(R.id.webview_goodsDetail)
    WebView webview_goodsDetail;

    /**
     * 聊天控件
     **/
    @Bind(R.id.linearLayout_openConverSation)
    LinearLayout linearLayout_openConverSation;
    /**
     * 进入购物车
     **/
    @Bind(R.id.linearLayout_toGoodsShopCart)
    LinearLayout linearLayout_toGoodsShopCart;
    /**
     * 代理授权按钮
     **/
    @Bind(R.id.btn_agentGoods)
    Button btn_agentGoods;
    /**
     * 加入购物车
     **/
    @Bind(R.id.btn_addShoppingCart)
    Button btn_addShoppingCart;
    /**
     * 购物车商品数量
     **/
    @Bind(R.id.tv_shopCartNumber)
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
            + "img{max-width:100%%;height:auto;}"
            + "</style>"
            + "</head>"
            + "<body style='padding:35px 10px 0 10px;margin:0 0 0 0;line-height:25px;'>";

    String htmlRoot = "</body></html>";
    /**代理界面**/
    private FragmentViewDialog fvd;

    /**
     * 当前商品不是活动商品
     **/
    // public static final int TypeActionCurrentGoodsISNoSportGoods = -100;
    @Override
    public void initView() {
        setContentView(R.layout.activity_goods_detail_general);
        ButterKnife.bind(mContext);

        iv_titleLeft.setVisibility(View.VISIBLE);
        iv_titleLeft.setOnClickListener(this);
        tv_title.setText("商品详情");
//        tv_titleRight.setVisibility(View.VISIBLE);
//        tv_titleRight.setText("店铺");
//        tv_titleRight.setOnClickListener(this);
        // iv_goodsImages.setLayoutParams(new RelativeLayout.LayoutParams(
        // RelativeLayout.LayoutParams.MATCH_PARENT, displayDeviceWidth));
        iv_shareGoods.setOnClickListener(this);
        relativeLayout_selectPackageType.setOnClickListener(this);
        linearLayout_openConverSation.setOnClickListener(this);
        linearLayout_toGoodsShopCart.setOnClickListener(this);
        btn_agentGoods.setOnClickListener(this);
        btn_addShoppingCart.setOnClickListener(this);
        // btn_buyNow.setOnClickListener(this);
        // iv_openConverSation.setOnClickListener(this);
        rg_goodsGroup
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // String str = "style=/"width:100%;height:auto/"";
                        // String str =
                        // "<style>img{ max-width:80%; height:auto;	}</style>";
                        switch (checkedId) {
                            // 获取商品详情描述
                            case R.id.rb_goodsDesc:
                                webview_goodsDetail.loadDataWithBaseURL(null,
                                        htmlHeader + mGoodsGeneralModel.getGoodsDesc()
                                                + htmlRoot, "text/html", "utf-8",
                                        null);
                                break;
                            // 获取商品参数
                            case R.id.rb_goodsParameter:
                                webview_goodsDetail.loadDataWithBaseURL(null,
                                        htmlHeader + mGoodsGeneralModel.getGoodsSpec()
                                                + htmlRoot, "text/html", "utf-8",
                                        null);
                                break;
                            // 获取包装售后数据
                            case R.id.rb_goodsPackage:
                                webview_goodsDetail.loadDataWithBaseURL(null,
                                        htmlHeader + mGoodsGeneralModel.getPackage()
                                                + htmlRoot, "text/html", "utf-8",
                                        null);
                                break;
                            // 05-11 16:57:45.1,
                            // ImgUrl=http://wcf.dainif.com/UploadFiles/HeadImg/20160511/516300435573.Jpeg,
                            // Integral=0.0, LocProCode=330000, LocCityCode=330100,
                            // LocSiteName=杭州站, OpenKey=516345046869, PassWord=,
                            // PhoneNum=18211673059, RealName=要磊, RoleID=3,
                            // RongCloudToken=/4GxIhFE4t7ehK12lbqiZ7LfFd646bAyQwVEmibNS/fuv3zZknoq9y9GA+f/t0nM0hxdXxndIy6XCbB5l8IpuA==,
                            // Sex=男士, ShopID=855, ShopName=夏河雨, ShopUserID=0,
                            // UserName=18211673059, WeChatImgUrl=null,
                            // loginShopID=0]
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
        //
    }

    @Override
    public void initData() {
        mGoodsGeneralModel = null;
        goodsID = getIntent().getIntExtra(InformationCodeUtil.IntentGoodsID, -1);
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
        }
        // 检查 当前商品是否被 收藏
        if (InformationCodeUtil.methodNameCheckFavor.equals(methodName)) {
            SoapObject requestSoapObject = new SoapObject(
                    ConnectServiceUtil.NAMESPACE, methodName);

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
        // 获取商品 详情
        if (InformationCodeUtil.methodNameGetGoodsItem.equals(methodName)) {
            try {
                Gson gson = new Gson();
                mGoodsGeneralModel = gson.fromJson(returnString,
                        GoodsGeneralModel.class);
                RefrushGoodsData();
            } catch (JsonSyntaxException e) {
                ToastUtils.show(mContext, "获取宝贝详情失败");
                finish();
            }
            return;
        }
        // 代理商品
        if (InformationCodeUtil.methodNameAddDelegate.equals(methodName)) {
            JSONResultMsgModel mJSONBackResultModel = null;
            try {
                LogUtil.LogShitou("returnString", returnString);
                Gson gson = new Gson();
                mJSONBackResultModel = 	gson.fromJson(returnString,JSONResultMsgModel.class);
                ToastUtils.show(mContext, mJSONBackResultModel.getMsg());
                mGoodsGeneralModel.setIsAgented(1);
                fvd.dismiss();
                fvd = null;
            } catch (JsonSyntaxException e) {

            }
        }
    }

    /**
     * 收到网络数据，刷新商品详情界面
     **/
    void RefrushGoodsData() {
        ImagerLoaderUtil.getInstance(mContext).displayMyImage(
                mGoodsGeneralModel.getImages().get(0), iv_goodsImages);
        tv_goodsName.setText(mGoodsGeneralModel.getGoodsName());
        DecimalFormat dcmFmt = new DecimalFormat("0.00");
        String goodsMinPrice = dcmFmt.format(mGoodsGeneralModel.getMinPrice());
        String goodsMaxPrice = dcmFmt.format(mGoodsGeneralModel.getMaxPrice());
        tv_goodsPrice.setText("￥" + goodsMinPrice + " - " + goodsMaxPrice);
        tv_flyCoinCanUsed.setText("可用" + mGoodsGeneralModel.getFlyCoin() + "飞币");
        rg_goodsGroup.check(R.id.rb_goodsDesc);
    }

    @Override
    public void connectServiceFailed(String methodName, Integer state,
                                     boolean whetherRefresh) {
        // 获取商品 详情
        if (InformationCodeUtil.methodNameGetGoodsItem.equals(methodName)) {
            ToastUtils.show(mContext, "获取宝贝详情失败,请检查网络状况");
            finish();
            return;
        }
        if (InformationCodeUtil.methodNameAddDelegate.equals(methodName)) {
            ToastUtils.show(mContext, "网络异常，代理商品失败");
        }
    }

    @Override
    public void connectServiceCancelled(String returnString, String methodName,
                                        Integer state, boolean whetherRefresh) {
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                intent = new Intent(mContext, ShopActivity.class);
                intent.putExtra(InformationCodeUtil.IntentSearchGoodsCurrentShopID,
                        mGoodsGeneralModel.getShopID());
                mContext.startActivity(intent);
                break;
            case R.id.iv_shareGoods:
                if (mGoodsGeneralModel.getIsAgented() == 1) {
                    IfOpenStartNewActivityAnim(false);
                    showShare();
                } else {
                    ToastUtils.show(mContext, "您尚未代理此商品,请先代理");
                }
                break;
            case R.id.linearLayout_toGoodsShopCart:
                 intent = new Intent(mContext, ShoppingCartActivity.class);
                 startActivity(intent);
                break;
            case R.id.relativeLayout_selectPackageType:
            case R.id.btn_addShoppingCart:
                if(mGoodsGeneralModel != null){
                    intent = new Intent(mContext, GoodsPackageActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentGoodsActivityCurrentGoodsModel,mGoodsGeneralModel);
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

    /**弹出代理商品确认界面**/
    private void toGoodsAgentConfirm() {
        fvd = new FragmentViewDialog();

        View view = View.inflate(mContext, R.layout.dialog_goods_agent_confirm, null);
        fvd.initView(view);

        ImageView iv_productImage = (ImageView) view.findViewById(R.id.iv_productImage);
        TextView tv_productName = (TextView) view.findViewById(R.id.tv_productName);
        TextView tv_productPrices = (TextView) view.findViewById(R.id.tv_productPrices);
        ImageView iv_closeImageView = (ImageView) view.findViewById(R.id.iv_closeImageView);
        Button btn_confirmAnget = (Button) view.findViewById(R.id.btn_confirmAnget);
        ImagerLoaderUtil.getInstance(mContext)
                .displayMyImage(mGoodsGeneralModel.getImages().get(0), iv_productImage);

        tv_productName.setText(mGoodsGeneralModel.getGoodsName());
        DecimalFormat dcm = new DecimalFormat("0.00");
        tv_productPrices.setText("￥: " + dcm.format(mGoodsGeneralModel.getMinPrice()) + " - " + dcm.format(mGoodsGeneralModel.getMaxPrice()));
        iv_closeImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fvd.dismiss();
            }
        });
        btn_confirmAnget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addDelegate();
            }
        });

        fvd.show(getSupportFragmentManager(), "fvd");
    }

    /**代理商品**/
    public void addDelegate(){
        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, InformationCodeUtil.methodNameAddDelegate);
        requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
        requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
        requestSoapObject.addProperty("goodsID", mGoodsGeneralModel.getDjLsh());
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
                (mContext, this, requestSoapObject , InformationCodeUtil.methodNameAddDelegate);
        connectCustomServiceAsyncTask.execute();
    }

    /**
     * 进入会话界面
     */
    void toConversationView() {
        if (mGoodsGeneralModel == null) {
            ToastUtils.show(mContext, "请先获取商品内容");
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

}
