package com.shuimunianhua.xianglixiangqin.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.frament.MyBaseFragment;
import com.shuimunianhua.xianglixiangqin.frament.ShopGoodsClassTypeFragment;
import com.shuimunianhua.xianglixiangqin.frament.ShopHomeFragment;
import com.shuimunianhua.xianglixiangqin.frament.ShopInstructionFragment;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/****
 * @author SHI
 * @action 店铺界面
 * @date 2016-1-20 20:28:08
 */
public class ShopActivity extends MyBaseActivity {

    /**返回**/
    @Bind(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    /**标题**/
    @Bind(R.id.tv_title)
    public TextView tv_title;
    /**购物车**/
    @Bind(R.id.tv_titleRight)
    TextView tv_titleRight;

    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.shop_main_radio)
    public RadioGroup shop_main_radio;
    /**
     * 当前店铺对象
     **/
    public int currentShopID = -1;
    public int currentShopUserID = -1;
    private final int pageIndexShopHome = 0;
    private final int pageIndexShopClass = 1;
    private final int pageIndexShopDetail = 2;


    private List<MyBaseFragment<ShopActivity>> listData_Fragment = new ArrayList<MyBaseFragment<ShopActivity>>();
    private MyFragmentAdapter mMyFragmentAdapter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText("首页");
        tv_titleRight.setVisibility(View.VISIBLE);
        tv_titleRight.setText("购物车");
        iv_titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_titleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra(InformationCodeUtil.IntentMainActivityCheckID,
                        R.id.linearLayout_shopcart);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void initData() {
        currentShopID = getIntent().getIntExtra(InformationCodeUtil.IntentSearchGoodsCurrentShopID, -1);
        if (currentShopID == -1) {
            finish();
            return;
        }

        listData_Fragment.add(new ShopHomeFragment());
        listData_Fragment.add(new ShopGoodsClassTypeFragment());
        listData_Fragment.add(new ShopInstructionFragment());
        mMyFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), listData_Fragment);
        viewPager.setAdapter(mMyFragmentAdapter);
        //默认选中首页
        shop_main_radio.check(R.id.rb_shop_home);
        shop_main_radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_shop_home://首页
                        viewPager.setCurrentItem(pageIndexShopHome);
                        tv_title.setText("首页");
                        break;
                    case R.id.rb_shop_class://分类
                        viewPager.setCurrentItem(pageIndexShopClass);
                        tv_title.setText("分类");
                        listData_Fragment.get(pageIndexShopClass).initData();
                        break;
                    case R.id.rb_shop_detail://详情
                        tv_title.setText("简介");
                        viewPager.setCurrentItem(pageIndexShopDetail);
                        break;
                    default:
                        break;
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case pageIndexShopHome://首页
                        shop_main_radio.check(R.id.rb_shop_home);
                        break;
                    case pageIndexShopClass://分类
                        shop_main_radio.check(R.id.rb_shop_class);
                        break;
                    case pageIndexShopDetail://详情
                        shop_main_radio.check(R.id.rb_shop_detail);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    private class MyFragmentAdapter extends FragmentPagerAdapter {

        private List<MyBaseFragment<ShopActivity>> listData;

        public MyFragmentAdapter(FragmentManager fm, List<MyBaseFragment<ShopActivity>> listData) {
            super(fm);
            this.listData = listData;
        }

        @Override
        public Fragment getItem(int position) {

            return listData.get(position);
        }

        @Override
        public int getCount() {
            return listData.size();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < listData_Fragment.size(); i++) {
            ButterKnife.unbind(listData_Fragment.get(i));
        }
    }
}

















