package net.zn.ddxj.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.common.PageHelperModel;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.Category;
import net.zn.ddxj.entity.CmsUser;
import net.zn.ddxj.entity.Information;
import net.zn.ddxj.entity.InformationCategory;
import net.zn.ddxj.entity.KeyWords;
import net.zn.ddxj.entity.SendMessageCategory;
import net.zn.ddxj.entity.SendMessageUser;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.WechatMenu;
import net.zn.ddxj.entity.WechatSendMessage;
import net.zn.ddxj.entity.WechatSync;
import net.zn.ddxj.entity.WechatUser;
import net.zn.ddxj.service.CategoryService;
import net.zn.ddxj.service.CmsService;
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
import net.zn.ddxj.utils.FrontUtils;
import net.zn.ddxj.utils.RedisUtils;
import net.zn.ddxj.utils.ResponseUtils;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.utils.wechat.WXException;
import net.zn.ddxj.utils.wechat.WechatUtils;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

import org.apache.http.ParseException;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;

@Controller
public class WechatController extends BaseController{
	
	private static final String WECHAT_MENU_LIST = "/wechat/wechat_menu_list.html";
	private static final String WECHAT_MENU_LIST_TPL = "/wechat/wechat_menu_list_tpl.html";
	private static final String WECHAT_MENU_LIST_EDIT = "/wechat/wechat_menu_edit.html";
	private static final String APP_MASS_MESSAGE_SEND = "/wechat/app_mass_message_send.html";
	private static final String WECHAT_MATERIAL_LIST = "/wechat/wechat_material_list.html";
	private static final String WECHAT_MATERIAL_LIST_TPL  = "/wechat/wechat_material_list_tpl.html";
	private static final String WECHAT_MATERIAL_RECORD_LIST = "/wechat/wechat_material_record_list.html";
	private static final String WECHAT_MATERIAL_RECORD_LIST_TPL  = "/wechat/wechat_material_record_list_tpl.html";
	private static final String WECHAT_USER_LIST  = "/wechat/wechat_user_list.html";
	private static final String WECHAT_USER_LIST_TPL  = "/wechat/wechat_user_list_tpl.html";
	private static final String WECHAT_SEND_MESSAGE_LIST  = "/wechat/wechat_send_message_list.html";
	private static final String WECHAT_SEND_MESSAGE_LIST_TPL  = "/wechat/wechat_send_message_list_tpl.html";
	private static final String WECHAT_MESSAGE_USER_LIST = "/wechat/wechat_message_user_list.html";
	private static final String WECHAT_MESSAGE_USER_LIST_TPL = "/wechat/wechat_message_user_list_tpl.html";
	private static final String WECHAT_MENU_SETTING = "/wechat/wechat_menu_setting.html";
	private static final String WECHAT_MENU_PICTURE_SETTING = "/wechat/wechat_menu_picture_setting.html";
	private static final String WECHAT_KEYWORDS_LIST = "/wechat/wechat_keywords_list.html";
	private static final String WECHAT_KEYWORDS_LIST_TPL = "/wechat/wechat_keywords_list_tpl.html";
	private static final String WECHAT_KEYWORDS_EDIT = "/wechat/wechat_keywords_edit.html";
	private static final String WECHAT_MENU_NO_KEYWORDS = "/wechat/wechat_menu_noKeywords.html";

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
	@Autowired
	private CmsService cmsService;
	@Autowired
	private CategoryService categoryService;
	
