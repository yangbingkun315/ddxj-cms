package net.zn.ddxj.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.Information;
import net.zn.ddxj.entity.InviteLink;
import net.zn.ddxj.entity.InviteRecord;
import net.zn.ddxj.entity.InviteSetting;
import net.zn.ddxj.entity.SendMessageCategory;
import net.zn.ddxj.entity.SendMessageUser;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.WechatMenu;
import net.zn.ddxj.entity.WechatSendMessage;
import net.zn.ddxj.entity.WechatSync;
import net.zn.ddxj.service.InformationService;
import net.zn.ddxj.service.InviteLinkService;
import net.zn.ddxj.service.InviteRecordService;
import net.zn.ddxj.service.InviteSettingService;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.tool.AsycService;
import net.zn.ddxj.tool.CacheSettingService;
import net.zn.ddxj.tool.WechatService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.RedisUtils;
import net.zn.ddxj.utils.wechat.CheckUtil;
import net.zn.ddxj.utils.wechat.WXException;
import net.zn.ddxj.utils.wechat.WechatUtils;
import net.zn.ddxj.utils.wechat.WxPayException;
import net.zn.ddxj.vo.RequestVo;

import org.apache.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;

@RestController
@Slf4j
public class WechatController {

	@Autowired
	public RedisUtils redisUtils;
	@Autowired
	private WechatService wechatService;
	@Autowired
	private InformationService informationService;
	@Autowired
	private InviteLinkService inviteLinkService;
	@Autowired
	private InviteRecordService inviteRecordService;
	@Autowired
	private InviteSettingService inviteSettingService;
	@Autowired
	private CacheSettingService cacheSetting;
	@Autowired
	private UserService userService;
	@Autowired
	private AsycService asycService;
	
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
		log.info(signature + "####" + timestamp + "####" + nonce + "####" + echostr + "####");
		log.info("#### TOKEN SUCCESS ####");
		if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
			return echostr;
		}
		return null;
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
	@RequestMapping("/query/wechat/user/info.ddxj")
	public ResponseBase getUserMessage(HttpServletRequest request, HttpServletResponse response,String code) throws ParseException, IOException {
		ResponseBase result = ResponseBase.getInitResponse();
		try
		{
			JSONObject jsAccessTokenJSON =WechatUtils.retrieveAccessTokenByCode(code);
			JSONObject userMessage = WechatUtils.retrieveUserInfoByAccessToken(jsAccessTokenJSON.getString("access_token"), jsAccessTokenJSON.getString("openid"));
			result.push("wechatUser", userMessage);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.SUCCESS);
			return result;
			
		}
		catch(WXException e)
		{
			result.push("ret", "1");
			result.push("code",e.getRet());
		}
		
		result.setResponse(Constants.FALSE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("获取微信用户信息失败");
		return result;
	}
	@RequestMapping("/query/news/view/list.ddxj")
	public ResponseBase queryNewsViewList(HttpServletRequest request, HttpServletResponse response,Integer currentPage) throws ParseException, IOException, IllegalAccessException
	{
		ResponseBase result = ResponseBase.getInitResponse();
		try 
		{
			if(currentPage == null)
			{
				currentPage = Constants.Number.ONE_INT;
			}
			JSONObject weiXinMaterial = WechatUtils.getWeiXinMaterial(wechatService.queryWechatToken(), currentPage, 10);
			JSONArray array = new JSONArray();
			JSONArray arrayItem = weiXinMaterial.getJSONArray("item");
			String total = weiXinMaterial.getString("total_count");
			if(arrayItem.size() > 0)
			{
				for(int i = 0;i < arrayItem.size();i++)
				{
					JSONObject item = arrayItem.getJSONObject(i);
					JSONObject newObject = new JSONObject();
					newObject.put("media_id", item.getString("media_id"));
					newObject.put("title", new String(item.getJSONObject("content").getJSONArray("news_item").getJSONObject(0).getString("title").getBytes("ISO-8859-1"),"UTF-8"));
					newObject.put("digest", new String(item.getJSONObject("content").getJSONArray("news_item").getJSONObject(0).getString("digest").getBytes("ISO-8859-1"),"UTF-8"));
					array.add(newObject);
				}
			}
			result.push("newsList", array);
			result.push("total", total);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("获取图文消息成功");
			
		} 
		catch (WXException e) 
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("获取图文消息失败");
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping("/query/news/view/details/list.ddxj")
	public ResponseBase queryNewsView(HttpServletRequest request, HttpServletResponse response,String media_id) throws ParseException, IOException, IllegalAccessException
	{
		ResponseBase result = ResponseBase.getInitResponse();
		try 
		{
			JSONObject weiXinMaterial = WechatUtils.getWeiXinMaterialDetail(wechatService.queryWechatToken(), media_id);
			JSONArray array = new JSONArray();
			JSONArray arrayItem = weiXinMaterial.getJSONArray("news_item");
			if(arrayItem.size() > 0)
			{
				for(int i = 0;i < arrayItem.size();i++)
				{
					JSONObject item = arrayItem.getJSONObject(i);
					if(item != null)
					{
						JSONObject newObject = new JSONObject();
						newObject.put("title", new String(item.getString("title").getBytes("ISO-8859-1"),"UTF-8"));
						newObject.put("thumb_media_id", item.getString("thumb_media_id"));
						newObject.put("show_cover_pic", new String(item.getString("show_cover_pic").getBytes("ISO-8859-1"),"UTF-8"));
						newObject.put("author", new String(item.getString("author").getBytes("ISO-8859-1"),"UTF-8"));
						newObject.put("digest",  new String(item.getString("digest").getBytes("ISO-8859-1"),"UTF-8"));
						newObject.put("content", new String(item.getString("content").getBytes("ISO-8859-1"),"UTF-8"));
						newObject.put("url", new String(item.getString("url").getBytes("ISO-8859-1"),"UTF-8"));
						newObject.put("content_source_url", new String(item.getString("content").getBytes("ISO-8859-1"),"UTF-8"));
						//根据素材ID查询是否有同步记录
						if(!CmsUtils.isNullOrEmpty(item.getString("thumb_media_id")))
						{
							Integer sysnCount = wechatService.queryWechatSyncById(item.getString("thumb_media_id"));
							newObject.put("is_sync", String.valueOf(sysnCount));
						}
						array.add(newObject);
					}
				}
			}
			result.push("newsDetailList", array);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("获取图文消息成功");
		}
		catch (WXException e) 
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("获取图文消息失败");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
     * 同步微信素材数据到资讯
     *
     * @param request
     * @param response
     * @author fancunxin
	 * @throws WXException 
	 * @throws QiniuException 
     * @throws ClientException 
     */
    @RequestMapping(value = "/add/wechat/sync/information.ddxj")
    public ResponseBase queryWechatUserList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo) throws IllegalAccessException, QiniuException, WXException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	Integer sysnCount = wechatService.queryWechatSyncById(requestVo.getMediaId());
    	if(sysnCount != null && sysnCount > 0)
    	{
    		result.setResponse(Constants.TRUE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg("此素材已同步");
        	return result;
    	}
    	
    	String imgUrl = WechatUtils.obtainWechatImgByMediaId(wechatService.queryWechatToken(),requestVo.getMediaId());
    	
    	Information info = new Information();
    	info.setImageOne(imgUrl);
    	info.setInfoType(4);
    	info.setStick(2);
    	info.setStringContent(requestVo.getInfoContent().getBytes());
    	info.setAuthor(!CmsUtils.isNullOrEmpty(requestVo.getAuthor())?requestVo.getAuthor():"点点小匠");
    	info.setInfoTitle(requestVo.getInfoTitle());
    	info.setInfoSummary(requestVo.getInfoSummary());
    	info.setStartTime(new Date());
    	info.setEndTime(DateUtils.getFromAmountMonthToDay(new Date(), Constants.Number.ONE_INT));
    	info.setUpdateTime(new Date());
    	info.setCreateTime(new Date());
		informationService.insertSelective(info);
		
		WechatSync sync = new WechatSync();
		sync.setThumbMediaId(requestVo.getMediaId());
		sync.setCreateTime(new Date());
		wechatService.addWechatSync(sync);
		
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("同步成功");
    	return result;
    }
    /**
     * 查询永久二维码表
     * @param request
     * @param response
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/manager/invite/link/list.ddxj")
    public ResponseBase queryInviteLinkRecord(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo){
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
    	List<InviteLink> inviteLinkList = inviteLinkService.queryInviteLinkRecord(requestVo);
    	PageInfo<InviteLink> pageInfo = new PageInfo<InviteLink>(inviteLinkList);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.push("inviteLinkList", pageInfo);
    	result.setResponseMsg("查询成功");
    	return result;
    }
    /**
     * 查询邀请列表
     * @param request
     * @param response
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/manager/invite/record/list.ddxj")
    public ResponseBase queryInviteRecord(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo){
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
    	List<InviteRecord> inviteList = inviteRecordService.selectInviteRecord(requestVo);
    	PageInfo<InviteRecord> pageInfo = new PageInfo<InviteRecord>(inviteList);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.push("inviteList", pageInfo);
    	result.setResponseMsg("查询成功");
    	return result;
    }
    /**
     * 查询weChat菜单
     * @param request
     * @param response
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/manager/query/menu/list.ddxj")
    public ResponseBase queryAllMenu(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo){
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
    	List<WechatMenu> weChatMenuList = wechatService.queryAllWeChatMenu();
    	PageInfo<WechatMenu> pageInfo = new PageInfo<WechatMenu>(weChatMenuList);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.push("weChatMenuList", pageInfo);
    	result.setResponseMsg("查询成功");
    	return result;
    }
    /**
     * 删除weChat菜单
     * @param request
     * @param response
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/manager/delete/menu/record.ddxj")
    public ResponseBase deleteMenu(HttpServletRequest request, HttpServletResponse response,Integer menuId){
    	ResponseBase result = ResponseBase.getInitResponse();
    	wechatService.deleteMenuByMenuId(menuId);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("删除成功");
    	return result;
    }
    /**
     * 查询微信所有的一级菜单
     * @param request
     * @param response
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/manager/query/parent/list.ddxj")
    public ResponseBase queryOneLevelMenu(HttpServletRequest request, HttpServletResponse response){
    	ResponseBase result = ResponseBase.getInitResponse();
    	List<WechatMenu> menuParentList = wechatService.queryOneLevelMenu();
    	PageInfo<WechatMenu> pageInfo = new PageInfo<WechatMenu>(menuParentList);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.push("menuParentList", pageInfo);
    	result.setResponseMsg("查询成功");
    	return result;
    }
    /**
     * 查询菜单详情
     * @param request
     * @param response
     * @param menuId
     * @return
     * @throws IllegalAccessException
     */
	@RequestMapping(value = "/manager/query/weChatMenu/details.ddxj")
	public ResponseBase queryMenuDetails(HttpServletRequest request, HttpServletResponse response,Integer menuId) throws IllegalAccessException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		WechatMenu menu = wechatService.queryMenuByMenuId(menuId);
		result.push("wechatMenu", menu);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询成功");
		return result;
	}
	/**
	 * 更新详情
	 * @param request
	 * @param response
	 * @param id
	 * @param pid
	 * @param name
	 * @param url
	 * @param icon
	 * @param sort
	 * @return
	 */
	@RequestMapping(value = "/manager/update/weChatMenu.ddxj")
	public ResponseBase updateMenu(HttpServletRequest request, HttpServletResponse response,Integer id,String menuName,Integer clickAction,Integer indexNumber,String menuUrl,String menuKey,Integer parentId,
			String replyText,String replyImage) 
	{
		return wechatService.updateWeChatMenu(id,menuName,clickAction,indexNumber,menuUrl,menuKey,parentId,replyText, replyImage);
	}
	
	@RequestMapping(value = "/manager/push/custom/menu.ddxj")
	public ResponseBase updateMenu(HttpServletRequest request, HttpServletResponse response) 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		result.push("status", wechatService.updateCustomerMenu());
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("success");
		return result;
	}
    
    /**
     * 分销设置-查询
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value="/invite/record/setting/find.ddxj")
    public ResponseBase findInviteRecordSetting(HttpServletRequest request, HttpServletResponse response,Integer pageNum,Integer pageSize,String title,String channel,String startTime,String endTime)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(pageNum, pageSize);
    	List<InviteSetting> settingList = inviteSettingService.queryInviteSetting(title,channel,startTime,endTime);
    	PageInfo<InviteSetting> pageInfo = new PageInfo<InviteSetting>(settingList);
    	result.push("inviteList", pageInfo);
    	result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("success");
    	return result;
    }
    
    /**
     * 分销设置-查询
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value="/invite/record/setting/findId.ddxj")
    public ResponseBase findByInviteId(HttpServletRequest request, HttpServletResponse response,Integer id)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	InviteSetting inviteSetting = inviteSettingService.findById(id);
    	result.push("invite", inviteSetting);
    	result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("success");
    	return result;
    }
    
    /**
     * 分销设置-修改
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value="/invite/record/setting/save.ddxj")
    public ResponseBase saveInviteRecordSetting(HttpServletRequest request, HttpServletResponse response,String title,String content,String img,String linkUrl,Integer type,Integer flag,Integer id)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	InviteSetting setting = new InviteSetting();
    	
    	if(!CmsUtils.isNullOrEmpty(id)){
    		setting.setId(id);
    	}
    	setting.setTitle(title);
    	setting.setContent(content);
    	setting.setLinkUrl(linkUrl);
    	setting.setImg(img);
    	setting.setCreateTime(new Date());
		setting.setUpdateTime(new Date());
		setting.setFlag(Constants.Number.ONE_INT);
		setting.setType(type);
		inviteSettingService.saveSetting(setting);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("设置成功");
    	log.info("#####################设置成功#####################");
    	return result;
    }
    
    @RequestMapping("/invite/record/delet.ddxj")
    public ResponseBase deleteInvite(Integer id){
    	ResponseBase result = ResponseBase.getInitResponse();
    	inviteSettingService.deleteInvite(id);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("删除成功");
    	return result;
    }
    
    /**
     * 自定义菜单-基本设置-查询
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws UnsupportedEncodingException 
     * @throws ClientException 
     */
    @RequestMapping(value="/wechat/menu/setting/find.ddxj")
    public ResponseBase findWechatMenuSetting(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	result.push("useSystemDefaultReply", Integer.valueOf(cacheSetting.findSettingValue(Constants.USE_SYSTEM_DEFAULT_REPLY)));
    	result.push("replyType", Integer.valueOf(cacheSetting.findSettingValue(Constants.REPLY_TYPE)));
    	result.push("replayWords", cacheSetting.findSettingValue(Constants.REPLAY_WORDS));
    	result.push("imageTextList", cacheSetting.findSettingValue(Constants.IMAGE_TEXT_LIST));
    	result.push("befollowedReplyImg", cacheSetting.findSettingValue(Constants.BEFOLLOWED_REPLY_IMG));
    	
    	try 
		{
			JSONObject weiXinMaterial = WechatUtils.getWeiXinMaterial(wechatService.queryWechatToken(), 1, 20);
			JSONArray array = new JSONArray();
			JSONArray arrayItem = weiXinMaterial.getJSONArray("item");
			if(arrayItem.size() > 0)
			{
				for(int i = 0;i < arrayItem.size();i++)
				{
					JSONObject item = arrayItem.getJSONObject(i);
					JSONObject newObject = new JSONObject();
					newObject.put("media_id", item.getString("media_id"));
					newObject.put("title", new String(item.getJSONObject("content").getJSONArray("news_item").getJSONObject(0).getString("title").getBytes("ISO-8859-1"),"UTF-8"));
					newObject.put("digest", new String(item.getJSONObject("content").getJSONArray("news_item").getJSONObject(0).getString("digest").getBytes("ISO-8859-1"),"UTF-8"));
					array.add(newObject);
				}
			}
			result.push("newsList", array);
		} 
		catch (WXException e) {}
    	
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("查询基本设置成功");
    	log.info("#####################查询基本设置成功#####################");
    	return result;
    }
    
    /**
     * 自定义菜单-基本设置-修改
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws WXException 
     * @throws ClientException 
     */
    @RequestMapping(value="/wechat/menu/setting/update.ddxj")
    public ResponseBase updateWechatMenuSetting(HttpServletRequest request, HttpServletResponse response,String useSystemDefaultReply,String replyType,String replayWords,String imageTextList,String befollowedReplyImg) throws WXException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	cacheSetting.saveSetting(Constants.USE_SYSTEM_DEFAULT_REPLY, useSystemDefaultReply);
    	cacheSetting.saveSetting(Constants.REPLY_TYPE, replyType);
    	cacheSetting.saveSetting(Constants.REPLAY_WORDS, replayWords);
    	cacheSetting.saveSetting(Constants.IMAGE_TEXT_LIST, imageTextList);
    	cacheSetting.saveSetting(Constants.BEFOLLOWED_REPLY_IMG, befollowedReplyImg);
    	
    	String img = cacheSetting.findSettingValue(Constants.BEFOLLOWED_REPLY_IMG);
    	
    	if(CmsUtils.isNullOrEmpty(befollowedReplyImg))
		{
			befollowedReplyImg = "http://res-test.diandxj.com/Fgz_XN_PfpcoLflGWJFY475Dqwcm";
		}
    	if(befollowedReplyImg != img)
    	{
    		JSONObject obj = WechatUtils.uploadWeiXinImage(wechatService.queryWechatToken(), befollowedReplyImg);
    		cacheSetting.saveSetting(Constants.BEFOLLOWED_REPLY_MEDIAID, obj.getString("media_id"));
    	}
    	
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("设置成功");
    	log.info("#####################设置成功#####################");
    	return result;
    }
    
    /**
     * 客服群发
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws UnsupportedEncodingException 
     * @throws WXException 
     * @throws ClientException 
     */
    @RequestMapping(value="/wechat/send/message/service/query.ddxj")
    public ResponseBase wechatSendMassageService_FIND(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	try 
		{
			JSONObject weiXinMaterial = WechatUtils.getWeiXinMaterial(wechatService.queryWechatToken(), 1, 20);
			JSONArray array = new JSONArray();
			JSONArray arrayItem = weiXinMaterial.getJSONArray("item");
			if(arrayItem.size() > 0)
			{
				for(int i = 0;i < arrayItem.size();i++)
				{
					JSONObject item = arrayItem.getJSONObject(i);
					JSONObject newObject = new JSONObject();
					newObject.put("media_id", item.getString("media_id"));
					newObject.put("title", new String(item.getJSONObject("content").getJSONArray("news_item").getJSONObject(0).getString("title").getBytes("ISO-8859-1"),"UTF-8"));
					newObject.put("digest", new String(item.getJSONObject("content").getJSONArray("news_item").getJSONObject(0).getString("digest").getBytes("ISO-8859-1"),"UTF-8"));
					array.add(newObject);
				}
			}
			result.push("newsList", array);
		} 
		catch (WXException e) {}
    	
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("查询客服群发设置成功");
    	log.info("#####################查询客服群发设置成功#####################");
    	return result;
    }
    
    /**
     * 客服群发
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws UnsupportedEncodingException 
     * @throws QiniuException 
     * @throws WXException 
     * @throws ClientException 
     */
    @RequestMapping(value="/wechat/send/message/service/send.ddxj")
    public ResponseBase wechatSendMassageService_SEND(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo) throws UnsupportedEncodingException, QiniuException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
		try
		{
			String accessToken = wechatService.queryWechatToken();
			String mediaId = null;
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			if(requestVo.getMassType() == 1)//文本
			{
				if(CmsUtils.isNullOrEmpty(requestVo.getMassContent()))
				{
					result.setResponse(Constants.FALSE);
			    	result.setResponseCode(Constants.SUCCESS_200);
			    	result.setResponseMsg("请填写回复文本");
			    	return result;
				}
				
			}
			else if(requestVo.getMassType() == 2)//图文素材
			{
				if(CmsUtils.isNullOrEmpty(requestVo.getMediaId()))
				{
					result.setResponse(Constants.FALSE);
			    	result.setResponseCode(Constants.SUCCESS_200);
			    	result.setResponseMsg("请选择图文素材");
			    	return result;
				}
				else
				{
					JSONObject weiXinMaterial = WechatUtils.getWeiXinMaterialDetail(wechatService.queryWechatToken(), requestVo.getMediaId());
					JSONArray arrayItem = weiXinMaterial.getJSONArray("news_item");
					if(arrayItem.size() > 0)
					{
						for(int i = 0;i < arrayItem.size();i++)
						{
							JSONObject item = arrayItem.getJSONObject(i);
							if(item != null)
							{
								Map<String,Object> data = new HashMap<String,Object>();
								data.put("title", new String(item.getString("title").getBytes("ISO-8859-1"),"UTF-8"));
								data.put("description", new String(item.getString("digest").getBytes("ISO-8859-1"),"UTF-8"));
								data.put("picurl", WechatUtils.obtainWechatImgByMediaId(wechatService.queryWechatToken(),item.getString("thumb_media_id")));
								data.put("url", new String(item.getString("url").getBytes("ISO-8859-1"),"UTF-8"));
								list.add(data);
							}
						}
					}
				}
			}
			else if(requestVo.getMassType() == 3)//图片
			{
				if(CmsUtils.isNullOrEmpty(requestVo.getImgUrl()))
				{
					result.setResponse(Constants.FALSE);
			    	result.setResponseCode(Constants.SUCCESS_200);
			    	result.setResponseMsg("请上传图文");
			    	return result;
				}
				try
				{
					JSONObject obj = WechatUtils.uploadWeiXinImage(wechatService.queryWechatToken(), requestVo.getImgUrl());
		    		mediaId = obj.getString("media_id");
				}
				catch(Exception e){}
			}
			
			//传入PreviewOpenId时会执行单人推送
			if(!CmsUtils.isNullOrEmpty(requestVo.getPreviewOpenId()))
			{
				WechatUtils.sendWechatCustomServiceMessage(accessToken, requestVo.getPreviewOpenId(), requestVo.getMassContent(), requestVo.getMassType(), mediaId,list);
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg("群发成功");
		    	return result;
			}
			
			requestVo.setPageSize(999999);
			requestVo.setPageNum(1);
			
			WechatSendMessage log = new WechatSendMessage();
			
			String[] category = null;
			if(!CmsUtils.isNullOrEmpty(requestVo.getCategorys()))
			{
				if(requestVo.getCategorys().indexOf(",") >= -1)
				{
					category = requestVo.getCategorys().split(",");
				}
				else
				{
					category = new String[1];
					category[0] = requestVo.getCategorys();
				}
				
				if(category.length > 0)
				{
					requestVo.setCategoryId1(Integer.valueOf(category[0]));
				}
				if(category.length > 1)
				{
					requestVo.setCategoryId2(Integer.valueOf(category[1]));
				}
				if(category.length > 2)
				{
					requestVo.setCategoryId3(Integer.valueOf(category[2]));
				}
			}
			
			if(!CmsUtils.isNullOrEmpty(requestVo.getProvince()))
			{
//				requestVo.setProvince(requestVo.getProvince().replace("省", "").replace("市", "").trim());
				requestVo.setProvince(requestVo.getProvince().trim());
			}
			if(!CmsUtils.isNullOrEmpty(requestVo.getCity()))
			{
				requestVo.setCity(requestVo.getCity().trim());
			}
			//根据OpenID列表群发
			List<User> userFollower = userService.queryUserFollowerByTerm(requestVo);
			if(CmsUtils.isNullOrEmpty(userFollower))
			{
				result.setResponse(Constants.FALSE);
		    	result.setResponseCode(Constants.SUCCESS_200);
		    	result.setResponseMsg("当前条件下未匹配到用户，不支持群发");
		    	return result;
			}
			if(userFollower.size() == 1)
			{
				result.setResponse(Constants.FALSE);
		    	result.setResponseCode(Constants.SUCCESS_200);
		    	result.setResponseMsg("当前条件下只匹配到一个用户，不支持群发"); 
		    	return result;
			}
			
			log.setSendType(1);
			log.setMassPlatform(4);
			log.setProvince(requestVo.getProvince());
			log.setCity(requestVo.getCity());
			log.setSex(requestVo.getMsgSex());
			log.setMassObject(4);
			log.setStartAge(DateUtils.getDate(requestVo.getStartAge(), "yyyy-MM-dd"));
			log.setEndAge(DateUtils.getDate(requestVo.getEndAge(), "yyyy-MM-dd"));
			log.setCreateTime(new Date());
			log.setUpdateTime(new Date());
			
			if(requestVo.getMassType() == 1)
			{
				log.setMassContent("文字：" + requestVo.getMassContent());
				log.setMassType(1);
			}
			else if(requestVo.getMassType() == 2)
			{
				for(Map<String, Object> o : list)
				{
					log.setMassContent( "图文消息："+ o.get("title"));
					break ;
				}
				log.setMassType(2);
			}
			else if(requestVo.getMassType() == 3)
			{
				log.setMassContent("图片：<img src='" + requestVo.getImgUrl() + "'/>");
				log.setMassType(3);
			}
			wechatService.addWechatSendMessage(log);
			
			//添加分类
			if(!CmsUtils.isNullOrEmpty(category) && category.length > 0)
			{
				for(String categoryId : category)
				{
					SendMessageCategory c = new SendMessageCategory();
					c.setCategoryId(Integer.valueOf(categoryId));
					c.setMessageId(log.getId());
					c.setCreateTime(new Date());
					c.setUpdateTime(new Date());
					wechatService.addSendMessageCategory(c);
				}
			}
			
			new customServiceMessageBatchThread(userFollower,requestVo.getMassContent(),requestVo.getMassType(),mediaId,list,log.getId(),accessToken).start();
			
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg("群发成功");
	    	return result;
		}
		catch (WXException e) 
		{
			result.setResponse(Constants.FALSE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg("操作失败，错误码为：" + e.getRet());
	    	return result;
		}
    }
    
    private class customServiceMessageBatchThread extends Thread
	{
    	private List<User> userFollower;
		private String textMessage;
		private int massContent;
		private String mediaId;
		private List<Map<String,Object>> list;
		private int logId;
		private String accessToken;
		
		public customServiceMessageBatchThread(List<User> userFollower, String textMessage,int massContent, String mediaId,List<Map<String, Object>> list, int logId,String accessToken) 
		{
			this.userFollower = userFollower;
			this.textMessage = textMessage;
			this.massContent = massContent;
			this.mediaId = mediaId;
			this.list = list;
			this.logId = logId;
			this.accessToken = accessToken;
		}

		@Override
		public void run()
		{
			int errorCount = 0;
			if(!CmsUtils.isNullOrEmpty(userFollower))
			{
				for(int i = 0;i < userFollower.size();i++)
				{
					SendMessageUser user = new SendMessageUser();
					try
					{
						JSONObject ret = WechatUtils.sendWechatCustomServiceMessage(accessToken,userFollower.get(i).getOpenid(),textMessage,massContent,mediaId,list);
						
						user.setUserId(userFollower.get(i).getId());
						user.setMessageId(logId);
						if(ret.getInt("errcode") == 0)
						{
							user.setSendType(1);
						}
						else
						{
							user.setSendType(2);
						}
						user.setSendTime(new Date());
						user.setCreateTime(new Date());
						user.setUpdateTime(new Date());
					}
					catch (WXException e)
					{
						errorCount++;
						user.setSendType(2);
					}
					catch(Exception e)
					{
						errorCount++;
						user.setSendType(2);
					}
					wechatService.addSendMessageUser(user);
				}
			}
		}
	}
    
    /**
     * 高级群发
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws UnsupportedEncodingException 
     * @throws WXException 
     * @throws ClientException 
     */
    @RequestMapping(value="/wechat/senior/mass/message/query.ddxj")
    public ResponseBase wechatSeniorMassage_FIND(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	String token = wechatService.queryWechatToken();
    	
    	try 
    	{
    		JSONArray array = findWechatMsgList();
    		result.push("tagsList", array);
		} catch (WXException f) {}
    	
    	try 
		{
			JSONObject weiXinMaterial = WechatUtils.getWeiXinMaterial(token, 1, 20);
			JSONArray array = new JSONArray();
			JSONArray arrayItem = weiXinMaterial.getJSONArray("item");
			if(arrayItem.size() > 0)
			{
				for(int i = 0;i < arrayItem.size();i++)
				{
					JSONObject item = arrayItem.getJSONObject(i);
					JSONObject newObject = new JSONObject();
					newObject.put("media_id", item.getString("media_id"));
					newObject.put("title", new String(item.getJSONObject("content").getJSONArray("news_item").getJSONObject(0).getString("title").getBytes("ISO-8859-1"),"UTF-8"));
					newObject.put("digest", new String(item.getJSONObject("content").getJSONArray("news_item").getJSONObject(0).getString("digest").getBytes("ISO-8859-1"),"UTF-8"));
					array.add(newObject);
				}
			}
			result.push("newsList", array);
		} 
		catch (WXException e) {}
    	
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("查询高级群发成功");
    	log.info("#####################查询高级群发成功#####################");
    	return result;
    }
    
    /**
     * 高级群发
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws UnsupportedEncodingException 
     * @throws QiniuException 
     * @throws WXException 
     * @throws ClientException 
     */
    @RequestMapping(value="/wechat/senior/mass/message/send.ddxj")
    public ResponseBase wechatSeniorMassage_SEND(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo) throws UnsupportedEncodingException, QiniuException
    {	
    	ResponseBase result = ResponseBase.getInitResponse();
    	
		try
		{
			RequestVo vo = new RequestVo();
			vo.setSendType(2);
			List<WechatSendMessage> totalCount = wechatService.queryWechatSendMessageList(requestVo);
			if(!CmsUtils.isNullOrEmpty(totalCount) && totalCount.size() >= 100)
			{
				result.setResponse(Constants.FALSE);
		    	result.setResponseCode(Constants.SUCCESS_200);
		    	result.setResponseMsg("因微信规定，服务号每日只可群发100次消息，不能继续发送！");
		    	return result;
			}
			
			String accessToken = wechatService.queryWechatToken();
			String mediaId = null;
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			if(requestVo.getMassType() == 1)//文本
			{
				if(CmsUtils.isNullOrEmpty(requestVo.getMassContent()))
				{
					result.setResponse(Constants.FALSE);
			    	result.setResponseCode(Constants.SUCCESS_200);
			    	result.setResponseMsg("请填写回复文本");
			    	return result;
				}
				
			}
			else if(requestVo.getMassType() == 2)//图文素材
			{
				if(CmsUtils.isNullOrEmpty(requestVo.getMediaId()))
				{
					result.setResponse(Constants.FALSE);
			    	result.setResponseCode(Constants.SUCCESS_200);
			    	result.setResponseMsg("请选择图文素材");
			    	return result;
				}
				else
				{
					JSONObject weiXinMaterial = WechatUtils.getWeiXinMaterialDetail(wechatService.queryWechatToken(), requestVo.getMediaId());
					JSONArray arrayItem = weiXinMaterial.getJSONArray("news_item");
					if(arrayItem.size() > 0)
					{
						for(int i = 0;i < arrayItem.size();i++)
						{
							JSONObject item = arrayItem.getJSONObject(i);
							if(item != null)
							{
								Map<String,Object> data = new HashMap<String,Object>();
								data.put("title", new String(item.getString("title").getBytes("ISO-8859-1"),"UTF-8"));
								data.put("description", new String(item.getString("digest").getBytes("ISO-8859-1"),"UTF-8"));
								data.put("picurl", WechatUtils.obtainWechatImgByMediaId(wechatService.queryWechatToken(),item.getString("thumb_media_id")));
								data.put("url", new String(item.getString("url").getBytes("ISO-8859-1"),"UTF-8"));
								list.add(data);
							}
						}
					}
				}
			}
			else if(requestVo.getMassType() == 3)//图片
			{
				if(CmsUtils.isNullOrEmpty(requestVo.getImgUrl()))
				{
					result.setResponse(Constants.FALSE);
			    	result.setResponseCode(Constants.SUCCESS_200);
			    	result.setResponseMsg("请上传图文");
			    	return result;
				}
				try
				{
					JSONObject obj = WechatUtils.uploadWeiXinImage(wechatService.queryWechatToken(), requestVo.getImgUrl());
		    		mediaId = obj.getString("media_id");
				}
				catch(Exception e){}
			}
			
//			if(!CmsUtils.isNullOrEmpty(requestVo.getPreviewOpenId()))
//			{
//				 WechatUtils.wechatPreViewMessage(accessToken,requestVo.getPreviewOpenId(), mediaId, requestVo.getMassType(), requestVo.getMassContent());
//				 result.setResponse(Constants.TRUE);
//				 result.setResponseCode(Constants.SUCCESS_200);
//				 result.setResponseMsg("发送预览成功，请查看手机微信");
//				 return result;
//			}
			
			requestVo.setPageSize(999999);
			requestVo.setPageNum(1);
			
			String[] category = null;
			if(!CmsUtils.isNullOrEmpty(requestVo.getCategorys()))
			{
				if(requestVo.getCategorys().indexOf(",") >= Constants.Number.ZERO_INT)
				{
					category = requestVo.getCategorys().split(",");
				}
				else
				{
					category = new String[Constants.Number.ONE_INT];
					category[0] = requestVo.getCategorys();
				}
				
				if(category.length > 0)
				{
					requestVo.setCategoryId1(Integer.valueOf(category[0]));
				}
				if(category.length > 1)
				{
					requestVo.setCategoryId2(Integer.valueOf(category[Constants.Number.ONE_INT]));
				}
				if(category.length > 2)
				{
					requestVo.setCategoryId3(Integer.valueOf(category[2]));
				}
			}
			
			if(!CmsUtils.isNullOrEmpty(requestVo.getProvince()))
			{
//				requestVo.setProvince(requestVo.getProvince().replace("省", "").replace("市", "").trim());
				requestVo.setProvince(requestVo.getProvince().trim());
			}
			
			if(CmsUtils.isNullOrEmpty(requestVo.getTagId()))
			{
				//根据OpenID列表群发
				List<User> userFollower = userService.queryUserFollowerByTerm(requestVo);
				if(CmsUtils.isNullOrEmpty(userFollower))
				{
					result.setResponse(Constants.FALSE);
			    	result.setResponseCode(Constants.SUCCESS_200);
			    	result.setResponseMsg("当前条件下未匹配到用户，不支持群发");
			    	return result;
				}
				if(userFollower.size() == Constants.Number.ONE_INT)
				{
					result.setResponse(Constants.FALSE);
			    	result.setResponseCode(Constants.SUCCESS_200);
			    	result.setResponseMsg("当前条件下只匹配到一个用户，不支持群发");
			    	return result;
				}
				
				new customMassMessageBatchThread(userFollower, list, category, mediaId, requestVo, accessToken).start();
				
				result.setResponse(Constants.TRUE);
		    	result.setResponseCode(Constants.SUCCESS_200);
		    	result.setResponseMsg("群发成功");
		    	return result;
			}
//			else if(!CmsUtils.isNullOrEmpty(requestVo.getTagId()))
//			{
//				if(mediaId == null)
//				{
//					mediaId = requestVo.getMediaId();
//				}
//				//标签接口
//				JSONObject ret = WechatUtils.wechatMassMessage(accessToken, requestVo.getTagId(), requestVo.getMassType(), requestVo.getMassContent(), mediaId);
//				
//				int totalNumber = 0;
//				
//				JSONArray array = findWechatMsgList();
//				for(int i = 0; i < array.size(); i++)
//				{
//					JSONObject o = array.getJSONObject(i);
//					int id = o.getInt("id");
//					if(id == requestVo.getTagId())
//					{
//						totalNumber = o.getInt("count");
//					}
//				}
//				
//				WechatSendMessage log = new WechatSendMessage();
//				log.setTotalCount(totalNumber);
//				log.setSuccessCount(totalNumber);
//				log.setMediaId(ret.getString("msg_id"));
//				log.setSendType(2);
//				log.setCreatetime(new Date());
//				if(requestVo.getMassType() == Constants.Number.ONE_INT)
//				{
//					log.setMassContent("文字：" + requestVo.getMassContent());
//					log.setMassType(Constants.Number.ONE_INT);
//				}
//				else if(requestVo.getMassType() == 2)
//				{
//					for(Map<String, Object> o : list)
//					{
//						log.setMassContent( "图文消息："+ o.get("title"));
//						break ;
//					}
//					log.setMassType(2);
//					log.setMediaId(mediaId);
//				}
//				else if(requestVo.getMassType() == 3)
//				{
//					log.setMassContent("图片：<img src='" + requestVo.getImgUrl() + "'/>");
//					log.setMassType(3);
//				}
//				wechatService.addWechatSendMessage(log);
//				
//				result.setResponse(Constants.TRUE);
//				result.setResponseCode(Constants.SUCCESS_200);
//				result.setResponseMsg("群发成功");
//				return result;
//			}
		}
		catch (WXException e) 
		{
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg("群发失败，错误码为：" + e.getRet());
	    	return result;
		}
		
		return null;
	}
    
    
    private class customMassMessageBatchThread extends Thread
	{
    	private List<User> userFollower;
		private List<Map<String,Object>> list;
		private String[] category;
		private String mediaId;
		private RequestVo requestVo;
		private String accessToken;
		
		public customMassMessageBatchThread(List<User> userFollower,List<Map<String,Object>> list,String[] category,String mediaId,RequestVo requestVo,String accessToken) 
		{
			this.userFollower = userFollower;
			this.list = list;
			this.category = category;
			this.mediaId = mediaId;
			this.requestVo = requestVo;
			this.accessToken = accessToken;
		}

		@Override
		public void run()
		{
			int errorCount = 0;
			if(!CmsUtils.isNullOrEmpty(userFollower))
			{
				
				for(int i = 0;i < userFollower.size();i = i + 10000)
				{
					int from = i;
					int to = i + 10000;
					if(to > userFollower.size())
					{
						to = userFollower.size();
					}
					List<User> sub = userFollower.subList(from, to);
					
					if(sub.size() > Constants.Number.ONE_INT)
					{
						List<String> openIdList = new ArrayList<>();
						for(User u : sub)
						{
							openIdList.add(u.getOpenid());
						}
						
						try {
							JSONObject ret = WechatUtils.wechatByOpenIdMassMessage(accessToken, openIdList, mediaId, requestVo.getMassType(), requestVo.getMassContent());
						
							WechatSendMessage log = new WechatSendMessage();
							log.setSendType(2);
							log.setMassPlatform(4);
							log.setProvince(requestVo.getProvince());
							log.setCity(requestVo.getCity());
							log.setSex(requestVo.getMsgSex());
							log.setMassObject(4);
							log.setStartAge(DateUtils.getDate(requestVo.getStartAge(), "yyyy-MM-dd"));
							log.setEndAge(DateUtils.getDate(requestVo.getStartAge(), "yyyy-MM-dd"));
							log.setCreateTime(new Date());
							log.setUpdateTime(new Date());
							
							if(requestVo.getMassType() == Constants.Number.ONE_INT)
							{
								log.setMassContent("文字：" + requestVo.getMassContent());
								log.setMassType(Constants.Number.ONE_INT);
							}
							else if(requestVo.getMassType() == 2)
							{
								for(Map<String, Object> o : list)
								{
									log.setMassContent( "图文消息："+ o.get("title"));
									break ;
								}
								log.setMassType(2);
							}
							else if(requestVo.getMassType() == 3)
							{
								log.setMassContent("图片：<img src='" + requestVo.getImgUrl() + "'/>");
								log.setMassType(3);
							}
							wechatService.addWechatSendMessage(log);
							
							//添加分类
							if(!CmsUtils.isNullOrEmpty(category) && category.length > 0)
							{
								for(String categoryId : category)
								{
									SendMessageCategory c = new SendMessageCategory();
									c.setCategoryId(Integer.valueOf(categoryId));
									c.setMessageId(log.getId());
									c.setCreateTime(new Date());
									c.setUpdateTime(new Date());
									wechatService.addSendMessageCategory(c);
								}
							}
							
							for(int j = 0;j < userFollower.size();j++)
							{
								SendMessageUser user = new SendMessageUser();
								try
								{
									user.setUserId(userFollower.get(j).getId());
									user.setMessageId(log.getId());
									user.setSendType(Constants.Number.ONE_INT);
									user.setSendTime(new Date());
									user.setCreateTime(new Date());
									user.setUpdateTime(new Date());
								}
								catch(Exception e)
								{
									errorCount++;
									user.setSendType(2);
								}
								wechatService.addSendMessageUser(user);
							}
						} catch (WXException e1) {}
					}
				}
			}
		}
	}
    
    public JSONArray findWechatMsgList() throws WXException
    {
    	JSONArray array = null;
		String obj = (String) redisUtils.get("tags_list_" + Constants.WECHAT_TAGS_LIST);
		if(CmsUtils.isNullOrEmpty(obj))
		{
			JSONObject o =  WechatUtils.queryWechatUserTagsList(wechatService.queryWechatToken());
			array = o.getJSONArray("tags");
			redisUtils.set("tags_list_" + Constants.WECHAT_TAGS_LIST, o.toString(), Constants.Number.WEEK_SECOND);
		}
		else
		{
			JSONObject o = JSONObject.fromObject(obj);
			array = o.getJSONArray("tags");
		}
		return array;
    }
    
    /**
     * 查询群发列表
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/wechat/send/message/List.ddxj")
    public ResponseBase queryMessageList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(requestVo.getPageNum(),requestVo.getPageSize());
    	List<WechatSendMessage> wechatSendMessageList = wechatService.queryWechatSendMessageList(requestVo);
		PageInfo<WechatSendMessage> pageInfo = new PageInfo<WechatSendMessage>(wechatSendMessageList);
		if(!CmsUtils.isNullOrEmpty(wechatSendMessageList))
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("已查询到符合条件的记录");
			result.push("wechatSendMessageList",pageInfo);
		}
		else
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("没有查询到符合条件的记录");
			result.push("wechatSendMessageList",pageInfo);
		}
		log.info("#####################查询群发列表成功#####################");
    	return result;
    }
    
    /**
     * 删除群发信息
     * @param request
     * @param response
     * @param wechatId
     * @return
     */
    @RequestMapping(value="/wechat/send/message/delete.ddxj")
    public ResponseBase deleteRecruit(HttpServletRequest request, HttpServletResponse response,Integer wechatId){
    	ResponseBase result=ResponseBase.getInitResponse();
    	wechatService.deleteMessageByWechatId(wechatId);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("删除成功");
		return result;
    }
    
    /**
     * APP群发
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws UnsupportedEncodingException 
     * @throws WXException 
     * @throws ClientException 
     */
    @RequestMapping(value="/app/mass/message/send.ddxj")
    public ResponseBase appMassMessage_SEND(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo) throws UnsupportedEncodingException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	if(CmsUtils.isNullOrEmpty(requestVo.getMassTitle()))
    	{
    		result.setResponse(Constants.FALSE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg("群发标题不能为空！");
        	return result;
    	}
    	
    	if(CmsUtils.isNullOrEmpty(requestVo.getMassContent()))
    	{
    		result.setResponse(Constants.FALSE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg("群发内容不能为空！");
    		return result;
    	}
    	
    	String[] category = null;
		if(!CmsUtils.isNullOrEmpty(requestVo.getCategorys()))
		{
			if(requestVo.getCategorys().indexOf(",") >= Constants.Number.REDUCE_ONE_INT)
			{
				category = requestVo.getCategorys().split(",");
			}
			else
			{
				category = new String[Constants.Number.ONE_INT];
				category[Constants.Number.ZERO_INT] = requestVo.getCategorys();
			}
			
			if(category.length > Constants.Number.ZERO_INT)
			{
				requestVo.setCategoryId1(Integer.valueOf(category[Constants.Number.ZERO_INT]));
			}
			if(category.length > Constants.Number.ONE_INT)
			{
				requestVo.setCategoryId2(Integer.valueOf(category[Constants.Number.ONE_INT]));
			}
			if(category.length > Constants.Number.TWO_INT)
			{
				requestVo.setCategoryId3(Integer.valueOf(category[Constants.Number.TWO_INT]));
			}
		}
    	
    	if(!CmsUtils.isNullOrEmpty(requestVo.getMsgSex()))
		{
			if(requestVo.getMsgSex() == Constants.Number.ONE_INT)
			{
				requestVo.setSex("M");
			}
			if(requestVo.getMsgSex() == Constants.Number.TWO_INT)
			{
				requestVo.setSex("F");
			}
		}
    	
    	if(!CmsUtils.isNullOrEmpty(requestVo.getMassObject()))
    	{
    		if(requestVo.getMassObject() == Constants.Number.TWO_INT)
    		{
    			requestVo.setRole(Constants.Number.TWO_INT);
    		}
    		if(requestVo.getMassObject() == Constants.Number.THREE_INT)
    		{
    			requestVo.setRole(Constants.Number.ONE_INT);
    		}
    	}
    	
    	requestVo.setOpenType(Constants.Number.ONE_INT);//不等于查询OpenId
		
		List<User> userFollower = userService.queryUserFollowerByTerm(requestVo);
		if(CmsUtils.isNullOrEmpty(userFollower))
		{
			result.setResponse(Constants.FALSE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg("当前条件下未匹配到用户，不支持群发");
	    	return result;
		}
    	
    	WechatSendMessage msg = new WechatSendMessage();
    	msg.setSendType(Constants.Number.THREE_INT);
    	msg.setMassType(Constants.Number.ONE_INT);
    	msg.setMassTitle(requestVo.getMassTitle());
    	msg.setMassContent(requestVo.getMassContent());
    	if(!CmsUtils.isNullOrEmpty(requestVo.getMassLink()))
    	{
    		msg.setMassLink(requestVo.getMassLink());
    	}
    	msg.setMassPlatform(requestVo.getMassPlatform());
    	msg.setMassObject(requestVo.getMassObject());
    	if(!CmsUtils.isNullOrEmpty(requestVo.getProvince()))
    	{
    		msg.setProvince(requestVo.getProvince());
    	}
    	if(!CmsUtils.isNullOrEmpty(requestVo.getCity()))
    	{
    		msg.setCity(requestVo.getCity());
    	}
    	if(!CmsUtils.isNullOrEmpty(requestVo.getMsgSex()))
    	{
    		msg.setSex(requestVo.getMsgSex());
    	}
    	if(!CmsUtils.isNullOrEmpty(requestVo.getStartAge()))
    	{
    		msg.setStartAge(DateUtils.getDate(requestVo.getStartAge(), "yyyy-MM-dd"));
    	}
    	if(!CmsUtils.isNullOrEmpty(requestVo.getEndAge()))
    	{
    		msg.setEndAge(DateUtils.getDate(requestVo.getEndAge(), "yyyy-MM-dd"));
    	}
    	if(!CmsUtils.isNullOrEmpty(requestVo.getTimingTime()))
    	{
    		msg.setTimingTime(DateUtils.getDate(requestVo.getTimingTime(), "yyyy-MM-dd HH:mm:ss"));
    	}
    	msg.setCreateTime(new Date());
    	msg.setUpdateTime(new Date());
    	
    	wechatService.addWechatSendMessage(msg);
		
		//添加分类
		if(!CmsUtils.isNullOrEmpty(category) && category.length > Constants.Number.ZERO_INT)
		{
			for(String categoryId : category)
			{
				SendMessageCategory c = new SendMessageCategory();
				c.setCategoryId(Integer.valueOf(categoryId));
				c.setMessageId(msg.getId());
				c.setCreateTime(new Date());
				c.setUpdateTime(new Date());
				wechatService.addSendMessageCategory(c);
			}
		}
		
		try {
			asycService.appCustomBatchMassMessage(userFollower,msg.getId());//开始群发
		} catch (Exception e) {}
    	
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("群发成功");
    	log.info("#####################群发成功#####################");
    	return result;
    }
    
    /**
     * 查询群发用户列表
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/send/message/user/List.ddxj")
    public ResponseBase querySendMessageUserList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(requestVo.getPageNum(),requestVo.getPageSize());
    	List<SendMessageUser> sendMessageUserList = wechatService.querySendMessageUserList(requestVo);
		PageInfo<SendMessageUser> pageInfo = new PageInfo<SendMessageUser>(sendMessageUserList);
		if(!CmsUtils.isNullOrEmpty(sendMessageUserList))
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("已查询到符合条件的记录");
			result.push("sendMessageUserList",pageInfo);
		}
		else
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("没有查询到符合条件的记录");
			result.push("sendMessageUserList",pageInfo);
		}
		log.info("#####################查询群发用户列表成功#####################");
    	return result;
    }
}
