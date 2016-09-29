package com.shuimunianhua.xianglixiangqin.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.Bind;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.adapter.MyBaseAdapter;
import com.shuimunianhua.xianglixiangqin.util.ActivityCollectorUtils;
import com.shuimunianhua.xianglixiangqin.util.DataCleanManagerUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.PreferencesUtilMy;
import com.shuimunianhua.xianglixiangqin.view.FragmentCommonDialog;

/**
 * 设置中心
 * @author SHI
 * 2016年5月3日 17:41:51
 */
public class SettingCenterActivity extends MyBaseActivity implements OnClickListener {

	
	
	/**左侧返回控件**/
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**标题**/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	/**内容**/
	@Bind(R.id.listView)
	 ListView listView;
	private List<String> listData;
	private ListViewAdapter listAdapter;
	/**注销**/
	@Bind(R.id.btn_logout)
	 Button btn_logout;
	
	private final String TypeNameCompletePersonalInfo = "完善个人信息";
	private final String TypeNameAccountSecurity = "账号安全";
	private final String TypeNameClearCache = "清除缓存";
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_setting_center);
		ButterKnife.bind(this);
		
		tv_title.setText(R.string.settingCenterTitle);
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(this);
		btn_logout.setOnClickListener(this);
	}

	@Override
	public void initData() {
		listData = new ArrayList<String>();
		listData.add(TypeNameCompletePersonalInfo);
		listData.add(TypeNameAccountSecurity);
		listData.add(TypeNameClearCache);
		listAdapter = new ListViewAdapter(mContext, listData);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				toOtherView(listData.get(position));
			}
		
		});
	}
	
	private void toOtherView(String name) {
		if (TypeNameCompletePersonalInfo.equals(name)) {
			Intent intent = new Intent(mContext, CompletePersonalInformationActivity.class);
			startActivity(intent);
			return;
		}
		if (TypeNameAccountSecurity.equals(name)) {
			startActivity(new Intent(mContext, PasswordChangeFirstActivity.class));
			return;
		}
		if (TypeNameClearCache.equals(name)) {
			
			showClearCachDialog();
			return;
		}
	}
	
	private void showLogoutDialog() {

		final FragmentCommonDialog fragmentDialog = new FragmentCommonDialog();
		fragmentDialog.initView("提示", "确定注销当前账号?", "取消", "注销",
				new FragmentCommonDialog.OnButtonClickListener() {

					@Override
					public void OnOkClick() {
						toLoginView();
					}

					@Override
					public void OnCancelClick() {
						
					}
				});
		fragmentDialog.show(getSupportFragmentManager(), "fragmentDialog");
	}

	/**注销账号，到登陆界面**/
	private void toLoginView(){
		PreferencesUtilMy.clearCustomModel(mContext);
		ActivityCollectorUtils.finishAll();
		if(InformationCodeUtil.whetherIsDaiNiFei){
			Intent intent = new Intent(mContext,
					LoginDaiNiFeiActivity.class);
			startActivity(intent);
		}else{
			Intent intent = new Intent(mContext,
					LoginShuiMuNianHuaActivity.class);
			startActivity(intent);
		}
	}
	
	/**
	 * 清除缓存
	 */
	private void showClearCachDialog() {
		String strTotlaCache = "0";
		try {
			strTotlaCache = DataCleanManagerUtil.getTotalCacheSize(mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		strTotlaCache = new StringBuffer()
		.append("当前缓存 ")
		.append(strTotlaCache).toString();
		final FragmentCommonDialog fragmentDialog = new FragmentCommonDialog();
		fragmentDialog.initView( strTotlaCache, "确定清除缓存?", "取消", "清除",
				new FragmentCommonDialog.OnButtonClickListener() {

					@Override
					public void OnOkClick() {
						DataCleanManagerUtil.clearAllCache(mContext);
					}

					@Override
					public void OnCancelClick() {
					}
				});
		fragmentDialog.show(getSupportFragmentManager(), "fragmentDialog");
	}


	
	private class ListViewAdapter extends MyBaseAdapter<String> {

		public ListViewAdapter(Context mContext, List<String> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			TextView tv_functionType;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.item_adapter_settingcenter_listview, null);
				tv_functionType = (TextView) convertView
						.findViewById(R.id.tv_name);
				convertView.setTag(tv_functionType);
			} else {
				tv_functionType = (TextView) convertView.getTag();
			}
			tv_functionType.setText(listData.get(position));
			return convertView;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_titleLeft:
			finish();
			break;
		case R.id.btn_logout:
			showLogoutDialog();		
			break;

		default:
			break;
		}
	}

}