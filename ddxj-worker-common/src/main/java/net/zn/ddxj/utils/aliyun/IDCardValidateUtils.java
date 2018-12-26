package net.zn.ddxj.utils.aliyun;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import net.zn.ddxj.utils.HttpUtils;
import net.zn.ddxj.utils.PropertiesUtils;
public class IDCardValidateUtils {

	/**
	 * 阿里云市场（验证身份证是否匹配）
	* @Title: IDCardValidateUtils.java  
	* @param @param realName
	* @param @param idCard
	* @param @return
	* @param @throws Exception参数  
	* @return String    返回类型 
	* @throws
	* @Package net.zn.ddxj.utils.aliyun  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @date 2018年5月11日  
	* @version V1.0
	 */
	public static String validateRealNameInterface(String realName,String idCard) throws Exception
	{
		
		String returnStr = null; // 返回结果定义
		URL url = null;
		HttpURLConnection httpURLConnection = null;
		try {
			String params = "realName="+realName+"&cardNo="+idCard; 
			url = new URL(PropertiesUtils.getPropertiesByName("realName_validate_url") + "?" + params);
			httpURLConnection = (HttpURLConnection) url.openConnection();			
			httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpURLConnection.setRequestProperty("Authorization", "APPCODE " + PropertiesUtils.getPropertiesByName("validate_appcode"));
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestMethod("POST"); 
			httpURLConnection.setUseCaches(false); // 不用缓存
			httpURLConnection.connect();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}

			reader.close();
			returnStr = buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		return returnStr;
	}
}
