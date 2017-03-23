package com.shi.xianglixiangqin.frament;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.activity.MyBaseActivity;
import com.shi.xianglixiangqin.activity.SearchGoodsActivity;
import com.shi.xianglixiangqin.adapter.MyBaseRecycleAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.model.GoodsClassJsonView;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.DensityUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/***
 * @action 分类
 * @author SHI
 * @date  2015-7-18 上午11:29:00
 */
public class MainGoodsClassTypeFragment_JvHe extends MyBaseFragment<MyBaseActivity> implements OnConnectServerStateListener<Integer>, SwipeRefreshLayout.OnRefreshListener {

	/**页面标题**/
	@BindView(R.id.tv_title)
	 TextView tv_title;

	/**页面标题**/
	@BindView(R.id.radioGroup)
	RadioGroup radioGroup;

	/**网络请求是否成功**/
	public boolean connectSuccessFlag;

	/**条目被选中位置*/
//	private View rootView;

	/**刷新控件**/
	@BindView(R.id.swipeRefreshLayout)
	SwipeRefreshLayout swipeRefreshLayout;
	/**具体分类信息GridView**/
	@BindView(R.id.recycleView)
	RecyclerView recycleView;

	RecycleAdapter recycleAdapter;
	private List<GoodsClassJsonView> listData_total = new ArrayList<GoodsClassJsonView>();
	private List<GoodsClassJsonView> listData_select= new ArrayList<GoodsClassJsonView>();

	@Override
	public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_classification_jvhe, container, false);
		unbinder = ButterKnife.bind(this,rootView);
		initView();
		return rootView;
	}

	public void initView() {

		tv_title.setText("产品分类");
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				listData_select.clear();
				listData_select.addAll(listData_total.get(checkedId).getGoodsClassJsonViews());
				recycleAdapter.notifyDataSetChanged();

			}
		});
		recycleAdapter = new RecycleAdapter(mActivity,listData_select);

		//设置GridLayoutManager布局管理器，实现GridView效果,每行展示四个item
		recycleView.setLayoutManager(new GridLayoutManager(mActivity,3));
		//设置默认动画，添加addData()或者removeData()时候的动画
		recycleView.setItemAnimator(new DefaultItemAnimator());

		recycleView.setAdapter(recycleAdapter);

		swipeRefreshLayout.setColorSchemeColors(Color.RED
				, Color.GREEN
				, Color.BLUE
				, Color.YELLOW
				, Color.CYAN
				, 0xFFFE5D14
				, Color.MAGENTA);
		swipeRefreshLayout.setOnRefreshListener(this);
	}


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
		if(hidden){
			swipeRefreshLayout.setRefreshing(false);
		}else{
			if(!connectSuccessFlag){
				initData();
			}
		}
	}

	
	public void getData() {

		//获取某个特定店铺的商品分类信息
		String methodName = InformationCodeUtil.methodNameGetUserCategories;
		SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE,methodName);
		soapObject.addProperty("shopID", MyApplication.getmCustomModel(mActivity).getCurrentBrowsingShopID());
		ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
				mActivity, this, soapObject, methodName);
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
				RadioButton radioButton = (RadioButton) View.inflate(mActivity,R.layout.item_goods_classtype_radiobutton,null);
				radioButton.setId(i);
				RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
						RadioGroup.LayoutParams.MATCH_PARENT,DensityUtil.dip2px(mActivity,50));
				radioButton.setText(listData_total.get(i).getClassName());
				radioGroup.addView(radioButton,layoutParams);
				if(i ==0)
				radioButton.setChecked(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			connectSuccessFlag = false;
		}
		if(swipeRefreshLayout != null)
		swipeRefreshLayout.setRefreshing(false);

	}

	@Override
	public void connectServiceFailed(String returnStrError, String methodName, Integer state, boolean whetherRefresh) {
		connectSuccessFlag = false;
		if(swipeRefreshLayout != null)
		swipeRefreshLayout.setRefreshing(false);
		ToastUtil.show(mActivity, returnStrError);
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

			View view = mLayoutInflater.inflate(R.layout.item_adapter_goods_classifcation_jvhe, parent, false);

			return new MyViewHolder(view);
		}

		@Override
		public void onBindViewHolder(final MyViewHolder holder, final int position) {
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					GoodsClassJsonView goodsClassJsonView = listData.get(position);
					Intent intent = new Intent(mActivity, SearchGoodsActivity.class);
					intent.putExtra(InformationCodeUtil.IntentSearchGoodsFilterClassID, goodsClassJsonView.getDjLsh());
					if(!InformationCodeUtil.AppName_DaiNiFei.equals(getResources().getText(R.string.app_name))){
						//如果不是带你飞平台
						int currentShopID = MyApplication.getmCustomModel(mActivity).getCurrentBrowsingShopID();
						intent.putExtra(InformationCodeUtil.IntentSearchGoodsCurrentShopID, currentShopID);
					}
					mActivity.startActivity(intent);
				}
			});
			GoodsClassJsonView productClassModel = listData.get(position);
			ImagerLoaderUtil.getInstance(mActivity).displayMyImage(productClassModel.getImgUrl(), holder.iv_goodsImage);
			holder.tv_goodsName.setText(productClassModel.getClassName());
		}


		protected class MyViewHolder extends RecyclerView.ViewHolder {

			ImageView iv_goodsImage;
			TextView tv_goodsName;

			public MyViewHolder(View itemView) {
				super(itemView);
				iv_goodsImage = (ImageView) itemView.findViewById(R.id.iv_goodsImage);
				tv_goodsName = (TextView) itemView.findViewById(R.id.tv_goodsName);
			}
		}

	}


}
