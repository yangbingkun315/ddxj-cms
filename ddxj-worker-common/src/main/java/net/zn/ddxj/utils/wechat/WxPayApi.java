package net.zn.ddxj.utils.wechat;



import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.PropertiesUtils;
import net.zn.ddxj.utils.json.JsonUtil;


public class WxPayApi {
	private static Logger Log = Logger.getLogger(WxPayApi.class);
	
	public static WxPayData pay(Map<String,Object> parems
			) throws NoSuchAlgorithmException, WxPayException, ParserConfigurationException, SAXException, IOException{
		WxPayData wxJsApiParam=new WxPayData();
		WxPayData unifiedOrderResult = GetUnifiedOrderResult(parems);
		wxJsApiParam = GetJsApiParameters(unifiedOrderResult);//获取H5调起JS API参数
		return wxJsApiParam;
	}
	

	/**
	 * 
	 * 通过code获取session_key和openid的返回数据
	 * @throws Exception
	 * @失败时抛异常Exception
	 */
	@SuppressWarnings("unchecked")
	public static String getOpenId(String code) throws Exception {
		//https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
		WxPayData data = new WxPayData();
		data.SetValue("appid", PropertiesUtils.getPropertiesByName("wx_app_id"));
		data.SetValue("secret", PropertiesUtils.getPropertiesByName("wx_app_secret"));
		data.SetValue("js_code", code);
		data.SetValue("grant_type", "authorization_code");
		String url = PropertiesUtils.getPropertiesByName("jscode2session_url")+ data.ToUrl();
		// 请求url以获取数据
		String result = HttpService.Get(url);
		Log.debug("request:"+data.ToUrl()+"  GetOpenidAndAccessTokenFromCode response : " + result);

		// 保存access_token，用于收货地址获取
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> jd = mapper.readValue(result, Map.class);

		String session_key = jd.get("session_key");

		// 获取用户openid
		String openid = jd.get("openid");

		Log.debug("Get openid : " + openid+" Get session_key : " + session_key);
		return openid;
	}
	
	/**
	 * 
	 * 从统一下单成功返回的数据中获取微信浏览器调起jsapi支付所需的参数， 微信浏览器调起JSAPI时的输入参数格式如下： { "appId" :
	 * "wx2421b1c4370ec43b", //公众号名称，由商户传入 "timeStamp":" 1395712654",
	 * //时间戳，自1970年以来的秒数 "nonceStr" : "e61463f8efa94090b1f366cccfbbb444", //随机串
	 * "package" : "prepay_id=u802345jgfjsdfgsdg888", "signType" : "MD5",
	 * //微信签名方式: "paySign" : "70EA570631E4BB79628FBCA90534C63FF7FADD89" //微信签名 }
	 * 
	 * @return string 微信浏览器调起JSAPI时的输入参数，json格式可以直接做参数用
	 *         更详细的说明请参考网页端调起支付API：http:
	 *         //pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_7
	 * @throws WxPayException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * 
	 */
	public static WxPayData GetJsApiParameters(WxPayData unifiedOrderResult)
			throws NoSuchAlgorithmException, WxPayException,
			JsonGenerationException, JsonMappingException, IOException {
		Log.debug("JsApiPay::GetJsApiParam is processing...");
		WxPayData jsApiParam = new WxPayData();
		jsApiParam.SetValue("appId", unifiedOrderResult.GetValue("appid"));
		jsApiParam.SetValue("timeStamp", GenerateTimeStamp());
		jsApiParam.SetValue("nonceStr", GenerateNonceStr());
		jsApiParam.SetValue("package", "prepay_id=" + unifiedOrderResult.GetValue("prepay_id"));
		jsApiParam.SetValue("signType", "MD5");
		jsApiParam.SetValue("paySign", jsApiParam.MakeSign("MD5"));
		jsApiParam.SetValue("out_trade_no", unifiedOrderResult.GetValue("out_trade_no"));
		jsApiParam.SetValue("total_fee", unifiedOrderResult.GetValue("total_fee"));
		jsApiParam.SetValue("result_code", unifiedOrderResult.GetValue("result_code"));
		
		Log.debug("Get jsApiParam : " + JsonUtil.map2jsonToString(jsApiParam.GetValues()));
		return jsApiParam;
	}
	