    /**
     * 自定义菜单GET
     * @param request
     * @param response
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/wechatMenu/list.htm",method = RequestMethod.GET)
    public String queryAllMenuGET(HttpServletRequest request, HttpServletResponse response, ModelMap model, RequestVo requestVo)
    {
    	return FrontUtils.findFrontTpl(request, response, model, WECHAT_MENU_LIST);
    }
    
    /**
     * 自定义菜单POST
     * @param request
     * @param response
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/wechatMenu/list.htm",method = RequestMethod.POST)
    public String queryAllMenuPOST(HttpServletRequest request, HttpServletResponse response, ModelMap model, RequestVo requestVo)
    {
    	PageHelper.startPage(requestVo.getCurrentPage(), 20);
    	List<WechatMenu> weChatMenuList = wechatService.queryAllWeChatMenu();
    	PageInfo<WechatMenu> pageInfo = new PageInfo<WechatMenu>(weChatMenuList);
    	PageHelperModel.responsePageModel(pageInfo,model);
    	model.addAttribute("weChatMenuList",pageInfo.getList());
    	return FrontUtils.findFrontTpl(request, response, model, WECHAT_MENU_LIST_TPL);
    }
    
    /**
     * 查询微信用户GET
     * @param request
     * @param response
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/wechatUser/list.htm",method = RequestMethod.GET)
    public String queryWechatUserGet(HttpServletRequest request, HttpServletResponse response, ModelMap model, CmsRequestVo requestVo)
    {
    	return FrontUtils.findFrontTpl(request, response, model, WECHAT_USER_LIST);
    }
    
    /**
     * 查询微信用户POST
     * @param request
     * @param response
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/wechatUser/list.htm",method = RequestMethod.POST)
    public String queryWechatUserPOST(HttpServletRequest request, HttpServletResponse response, ModelMap model, CmsRequestVo requestVo)
    {
    	PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
    	List<WechatUser> wechatUserList = userService.queryWechatUserListCms(requestVo);
    	PageInfo<WechatUser> pageInfo = new PageInfo<WechatUser>(wechatUserList);
    	PageHelperModel.responsePageModel(pageInfo,model);
    	model.addAttribute("wechatUserList",pageInfo.getList());
    	return FrontUtils.findFrontTpl(request, response, model, WECHAT_USER_LIST_TPL);
    }
    
    /**
     * 删除自定义菜单
     * @param request
     * @param response
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/wechatMenu/record.htm",method = RequestMethod.POST)
    public String deleteMenu(HttpServletRequest request, HttpServletResponse response,Integer menuId)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	wechatService.deleteMenuByMenuId(menuId);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("删除成功！");
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
    	return null;
    }
    
    /**
	 * 更新自定义菜单GET
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/update/wechatMenu/edit.htm",method = RequestMethod.GET)
	public String updateMenuGET(HttpServletRequest request, HttpServletResponse response,ModelMap model,Integer id) 
	{
		if(id != null && id > 0)
		{
			WechatMenu menu = wechatService.queryMenuByMenuId(id);
			model.addAttribute("wechatMenu", menu);
		}
		List<WechatMenu> menuParentList = wechatService.queryOneLevelMenu();
    	model.addAttribute("menuParentList", menuParentList);
		return FrontUtils.findFrontTpl(request, response, model, WECHAT_MENU_LIST_EDIT);
	}
    
    /**
	 * 更新自定义菜单POST
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/update/wechatMenu/edit.htm",method = RequestMethod.POST)
	public String updateMenuPOST(HttpServletRequest request, HttpServletResponse response,Integer id,String menuName,Integer clickAction,Integer indexNumber,String menuUrl,String menuKey,Integer parentId,
			String replyText,String replyImage) 
	{
		ResponseBase base = wechatService.updateWeChatMenu(id,menuName,clickAction,indexNumber,menuUrl,menuKey,parentId,replyText, replyImage);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(base));
		return null;
	}
	
	/**
	 * 发布微信自定义菜单POST
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wechat/menu/push.htm",method = RequestMethod.POST)
	public String wechatMenuPushPOST(HttpServletRequest request, HttpServletResponse response,ModelMap model) 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		result.push("status", wechatService.updateCustomerMenu());
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("发布成功！");
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
     * APP群发GET
     *
     * @param request
     * @param response
     * @author fancunxin
     */
    @RequestMapping(value="/app/mass/message/send.htm",method = RequestMethod.GET)
    public String appMassMessageGET(HttpServletRequest request, HttpServletResponse response,ModelMap model)
    {
    	List<Category> categoryList = categoryService.getCategoryByType(2);
    	model.addAttribute("categoryList", categoryList);
    	return FrontUtils.findFrontTpl(request, response, model, APP_MASS_MESSAGE_SEND);
    }
	
