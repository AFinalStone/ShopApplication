package com.afinalstone.androidstudy.view.roolpager;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;

/**
 * 轮播图ViewPager
 * @author SHI
 * 2016年5月23日 19:00:47
 */
public class RollViewPager extends ViewPager {
	
	
    private Context mContext;
    private List<String> pagetTopImagerUrls = new ArrayList<String>(); 
    private OnItemClickListener onItemClickListener;//条目点击监听，由调用者传递
    private MyAdapter myAdapter = new MyAdapter();
    //轮播图是否正在转动
    private boolean isRollRuning = false;
    private int timeDelay = 4000;
    private final int MESSAGE_01 = 0;
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
//            	Log.i("线程消息", "被执行");

                final int item = getCurrentItem()+1;
                postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if(pagetTopImagerUrls.size() > 1){
                            setCurrentItem(item);
                            handler.sendEmptyMessage(MESSAGE_01);
                        }
                    }
                },timeDelay);

        };
    };	
	public RollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
        setAdapter(myAdapter);
	}

	public RollViewPager(Context context) {
		super(context);
		mContext = context;
        setAdapter(myAdapter);
	}
	
	
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
	
    /**
     * 接受图片地址
     * @param topImageUrls
     */
    public void setImageUris(List<String> topImageUrls) {
        handler.removeMessages(MESSAGE_01);
    	pagetTopImagerUrls.clear();
        pagetTopImagerUrls.addAll(topImageUrls);
        myAdapter.notifyDataSetChanged();
    }

    
    /**
     * 开启轮播图自动滚动功能
     */
    public  void  startRoll() {
    	
    //通过handler发送延时消息
//    if(!isRollRuning){
        handler.removeMessages(MESSAGE_01);
        isRollRuning = true;
        post(new Runnable() {

            @Override
            public void run() {
                if(pagetTopImagerUrls.size() > 1){
                    setCurrentItem(pagetTopImagerUrls.size()*1000);
                    handler.sendEmptyMessage(MESSAGE_01);
                }
            }
        });
//    }
    }

    /**
     * 开启轮播图自动滚动功能
     */
    public  void startRoll(int timeDelay) {
    	
		if(!isRollRuning){
    		handler.removeMessages(MESSAGE_01);
    		isRollRuning = true;
    		//通过handler发送延时消息
    		if(timeDelay != 0){
    			this.timeDelay = timeDelay;
    		}
    		handler.sendEmptyMessageDelayed(MESSAGE_01, timeDelay);
		}
    }
    
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
    	
//    	if(visibility == View.GONE){
//		   handler.removeMessages(MESSAGE_01);
//    	}
//
//    	if(visibility == View.VISIBLE){
//        	if(isRollRuning){
//        		isRollRuning = false;
//        		startRoll();
//        	}
//    	}
    	
    	super.onWindowVisibilityChanged(visibility);
    }
    
    class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
        	if(pagetTopImagerUrls.size() > 1){
        		return Integer.MAX_VALUE-1;
        	}else{
        		return pagetTopImagerUrls.size();
        	}
        }
        
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
        	int currentPosition = position%pagetTopImagerUrls.size();
        	View view =  View.inflate(getContext(), R.layout.item_adapter_roolviewpager, null);
        	ImageView imageview = (ImageView) view.findViewById(R.id.myImage);
            String url = pagetTopImagerUrls.get(currentPosition);  
            container.addView(view);
            ImagerLoaderUtil.getInstance(mContext).displayMyImage(url, imageview);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }
	
//
//    private int downX = 0;//按下的x坐标
//    private int downY = 0;//按下的y坐标
//    private int downTime = 0;//按下的时�?
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//	       switch (event.getAction()) {
//           case MotionEvent.ACTION_DOWN:
//           downX = (int) event.getX();
//           downY = (int) event.getY();
//           downTime = (int) SystemClock.uptimeMillis();
//           //按下停止轮播
////   		   handler.removeMessages(MESSAGE_01);
//           break;
//
//           case MotionEvent.ACTION_MOVE:
//           break;
//
//           case MotionEvent.ACTION_UP:
//           int upX = (int) event.getX();
//           int upY = (int) event.getY();
//           int disX = Math.abs(upX - downX);
//           int disY = Math.abs(upY - downY);
//           int upTime = (int) SystemClock.uptimeMillis();
//           int disTime = upTime - downTime;
//           //何谓单击事件，用户按下与抬起的点，可以不是同一个点(5px)
//           //按下和抬起的时间。不能超过500ms
//           if(disX<5&&disY<5&& disTime<500){
//               //调用条目点击监听
//               if(onItemClickListener!=null){
//                   onItemClickListener.onItemClick(getCurrentItem());//点击的肯定是看到的
//               }
//           }
//
////           case MotionEvent.ACTION_CANCEL://如果用户在轮播图按下，然后非轮播图区域抬起，就会执行cancel
////           	if(isRollRuning){
////           		isRollRuning = false;
////           		startRoll();//重新轮播
////           	}
////           break;
//           default:
//           break;
//       }
//    	return super.onTouchEvent(event);
//    }
    
    /**
     * 给viewpager添加自定义的条目点击事件
     * 1. 定义条目点击事件接口
     * 2. 暴露一个设置条目点击监听接口的方法
     * 3. 触发条目点击监听方法
     */
    public interface OnItemClickListener {
        //条目点击，位置
        void onItemClick(int position);
    }
	
}
