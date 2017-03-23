package com.shi.xianglixiangqin.view;


import com.afinalstone.androidstudy.view.wheel.widget.OnWheelChangedListener;
import com.afinalstone.androidstudy.view.wheel.widget.WheelView;
import com.afinalstone.androidstudy.view.wheel.widget.adapters.BaseWheelTextAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.model.AddressSelectModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


/**
 * 选择收货地址
 * SHI
 * 2016年5月19日 15:31:27
 */
public class SelectAddressPopWindow extends PopupWindow implements OnWheelChangedListener {

	private Context mContext;
	/**
	 * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
	 */
	private List<AddressSelectModel> listData_All;
	private List<AddressSelectModel> listData_Second;
	private List<AddressSelectModel> listData_Third;

	public AddressSelectModel selectCurrentFirstPosition;
	public AddressSelectModel selectCurrentSecondPosition;
	public AddressSelectModel selectCurrentThirdPosition;
	/**
	 * 省的WheelView控件
	 */
	private WheelView mProvince;
	/**
	 * 市的WheelView控件
	 */
	private WheelView mCity;
	/**
	 * 区的WheelView控件
	 */
	private WheelView mArea;

	/**取消**/
	private TextView tv_cancel;
	/**确定**/
	private TextView tv_ok;

	View selectAddressView;

	@SuppressLint("InflateParams")
	public SelectAddressPopWindow(Context context, OnClickListener itemsOnClick) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		selectAddressView = inflater.inflate(R.layout.dialog_address_position_select_bottom, null);
		initSelectAddressData();

		mProvince = (WheelView) selectAddressView.findViewById(R.id.id_province);
		mCity = (WheelView) selectAddressView.findViewById(R.id.id_city);
		mArea = (WheelView) selectAddressView.findViewById(R.id.id_area);
		mProvince.setViewAdapter(new MyWheelAdapter(mContext, listData_All));
		// 添加change事件
		mProvince.addChangingListener(this);
		// 添加change事件
		mCity.addChangingListener(this);
		// 添加change事件
		mArea.addChangingListener(this);
		mProvince.setVisibleItems(7);
		mCity.setVisibleItems(7);
		mArea.setVisibleItems(7);
		mProvince.setCurrentItem(0);
		updateCities();
		updateAreas();
		tv_cancel = (TextView) selectAddressView.findViewById(R.id.tv_cancel);
		tv_ok = (TextView) selectAddressView.findViewById(R.id.tv_ok);
		tv_cancel.setOnClickListener(itemsOnClick);
		tv_ok.setOnClickListener(itemsOnClick);

		// 设置SelectPicPopupWindow的View
		this.setContentView(selectAddressView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
//		// 设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.PopupAnimationStyle);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		selectAddressView.setOnTouchListener(new OnTouchListener() {

			@Override
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {

				int height = selectAddressView.findViewById(R.id.linearLayout_bottom).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

	/**
	 * change事件的处理
	 */
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue)
	{
		if (wheel == mProvince)
		{
			updateCities();
		} else if (wheel == mCity)
		{
			updateAreas();
		} else if (wheel == mArea)
		{
			selectCurrentThirdPosition = listData_Third.get(newValue);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas()
	{
		int pCurrent = mCity.getCurrentItem();
		selectCurrentSecondPosition =  listData_Second.get(pCurrent);
		listData_Third = selectCurrentSecondPosition.getSubChinaCity();
		mArea.setViewAdapter(new MyWheelAdapter(mContext, listData_Third));
		mArea.setCurrentItem(0);
		if(listData_Third != null && listData_Third.size() != 0){
			selectCurrentThirdPosition = listData_Third.get(0);
		}
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities()
	{
		int pCurrent = mProvince.getCurrentItem();
		selectCurrentFirstPosition = listData_All.get(pCurrent);
		listData_Second = selectCurrentFirstPosition.getSubChinaCity();
		mCity.setViewAdapter(new MyWheelAdapter(mContext, listData_Second));
		mCity.setCurrentItem(0);
		updateAreas();
	}

	private class MyWheelAdapter extends BaseWheelTextAdapter<AddressSelectModel> {

		public MyWheelAdapter(Context context, List<AddressSelectModel> listData) {
			super(context, listData);
		}

		@Override
		protected CharSequence getItemText(int index) {
			return listData.get(index).getName();
		}
	}

	private void initSelectAddressData(){
		AssetManager am = mContext.getAssets();
		Gson gson = new Gson();
		try {
			InputStream is = am.open("json_address_select");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			listData_All = gson.fromJson(sb.toString(), new TypeToken<List<AddressSelectModel>>(){}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
