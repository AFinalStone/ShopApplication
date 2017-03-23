package com.shi.xianglixiangqin.rongyun;

import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;

import com.shi.xianglixiangqin.util.LogUtil;

import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.provider.PrivateConversationProvider;

/**
 * Created by SHI on 2016/6/27.
 */
@ConversationProviderTag(
        conversationType = "private",
        portraitPosition = 1
)
public class MyPrivateConversationProvider extends PrivateConversationProvider {

    public MyPrivateConversationProvider() {
        super();
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        return super.newView(context, viewGroup);
    }

    @Override
    public void bindView(View view, int position, UIConversation data) {
        LogUtil.LogShitou("UIConversation",data.toString());
        super.bindView(view, position, data);
    }

    @Override
    public Spannable getSummary(UIConversation data) {
        return super.getSummary(data);
    }

    @Override
    public String getTitle(String userId) {
//        LogUtil.LogShitou("测试测试测试测试测试测试测试测试测试测试测试测试"+userId);
        return super.getTitle(userId);
    }

    @Override
    public Uri getPortraitUri(String id) {
        return super.getPortraitUri(id);
    }
}
