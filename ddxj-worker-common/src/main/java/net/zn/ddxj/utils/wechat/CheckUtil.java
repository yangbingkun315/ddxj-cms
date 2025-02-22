package net.zn.ddxj.utils.wechat;

import java.security.MessageDigest;
import java.util.Arrays;

import net.zn.ddxj.constants.Constants;


/**
 * 用于微信接入验证
 * 
 * @author dongbs May 21, 20177:54:07 PM
 *
 */
public class CheckUtil {
	// 测试服务器的token

	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		String[] arr = new String[] { Constants.TOKEN_KEY, timestamp, nonce };
		// 排序
		Arrays.sort(arr);
		// 生成字符串
		StringBuffer content = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		// sha1加密
		String temp = getSha1(content.toString());

		return temp.equals(signature);
	}

	public static String checkApiSignature(String timestamp, String noncestr, String url, String jsapi_ticket) {
		String[] arr = new String[] { "url=" + url, "timestamp=" + timestamp, "noncestr=" + noncestr,
				"jsapi_ticket=" + jsapi_ticket };

		// 排序
		Arrays.sort(arr);

		// 生成字符串
		StringBuffer content = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i == arr.length - 1)
				content.append(arr[i]);
			else
				content.append(arr[i] + "&");
		}
		// System.out.println("加密前的字符串为：" + content.toString());
		// sha1加密
		String signature = getSha1(content.toString());
		// System.out.println("加密后的字符串为：" + signature);
		return signature;
	}

	/**
	 * Sha1加密方法
	 * 
	 * @param str
	 * @return
	 */
	public static String getSha1(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("UTF-8"));

			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
	}
}
