package com.shi.xianglixiangqin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class ScrollGridView extends GridView {
	public ScrollGridView(Context context) {
		super(context);
	}

	public ScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}
	
    /**
     * Demo描述:
     * 监听ScrollView滑动到顶端和底部
     * 
     * 注意事项:
     * 1 mScrollView.getChildAt(0).getMeasuredHeight()表示:
     *   ScrollView所占的高度.即ScrollView内容的高度.常常有一
     *   部分内容要滑动后才可见,这部分的高度也包含在了
     *   mScrollView.getChildAt(0).getMeasuredHeight()中
     *   
     * 2 view.getScrollY表示:
     *   ScrollView顶端已经滑出去的高度
     *   
     * 3 view.getHeight()表示:
     *   ScrollView的可见高度
     *   
     */
//    private class TouchListenerImpl implements OnTouchListener{
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            switch (motionEvent.getAction()) {
//            case MotionEvent.ACTION_DOWN:
// 
//                break;
//            case MotionEvent.ACTION_UP:
//                 int scrollY=view.getScrollY();
//                 int height=view.getHeight();
//                 int scrollViewMeasuredHeight=getChildAt(0).getMeasuredHeight();
//                 //当前控件滑动到了底部
//                 if((scrollY+height)==scrollViewMeasuredHeight){
//                	 Log.i("DownRefrushGridView", "scrollView滑动到了底部 scrollY="+scrollY);
//                	 Log.i("DownRefrushGridView", "scrollView滑动到了底部 height="+height);
//                	 Log.i("DownRefrushGridView", "scrollView滑动到了底部 scrollViewMeasuredHeight="+scrollViewMeasuredHeight);
//                 }
//                break;
// 
//            default:
//                break;
//            }
//            return false;
//        }
//         
//    };	

}
