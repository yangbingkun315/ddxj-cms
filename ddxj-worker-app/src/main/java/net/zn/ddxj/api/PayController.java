package net.zn.ddxj.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zn.ddxj.base.BaseController;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.PaymentRecord;
import net.zn.ddxj.service.PayService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.alipay.AlipayConfig;
import net.zn.ddxj.utils.alipay.AlipayUtils;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.utils.unionpay.AcpService;
import net.zn.ddxj.utils.unionpay.SDKConfig;
import net.zn.ddxj.utils.unionpay.SDKConstants;
import net.zn.ddxj.utils.unionpay.UnionpayBase;
import net.zn.ddxj.utils.wechat.AppWxPayApi;
import net.zn.ddxj.utils.wechat.AppWxPayData;
import net.zn.ddxj.utils.wechat.GetWechatBank;
import net.zn.ddxj.utils.wechat.WxPayApi;
import net.zn.ddxj.utils.wechat.WxPayData;
import net.zn.ddxj.utils.wechat.WxPayException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

/**
 * 支付
 * @author ddxj
 *
 */
@RestController
@Slf4j
public class PayController extends BaseController {
	@Autowired
	private PayService payService;
	 /**
     * 微信支付
     * @param request
     * @param response
     * @param userId
     * @param toPhone
     * @param transferMoney
     * @param payType
     * @return
	 * @throws Exception 
     */
    @RequestMapping(value="/add/recharge/info.ddxj")	
    public ResponseBase addRechargeInfo(HttpServletRequest request, HttpServletResponse response,Integer userId,String totalValue,String openId,Integer payType,String payWay) throws Exception{
    	ResponseBase result=ResponseBase.getInitResponse();
    	if(CmsUtils.isNullOrEmpty(userId) || userId == Constants.Number.ZERO_INT)
    	{
    		result.setResponse(Constants.FALSE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg(Constants.Prompt.USER_ID_NULL);
        	return result;
    	}
    	if(CmsUtils.isNullOrEmpty(totalValue))
    	{
    		result.setResponse(Constants.FALSE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg(Constants.Prompt.AMOUNT_ERROR);
        	return result;
    	}
    	BigDecimal money = new BigDecimal(totalValue);
    	BigDecimal maxMoney = new BigDecimal(500000);
    	if(money.compareTo(maxMoney) == 1)
    	{
    		result.setResponse(Constants.FALSE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg(Constants.Prompt.MAX_AMOUNT_ERROR);
        	return result;
    	}
    	if(CmsUtils.isNullOrEmpty(payWay))
    	{
    		result.setResponse(Constants.FALSE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.PAY_STATUS_NULL);
    		return result;
    	}
    	Map<String,Object> parem = new HashMap<String,Object>();
    	if(!CmsUtils.isNullOrEmpty(payType) && payType == Constants.Number.ONE_INT)//微信支付
    	{
    		parem.put("attach", "");//附加数据
     		if(!CmsUtils.isNullOrEmpty(payWay) && "JSAPI".equals(payWay))//公众号支付
     		{
     			if(CmsUtils.isNullOrEmpty(openId))
     			{
     				result.setResponse(Constants.FALSE);
     	        	result.setResponseCode(Constants.SUCCESS_200);
     	        	result.setResponseMsg("openId不能为空");
     	        	return result;
     			}
     			parem.put("openId", openId);//用户ID
     		}
    		parem.put("out_trade_no", UUID.randomUUID().toString().replaceAll("-", ""));//商户订单号
    		parem.put("body", "点点小匠充值"+new BigDecimal(totalValue).setScale(Constants.Number.TWO_INT, RoundingMode.HALF_UP)+"元");//订单优惠标记   商品描述
    		BigDecimal totalFee = new BigDecimal(totalValue).setScale(Constants.Number.TWO_INT, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
    		parem.put("total_fee", totalFee.intValue());//支付金额
    		if(!CmsUtils.isNullOrEmpty(payWay) && "JSAPI".equals(payWay))//公众号支付
     		{
    			WxPayData wxPayData=WxPayApi.pay(parem);
    			if(!CmsUtils.isNullOrEmpty(wxPayData))
    			{
    				if(!CmsUtils.isNullOrEmpty(wxPayData.GetValue("result_code")) && "SUCCESS".equals(wxPayData.GetValue("result_code")))//说明统一下单成功
    				{
    					PaymentRecord paymentRecord = new PaymentRecord();
    					paymentRecord.setOutTradeNo(String.valueOf(wxPayData.GetValue("out_trade_no")));//商户订单号
    					paymentRecord.setOpenid(openId);//openid
    					paymentRecord.setUserId(userId);
    					paymentRecord.setTradeType("JSAPI");
    					BigDecimal newTotalFee = new BigDecimal(totalValue).divide(new BigDecimal("100"));
    					paymentRecord.setTotalFee(newTotalFee);//支付金额
    					paymentRecord.setCreateTime(new Date());//创建时间
    					paymentRecord.setUpdateTime(new Date());
    					payService.addPayMentRecord(paymentRecord);
    				}
    			}
    			result.push("pay", JsonUtil.map2jsonToObject(wxPayData.GetValues()));
    			result.setResponse(Constants.TRUE);
    	    	result.setResponseCode(Constants.SUCCESS_200);
    	    	result.setResponseMsg("统一下单成功");
     		}
    		else if(!CmsUtils.isNullOrEmpty(payWay) && "APP".equals(payWay))//微信支付
    		{
    			AppWxPayData appwxPayData=AppWxPayApi.pay(parem);
    			if(!CmsUtils.isNullOrEmpty(appwxPayData))
    			{
    				if(!CmsUtils.isNullOrEmpty(appwxPayData.GetValue("result_code")) && "SUCCESS".equals(appwxPayData.GetValue("result_code")))//说明统一下单成功
    				{
    					PaymentRecord paymentRecord = new PaymentRecord();
    					paymentRecord.setOutTradeNo(String.valueOf(appwxPayData.GetValue("out_trade_no")));//商户订单号
    					paymentRecord.setTradeType("WECHAT");
    					paymentRecord.setUserId(userId);
    					BigDecimal newTotalFee = new BigDecimal(String.valueOf(appwxPayData.GetValue("total_fee"))).divide(new BigDecimal("100"));
    					paymentRecord.setTotalFee(newTotalFee);//支付金额
    					paymentRecord.setCreateTime(new Date());//创建时间
    					paymentRecord.setUpdateTime(new Date());
    					payService.addPayMentRecord(paymentRecord);
    				}
    			}
    			result.push("pay", JsonUtil.map2jsonToObject(appwxPayData.GetValues()));
    			result.setResponse(Constants.TRUE);
    	    	result.setResponseCode(Constants.SUCCESS_200);
    	    	result.setResponseMsg("统一下单成功");
    		}
    	}
    	else if(!CmsUtils.isNullOrEmpty(payType) && payType == Constants.Number.TWO_INT)//支付宝支付
    	{
    		parem.put("out_trade_no", UUID.randomUUID().toString().replaceAll("-", ""));//商户订单号
    		parem.put("total_amount", totalValue);//支付金额
    		parem.put("body", "点点小匠充值"+new BigDecimal(totalValue).setScale(Constants.Number.TWO_INT, RoundingMode.HALF_UP)+"元");//商品信息
    		parem.put("subject", "点点小匠充值");//商品名称
    		parem.put("disable_pay_channels", "credit_group");//禁用充值类型
    		
    		String orderString = AlipayUtils.aliPrePay(parem);//统一下单
    		if(!CmsUtils.isNullOrEmpty(orderString))
    		{
    			PaymentRecord paymentRecord = new PaymentRecord();
    			paymentRecord.setOutTradeNo(String.valueOf(parem.get("out_trade_no").toString()));//商户订单号
    			paymentRecord.setTradeType("ALIPAY");
    			paymentRecord.setUserId(userId);
    			paymentRecord.setTotalFee(new BigDecimal(parem.get("total_amount").toString()));//支付金额
    			paymentRecord.setCreateTime(new Date());//创建时间
    			paymentRecord.setUpdateTime(new Date());
    			payService.addPayMentRecord(paymentRecord);
    		}
        	result.push("orderString", orderString);
        	result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg("统一下单成功");
    	}
    	return result;
    }
    /**
     * 微信回调页面，支付完成回调
     * @param request
     * @param response
     * @throws Exception
     */
	@RequestMapping(value = "/wechat/notifyUrl.ddxj")
	public String wechatNotifyUrl(HttpServletRequest request,HttpServletResponse response) throws Exception{
		// 接收从微信后台POST过来的数据
		String requestJson = CmsUtils.readRequestStream(request);
		// 转换数据格式并验证签名
		WxPayData notifyData = new WxPayData();
		WxPayData res = new WxPayData();
		try {
			notifyData.FromXml(requestJson);
			log.info("支付成功参数回调" + notifyData.GetValues());
			if (!notifyData.IsSet("transaction_id")) {
				// 若transaction_id不存在，则立即返回结果给微信支付后台
				res.SetValue("return_code", "FAIL");
				res.SetValue("return_msg", "支付结果中微信订单号不存在");
				log.error("The Pay result is error : " + res.ToXml());
			} else {
				String transactionId = notifyData.GetValue("transaction_id").toString();
				// 查询订单，判断订单真实性
				if (!WxPayApi.QueryOrder(transactionId)) {					
					// 若订单查询失败，则立即返回结果给微信支付后台
					res.SetValue("return_code", "FAIL");
					res.SetValue("return_msg", "订单查询失败");
					log.error("Order query failure : " + res.ToXml());
				}
				// 查询订单成功
				else {
					String outTradeNo=notifyData.GetValue("out_trade_no").toString();
					if(!CmsUtils.isNullOrEmpty(outTradeNo)){
						res.SetValue("return_code", "SUCCESS");
						res.SetValue("return_msg", "OK");
						log.info("支付成功");
						log.info("out_trade_no:"+outTradeNo);
						PaymentRecord payOutTradeNo = payService.selectPayOutTradeNo(outTradeNo);
						payOutTradeNo.setTransactionId(String.valueOf(notifyData.GetValue("transaction_id")));//订单号
						payOutTradeNo.setBankType(GetWechatBank.bankCodeByBankName(String.valueOf(notifyData.GetValue("bank_type"))));//银行类型
						payOutTradeNo.setTimeEnd(String.valueOf(notifyData.GetValue("time_end")));//支付完成时间
						payOutTradeNo.setOpenid(String.valueOf(notifyData.GetValue("openid")));//支付人openid
						payOutTradeNo.setPayStatus(2);
						BigDecimal totalFee = new BigDecimal(String.valueOf(notifyData.GetValue("total_fee"))).divide(new BigDecimal("100"));
						payOutTradeNo.setTotalFee(totalFee);//支付金额
						payOutTradeNo.setUpdateTime(new Date());
						payService.updatePayMentRecord(payOutTradeNo);
						res.SetValue("transactionId", payOutTradeNo.getTransactionId());
					}else{
						res.SetValue("return_code", "FAIL");
						res.SetValue("return_msg", "更新状态失败");
						log.error("Order query failure : " + res.ToXml());
					}
				}
			}
			
			
		} catch (WxPayException ex) {
			// 若签名错误，则立即返回结果给微信支付后台
			res.SetValue("return_code", "FAIL");
			res.SetValue("return_msg", ex.getMessage());
			log.error("Sign check error : " + res.ToXml());
		}
		log.error("支付成功，给微信支付返回 ：" + res.ToXml());
		return res.ToXml();
	}
	/**
     * 微信回调页面，支付完成回调
     * @param request
     * @param response
     * @throws Exception
     */
	@RequestMapping(value = "/app/notifyUrl.ddxj")
	public String appNotifyUrl(HttpServletRequest request,HttpServletResponse response) throws Exception{
		// 接收从微信后台POST过来的数据
		String requestJson = CmsUtils.readRequestStream(request);
		// 转换数据格式并验证签名
		AppWxPayData notifyData = new AppWxPayData();
		WxPayData res = new WxPayData();
		try {
			notifyData.FromXml(requestJson);
			log.info("支付成功参数回调" + notifyData.GetValues());
			if (!notifyData.IsSet("transaction_id")) {
				// 若transaction_id不存在，则立即返回结果给微信支付后台
				res.SetValue("return_code", "FAIL");
				res.SetValue("return_msg", "支付结果中微信订单号不存在");
				log.error("The Pay result is error : " + res.ToXml());
			} else {
				String transactionId = notifyData.GetValue("transaction_id").toString();
				// 查询订单，判断订单真实性
				if (!AppWxPayApi.QueryOrder(transactionId)) {					
					// 若订单查询失败，则立即返回结果给微信支付后台
					res.SetValue("return_code", "FAIL");
					res.SetValue("return_msg", "订单查询失败");
					log.error("Order query failure : " + res.ToXml());
				}
				// 查询订单成功
				else {
					String outTradeNo=notifyData.GetValue("out_trade_no").toString();
					if(outTradeNo != null){
						res.SetValue("return_code", "SUCCESS");
						res.SetValue("return_msg", "OK");
						log.info("支付成功");
						PaymentRecord payOutTradeNo = payService.selectPayOutTradeNo(outTradeNo);
						payOutTradeNo.setTransactionId(String.valueOf(notifyData.GetValue("transaction_id")));//订单号
						payOutTradeNo.setBankType(GetWechatBank.bankCodeByBankName(String.valueOf(notifyData.GetValue("bank_type"))));//银行类型
						payOutTradeNo.setPayStatus(2);
						payOutTradeNo.setTimeEnd(String.valueOf(notifyData.GetValue("time_end")));//支付完成时间
						payOutTradeNo.setOpenid(String.valueOf(notifyData.GetValue("openid")));//支付人openid
						BigDecimal totalFee = new BigDecimal(String.valueOf(notifyData.GetValue("total_fee"))).divide(new BigDecimal("100"));
						payOutTradeNo.setTotalFee(totalFee);//支付金额
						payOutTradeNo.setUpdateTime(new Date());
						payService.updatePayMentRecord(payOutTradeNo);
						res.SetValue("transactionId", payOutTradeNo.getTransactionId());
					}else{
						res.SetValue("return_code", "FAIL");
						res.SetValue("return_msg", "更新状态失败");
						log.error("Order query failure : " + res.ToXml());
					}
				}
			}
			
			
		} catch (WxPayException ex) {
			// 若签名错误，则立即返回结果给微信支付后台
			res.SetValue("return_code", "FAIL");
			res.SetValue("return_msg", ex.getMessage());
			log.error("Sign check error : " + res.ToXml());
		}
		log.error("支付成功，给微信支付返回 ：" + res.ToXml());
		return res.ToXml();
	}
	/**
	 * 重要：联调测试时请仔细阅读注释！
	 * 
	 * 产品：跳转网关支付产品<br>
	 * 交易：消费：前台跳转，有前台通知应答和后台通知应答<br>
	 * 日期： 2015-09<br>

	 * 版权： 中国银联<br>
	 * 声明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障<br>
	 * 提示：该接口参考文档位置：open.unionpay.com帮助中心 下载  产品接口规范  《网关支付产品接口规范》，<br>
	 *              《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表)<br>
	 *              《全渠道平台接入接口规范 第3部分 文件接口》（对账文件格式说明）<br>
	 * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案：
	 * 							        调试过程中的问题或其他问题请在 https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
	 *                             测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/respCodeList 输入应答码搜索解决方案
	 *                          2） 咨询在线人工支持： open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
	 * 交易说明:1）以后台通知或交易状态查询交易确定交易成功,前台通知不能作为判断成功的标准.
	 *       2）交易状态查询交易（Form_6_5_Query）建议调用机制：前台类交易建议间隔（5分、10分、30分、60分、120分）发起交易查询，如果查询到结果成功，则不用再查询。（失败，处理中，查询不到订单均可能为中间状态）。也可以建议商户使用payTimeout（支付超时时间），过了这个时间点查询，得到的结果为最终结果。
	 * @throws IOException 
	 */
	@RequestMapping(value = "/unionpay/pay.ddxj")
	public void unionpayPay(HttpServletRequest request, HttpServletResponse response,String totalMoney) throws IOException {
		SDKConfig.getConfig().loadPropertiesFromSrc();
		response.setContentType("text/html; charset="+ UnionpayBase.encoding);
		Map<String, String> requestData = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		requestData.put("version", UnionpayBase.version);   			  //版本号，全渠道默认值
		requestData.put("encoding", UnionpayBase.encoding); 			  //字符集编码，可以使用UTF-8,GBK两种方式
		requestData.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
		requestData.put("txnType", "01");               			  //交易类型 ，01：消费
		requestData.put("txnSubType", "01");            			  //交易子类型， 01：自助消费
		requestData.put("bizType", "000201");           			  //业务类型，B2C网关支付，手机wap支付
		requestData.put("channelType", "07");           			  //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机
		/***商户接入参数***/
		requestData.put("merId", SDKConfig.getConfig().getMerId());    	          			  //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
		requestData.put("accessType", "0");             			  //接入类型，0：直连商户 
		requestData.put("orderId",DateUtils.getStringDate(new Date(), "yyyyMMddHHmmss")+CmsUtils.generateNumber(4));             //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则		
		requestData.put("txnTime", DateUtils.getStringDate(new Date(), "yyyyMMddHHmmss"));        //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		requestData.put("currencyCode", "156");         			  //交易币种（境内商户一般是156 人民币）		
		requestData.put("txnAmt", String.valueOf(new BigDecimal(totalMoney).multiply(new BigDecimal("100"))));             			      //交易金额，单位分，不要带小数点
		//requestData.put("reqReserved", "透传字段");        		      //请求方保留域，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节。出现&={}[]符号时可能导致查询接口应答报文解析失败，建议尽量只传字母数字并使用|分割，或者可以最外层做一次base64编码(base64编码之后出现的等号不会导致解析失败可以不用管)。		
		
		//前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
		//如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
		//异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		requestData.put("frontUrl", UnionpayBase.frontUrl);
		
		//后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
		//后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		//注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码 
		//    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
		//    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		requestData.put("backUrl", UnionpayBase.backUrl);

		// 订单超时时间。
		// 超过此时间后，除网银交易外，其他交易银联系统会拒绝受理，提示超时。 跳转银行网银交易如果超时后交易成功，会自动退款，大约5个工作日金额返还到持卡人账户。
		// 此时间建议取支付时的北京时间加15分钟。
		// 超过超时时间调查询接口应答origRespCode不是A6或者00的就可以判断为失败。
		requestData.put("payTimeout", new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis() + 15 * 60 * 1000));
		
		//////////////////////////////////////////////////
		//
		//       报文中特殊用法请查看 PCwap网关跳转支付特殊用法.txt
		//
		//////////////////////////////////////////////////
		
		/**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
		Map<String, String> submitFromData = AcpService.sign(requestData,UnionpayBase.encoding);  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		
		String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();  //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
		String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData,UnionpayBase.encoding);   //生成自动跳转的Html表单
		
		log.info("打印请求HTML，此为请求报文，为联调排查问题的依据："+html);
		//将生成的html写到浏览器中完成自动跳转打开银联支付页面；这里调用signData之后，将html写到浏览器跳转到银联页面之前均不能对html中的表单项的名称和值进行修改，如果修改会导致验签不通过
		response.getWriter().write(html);
	}
	/**
	 * 重要：联调测试时请仔细阅读注释！
	 * 
	 * 产品：跳转网关支付产品<br>
	 * 功能：前台通知接收处理示例 <br>
	 * 日期： 2015-09<br>
	 
	 * 版权： 中国银联<br>
	 * 声明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障<br>
	 * 该接口参考文档位置：open.unionpay.com帮助中心 下载  产品接口规范  《网关支付产品接口规范》，<br>
	 *              《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表），
	 * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案：
	 * 							        调试过程中的问题或其他问题请在 https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
	 *                             测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/respCodeList 输入应答码搜索解决方案
	 *                          2） 咨询在线人工支持： open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
	 * 交易说明：	支付成功点击“返回商户”按钮的时候出现的处理页面示例
	 * 			为保证安全，涉及资金类的交易，收到通知后请再发起查询接口确认交易成功。不涉及资金的交易可以以通知接口respCode=00判断成功。
	 * 			未收到通知时，查询接口调用时间点请参照此FAQ：https://open.unionpay.com/ajweb/help/faq/list?id=77&level=0&from=0
	 */
	@RequestMapping(value = "/front/rcv/response.ddxj")
	public ResponseBase unionpayPay(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ResponseBase result = ResponseBase.getInitResponse();
		log.info("##########银联支付前端回调###########");
		String encoding = request.getParameter(SDKConstants.param_encoding);
		Map<String, String> respParam = getAllRequestParam(request);
		// 打印请求报文
		log.info(JsonUtil.map2jsonToString(respParam));

		Map<String, String> valideData = new HashMap<String,String>();
		if (!CmsUtils.isNullOrEmpty(respParam) && respParam.size() > 0) {
			Iterator<Entry<String, String>> it = respParam.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> e = it.next();
				String key = (String) e.getKey();
				String value = (String) e.getValue();
				valideData.put(key, new String(value.getBytes(encoding), encoding));
			}
		}
		if (!AcpService.validate(valideData, encoding)) {
			valideData.put("signStatusMsg", "验证签名结果[失败].");
			valideData.put("signStatusStatus", "error");
			log.info("验证签名结果[失败]");
		} else {
			valideData.put("signStatusMsg", "验证签名结果[成功].");
			valideData.put("signStatusStatus", "error");
			log.info("验证签名结果[成功].");
			log.info(valideData.get("orderId")); //其他字段也可用类似方式获取
			//String respCode = valideData.get("respCode");
			//判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
		}
		result.setData(valideData);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.SUCCESS);
		return result;
	}
	/**
	 * 重要：联调测试时请仔细阅读注释！
	 * 
	 * 产品：跳转网关支付产品<br>
	 * 功能：后台通知接收处理示例 <br>
	 * 日期： 2015-09<br>
	 
	 * 版权： 中国银联<br>
	 * 声明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障<br>
	 * 该接口参考文档位置：open.unionpay.com帮助中心 下载  产品接口规范  《网关支付产品接口规范》，<br>
	 *              《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表），
	 * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案：
	 * 							        调试过程中的问题或其他问题请在 https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
	 *                             测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/respCodeList 输入应答码搜索解决方案
	 *                           2） 咨询在线人工支持： open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
	 * 交易说明：	前台类交易成功才会发送后台通知。后台类交易（有后台通知的接口）交易结束之后成功失败都会发通知。
	 *			为保证安全，涉及资金类的交易，收到通知后请再发起查询接口确认交易成功。不涉及资金的交易可以以通知接口respCode=00判断成功。
	 * 			未收到通知时，查询接口调用时间点请参照此FAQ：https://open.unionpay.com/ajweb/help/faq/list?id=77&level=0&from=0
	 */
	@RequestMapping(value = "/back/rcv/response.ddxj")
	public ResponseBase backRcvResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("##########银联支付后端回调###########");
		ResponseBase result = ResponseBase.getInitResponse();
		String encoding = request.getParameter(SDKConstants.param_encoding);
		// 获取银联通知服务器发送的后台通知参数
		Map<String, String> reqParam = getAllRequestParam(request);
		// 打印请求报文
		log.info(JsonUtil.map2jsonToString(reqParam));
		
		//重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
		if (!AcpService.validate(reqParam, encoding)) {
			result.push("signStatusMsg", "验证签名结果[失败].");
			result.push("signStatusStatus", "error");
			log.info("验证签名结果[失败]");
			
		} else {
			//【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态
			result.push("signStatusMsg", "验证签名结果[成功].");
			result.push("signStatusStatus", "success");
			log.info("验证签名结果[成功].");
			//String orderId =reqParam.get("orderId"); //获取后台通知的数据，其他字段也可用类似方式获取
			//String respCode = reqParam.get("respCode");
			//判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
			
		}
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.SUCCESS);
		return result;
	}
	
