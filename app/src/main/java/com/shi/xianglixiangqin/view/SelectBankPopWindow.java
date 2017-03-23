package com.shi.xianglixiangqin.view;


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

import com.afinalstone.androidstudy.view.wheel.widget.OnWheelChangedListener;
import com.afinalstone.androidstudy.view.wheel.widget.WheelView;
import com.afinalstone.androidstudy.view.wheel.widget.adapters.BaseWheelTextAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.model.AddressSelectModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * 选择银行
 * SHI
 * 2016年5月19日 15:31:27
 */
public class SelectBankPopWindow extends PopupWindow implements OnWheelChangedListener {

	private Context mContext;

	private List<String> listData_All;

	public String currentSelectBank;
	/**
	 * 银行的WheelView控件
	 */
	private WheelView wheelView_Bank;

	/**取消**/
	private TextView tv_cancel;
	/**确定**/
	private TextView tv_ok;

	View selectBankView;

	@SuppressLint("InflateParams")
	public SelectBankPopWindow(Context context, OnClickListener itemsOnClick) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		selectBankView = inflater.inflate(R.layout.dialog_bank_select_bottom, null);
		initSelectBankData();

		wheelView_Bank = (WheelView) selectBankView.findViewById(R.id.wheelView_Bank);
		wheelView_Bank.setViewAdapter(new MyWheelAdapter(mContext, listData_All));
		// 添加change事件
		wheelView_Bank.addChangingListener(this);
		wheelView_Bank.setVisibleItems(7);
		wheelView_Bank.setCurrentItem(0);
		tv_cancel = (TextView) selectBankView.findViewById(R.id.tv_cancel);
		tv_ok = (TextView) selectBankView.findViewById(R.id.tv_ok);
		tv_cancel.setOnClickListener(itemsOnClick);
		tv_ok.setOnClickListener(itemsOnClick);

		// 设置SelectPicPopupWindow的View
		this.setContentView(selectBankView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		selectBankView.setOnTouchListener(new OnTouchListener() {

			@Override
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {

				int height = selectBankView.findViewById(R.id.linearLayout_bottom).getTop();
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
		currentSelectBank = listData_All.get(newValue);
	}

	private class MyWheelAdapter extends BaseWheelTextAdapter<String> {

		public MyWheelAdapter(Context context, List<String> listData) {
			super(context, listData);
		}

		@Override
		protected CharSequence getItemText(int index) {
			return listData.get(index);
		}
	}

	private void initSelectBankData(){

		listData_All = new ArrayList<>();
		listData_All.add("中国工商银行");
		listData_All.add("中国工商银行");
		listData_All.add("中国工商银行");
		listData_All.add("中国工商银行");
		listData_All.add("中国工商银行");
		listData_All.add("中国工商银行");
		listData_All.add("中国建设银行");
		listData_All.add("中国建设银行");
		listData_All.add("中国建设银行");
		listData_All.add("中国建设银行");
		listData_All.add("中国工商银行");
		listData_All.add("中国工商银行");
		listData_All.add("中国建设银行");
		listData_All.add("中国工商银行");
		listData_All.add("中国工商银行");
//		AssetManager am = mContext.getAssets();
//		Gson gson = new Gson();
//		try {
//			InputStream is = am.open("json_address_select");
//			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//			StringBuilder sb = new StringBuilder();
//			String line = null;
//			try {
//				while ((line = reader.readLine()) != null) {
//					sb.append(line + "\n");
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					is.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			listData_All = gson.fromJson(sb.toString(), new TypeToken<List<AddressSelectModel>>(){}.getType());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		currentSelectBank = listData_All.get(0);
	}

}