	/**
	 * 调用统一下单，获得下单结果
	 * 
	 * @return 统一下单结果
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws WxPayException 
	 * @throws NoSuchAlgorithmException 
	 * @throws Exception 
	 * @失败时抛异常WxPayException
	 */
	public static WxPayData GetUnifiedOrderResult(Map<String,Object> parem) throws NoSuchAlgorithmException, WxPayException, ParserConfigurationException, SAXException, IOException
			{
		WxPayData data = new WxPayData();
		data.SetValue("body", parem.get("body"));
//		data.SetValue("attach", parem.get("attach"));
		data.SetValue("out_trade_no", parem.get("out_trade_no"));
		data.SetValue("total_fee", Integer.parseInt(String.valueOf(parem.get("total_fee"))));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar c = Calendar.getInstance();
		data.SetValue("time_start", dateFormat.format(c.getTime()));
		c.add(Calendar.DATE, 2);
		data.SetValue("time_expire", dateFormat.format(c.getTime()));
		data.SetValue("product_id", String.valueOf(new Date().getTime()));
		data.SetValue("trade_type", "JSAPI");//交易类型 JSAPI 公众号支付 NATIVE 扫码支付 APP APP支付 H5支付的交易类型为MWEB
		data.SetValue("openid", parem.get("openId"));//只有在公众号支付时才是必须传
		data.SetValue("notify_url", PropertiesUtils.getPropertiesByName("wechat_notify_url"));
		data.SetValue("limit_pay", "no_credit");//限制信用卡

		WxPayData result = UnifiedOrder(data, 0);
		if (!result.IsSet("appid") || !result.IsSet("prepay_id")
				|| result.GetValue("prepay_id").toString().equalsIgnoreCase("")) {
			Log.error("UnifiedOrder response error!");
			throw new WxPayException("UnifiedOrder response error!");
		}
		Log.info("##########统一下单回调参数："+JsonUtil.map2jsonToString(result.GetValues()));
		return result;
	}
	/**
	 * 
	 * 统一下单
	 * 
	 * @param WxPaydata
	 *            inputObj 提交给统一下单API的参数
	 * @param int timeOut 超时时间
	 * @throws WxPayException
	 * @return 成功时返回，其他抛异常
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static WxPayData UnifiedOrder(WxPayData inputObj, int timeOut)
			throws WxPayException, NoSuchAlgorithmException,
			ParserConfigurationException, SAXException, IOException {
		timeOut = (timeOut == 0 ? 6 : timeOut);
		String url = PropertiesUtils.getPropertiesByName("unifiedorder_url");
		// 检测必填参数
		if (!inputObj.IsSet("out_trade_no")) {
			throw new WxPayException("缺少统一支付接口必填参数out_trade_no！");
		} else if (!inputObj.IsSet("body")) {
			throw new WxPayException("缺少统一支付接口必填参数body！");
		} else if (!inputObj.IsSet("total_fee")) {
			throw new WxPayException("缺少统一支付接口必填参数total_fee！");
		} else if (!inputObj.IsSet("trade_type")) {
			throw new WxPayException("缺少统一支付接口必填参数trade_type！");
		}
		// 关联参数
		if (inputObj.GetValue("trade_type").toString()
				.equalsIgnoreCase("JSAPI")
				&& !inputObj.IsSet("openid")) {
			throw new WxPayException(
					"统一支付接口中，缺少必填参数openid！trade_type为JSAPI时，openid为必填参数！");
		}
		if (inputObj.GetValue("trade_type").toString()
				.equalsIgnoreCase("NATIVE")
				&& !inputObj.IsSet("product_id")) {
			throw new WxPayException(
					"统一支付接口中，缺少必填参数product_id！trade_type为JSAPI时，product_id为必填参数！");
		}

		// 异步通知url未设置，则使用配置文件中的url
		if (!inputObj.IsSet("notify_url")) {
			inputObj.SetValue("notify_url", PropertiesUtils.getPropertiesByName("wechat_notify_url"));// 异步通知url
		}

		inputObj.SetValue("appid", PropertiesUtils.getPropertiesByName("wx_app_id"));// 公众账号ID
		inputObj.SetValue("mch_id", PropertiesUtils.getPropertiesByName("wechat_mach_id"));// 微信公众号支付商户号
		inputObj.SetValue("spbill_create_ip", WxPayConfig.IP);// 终端ip
		inputObj.SetValue("nonce_str", GenerateNonceStr());// 随机字符串

		// 签名
		inputObj.SetValue("sign", inputObj.MakeSign("MD5"));
		String xml = inputObj.ToXml();

		long start = System.currentTimeMillis();
		Log.info("#########微信统一下单请求参数："+inputObj.GetValues());
		String response = HttpService.Post(xml, url, false, timeOut);

		long end = System.currentTimeMillis();
		int timeCost = (int) ((end - start));

		WxPayData result = new WxPayData();
		JsonUtil.map2jsonToString(result.FromXml(response));
		result.SetValue("out_trade_no", inputObj.GetValue("out_trade_no"));
		result.SetValue("total_fee", inputObj.GetValue("total_fee"));
		Log.info("#########微信统一下单整体耗时："+timeCost+"ms #######");
		return result;
	}

	

	/**
	 * 生成时间戳，标准北京时间，时区为东八区，自1970年1月1日 0点0分0秒以来的秒数
	 * 
	 * @return 时间戳
	 */
	public static String GenerateTimeStamp() {
		;
		return System.currentTimeMillis() + "";
	}

