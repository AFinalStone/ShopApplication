package com.shi.xianglixiangqin.activity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import com.afinalstone.androidstudy.view.TabIndicatorView;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.model.CustomModel;
import com.shi.xianglixiangqin.pager.BasePager;
import com.shi.xianglixiangqin.pager.MyOrderGeneralPager;
import com.shi.xianglixiangqin.pager.MyOrderToPayPager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

/****
 *  我的订单界面
 * @author SHI
 *  2016年3月10日 16:56:51
 */
public class MyOrderActivity extends MyBaseActivity{
	/**左侧返回按钮**/
	@BindView(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**标题**/
	@BindView(R.id.tv_title)
	 TextView tv_title;
	/** tab **/
	@BindView(R.id.tabIndicatorView)
	 TabIndicatorView tabIndicatorView;
	/** viewPager **/
	@BindView(R.id.viewPager)
	 ViewPager viewPager;
	/**页面数据**/
    private List<BasePager> listData_pagers;
    /**适配器**/
    private MyPagerAdapter pagerAdapter;
	/** 当前登录用户 **/
	public CustomModel mCustomModel;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_my_order);
		ButterKnife.bind(this);
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_title.setText("我的订单");
		tabIndicatorView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				viewPager.setCurrentItem(checkedId);
			}
		});
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(final int position) {
				tabIndicatorView.setCurrentSelectItem(position);
				listData_pagers.get(position).initView();
				listData_pagers.get(position).initData();
			}
			
			@Override
			public void onPageScrolled(final int arg0, final float arg1, final int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(final int position) {
			}
		});
		
	}
	
	@Override
	public void initData() {
		mCustomModel = MyApplication.getmCustomModel(mContext);
		listData_pagers = new ArrayList<BasePager>();
		List<String> listData_title = new ArrayList<String>();
		
		BasePager basePager = new MyOrderGeneralPager(this);
		basePager.title = "全部";
		basePager.classID = 0;
		listData_title.add(basePager.title);
		listData_pagers.add(basePager);
		
		basePager = new MyOrderToPayPager(this);
		basePager.title = "待付款";
		basePager.classID = 1;
		listData_title.add(basePager.title);
		listData_pagers.add(basePager);
		
		basePager = new MyOrderGeneralPager(this);
		basePager.title = "待发货";
		basePager.classID = 3;
		listData_title.add(basePager.title);
		listData_pagers.add(basePager);
		
		basePager = new MyOrderGeneralPager(this);
		basePager.title = "待收货";
		basePager.classID = 4;
		listData_title.add(basePager.title);
		listData_pagers.add(basePager);
		
		basePager = new MyOrderGeneralPager(this);
		basePager.title = "欠款订单";
		basePager.classID = 16;
		listData_title.add(basePager.title);
		listData_pagers.add(basePager);
		
		pagerAdapter = new MyPagerAdapter();
		viewPager.setAdapter(pagerAdapter);
		tabIndicatorView.initIndicatorBottom(displayDeviceWidth/5, RadioGroup.LayoutParams.MATCH_PARENT);
		tabIndicatorView.refreshRadioGroup(listData_title);
		viewPager.postDelayed(new Runnable() {
			@Override
			public void run() {
				tabIndicatorView.setCurrentSelectItem(0);
//				listData_pagers.get(1).initView();
//				listData_pagers.get(1).initData();
			}
		},50);
	}
	
	@Override
	protected void onResume() {

		int positionChange = viewPager.getCurrentItem();

		if(positionChange == 0){
			for(int i=0; i<listData_pagers.size(); i++){
				listData_pagers.get(i).connectSuccessFlag = false;
			}
		}else if(positionChange == listData_pagers.size()-1 ){
			listData_pagers.get(positionChange).connectSuccessFlag = false;
			listData_pagers.get(0).connectSuccessFlag = false;
		}else if("待发货".equals(listData_pagers.get(positionChange).title)){

		}
		else{
			listData_pagers.get(positionChange).connectSuccessFlag = false;
			listData_pagers.get(0).connectSuccessFlag = false;
			listData_pagers.get(positionChange+1).connectSuccessFlag = false;
		}
		listData_pagers.get(positionChange).initView();
		listData_pagers.get(positionChange).initData();
		super.onResume();
	}

	
    class MyPagerAdapter extends PagerAdapter{

    	
        @Override
        public int getCount() {
            return listData_pagers.size();
        }
        
        @Override
        public CharSequence getPageTitle(int position) {
        	return listData_pagers.get(position).title;
        }
        
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager currentPager = listData_pagers.get(position);
            View view = currentPager.initView();
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
	


}
