package com.shi.xianglixiangqin.activity;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.model.GoodsIntegralModel;
import com.shi.xianglixiangqin.model.GoodsIntegralPackageColorBuyTypeModel;
import com.shi.xianglixiangqin.model.GoodsIntegralPackageColorModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.DensityUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author SHI
 * @action 活动商品详情页面
 * @date 2015-7-29 下午8:35:55
 */
public class GoodsDetailIntegralActivity extends MyBaseActivity implements
        OnConnectServerStateListener<Integer>{
    /**商品图片**/
    @BindView(R.id.viewPager_goodsImages)
    ViewPager viewPager_goodsImages;
    List<String> listData_goodsImages = new ArrayList<String>();
    MyPagerAdapter myPagerAdapter;
    /**商品名称**/
    @BindView(R.id.tv_goodsName)
    TextView tv_goodsName;
    /**商品价格**/
    @BindView(R.id.tv_goodsPrice)
    TextView tv_goodsPrice;
    /**商品选择标签**/
    @BindView(R.id.rg_goodsGroup)
    RadioGroup rg_goodsGroup;
    /**商品具体参数信息**/
    @BindView(R.id.webView_goodsDetail)
    WebView webView_goodsDetail;
    /**
     * 当前商品ID
     **/
    int goodsID;
    /**当前商品积分价格**/
    long goodsJFPrices;
    /**
     * 当前商品详细对象
     **/
    public GoodsIntegralModel mGoodsIntegralModel;
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
        setContentView(R.layout.activity_goods_detail_integral);
        ButterKnife.bind(mContext);
        rg_goodsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // String str = "style=/"width:100%;height:auto/"";
                // String str =
                // "<style>img{ max-width:80%; height:auto;	}</style>";
                switch (checkedId) {
                    // 获取商品详情描述
                    case R.id.rb_goodsDesc:
                        webView_goodsDetail.loadDataWithBaseURL(
                                null,
                                htmlHeader
                                        + mGoodsIntegralModel.getGoodsdesc()
                                        + htmlRoot, "text/html", "utf-8",
                                null);
                        break;
                    // 获取商品参数
                    case R.id.rb_goodsParameter:
                        webView_goodsDetail.loadDataWithBaseURL(
                                null,
                                htmlHeader
                                        + mGoodsIntegralModel.getGoodsspec()
                                        + htmlRoot, "text/html", "utf-8",
                                null);
                        break;
                    // 获取包装售后数据
                    case R.id.rb_goodsPackage:
                        webView_goodsDetail.loadDataWithBaseURL(
                                null,
                                htmlHeader
                                        + mGoodsIntegralModel.getGoodsbz()
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
        mGoodsIntegralModel = null;
        Intent mIntent = getIntent();
        goodsID = mIntent.getIntExtra(InformationCodeUtil.IntentGoodsID, -1);
        goodsJFPrices = mIntent.getLongExtra(InformationCodeUtil.IntentGoodsJfPrices, 0);
        tv_goodsPrice.setText(goodsJFPrices+"积分");
        myPagerAdapter = new MyPagerAdapter();
        viewPager_goodsImages.setAdapter(myPagerAdapter);
        getData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mGoodsIntegralModel = null;
        goodsID = getIntent().getIntExtra(InformationCodeUtil.IntentGoodsID, -1);
        getData();
        super.onNewIntent(intent);
    }

    void getData() {
        String methodName = InformationCodeUtil.methodNameGetJFSPDetail;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("shopid", MyApplication.getmCustomModel(mContext).getCurrentBrowsingShopID());
        soapObject.addProperty("gid", goodsID);
        ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                mContext, this, soapObject, methodName);
        connectCustomServiceAsyncTask.initProgressDialog(false);
        connectCustomServiceAsyncTask.execute();
    }

    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, Integer state, boolean whetherRefresh) {
        LogUtil.LogShitou("积分商品", returnString);
        // 获取积分商品详情成功
        if (InformationCodeUtil.methodNameGetJFSPDetail
                .equals(methodName)) {
            try {
                Gson gson = new Gson();
                mGoodsIntegralModel = null;
                mGoodsIntegralModel = gson.fromJson(
                        returnString, GoodsIntegralModel.class);
                refreshGoodsData();
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                ToastUtil.show(mContext, "数据异常");
                finish();
            }
        }
    }

    private void refreshGoodsData() {
        listData_goodsImages.clear();
        listData_goodsImages.addAll(mGoodsIntegralModel.getImgs());
        myPagerAdapter.notifyDataSetChanged();
        tv_goodsName.setText(mGoodsIntegralModel.getGoodsname());
        rg_goodsGroup.check(R.id.rb_goodsDesc);
    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Integer state,
                                     boolean whetherRefresh) {
        // 获取商品 详情
        if (InformationCodeUtil.methodNameGetJFSPDetail.equals(methodName)) {
            ToastUtil.show(mContext, returnStrError);
            finish();
        }
    }

    @Override
    public void connectServiceCancelled(String returnString,
                                        String methodName, Integer state, boolean whetherRefresh) {
        // 获取商品 详情
        if (InformationCodeUtil.methodNameGetJFSPDetail.equals(methodName)) {
            finish();
        }
    }


    @OnClick({R.id.btn_justToExChange ,R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_justToExChange:
                if (mGoodsIntegralModel != null) {
                    Intent intent = new Intent(mContext, GoodsDetailIntegralPackageActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentGoodsActivityCurrentGoodsModel, mGoodsIntegralModel);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_bottom, R.anim.not_change);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

//    /**
//     * 进入会话界面
//     */
//    void toConversationView() {
//
//        if (mGoodsIntegralModel == null) {
//            ToastUtil.show(mContext, "请先获取商品内容");
//            return;
//        }
//        Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
//        String targetId = new StringBuffer().append(mGoodsInfoActModel.getUserID()).toString();
//        String title = mGoodsInfoActModel.getShopName();
//
//        Uri data = Uri.parse("rong://" + mContext.getApplicationInfo().processName)
//                .buildUpon()
//                .appendPath("conversation").appendPath(conversationType.getName().toLowerCase())
//                .appendQueryParameter(ConversationGeneralActivity.INTENT_TARGETID, targetId)
//                .appendQueryParameter(ConversationGeneralActivity.INTENT_TITLE, title)
//                .appendQueryParameter(
//                        ConversationGeneralActivity.INTENT_GOODSTYPE, ConversationGeneralActivity.FLAGSPORTTYPEGOODS)
//                .build();
//        Intent intent = new Intent(this, ConversationGeneralActivity.class);
//        intent.setData(data);
//        Gson gson = new Gson();
//        String jsonString = gson.toJson(mGoodsInfoActModel);
//        intent.putExtra(ConversationGeneralActivity.INTENT_JSONGOODSDETAIL, jsonString);
//        startActivity(intent);
//    }

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
