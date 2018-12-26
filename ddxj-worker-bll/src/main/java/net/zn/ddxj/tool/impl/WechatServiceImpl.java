package net.zn.ddxj.tool.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.KeyWords;
import net.zn.ddxj.entity.SendMessageCategory;
import net.zn.ddxj.entity.SendMessageUser;
import net.zn.ddxj.entity.WechatMenu;
import net.zn.ddxj.entity.WechatSendMessage;
import net.zn.ddxj.entity.WechatSync;
import net.zn.ddxj.entity.WechatToken;
import net.zn.ddxj.entity.WechatUser;
import net.zn.ddxj.entity.WechatUserTag;
import net.zn.ddxj.mapper.KeyWordsMapper;
import net.zn.ddxj.mapper.SendMessageCategoryMapper;
import net.zn.ddxj.mapper.SendMessageUserMapper;
import net.zn.ddxj.mapper.WechatMenuMapper;
import net.zn.ddxj.mapper.WechatSendMessageMapper;
import net.zn.ddxj.mapper.WechatSyncMapper;
import net.zn.ddxj.mapper.WechatTokenMapper;
import net.zn.ddxj.mapper.WechatUserMapper;
import net.zn.ddxj.mapper.WechatUserTagMapper;
import net.zn.ddxj.tool.WechatService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.utils.wechat.WXException;
import net.zn.ddxj.utils.wechat.WechatUtils;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;
@Service
public class WechatServiceImpl implements WechatService {

