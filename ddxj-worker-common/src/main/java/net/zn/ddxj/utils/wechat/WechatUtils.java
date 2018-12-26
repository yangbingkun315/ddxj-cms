package net.zn.ddxj.utils.wechat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.qiniu.common.QiniuException;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.PropertiesUtils;
import net.zn.ddxj.utils.QiNiuUploadManager;
import net.zn.ddxj.utils.json.JsonUtil;

@Slf4j
public class WechatUtils {
	
	/** 
     * 
     * 获取微信access_token 
     * <功能详细描述> 
     * @param appid 
     * @param secret 
     * @return 
     * @see [类、类#方法、类#成员] 
     */      
    public static JSONObject retrieveAccessToken(String appId,String appSecret) throws WXException
	{
	    try
	    {
	        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
	        HttpClient client = new HttpClient();
	        GetMethod getMethod = new GetMethod(url);
	        client.executeMethod(getMethod);
	        String returnString = getMethod.getResponseBodyAsString();
	        JSONObject retObject = JSONObject.fromObject(returnString);
	        String errorCode = "0";
	        if(!CmsUtils.isNullOrEmpty(retObject.get("errcode")))
	        {
	        	errorCode = retObject.getString("errcode");
	        }
	        if(CmsUtils.isNullOrEmpty(errorCode) || "0".equals(errorCode))
	        {
	            return retObject;
	        }
	        else
	        {
	            throw new WXException(Integer.parseInt(errorCode));
	        }
	    }
	    catch (WXException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new WXException(-1);
		}
	}
    
