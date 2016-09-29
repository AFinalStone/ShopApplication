package com.shuimunianhua.xianglixiangqin.activity;

import java.util.ArrayList;
import java.util.List;
import org.ksoap2.serialization.SoapObject;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afinalstone.androidstudy.swiperefreshview.OnSwipeRefreshViewListener;
import com.afinalstone.androidstudy.menulistview.Menu;
import com.afinalstone.androidstudy.menulistview.MenuCreator;
import com.afinalstone.androidstudy.menulistview.MenuItem;
import com.afinalstone.androidstudy.menulistview.MenuListView.OnMenuItemClickListener;
import com.afinalstone.androidstudy.swiperefreshview.SwipeRefreshMenuListView;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.adapter.MyBaseAdapter;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultBaseModel;
import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.model.CustomModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsMyAgentModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.DensityUtil;
import com.shuimunianhua.xianglixiangqin.util.ImagerLoaderUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.view.FragmentCommonDialog;

/**
 * 微商城特价商品管理
 * @author SHI
 * 2016年5月12日 17:12:08
 */
public class WXShopGoodsManageActivity extends MyBaseActivity implements
		OnClickListener, OnConnectServerStateListener<Integer>,
		OnSwipeRefreshViewListener {
	/** 后退控件 **/
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/** 页面标题设置为我的代理商品 **/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	/** 标题栏右侧按钮 **/
	@Bind(R.id.tv_titleRight)
	 TextView tv_titleRight;
	/** 搜索内容 **/
	@Bind(R.id.et_searchContext)
	 EditText et_searchContext;
	/** 搜索按钮 **/
	@Bind(R.id.btn_searchContext)
	 Button btn_searchContext;
	/** 分类 或 筛选 按钮控件 **/
	@Bind(R.id.btn_sorting)
	 Button btn_sorting;
	/** 搜索结果列表 **/
	@Bind(R.id.swipeRefreshMenuListView)
	 SwipeRefreshMenuListView swipeRefreshMenuListView;
	/** 全选控件 **/
	@Bind(R.id.cb_selectAll)
	 CheckBox cb_selectAll;
	/** 批量加价控件 **/
	@Bind(R.id.btn_addToSpecialPricesGoods)
	 Button btn_addToSpecialPricesGoods;
	/** 当前登录用户 **/
	CustomModel mCustomModel;
	/** 数据源 **/
	private List<GoodsMyAgentModel> listData;
	/** 适配器 **/
	private MyAdapter myAdapter;
	/** 当前需要筛选的商品的ClassID **/
	private int currentFilterClassID = 0;
	/** 当前需要筛选的商品的FilterIDS **/
	private String currentFilterFilterIDS = "";
	/** 当前需要筛选的商品的keyWord **/
	private String currentFilterkeyWord = "";
	/** 当前页数 **/
	private int currentPpageIndex = 1;

	/**筛选页面请求码**/
	private int RequestCode_Fillter = 1;

	public void initView() {
		setContentView(R.layout.activity_wxshop_manage);
		ButterKnife.bind(this);
	}

	public void initData() {
		mCustomModel = MyApplication.getmCustomModel(mContext);
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(this);
		tv_title.setText(R.string.wxShopManageTitle);
		btn_searchContext.setOnClickListener(this);
		btn_sorting.setOnClickListener(this);
		btn_addToSpecialPricesGoods.setOnClickListener(this);
		cb_selectAll
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
							for (int i = 0; i < listData.size(); i++) {
								listData.get(i).setWhetherSelect(isChecked);
							}
							myAdapter.notifyDataSetChanged();
					}
				});

		listData = new ArrayList<GoodsMyAgentModel>();
		myAdapter = new MyAdapter(this, listData);
		swipeRefreshMenuListView.getListView().setAdapter(myAdapter);
		swipeRefreshMenuListView.setOnRefreshListener(this);
		// step 1. create a MenuCreator
		MenuCreator creator = new MenuCreator() {

			@Override
			public void create(Menu menu) {
				MenuItem openItem = new MenuItem(mContext);
				openItem.setBackground(R.color.white);
				openItem.setWidth(DensityUtil.dip2px(mContext, 60));
				openItem.setTitle(R.string.cancleSettingSpecialPrices);
				openItem.setTitleSize(14);
				openItem.setTitleColor(getResources().getColor(R.color.redTitleBarBackground));
				menu.addMenuItem(openItem);

			}
		};
		swipeRefreshMenuListView.getListView().setMenuCreator(creator);
		swipeRefreshMenuListView.getListView().setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, Menu menu, int index) {
				switch (index) {
				case 0:
					changeSpecialGoodsState(listData.get(position).getGoodsAgentID()+"",0);
					break;
				}
			}

		});
		swipeRefreshMenuListView.openRefreshState();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		LogUtil.LogShitou("onNewIntent");
		currentFilterClassID = intent.getIntExtra(InformationCodeUtil.IntentWXShopGoodsManagerActivityFilterClassID, -1);
		currentFilterFilterIDS = "";
		currentFilterkeyWord = "";
		// 当前界面是筛选界面
		if (currentFilterClassID > 0) {
			btn_sorting.setText("筛选");
			tv_titleRight.setVisibility(View.VISIBLE);
			tv_titleRight.setText("分类");
			tv_titleRight.setOnClickListener(this);
		}
		listData.clear();
		myAdapter.notifyDataSetChanged();
		swipeRefreshMenuListView.openRefreshState();
		super.onNewIntent(intent);
	}
	
	private void changeSpecialGoodsState(String Ids,int ifTj) {
		SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, InformationCodeUtil.methodNameBatchSetTjProducts);
		requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
		requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
		requestSoapObject.addProperty("Ids", Ids);
		requestSoapObject.addProperty("IfTj", ifTj);
		ConnectGoodsServiceAsyncTask connectGoodsTask = new ConnectGoodsServiceAsyncTask
				(mContext, this, requestSoapObject, InformationCodeUtil.methodNameBatchSetTjProducts);
		connectGoodsTask.execute();
	}


	private void getData(boolean whetherRefresh) {

		// 如果是请求刷新数据，则把pageIndex置为1
		if (whetherRefresh) {
			currentPpageIndex = 1;
			listData.clear();
			myAdapter.notifyDataSetChanged();
		} else {
			currentPpageIndex++;
		}
		cb_selectAll.setChecked(false);
		SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE,
				InformationCodeUtil.methodNameGetAgentProducts);
		soapObject.addProperty("customID", mCustomModel.getDjLsh());
		soapObject.addProperty("openKey", mCustomModel.getOpenKey());
		soapObject.addProperty("keyWord", currentFilterkeyWord);
		soapObject.addProperty("classID", currentFilterClassID);
		soapObject.addProperty("filterIDS", currentFilterFilterIDS);
		soapObject.addProperty("pageSize", 10);
		soapObject.addProperty("pageIndex", currentPpageIndex);
		ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
				mContext, this, soapObject,
				InformationCodeUtil.methodNameGetAgentProducts, whetherRefresh);
		connectCustomServiceAsyncTask.initProgressDialog(false, "");
		connectCustomServiceAsyncTask.execute();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_FIRST_USER:
			currentFilterFilterIDS = data.getStringExtra("SelectedSubPid");
			currentFilterkeyWord = "";
			et_searchContext.setText("");
			LogUtil.LogShitou("SelectedSubPid", currentFilterFilterIDS);
			swipeRefreshMenuListView.openRefreshState();
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_titleLeft:
			finish();
			break;
		case R.id.btn_sorting:
			if ("筛选".equals(btn_sorting.getText())) {
				intent = new Intent(this, FilterFirstActivity.class);
				intent.putExtra(InformationCodeUtil.IntentFilterFirstActivityFilterClassID, currentFilterClassID);
				startActivityForResult(intent, RequestCode_Fillter);
			}else{
				intent = new Intent(this, WXShopGoodsClassTypeActivity.class);
				startActivity(intent);
			}
			break;
		// 标题栏右侧分类按钮
		case R.id.tv_titleRight:
			intent = new Intent(this, WXShopGoodsClassTypeActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_searchContext:
			listData.clear();
			myAdapter.notifyDataSetChanged();
			String searchContext = et_searchContext.getText().toString().trim();
			currentFilterkeyWord = searchContext;
			swipeRefreshMenuListView.openRefreshState();
			break;
		case R.id.btn_addToSpecialPricesGoods:
			StringBuffer str = new StringBuffer();
			int startIndex = -1;
			for (int i = 0; i < listData.size(); i++) {
				if (listData.get(i).getWhetherSelect()) {
					if (startIndex == -1) {
						startIndex = i;
					}
					if (i != startIndex) {
						str.append(",");
					}
					str.append(listData.get(i).getGoodsAgentID());
				}
			}
			showAddPriceFDialog(str.toString());
			break;
		default:
			break;
		}
		// TODO
	}
	
	public void showAddPriceFDialog(final String Ids) {

		if (Ids.isEmpty()) {
			ToastUtils.show(this, "请至少选择一件需要加价的代理商品");
			return;
		} 
		FragmentCommonDialog fdialog = new FragmentCommonDialog();
		fdialog.initView("提示", "确认添加这些商品为特价商品吗?","取消","确定",
			new FragmentCommonDialog.OnButtonClickListener() {
			
			@Override
			public void OnOkClick() {
				changeSpecialGoodsState(Ids, 1);
			}
			
			@Override
			public void OnCancelClick() {
			}
		});
		fdialog.show(getSupportFragmentManager(), "fragmentDailog");

	}


	/**
	 * 微店
	 * @author SHI 
	 * 2016-2-17 15:41:14
	 */
	private class MyAdapter extends MyBaseAdapter<GoodsMyAgentModel> {

		public MyAdapter(Context mContext,
				List<GoodsMyAgentModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.item_adapter_wxshopmanager_listview, null);
				holder = new ViewHolder();
				holder.cb_whetherSelect = (CheckBox) convertView
						.findViewById(R.id.cb_whetherSelect);
				holder.iv_productImage = (ImageView) convertView
						.findViewById(R.id.shoping_goods_image);
				holder.tv_productTitle = (TextView) convertView
						.findViewById(R.id.shoping_goods_title);
				holder.tv_productPrice = (TextView) convertView
						.findViewById(R.id.shoping_goods_price);
				holder.tv_agentAddMoney = (TextView) convertView
						.findViewById(R.id.tv_agentAddMoney);
				holder.btn_setToSpecialPricesGoods = (Button) convertView
						.findViewById(R.id.btn_setToSpecialPricesGoods);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final GoodsMyAgentModel productModel = listData.get(position);
			if (productModel != null) {
				holder.cb_whetherSelect
						.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								productModel.setWhetherSelect(isChecked);
							}
						});
				holder.cb_whetherSelect.setChecked(productModel.getWhetherSelect());
				ImagerLoaderUtil.getInstance(mContext).displayMyImage(
						productModel.getImgUrl(), holder.iv_productImage);
				holder.iv_productImage
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(
										WXShopGoodsManageActivity.this,
										GoodsDetailGeneralActivity.class);
								intent.putExtra(InformationCodeUtil.IntentGoodsID, Integer
										.valueOf(productModel.getDjLsh()));
								startActivity(intent);
							}
						});
				holder.tv_productTitle.setText(productModel.getGoodsName());
				holder.tv_productPrice
						.setText(productModel.getMinPrice() + "元");
				if (productModel.getAgentAddMoney() != 0) {
					holder.tv_agentAddMoney.setVisibility(View.VISIBLE);
					holder.tv_agentAddMoney.setText("(已加￥"
							+ productModel.getAgentAddMoney() + ")");
				} else {
					holder.tv_agentAddMoney.setVisibility(View.GONE);
				}
				holder.btn_setToSpecialPricesGoods.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								changeSpecialGoodsState(productModel.getGoodsAgentID()+"", 1);
							}
						});
				if(productModel.getIfTj()){
					holder.btn_setToSpecialPricesGoods.setText(R.string.specialPricesGoods);
					holder.btn_setToSpecialPricesGoods.setEnabled(false);
				}else{
					holder.btn_setToSpecialPricesGoods.setText(R.string.setToSpecialPricesGoods);
					holder.btn_setToSpecialPricesGoods.setEnabled(true);
				}
			}

			return convertView;
		}

		private class ViewHolder {
			/** 商品是否选中 */
			private CheckBox cb_whetherSelect;
			/** 商品图片 */
			private ImageView iv_productImage;
			/** 商品标题 **/
			private TextView tv_productTitle;
			/** 商品价格 **/
			private TextView tv_productPrice;
			/** 商品代理加价价格 **/
			private TextView tv_agentAddMoney;
			/** 设为特价商品按钮 **/
			private Button btn_setToSpecialPricesGoods;
		}

	}


	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		 LogUtil.LogShitou("returnSoapObject", returnString);

		if (methodName == InformationCodeUtil.methodNameGetAgentProducts) {
			try {
				UpdateListView(returnString, methodName, whetherRefresh);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		if (methodName == InformationCodeUtil.methodNameBatchSetTjProducts) {
			try {
				Gson gson = new Gson();
				JSONResultMsgModel msg = gson.fromJson(returnString, JSONResultMsgModel.class);
				ToastUtils.show(this, msg.getMsg());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
	}

	/** 网络请求成功，更新ListView **/
	private void UpdateListView(String returnString, String methodName,
			boolean whetherRefresh) {
		List<GoodsMyAgentModel> list = null;
		try {
			Gson gson = new Gson();
			JSONResultBaseModel<GoodsMyAgentModel> status = gson.fromJson(
					returnString,
					new TypeToken<JSONResultBaseModel<GoodsMyAgentModel>>() {
					}.getType());
			list = status.getList();

		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			if (!whetherRefresh) {
				currentPpageIndex--;
			}
		}

		if (list == null || list.size() == 0) {
			if (whetherRefresh) {
				ToastUtils.show(mContext, "未搜到符合条件的商品");
			} else {
				ToastUtils.show(mContext, "暂无更多商品数据");
			}
		} else {
			listData.addAll(list);
			myAdapter.notifyDataSetChanged();
		}
		if(swipeRefreshMenuListView != null)
		swipeRefreshMenuListView.closeRefreshState();

	}

	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {

		if (methodName == InformationCodeUtil.methodNameGetAgentProducts) {
			if (!whetherRefresh) {
				currentPpageIndex--;
			}
			ToastUtils.show(mContext, "网络异常，获取数据失败");
			if(swipeRefreshMenuListView != null)
			swipeRefreshMenuListView.closeRefreshState();
			listData.clear();
			myAdapter.notifyDataSetChanged();
			return;
		}

		if (methodName == InformationCodeUtil.methodNameBatchSetTjProducts) {
			ToastUtils.show(this, "网络异常,商品设置失败");
			return;
		}

	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
	}

	@Override
	public void onTopRefrushListener() {
		getData(true);
	}

	@Override
	public void onBottomRefrushListener() {
		getData(false);
	}

}
