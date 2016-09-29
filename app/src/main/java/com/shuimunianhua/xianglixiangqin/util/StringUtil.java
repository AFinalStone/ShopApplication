package com.shuimunianhua.xianglixiangqin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;


/***
 * @action 字符串工具类
 * @author ZHU
 * @date  2015-6-26 上午11:10:34
 */
public class StringUtil {

	
//	/**手机号正则表达式**/
//	public  static String regExpPhotoNumber = "^[1][0-9]{10}$";
//	/**邮箱号正则表达式**/
//	public final static String regExpEmailNumber = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	/**
	 * 判断字符串是否符合正则表达式
	 * @param textString 	要判断的字符串
	 * @param regex 	条件
	 * @return
	 */
	public static boolean matchRegex(String textString,String regex){	
		if(TextUtils.isEmpty(textString)){
			return false;
		}
		return textString.matches(regex);
	}

	/**
	 * 判断字符串是否为null
	 * @param string
	 * @return true 为null　false 不为null
	 */
	public static boolean isEmpty(String string){
		return TextUtils.isEmpty(string);
	}
	/**
	 * 字符串首字母大写
	 * @param textString
	 * @return
	 */
	public static String InitialsUpperCase(String textString){
		char[] textStringArray = textString.toCharArray();
		if(Character.isLowerCase(textStringArray[0])){
			textStringArray[0]-=32;
			return String.valueOf(textStringArray);
		}
		return textString;
	}

	/**去除某字符**/
	public static String removingCharacters(String strString,String c){
		return strString.replace(c,""); 
	}
	
	/***
	 * 字符串转整形
	 * @return
	 */
	public static int characterTransferValue(String stringText){
		if(stringText.isEmpty()){
			return 0;
		}
		return Integer.parseInt(stringText);
	}
	
	/**将浮点型数字按照pattern的格式转换成字符串**/
	public static String doubleToString(Double doubleNum, String pattern){
		DecimalFormat dcmFmt = new DecimalFormat(pattern);
		return dcmFmt.format(doubleNum);
	}
	
	/**
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
	
	/**
	 * 去除特殊字符或将所有中文标号替换为英文标号
	 * 
	 * @param str
	 * @return
	 */
	public static String stringFilter(String str) {
		str = str.replaceAll("【", "[").replaceAll("】", "]")
				.replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * 把流对象转换为字符串
	 * @param is
	 * @return
	 */
	public static String inputStreamToString(InputStream is) {      
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));      
        StringBuilder sb = new StringBuilder();      
        String line = null;      
       try {      
           while ((line = reader.readLine()) != null) {      
                sb.append(line + "\n");      
            }      
        } catch (IOException e) {      
            e.printStackTrace();      
        } finally {      
           try {      
                is.close();      
            } catch (IOException e) {      
                e.printStackTrace();      
            }      
        }      
    
       return sb.toString();      
    }
	

}
