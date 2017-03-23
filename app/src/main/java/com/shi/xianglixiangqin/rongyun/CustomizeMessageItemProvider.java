package com.shi.xianglixiangqin.rongyun;

import com.google.gson.Gson;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.activity.GoodsDetailGeneralActivity;
import com.shi.xianglixiangqin.activity.GoodsDetailSportActivity;
import com.shi.xianglixiangqin.model.GoodsGeneralModel;
import com.shi.xianglixiangqin.model.GoodsSportModel;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider.MessageProvider;
import io.rong.imlib.model.Message;

@ProviderTag(messageContent = RCDTestMessage.class)
public class CustomizeMessageItemProvider extends
		MessageProvider<RCDTestMessage> {

	class ViewHolder {
		RelativeLayout relativeLayout_goodsInfo;
		ImageView iv_goodsImages;
		TextView tv_goodsName;
		TextView tv_goodsPrice;
	}

	@Override
	public View newView(Context context, ViewGroup arg1) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.item_customize_message, null);
		ViewHolder holder = new ViewHolder();
		holder.relativeLayout_goodsInfo = (RelativeLayout) view.findViewById(R.id.relativeLayout_goodsInfo);
		holder.iv_goodsImages = (ImageView) view.findViewById(R.id.iv_goodsImages);
		holder.tv_goodsName = (TextView) view.findViewById(R.id.tv_goodsName);
		holder.tv_goodsPrice = (TextView) view.findViewById(R.id.tv_goodsPrice);
		view.setTag(holder);
		return view;
	}

	@Override
	public void bindView(View view, int position,
			RCDTestMessage customizeMessage, UIMessage message) {
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.iv_goodsImages.setImageResource(R.drawable.ic_empty);
		holder.tv_goodsName.setText("");
		holder.tv_goodsPrice.setText("");
		if (message.getMessageDirection() == Message.MessageDirection.SEND) {
			holder.relativeLayout_goodsInfo
					.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
		} else {
			holder.relativeLayout_goodsInfo
					.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
		}
		try {
				String flagGoodsType = customizeMessage.getContent();
				//普通商品
				if(ConversationGeneralActivity.FLAGGENERALTYPEGOODS.equals(flagGoodsType)){
					GoodsGeneralModel goodsDetail = parseMessageToGoodsGeneralModel(customizeMessage);
					if(goodsDetail != null){
						ImagerLoaderUtil.getInstance(
								view.getContext()).displayMyImage(goodsDetail.getImages().get(0), holder.iv_goodsImages);
						holder.tv_goodsName.setText(goodsDetail.getGoodsName());
						holder.tv_goodsPrice.setText("￥"+(int)goodsDetail.getMinPrice()+"-￥"+(int)goodsDetail.getMaxPrice());
						return;
					}
				}
				//活动商品
				if(ConversationGeneralActivity.FLAGSPORTTYPEGOODS.equals(flagGoodsType)){
					GoodsSportModel goodsDetail = parseMessageToGoodsSportModel(customizeMessage);
					if(goodsDetail != null){
						ImagerLoaderUtil.getInstance(
								view.getContext()).displayMyImage(goodsDetail.getImages().get(0), holder.iv_goodsImages);
						holder.tv_goodsName.setText(goodsDetail.getGoodsName());
						holder.tv_goodsPrice.setText("￥"+(int)goodsDetail.getPrice());
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Spannable getContentSummary(RCDTestMessage arg0) {
		return new SpannableString("商品详情");
	}

	@Override
	public void onItemClick(View view, int position, RCDTestMessage customizeMessage,
			UIMessage message) {
		try {
			Context context = view.getContext();
			String flagGoodsType = customizeMessage.getContent();

				//普通商品
				if(ConversationGeneralActivity.FLAGGENERALTYPEGOODS.equals(flagGoodsType)){
					GoodsGeneralModel goodsDetail = parseMessageToGoodsGeneralModel(customizeMessage);
					if(goodsDetail != null){
						Intent intent = new Intent(context, GoodsDetailGeneralActivity.class);
						intent.putExtra(InformationCodeUtil.IntentGoodsID, goodsDetail.getDjLsh());
						context.startActivity(intent);
						return;
					}
				}
				//活动商品
				if(ConversationGeneralActivity.FLAGSPORTTYPEGOODS.equals(flagGoodsType)){
					GoodsSportModel goodsDetail = parseMessageToGoodsSportModel(customizeMessage);
					if(goodsDetail != null){
						Intent intent = new Intent(context, GoodsDetailSportActivity.class);
						intent.putExtra(InformationCodeUtil.IntentPlatformActionID, goodsDetail.getPlatformActionID());
						intent.putExtra(InformationCodeUtil.IntentPlatformActionType, goodsDetail.getPlatformActionType());
						context.startActivity(intent);
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onItemLongClick(View view, int position, RCDTestMessage customizeMessage,
			UIMessage message) {
	}

	/**
	 * 解析当前消息对象的content内容为普通商品详情对象
	 * @param customizeMessage  当前消息对象
	 * @return
	 */
	public GoodsGeneralModel parseMessageToGoodsGeneralModel(RCDTestMessage customizeMessage){

		GoodsGeneralModel goodsDetail = null;
		try {
			Gson gson = new Gson();
//			LogUtil.LogShitou("当前普通商品content", customizeMessage.getContent());
//			LogUtil.LogShitou("当前普通商品extra", customizeMessage.getExtra());
			goodsDetail = gson.fromJson(customizeMessage.getExtra(), GoodsGeneralModel.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return goodsDetail;
	}
	/**
	 * 解析当前消息对象的content内容为活动商品详情对象
	 * @param customizeMessage  当前消息对象
	 * @return
	 */
	public GoodsSportModel parseMessageToGoodsSportModel(RCDTestMessage customizeMessage){

		GoodsSportModel goodsDetail = null;
		try {
			Gson gson = new Gson();
//			LogUtil.LogShitou("当前活动商品content", customizeMessage.getContent());
//			LogUtil.LogShitou("当前活动商品extra", customizeMessage.getExtra());
			String jsonString = customizeMessage.getExtra();
			//为了兼容苹果数据
			jsonString = jsonString.replaceAll("IsRunning", "Runiing");
			goodsDetail = gson.fromJson(jsonString, GoodsSportModel.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return goodsDetail;
	}

}

