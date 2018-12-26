package net.zn.ddxj.utils.wechat;



import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.zn.ddxj.utils.PropertiesUtils;
import net.zn.ddxj.utils.json.JsonUtil;


public class AppWxPayApi {
	private static Logger Log = Logger.getLogger(AppWxPayApi.class);
	
	public static AppWxPayData pay(Map<String,Object> parems
			) throws NoSuchAlgorithmException, WxPayException, ParserConfigurationException, SAXException, IOException{
		AppWxPayData wxJsApiParam=new AppWxPayData();
		AppWxPayData unifiedOrderResult = GetUnifiedOrderResult(parems);
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
		AppWxPayData data = new AppWxPayData();
		data.SetValue("appid", PropertiesUtils.getPropertiesByName("kf_app_id"));
		data.SetValue("secret", PropertiesUtils.getPropertiesByName("kf_app_secret"));
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
	public static AppWxPayData GetJsApiParameters(AppWxPayData unifiedOrderResult)
			throws NoSuchAlgorithmException, WxPayException,
			JsonGenerationException, JsonMappingException, IOException {
		Log.debug("JsApiPay::GetJsApiParam is processing...");
		AppWxPayData jsApiParam = new AppWxPayData();
		jsApiParam.SetValue("appid", unifiedOrderResult.GetValue("appid"));
		jsApiParam.SetValue("noncestr", AppWxPayApi.getRandomString(32));
		jsApiParam.SetValue("package", "Sign=WXPay");
		jsApiParam.SetValue("partnerid", unifiedOrderResult.GetValue("mch_id"));
		jsApiParam.SetValue("prepayid", unifiedOrderResult.GetValue("prepay_id"));
		jsApiParam.SetValue("timestamp", System.currentTimeMillis() / 1000);
		jsApiParam.SetValue("sign", jsApiParam.MakeSign("MD5"));
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
	public static AppWxPayData GetUnifiedOrderResult(Map<String,Object> parem) throws NoSuchAlgorithmException, WxPayException, ParserConfigurationException, SAXException, IOException
			{
		AppWxPayData data = new AppWxPayData();
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
		data.SetValue("trade_type", "APP");//交易类型 JSAPI 公众号支付 NATIVE 扫码支付 APP APP支付 H5支付的交易类型为MWEB
		data.SetValue("notify_url", PropertiesUtils.getPropertiesByName("app_notify_url"));
		data.SetValue("limit_pay", "no_credit");//限制信用卡

		AppWxPayData result = UnifiedOrder(data, 0);
		result.SetValue("total_fee", String.valueOf(parem.get("total_fee")));
		result.SetValue("out_trade_no", parem.get("out_trade_no"));
		if (!result.IsSet("appid") || !result.IsSet("prepay_id")
				|| result.GetValue("prepay_id").toString().equalsIgnoreCase("")) {
			Log.error("UnifiedOrder response error!");
			throw new WxPayException("UnifiedOrder response error!");
		}
		Log.info("#########统一下单回调参数："+JsonUtil.map2jsonToString(result.GetValues()));
		return result;
	}
	/**
	 * 
	 * 统一下单
	 * 
	 * @param AppWxPayData
	 *            inputObj 提交给统一下单API的参数
	 * @param int timeOut 超时时间
	 * @throws WxPayException
	 * @return 成功时返回，其他抛异常
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static AppWxPayData UnifiedOrder(AppWxPayData inputObj, int timeOut)
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
			inputObj.SetValue("notify_url", PropertiesUtils.getPropertiesByName("app_notify_url"));// 异步通知url
		}

		inputObj.SetValue("appid", PropertiesUtils.getPropertiesByName("kf_app_id"));// 公众账号ID
		inputObj.SetValue("mch_id", PropertiesUtils.getPropertiesByName("app_mach_id"));// APP支付商户号
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

		AppWxPayData result = new AppWxPayData();
		JsonUtil.map2jsonToString(result.FromXml(response));
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
	//随机字符串生成  
    public static String getRandomString(int length) { //length表示生成字符串的长度      
           String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";         
           Random random = new Random();         
           StringBuffer sb = new StringBuffer();         
           for (int i = 0; i < length; i++) {         
               int number = random.nextInt(base.length());         
               sb.append(base.charAt(number));         
           }         
           return sb.toString();         
        }  
	// 查询订单
	public static boolean QueryOrder(String transaction_id) throws NoSuchAlgorithmException, WxPayException,
			ParserConfigurationException, SAXException, IOException {
		AppWxPayData req = new AppWxPayData();
		req.SetValue("transaction_id", transaction_id);
		AppWxPayData res = OrderQuery(req, 0);
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
	 * @param AppWxPayData
	 *            inputObj 提交给查询订单API的参数
	 * @param int timeOut 超时时间
	 * @throws WxPayException
	 * @return 成功时返回订单查询结果，其他抛异常
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static AppWxPayData OrderQuery(AppWxPayData inputObj, int timeOut)
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

		inputObj.SetValue("appid", PropertiesUtils.getPropertiesByName("kf_app_id"));// 公众账号ID
		inputObj.SetValue("mch_id", PropertiesUtils.getPropertiesByName("app_mach_id"));// 商户号
		inputObj.SetValue("nonce_str", AppWxPayApi.getRandomString(32));// 随机字符串
		inputObj.SetValue("sign", inputObj.MakeSign("MD5"));// 签名

		String xml = inputObj.ToXml();

		long start = System.currentTimeMillis();

		Log.debug("OrderQuery request : " + xml);
		String response = HttpService.Post(xml, url, false, timeOut);// 调用HTTP通信接口提交数据
		Log.debug("OrderQuery response : " + response);

		long end = System.currentTimeMillis();
		int timeCost = (int) ((end - start));// 获得接口耗时

		// 将xml格式的数据转化为对象以返回
		AppWxPayData result = new AppWxPayData();
		result.FromXml(response);

		return result;
	}
}
