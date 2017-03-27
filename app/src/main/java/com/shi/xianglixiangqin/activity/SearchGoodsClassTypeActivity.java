package com.shi.xianglixiangqin.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.frament.MainGoodsClassTypeFragment_DaiNiFei;
import com.shi.xianglixiangqin.frament.MainGoodsClassTypeFragment_JvHe;
import com.shi.xianglixiangqin.util.InformationCodeUtil;

/***
 *
 * @author SHI
 * 聚合批发系统商品的分类信息界面
 * 2016-2-1 11:18:24
 * 需要用到店铺用户UserID(本页请求特定店铺商品分类信息时候要用到)，店铺ID(为了进入下一个界面商品搜索界面时候使用)
 */
public class SearchGoodsClassTypeActivity extends MyBaseActivity {

	@Override
	public void initView() {
		setContentView(R.layout.activity_classification);
	}

	@Override
	public void initData() {

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		if(InformationCodeUtil.AppName_DaiNiFei.equals(getResources().getText(R.string.app_name))){
			MainGoodsClassTypeFragment_DaiNiFei mainGoodsClassTypeFragment_DaiNiFei = new MainGoodsClassTypeFragment_DaiNiFei();
			transaction.add(R.id.frameLayout, mainGoodsClassTypeFragment_DaiNiFei, "mainGoodsClassTypeFragment").commit();
		}else{
			MainGoodsClassTypeFragment_JvHe mainGoodsClassTypeFragment_JvHe = new MainGoodsClassTypeFragment_JvHe();
			transaction.add(R.id.frameLayout, mainGoodsClassTypeFragment_JvHe, "mainGoodsClassTypeFragment").commit();
		}


	}
}




