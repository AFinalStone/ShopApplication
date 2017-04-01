package com.shi.xianglixiangqin.json;

/***
 * @action 返回结果实体类
 * @author SHI
 * @date  2015-8-4 下午8:20:01
 */
public class JSONResultMsgModel {
	/**状态(0失败/1成功)**/
	private int Sign;
	/**消息**/
	private String Msg;
	/**记录编号**/
	private int DjLsh;
	/**订单编号**/
	private String Tags;
	public int getSign() {
		return Sign;
	}
	public void setSign(int sign) {
		Sign = sign;
	}
	public String getMsg() {
		return Msg;
	}
	public void setMsg(String msg) {
		Msg = msg;
	}
	public int getDjLsh() {
		return DjLsh;
	}
	public void setDjLsh(int djLsh) {
		DjLsh = djLsh;
	}

	public String getTags() {
		return Tags;
	}

	public void setTags(String tags) {
		Tags = tags;
	}
}
