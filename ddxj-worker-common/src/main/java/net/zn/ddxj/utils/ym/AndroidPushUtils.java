package net.zn.ddxj.utils.ym;

import java.util.HashMap;
import java.util.Map;

import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.PropertiesUtils;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.utils.ym.AndroidNotification.AfterOpenAction;
import net.zn.ddxj.utils.ym.android.AndroidBroadcast;
import net.zn.ddxj.utils.ym.android.AndroidListcast;
import net.zn.ddxj.utils.ym.android.AndroidUnicast;

/**
 * 友盟推送(安卓端)
 * @author ddxj
 *
 */
public class AndroidPushUtils 
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
	public static void sendAndroidSingleMsg(Map<String,Object> param) throws Exception
	{
		AndroidUnicast unicast = new AndroidUnicast(PropertiesUtils.getPropertiesByName("u_push_android_app_key"),PropertiesUtils.getPropertiesByName("u_push_android_app_master_secret"));
		unicast.setDeviceToken(param.get("deviceToken").toString());//用户唯一标识
//		unicast.setTicker(param.get("ticker").toString());//通知栏提示文字
		unicast.setTitle(param.get("title").toString());//通知标题
		if(!CmsUtils.isNullOrEmpty(param.get("text").toString()))
		{
			unicast.setText(param.get("text").toString());//通知文字描述
		}else
		{
			unicast.setText("");//通知文字描述
		}
		String img = String.valueOf(param.get("img"));
		unicast.setImg(!CmsUtils.isNullOrEmpty(img) ? img : PropertiesUtils.getPropertiesByName("static_url")+"/Fgz_XN_PfpcoLflGWJFY475Dqwcm");//通知栏大图标的URL链接。该字段的优先级大于largeIcon。该字段要求以http或者https开头。
		unicast.goCustomAfterOpen(param.get("data").toString());
		unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);//消息类型: notification(通知)、message(消息)
		// TODO 如果是测试设备，则将“ProductMode”设置为“false”。 
		// 有关如何注册测试设备，请参阅开发人员文档。 
		unicast.setProductionMode(Boolean.valueOf(Constants.PRODUCTION_MODE));
		unicast.setTestMode();
		client.send(unicast);
	}
	
	/**
	 * 广播，发送给所有人
	* @Title: YMPushUtils.java  
	* @param @param param
	* @param @throws Exception参数  
	* @return void    返回类型 
	* @throws
	* @Package net.zn.ddxj.utils.ym  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @date 2018年5月16日  
	* @version V1.0
	 */
	public static void sendAndroidBroadcast(Map<String,Object> param) throws Exception {
		AndroidBroadcast broadcast = new AndroidBroadcast(PropertiesUtils.getPropertiesByName("u_push_android_app_key"),PropertiesUtils.getPropertiesByName("u_push_android_app_master_secret"));
		broadcast.setTicker(param.get("ticker").toString());//通知栏提示文字
		broadcast.setTitle(param.get("title").toString());//通知标题
		broadcast.setText(param.get("text").toString());//通知文字描述
		broadcast.setImg(PropertiesUtils.getPropertiesByName("static_url")+"/Fgz_XN_PfpcoLflGWJFY475Dqwcm");//通知栏大图标的URL链接。该字段的优先级大于largeIcon。该字段要求以http或者https开头。
		broadcast.goAppAfterOpen();
		broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO 如果是测试设备，则将“ProductMode”设置为“false”。 
		// 有关如何注册测试设备，请参阅开发人员文档。 
		broadcast.setProductionMode(Boolean.valueOf(Constants.PRODUCTION_MODE));
		client.send(broadcast);
	}
	
	public static int sendAndroidBatchMsg(Map<String,Object> param) throws Exception {
		AndroidListcast listcast = new AndroidListcast(PropertiesUtils.getPropertiesByName("u_push_android_app_key"),PropertiesUtils.getPropertiesByName("u_push_android_app_master_secret"));
		listcast.setDeviceToken(param.get("deviceToken").toString());
		listcast.setTicker(param.get("ticker").toString());//通知栏提示文字
		listcast.setTitle(param.get("title").toString());//通知标题
		listcast.setText(param.get("text").toString());//通知文字描述
		if(!CmsUtils.isNullOrEmpty(param.get("timingTime")))
		{
			listcast.setStartTime(param.get("timingTime").toString());
		}
		
		listcast.setImg(PropertiesUtils.getPropertiesByName("static_url")+"/Fgz_XN_PfpcoLflGWJFY475Dqwcm");//通知栏大图标的URL链接。该字段的优先级大于largeIcon。该字段要求以http或者https开头。
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("messageTypeId", "1");
		listcast.goCustomAfterOpen(JsonUtil.map2jsonToString(map));
		
		listcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO 如果是测试设备，则将“ProductMode”设置为“false”。 
		// 有关如何注册测试设备，请参阅开发人员文档。 
		listcast.setTestMode();
		listcast.setProductionMode(Boolean.valueOf(Constants.PRODUCTION_MODE));
		return client.send(listcast);
	}
}
