package com.shi.xianglixiangqin.activity;


import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.PreferencesUtil;


/***
 * 
 * @author SHI
 *	guide_page_01
 *  2016-2-1 11:40:26
 */
public class GuideActivity extends MyBaseActivity {

	/**guide_page_01**/
	ViewPager viewPager;
	
	/**数据**/
	 int [] imageIds = {R.mipmap.guide_page_01,R.mipmap.guide_page_02,R.mipmap.guide_page_03};
	
	MyPagerAdapter myPagerAdapter;
	@Override
	public void initView() {
		setContentView(R.layout.activity_guide);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		viewPager = (ViewPager) findViewById(R.id.lazyViewPager);
	}

	@Override
	public void initData() {
		myPagerAdapter = new MyPagerAdapter();
		viewPager.setAdapter(myPagerAdapter);
	}
	
    class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return imageIds.length;
        }
        /**
         * 1. 根据position获取对应的view，给view添加到container
         * 2. 返回一个view（Viewpaer的每个界面的内容）
         *      采用的是Viewpager+自定义的view（对外提供自己长什么样子：view）
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int currentImageViewId = imageIds[position];
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            imageView.setBackgroundResource(currentImageViewId);
            if(position == imageIds.length-1){
            	imageView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						PreferencesUtil.putBoolean(mContext,InformationCodeUtil.KeyFirstOpenApp,false);
						Intent intent = new Intent(mContext,LoginActivity.class);
						mContext.startActivity(intent);
						finish();
					}
				});
            }
            container.addView(imageView);
            return imageView;
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
	
	

}











