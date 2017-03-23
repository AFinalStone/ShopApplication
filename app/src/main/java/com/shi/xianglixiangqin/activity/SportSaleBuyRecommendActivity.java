package com.shi.xianglixiangqin.activity;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import butterknife.ButterKnife;
import butterknife.BindView;
import com.afinalstone.androidstudy.swiperefreshview.OnSwipeRefreshViewListener;
import com.afinalstone.androidstudy.swiperefreshview.SwipeRefreshGridView;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.model.GoodsGeneralModel;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

/****
 *  精品推荐活动界面
 * @author SHI
 *  2016年5月25日 17:08:12
 */
public class SportSaleBuyRecommendActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer>, OnSwipeRefreshViewListener {
	
	/**左侧返回按钮**/
	@BindView(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**标题**/
	@BindView(R.id.tv_title)
	 TextView tv_title;
	/**右侧标题**/
	@BindView(R.id.tv_titleRight)
	 TextView tv_titleRight;
	/** viewPager **/
	@BindView(R.id.swipeRefreshGridView)
	 SwipeRefreshGridView swipeRefreshGridView;
	/**页面数据**/
    private List<GoodsGeneralModel> listData = new ArrayList<GoodsGeneralModel>();;
    /**适配器**/
    private MyAdapter myAdapter;
	@Override
	public void initView() {
		setContentView(R.layout.activity_sport_sale_jp_recomment);
		ButterKnife.bind(this);
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_title.setText("精品推荐");
		
		myAdapter = new MyAdapter(mContext,listData);
		swipeRefreshGridView.getGridView().setAdapter(myAdapter);
		swipeRefreshGridView.getGridView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
		swipeRefreshGridView.setOnRefreshListener(this);

	}
	
	@Override
	public void initData() {
		swipeRefreshGridView.openRefreshState();	
	}

	public void getData(){
		String methodName = InformationCodeUtil.methodNameGetActGoodsClass;
		SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
		requestSoapObject.addProperty("platformActionType", 2);
		requestSoapObject.addProperty("cityCode", MyApplication.getmCustomModel(mContext).getLocCityCode());
		ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask
				(mContext, this, requestSoapObject , methodName);	
		connectGoodsServiceAsyncTask.initProgressDialog(false);
		connectGoodsServiceAsyncTask.execute();
	}
	
	
	private class MyAdapter extends MyBaseAdapter<GoodsGeneralModel>{

		public MyAdapter(Context mContext, List<GoodsGeneralModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = View.inflate(mContext, R.layout.item_adapter_sale_boutique_recomment, null);
				holder = new ViewHolder();
				holder.iv_productImage = (ImageView) convertView.findViewById(R.id.iv_productImage);
				holder.tv_productName = (TextView) convertView.findViewById(R.id.tv_productName);
			
				holder.tv_productPrice_new = (TextView) convertView.findViewById(R.id.tv_productPrice_new);
				holder.tv_productPrice_old = (TextView) convertView.findViewById(R.id.tv_productPrice_old);
				holder.tv_productPrice_old.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			GoodsGeneralModel currentGoodsGeneralModel = listData.get(position);
			
			ImagerLoaderUtil.getInstance(mContext).displayMyImage(currentGoodsGeneralModel.getImgUrl(), holder.iv_productImage);
			holder.tv_productName.setText(currentGoodsGeneralModel.getGoodsName());
			holder.tv_productPrice_new.setText("￥"+(int)currentGoodsGeneralModel.getMinPrice());
			holder.tv_productPrice_old.setText("￥"+(int)currentGoodsGeneralModel.getMaxPrice());
			return convertView;
		}
		
		private class ViewHolder {
			/**商品图片**/
			ImageView iv_productImage;
			/**商品名称**/
			TextView tv_productName;
			/**商品价格**/
			TextView tv_productPrice_new;
			TextView tv_productPrice_old;
		}
		
	}
	
	
	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
			listData.clear();
			for (int i = 0; i < 8; i++) {
				GoodsGeneralModel model = new GoodsGeneralModel();
				model.setImgUrl("drawable://"+R.drawable.imageview_a02);
				model.setGoodsName("Apple iPhone 5s(A1530) 16GB 金色 移动联通 4G 手机");
				model.setMaxPrice(6500);
				model.setMinPrice(2500);
				listData.add(model);
			}
			myAdapter.notifyDataSetChanged();
			swipeRefreshGridView.closeRefreshState();
	}	
	@Override
	public void connectServiceFailed(String returnStrError, String methodName, Integer state,
			boolean whetherRefresh) {
		swipeRefreshGridView.closeRefreshState();

	}
	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		swipeRefreshGridView.closeRefreshState();
	}

	@Override
	public void onTopRefrushListener() {
		getData();
	}

	@Override
	public void onBottomRefrushListener() {
		getData();
	}

}
