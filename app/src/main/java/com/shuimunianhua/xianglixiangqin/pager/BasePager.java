package com.shuimunianhua.xianglixiangqin.pager;

import android.app.Activity;
import android.view.View;

public abstract class BasePager<T extends Activity> {
    //上下文
    public T mActivity;
	/**是否成功联网获取数据标记**/
	public boolean connectSuccessFlag;
	/**当前pager对应的view**/
	protected View view;
	/**当前页面对应的标题**/
	public String title;
	/**当前页面对应的活动商品分类ID**/
	public int classID;

    public BasePager(T context){
        this.mActivity = context;
        connectSuccessFlag = false;
    }
    /**
     * 对外提供一个功能：能够返回自己的界面视图
     * initView是被HomeFragment内部的Viewpager的适配器的instantiateItem调用
     */
    public abstract View initView();

    /**
     * 初始化数据
     * initData是被HomeFragment内部的Viewpager的适配器的instantiateItem调用
     */
    public abstract void initData();
    
}
