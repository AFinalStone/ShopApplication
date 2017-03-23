package com.shi.xianglixiangqin.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shi.xianglixiangqin.R;


/**
 * 选择微店分享方式
 * SHI
 * 2016-12-26 18:32:01
 */
public class SelectShareTypePopWindow extends PopupWindow {

	/**
	 * 图库选择
	 */
	private TextView tv_shareMyShopUrl;
	/**
	 * 拍照
	 */
	private TextView tv_shareMyShopImage;
	/**
	 * 取消
	 */
	private TextView tv_cancel;

	private View mMenuView;

	public SelectShareTypePopWindow(Context context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.item_pop_select_share_type, null);
		tv_shareMyShopUrl = (TextView) mMenuView.findViewById(R.id.tv_shareMyShopUrl);
		tv_shareMyShopImage = (TextView) mMenuView.findViewById(R.id.tv_shareMyShopImage);
		tv_cancel = (TextView) mMenuView.findViewById(R.id.tv_cancel);
		// 设置按钮监听
		tv_shareMyShopUrl.setOnClickListener(itemsOnClick);
		tv_shareMyShopImage.setOnClickListener(itemsOnClick);
		tv_cancel.setOnClickListener(itemsOnClick);
		
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.PopupAnimationStyle);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			@Override
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
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

}
