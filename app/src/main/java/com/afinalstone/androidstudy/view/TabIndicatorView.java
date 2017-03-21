package com.afinalstone.androidstudy.view;

import java.util.List;


import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.shi.xianglixiangqin.R;


/**
 * 自定义的TabIndicator
 * @author SHI
 * 2016年5月23日 14:04:44
 */
public class TabIndicatorView extends RelativeLayout {

	private HorizontalScrollView hs_indicator;
	private RadioGroup rg_indicator;
	private ImageView iv_indicator;
	private Context mContext;
	private int radioGroupButtonWidth;
	private int radioGroupButtonHeight;
	private int currentIndicatorLeft = 0;
	private OnCheckedChangeListener onCheckedChangeListener;
	private int colorRed = 0xffe73920;
	private int colorBlack = 0xff000000;
	/**radioButton 状态颜色选择集合**/
	private ColorStateList colorStateList;
	
//	public TabIndicatorView(Context context, AttributeSet attrs, int defStyleAttr,
//			int defStyleRes) {
//		super(context, attrs, defStyleAttr, defStyleRes);
//		initView(context);
//	}

	public TabIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	public TabIndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public TabIndicatorView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		mContext = context;
		initIndicatorGroupButton(
				mContext.getResources().getDisplayMetrics().widthPixels*7 / 24
				, RadioGroup.LayoutParams.MATCH_PARENT);
		View view = View.inflate(context, R.layout.item_tab_radiogroup, null);
		hs_indicator = (HorizontalScrollView) view.findViewById(R.id.hs_indicator);
		rg_indicator = (RadioGroup) view.findViewById(R.id.rg_indicator);
		iv_indicator = (ImageView) view.findViewById(R.id.iv_indicator);
		addView(view);
	}

	/**
	 * 初始化indicator Tab 按钮的宽和高
	 * @param width
	 * @param height
	 */
	public void initIndicatorGroupButton(int width, int height){
		initIndicatorGroupButton( width, height, createColorStateList(colorRed, colorBlack));	
	}
	
	/**
	 *  初始化indicator Tab 按钮的宽和高
	 * @param width
	 * @param height
	 * @param colorStateList  颜色状态选择器
	 */
	public void initIndicatorGroupButton(int width, int height, ColorStateList colorStateList){
		radioGroupButtonWidth = width;
		radioGroupButtonHeight = height;	
		this.colorStateList = colorStateList;	
	}
	
	/**
	 * 初始化底部横条Indicator的宽和高以及显示图像颜色
	 * @param width
	 * @param height
	 * @param resId
	 */
	public void initIndicatorBottom(int width, int height, int resId) {
		radioGroupButtonWidth = width;
		radioGroupButtonHeight = height;
		android.view.ViewGroup.LayoutParams indicator_LayoutParams = iv_indicator
				.getLayoutParams();
		indicator_LayoutParams.width = radioGroupButtonWidth;
		iv_indicator.setLayoutParams(indicator_LayoutParams);
		iv_indicator.setPadding(indicator_LayoutParams.width / 4, 0,
				indicator_LayoutParams.width / 4, 0);
		iv_indicator.setImageResource(resId);
	}
	/**
	 * 初始化底部横条Indicator的宽和高
	 * @param width
	 * @param height
	 */
	public void initIndicatorBottom(int width, int height) {
		radioGroupButtonWidth = width;
		radioGroupButtonHeight = height;
		android.view.ViewGroup.LayoutParams indicator_LayoutParams = iv_indicator
				.getLayoutParams();
		indicator_LayoutParams.width = radioGroupButtonWidth;
		iv_indicator.setLayoutParams(indicator_LayoutParams);
		iv_indicator.setPadding(indicator_LayoutParams.width / 4, 0,
				indicator_LayoutParams.width / 4, 0);
	}

	/** 对TextView设置不同状态时其文字显示颜色 */
	private ColorStateList createColorStateList(int check, int normal) {
		int[] colors = new int[] {check, normal};
		int[][] states = new int[2][];
		states[0] = new int[] { android.R.attr.state_checked};
		states[1] = new int[] {};
		ColorStateList colorList = new ColorStateList(states, colors);
		return colorList;
	}

	/**
	 * 初始化Tab数据
	 * @param listTabText
	 */
	public void refreshRadioGroup(final List<String> listTabText) {
		//创建RadioButton并添加到RadioGroup中
		for (int i = 0; i < listTabText.size(); i++) {
			RadioButton rb = new RadioButton(mContext);
			rb.setText(listTabText.get(i));
			rb.setId(i);
			RadioGroup.LayoutParams layoutParam = new RadioGroup.LayoutParams(
					new RadioGroup.LayoutParams(radioGroupButtonWidth,
							radioGroupButtonHeight));
			rb.setLayoutParams(layoutParam);
			rb.setTextSize(14);
			rb.setTextColor(colorStateList);
			rb.setGravity(Gravity.CENTER);
			rb.setButtonDrawable(android.R.color.transparent);
			
			rg_indicator.addView(rb);
		}
		rg_indicator.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						if(rg_indicator.getChildAt(checkedId)!=null){
							TranslateAnimation animation = new TranslateAnimation(
									currentIndicatorLeft ,
									((RadioButton) rg_indicator.getChildAt(checkedId)).getLeft(), 0f, 0f);
							animation.setInterpolator(new LinearInterpolator());
							animation.setDuration(100);
							animation.setFillAfter(true);
							//执行位移动画
							iv_indicator.startAnimation(animation);
							
							//记录当前 下标的距最左侧的 距离
							currentIndicatorLeft = ((RadioButton) rg_indicator.getChildAt(checkedId)).getLeft();
							
							RadioButton radioButton1 = ((RadioButton) rg_indicator
									.getChildAt(checkedId));
							RadioButton radioButton2 = ((RadioButton) (RadioButton) rg_indicator
									.getChildAt(1));
							if (radioButton1 != null && radioButton2 != null) {
								hs_indicator.smoothScrollTo(
										(checkedId > 1 ? radioButton1.getLeft() : 0)
												- radioButton2.getLeft(), 0);
								if(onCheckedChangeListener != null)
									onCheckedChangeListener.onCheckedChanged(group, checkedId);
							}		
						}

					}
				});
		initIndicatorBottom(radioGroupButtonWidth,radioGroupButtonHeight,android.R.color.holo_red_light);
	}
	
	/**
	 * 设置监听事件
	 * @param onCheckedChangeListener
	 */
	public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
		this.onCheckedChangeListener = onCheckedChangeListener;
	}
	
	/**
	 * 设置当前选中条目
	 * @param currentPosition
	 */
	public void setCurrentSelectItem(int currentPosition){
		RadioButton radioButton = ((RadioButton) rg_indicator
				.getChildAt(currentPosition));
		if (radioButton != null)
			radioButton.performClick();
	}
	
	/**
	 * 获取当前选中条目的position
	 * @return
	 */
	public int getCurrentSelectPosition(){
		int currentIdPosition = rg_indicator.getCheckedRadioButtonId();
		return currentIdPosition;
	}
	
	
}
