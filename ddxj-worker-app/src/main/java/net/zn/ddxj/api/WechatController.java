package net.zn.ddxj.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.qiniu.common.QiniuException;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.InviteLink;
import net.zn.ddxj.entity.InviteRecord;
import net.zn.ddxj.entity.KeyWords;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.WechatMenu;
import net.zn.ddxj.service.InviteLinkService;
import net.zn.ddxj.service.InviteRecordService;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.tool.CacheSettingService;
import net.zn.ddxj.tool.WechatService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.PropertiesUtils;
import net.zn.ddxj.utils.wechat.CheckUtil;
import net.zn.ddxj.utils.wechat.CustomPushUtils;
import net.zn.ddxj.utils.wechat.WXException;
import net.zn.ddxj.utils.wechat.WechatUtils;
import net.zn.ddxj.utils.wechat.WxPayException;

@RestController
@Slf4j
public class WechatController {

	@Autowired
	private WechatService wechatService;
	
	@Autowired
	private InviteLinkService inviteLinkService;
	
	@Autowired
	private InviteRecordService inviteRecordService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CacheSettingService cacheSetting;
	/**
	 * 验证TOKEN
	 * @param req
	 * @param response
	 * @throws IOException
	 * @throws WxPayException 
	 */
	@RequestMapping(value = "/validate/wechat/token.ddxj")
	public String validateWechatToken(HttpServletRequest req, HttpServletResponse response,@RequestBody(required=false) String body) throws IOException, WxPayException {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		try 
		{
			if(!CmsUtils.isNullOrEmpty(body))
			{
				Map<String, Object> fromXml = fromXml(body);
				log.info("公众号操作："+fromXml.toString());
				if(!CmsUtils.isNullOrEmpty(fromXml))
				{
					String msgType = fromXml.get("MsgType").toString();
					//消息类型
					if("event".equals(msgType)){
						String event = String.valueOf(fromXml.get("Event"));//事件类型
						if(!CmsUtils.isNullOrEmpty(event))
						{
							if(Constants.SUBSCRIBE.equals(event))//首次关注公众号
							{
								try {
									return subscribe(fromXml);
								} catch (WXException e) {}
							}
							else if(Constants.UNSUBSCRIBE.equals(event))//直接取消关注公众号
							{
								unSubscribe(fromXml);
							}
							else if(Constants.CLICK.equals(event))//消息事件推送
							{
								String eventKey = String.valueOf(fromXml.get("EventKey"));//事件类型
								if(!CmsUtils.isNullOrEmpty(eventKey))
								{
									WechatMenu queryWechatMenuByKey = wechatService.queryWechatMenuByKey(eventKey);
									return CustomPushUtils.customAssembleText(fromXml,queryWechatMenuByKey.getReplyText());
								}
							}
						}
					}
					else if("text".equals(msgType))//关键字回复
					{
						List<KeyWords> keyWordsList = wechatService.findKeyWordsByKeyWords(String.valueOf(fromXml.get("Content")));
						if(!CmsUtils.isNullOrEmpty(keyWordsList))
						{
							String openId = fromXml.get("FromUserName").toString();
							String toUserName = fromXml.get("ToUserName").toString();
							KeyWords keywords =null;
							//完全匹配的
							for(int i = 0;i<keyWordsList.size();i++)
							{
								if(keyWordsList.get(i).getIsMatch() == 1 && keyWordsList.get(i).getKeyWords().equals(String.valueOf(fromXml.get("Content"))))
								{
									keywords = keyWordsList.get(i);
									break ;
								}
							}
							if(CmsUtils.isNullOrEmpty(keywords))
							{
								//不完全匹配的
								for(int i = 0;i<keyWordsList.size();i++)
								{
									if(keyWordsList.get(i).getIsMatch() == 2)
									{
										keywords = keyWordsList.get(i);
										break ;
									}
								}
							}
							if(!CmsUtils.isNullOrEmpty(keywords))
							{
								Map<String,Object> imageMap = new HashMap<String,Object>();
								imageMap.put("ToUserName", openId);
								imageMap.put("FromUserName", toUserName);
								imageMap.put("CreateTime", ((int) System.currentTimeMillis()));
								if(keywords.getReplyType() == Constants.Number.ONE_INT)//文字回复
								{
									return CustomPushUtils.customAssembleText(fromXml,keywords.getReplayWords());
								}
								else if(keywords.getReplyType() == Constants.Number.TWO_INT)//图片回复
								{
									imageMap.put("MsgType", Constants.MESSAGE_IMAGE);
					        		Map<String,Object> parem1 = new HashMap<String,Object>();
					        		parem1.put("MediaId", keywords.getReplayWords().split("###")[1]);
					        		imageMap.put("Image", parem1);
					        		return WechatUtils.ToXml(imageMap);
								}
								else if(keywords.getReplyType() == Constants.Number.THREE_INT)//图文消息
								{
									try 
									{
										
										JSONObject weiXinMaterial = WechatUtils.getWeiXinMaterialDetail(wechatService.queryWechatToken(), keywords.getReplayWords().split("###")[1]);
										JSONArray arrayItem = weiXinMaterial.getJSONArray("news_item");
										List<Map<String,Object>> articleList = new ArrayList<Map<String,Object>>();
										if(arrayItem.size() > 0)
										{
											for(int i = 0;i < arrayItem.size();i++)
											{
												JSONObject item = arrayItem.getJSONObject(i);
												if(item != null)
												{
													Map<String,Object> map0 = new HashMap<String,Object>();
													Map<String,Object> map = new HashMap<String,Object>();
													map.put("Title", new String(item.getString("title").getBytes("ISO-8859-1"),"UTF-8"));
													map.put("Description", new String(item.getString("digest").getBytes("ISO-8859-1"),"UTF-8"));
													map.put("PicUrl", WechatUtils.obtainWechatImgByMediaId(wechatService.queryWechatToken(),item.getString("thumb_media_id")));
													map.put("Url", new String(item.getString("url").getBytes("ISO-8859-1"),"UTF-8"));
													map0.put("item", map);
													articleList.add(map0);
												}
											}
										}
										imageMap.put("MsgType", Constants.MESSAGE_NEWS);
										imageMap.put("ArticleCount", articleList.size());
										imageMap.put("Articles", articleList);
										return WechatUtils.ToXml(imageMap);
									}
									catch (WXException e) 
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
								}
							}
							else
							{
								try {
									return invalidKeywordsReply(fromXml);
								} catch (WXException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
						}
						else
						{
							try {
								return invalidKeywordsReply(fromXml);
							} catch (WXException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}
					else if("image".equals(msgType))//接受图片时回复
					{
						try {
							return pictureResponse(fromXml);
						} catch (WXException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} 
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		log.info(signature + "####" + timestamp + "####" + nonce + "####" + echostr + "####");
		log.info("#### TOKEN SUCCESS ####");
		if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
			return echostr;
		}
		return null;
	}
	/**
	 * @将xml转为WxPayData对象并返回对象内部的数据
	 * @param string
	 *            待转换的xml串
	 * @return 经转换得到的Dictionary
	 * @throws WxPayException
	 * @throws ParserConfigurationException
	 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 * @throws SAXException
	 */
	public Map<String, Object> fromXml(String xml) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException{
		Map<String,Object> mValues = new HashMap<String,Object>();
		if (xml == null || xml.isEmpty()) {
			log.error("xml为空!");
		}

		DocumentBuilderFactory documentBuildFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder doccumentBuilder = documentBuildFactory
				.newDocumentBuilder();
		Document document = doccumentBuilder.parse(new ByteArrayInputStream(xml
				.getBytes("UTF-8")));
		Node xmlNode = document.getFirstChild();// 获取到根节点<xml>
		NodeList nodes = xmlNode.getChildNodes();
		for (int i = 0, length = nodes.getLength(); i < length; i++) {
			Node xn = nodes.item(i);
			if (xn instanceof Element) {
				Element xe = (Element) xn;
				mValues.put(xe.getNodeName(), xe.getTextContent());// 获取xml的键值对到WxPayData内部的数据中
			}
		}
		return mValues;
	}
	/**
	 * 授权具体信息
	 * 
	 * @author dongbisheng
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@RequestMapping(value="/query/wechat/user/info.ddxj")
	public ResponseBase getUserMessage(HttpServletRequest request, HttpServletResponse response,String code) throws ParseException, IOException {
		ResponseBase result = ResponseBase.getInitResponse();
		try
		{
			JSONObject jsAccessTokenJSON =WechatUtils.retrieveAccessTokenByCode(code);
			JSONObject userMessage = WechatUtils.retrieveWechatUserInfo(wechatService.queryWechatToken(), jsAccessTokenJSON.getString("openid"));
			result.push("wechatUser", userMessage);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.SUCCESS);
			return result;
			
		}
		catch(WXException e)
		{
			result.push("ret", Constants.Number.ONE_STRING);
			result.push("code",e.getRet());
		}
		
		result.setResponse(Constants.FALSE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.GET_WECHAT_USER_INFORMATION_FAILURE);
		return result;
	}
	/**
	 * 查询是否关注公众号
	 * @param request
	 * @param response
	 * @param accessToken
	 * @param openId
	 * @return
	 * @throws WXException
	 */
    @RequestMapping(value="/query/user/is/followWeChat.ddxj")
    public ResponseBase queryUserIsFollow(HttpServletRequest request, HttpServletResponse response,String code,String openId) throws WXException{
    	ResponseBase result = ResponseBase.getInitResponse();
    	JSONObject followWeChat = new JSONObject();
    	if(CmsUtils.isNullOrEmpty(openId))
    	{
    		JSONObject jsAccessTokenJSON =WechatUtils.retrieveAccessTokenByCode(code);
    		followWeChat = WechatUtils.retrieveWechatUserInfo(wechatService.queryWechatToken(), jsAccessTokenJSON.getString("openid"));
    	}
    	else
    	{
    		followWeChat = WechatUtils.retrieveWechatUserInfo(wechatService.queryWechatToken(), openId);
    	}
		String subscribe=String.valueOf(followWeChat.get("subscribe"));
		if("1".equals(subscribe))
		{
			result.push("subscribe", Constants.YES);
			result.push("openId", followWeChat.getString("openid"));
    		result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.WECHAT_USER__SUBSCRIBE);
		}
		else 
		{
			result.push("subscribe", Constants.NO);
			result.push("openId", followWeChat.getString("openid"));
    		result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.WECHAT_USER__UNSUBSCRIBE);
		}
		return result;
    	
    }
    /**
	 * 微信分享
	 * @param request
	 * @param response
     * @throws Exception 
	 */
	@RequestMapping("/query/auth/share.ddxj")
	public ResponseBase getJsapiTicket(HttpServletRequest request, HttpServletResponse response,String url) throws Exception {
		ResponseBase result = ResponseBase.getInitResponse();
		// 2、通过access_token获取jsapi_ticket
		try
		{
			String jsapiTicket = WechatUtils.retrieveJSAPITicket(wechatService.queryWechatToken());
			String timestamp = Long.toString(System.currentTimeMillis() / 1000);
			String nonceStr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);//随机字符串

			//参数
			//5、将参数排序并拼接字符串
			//url ="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+PropertiesUtils.getPropertiesByName("wx_app_id")+"&redirect_uri="+url+"&response_type=code&scope=snsapi_userinfo&state=1"; 
			String str = "jsapi_ticket="+jsapiTicket+"&noncestr="+nonceStr+"&timestamp="+timestamp+"&url="+url;
			String signature = WechatUtils.getSignature(str);
			result.push("url", url);
			result.push("jsapi_ticket", jsapiTicket);
			result.push("nonceStr", nonceStr);
			result.push("appId", PropertiesUtils.getPropertiesByName("wx_app_id"));
			result.push("timestamp", timestamp);
			result.push("signature", signature);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.QUERY_INFORMATION_SUCCESSFUL);
			
		}
		catch(WXException e)
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.QUERY_INFORMATION_FAILURE);
		}
		return result;
	}
	
	/**
	 * 查询永久二维码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/query/wechat/lastingqrcode.ddxj")
	public ResponseBase queryWechatQRcode(HttpServletRequest request, HttpServletResponse response){
		ResponseBase result = ResponseBase.getInitResponse();
			String userId = request.getParameter("userId");
			User user = userService.selectByPrimaryKey(Integer.valueOf(userId));
			if(!CmsUtils.isNullOrEmpty(user))
			{
				String ticket = null;
				InviteLink inviteLink = inviteLinkService.findByUserId(Integer.valueOf(userId));
				if(CmsUtils.isNullOrEmpty(inviteLink)){
					result.push("wechatUrl", "");
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.QUERY_INFORMATION_FAILURE);
					return result;
				}else{
					ticket = inviteLink.getTicket();
				}
				result.push("wechatUrl", ticket);
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.QUERY_INFORMATION_SUCCESSFUL);
			}else
			{
				result.push("wechatUrl", "");
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.QUERY_INFORMATION_FAILURE);
			}
		return result;
	}
	
	/**
	 * 获取永久二维码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/query/wechat/qrcode.ddxj")
	public ResponseBase getWechatQRcode(HttpServletRequest request, HttpServletResponse response){
		ResponseBase result = ResponseBase.getInitResponse();
		try 
		{
			//String phone = request.getParameter("phone");
			String userId = request.getParameter("userId");
			User user = userService.selectByPrimaryKey(Integer.valueOf(userId));
			if(!CmsUtils.isNullOrEmpty(user))
			{
				String phone = user.getPhone();
				String ticket = null;
				InviteLink inviteLink = inviteLinkService.findByUserId(Integer.valueOf(userId));
				if(CmsUtils.isNullOrEmpty(inviteLink)){
					JSONObject json = WechatUtils.retrieveForeverQRTicket(wechatService.queryWechatToken(), phone);
					ticket = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ json.getString("ticket");
					String url = json.getString("url");
					//保存二维码信息加入邀请表
					inviteLinkService.insertInviteLink(Integer.valueOf(userId),ticket,url);
				}else{
					ticket = inviteLink.getTicket();
				}
				result.push("wechatUrl", ticket);
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.QUERY_INFORMATION_SUCCESSFUL);
			}else
			{
				result.push("wechatUrl", "");
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.QUERY_INFORMATION_FAILURE);
			}
		}
		catch (WXException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	/**
	 * 接受图片时回复
	 * @param fromXml
	 * @return
	 * @throws WXException
	 * @throws WxPayException
	 * @throws UnsupportedEncodingException
	 * @throws QiniuException
	 */
	private String pictureResponse(Map<String,Object> fromXml) throws WXException, WxPayException, UnsupportedEncodingException, QiniuException
	{
		String openId = fromXml.get("FromUserName").toString();
		String toUserName = fromXml.get("ToUserName").toString();
		String useSystemDefaultReplyPic = cacheSetting.findSettingValue(Constants.USE_SYSTEM_DEFAULT_REPLY_PIC);
		String replyTypePic = cacheSetting.findSettingValue(Constants.REPLY_TYPE_PIC);
    	String replayWordsPic = cacheSetting.findSettingValue(Constants.REPLAY_WORDS_PIC);
    	String imageTextListPic = cacheSetting.findSettingValue(Constants.IMAGE_TEXT_LIST_PIC);
    	String imageMediaIdPic = cacheSetting.findSettingValue(Constants.BEFOLLOWED_REPLY_MEDIAID_PIC);
    	Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("ToUserName", openId);
		resultMap.put("FromUserName", toUserName);
		resultMap.put("CreateTime", ((int) System.currentTimeMillis()));
    	//接收图片时回复处理
    	StringBuffer contentMsg = new StringBuffer();
    	if(Constants.Number.ONE_STRING.equals(useSystemDefaultReplyPic))
    	{
     		contentMsg.append("如果有什么疑问，请联系我们的客服，客服电话：021-65252279，4008-008-739 或者可以联系我们QQ客服：4008008739");  
			resultMap.put("MsgType", Constants.MESSAGE_TEXT);
			resultMap.put("Content", contentMsg.toString());
    	}
    	else if(Constants.Number.TWO_STRING.equals(useSystemDefaultReplyPic))
    	{
    		if(Constants.Number.ONE_STRING.equals(replyTypePic))//文字信息
			{
				contentMsg.append(replayWordsPic.toString());
				resultMap.put("MsgType", Constants.MESSAGE_TEXT);
				resultMap.put("Content", contentMsg.toString());
			}
    		else if(Constants.Number.TWO_STRING.equals(replyTypePic))//图文信息
    		{
    			try {
					JSONObject weiXinMaterial = WechatUtils.getWeiXinMaterialDetail(wechatService.queryWechatToken(), imageTextListPic);
					JSONArray arrayItem = weiXinMaterial.getJSONArray("news_item");
					List<Map<String,Object>> articleList = new ArrayList<Map<String,Object>>();
					if(arrayItem.size() > 0)
					{
						for(int i = 0;i < arrayItem.size();i++)
						{
							JSONObject item = arrayItem.getJSONObject(i);
							if(item != null)
							{
								Map<String,Object> map0 = new HashMap<String,Object>();
								Map<String,Object> map = new HashMap<String,Object>();
								map.put("Title", new String(item.getString("title").getBytes("ISO-8859-1"),"UTF-8"));
								map.put("Description", new String(item.getString("digest").getBytes("ISO-8859-1"),"UTF-8"));
								map.put("PicUrl", WechatUtils.obtainWechatImgByMediaId(wechatService.queryWechatToken(),item.getString("thumb_media_id")));
								map.put("Url", new String(item.getString("url").getBytes("ISO-8859-1"),"UTF-8"));
								map0.put("item", map);
								articleList.add(map0);
							}
						}
					}
	                resultMap.put("MsgType", Constants.MESSAGE_NEWS);
	        		resultMap.put("ArticleCount", articleList.size());
	        		resultMap.put("Articles", articleList);
    			} catch (WXException e) {
					e.printStackTrace();
				}
    		}
    		else if(Constants.Number.THREE_STRING.equals(replyTypePic))//回复图片
    		{
    			resultMap.put("MsgType", Constants.MESSAGE_IMAGE);
        		Map<String,Object> parem1 = new HashMap<String,Object>();
        		parem1.put("MediaId", imageMediaIdPic);
        		resultMap.put("Image", parem1);
    		}
    	}
    	return WechatUtils.ToXml(resultMap);
	}
	/**
	 * 关注回复
	 * @param fromXml
	 * @return
	 * @throws WxPayException
	 * @throws WXException
	 * @throws UnsupportedEncodingException
	 * @throws QiniuException
	 */
	private String subscribe(Map<String,Object> fromXml) throws WxPayException, WXException, UnsupportedEncodingException, QiniuException
	{
		String openId = fromXml.get("FromUserName").toString();
		String toUserName = fromXml.get("ToUserName").toString();
		String 	eventKey = fromXml.get("EventKey").toString();
		
		String useSystemDefaultReply = cacheSetting.findSettingValue(Constants.USE_SYSTEM_DEFAULT_REPLY);
		String replyType = cacheSetting.findSettingValue(Constants.REPLY_TYPE);
    	String replayWords = cacheSetting.findSettingValue(Constants.REPLAY_WORDS);
    	String imageTextList = cacheSetting.findSettingValue(Constants.IMAGE_TEXT_LIST);
    	String imageMediaId = cacheSetting.findSettingValue(Constants.BEFOLLOWED_REPLY_MEDIAID);
    	
		String phone = "";
		if(!CmsUtils.isNullOrEmpty(eventKey))
		{
			phone = eventKey.substring(8);
		}
		InviteRecord inviteRecord = inviteRecordService.findRecordByOpenId(openId, null);
		//对于没有过邀请记录的加入邀请记录
		if(CmsUtils.isNullOrEmpty(inviteRecord)){
			if(!CmsUtils.isNullOrEmpty(phone))
			{
				Integer userId = userService.findUserIdByPhone(phone);
				inviteRecordService.inserRecord(userId,openId);
			}
		}else{
			//如果是之前取关的 更改状态
			if(2 == inviteRecord.getStatus()){
				inviteRecord.setStatus(1);
				inviteRecord.setUpdateTime(new Date());
				inviteRecordService.updateInviteRecord(inviteRecord);
			}
		}
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("ToUserName", openId);
		resultMap.put("FromUserName", toUserName);
		resultMap.put("CreateTime", ((int) System.currentTimeMillis()));
		
		//关注回复
		StringBuffer contentMsg = new StringBuffer();
		if(Constants.Number.ONE_STRING.equals(useSystemDefaultReply))
		{
			contentMsg.append("欢迎关注点点小匠").append("\n\n");  
			contentMsg.append("欢迎关注点点小匠,请点击右侧下方菜单即刻注册。");
			resultMap.put("MsgType", Constants.MESSAGE_TEXT);
			resultMap.put("Content", contentMsg.toString());
		}
		else if(Constants.Number.TWO_STRING.equals(useSystemDefaultReply))
		{
			if(Constants.Number.ONE_STRING.equals(replyType))//文字信息
			{
				contentMsg.append(replayWords.toString());
				resultMap.put("MsgType", Constants.MESSAGE_TEXT);
				resultMap.put("Content", contentMsg.toString());
			}
			else if(Constants.Number.TWO_STRING.equals(replyType))//图文信息
			{
				JSONObject weiXinMaterial = WechatUtils.getWeiXinMaterialDetail(wechatService.queryWechatToken(), imageTextList);
				JSONArray arrayItem = weiXinMaterial.getJSONArray("news_item");
				List<Map<String,Object>> articleList = new ArrayList<Map<String,Object>>();
				if(arrayItem.size() > 0)
				{
					for(int i = 0;i < arrayItem.size();i++)
					{
						JSONObject item = arrayItem.getJSONObject(i);
						if(item != null)
						{
							Map<String,Object> map0 = new HashMap<String,Object>();
							Map<String,Object> map = new HashMap<String,Object>();
							map.put("Title", new String(item.getString("title").getBytes("ISO-8859-1"),"UTF-8"));
							map.put("Description", new String(item.getString("digest").getBytes("ISO-8859-1"),"UTF-8"));
							map.put("PicUrl", WechatUtils.obtainWechatImgByMediaId(wechatService.queryWechatToken(),item.getString("thumb_media_id")));
							map.put("Url", new String(item.getString("url").getBytes("ISO-8859-1"),"UTF-8"));
							map0.put("item", map);
							articleList.add(map0);
						}
					}
				}
                resultMap.put("MsgType", Constants.MESSAGE_NEWS);
        		resultMap.put("ArticleCount", articleList.size());
        		resultMap.put("Articles", articleList);
			}
			else if(Constants.Number.THREE_STRING.equals(replyType))//回复图片
			{
        		resultMap.put("MsgType", Constants.MESSAGE_IMAGE);
        		Map<String,Object> parem1 = new HashMap<String,Object>();
        		parem1.put("MediaId", imageMediaId);
        		resultMap.put("Image", parem1);
			}
		}
		return WechatUtils.ToXml(resultMap);
	}
	
	public void unSubscribe(Map<String,Object> fromXml){
		String opendId = fromXml.get("FromUserName").toString();
		InviteRecord inviRecord = inviteRecordService.findRecordByOpenId(opendId, Constants.Number.ONE_INT);
		if(!CmsUtils.isNullOrEmpty(inviRecord)){
			inviRecord.setStatus(Constants.Number.TWO_INT);
			inviRecord.setUpdateTime(new Date());
			inviteRecordService.updateInviteRecord(inviRecord);
		}
	}
	
	private String invalidKeywordsReply(Map<String,Object> fromXml) throws WXException, WxPayException, UnsupportedEncodingException, QiniuException
	{
		String openId = fromXml.get("FromUserName").toString();
		String toUserName = fromXml.get("ToUserName").toString();
		String noKeyWordsDefaultReply = cacheSetting.findSettingValue(Constants.NO_KEYWORDS_DEFAULT_REPLY);
		String noReplyType = cacheSetting.findSettingValue(Constants.NO_REPLY_TYPE);
    	String noReplayWords = cacheSetting.findSettingValue(Constants.NO_REPLAY_WORDS);
    	String noImageTextList = cacheSetting.findSettingValue(Constants.NO_IMAGE_TEXT_LIST);
    	String noBefollowedReplyMediaId = cacheSetting.findSettingValue(Constants.NO_BEFOLLOWED_REPLY_MEDIAID);
    	Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("ToUserName", openId);
		resultMap.put("FromUserName", toUserName);
		resultMap.put("CreateTime", ((int) System.currentTimeMillis()));
    	//接收图片时回复处理
    	StringBuffer contentMsg = new StringBuffer();
    	if(CmsUtils.isNullOrEmpty(noKeyWordsDefaultReply) || Constants.Number.ONE_STRING.equals(noKeyWordsDefaultReply))
    	{
    		contentMsg.append("如果有什么疑问，请联系我们的客服，客服电话：021-65252279，4008-008-739 或者可以联系我们QQ客服：4008008739");  
			resultMap.put("MsgType", Constants.MESSAGE_TEXT);
			resultMap.put("Content", contentMsg.toString());
    	}
    	else if(Constants.Number.TWO_STRING.equals(noKeyWordsDefaultReply))
    	{
    		if(Constants.Number.ONE_STRING.equals(noReplyType))//文字信息
			{
				contentMsg.append(noReplayWords.toString());
				resultMap.put("MsgType", Constants.MESSAGE_TEXT);
				resultMap.put("Content", contentMsg.toString());
			}
    		else if(Constants.Number.TWO_STRING.equals(noReplyType))//图文信息
    		{
    			try {
					JSONObject weiXinMaterial = WechatUtils.getWeiXinMaterialDetail(wechatService.queryWechatToken(), noImageTextList);
					JSONArray arrayItem = weiXinMaterial.getJSONArray("news_item");
					List<Map<String,Object>> articleList = new ArrayList<Map<String,Object>>();
					if(arrayItem.size() > 0)
					{
						for(int i = 0;i < arrayItem.size();i++)
						{
							JSONObject item = arrayItem.getJSONObject(i);
							if(item != null)
							{
								Map<String,Object> map0 = new HashMap<String,Object>();
								Map<String,Object> map = new HashMap<String,Object>();
								map.put("Title", new String(item.getString("title").getBytes("ISO-8859-1"),"UTF-8"));
								map.put("Description", new String(item.getString("digest").getBytes("ISO-8859-1"),"UTF-8"));
								map.put("PicUrl", WechatUtils.obtainWechatImgByMediaId(wechatService.queryWechatToken(),item.getString("thumb_media_id")));
								map.put("Url", new String(item.getString("url").getBytes("ISO-8859-1"),"UTF-8"));
								map0.put("item", map);
								articleList.add(map0);
							}
						}
					}
	                resultMap.put("MsgType", Constants.MESSAGE_NEWS);
	        		resultMap.put("ArticleCount", articleList.size());
	        		resultMap.put("Articles", articleList);
    			} catch (WXException e) {
					e.printStackTrace();
				}
    		}
    		else if(Constants.Number.THREE_STRING.equals(noReplyType))//回复图片
    		{
    			resultMap.put("MsgType", Constants.MESSAGE_IMAGE);
        		Map<String,Object> parem1 = new HashMap<String,Object>();
        		parem1.put("MediaId", noBefollowedReplyMediaId);
        		resultMap.put("Image", parem1);
    		}
    	}
    	return WechatUtils.ToXml(resultMap);
	}
}