	/**
     * 支付宝回调接口
     * @param request
     * @param response
     * @author fancunxin
     * @return
     */
    @RequestMapping(value="/alipay/callback/upload.ddxj")
    public String alipayCallBackUpload(HttpServletRequest request, HttpServletResponse respons)
    {
    	log.info("##########支付宝支付回调###########");
    	
    	String success = "success";
    	
    	Map<String,String> data = getAllRequestParam(request);//得到回调参数
    	boolean flag = false;
		try {
			flag = AlipaySignature.rsaCheckV1(data, AlipayConfig.ALIPAY_PUBLIC_KEY, "UTF-8",AlipayConfig.SIGNTYPE);//开始验签
		} catch (AlipayApiException e) {log.info("##########验签异常###########");}
    	
		if(flag)//验签通过
		{
			//验证该通知数据中的out_trade_no是否为商户系统中创建的订单号
			PaymentRecord payOutTradeNo = payService.selectPayOutTradeNo(data.get("out_trade_no"));
			if(!CmsUtils.isNullOrEmpty(payOutTradeNo))
			{
				//判断total_amount是否确实为该订单的实际金额
				BigDecimal amount = new BigDecimal(data.get("total_amount"));
				if(amount.compareTo(payOutTradeNo.getTotalFee()) != 0)
				{
					log.info("##########金额有误###########");
					return null;
				}
				//校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
				String sellerId = data.get("seller_id");
				if(!AlipayConfig.PARTNER.equals(sellerId))
				{
					log.info("##########商户支付宝用户号不匹配###########");
					return null;
				}
				//验证app_id是否为该商户本身
				String appId = data.get("app_id");
				if(!AlipayConfig.APPID.equals(appId))
				{
					log.info("##########APPID不匹配###########");
					return null;
				}
				//全部验证通过后，才可以认定买家付款成功。
				String fundBillList = data.get("fund_bill_list");
				JSONArray json = JsonUtil.list2jsonToArray(fundBillList.trim());
				String fundChannel = json.getJSONObject(0).get("fundChannel").toString();
				if("ALIPAYACCOUNT".equals(fundChannel))//支付宝余额
				{
					payOutTradeNo.setBankType("支付宝余额");
				}
				else if("FINANCEACCOUNT".equals(fundChannel))//余额宝
				{
					payOutTradeNo.setBankType("余额宝");
				}
				else if("FINANCEACCOUNT".equals(fundChannel))//商家储值卡
				{
					payOutTradeNo.setBankType("商家储值卡");
				}
				payOutTradeNo.setTransactionId(String.valueOf(data.get("trade_no")));//交易流水号
				payOutTradeNo.setTimeEnd(String.valueOf(data.get("gmt_payment")));//支付完成时间
				payOutTradeNo.setPayStatus(2);
				payOutTradeNo.setTotalFee(amount);//支付金额
				payOutTradeNo.setUpdateTime(new Date());
				payService.updatePayMentRecord(payOutTradeNo);
				
				log.info("##########充值成功###########");
				return success;
			}
			else
			{
				log.info("##########订单不存在###########");
				return null;
			}
		}
		else
		{
			log.info("##########验签失败###########");
			return null;
		}
    }
	
	/**
	 * 获取请求参数中所有的信息
	 * 当商户上送frontUrl或backUrl地址中带有参数信息的时候，
	 * 这种方式会将url地址中的参数读到map中，会导多出来这些信息从而致验签失败，这个时候可以自行修改过滤掉url中的参数或者使用getAllRequestParamStream方法。
	 * @param request
	 * @return
	 */
	public static Map<String, String> getAllRequestParam(
			final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				if (res.get(en) == null || "".equals(res.get(en))) {
					// System.out.println("======为空的字段名===="+en);
					res.remove(en);
				}
			}
		}
		return res;
	}
}
