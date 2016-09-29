package com.shuimunianhua.xianglixiangqin.frament;


import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import butterknife.ButterKnife;
import butterknife.Bind;

import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.activity.MainActivity;

/**
 * @author SHI
 * @action 个人中心
 * @date 2016-2-2 11:26:01
 */
public class MainMessageFragment extends MyBaseFragment<MainActivity> {
    /**
     * 主页标题
     **/
    @Bind(R.id.tv_title)
    TextView tv_title;
    /**
     * 当前界面内容
     **/
    private View rootView;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = View.inflate(mActivity, R.layout.conversation_list, null);
            ButterKnife.bind(this, rootView);
            initView();
        }
        return rootView;
    }

    private void initView() {
        tv_title.setText("会话列表");
    }

    public void initData() {
        beginFragment();
    }

//
//	/**
//	 * 加载 会话列表 ConversationListFragment
//	 */
//	private void enterFragment() {
//
//			fragment = (ConversationListFragment) mActivity.getSupportFragmentManager()
//					.findFragmentById(R.id.conversationlist);
//			Uri uri = Uri
//					.parse("rong://" + mActivity.getApplicationInfo().packageName)
//					.buildUpon()
//					.appendPath("conversationlist")
//					.appendQueryParameter(
//							Conversation.ConversationType.PRIVATE.getName(),
//							"false") // 设置私聊会话非聚合显示
//							.appendQueryParameter(
//									Conversation.ConversationType.GROUP.getName(), "true")// 设置群组会话聚合显示
//									.appendQueryParameter(
//											Conversation.ConversationType.DISCUSSION.getName(),
//											"false")// 设置讨论组会话非聚合显示
//											.appendQueryParameter(
//													Conversation.ConversationType.SYSTEM.getName(), "false")// 设置系统会话非聚合显示
//													.build();
//
//			fragment.setUri(uri);
//	}

	 /**
	 * 加载 会话列表 ConversationListFragment
	 */
    private void beginFragment() {
        ConversationListFragment fragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + mActivity.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();
        fragment.setUri(uri);

        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        //rong_content 为你要加载的 id
        transaction.add(R.id.conversationlist, fragment);
        transaction.commit();
    }

}

