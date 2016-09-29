package com.shuimunianhua.xianglixiangqin.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import butterknife.ButterKnife;
import butterknife.Bind;
import com.afinalstone.androidstudy.view.TabIndicatorView;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.pager.BasePager;
import com.shuimunianhua.xianglixiangqin.pager.SportSaleGroupPager;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

/****
 *  团购活动界面
 * @author SHI
 *  2016年3月10日 16:56:51
 */
public class SportSaleBuyGroupActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer> {
	
	/**左侧返回按钮**/
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**标题**/
	@Bind(R.id.tv_title)
	 TextView tv_title;

	/** TabIndicatorView **/
	@Bind(R.id.tabIndicatorView)
	 TabIndicatorView tabIndicatorView;
	/** 右侧旋转动画 **/
	@Bind(R.id.cb_saleTypeSelect)
	 CheckBox cb_saleTypeSelect;
	/** viewPager **/
	@Bind(R.id.viewPager)
	 ViewPager viewPager;
	/**页面数据**/
    private List<SportSaleGroupPager> listData_pagers;
    /**适配器**/
    private MyPagerAdapter pagerAdapter;
    /**当前活动类型为精品团购**/
	public final int currentPlatformActionType = 2;
	/**如果是获取某个特定店铺的活动商品，这个字段会变成当前店铺ID**/
	public int currentShopID;
	/**获取平台活动商品，这个字段为城市站,获取某个特定店铺的活动商品，这个字段为空**/
	public String currentCityCode;

	private PopupWindow pop;
	@Override
	public void initView() {
		setContentView(R.layout.activity_sport_sale);
		ButterKnife.bind(this);
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_title.setText("团购中心");
		listData_pagers = new ArrayList<SportSaleGroupPager>();
		pagerAdapter = new MyPagerAdapter();
		viewPager.setAdapter(pagerAdapter);
		cb_saleTypeSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked){
				RotateAnimation ra01 = new RotateAnimation(0f, 180f,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
				ra01.setDuration(300);
				ra01.setFillAfter(true);
				cb_saleTypeSelect.startAnimation(ra01);
				showPopuWindow();
			}else{
				RotateAnimation ra02 = new RotateAnimation(180f, 0f,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				ra02.setDuration(300);
				ra02.setFillAfter(true);
				cb_saleTypeSelect.startAnimation(ra02);
			}
		}
	});
		
		tabIndicatorView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				viewPager.setCurrentItem(checkedId);
			}
		});
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(final int position) {
				tabIndicatorView.setCurrentSelectItem(position);
			}
			
			@Override
			public void onPageScrolled(final int arg0, final float arg1, final int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(final int position) {
			}
		});
		
	}
	
	private void showPopuWindow(){
		View contentView = View.inflate(mContext, R.layout.item_pop_sport_type_select, null);
		GridView gridView = (GridView) contentView.findViewById(R.id.gridView);
		GridViewAdapter gridViewAdapter = new GridViewAdapter();
		gridView.setAdapter(gridViewAdapter);
		pop = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, displayDeviceHeight/2, true);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOutsideTouchable(true);
		pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				cb_saleTypeSelect.setChecked(false);
			}
		});
		pop.showAsDropDown(tabIndicatorView);
	}
	
	@Override
	public void initData() {
		currentShopID = getIntent().getIntExtra(InformationCodeUtil.IntentSportSaleByActivityCurrentShopID,0);
		if(currentShopID == 0){
			currentCityCode = MyApplication.getmCustomModel(mContext).getLocCityCode();
			getData();
		}else{
			currentCityCode = "";
			refreshShopSportView();
		}
	}

	private void refreshShopSportView(){
			List<String> listData = new ArrayList<String>();
			SportSaleGroupPager basePager = new SportSaleGroupPager(this);
			basePager.title = "店铺活动商品";
			basePager.classID = -1;
			listData.add(basePager.title);
			listData_pagers.add(basePager);
			pagerAdapter.notifyDataSetChanged();
			tabIndicatorView.initIndicatorBottom(displayDeviceWidth/3, RadioGroup.LayoutParams.MATCH_PARENT);
			tabIndicatorView.refreshRadioGroup(listData);
	}

	public void getData(){
		String methodName = InformationCodeUtil.methodNameGetActGoodsClass;
		SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
		requestSoapObject.addProperty("platformActionType", currentPlatformActionType);
		requestSoapObject.addProperty("cityCode", MyApplication.getmCustomModel(mContext).getLocCityCode());
		ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask
				(mContext, this, requestSoapObject , methodName);	
		connectGoodsServiceAsyncTask.execute();
	}
	
	private class GridViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return listData_pagers.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mContext, R.layout.item_adapter_sportsale_gridview, null);
			TextView tv = (TextView) view.findViewById(R.id.textView);
			tv.setText(listData_pagers.get(position).title);
			tv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					tabIndicatorView.setCurrentSelectItem(position);
					pop.dismiss();
				}
			});
			return view;
		}
	}
	
    class MyPagerAdapter extends PagerAdapter{

    	
        @Override
        public int getCount() {
        	if(listData_pagers == null){
        		return 0;
        	}
            return listData_pagers.size();
        }
        
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager currentPager = listData_pagers.get(position);
            View view = currentPager.initView();
            currentPager.initData();//***********初始化数据******************
            container.addView(view);
            return view;
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
    
	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
//		LogUtil.LogShitou("返回结果",provinceSoapObject.toString());
		try {
			JSONArray jsonArray = new JSONArray(returnString);
			List<String> listData = new ArrayList<String>();
			
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				SportSaleGroupPager basePager = new SportSaleGroupPager(this);
				basePager.title = jsonObject.getString("ClassName");
				basePager.classID = jsonObject.getInt("DjLsh");
				listData.add(basePager.title);
				listData_pagers.add(basePager);
			}
			pagerAdapter.notifyDataSetChanged();
			tabIndicatorView.initIndicatorBottom(displayDeviceWidth/4, RadioGroup.LayoutParams.MATCH_PARENT);
			tabIndicatorView.refreshRadioGroup(listData);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {
		
	}
	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		
	}

}