    public static void saveOrUpdateCustomerMenu(String accessToken,String menus) throws WXException
	{
	    try
	    {
	        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken;
	        HttpClient client = new HttpClient();
	        PostMethod postMethod = new PostMethod(url);
	        
	        byte[] b = menus.getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is);
			
			postMethod.setRequestEntity(re);
			
	        client.executeMethod(postMethod);
	        String returnString = postMethod.getResponseBodyAsString();
	        JSONObject retObject = JSONObject.fromObject(returnString);
	        String errorCode = "0";
	        if(!CmsUtils.isNullOrEmpty(retObject.get("errcode")))
	        {
	        	errorCode = retObject.getString("errcode");
	        }
	        if(!"0".equals(errorCode))
	        {
	            throw new WXException(Integer.parseInt(errorCode));
	        }
	    }
	    catch (WXException e)
	    {
	        throw e;
	    }
	    catch (Exception e)
	    {
	    	throw new WXException(-1);
	    }
	}
    /**
  	 * 上传图片到微信
  	 * 
  	 * @author 何俊辉
  	 * @param access_token
  	 * @param openid
  	 * @return
  	 * @throws ParseException
  	 * @throws IOException
  	 */
    public static JSONObject uploadWeiXinImage(String accessToken,String imageUrl) throws WXException
	{
		try
		{
			HttpClient client = new HttpClient();
			GetMethod getMethod = new GetMethod(imageUrl);
			client.executeMethod(getMethod);
			
			String url = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + accessToken + "&type=image";
			HttpPost postMethod = new HttpPost(url);
			
			String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("media", getMethod.getResponseBody(),ContentType.MULTIPART_FORM_DATA,fileName);// 文件流
            
            HttpEntity entity = builder.build();
            postMethod.setEntity(entity);
            
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(postMethod);// 执行提交
            
			String returnString =  IOUtils.toString(response.getEntity().getContent());
			
			JSONObject retObject = JSONObject.fromObject(returnString);
			 String errorCode = "0";
	        if(!CmsUtils.isNullOrEmpty(retObject.get("errcode")))
	        {
	        	errorCode = retObject.getString("errcode");
	        }
		        if(!"0".equals(errorCode))
		        {
		            throw new WXException(Integer.parseInt(errorCode));
		        }
		        else
		        {
		        	return retObject;
		        }
		}
		catch (WXException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new WXException(-1);
		}
	}
    public static JSONObject retrieveUserInfoByAccessToken(String accessToken,String openId) throws WXException
    {
    	try
    	{
    		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
    		HttpClient client = new HttpClient();
    		GetMethod getMethod = new GetMethod(url);
    		client.executeMethod(getMethod);
    		String returnString = new String(getMethod.getResponseBodyAsString().getBytes("ISO-8859-1"),"UTF-8");
    		JSONObject retObject = JSONObject.fromObject(returnString);
    		String errorCode = String.valueOf(retObject.get("errcode"));
    		if(CmsUtils.isNullOrEmpty(errorCode) || "0".equals(errorCode))
    		{
    			return retObject;
    		}
    		else
    		{
    			throw new WXException(Integer.parseInt(errorCode));
    		}
    	}
    	catch (WXException e)
    	{
    		throw e;
    	}
    	catch (Exception e)
    	{
    		throw new WXException(-1);
    	}
    }
    /**
	 * 查询公众平台用户信息
	 * @param client_id
	 * @param client_secret
	 * @return
	 * @throws WXException
	 */
	public static JSONObject retrieveWechatUserInfo (String access_token,String openid) throws WXException
	{
		try
		{
			String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
			HttpClient client = new HttpClient();
			GetMethod getMethod = new GetMethod(url);
			client.executeMethod(getMethod);
			String returnString = getMethod.getResponseBodyAsString();
			returnString = new String(returnString.getBytes("ISO-8859-1"),"UTF-8");
			JSONObject retObject = JSONObject.fromObject(returnString);
			String errorCode = String.valueOf(retObject.get("errcode"));
			if(CmsUtils.isNullOrEmpty(errorCode) || "0".equals(errorCode))
			{
				return retObject;
			}
			else
			{
				throw new WXException(Integer.parseInt(errorCode));
			}
		}
		catch (WXException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new WXException(-1);
		}
	}
	/**
	 * 微信关注粉丝列表
	 * @param access_token
	 * @param count
	 * @return
	 * @throws WXException
	 * @author heJunHui
	 * @date 2016-11-8 上午9:41:30
	 */
	public static JSONObject getUserFollowerList(String access_token,String next_openid) throws WXException
	{
		try
		{
			String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token="+access_token;
			if(!CmsUtils.isNullOrEmpty(next_openid))
			{
				url +="&next_openid="+next_openid;
			}
			
			HttpClient client = new HttpClient();
			GetMethod getMethod = new GetMethod(url);
			client.executeMethod(getMethod);
			String returnString = getMethod.getResponseBodyAsString();
			JSONObject retObject = JSONObject.fromObject(returnString);
			String errorCode = "0";
			if(!CmsUtils.isNullOrEmpty(retObject.get("errcode")))
			{
				errorCode = retObject.getString("errcode");
			}
			if(CmsUtils.isNullOrEmpty(errorCode) || "0".equals(errorCode))
			{
				return retObject;
			}
			else
			{
				throw new WXException(Integer.parseInt(errorCode));
			}
		}
		catch (WXException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new WXException(-1);
		}
	}
	/**
	 * 根据code获取用户信息
	 * @param code
	 * @return
	 * @throws WXException
	 */
	public static JSONObject retrieveAccessTokenByCode(String code) throws WXException
	{
		try
		{
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + PropertiesUtils.getPropertiesByName("wx_app_id") + "&secret=" + PropertiesUtils.getPropertiesByName("wx_app_secret") + "&code=" + code + "&grant_type=authorization_code";
			HttpClient client = new HttpClient();
			GetMethod getMethod = new GetMethod(url);
			client.executeMethod(getMethod);
			String returnString = getMethod.getResponseBodyAsString();
			JSONObject retObject = JSONObject.fromObject(returnString);
			String errorCode = "0";
			if(!CmsUtils.isNullOrEmpty(retObject.get("errcode")))
			{
				errorCode = retObject.getString("errcode");
			}
			if(CmsUtils.isNullOrEmpty(errorCode) || "0".equals(errorCode))
			{
				return retObject;
			}
			else
			{
				throw new WXException(Integer.parseInt(errorCode));
			}
		}
		catch (WXException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new WXException(-1);
		}
	}
	/**
	 * 批量获取用户信息
	 * @param access_token
	 * @param count
	 * @return
	 * @throws WXException
	 * @author heJunHui
	 * @date 2016-11-8 上午9:43:00
	 */
	public static JSONObject BulkAccessToUserBasicInformation(String access_token,List<Map<String,Object>> list) throws WXException
	{
		
		try
		{
			String url = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token="+access_token;

			Map<String,List<Map<String,Object>>> data = new HashMap<String,List<Map<String,Object>>>();
			data.put("user_list",list);
			
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			
			byte[] b = JsonUtil.map2jsonToString(data).getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is);
			
			postMethod.setRequestEntity(re);
			
			client.executeMethod(postMethod);
			String returnString = postMethod.getResponseBodyAsString();
			JSONObject retObject = JSONObject.fromObject(returnString);
			String errorCode = "0";
			if(!CmsUtils.isNullOrEmpty(retObject.get("errcode")))
			{
				errorCode = retObject.getString("errcode");
			}
			if(CmsUtils.isNullOrEmpty(errorCode) || "0".equals(errorCode))
			{
				return retObject;
			}
			else
			{
				throw new WXException(Integer.parseInt(errorCode));
			}
		}
		catch (WXException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new WXException(-1);
		}
	}
	/**
	 * 获取图文素材
	 * @param accessToken
	 * @param currentPage
	 * @return
	 * @throws WXException
	 */
	public static JSONObject getWeiXinMaterial(String accessToken,int currentPage,int count) throws WXException
	{
		try
		{
			Map<String,String> data = new HashMap<String,String>();
			data.put("type", "news");
			data.put("count", String.valueOf(count));
			int offset = (currentPage - 1) * 10;
			data.put("offset", String.valueOf(offset));
			
			String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + accessToken;
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			
			byte[] b = JsonUtil.map2jsonToString(data).getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is);
			
			postMethod.setRequestEntity(re);
			
			client.executeMethod(postMethod);
			
			String returnString =  postMethod.getResponseBodyAsString();
			
			JSONObject retObject = JSONObject.fromObject(returnString);
			String errorCode = "0";
			if(!CmsUtils.isNullOrEmpty(retObject.get("errcode")))
			{
				errorCode = retObject.getString("errcode");
			}
			if(CmsUtils.isNullOrEmpty(errorCode) || "0".equals(errorCode))
			{
					return retObject;
			}
			else
			{
				throw new WXException(Integer.parseInt(errorCode));
			}
		}
		catch (WXException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new WXException(-1);
		}
	}
	
	/**
	 * 获取图文素材详情
	 * @param accessToken
	 * @param mediaId
	 * @return
	 * @throws WXException
	 */
	public static JSONObject getWeiXinMaterialDetail(String accessToken,String mediaId) throws WXException
	{
		try
		{
			Map<String,String> data = new HashMap<String,String>();
			data.put("media_id", mediaId);
			
			String url = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=" + accessToken;
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			
			byte[] b = JsonUtil.map2jsonToString(data).getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is);
			
			postMethod.setRequestEntity(re);
			
			client.executeMethod(postMethod);
			
			String returnString =  postMethod.getResponseBodyAsString();
			
			JSONObject retObject = JSONObject.fromObject(returnString);
			String errorCode = "0";
			if(!CmsUtils.isNullOrEmpty(retObject.get("errcode")))
			{
				errorCode = retObject.getString("errcode");
			}
			if(CmsUtils.isNullOrEmpty(errorCode) || "0".equals(errorCode))
			{
				return retObject;
			}
			else
			{
				throw new WXException(Integer.parseInt(errorCode));
			}
		}
		catch (WXException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new WXException(-1);
		}
	}
	public static byte[] queryWeiXinNewsThumb(String accessToken,String thumb_media_id) throws WXException
	{
		try
		{
			Map<String,String> data = new HashMap<String,String>();
			data.put("media_id", thumb_media_id);
			
			String url = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=" + accessToken;
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			
			byte[] b = JsonUtil.map2jsonToString(data).getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is);
			
			postMethod.setRequestEntity(re);
			
			client.executeMethod(postMethod);
			
			String returnString =  postMethod.getResponseBodyAsString();
			
			return returnString.getBytes("ISO-8859-1");
		}
		catch (Exception e)
		{
			throw new WXException(-1);
		}
	}
	
	//根据微信素材 thumb_media_id 得到图片URL
	public static String obtainWechatImgByMediaId(String token,String thumb_media_id) throws WXException, QiniuException
    {
    	byte[] weiXinMaterial = queryWeiXinNewsThumb(token, thumb_media_id);
    	Calendar cal = Calendar.getInstance();
        String fileName = CmsUtils.generateStr(32);
        String filePath = Constants.UPLOAD_IMAGE_PATH + cal.get(Calendar.YEAR) + Constants.SPT + (cal.get(Calendar.MONTH) + 1) + Constants.SPT +(cal.get(Calendar.DATE) + 1) + Constants.SPT + fileName;
        // 上传文件
    	QiNiuUploadManager.uploadFile(filePath, weiXinMaterial);
    	return PropertiesUtils.getPropertiesByName("static_url") + filePath;
    }
	
	//发送微信模板消息
	public static String sendWechatTemplateMassage(String accessToken,String content) throws WXException
	{
		try
		{
			String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			
			byte[] b = content.getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is);
			
			postMethod.setRequestEntity(re);
			
			client.executeMethod(postMethod);
			
			String returnString =  postMethod.getResponseBodyAsString();
			
			return returnString;
	        
		}
		catch (Exception e)
		{
			throw new WXException(-1);
		}
	}
	public static String retrieveJSAPITicket(String accessToken) throws WXException
	{
		try
		{
			String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
			HttpClient client = new HttpClient();
			GetMethod getMethod = new GetMethod(url);
			client.executeMethod(getMethod);
			String returnString = getMethod.getResponseBodyAsString();
			JSONObject retObject = JSONObject.fromObject(returnString);
			String errorCode = "0";
			if(!CmsUtils.isNullOrEmpty(retObject.get("errcode")))
			{
				errorCode = retObject.getString("errcode");
			}
			if(CmsUtils.isNullOrEmpty(errorCode) || "0".equals(errorCode))
			{
				return retObject.getString("ticket");
			}
			else
			{
				throw new WXException(Integer.parseInt(errorCode));
			}
		}
		catch (WXException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new WXException(-1);
		}
	}
	 /**
     * 验证签名。
     * 
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static String getSignature(String sKey) throws Exception {
        String ciphertext = null;
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(sKey.toString().getBytes());
        ciphertext = byteToStr(digest);
        return ciphertext.toLowerCase();
    }

 /** 
     * 将字节数组转换为十六进制字符串 
     *  
     * @param byteArray 
     * @return 
     */ 
    private static String byteToStr(byte[] byteArray) {  
        String strDigest = "";  
        for (int i = 0; i < byteArray.length; i++) {  
            strDigest += byteToHexStr(byteArray[i]);  
        }  
        return strDigest;  
    }  
    public static JSONObject retrieveForeverQRTicket(String accessToken,String sceneId) throws WXException
	{
		// 组织内容
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("action_name", "QR_LIMIT_STR_SCENE");
		
		Map<String,Object> action_info = new HashMap<String,Object>();
		
		Map<String,Object> scene = new HashMap<String,Object>();
		scene.put("scene_str", sceneId);
		
		action_info.put("scene", scene);
		
		data.put("action_info", action_info);
		
		String sendContent = JsonUtil.map2jsonToString(data);
		
		try
		{
			String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			
			byte[] b = sendContent.getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is);
			
			postMethod.setRequestEntity(re);
			
			client.executeMethod(postMethod);
			String returnString = postMethod.getResponseBodyAsString();
			JSONObject retObject = JSONObject.fromObject(returnString);
			if(retObject.has("ticket") && retObject.has("url"))
			{
				return retObject;
			}
			else
			{
				String errorCode = retObject.getString("errcode");
				throw new WXException(Integer.parseInt(errorCode));
			}
		}
		catch (WXException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new WXException(-1);
		}
	}
  /** 
     * 将字节转换为十六进制字符串 
     *  
     * @param mByte 
     * @return 
     */ 
    private static String byteToHexStr(byte mByte) {  
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };  
        char[] tempArr = new char[2];  
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];  
        tempArr[1] = Digit[mByte & 0X0F];  

        String s = new String(tempArr);  
        return s;  
    }
    
    /**
	 * @将Dictionary转成xml
	 * @return 经转换得到的xml串
	 * @throws WxPayException
	 **/
	public static String ToXml(Map<String,Object> map) throws WxPayException {
		// 数据为空时不能转化为xml格式
		if (0 == map.size()) {
			throw new WxPayException("MAP数据为空!");
		}

		String xml = "<xml>";
		for (Entry<String, Object> pair : map.entrySet()) {
			// 字段值不能为null，会影响后续流程
			if (pair.getValue() == null) {
				throw new WxPayException("MAP内部含有值为null的字段!");
			}

			if (pair.getValue() instanceof Integer) {
				xml += "<" + pair.getKey() + ">" + pair.getValue() + "</"
						+ pair.getKey() + ">";
			} else if (pair.getValue() instanceof String) {
				xml += "<" + pair.getKey() + ">" + "<![CDATA["
						+ pair.getValue() + "]]></" + pair.getKey() + ">";
			} else if (pair.getValue() instanceof Map) {
				Map<String,Object> data = (Map<String, Object>) pair.getValue();
				xml+="<" + pair.getKey() + ">";
				for (Entry<String, Object> pairs : data.entrySet()) 
				{
					if (pairs.getValue() instanceof Integer) {
						xml += "<" + pairs.getKey() + ">" + pairs.getValue() + "</"
								+ pairs.getKey() + ">";
					} else if (pairs.getValue() instanceof String) {
						xml += "<" + pairs.getKey() + ">" + "<![CDATA["
								+ pairs.getValue() + "]]></" + pairs.getKey() + ">";
					}
				}
				xml+="</" + pair.getKey() + ">";
			} 
			else if (pair.getValue() instanceof List) 
			{
				List<Map<String,Object>> data = (List<Map<String, Object>>) pair.getValue();
				xml+="<" + pair.getKey() + ">";//accccccccc
				for(int i = 0; i<data.size();i++)
				{
					Map<String,Object> listMap = data.get(i);
					for (Entry<String, Object> pairs : listMap.entrySet()) 
					{
						xml+="<" + pairs.getKey() + ">";//item
						
						Map<String,Object> data123 = (Map<String, Object>) pairs.getValue();
						
						for (Entry<String, Object> pairs1 : data123.entrySet()) //变量
						{
							if (pairs.getValue() instanceof Integer) {
								xml += "<" + pairs1.getKey() + ">" + pairs1.getValue() + "</"
										+ pairs1.getKey() + ">";
							} else if (pairs1.getValue() instanceof String) {
								xml += "<" + pairs1.getKey() + ">" + "<![CDATA["
										+ pairs1.getValue() + "]]></" + pairs1.getKey() + ">";
							}
						}
						xml+="</" + pairs.getKey() + ">";
					}
				}
				xml+="</" + pair.getKey() + ">";
			} 
			else// 除了string和int类型不能含有其他数据类型
			{
				throw new WxPayException("MAP字段数据类型错误!");
			}
		}
		xml += "</xml>";
		
		log.info("xml转换" + xml);
		return xml;
	}
	
	/**
	 * 设置微信自动回复
	 * @param accessToken
	 * @param currentPage
	 * @return
	 * @throws WXException
	 */
	public static JSONObject setWeiXinAutomaticReply(String accessToken,int currentPage,int count) throws WXException
	{
		try
		{
			Map<String,String> data = new HashMap<String,String>();
			data.put("type", "news");
			data.put("count", String.valueOf(count));
			int offset = (currentPage - 1) * 10;
			data.put("offset", String.valueOf(offset));
			
			String url = "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token=ACCESS_TOKEN" + accessToken;
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			
			byte[] b = JsonUtil.map2jsonToString(data).getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is);
			
			postMethod.setRequestEntity(re);
			
			client.executeMethod(postMethod);
			
			String returnString =  postMethod.getResponseBodyAsString();
			
			JSONObject retObject = JSONObject.fromObject(returnString);
			String errorCode = "0";
			if(!CmsUtils.isNullOrEmpty(retObject.get("errcode")))
			{
				errorCode = retObject.getString("errcode");
			}
			if(CmsUtils.isNullOrEmpty(errorCode) || "0".equals(errorCode))
			{
				return retObject;
			}
			else
			{
				throw new WXException(Integer.parseInt(errorCode));
			}
		}
		catch (WXException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new WXException(-1);
		}
	}
	
	/**
	 * 发送客服消息
	 * @param accessToken
	 * @author fancunxin
	 * @return
	 * @throws WXException
	 */
	public static JSONObject sendWechatCustomServiceMessage(String accessToken,String openId,String content,int massType,String mediaId,List<Map<String,Object>> list) throws WXException
	{
		try
		{
			// 组织内容
			Map<String,Object> data = new HashMap<String,Object>();
			Map<String,Object> map = new HashMap<String,Object>();
			data.put("touser", openId);
			if(massType == 1)
			{
				data.put("msgtype", "text");
				map.put("content", content);
				data.put("text", map);
				
			}
			else if(massType == 2)
			{
				data.put("msgtype", "news");
				map.put("articles", list);
				data.put("news", map);
				
			}
			else if(massType == 3)
			{
				data.put("msgtype", "image");
				map.put("media_id", mediaId);
				data.put("image", map);
			}
			String sendContent = JsonUtil.map2jsonToString(data);
			
			String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken;
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			
			byte[] b = sendContent.getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is);
			
			postMethod.setRequestEntity(re);
			
			client.executeMethod(postMethod);
			String returnString = postMethod.getResponseBodyAsString();
			JSONObject retObject = JSONObject.fromObject(returnString);
			return retObject;
		}
		catch (Exception e){throw new WXException(-1);}
	}
	
	/**
	 * 根据标签群发
	 * @param accessToken
	 * @author fancunxin
	 * @return
	 * @throws WXException
	 */
	public static JSONObject wechatMassMessage(String access_token,Integer tagId,Integer massType,String textMessage,String mediaId) throws WXException
	{
		try
		{
			String url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token="+access_token;
			
			Map<String,Object> data = new HashMap<String,Object>();
			Map<String,Object> map = new HashMap<String,Object>();
			Map<String,Object> message = new HashMap<String,Object>();
			
			// tagId = 用户标签ID
			if(tagId != null && tagId > 0)
			{
				map.put("is_to_all", false);
				map.put("tag_id", tagId);
			}
			else
			{
				map.put("is_to_all", true);
			}
			data.put("filter", map);
			
			if(massType == 1)
			{
				//文本内容
				message.put("content", textMessage);
				data.put("text", message);
				data.put("msgtype", "text");
			}
			else if(massType == 2)
			{
				//图文素材
				message.put("media_id", mediaId);
				data.put("mpnews", message);
				data.put("msgtype", "mpnews");
				data.put("send_ignore_reprint", 0);
			}
			else if(massType == 3)
			{
				//图文素材
				message.put("media_id", mediaId);
				data.put("image", message);
				data.put("msgtype", "image");
			}
			
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			byte[] b = JsonUtil.map2jsonToString(data).getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is);
			
			postMethod.setRequestEntity(re);
			
			client.executeMethod(postMethod);
			String returnString = postMethod.getResponseBodyAsString();
			JSONObject retObject = JSONObject.fromObject(returnString);
			String errorCode = retObject.getString("errcode");
			if(CmsUtils.isNullOrEmpty(errorCode) || "0".equals(errorCode))
			{
				return retObject;
			}
			else
			{
				throw new WXException(Integer.parseInt(errorCode));
			}
		}
		catch (WXException e){throw e;}
		catch (Exception e){throw new WXException(-1);}
	}
	
	/**
	 * 根据openId群发消息
	 * @param accessToken
	 * @author fancunxin
	 * @return
	 * @throws WXException
	 */
	public static JSONObject wechatByOpenIdMassMessage(String access_token,List<String> openIdList,String mediaId,Integer massType,String textMessage) throws WXException
	{
		try
		{
			String url = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token="+access_token;
			
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("touser", openIdList);
			
			Map<String,Object> map = new HashMap<String,Object>();
			if(massType == 1)
			{
				map.put("content", textMessage);
				data.put("text",map);
				data.put("msgtype", "text");
			}
			else if(massType == 2)
			{
				map.put("media_id", mediaId);
				data.put("mpnews", map);
				data.put("msgtype", "mpnews");
			}
			else if(massType == 3)
			{
				map.put("media_id", mediaId);
				data.put("image", map);
				data.put("msgtype", "image");
			}
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			byte[] b = JsonUtil.map2jsonToString(data).getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is);
			
			postMethod.setRequestEntity(re);
			
			client.executeMethod(postMethod);
			String returnString = postMethod.getResponseBodyAsString();
			JSONObject retObject = JSONObject.fromObject(returnString);
			return retObject;
		}
		catch (Exception e){throw new WXException(-1);}
	}
	
	/**
	 * 查询公众号下的所有用户标签
	 * @param accessToken
	 * @author fancunxin
	 * @return
	 * @throws WXException
	 */
	public static JSONObject queryWechatUserTagsList(String access_token) throws WXException
	{
		try
		{
			String url = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=" + access_token;
			
			HttpClient client = new HttpClient();
			GetMethod getMethod = new GetMethod(url);
			
			client.executeMethod(getMethod);
		    String returnString = new String(getMethod.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8");
			JSONObject retObject = JSONObject.fromObject(new String(returnString));
			return retObject;
		}
		catch (Exception e){throw new WXException(-1);}
	}
	
	/**
	 * 查询公众号下的所有用户标签
	 * @param accessToken
	 * @author fancunxin
	 * @return
	 * @throws WXException
	 */
	public static JSONObject addEasemobUser(List<Map<String, Object>> data,String access_token) throws WXException
	{
		try
		{
			String url = "https://a1.easemob.com/" + PropertiesUtils.getPropertiesByName("ORG_NAME") + "/" + PropertiesUtils.getPropertiesByName("APP_NAME") + "/users";
			
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Content-Type","application/json");
			postMethod.setRequestHeader("Authorization", access_token);
			
			byte[] b = null;
			if(data.size() == 1)
			{
				b = JsonUtil.map2jsonToString(data.get(0)).getBytes("utf-8");
			}
			else
			{
				b = JsonUtil.list2jsonToString(data).getBytes("utf-8");
			}
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is);
			
			postMethod.setRequestEntity(re);
			
			client.executeMethod(postMethod);
			String returnString = postMethod.getResponseBodyAsString();
			JSONObject retObject = JSONObject.fromObject(returnString);
			return retObject;
		}
		catch (Exception e){throw new WXException(-1);}
	}
}
