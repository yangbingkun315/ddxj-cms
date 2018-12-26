package net.zn.ddxj.utils.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.alibaba.fastjson.JSONObject;

import net.zn.ddxj.utils.aliyun.FindBankUtils;

/**
 * 获取银行中文
 * @author ddxj
 *
 */
public class GetWechatBank {
	
	
	/**
	 * 获取微信所有银行列表
	* @Title: FindBankUtils.java  
	* @param @return参数  
	* @return JSONObject    返回类型 
	* @throws
	* @Package net.zn.ddxj.utils.aliyun  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @date 2018年5月11日  
	* @version V1.0
	 */
	public static JSONObject queryBankJSON()
	{
		String str = FindBankUtils.read(FindBankUtils.class.getResourceAsStream("/json/wechat-pay-bank.json"));
		JSONObject json = new JSONObject().parseObject(str);
		return json;
	}
	/**
	 * 根据微信返回的银行CODE，得出银行信息
	* @Title: GetWechatBank.java  
	* @param @param bankCode
	* @param @return参数  
	* @return String    返回类型 
	* @throws
	* @Package net.zn.ddxj.utils.wechat  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @date 2018年5月14日  
	* @version V1.0
	 */
	public static String bankCodeByBankName(String bankCode)
	{
		JSONObject queryBankJSON = GetWechatBank.queryBankJSON();
		
		return queryBankJSON.getString(bankCode);
	}
}
