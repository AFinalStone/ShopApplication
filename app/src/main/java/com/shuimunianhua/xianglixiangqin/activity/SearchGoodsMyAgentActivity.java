package com.shuimunianhua.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afinalstone.androidstudy.menulistview.Menu;
import com.afinalstone.androidstudy.menulistview.MenuCreator;
import com.afinalstone.androidstudy.menulistview.MenuItem;
import com.afinalstone.androidstudy.menulistview.MenuListView.OnMenuItemClickListener;
import com.afinalstone.androidstudy.swiperefreshview.OnSwipeRefreshViewListener;
import com.afinalstone.androidstudy.swiperefreshview.SwipeRefreshMenuListView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.adapter.MyBaseAdapter;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultBaseModel;
import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsMyAgentModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.DensityUtil;
import com.shuimunianhua.xianglixiangqin.util.ImagerLoaderUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;
import com.shuimunianhua.xianglixiangqin.view.FragmentViewDialog;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/***
 * @action 我的代理
 * @author SHI 2016-2-1 11:37:04
 */
public class SearchGoodsMyAgentActivity extends MyBaseActivity implements
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
	@Bind(R.id.btn_addAllPrice)
	Button btn_addAllPrice;
	/** 数据源 **/
	private ArrayList<GoodsMyAgentModel> listData = new ArrayList<GoodsMyAgentModel>();
	/** 适配器 **/
	private MyAgentGoodsAdapter myAgentGoodsAdapter;
	/** 搜索需要用到的字段当前需要筛选的商品的ClassID **/
	private int currentFilterClassID = 0;
	private String currentFilterFilterIDS = "";
	private String currentFilterKeyWord = "";
	private String GoodsAgentIDS = "";
	private int currentPageIndex = 1;

	private View listViewIsEmpty;

	public void initView() {
		setContentView(R.layout.activity_search_my_agent_goods);
		ButterKnife.bind(this);
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(this);
		tv_title.setText("我的代理商品");
		btn_searchContext.setOnClickListener(this);
		btn_sorting.setOnClickListener(this);
		btn_addAllPrice.setOnClickListener(this);
		cb_selectAll.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						for (int i = 0; i < listData.size(); i++) {
							listData.get(i).setWhetherSelect(isChecked);
						}
						myAgentGoodsAdapter.notifyDataSetChanged();
					}
				});

		listViewIsEmpty = View.inflate(mContext,R.layout.item_adapter_list_view_empty, null);
		((ImageView)listViewIsEmpty.findViewById(R.id.iv_empty)).setImageResource(R.drawable.icon_my_agent_is_empty);
		if(swipeRefreshMenuListView.getListView().getHeaderViewsCount() == 0){
			swipeRefreshMenuListView.getListView().addHeaderView(listViewIsEmpty);
		}

		myAgentGoodsAdapter = new MyAgentGoodsAdapter(this, listData);
		swipeRefreshMenuListView.getListView()
				.setAdapter(myAgentGoodsAdapter);

		swipeRefreshMenuListView.setOnRefreshListener(this);
		MenuCreator creator = new MenuCreator() {

			@Override
			public void create(Menu menu) {
				MenuItem openItem = new MenuItem(mContext);
				openItem.setBackground(R.color.redTitleBarBackground);
				openItem.setWidth(DensityUtil.dip2px(mContext, 80));
				openItem.setTitle(R.string.cancleSettingSpecialPrices);
				openItem.setIcon(R.drawable.delete_04);
				openItem.setTitleSize(14);
				openItem.setTitleColor(getResources().getColor(R.color.white));
				menu.addMenuItem(openItem);
			}
		};
		swipeRefreshMenuListView.getListView().setMenuCreator(
				creator);
		swipeRefreshMenuListView.getListView()
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public void onMenuItemClick(int position, Menu menu,
												int index) {
						switch (index) {
							case 0:
								cancelGoodAgent(position);
								break;
						}
					}

				});

	}

	public void initData() {
		swipeRefreshMenuListView.openRefreshState();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		currentFilterClassID = intent.getIntExtra(InformationCodeUtil.IntentSearchGoodsFilterClassID, 0);
		currentFilterFilterIDS = "";
		currentFilterKeyWord = "";
		// 当前界面是筛选界面
		if (currentFilterClassID > 0) {
			btn_sorting.setText("筛选");
			tv_titleRight.setVisibility(View.VISIBLE);
			tv_titleRight.setText("分类");
			tv_titleRight.setOnClickListener(this);
		}
		listData.clear();
		myAgentGoodsAdapter.notifyDataSetChanged();
		swipeRefreshMenuListView.openRefreshState();
		super.onNewIntent(intent);
	}

	private void getData(boolean whetherRefresh) {

		// 如果是请求刷新数据，则把pageIndex置为1
		if (whetherRefresh) {
			currentPageIndex = 1;
			listData.clear();
			myAgentGoodsAdapter.notifyDataSetChanged();
		} else {
			currentPageIndex++;
		}
		cb_selectAll.setChecked(false);
		SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE,
				InformationCodeUtil.methodNameGetAgentProducts);
		soapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
		soapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
		soapObject.addProperty("keyWord", currentFilterKeyWord);
		soapObject.addProperty("classID", currentFilterClassID);
		soapObject.addProperty("filterIDS", currentFilterFilterIDS);
		soapObject.addProperty("pageSize", 10);
		soapObject.addProperty("pageIndex", currentPageIndex);
		ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
				mContext, this, soapObject,
				InformationCodeUtil.methodNameGetAgentProducts, whetherRefresh);
		connectCustomServiceAsyncTask.initProgressDialog(false, "");
		connectCustomServiceAsyncTask.execute();

	}

	private void updateMyAgentGoodsPrice(
			String strAddPriceNumber,
			String strFirstDistributionNumber,
			String strSecondDistributionNumber,
			String strThirdDistributionNumber) {

		String methodName = InformationCodeUtil.methodNameBatchUpdatePrice;
		SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
		soapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
		soapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
		soapObject.addProperty("Ids", GoodsAgentIDS);
		soapObject.addProperty("AgentAddMoneyStr", strAddPriceNumber);
		soapObject.addProperty("ParentIDBackMoney1Str",
				strFirstDistributionNumber);
		soapObject.addProperty("ParentIDBackMoney2Str",
				strSecondDistributionNumber);
		soapObject.addProperty("ParentIDBackMoney3Str",
				strThirdDistributionNumber);
		LogUtil.LogShitou("soapObject", soapObject.toString());
		ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
				mContext, SearchGoodsMyAgentActivity.this, soapObject,
				methodName);
		connectGoodsServiceAsyncTask.execute();
	}

	private void cancelGoodAgent(int position) {
		String methodName = InformationCodeUtil.methodNameCancelGoodAgent;
		SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
		soapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
		soapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
		soapObject.addProperty("goodsID",listData.get(position).getDjLsh());
		ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
				mContext, SearchGoodsMyAgentActivity.this, soapObject, methodName, position);
		connectGoodsServiceAsyncTask.execute();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_FIRST_USER:
			currentFilterFilterIDS = data.getStringExtra("SelectedSubPid");
			currentFilterKeyWord = "";
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
		case R.id.btn_addAllPrice:
			StringBuffer str = new StringBuffer("");
			int index = -1;
			for (int i = 0; i < listData.size(); i++) {
				LogUtil.LogShitou("listDataItem:", listData.get(i).toString());
				if (listData.get(i).getWhetherSelect()) {
					if (index != -1) {
						str.append(",");
					}
					index = i;
					str.append(listData.get(i).getGoodsAgentID());
				}
			}
			showAddPriceFDialog(str.toString());
			break;
		// 分类或筛选按钮
		case R.id.btn_sorting:
			if ("筛选".equals(btn_sorting.getText())) {
				intent = new Intent(this, FilterFirstActivity.class);
				intent.putExtra(InformationCodeUtil.IntentFilterFirstActivityFilterClassID, currentFilterClassID);
				startActivityForResult(intent, 0);
			} else {
				intent = new Intent(this, SearchGoodsMyAgentClassTypeActivity.class);
				startActivity(intent);
			}
			break;
		// 标题栏右侧分类按钮
		case R.id.tv_titleRight:
			intent = new Intent(this, SearchGoodsMyAgentClassTypeActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_searchContext:
			listData.clear();
			myAgentGoodsAdapter.notifyDataSetChanged();
			String searchContext = et_searchContext.getText().toString().trim();
			currentFilterKeyWord = searchContext;
			swipeRefreshMenuListView.openRefreshState();
			break;
		default:
			break;
		}
	}

	public void showAddPriceFDialog(String Ids) {

		if (Ids.isEmpty()) {
			ToastUtils.show(this, "请至少选择一件需要加价的代理商品");
			return;
		} else {
			GoodsAgentIDS = Ids;
		}
		View view = View.inflate(this, R.layout.dialog_myagentgoods_addprice, null);
		final FragmentViewDialog fdialog = new FragmentViewDialog();
		fdialog.initView(view);
		final EditText addPriceNumber = (EditText) view
				.findViewById(R.id.addPriceNumber);
		final EditText firstDistributionNumber = (EditText) view
				.findViewById(R.id.firstDistributionNumber);
		final EditText secondDistributionNumber = (EditText) view
				.findViewById(R.id.secondDistributionNumber);
		final EditText thirdDistributionNumber = (EditText) view
				.findViewById(R.id.thirdDistributionNumber);
		ImageView btnOk = (ImageView) view.findViewById(R.id.btn_ok);
		ImageView btnCancel = (ImageView) view.findViewById(R.id.btn_cancel);

		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String strAddPriceNumber = addPriceNumber.getText().toString();
				String strFirstDistributionNumber = firstDistributionNumber
						.getText().toString();
				String strSecondDistributionNumber = secondDistributionNumber
						.getText().toString();
				String strThirdDistributionNumber = thirdDistributionNumber
						.getText().toString();
				if (strAddPriceNumber.isEmpty()
						|| strFirstDistributionNumber.isEmpty()
						|| strSecondDistributionNumber.isEmpty()
						|| strThirdDistributionNumber.isEmpty()) {
					ToastUtils.show(SearchGoodsMyAgentActivity.this, "加价内容不能为空");
					return;
				}

				updateMyAgentGoodsPrice(strAddPriceNumber,
						strFirstDistributionNumber,
						strSecondDistributionNumber, strThirdDistributionNumber);
				fdialog.dismiss();
			}

		});
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fdialog.dismiss();
			}
		});

		fdialog.show(getSupportFragmentManager(), "fragmentDialog");
	}

	/**
	 * 我的代理
	 * 
	 * @author SHI 2016-2-17 15:41:14
	 */
	private class MyAgentGoodsAdapter extends MyBaseAdapter<GoodsMyAgentModel> {

		public MyAgentGoodsAdapter(Context mContext, List<GoodsMyAgentModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.item_adapter_myagent_listview, null);
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
				holder.btn_addPrice = (Button) convertView
						.findViewById(R.id.tv_addPrice);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final GoodsMyAgentModel productModel = listData.get(position);
			holder.cb_whetherSelect.setChecked(productModel
					.getWhetherSelect());
			holder.cb_whetherSelect
						.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								productModel.setWhetherSelect(isChecked);
							}
						});
				ImagerLoaderUtil.getInstance(mContext).displayMyImage(
						productModel.getImgUrl(), holder.iv_productImage);
				holder.iv_productImage
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(mContext,
										GoodsDetailGeneralActivity.class);
								intent.putExtra(
										InformationCodeUtil.IntentGoodsID,
										Integer.valueOf(productModel.getDjLsh()));
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
				holder.btn_addPrice
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								showAddPriceFDialog(productModel
										.getGoodsAgentID() + "");
							}
						});

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
			/** 加价按钮 **/
			private Button btn_addPrice;
		}

	}


	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		 LogUtil.LogShitou("returnSoapObject", returnString);

		if (methodName == InformationCodeUtil.methodNameGetAgentProducts) {
			try {
				UpdateListView(returnString, whetherRefresh);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		if (methodName == InformationCodeUtil.methodNameBatchUpdatePrice) {

			try {
				Gson gson = new Gson();
				JSONResultMsgModel msg = gson.fromJson(returnString, JSONResultMsgModel.class);
				ToastUtils.show(this, msg.getMsg());	
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		if (methodName == InformationCodeUtil.methodNameCancelGoodAgent) {
			try {
				Gson gson = new Gson();
				JSONResultMsgModel msg = gson.fromJson(returnString, JSONResultMsgModel.class);
				ToastUtils.show(this, msg.getMsg());	
				if(msg.getSign() == 1){
					listData.remove(state.intValue());
					myAgentGoodsAdapter.notifyDataSetChanged();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** 网络请求成功，更新ListView **/
	private void UpdateListView(String returnString,
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
				currentPageIndex--;
			}
		}

		if (list == null || list.size() == 0) {
			if (whetherRefresh) {
				if(swipeRefreshMenuListView.getListView().getHeaderViewsCount() == 0){
					swipeRefreshMenuListView.getListView().addHeaderView(listViewIsEmpty);
				}
			} else {
				ToastUtils.show(mContext, "暂无更多商品数据");
			}
		} else {
			swipeRefreshMenuListView.getListView().removeHeaderView(listViewIsEmpty);
			listData.addAll(list);
			myAgentGoodsAdapter.notifyDataSetChanged();

		}

		swipeRefreshMenuListView.closeRefreshState();

	}

	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {

		if (methodName == InformationCodeUtil.methodNameGetAgentProducts) {
			if (!whetherRefresh) {
				currentPageIndex--;
			}
			ToastUtils.show(mContext, "网络异常，获取数据失败");
			if(swipeRefreshMenuListView != null){
				swipeRefreshMenuListView.getListView().removeHeaderView(listViewIsEmpty);
				swipeRefreshMenuListView.closeRefreshState();
			}
			listData.clear();
			myAgentGoodsAdapter.notifyDataSetChanged();
			return;
		}

		if (methodName == InformationCodeUtil.methodNameBatchUpdatePrice) {
			ToastUtils.show(this, "网络异常,改价失败");
			return;
		}

		if (methodName == InformationCodeUtil.methodNameCancelGoodAgent) {
			ToastUtils.show(this, "网络异常,代理商品删除失败");
			return;
		}

	}

	@Override
	public void connectServiceCancelled(String returnString, String methodName,
			Integer state, boolean whetherRefresh) {
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
