package net.zn.ddxj.utils.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.dysmsapi.transform.v20170525.SendSmsResponseUnmarshaller;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import net.zn.ddxj.utils.PropertiesUtils;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 阿里大鱼短信验证 短信API 要将AK替换成开通了云通信-短信产品功能的AK 工程依赖了2个jar包(存放在工程的lib目录下)
 *
 * 备注:Demo工程编码采用UTF-8 国际短信发送请勿参照此DEMO
 * 
 * @author dongbisheng
 * @date Aug 18, 2017 1:24:26 PM
 */
public class SmsUtil {
	public static final Integer NUM = 6;
	// 产品名称:云通信短信API产品,开发者无需替换
	static final String product = "Dysmsapi";
	// 产品域名,开发者无需替换
	static final String domain = "dysmsapi.aliyuncs.com";

	// TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
	//外包的
//	static final String accessKeyId = "LTAI7qrQkPpyeAtk";
//	static final String accessKeySecret = "aK5oWEzYwS8BvaJs2NFmXlMRVNPjrm";
	
	//公司的
	static final String accessKeyId = PropertiesUtils.getPropertiesByName("ytx_access_key_id");
	static final String accessKeySecret = PropertiesUtils.getPropertiesByName("ytx_access_key_secret");

	public static SendSmsResponse sendSms(String phone,Integer type) throws ClientException {

		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(phone);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName("点点小匠");
		// 必填:短信模板-可在短信控制台中找到
		// 随机生成 num 位验证码
		String code = "";
		Random r = new Random(new Date().getTime());
		for (int i = 0; i < NUM; i++) {
			code = code + r.nextInt(10);
		}
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"" + code + "\"}");

		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");

		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		// request.setOutId("85195275"); //工号单？

		// hint 此处可能会抛出异常，注意catch
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

		return sendSmsResponse;
	}

	public static String getSmsCode(String phone ,Integer type,Integer loginType) throws ClientException {

		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(phone);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName("点点小匠");
		// 必填:短信模板-可在短信控制台中找到
		/*
		request.setTemplateCode("SMS_129746846");//1.注册验证码
		request.setTemplateCode("SMS_133963984");//2.修改密码
		request.setTemplateCode("SMS_133979020");//3.修改支付密码
		request.setTemplateCode("SMS_133969071");//4.绑定银行卡
		request.setTemplateCode("SMS_133979023");//5.授信验证码
		request.setTemplateCode("SMS_133979024");//6.更改手机号码
		request.setTemplateCode("SMS_137410320");//7.用户登录
		request.setTemplateCode("SMS_134250055");//8.忘记密码
		request.setTemplateCode("SMS_139030197");//9.网站发布招聘
		request.setTemplateCode("SMS_143867004");//10.还款验证码
		
		 */		
		if(type==1){
			if(loginType == 1)//登录
			{
				request.setTemplateCode("SMS_137410320");//7.用户登录
			}
			else//注册
			{
				//request.setTemplateCode("SMS_129746846");//1.注册验证码
				request.setTemplateCode("SMS_140550055");//1.注册验证码SMS_140550055
			}
		}else if(type==2){
			request.setTemplateCode("SMS_133963984");//2.修改密码
		}else if(type==3){
			request.setTemplateCode("SMS_133979020");//3.修改支付密码
			//request.setTemplateCode("SMS_139985701");//3.忘记支付密码
		}else if(type==4){
			request.setTemplateCode("SMS_133969071");//4.绑定银行卡
		}else if(type==5){
			//request.setTemplateCode("SMS_133979023");//5.授信验证码
			//request.setTemplateCode("SMS_139980513");//5.授信验证码
			request.setTemplateCode("SMS_139977025");//5.授信验证码
		}else if(type==6){
			request.setTemplateCode("SMS_133979024");//6.更改手机号码
		}else if(type==7){
			request.setTemplateCode("SMS_134250055");//8.忘记密码
		}else if(type==9){
			request.setTemplateCode("SMS_139030197");//9.网站发布招聘
		}else if(type==10){
			request.setTemplateCode("SMS_143867004");//9.还款验证码
		}
		// 随机生成 num 位验证码
		String code = "";
		Random r = new Random(new Date().getTime());
		for (int i = 0; i < NUM; i++) {
			code = code + r.nextInt(10);
		}
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		// request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"" + code +
		// "\"}");
		request.setTemplateParam("{ \"code\":\"" + code + "\"}");
		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");

		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		// request.setOutId("85195275"); //工号单？

		// hint 此处可能会抛出异常，注意catch
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

		return code;
	}

	public static QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {

		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象
		QuerySendDetailsRequest request = new QuerySendDetailsRequest();
		// 必填-号码
		request.setPhoneNumber("15268022895");
		// 可选-流水号
		request.setBizId(bizId);
		// 必填-发送日期 支持30天内记录查询，格式yyyyMMdd
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		request.setSendDate(ft.format(new Date()));
		// 必填-页大小
		request.setPageSize(10L);
		// 必填-当前页码从1开始计数
		request.setCurrentPage(1L);

		// hint 此处可能会抛出异常，注意catch
		QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

		return querySendDetailsResponse;
	}

	public static void main(String[] args) throws ClientException, InterruptedException {

		//getSmsCode("17621328341",1);
//		// 发短信
//		SendSmsResponse response = sendSms("17621328341");
//		System.out.println("短信接口返回的数据----------------");
//		System.out.println("Code=" + response.getCode());
//		System.out.println("Message=" + response.getMessage());
//		System.out.println("RequestId=" + response.getRequestId());
//		System.out.println("BizId=" + response.getBizId());
//
//		Thread.sleep(3000L);
//
//		// 查明细
//		if (response.getCode() != null && response.getCode().equals("OK")) {
//			QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId());
//			System.out.println("短信明细查询接口返回数据----------------");
//			System.out.println("Code=" + querySendDetailsResponse.getCode());
//			System.out.println("Message=" + querySendDetailsResponse.getMessage());
//			int i = 0;
//			for (QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse
//					.getSmsSendDetailDTOs()) {
//				System.out.println("SmsSendDetailDTO[" + i + "]:");
//				System.out.println("Content=" + smsSendDetailDTO.getContent());
//				System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
//				System.out.println("OutId=" + smsSendDetailDTO.getOutId());
//				System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
//				System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
//				System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
//				System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
//				System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
//			}
//			System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
//			System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
//		}

	}
}
