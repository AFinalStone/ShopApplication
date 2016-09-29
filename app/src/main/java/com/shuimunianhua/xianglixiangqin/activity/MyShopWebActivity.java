package com.shuimunianhua.xianglixiangqin.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Bind;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.util.ProgressDialogUtil;

/****
 * @action 店铺界面
 * @author ZHU
 * @date 2015-8-9 下午7:37:45
 */
public class MyShopWebActivity extends MyBaseActivity {

	/**左侧返回控件**/
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**标题**/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	/**标题**/
	@Bind(R.id.tv_titleRight)
	 TextView tv_titleRight;
	/**标题栏控件**/
	@Bind(R.id.relativeLayout_title)
	 RelativeLayout relativeLayout_title;
	/**webView控件**/
	@Bind(R.id.webView)
	 WebView webView;
	private WebSettings webSetting;
	/**当前用户的店铺名称**/
	private String shopName;
	/**当前用户的店铺ID**/
	private int shopId;
	/** 路径 **/
	public static String IMAGELOGO_URL;
	/** Log图片 **/
	private static final String FILE_NAME = "/weidian.jpg";	
	@SuppressLint("ResourceAsColor") @Override
	public void initView() {
		setContentView(R.layout.activity_my_shop_web);
		ButterKnife.bind(this);
		
		relativeLayout_title.setBackgroundResource(R.drawable.shopweb_titlebar_background);
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setImageResource(R.drawable.shopweb_titleleft);
		iv_titleLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_titleRight.setVisibility(View.VISIBLE);
		tv_titleRight.setTextColor(Color.BLACK);
		tv_titleRight.setText("分享");
		tv_titleRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IfOpenStartNewActivityAnim(false);
				showShare();
			}
		});
		tv_title.setTextColor(Color.BLACK);
		tv_title.setText("我的店铺");
		
		initWebView();
	}
	
	@Override
	public void initData() {
		
		shopName = MyApplication.getmCustomModel(mContext).getShopName();		
		shopId = MyApplication.getmCustomModel(mContext).getShopID();	
		if(shopId == -1){
			return;
		}
		webView.loadUrl(
		"http://m.dainif.com//WeiXinAPI/Agent/Default.aspx?ShopID="+ shopId);
	}

	private void initWebView() {
		// 设置webView不自动加载图片
		webSetting = webView.getSettings();
		if (Build.VERSION.SDK_INT >= 19) {
			webSetting.setLoadsImagesAutomatically(true);
		} else {
			webSetting.setLoadsImagesAutomatically(false);
		}
		//**这两个属性使得webview加载网页内容自动适配屏幕**//
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
		// 设置 web点击不使用其他浏览器
		webView.setWebViewClient(new MyWebViewClient());	
	}
	
	class MyWebViewClient extends WebViewClient {
		
		// 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			// 如果不需要其他对点击链接事件的处理返回true，否则返回false
			return true;
		}
		//在加载网页内容的时候显示进度条
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			ProgressDialogUtil.startProgressDialog(mContext, R.string.str_loadingMsg);
		}
		//加载网页内容结束时关闭进度条
		@Override
		public void onPageFinished(WebView view, String url) {
			ProgressDialogUtil.stopProgressDialog();
			if(webView == null){
				return;
			}
			if (!webView.getSettings().getLoadsImagesAutomatically()) {
				webView.getSettings().setLoadsImagesAutomatically(true);
			}
			// Toast.makeText(ShopWebActivity.this, "无店铺信息",
			// Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			// wxWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8",
			// null);
			ToastUtils.show(mContext, "请求数据失败，请检查网络");
			finish();
		}


	}

	/** 把图片从drawable复制到sdcard中 **/
	private void initImagePath() {
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())
					&& Environment.getExternalStorageDirectory().exists()) {
				IMAGELOGO_URL = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + FILE_NAME;
			} else {
				IMAGELOGO_URL = getApplication().getFilesDir().getAbsolutePath()
						+ FILE_NAME;
			}
			File file = new File(IMAGELOGO_URL);
			if (!file.exists()) {
				InputStream  is = getAssets().open("weidian.jpg");
				is.available();
				Bitmap pic = BitmapFactory.decodeStream(is);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
			}
		} catch (Throwable t) {
			t.printStackTrace();
			IMAGELOGO_URL = null;
		}
	}
	
	
	/** 微信好友分享参数 **/
	private void showShare() {
		//把assert里面的weidian.jpg图片复制到外部存储卡中
		if(MyApplication.getmCustomModel(mContext).getWeChatImgUrl() == null){
			initImagePath();
		}else{
			IMAGELOGO_URL = MyApplication.getmCustomModel(mContext).getWeChatImgUrl();
		}
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		//		oks.disableSSOWhenAuthorize();
		// text是分享文本：所有平台都需要这个字段
		if(shopName == null || shopName.isEmpty()){
			oks.setText(" "); // 最多40个字符	
			// title标题：微信、QQ（新浪微博不需要标题）
			oks.setTitle(" "); // 最多30个字符
		}else{
			// title标题：微信、QQ（新浪微博不需要标题）
			oks.setTitle("好店推荐"); // 最多30个字符
			oks.setText(shopName); // 最多30个字符
		}

		oks.setImagePath(IMAGELOGO_URL);
		//		oks.setImagePath(Environment.getExternalStorageDirectory() + FILE_NAME);// 确保SDcard下面存在此张图片
		// url：仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://m.dainif.com//WeiXinAPI/Agent/Default.aspx?ShopID="+ shopId);
		//		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
		//			@Override
		//			public void onShare(Platform platform,
		//					cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
		//				if ("WechatMoments".equals(platform.getName())) {
		//					Bitmap imageData = BitmapFactory.decodeResource(
		//							getResources(), R.drawable.weidian);
		//					paramsToShare.setImageData(imageData);
		//				}
		//				if ("Wechat".equals(platform.getName())) {
		//					Bitmap imageData = BitmapFactory.decodeResource(
		//							getResources(), R.drawable.weidian);
		//					paramsToShare.setImageData(imageData);
		//				}
		//			}
		//		});
		// 启动分享GUI
		oks.show(this);
	}
	
	@Override
	public void onBackPressed() {
        if (webView.canGoBack()) {       
        	webView.goBack();       
            return;       
        }   
		super.onBackPressed();
	}

}
