            package com.shi.xianglixiangqin.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.google.gson.Gson;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultMsgModel;
import com.shi.xianglixiangqin.pager.SelectCityLocationPager;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * 城市站选择popWindow
 * @author SHI
 * 2016年4月28日 16:11:17
 */
public class SelectCityZhanPopWindow extends PopupWindow implements OnConnectServerStateListener<Integer> {

	private Activity mContext;
	private List<SelectCityLocationPager> listData;
	private ViewPager viewPager;
	private RadioGroup radioGroup_indicator;
//	private ImageView iv_indicatorBottom;
	private MyPagerAdapter pagerAdapter;
	
	private int radioGroupButtonWidth;

	
	public SelectCityZhanPopWindow(Activity mContext, int width, int height,
								   boolean focusable) {
		this.mContext = mContext;
		setContentView(initView());
        setWidth(width);
        setHeight(height);
        setFocusable(focusable);	
		setAnimationStyle(R.style.PopupAnimationStyle);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		radioGroupButtonWidth = mContext.getResources().getDisplayMetrics().widthPixels/4;
	}

	private View initView(){
		View contentView = View.inflate(mContext, R.layout.item_pop_city_location_select, null);
		viewPager = (ViewPager) contentView.findViewById(R.id.viewPager);
		ImageButton btn_left = (ImageButton) contentView.findViewById(R.id.btn_left);
		ImageButton btn_right = (ImageButton) contentView.findViewById(R.id.btn_right);
		final HorizontalScrollView hscrollView_indicator = (HorizontalScrollView) contentView.findViewById(R.id.hscrollView_indicator);
		radioGroup_indicator = (RadioGroup) contentView.findViewById(R.id.radioGroup_indicator);

//		iv_indicatorBottom = (ImageView) contentView.findViewById(R.id.iv_indicatorBottom);
//		LayoutParams indicator_LayoutParams =  iv_indicatorBottom.getLayoutParams();
//		indicator_LayoutParams.width = radioGroupButtonWidth/4;
//		indicator_LayoutParams.height = DensityUtil.dip2px(mContext, 2);
//		iv_indicatorBottom.setLayoutParams(indicator_LayoutParams);
//		iv_indicatorBottom.setPadding(indicator_LayoutParams.width/4, 0, indicator_LayoutParams.width/4, 0);
		
		btn_left.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int currentId = radioGroup_indicator.getCheckedRadioButtonId();
				if(currentId > 0){
					currentId--;
				}
				RadioButton radioButton = ((RadioButton)radioGroup_indicator.getChildAt(currentId));
				if(radioButton != null)
				radioButton.performClick();
			}
		});
		btn_right.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int currentId = radioGroup_indicator.getCheckedRadioButtonId();
				if(currentId < listData.size()-1){
					currentId++;
				}
				RadioButton radioButton = ((RadioButton)radioGroup_indicator.getChildAt(currentId));
				if(radioButton != null)
				radioButton.performClick();
			}
		});
		
		radioGroup_indicator.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				LogUtil.LogShitou("当前标号", ""+checkedId);
				
				RadioButton radioButton1 = ((RadioButton)radioGroup_indicator.getChildAt(checkedId));
				RadioButton radioButton2 = ((RadioButton)(RadioButton) radioGroup_indicator.getChildAt(1));
				if(radioButton1 != null && radioButton2 != null){
					hscrollView_indicator.smoothScrollTo(
							(checkedId > 1 ? radioButton1.getLeft() : 0) 
							- radioButton2.getLeft(), 0);
					
				}
			}
		});
		
		listData = new ArrayList<SelectCityLocationPager>();
		pagerAdapter = new MyPagerAdapter(listData);
		viewPager.setAdapter(pagerAdapter);	
		return contentView;
	}
	
	public void initData(){
		getData(InformationCodeUtil.methodNameGetSiteProvince);
	}
	

	private void getData(String methodName){
		if(InformationCodeUtil.methodNameGetSiteProvince.equals(methodName)){

			SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
			requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
					(mContext, this, requestSoapObject, methodName);
			connectCustomServiceAsyncTask.execute();
			return;
		}
	}
	
	
    class MyPagerAdapter extends PagerAdapter{
    	
    	private  List<SelectCityLocationPager> listData_pagers;
    	
        public MyPagerAdapter(List<SelectCityLocationPager> listData_pagers) {
			super();
			this.listData_pagers = listData_pagers;
		}

		@Override
        public int getCount() {
			if(listData_pagers == null)
			return 0;
            return listData_pagers.size();
        }
        
        @Override
        public CharSequence getPageTitle(int position) {
        	return listData_pagers.get(position).title;
        }
        
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
        	SelectCityLocationPager currentPager = listData_pagers.get(position);
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
//		String result = returnSoapObject.getPropertyAsString(methodName + "Result");
	
		if(InformationCodeUtil.methodNameGetSiteProvince.equals(methodName)){
			LogUtil.LogShitou("返回结果",returnString);
			refreshView(returnString);
		}
		
	}
	/**
	 * 刷新页面
	 */
	private void refreshView(String result){
		try {
			Gson gson = new Gson();
			JSONResultMsgModel mJSONResultMsgModel = gson.fromJson(result, JSONResultMsgModel.class);
			JSONArray jsonArry = new JSONArray(mJSONResultMsgModel.getMsg());
			for (int i = 0; i < jsonArry.length(); i++) {
				JSONObject jsonObject = jsonArry.getJSONObject(i);
				SelectCityLocationPager basePager = new SelectCityLocationPager(mContext,this);
				basePager.title = jsonObject.getString("Name");
				basePager.classID = jsonObject.getInt("Code");
				LogUtil.LogShitou("title和classID的值为", basePager.title+"和"+basePager.classID);
				listData.add(basePager);
			}
			pagerAdapter.notifyDataSetChanged();
			refreshRadioGroup();
		} catch (Exception e) {
			e.printStackTrace();
			dismiss();
		}	
	}
	
	private void refreshRadioGroup(){

		for (int i = 0; i < listData.size(); i++) {
			RadioButton rb = (RadioButton) View.inflate(mContext, R.layout.item_radiobutton_indicator, null);
			rb.setText(listData.get(i).title);
			rb.setId(i);
			RadioGroup.LayoutParams layoutParam = new RadioGroup.LayoutParams(
					new RadioGroup.LayoutParams(radioGroupButtonWidth
							,RadioGroup.LayoutParams.WRAP_CONTENT));
			radioGroup_indicator.addView(rb,layoutParam);
		}
		radioGroup_indicator.check(0);
	}
	
	@Override
	public void connectServiceFailed(String returnStrError, String methodName, Integer state,
			boolean whetherRefresh) {
		
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		
	}
	
}
