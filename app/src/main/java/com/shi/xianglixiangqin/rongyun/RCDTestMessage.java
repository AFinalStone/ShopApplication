package com.shi.xianglixiangqin.rongyun;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.util.Log;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

@MessageTag(value = "RCD:TstMsg", flag = MessageTag.ISCOUNTED
		| MessageTag.ISPERSISTED)
public class RCDTestMessage extends MessageContent {

	/**标记字段,标记当前商品是什么类型的**/
	private String content;
	/**额外字段，存放当前商品详情**/
	private String extra;

	public RCDTestMessage(String content,String extra) {
		this.content = content;
		this.extra = extra;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public RCDTestMessage(byte[] data) {
	    String jsonStr = null;

	    try {
	    	jsonStr = new String(data, "UTF-8");
	    } catch (UnsupportedEncodingException e1) {

	    }

	    try {
	        JSONObject jsonObj = new JSONObject(jsonStr);
	        setContent(jsonObj.getString(ConversationGeneralActivity.KEYCONTENT));
	        setExtra(jsonObj.getString(ConversationGeneralActivity.KEYEXTRA));
	    } catch (JSONException e) {
	        RLog.e(this, "JSONException", e.getMessage());
	    }

	}


	@Override
	public byte[] encode() {
		JSONObject jsonObj = new JSONObject();

		try {
			jsonObj.put(ConversationGeneralActivity.KEYCONTENT, content);
			jsonObj.put(ConversationGeneralActivity.KEYEXTRA, extra);

		} catch (JSONException e) {
			Log.e("JSONException", e.getMessage());
		}

		try {
			return jsonObj.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}


	// 给消息赋值。
	public RCDTestMessage(Parcel in) {
		setContent(ParcelUtils.readFromParcel(in));// 该类为工具类，消息属性
		setExtra(ParcelUtils.readFromParcel(in));// 该类为工具类，消息属性
		// 这里可继续增加你消息的属性
	}

	/**
	 * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
	 */
	public static final Creator<RCDTestMessage> CREATOR = new Creator<RCDTestMessage>() {

		@Override
		public RCDTestMessage createFromParcel(Parcel source) {
			return new RCDTestMessage(source);
		}

		@Override
		public RCDTestMessage[] newArray(int size) {
			return new RCDTestMessage[size];
		}
	};

	/**
	 * 描述了包含在 Parcelable 对象排列信息中的特殊对象的类型。
	 *
	 * @return 一个标志位，表明Parcelable对象特殊对象类型集合的排列。
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * 将类的数据写入外部提供的 Parcel 中。
	 *
	 * @param dest
	 *            对象被写入的 Parcel。
	 * @param flags
	 *            对象如何被写入的附加标志。
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		try {
			ParcelUtils.writeToParcel(dest, content);// 该类为工具类，对消息中属性进行序列化
			ParcelUtils.writeToParcel(dest, extra);// 该类为工具类，对消息中属性进行序列化
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