	@Autowired
	private WechatTokenMapper wechatTokenMapper;
	@Autowired
	private WechatUserMapper wechatUserMapper;
	@Autowired
	private WechatUserTagMapper wechatUserTagMapper;	
	@Autowired
	private WechatSyncMapper wechatSyncMapper;
	@Autowired
	private WechatMenuMapper wechatMenuMapper;
	@Autowired
	private WechatSendMessageMapper wechatSendMessageMapper;
	@Autowired
	private SendMessageUserMapper sendMessageUserMapper;
	@Autowired
	private SendMessageCategoryMapper sendMessageCategoryMapper;
	@Autowired
	private KeyWordsMapper keyWordsMapper;
	@Override
	public String queryWechatToken() 
	{
		return getAcessToken();
	}
	public String getAcessToken()
	{
		WechatToken lastToken = wechatTokenMapper.queryLastToken();
        if(CmsUtils.isNullOrEmpty(lastToken))//查询数据库
        {
        	try 
        	{
	        	JSONObject tokenJson = WechatUtils.retrieveAccessToken(Constants.WX_APP_ID, Constants.WX_APP_SECRET);
	        	if(!CmsUtils.isNullOrEmpty(tokenJson.getString("access_token")))//验证有没有token
	        	{
	        		WechatToken wechatToken = new WechatToken();
	        		wechatToken.setAccessToken(tokenJson.getString("access_token"));
	        		wechatToken.setExpiresIn(tokenJson.getInt("expires_in"));
	        		wechatToken.setCreateTime(new Date());
	        		wechatTokenMapper.insertSelective(wechatToken);
	        		return tokenJson.getString("access_token");
	        	}
	        	else
	        	{
	        		return null;
	        	}
        	}
        	catch (WXException e) 
        	{
				e.printStackTrace();
			}
        }
        else
        {
        	
            long now = System.currentTimeMillis();
            long acctime = lastToken.getCreateTime().getTime();
            if (CmsUtils.isNullOrEmpty(lastToken.getAccessToken()) || ((now - acctime) / 1000) >= 6000) {//查询数据库中的token是否超时
            	try 
            	{
            		JSONObject tokenJson = WechatUtils.retrieveAccessToken(Constants.WX_APP_ID, Constants.WX_APP_SECRET);
                	if(!CmsUtils.isNullOrEmpty(tokenJson.getString("access_token")))//验证有没有token
                	{
                		WechatToken wechatToken = new WechatToken();
                		wechatToken.setAccessToken(tokenJson.getString("access_token"));
                		wechatToken.setExpiresIn(tokenJson.getInt("expires_in"));
                		wechatToken.setCreateTime(new Date());
                		wechatTokenMapper.insertSelective(wechatToken);
                		return tokenJson.getString("access_token");
                	}
                	else
                	{
                		return null;
                	}
				}
            	catch (WXException e) 
            	{
					e.printStackTrace();
				}
            } 
            else 
            {
                return lastToken.getAccessToken();
            }
        
        }
		return null;
	}
	@Override
	public void initWechatFollow()
	{
		// 循环加载粉丝信息
		String token = queryWechatToken();
		try
		{
			JSONObject ret = WechatUtils.getUserFollowerList(token,null);//

			if(ret.has("data") && ret.getJSONObject("data").has("openid"))
			{
				
				JSONArray list = ret.getJSONObject("data").getJSONArray("openid");
				
				String next_openid = ret.getString("next_openid");
				
				int maxExcute = 0;
				
				while(!CmsUtils.isNullOrEmpty(next_openid) && maxExcute < 100)
				{
					maxExcute++;
					try
					{
						ret = WechatUtils.getUserFollowerList(token,next_openid);
						next_openid = ret.getString("next_openid");
						
						if(ret.has("data") && ret.getJSONObject("data").has("openid"))
						{
							list.add(ret.getJSONObject("data").getJSONArray("openid"));
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				
				List<String> result = new ArrayList<String>();
				List<String> exists = queryWechatFollowerIds();//查询数据库所有openid
				if(CmsUtils.isNullOrEmpty(exists))
				{
					for(int i = 0;i < list.size();i++)
					{
						result.add(list.getString(i));
					}
				}
				else
				{
					for(int i = 0;i < list.size();i++)
					{
						String openId = list.getString(i);
						if(exists.indexOf(openId) == -1)
						{
							result.add(list.getString(i));
						}
					}
				}
				// 把过滤完的拿去加载
				List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
				for(int i = 0;i < result.size();i++)
				{
					Map<String,Object> openId = new HashMap<String,Object>();
					openId.put("openid", result.get(i));
					data.add(openId);
					
					if(data.size() >= 100 || i + 1 == result.size())
					{
						try
						{
							JSONObject obj = WechatUtils.BulkAccessToUserBasicInformation(token,data);
							
							JSONArray dataList = obj.getJSONArray("user_info_list");
							
							List<WechatUser> d = new ArrayList<WechatUser>();
							
							for(int j =0;j<dataList.size();j++)
							{
								JSONObject o = dataList.getJSONObject(j);
								WechatUser f = createUserFollower(o);
								d.add(f);
							}
							
							addWechatUser(d);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						
						data.clear();
					}
				}
			}
		
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
	}
	public WechatUser createUserFollower(JSONObject o) throws UnsupportedEncodingException
	{
		WechatUser f = new WechatUser();
		
		f.setNickName(new String(o.getString("nickname").getBytes("ISO-8859-1"),"utf-8").trim());
		
		f.setCity(new String(o.getString("city").getBytes("ISO-8859-1"),"utf-8").trim());
		f.setCountry(new String(o.getString("country").getBytes("ISO-8859-1"),"utf-8").trim());
		f.setHeadImgUrl(o.getString("headimgurl"));
		f.setOpenId(o.getString("openid"));
		f.setProvince(new String(o.getString("province").getBytes("ISO-8859-1"),"utf-8").trim());
		f.setRemark(new String(o.getString("remark").getBytes("ISO-8859-1"),"utf-8").trim());
		f.setSex(o.getString("sex"));
		f.setSubscribeTime(DateUtils.getStringDate(DateUtils.getDateTime(Long.valueOf(o.getLong("subscribe_time") * 1000)), "yyyy-MM-dd"));
		f.setSubscribeScene(o.getString("subscribe_scene"));
		JSONArray tags = o.getJSONArray("tagid_list");
		if(!CmsUtils.isNullOrEmpty(tags))
		{
			for(int i =0;i<tags.size();i++)
			{
				int id = tags.getInt(i);
				
				WechatUserTag followerTag = queryWechatUserTag(id);
				if(followerTag != null)
				{
					addWechatUserTagBetw(o.getString("openid"), followerTag.getId());
				}
			}
		}
		
		long followTime = o.getLong("subscribe_time");
		f.setUpdateTime(new Date(followTime*1000L));
		return f;
	}
	@Override
	public List<String> queryWechatFollowerIds() {
		
		return wechatUserMapper.queryWechatUserOpenId();
	}
	@Override
	public WechatUserTag queryWechatUserTag(Integer tagId) {
		// TODO Auto-generated method stub
		return wechatUserTagMapper.selectByPrimaryKey(tagId);
	}
	@Override
	public int addWechatUserTagBetw(String openId, Integer tagId) {
		// TODO Auto-generated method stub
		return wechatUserTagMapper.addWechatUserTag(openId, tagId);
	}
	@Override
	public void addWechatUser(List<WechatUser> wechatUser) {
		for(int i =0;i<wechatUser.size();i++)
		{
			wechatUser.get(i).setCreateTime(new Date());
			wechatUserMapper.insertSelective(wechatUser.get(i));
		}
	}
	@Override
	public int queryWechatSyncById(String wechatId) {
		// TODO Auto-generated method stub
		return wechatSyncMapper.queryWechatSyncById(wechatId);
	}
	@Override
	public int addWechatSync(WechatSync sync) {
		// TODO Auto-generated method stub
		return wechatSyncMapper.insertSelective(sync);
	}
	@Override
	public List<WechatMenu> queryAllWeChatMenu( ) {
		// TODO Auto-generated method stub
		return wechatMenuMapper.queryAllWeChatMenu();
	}
	@Override
	public int deleteMenuByMenuId(Integer menuId) {
		// TODO Auto-generated method stub
		return wechatMenuMapper.deleteMeunByMenuId(menuId);
	}
	@Override
	public List<WechatMenu> queryOneLevelMenu() {
		// TODO Auto-generated method stub
		return wechatMenuMapper.queryOneLevelMenu();
	}
	@Override
	public WechatMenu queryMenuByMenuId(Integer id) {
		// TODO Auto-generated method stub
		return wechatMenuMapper.selectByPrimaryKey(id);
	}
	@Override
	public ResponseBase updateWeChatMenu(Integer id, String menuName, Integer clickAction, Integer indexNumber,
			String menuUrl, String menuKey, Integer parentId, String replyText, String replyImage) {
		// TODO Auto-generated method stub
		ResponseBase result = ResponseBase.getInitResponse();
		if(CmsUtils.isNullOrEmpty(id))
		{
			if(parentId == null || parentId <= 0)
			{
				List<WechatMenu> menuParentList = wechatMenuMapper.queryOneLevelMenu();
				if(menuParentList.size()>=3)
				{
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg("一级菜单只能设置3个");
					return result;
				}
			}
			if(!CmsUtils.isNullOrEmpty(parentId))
			{
				List<WechatMenu>secondMenuList=wechatMenuMapper.querySecondMenu(parentId);
				if(secondMenuList.size()>=5)
				{
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg("二级菜单只能设置5个");
					return result;
				}
			}
		}
		
		WechatMenu  wechatMenu = null;
		if(!CmsUtils.isNullOrEmpty(id))
		{
			wechatMenu = wechatMenuMapper.selectByPrimaryKey(id);
			wechatMenu.setMenuName(menuName);
			wechatMenu.setClickAction(clickAction);
			if(clickAction == 2) //2-回复图片
			{
				
				try 
				{
					JSONObject uploadWeiXinImage = WechatUtils.uploadWeiXinImage(queryWechatToken(), replyImage);
					wechatMenu.setMenuKey(String.valueOf(uploadWeiXinImage.get("media_id")));
					wechatMenu.setReplyImage(replyImage);
					wechatMenu.setReplyText("");
					wechatMenu.setClickAction(clickAction);
				} 
				catch (WXException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(clickAction == 0)//0-无触发
			{
				wechatMenu.setClickAction(clickAction);
				wechatMenu.setMenuUrl("");
				wechatMenu.setReplyImage("");
				wechatMenu.setReplyText("");
				wechatMenu.setMenuKey("");
			}
			
			
			if(clickAction == 1)//1-自定义链接
			{
				wechatMenu.setMenuUrl(menuUrl);
				wechatMenu.setClickAction(clickAction);
				wechatMenu.setReplyImage("");
				wechatMenu.setMenuKey("");
				wechatMenu.setReplyText("");
			}
			
			if(clickAction == 3)//3-回复文字
			{
				wechatMenu.setReplyText(replyText);
				wechatMenu.setClickAction(clickAction);
				wechatMenu.setMenuUrl("");
				wechatMenu.setReplyImage("");
				wechatMenu.setMenuKey(menuKey);
			}
			
			wechatMenu.setParentId(parentId);
			wechatMenu.setIndexNumber(indexNumber);
			wechatMenu.setUpdateTime(new Date());
			wechatMenuMapper.updateByPrimaryKeySelective(wechatMenu);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("更新成功");
		}
		else
		{
			
			wechatMenu = new WechatMenu();
			if(parentId==null)
			{
				wechatMenu.setParentId(0);
			}
			else
			{
				wechatMenu.setParentId(parentId);
			}
			wechatMenu.setMenuName(menuName);
			
			if(clickAction == 0)//0-无触发
			{
				wechatMenu.setClickAction(clickAction);
				wechatMenu.setMenuUrl("");
				wechatMenu.setReplyImage("");
				wechatMenu.setReplyText("");
				wechatMenu.setMenuKey("");
			}
			
			
			if(clickAction == 1)//1-自定义链接
			{
				wechatMenu.setMenuUrl(menuUrl);
				wechatMenu.setClickAction(clickAction);
				wechatMenu.setReplyImage("");
				wechatMenu.setMenuKey("");
				wechatMenu.setReplyText("");
			}
			if(clickAction == 2)
			{
				
				try 
				{
					JSONObject uploadWeiXinImage = WechatUtils.uploadWeiXinImage(queryWechatToken(), replyImage);
					System.out.println(uploadWeiXinImage);
				} 
				catch (WXException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(clickAction == 3)//3-回复文字
			{
				wechatMenu.setReplyText(replyText);
				wechatMenu.setClickAction(clickAction);
				wechatMenu.setMenuUrl("");
				wechatMenu.setReplyImage("");
				wechatMenu.setMenuKey(menuKey);
			}
			
			wechatMenu.setIndexNumber(indexNumber);
			wechatMenu.setMenuUrl(menuUrl);
			wechatMenu.setMenuKey(menuKey);
			wechatMenu.setReplyText(replyText);
			wechatMenu.setReplyImage(replyImage);
			wechatMenu.setCreateTime(new Date());
			wechatMenu.setUpdateTime(new Date());
			wechatMenuMapper.insertSelective(wechatMenu);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("添加成功");
		}
		return result;
	}

	@Override
	public int updateCustomerMenu()
	{
		String accessToken = queryWechatToken();//获取token
		List<WechatMenu> parentMenuList = queryOneLevelMenu();
		// 更新微信端
        Map<String,List<Map<String,Object>>> requestMap = new HashMap<String,List<Map<String,Object>>>();
        List<Map<String,Object>> buttonList = new ArrayList<Map<String,Object>>();
        // 组织菜单json
        if(parentMenuList.size() > 3)
        {
        	return 1;
        }
        // 先排序
        Set<WechatMenu> menuItems = new TreeSet<WechatMenu>(new Comparator<WechatMenu>()
        {
            @Override
            public int compare(WechatMenu m1, WechatMenu m2)
            {
            	return CmsUtils.compareIndexNumber(m1.getIndexNumber(), m2.getIndexNumber(), m1.getId(), m2.getId());
            }
        });
        for(WechatMenu menu : parentMenuList)
        {
            Map<String,Object> button = renderMenuMap(menu);
            
            List<WechatMenu> subMenus = wechatMenuMapper.querySecondMenu(menu.getId());
            // 如果是有子菜单
            if(!CmsUtils.isNullOrEmpty(subMenus))
            {
            	if(subMenus.size() > 5)
                {
                	return 7;
                }
                menuItems.clear();
                menuItems.addAll(subMenus);
                
                List<Map<String,Object>> subMenuList = new ArrayList<Map<String,Object>>();
                for(WechatMenu subMenu : menuItems)
                {
                    subMenuList.add(renderMenuMap(subMenu));
                }
                button.put("sub_button", subMenuList);
            }
            buttonList.add(button);
        }
        requestMap.put("button", buttonList);
        try {
			WechatUtils.saveOrUpdateCustomerMenu(accessToken, String.valueOf(JsonUtil.map2jsonToObject(requestMap)));
		} catch (WXException e) {
			e.printStackTrace();
			return 5;
		}
        return 0;
	}
	private Map<String,Object> renderMenuMap(WechatMenu menu)
	{
	    Map<String,Object> button = new HashMap<String,Object>();
        button.put("name", menu.getMenuName());
        // 哪种触发方式
        if(menu.getClickAction() == Constants.MSG_CLICK_ACTION_LINK)//自定义链接
        {
            button.put("type", "view");
            String url = menu.getMenuUrl();
            button.put("url", url);
        }
        else if(menu.getClickAction() == Constants.MSG_CLICK_ACTION_SEND_WEIXIN_IMAGES_TEXT)//图片
        {
        	button.put("type", "media_id");
        	button.put("media_id",menu.getMenuKey());
        }
        else
        {
            button.put("type", "click");
            button.put("key", menu.getMenuKey());
        }
        return button;
	}
	@Override
	public WechatMenu queryWechatMenuByKey(String key) {
		// TODO Auto-generated method stub
		return wechatMenuMapper.queryWechatMenuByKey(key);
	}
	
	@Override
	public int addWechatSendMessage(WechatSendMessage message) {
		// TODO Auto-generated method stub
		return wechatSendMessageMapper.insertSelective(message);
	}
	
	@Override
	public WechatSendMessage queryWechatSendMessageById(int logId) {
		// TODO Auto-generated method stub
		return wechatSendMessageMapper.selectByPrimaryKey(logId);
	}
	
	@Override
	public int updateWechatSendMessage(WechatSendMessage message) {
		// TODO Auto-generated method stub
		return wechatSendMessageMapper.updateByPrimaryKeySelective(message);
	}
	@Override
	public List<WechatSendMessage> queryWechatSendMessageList(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return wechatSendMessageMapper.queryWechatSendMessageList(requestVo);
	}
	@Override
	public int deleteMessageByWechatId(int wechatId) {
		// TODO Auto-generated method stub
		return wechatSendMessageMapper.deleteByPrimaryKey(wechatId);
	}
	@Override
	public int addSendMessageUser(SendMessageUser user) {
		// TODO Auto-generated method stub
		return sendMessageUserMapper.insertSelective(user);
	}
	@Override
	public int addSendMessageCategory(SendMessageCategory category) {
		// TODO Auto-generated method stub
		return sendMessageCategoryMapper.insertSelective(category);
	}
	@Override
	public List<SendMessageUser> querySendMessageUserList(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return sendMessageUserMapper.querySendMessageUserList(requestVo);
	}
	@Override
	public List<KeyWords> findKeyWordsList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return keyWordsMapper.findKeyWordsList(requestVo);
	}
	@Override
	public KeyWords findKeyWordsById(Integer keyWordsId) {
		// TODO Auto-generated method stub
		return keyWordsMapper.selectByPrimaryKey(keyWordsId);
	}
	@Override
	public int updateKeyWords(KeyWords keyWords) {
		if(keyWords.getId() != null && keyWords.getId() > 0)
		{
			keyWordsMapper.updateByPrimaryKeySelective(keyWords);
		}
		else
		{
			keyWordsMapper.insertSelective(keyWords);
		}
		return 0;
	}
	@Override
	public int deleteKeywords(Integer keywordsId) {
		// TODO Auto-generated method stub
		return keyWordsMapper.updateKeywordsFlagById(keywordsId);
	}
	@Override
	public List<KeyWords> findKeyWordsByKeyWords(String keyWords) {
		// TODO Auto-generated method stub
		return keyWordsMapper.findKeyWordsByKeyWords(keyWords);
	}
}
