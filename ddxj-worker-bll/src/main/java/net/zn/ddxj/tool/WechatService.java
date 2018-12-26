package net.zn.ddxj.tool;

import java.util.List;

import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.entity.KeyWords;
import net.zn.ddxj.entity.SendMessageCategory;
import net.zn.ddxj.entity.SendMessageUser;
import net.zn.ddxj.entity.WechatMenu;
import net.zn.ddxj.entity.WechatSendMessage;
import net.zn.ddxj.entity.WechatSync;
import net.zn.ddxj.entity.WechatUser;
import net.zn.ddxj.entity.WechatUserTag;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface WechatService {
	String queryWechatToken();
	List<String> queryWechatFollowerIds();
	WechatUserTag queryWechatUserTag(Integer tagId);
	int addWechatUserTagBetw(String openId,Integer tagId);
	void addWechatUser(List<WechatUser> wechatUser);
	void initWechatFollow();
	int queryWechatSyncById(String wechatId);//根据素材id查询是否有同步记录
	int addWechatSync(WechatSync sync);
	List<WechatMenu>queryAllWeChatMenu( );
	WechatMenu queryWechatMenuByKey(String key);
	int deleteMenuByMenuId(Integer menuId);
	List<WechatMenu> queryOneLevelMenu( );
	WechatMenu queryMenuByMenuId(Integer menuId);
	ResponseBase updateWeChatMenu(Integer id, String menuName, Integer clickAction, Integer indexNumber, String menuUrl,String menuKey, Integer parentId, String replyText, String replyImage);
	int updateCustomerMenu();
	int addWechatSendMessage(WechatSendMessage message);//添加群发记录
	WechatSendMessage queryWechatSendMessageById(int logId);//根据群发ID查询群发记录
	int updateWechatSendMessage(WechatSendMessage message);//修改群发记录
	List<WechatSendMessage> queryWechatSendMessageList(RequestVo requestVo);//多条件查询群发记录
	int deleteMessageByWechatId(int wechatId);//删除群发记录
	int addSendMessageUser(SendMessageUser user);//添加群发用户
	int addSendMessageCategory(SendMessageCategory category);//添加群发分类
	List<SendMessageUser> querySendMessageUserList(RequestVo requestVo);//多条件查询群发用户列表
	List<KeyWords> findKeyWordsList(CmsRequestVo requestVo);
	List<KeyWords> findKeyWordsByKeyWords(String keyWords);
	KeyWords findKeyWordsById(Integer keyWordsId);
	int updateKeyWords(KeyWords keyWords);
	int deleteKeywords(Integer keywordsId);
}
