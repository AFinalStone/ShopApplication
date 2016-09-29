package com.shuimunianhua.xianglixiangqin.rongyun;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.activity.MyBaseActivity;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.StringUtil;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

/**
 * @ClassName: ConversationActivity 
 * @Description:融云会话页面
 * @author SHI
 * @date 2016年5月3日 11:28:51
 */
public class ConversationGeneralActivity extends MyBaseActivity{
	/**返回界面**/
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**标题文本**/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	/**目标 Id**/
	private String mTargetId;
	/**刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId**/
	private String mTargetIds;
	/**会话标题**/
	private String strtitle;
	/**会话附带内容(当前会话是否从商品详情进入)**/
	private String strGoodsType;
	/**会话附带内容(商品详情json)**/
	private String jsonGoodsDetail;
	/**会话类型**/
	private Conversation.ConversationType mConversationType;
	/**会话对象id**/
	public static final String INTENT_TARGETID = "targetId";
	/**会话对象标题**/
	public static final String INTENT_TITLE = "title";
	/**当前会话是否从商品详情进入**/
	public static final String INTENT_GOODSTYPE = "goodsType";
	/**当前会话是否从商品详情进入**/
	public static final String INTENT_JSONGOODSDETAIL = "jsonGoodsDetail";
	/**标记字段,存放当前商品是什么类型的**/
	public static final String KEYCONTENT = "content";
	/**额外字段，存放当前商品详情**/
	public static final String KEYEXTRA = "extra";
	/**当前商品是普通商品**/
	public static final String FLAGGENERALTYPEGOODS = "1";
	/**当前商品是活动商品**/
	public static final String FLAGSPORTTYPEGOODS = "2";	

	@Override
	public void initView() {
		setContentView(R.layout.conversation);
		ButterKnife.bind(this);
		iv_titleLeft.setVisibility(View.VISIBLE);
		this.iv_titleLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void initData() {
		/**
		 * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
		 */
		Intent intent = getIntent();

		LogUtil.LogShitou("用户信息:", intent.getDataString());
		strtitle = intent.getData().getQueryParameter(INTENT_TITLE);
		mTargetId = intent.getData().getQueryParameter(INTENT_TARGETID);
		strGoodsType = intent.getData().getQueryParameter(INTENT_GOODSTYPE);
		if (StringUtil.isEmpty(strtitle) || "null".equals(strtitle)) {
			tv_title.setText("会话窗口");
		} else {
			tv_title.setText(strtitle);
		}
		//获得当前会话类型
		mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

//		if (MyApplication.getmCustomModel(mContext).getDjLsh() != 16) {
//			mTargetId = "16";
//		}
		//如果是从商品详情进来的会话，则先把商品详情发送过去
		if (!(StringUtil.isEmpty(strGoodsType) || "null".equals(strGoodsType))) {
			LogUtil.LogShitou("商品类型" + ":" + strGoodsType);
			jsonGoodsDetail = intent.getStringExtra(INTENT_JSONGOODSDETAIL);
			RCDTestMessage customizeMessage = new RCDTestMessage(strGoodsType, jsonGoodsDetail);
			RongIM.getInstance().getRongIMClient().sendMessage(Conversation.ConversationType.PRIVATE, mTargetId,
					customizeMessage, "", "", null);
		}
			enterFragment(mConversationType, mTargetId);
		}

		@Override
		protected void onNewIntent (Intent intent){
			/**
			 * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
			 */
			LogUtil.LogShitou("用户信息:", intent.getDataString());
			strtitle = intent.getData().getQueryParameter(INTENT_TITLE);
			mTargetId = intent.getData().getQueryParameter(INTENT_TARGETID);
			if (StringUtil.isEmpty(strtitle) || "null".equals(strtitle)) {
				tv_title.setText("会话窗口");
			} else {
				tv_title.setText(strtitle);
			}
			//获得当前会话类型
			mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

			if (MyApplication.getmCustomModel(mContext).getDjLsh() != 16) {
				mTargetId = "16";
			}
			enterFragment(mConversationType, mTargetId);
			super.onNewIntent(intent);
		}

		/**
		 * 加载会话页面 ConversationFragment
		 *
		 * @param mConversationType 会话类型
		 * @param mTargetId 目标 Id
		 */
		@SuppressLint("DefaultLocale")
		private void enterFragment (Conversation.ConversationType mConversationType, String
		mTargetId){
			ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);
			Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
					.appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
					.appendQueryParameter("targetId", mTargetId).build();
			fragment.setUri(uri);
		}

}
