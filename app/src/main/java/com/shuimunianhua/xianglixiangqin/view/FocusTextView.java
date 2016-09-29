package com.shuimunianhua.xianglixiangqin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 焦点TextView
 * @author SHI
 * @time 2016/7/29 15:27
 */
public class FocusTextView extends TextView {

	public FocusTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FocusTextView(Context context) {
		super(context);
	}

	
	@Override
	public boolean isFocused() {
		return true;
	}
	
}
