package com.shuimunianhua.xianglixiangqin.activity;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shuimunianhua.xianglixiangqin.adapter.MyBaseRecycleAdapter;
import com.shuimunianhua.xianglixiangqin.model.GoodsClassJsonView;
import com.shuimunianhua.xianglixiangqin.util.DensityUtil;
import com.shuimunianhua.xianglixiangqin.util.ImagerLoaderUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;

/***
 *
 * @author SHI
 * 商品的分类信息界面
 * 2016-2-1 11:18:24
 * 需要用到店铺用户UserID(本页请求特定店铺商品分类信息时候要用到)，店铺ID(为了进入下一个界面商品搜索界面时候使用)
 */
public class SearchGoodsClassTypeActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer>, SwipeRefreshLayout.OnRefreshListener {

	/**页面标题**/
	@Bind(R.id.tv_title)
	TextView tv_title;

	/**页面标题**/
	@Bind(R.id.radioGroup)
	RadioGroup radioGroup;

	/**网络请求是否成功**/
	public boolean connectSuccessFlag;

	int currentShopID;

	/**刷新控件**/
	@Bind(R.id.swipeRefreshLayout)
	SwipeRefreshLayout swipeRefreshLayout;
	/**具体分类信息GridView**/
	@Bind(R.id.recycleView)
	RecyclerView recycleView;

	RecycleAdapter recycleAdapter;
	private List<GoodsClassJsonView> listData_total = new ArrayList<GoodsClassJsonView>();
	private List<GoodsClassJsonView> listData_select= new ArrayList<GoodsClassJsonView>();

	@Override
	public void initView() {
		setContentView(R.layout.fragment_classification_ex);
		ButterKnife.bind(this);

		tv_title.setText("产品分类");
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				listData_select.clear();
				listData_select.addAll(listData_total.get(checkedId).getGoodsClassJsonViews());
				recycleAdapter.notifyDataSetChanged();

			}
		});
		recycleAdapter = new RecycleAdapter(mContext,listData_select);

		//设置GridLayoutManager布局管理器，实现GridView效果,每行展示四个item
		recycleView.setLayoutManager(new GridLayoutManager(mContext,3));
		//设置默认动画，添加addData()或者removeData()时候的动画
		recycleView.setItemAnimator(new DefaultItemAnimator());

		recycleView.setAdapter(recycleAdapter);
		recycleAdapter.setOnItemClickLitener(new MyBaseRecycleAdapter.OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {
				GoodsClassJsonView goodsClassJsonView = listData_select.get(position);
				Intent intent = new Intent(mContext, SearchGoodsActivity.class);
				intent.putExtra(InformationCodeUtil.IntentSearchGoodsFilterClassID, goodsClassJsonView.getDjLsh());
					//如果是聚合批发系统
				intent.putExtra(InformationCodeUtil.IntentSearchGoodsCurrentShopID, currentShopID);
				mContext.startActivity(intent);
			}

			@Override
			public void onItemLongClick(View view, int position) {

			}
		});

		swipeRefreshLayout.setColorSchemeColors(Color.RED
				, Color.GREEN
				, Color.BLUE
				, Color.YELLOW
				, Color.CYAN
				, 0xFFFE5D14
				, Color.MAGENTA);
		swipeRefreshLayout.setOnRefreshListener(this);
		currentShopID = getIntent().getIntExtra(InformationCodeUtil.IntentSearchGoodsCurrentShopID,-1);
	}

	/**初始化一级列表**/
	@Override
	public void initData() {
		if(!connectSuccessFlag) {
			connectSuccessFlag = true;
			swipeRefreshLayout.post(new Runnable() {
				@Override
				public void run() {
					swipeRefreshLayout.setRefreshing(true);
					onRefresh();
				}
			});
		}

	}


	public void getData() {

		//获取某个特定店铺的商品分类信息
		String methodName = InformationCodeUtil.methodNameGetUserCategories;
		SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE,methodName);
		soapObject.addProperty("shopID", currentShopID);
		ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
				mContext, this, soapObject, methodName);
		connectGoodsServiceAsyncTask.initProgressDialog(false);
		connectGoodsServiceAsyncTask.execute();

	}

	@Override
	public void connectServiceSuccessful(String returnString,String methodName,
										 Integer state, boolean whetherRefresh) {

		LogUtil.LogShitou("商品分类",returnString);
		try {
			Gson gson = new Gson();
			List<GoodsClassJsonView> results = gson.fromJson
					(returnString, new TypeToken<List<GoodsClassJsonView>>(){}.getType());
			listData_total.clear();
			radioGroup.removeAllViews();
			listData_total.addAll(results);
			for (int i=0; i<listData_total.size(); i++){
				RadioButton radioButton = (RadioButton) View.inflate(mContext,R.layout.item_goods_classtype_radiobutton,null);
				radioButton.setId(i);
				RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
						RadioGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext,50));
				radioButton.setText(listData_total.get(i).getClassName());
				radioGroup.addView(radioButton,layoutParams);
				if(i ==0)
					radioButton.setChecked(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			connectSuccessFlag = false;
		}

		swipeRefreshLayout.setRefreshing(false);

	}

	@Override
	public void connectServiceFailed(String methodName, Integer state, boolean whetherRefresh) {
		connectSuccessFlag = false;
		swipeRefreshLayout.setRefreshing(false);

		ToastUtils.show(mContext, "网络异常，分类数据请求失败");

	}

	@Override
	public void connectServiceCancelled(String returnString,
										String methodName, Integer state, boolean whetherRefresh) {
		connectSuccessFlag = false;
	}

	@Override
	public void onRefresh() {
		getData();
	}


	/**
	 * 适配器
	 **/
	protected class RecycleAdapter extends MyBaseRecycleAdapter<RecycleAdapter.MyViewHolder, GoodsClassJsonView> {

		public RecycleAdapter(Context context, List<GoodsClassJsonView> listData) {
			super(context, listData);
		}

		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

			View view = mLayoutInflater.inflate(R.layout.item_adapter_goods_classifcation_ex, parent, false);

			return new MyViewHolder(view);
		}

		@Override
		public void onFillViewHolderValue(MyViewHolder holder, int position) {
			GoodsClassJsonView productClassModel = listData.get(position);
			ImagerLoaderUtil.getInstance(mContext).displayMyImage(productClassModel.getImgUrl(),holder.iv_goodsImage);
			holder.tv_goodsName.setText(productClassModel.getClassName());
		}


		protected class MyViewHolder extends RecyclerView.ViewHolder {

			@Bind(R.id.iv_goodsImage)
			ImageView iv_goodsImage;

			@Bind(R.id.tv_goodsName)
			TextView tv_goodsName;

			public MyViewHolder(View itemView) {
				super(itemView);
				iv_goodsImage = (ImageView) itemView.findViewById(R.id.iv_goodsImage);
				tv_goodsName = (TextView) itemView.findViewById(R.id.tv_goodsName);
			}
		}

	}
}




