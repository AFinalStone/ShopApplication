package com.shuimunianhua.xianglixiangqin.activity;

import android.support.v4.app.FragmentTransaction;

import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.frament.MainShoppingCartFragment;

public class ShoppingCartActivity extends MyBaseActivity {

    @Override
    public void initView() {
        setContentView(R.layout.activity_shopping_cart);
    }

    @Override
    public void initData() {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        MainShoppingCartFragment mainShoppingCartFragment = new MainShoppingCartFragment();
        transaction.add(R.id.frameLayout, mainShoppingCartFragment,"mainShoppingCartFragment");

        transaction.commit();
    }

}