	/**
     * APP群发POST
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws UnsupportedEncodingException 
     * @throws WXException 
     * @throws ClientException 
     */
    @RequestMapping(value="/app/mass/message/send.htm",method = RequestMethod.POST)
    public String appMassMessagePOST(HttpServletRequest request, HttpServletResponse response,ModelMap model,RequestVo requestVo) throws UnsupportedEncodingException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	if(CmsUtils.isNullOrEmpty(requestVo.getMassTitle()))
    	{
    		result.setResponse(Constants.FALSE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg("群发标题不能为空！");
        	ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
        	return null;
    	}
    	
    	if(CmsUtils.isNullOrEmpty(requestVo.getMassContent()))
    	{
    		result.setResponse(Constants.FALSE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg("群发内容不能为空！");
    		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
        	return null;
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
	    	ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
        	return null;
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
    	ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
    	return null;
    }
    
    /**
     * 群发列表GET
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/wechat/send/message/List.htm",method = RequestMethod.GET)
    public String queryMessageListGET(HttpServletRequest request, HttpServletResponse response,ModelMap model,RequestVo requestVo)
    {
    	
    	return FrontUtils.findFrontTpl(request, response, model, WECHAT_SEND_MESSAGE_LIST);
    }
    
    /**
     * 群发列表POST
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/wechat/send/message/List.htm",method = RequestMethod.POST)
    public String queryMessageListPOST(HttpServletRequest request, HttpServletResponse response,ModelMap model,RequestVo requestVo)
    {
    	PageHelper.startPage(requestVo.getCurrentPage(),20);
    	List<WechatSendMessage> wechatSendMessageList = wechatService.queryWechatSendMessageList(requestVo);
		PageInfo<WechatSendMessage> pageInfo = new PageInfo<WechatSendMessage>(wechatSendMessageList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("wechatSendMessageList",wechatSendMessageList);
    	return FrontUtils.findFrontTpl(request, response, model, WECHAT_SEND_MESSAGE_LIST_TPL);
    }
    
    /**
     * 删除群发信息POST
     * @param request
     * @param response
     * @param wechatId
     * @return
     */
    @RequestMapping(value="/wechat/send/message/delete.htm",method = RequestMethod.POST)
    public String deleteRecruit(HttpServletRequest request, HttpServletResponse response,Integer wechatId)
    {
    	ResponseBase result=ResponseBase.getInitResponse();
    	wechatService.deleteMessageByWechatId(wechatId);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
    	ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
    }
    
    /**
     * 群发用户GET
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/wechat/message/user/list.htm",method = RequestMethod.GET)
    public String queryMessageUserListGET(HttpServletRequest request, HttpServletResponse response,ModelMap model,int msgId)
    {
    	model.addAttribute("msgId",msgId);
    	return FrontUtils.findFrontTpl(request, response, model, WECHAT_MESSAGE_USER_LIST);
    }
    
    /**
     * 群发用户POST
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/wechat/message/user/list.htm",method = RequestMethod.POST)
    public String queryMessageUserListPOST(HttpServletRequest request, HttpServletResponse response,ModelMap model,RequestVo requestVo)
    {
    	
    	PageHelper.startPage(requestVo.getCurrentPage(),20);
    	List<SendMessageUser> sendMessageUserList = wechatService.querySendMessageUserList(requestVo);
		PageInfo<SendMessageUser> pageInfo = new PageInfo<SendMessageUser>(sendMessageUserList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("sendMessageUserList",sendMessageUserList);
    	return FrontUtils.findFrontTpl(request, response, model, WECHAT_MESSAGE_USER_LIST_TPL);
    }
    
	/**
	 * 查询微信素材GET
	 * @param request
	 * @param response
	 * @param model
	 * @param requestVo
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	
	@RequestMapping(value="/query/wechatMaterial/list.htm",method = RequestMethod.GET)
    public String queryWechatMaterialGET(HttpServletRequest request, HttpServletResponse response, ModelMap model,  Integer currentPage) throws UnsupportedEncodingException
    {
		return FrontUtils.findFrontTpl(request, response, model, WECHAT_MATERIAL_LIST);
    }
	/**
	 * 查询微信素材POST
	 * @param request
	 * @param response
	 * @param model
	 * @param currentPage
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/query/wechatMaterial/list.htm",method = RequestMethod.POST)
    public String queryWechatMaterialPOST(HttpServletRequest request, HttpServletResponse response, ModelMap model,  Integer currentPage) throws UnsupportedEncodingException
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
			model.addAttribute("newsList",array);
			model.addAttribute("total",total);
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
		return FrontUtils.findFrontTpl(request, response, model, WECHAT_MATERIAL_LIST_TPL);
    }
	/**
	 * 查询素材文章列表GET
	 * @param request
	 * @param response
	 * @param media_id
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value="/query/news/view/details/list.htm",method = RequestMethod.GET)
	public String queryNewsViewGET(HttpServletRequest request, HttpServletResponse response,ModelMap model,String mediaId) throws ParseException, IOException, IllegalAccessException
	{
		
		model.addAttribute("mediaId",mediaId);
		return FrontUtils.findFrontTpl(request, response, model, WECHAT_MATERIAL_RECORD_LIST);

	}
	/**
	 * 查询素材文章列表POST
	 * @param request
	 * @param response
	 * @param model
	 * @param media_id
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws WXException 
	 */
	@RequestMapping(value="/query/news/view/details/list.htm",method = RequestMethod.POST)
    public String queryNewsViewPOST(HttpServletRequest request, HttpServletResponse response, ModelMap model,String mediaId) throws UnsupportedEncodingException, WXException
    {
		
		ResponseBase result = ResponseBase.getInitResponse();
		try 
		{
			JSONObject weiXinMaterial = WechatUtils.getWeiXinMaterialDetail(wechatService.queryWechatToken(), mediaId);
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
			result.push("mediaId", mediaId);
			result.push("newsDetailList", array);
			model.addAttribute("newsDetailList",array);
			model.addAttribute("total",array.size());
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
		return FrontUtils.findFrontTpl(request, response, model, WECHAT_MATERIAL_RECORD_LIST_TPL);
    }
	/**
	 * 同步图文素材到资讯POST
	 * @param request
	 * @param response
	 * @param model
	 * @param requestVo
	 * @return
	 * @throws QiniuException
	 * @throws WXException
	 */
	@RequestMapping(value="/add/wechat/sync/information.htm",method = RequestMethod.POST)
	public String synchroMaterialPOST(HttpServletRequest request, HttpServletResponse response, ModelMap model,RequestVo requestVo) throws QiniuException, WXException{
		ResponseBase result = ResponseBase.getInitResponse();
	    
	    Integer sysnCount = wechatService.queryWechatSyncById(requestVo.getMediaId());
	    if(sysnCount != null && sysnCount > 0)
	    {
	      result.setResponse(Constants.TRUE);
	        result.setResponseCode(Constants.SUCCESS_200);
	        result.setResponseMsg("此素材已同步");
		    ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			return null;
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
	    //向咨询分类列表里插入一条记录
	    InformationCategory infoCategory=new InformationCategory();
	    infoCategory.setInfoId(info.getId());
	    infoCategory.setCategoryId(6);
	    infoCategory.setCreateTime(new Date());
	    infoCategory.setUpdateTime(new Date());
	    informationService.addInformationCategory(infoCategory);
	    //向微信sync插入一条记录
		WechatSync sync = new WechatSync();
		sync.setThumbMediaId(requestVo.getMediaId());
		sync.setCreateTime(new Date());
		wechatService.addWechatSync(sync);
		
		//日志记录
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-公众号管理-素材同步-（"+requestVo.getMediaId()+"）",sessionUser.getId());
	    result.setResponse(Constants.TRUE);
	    result.setResponseCode(Constants.SUCCESS_200);
	    result.setResponseMsg("同步成功");
	    ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
		
	}

	/**
     * 自定义菜单-基本设置GET
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws UnsupportedEncodingException 
     * @throws ClientException 
     */
    @RequestMapping(value="/wechat/menu/setting.htm",method = RequestMethod.GET)
    public String wechatMenuSettingGET(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws UnsupportedEncodingException
    {
    	model.addAttribute("useSystemDefaultReply", Integer.valueOf(cacheSetting.findSettingValue(Constants.USE_SYSTEM_DEFAULT_REPLY)));
    	model.addAttribute("replyType", Integer.valueOf(cacheSetting.findSettingValue(Constants.REPLY_TYPE)));
    	model.addAttribute("replayWords", cacheSetting.findSettingValue(Constants.REPLAY_WORDS));
    	model.addAttribute("imageTextList", cacheSetting.findSettingValue(Constants.IMAGE_TEXT_LIST));
    	model.addAttribute("befollowedReplyImg", cacheSetting.findSettingValue(Constants.BEFOLLOWED_REPLY_IMG));
    	
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
			model.addAttribute("newsList", array);
		}
		catch (WXException e) {}
    	
    	return FrontUtils.findFrontTpl(request, response, model, WECHAT_MENU_SETTING);
    }
    
	/**
     * 接收图片时-基本设置GET
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws UnsupportedEncodingException 
     * @throws ClientException 
     */
    @RequestMapping(value="/wechat/menu/picture/setting.htm",method = RequestMethod.GET)
    public String wechatMenuPictureSettingGET(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws UnsupportedEncodingException
    {
    	model.addAttribute("useSystemDefaultReplyPic", Integer.valueOf(cacheSetting.findSettingValue(Constants.USE_SYSTEM_DEFAULT_REPLY_PIC)));
    	model.addAttribute("replyTypePic", Integer.valueOf(cacheSetting.findSettingValue(Constants.REPLY_TYPE_PIC)));
    	model.addAttribute("replayWordsPic", cacheSetting.findSettingValue(Constants.REPLAY_WORDS_PIC));
    	model.addAttribute("imageTextListPic", cacheSetting.findSettingValue(Constants.IMAGE_TEXT_LIST_PIC));
    	model.addAttribute("befollowedReplyImgPic", cacheSetting.findSettingValue(Constants.BEFOLLOWED_REPLY_IMG_PIC));
    	
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
			model.addAttribute("newsList", array);
		}
		catch (WXException e) {}
    	
    	return FrontUtils.findFrontTpl(request, response, model, WECHAT_MENU_PICTURE_SETTING);
    }
    
    /**
     * 接收图片时-基本设置POST
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws UnsupportedEncodingException 
     * @throws ClientException 
     */
    @RequestMapping(value="/wechat/menu/picture/setting.htm",method = RequestMethod.POST)
    public String wechatMenuPictureSettingPOST(HttpServletRequest request, HttpServletResponse response,ModelMap model,String useSystemDefaultReplyPic,String replyTypePic,String replayWordsPic,String imageTextListPic,String befollowedReplyImgPic) throws WXException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	cacheSetting.saveSetting(Constants.USE_SYSTEM_DEFAULT_REPLY_PIC, useSystemDefaultReplyPic);
    	cacheSetting.saveSetting(Constants.REPLY_TYPE_PIC, replyTypePic);
    	cacheSetting.saveSetting(Constants.REPLAY_WORDS_PIC, replayWordsPic);
    	cacheSetting.saveSetting(Constants.IMAGE_TEXT_LIST_PIC, imageTextListPic);
    	cacheSetting.saveSetting(Constants.BEFOLLOWED_REPLY_IMG_PIC, befollowedReplyImgPic);
    	
    	String img = cacheSetting.findSettingValue(Constants.BEFOLLOWED_REPLY_IMG_PIC);
    	
    	if(CmsUtils.isNullOrEmpty(befollowedReplyImgPic))
		{
			befollowedReplyImgPic = "http://res-test.diandxj.com/Fgz_XN_PfpcoLflGWJFY475Dqwcm";
		}
    	if(befollowedReplyImgPic != img)
    	{
    		JSONObject obj = WechatUtils.uploadWeiXinImage(wechatService.queryWechatToken(), befollowedReplyImgPic);
    		cacheSetting.saveSetting(Constants.BEFOLLOWED_REPLY_MEDIAID_PIC, obj.getString("media_id"));
    	}
    	
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("设置成功");
    	ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
    	return null;
    }
    
    /**
     * 自定义菜单-基本设置POST
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws UnsupportedEncodingException 
     * @throws ClientException 
     */
    @RequestMapping(value="/wechat/menu/setting.htm",method = RequestMethod.POST)
    public String wechatMenuSettingPOST(HttpServletRequest request, HttpServletResponse response,ModelMap model,String useSystemDefaultReply,String replyType,String replayWords,String imageTextList,String befollowedReplyImg) throws WXException
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
    	result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
    	ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
    	return null;
    }
    /**
     * 查询关键字
    * @Title: queryKeyWords_POST
    * @Description: TODO()
    * @param @param request
    * @param @param response
    * @param @param model
    * @param @param requestVo
    * @param @return    参数
    * @return String    返回类型
    * @author  上海众宁网络科技有限公司-何俊辉
    * @throws
     */
    @RequestMapping(value = "/wechat/keywords/list.htm", method = RequestMethod.GET)
    public String queryKeyWords_GET(HttpServletRequest request, HttpServletResponse response, ModelMap model)
    {
        return FrontUtils.findFrontTpl(request, response, model, WECHAT_KEYWORDS_LIST);   
    }
    /**
     * 修改关键字
    * @Title: updateKeywords
    * @Description: TODO()
    * @param @param request
    * @param @param response
    * @param @param model
    * @param @param id
    * @param @return
    * @param @throws UnsupportedEncodingException    参数
    * @return String    返回类型
    * @author  上海众宁网络科技有限公司-何俊辉
    * @throws
     */
    @RequestMapping(value = "/wechat/keywords/edit.htm", method = RequestMethod.GET)
    public String updateKeywords(HttpServletRequest request, HttpServletResponse response, ModelMap model,Integer id) throws UnsupportedEncodingException
    {
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
			model.addAttribute("newsList", array);
		}
		catch (WXException e) {}
    	if(id != null && id > 0)
    	{
    		KeyWords keyWords = wechatService.findKeyWordsById(id);
    		model.addAttribute("keyWords",keyWords);
    	}
    	return FrontUtils.findFrontTpl(request, response, model, WECHAT_KEYWORDS_EDIT);   
    }
    /**
     * 查询关键字
    * @Title: queryKeyWords_POST
    * @Description: TODO()
    * @param @param request
    * @param @param response
    * @param @param model
    * @param @param requestVo
    * @param @return    参数
    * @return String    返回类型
    * @author  上海众宁网络科技有限公司-何俊辉
    * @throws
     */
    @RequestMapping(value = "/wechat/keywords/list.htm", method = RequestMethod.POST)
    public String queryKeyWords_POST(HttpServletRequest request, HttpServletResponse response, ModelMap model,CmsRequestVo requestVo)
    {
       
    	PageHelper.startPage(requestVo.getCurrentPage(),20);
        List<KeyWords> findKeyWordsList = wechatService.findKeyWordsList(requestVo);
		PageInfo<KeyWords> pageInfo = new PageInfo<KeyWords>(findKeyWordsList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("keywordsList",pageInfo.getList());
        return FrontUtils.findFrontTpl(request, response, model, WECHAT_KEYWORDS_LIST_TPL);   
    }
    /**
     * 修改关键字
    * @Title: keywordsEdit_POST
    * @Description: TODO()
    * @param @param request
    * @param @param response
    * @param @param model
    * @param @param requestVo
    * @param @return    参数
    * @return String    返回类型
    * @author  上海众宁网络科技有限公司-何俊辉
    * @throws
     */
    @RequestMapping(value = "/wechat/keywords/edit.htm", method = RequestMethod.POST)
    public String keywordsEdit_POST(HttpServletRequest request, HttpServletResponse response, ModelMap model,CmsRequestVo requestVo)
    {
       
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	KeyWords keyWords = null;
		if(requestVo.getId() != null && requestVo.getId() > 0) //修改
		{
			keyWords = wechatService.findKeyWordsById(requestVo.getId());
		}
		else
		{
			keyWords = new KeyWords();
			keyWords.setCreateTime(new Date());
		}
		if(requestVo.getReplyType() == Constants.Number.ONE_INT)
		{
			keyWords.setReplayWords(requestVo.getReplayWords());
		}
		else if(requestVo.getReplyType() == Constants.Number.TWO_INT)
		{
			if(!CmsUtils.isNullOrEmpty(keyWords.getReplayWords()))
			{
				if(!keyWords.getReplayWords().split("###")[0].equals(requestVo.getBefollowedReplyImg()))
				{
					try {
						JSONObject obj = WechatUtils.uploadWeiXinImage(wechatService.queryWechatToken(), requestVo.getBefollowedReplyImg());
						StringBuffer sub = new StringBuffer();
						sub.append(requestVo.getBefollowedReplyImg() + "###" + obj.getString("media_id"));
						keyWords.setReplayWords(sub.toString());
					} catch (WXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else
			{
				try {
					JSONObject obj = WechatUtils.uploadWeiXinImage(wechatService.queryWechatToken(), requestVo.getBefollowedReplyImg());
					StringBuffer sub = new StringBuffer();
					sub.append(requestVo.getBefollowedReplyImg() + "###" + obj.getString("media_id"));
					keyWords.setReplayWords(sub.toString());
				} catch (WXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(requestVo.getReplyType() == Constants.Number.THREE_INT)
		{
			keyWords.setReplayWords(requestVo.getImageTextList());
		}
		if(!CmsUtils.isNullOrEmpty(requestVo.getIsMatchAllKeyWords()))
		{
			keyWords.setIsMatch(1);
		}
		else
		{
			keyWords.setIsMatch(2);
		}
		keyWords.setKeyWords(requestVo.getKeyWords());
		keyWords.setReplyType(requestVo.getReplyType());
		keyWords.setUpdateTime(new Date());
		wechatService.updateKeyWords(keyWords);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
    }
    /**
     * 删除关键字
    * @Title: deleteKeyWords
    * @Description: TODO()
    * @param @param request
    * @param @param response
    * @param @param keywordsId
    * @param @return    参数
    * @return String    返回类型
    * @author  上海众宁网络科技有限公司-何俊辉
    * @throws
     */
    @RequestMapping(value="/delete/keywords.htm",method = RequestMethod.POST)
    public String deleteKeyWords(HttpServletRequest request, HttpServletResponse response,Integer keywordsId)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	wechatService.deleteKeywords(keywordsId);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
    	return null;
    }
    /**
     * 无关键字回复-GET
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws UnsupportedEncodingException 
     * @throws ClientException 
     */
    @RequestMapping(value="/wechat/menu/noKeywords.htm",method = RequestMethod.GET)
    public String wechatMenuNoKeywordsGET(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws UnsupportedEncodingException
    {
    	model.addAttribute("noKeyWordsDefaultReply", Integer.valueOf(cacheSetting.findSettingValue(Constants.NO_KEYWORDS_DEFAULT_REPLY)));
    	model.addAttribute("noReplyType", Integer.valueOf(cacheSetting.findSettingValue(Constants.NO_REPLY_TYPE)));
    	model.addAttribute("noReplayWords", cacheSetting.findSettingValue(Constants.NO_REPLAY_WORDS));
    	model.addAttribute("noImageTextList", cacheSetting.findSettingValue(Constants.NO_IMAGE_TEXT_LIST));
    	model.addAttribute("noBefollowedReplyImg", cacheSetting.findSettingValue(Constants.NO_BEFOLLOWED_REPLY_IMG));
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
			model.addAttribute("newsList", array);
		}
		catch (WXException e) {}
    	
    	return FrontUtils.findFrontTpl(request, response, model, WECHAT_MENU_NO_KEYWORDS);
    }
    
    /**
     * 无关键字回复-POST
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws UnsupportedEncodingException 
     * @throws ClientException 
     */
    @RequestMapping(value="/wechat/menu/noKeywords.htm",method = RequestMethod.POST)
    public String wechatMenuNoKeywordsPOST(HttpServletRequest request, HttpServletResponse response,ModelMap model,String noKeyWordsDefaultReply,String noReplyType,String noReplayWords,String noImageTextList,String noBefollowedReplyImg) throws WXException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	cacheSetting.saveSetting(Constants.NO_KEYWORDS_DEFAULT_REPLY, noKeyWordsDefaultReply);
    	cacheSetting.saveSetting(Constants.NO_REPLY_TYPE, noReplyType);
    	cacheSetting.saveSetting(Constants.NO_REPLAY_WORDS, noReplayWords);
    	cacheSetting.saveSetting(Constants.NO_IMAGE_TEXT_LIST, noImageTextList);
    	cacheSetting.saveSetting(Constants.NO_BEFOLLOWED_REPLY_IMG, noBefollowedReplyImg);
    	
    	String img = cacheSetting.findSettingValue(Constants.NO_BEFOLLOWED_REPLY_IMG);
    	
    	if(CmsUtils.isNullOrEmpty(noBefollowedReplyImg))
		{
    		noBefollowedReplyImg = "http://res-test.diandxj.com/Fgz_XN_PfpcoLflGWJFY475Dqwcm";
		}
    	if(noBefollowedReplyImg != img)
    	{
    		JSONObject obj = WechatUtils.uploadWeiXinImage(wechatService.queryWechatToken(), noBefollowedReplyImg);
    		cacheSetting.saveSetting(Constants.NO_BEFOLLOWED_REPLY_MEDIAID, obj.getString("media_id"));
    	}
    	
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
    	ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
    	return null;
    }
    
}
