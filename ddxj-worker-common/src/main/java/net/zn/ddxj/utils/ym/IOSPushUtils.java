package net.zn.ddxj.utils.ym;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.PropertiesUtils;
import net.zn.ddxj.utils.ym.ios.IOSBroadcast;
import net.zn.ddxj.utils.ym.ios.IOSListcast;
import net.zn.ddxj.utils.ym.ios.IOSUnicast;

/**
 * 友盟推送（ios端）
 * @author ddxj
 *
 */
public class IOSPushUtils 
{
	private static PushClient client = new PushClient();
	
	/**
	 * 用于单条推送，为通知类
	* @Title: YMPushUtils.java  
	* @param @param ticker 通知栏提示文字
	* @param @param title 通知标题
	* @param @param text参数  通知文字描述
	* @param @param linkUrl参数  通知URL
	* @return void    返回类型 
	* @throws
	* @Package net.zn.ddxj.utils.ym  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @date 2018年5月16日  
	* @version V1.0
	 * @throws Exception 
	 */
	public static void sendIOSSingleMsg(Map<String,Object> param) throws Exception
	{
		IOSUnicast unicast = new IOSUnicast(PropertiesUtils.getPropertiesByName("u_push_ios_app_key"),PropertiesUtils.getPropertiesByName("u_push_ios_app_master_secret"));
		// TODO Set your device token
		unicast.setDeviceToken(param.get("deviceToken").toString());
		unicast.setAlert(param.get("content").toString());// 当content-available=1时(静默推送)，可选; 否则必填。// 可为JSON类型和字符串类型
		unicast.setBadge(0);
		unicast.setSound("default");//通知声音
		String customized = String.valueOf(param.get("customized"));
		if(!CmsUtils.isNullOrEmpty(customized))//自定义参数
		{
			JSONObject custom = JSONObject.parseObject(customized);
			
			for(String jsonKey : custom.keySet())
			{
				unicast.setCustomizedField(jsonKey,custom.getString(jsonKey));
			}
		}
		// TODO 如果您的应用程序处于生产模式下，则将“Production_mode”设置为“true” 
		unicast.setTestMode();
		unicast.setProductionMode(Boolean.valueOf(Constants.PRODUCTION_MODE));
		// Set customized fields
		unicast.setCustomizedField("test", "helloworld");
		client.send(unicast);
	}
	
	/**
	 * 广播，发送给所有人
	* @Title: IOSPushUtils.java  
	* @param @param param
	* @param @throws Exception参数  
	* @return void    返回类型 
	* @throws
	* @Package net.zn.ddxj.utils.ym  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @date 2018年5月18日  
	* @version V1.0
	 */
	public static void sendIOSBroadcast(Map<String,String> param) throws Exception {
		IOSBroadcast broadcast = new IOSBroadcast(PropertiesUtils.getPropertiesByName("u_push_ios_app_key"),PropertiesUtils.getPropertiesByName("u_push_ios_app_master_secret"));
		broadcast.setAlert(param.get("alert"));//推送内容
		broadcast.setBadge(0);
		broadcast.setSound("default");
		String customized = String.valueOf(param.get("customized"));
		if(!CmsUtils.isNullOrEmpty(customized))//自定义参数
		{
			JSONObject custom = JSONObject.parseObject(customized);
			
			for(String jsonKey : custom.keySet())
			{
				broadcast.setCustomizedField(jsonKey,custom.getString(jsonKey));
			}
		}
		broadcast.setTestMode();
		client.send(broadcast);
	}
	/**
	 * 批量广播
	* @Title: IOSPushUtils.java  
	* @param @param param
	* @param @throws Exception参数  
	* @return void    返回类型 
	* @throws
	* @Package net.zn.ddxj.utils.ym  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @date 2018年5月18日  
	* @version V1.0
	 * @return 
	 */
	public static int sendIOSBatchMsg(Map<String,Object> param) throws Exception
	{
		IOSListcast listcast = new IOSListcast(PropertiesUtils.getPropertiesByName("u_push_ios_app_key"),PropertiesUtils.getPropertiesByName("u_push_ios_app_master_secret"));
//		IOSListcast listcast = new IOSListcast("5aead35df43e487b84000072","fyorqlp4pab1jexcl4vmznwjupm1lu2a");
		listcast.setDeviceToken(param.get("deviceToken").toString());//多个token用英文逗号隔开
		org.json.JSONObject alertContent = new org.json.JSONObject();
		alertContent.put("title", param.get("title"));//标题
		//alertContent.put("subtitle", param.get("subtitle"));//简介
		alertContent.put("body", param.get("body"));//内容
		listcast.setAlert(alertContent);
		
		if(!CmsUtils.isNullOrEmpty(param.get("timingTime")))
		{
			listcast.setStartTime(param.get("timingTime").toString());
		}
		
		listcast.setSound("default");//通知声音
		listcast.setBadge(0);
		listcast.setCustomizedField("messageTypeId", "1");
		
		String customized = String.valueOf(param.get("customized"));
		if(!CmsUtils.isNullOrEmpty(customized))//自定义参数
		{
			JSONObject custom = JSONObject.parseObject(customized);
			
			for(String jsonKey : custom.keySet())
			{
				listcast.setCustomizedField(jsonKey,custom.getString(jsonKey));
			}
		}
		// TODO 如果您的应用程序处于生产模式下，则将“Production_mode”设置为“true” 
		listcast.setTestMode();
		listcast.setProductionMode(Boolean.valueOf(Constants.PRODUCTION_MODE));
		return client.send(listcast);
	}
	
	public static void main(String[] args) {
		// TODO set your appkey and master secret here
		IOSPushUtils iosPushUtils = new IOSPushUtils();
		try {
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("deviceToken", "c6c81fbcf27355a08b75e3b417377eff9d1a0659f6423525003eacdec689cec0");
			JSONObject customized = new JSONObject();
			customized.put("messageTypeId", 4);//跳转到消息中心交易页面标识
			param.put("customized", customized);
			param.put("title", "全球最大的中文搜索引擎、最大的中文网站。");
			param.put("subtitle", "“百度”二字,来自于八百年前南宋词人辛弃疾的一句词：众里寻他千百度。这句话描述了词人对理想的执着追求。");
			param.put("body", "百度拥有数万名研发工程师，这是中国乃至全球最为优秀的技术团队。这支队伍掌握着世界上最为先进的搜索引擎技术，使百度成为中国掌握世界尖端科学核心技术的中国高科技企业，也使中国成为美国、俄罗斯、和韩国之外，全球仅有的4个拥有搜索引擎核心技术的国家之一。");
			IOSPushUtils.sendIOSSingleMsg(param);
			/* TODO these methods are all available, just fill in some fields and do the test
			 * demo.sendAndroidCustomizedcastFile();
			 * demo.sendAndroidBroadcast();
			 * demo.sendAndroidGroupcast();
			 * demo.sendAndroidCustomizedcast();
			 * demo.sendAndroidFilecast();
			 * 
			 * demo.sendIOSBroadcast();
			 * demo.sendIOSUnicast();
			 * demo.sendIOSGroupcast();
			 * demo.sendIOSCustomizedcast();
			 * demo.sendIOSFilecast();
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
