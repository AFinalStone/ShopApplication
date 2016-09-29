package com.shuimunianhua.xianglixiangqin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 1.不能预加载 2.屏蔽左右滑动
 * 
 * @author wangdh
 * 
 */
public class MyLazyViewpager extends LazyViewPager {

	public MyLazyViewpager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLazyViewpager(Context context) {
		super(context);
	}

	/**
	 * 不让Viewpager中断事件，不让Viewpager消费事件
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

}