	/**
	 * 生成随机串，随机串包含字母或数字
	 * 
	 * @return 随机串
	 */
	public static String GenerateNonceStr() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	// 查询订单
	public static boolean QueryOrder(String transaction_id) throws NoSuchAlgorithmException, WxPayException,
			ParserConfigurationException, SAXException, IOException {
		WxPayData req = new WxPayData();
		req.SetValue("transaction_id", transaction_id);
		WxPayData res = OrderQuery(req, 0);
		if (res.GetValue("return_code").toString().equalsIgnoreCase("SUCCESS") && 
			res.GetValue("result_code").toString().equalsIgnoreCase("SUCCESS")) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 
	 * 查询订单
	 * 
	 * @param WxPayData
	 *            inputObj 提交给查询订单API的参数
	 * @param int timeOut 超时时间
	 * @throws WxPayException
	 * @return 成功时返回订单查询结果，其他抛异常
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static WxPayData OrderQuery(WxPayData inputObj, int timeOut)
			throws WxPayException, NoSuchAlgorithmException,
			ParserConfigurationException, SAXException, IOException {
		timeOut = (timeOut == 0 ? 6 : timeOut);
		String url = PropertiesUtils.getPropertiesByName("orderquery_url");
		// 检测必填参数
		if (!inputObj.IsSet("out_trade_no")
				&& !inputObj.IsSet("transaction_id")) {
			throw new WxPayException(
					"订单查询接口中，out_trade_no、transaction_id至少填一个！");
		}

		inputObj.SetValue("appid", PropertiesUtils.getPropertiesByName("wx_app_id"));// 公众账号ID
		inputObj.SetValue("mch_id", PropertiesUtils.getPropertiesByName("wechat_mach_id"));// 商户号
		inputObj.SetValue("nonce_str", WxPayApi.GenerateNonceStr());// 随机字符串
		inputObj.SetValue("sign", inputObj.MakeSign("MD5"));// 签名

		String xml = inputObj.ToXml();

		long start = System.currentTimeMillis();

		Log.debug("OrderQuery request : " + xml);
		String response = HttpService.Post(xml, url, false, timeOut);// 调用HTTP通信接口提交数据
		Log.debug("OrderQuery response : " + response);

		long end = System.currentTimeMillis();
		int timeCost = (int) ((end - start));// 获得接口耗时

		// 将xml格式的数据转化为对象以返回
		WxPayData result = new WxPayData();
		result.FromXml(response);

		return result;
	}
}
